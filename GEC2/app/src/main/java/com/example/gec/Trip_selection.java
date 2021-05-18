package com.example.gec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Trip_selection extends AppCompatActivity {
    DatabaseReference reference;
    String username;
    DatabaseReference reference2;
    EditText ev;
    String existingtrips;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_selection);
        Intent intent = getIntent();
        String username1 = intent.getStringExtra("username");
        String username2 = username1.replace("@","").toLowerCase();
        username = username2.replace(".","");
        btn = findViewById(R.id.button8);
        ev = findViewById(R.id.editText3);
        reference = FirebaseDatabase.getInstance().getReference().child(username).child("Trips");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String tripname = ev.getText().toString();
                if (tripname.equals("")) {
                    ev.setError("this field can't be empty");
                    return;
                }
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        existingtrips = "";
                        for (DataSnapshot sp : snapshot.getChildren()) {
                            existingtrips = existingtrips + sp.getValue().toString() + " ";
                        }
                        if (existingtrips.contains(tripname)) {
                            ev.setError("this trip already exists");
                            ev.setText("");
                            return;
                        } else {
                            reference.child(tripname).setValue(tripname);
                            ev.setText("");
                            Toast.makeText(getApplicationContext(), "successfully added trip", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), add_members.class);
                            intent.putExtra("tripname", tripname);
                            intent.putExtra("username",username);
                            finish();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    public void skip(View view)
    {
        Intent intent = new Intent(getApplicationContext(),Trip_page.class);
        intent.putExtra("username",username);
        finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to sign out?").setCancelable(false).setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                Toast.makeText(getApplicationContext(),"User Signed out",Toast.LENGTH_SHORT).show();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        return;
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}