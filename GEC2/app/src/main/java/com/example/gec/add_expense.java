package com.example.gec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class add_expense extends AppCompatActivity {
    int expense;
    ListView listView;
    String t;
    String username;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        t = intent.getStringExtra("tripname");
        tv = findViewById(R.id.textView7);
        tv.setText(t + " is onn!!");
        listView = findViewById(R.id.listv);
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listview,R.id.textView12,list);
        listView.setAdapter(adapter);
        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference().child(username).child(t);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot sp : snapshot.getChildren())
                {
                    addm m = sp.getValue(addm.class);
                    String txt = m.getMname();
                    expense = m.getExpense();
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
                Intent intent = new Intent(getApplicationContext(),enter_exp.class);
                intent.putExtra("mname",list.get(i));
                intent.putExtra("tripname",t);
                intent.putExtra("username",username);
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                startActivity(intent);
            }
        });
    }
    public void end(View view)
    {
        Intent intent = new Intent(getApplicationContext(),Final.class);
        intent.putExtra("tripname",t);
        intent.putExtra("username",username);
        finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        startActivity(intent);
    }
    public void exit(View view)
    {
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),add_members.class);
        intent.putExtra("tripname",t);
        intent.putExtra("username",username);
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        startActivity(intent);

    }
}