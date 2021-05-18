package com.example.gec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class add_members extends AppCompatActivity {
    TextView tv;
    String username;
    EditText ev;
    Button add;
    DatabaseReference reference;
    FirebaseDatabase ref;
    String mname;
    addm m;
    String t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);
        final Intent intent = getIntent();
        username = intent.getStringExtra("username");
        ev = findViewById(R.id.editText);
        t = intent.getStringExtra("tripname");
        add = findViewById(R.id.button4);
        tv = findViewById(R.id.textView6);
        tv.setText(intent.getStringExtra("tripname")+" is Onn!!");
        reference = FirebaseDatabase.getInstance().getReference().child(username).child(intent.getStringExtra("tripname"));
        m = new addm();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mname = ev.getText().toString();
                if(mname.equals(""))
                {
                    ev.setText("");
                    ev.setError("this field can't be empty");
                    return;
                }
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String existingmembers = "";
                        for (DataSnapshot sp : snapshot.getChildren()) {
                                addm m1= sp.getValue(addm.class);
                                existingmembers = existingmembers + m1.getMname();
                        }
                        if(existingmembers.contains(mname))
                        {
                            ev.setError("Member already exists");
                            ev.setText("");
                            return;
                        }
                        else
                        {
                            m.setMname(mname);
                            m.setExpense(0);
                            reference.child(m.getMname()).setValue(m);
                            Toast.makeText(getApplicationContext(),"Member Added",Toast.LENGTH_SHORT).show();
                            ev.setText("");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    public void exp(View view)
    {
        Intent intent = new Intent(getApplicationContext(),add_expense.class);
        intent.putExtra("tripname",t);
        intent.putExtra("username",username);
        finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        Intent intent = new Intent(getApplicationContext(),Trip_page.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }
}