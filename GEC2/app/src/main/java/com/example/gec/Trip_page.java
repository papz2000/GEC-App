package com.example.gec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Trip_page extends AppCompatActivity {
    String username;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_page);
        listView = findViewById(R.id.lv);
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listview,R.id.textView12,list);
        listView.setAdapter(adapter);
        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference().child(username).child("Trips");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                   list.clear();
                    for(DataSnapshot sp : snapshot.getChildren())
                    {

                        String txt = sp.getValue().toString();
                        list.add(txt);
                    }
                    adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),list.get(i),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),add_members.class);
                intent.putExtra("tripname",list.get(i));
                intent.putExtra("username",username);
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(),Trip_selection.class);
        intent.putExtra("username",username);
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        startActivity(intent);
    }
}