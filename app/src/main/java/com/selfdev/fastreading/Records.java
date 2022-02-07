package com.selfdev.fastreading;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selfdev.fastreading.Objects.Record;
import com.selfdev.fastreading.adapters.RecordAdapter;

import java.util.ArrayList;

public class Records extends AppCompatActivity {

    FirebaseDatabase database;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        ctx = this;
        RecyclerView rv = findViewById(R.id.recordsList);

        ArrayList<Record> records = new ArrayList<Record>();

        database = FirebaseDatabase.getInstance("https://fastreading-515a9-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference ref = database.getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = "";
                int speed = 0;
                int understanding = 0;
                for (DataSnapshot child:snapshot.getChildren()
                     ) {
                        name = (String)(child.child("login").getValue());
                        speed = (int)((long)(child.child("recordSpeed").getValue()));
                        understanding = (int)((long)(child.child("recordUnderstanding").getValue()));
                        records.add(new Record(name, speed, understanding));
                }
                RecordAdapter adapter = new RecordAdapter(ctx, records);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onBackClick(View view)
    {
        this.finish();
    }
}