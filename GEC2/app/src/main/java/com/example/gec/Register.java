package com.example.gec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class Register extends AppCompatActivity {
    EditText email;
    EditText password;
    ProgressBar progress;
    FirebaseAuth firebase;
    Button reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.ed);
        password = findViewById(R.id.editTextTextPassword2);
        firebase = FirebaseAuth.getInstance();
        progress = findViewById(R.id.progressBar);
        reg = findViewById(R.id.button2);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String em = email.getText().toString();
                final String pass = password.getText().toString();
                if(email.length()==0)
                {
                    email.setError("this field can't be empty");
                    email.setText("");
                    return;
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(em).matches())
                {
                    email.setError("Invalid email address");
                    return;
                }
                if(password.length()==0)
                {
                    password.setError("this field can't be empty");
                    password.setText("");
                    return;
                }
                if(password.length()<8)
                {
                    password.setError("password length must be equal to or greater than 8");
                    password.setText("");
                    return;
                }
                progress.setVisibility(View.VISIBLE);
                firebase.fetchSignInMethodsForEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if(!task.getResult().getSignInMethods().isEmpty())
                            {
                                Toast.makeText(getApplicationContext(),"User with Email id already exists please login",Toast.LENGTH_SHORT).show();
                                email.setText("");
                                password.setText("");
                                finish();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            }
                            else {
                                firebase.createUserWithEmailAndPassword(em, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Register.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                            email.setText("");
                                            password.setText("");
                                            progress.setVisibility(View.INVISIBLE);
                                            finish();
                                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                                            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        } else {
                                            Toast.makeText(Register.this, "invalid email id", Toast.LENGTH_SHORT).show();
                                            progress.setVisibility(View.INVISIBLE);
                                            email.setText("");
                                            password.setText("");
                                        }
                                    }
                                });
                            }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}