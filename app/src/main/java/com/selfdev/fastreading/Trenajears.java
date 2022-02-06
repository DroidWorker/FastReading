package com.selfdev.fastreading;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.selfdev.fastreading.adapters.TrenajearsAdapter;

import java.util.ArrayList;
import java.util.List;

public class Trenajears extends AppCompatActivity {
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trenajears);
        ctx = this;

        RecyclerView rvList = findViewById(R.id.trenList);
        ArrayList<String> trenagers = new ArrayList<String>();
        trenagers.add("таблица Шульте");
        trenagers.add("поле зрения");
        trenagers.add("клиновидные таблицы");
        trenagers.add("Преграды");
        TrenajearsAdapter adapter = new TrenajearsAdapter(this, trenagers);
        rvList.setAdapter(adapter);
    }

    public void onBackClick(View view)
    {
        this.finish();
    }
}
