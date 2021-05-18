package com.example.gec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class Final extends AppCompatActivity {
    String report;
    DatabaseReference reference;
    String username;
    int count=0;
    int texp=0;
    ListView lv1;
    String trip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        TextView tv = findViewById(R.id.textView10);
        tv.setPaintFlags(tv.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        report="";
        trip = intent.getStringExtra("tripname");
        lv1 = findViewById(R.id.lv1);
        final ArrayList<String> list1 = new ArrayList<>();
        final ArrayAdapter adapter1 = new ArrayAdapter<String>(this,R.layout.listview2,R.id.textView13,list1);
        lv1.setAdapter(adapter1);
        reference = FirebaseDatabase.getInstance().getReference().child(username).child(trip);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1.clear();
                report="";
                report=report+ "Trip Name -> " + trip + "\n";
                report = report + "\n";
                for(DataSnapshot sp : snapshot.getChildren())
                {
                    count++;
                    addm info = sp.getValue(addm.class);
                    String txt = info.getMname();
                    int exp = info.getExpense();
                    texp=texp+exp;
                    list1.add("Name                       "+txt);
                    list1.add("Amount to be paid  "+exp);
                    report = report +"Name                       "+txt+"\n";
                    report = report + "Amount to be paid  "+exp + "\n";
                    report = report + "\n";

                    list1.add(" ");
                }
                adapter1.notifyDataSetChanged();
                list1.add("Total Expense of this Trip was \n ->"+texp);
                String avg = String.format("%.2f",(float)texp/count);
                list1.add("Average expense per member was \n ->"+avg+"\n");
                list1.add("Hope you all had a Great Time and till next time Tata!!");
                report = report + "Total Expense of this Trip was \n ->"+texp+"\n";
                report = report + "\n";
                report = report +"Average expense per member was \n ->"+avg+"\n";
                report = report + "\n";
                report = report + "Hope you all had a Great Time and till next time Tata!!";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
    public void exit(View view)
    {
        reference.removeValue();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(username).child("Trips");
        ref.child(trip).removeValue();
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
    public  void share(View view)
    {
        Intent shareing = new Intent(Intent.ACTION_SEND);
        shareing.setType("text/plain");
        shareing.putExtra(Intent.EXTRA_TEXT,report);
        reference.removeValue();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(username).child("Trips");
        ref.child(trip).removeValue();
        finish();
        startActivity(Intent.createChooser(shareing,"Share Using"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),add_expense.class);
        intent.putExtra("tripname",trip);
        intent.putExtra("username",username);
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        startActivity(intent);
    }
}