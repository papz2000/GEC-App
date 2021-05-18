package com.example.gec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class MainActivity extends AppCompatActivity {
    long backpressedtime;
    EditText ev1;
    EditText ev2;
    FirebaseAuth firebase;
    ProgressBar pv;
    TextView tv;
    Button lgn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textView17);
        tv.setPaintFlags(tv.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        ev1 = findViewById(R.id.editText2);
        ev2 = findViewById(R.id.editTextTextPassword);
        lgn = findViewById(R.id.button);
        firebase = FirebaseAuth.getInstance();
        pv = findViewById(R.id.progressBar2);

        lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em = ev1.getText().toString();
                if(em.equals(""))
                {
                    ev1.setError("this field can't be empty");
                    return;
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(em).matches())
                {
                    ev1.setError("Invalid email address");
                    return;
                }
                String pass = ev2.getText().toString();
                if(pass.equals(""))
                {
                    ev2.setError("this field can't be empty");
                    return;
                }
                if(pass.length()<8)
                {
                    ev2.setError("password contains at least 8 characters");
                    return;
                }
                pv.setVisibility(View.VISIBLE);
                firebase.signInWithEmailAndPassword(em,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            pv.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, "Successfully logged-in", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Trip_selection.class);
                            intent.putExtra("username",ev1.getText().toString().toLowerCase());
                            ev1.setText("");
                            ev2.setText("");
                            finish();
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                            startActivity(intent);
                        }
                        else
                        {
                            pv.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                            ev1.setText("");
                            ev2.setText("");
                        }
                    }
                });
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String em = ev1.getText().toString();
                if(em.equals(""))
                {
                    ev1.setError("this field can't be empty");
                    return;
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(em).matches())
                {
                    ev1.setError("Invalid email address");
                    return;
                }
                firebase.fetchSignInMethodsForEmail(em).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if(task.getResult().getSignInMethods().isEmpty())
                        {
                            Toast.makeText(getApplicationContext(),"User with email id provided does not exists",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            firebase.sendPasswordResetEmail(em);
                            ev2.setText("");
                            Toast.makeText(getApplicationContext(),"Password reset mail sent to your email id",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed() {
        if(backpressedtime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        backpressedtime = System.currentTimeMillis();

    }
    public void register(View view)
    {
        Intent intent = new Intent(getApplicationContext(),Register.class);
        finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        startActivity(intent);
    }
}