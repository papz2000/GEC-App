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

public class enter_exp extends AppCompatActivity {
    String t;
    int count=0;
    addm m;
    String mname;
    String username;
   // int exp;
    TextView tv;
    Button btn;
    Button btn1;
    EditText ev;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        count = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_exp);
        ev = findViewById(R.id.editTextNumber);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        tv = findViewById(R.id.textView9);
        t = intent.getStringExtra("tripname");
        mname = intent.getStringExtra("mname");
        tv.setText("Add or remove expense for \n"+mname);
        btn = findViewById(R.id.button6);
        btn1 = findViewById(R.id.button14);
        reference = FirebaseDatabase.getInstance().getReference().child(username).child(intent.getStringExtra("tripname"));
        m = new addm();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ev.getText().toString().equals(""))
                {
                    ev.setError("this field can't be empty");
                    return;
                }
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot sp : snapshot.getChildren()) {
                            {
                                m = sp.getValue(addm.class);
                                if (m.getMname().equals(mname) && count == 0) {
                                    m.setExpense(m.getExpense() + Integer.parseInt(ev.getText().toString()));
                                    m.setMname(mname);
                                    reference.child(mname).setValue(m);
                                    count = 1;
                                    Intent intent = new Intent(getApplicationContext(), add_expense.class);
                                    intent.putExtra("tripname", t);
                                    intent.putExtra("username",username);
                                    Toast.makeText(getApplicationContext(),"Expense added",Toast.LENGTH_SHORT).show();
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ev.getText().toString().equals(""))
                {
                    ev.setError("this field can't be empty");
                    return;
                }
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot sp : snapshot.getChildren()) {
                            {
                                m = sp.getValue(addm.class);
                                if (m.getMname().equals(mname) && count == 0) {
                                        m.setExpense(m.getExpense() - Integer.parseInt(ev.getText().toString()));
                                        m.setMname(mname);
                                        reference.child(mname).setValue(m);
                                        count = 1;
                                        Intent intent = new Intent(getApplicationContext(), add_expense.class);
                                        intent.putExtra("tripname", t);
                                        intent.putExtra("username",username);
                                        Toast.makeText(getApplicationContext(), "Expense removed", Toast.LENGTH_SHORT).show();
                                        finish();
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                        startActivity(intent);
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(getApplicationContext(),add_expense.class);
        intent.putExtra("tripname",t);
        intent.putExtra("username",username);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        startActivity(intent);
    }
}