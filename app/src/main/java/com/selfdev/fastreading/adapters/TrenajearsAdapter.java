package com.selfdev.fastreading.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.selfdev.fastreading.R;
import com.selfdev.fastreading.TrenajearActivity;

import java.util.ArrayList;
import java.util.List;

public class TrenajearsAdapter  extends RecyclerView.Adapter<TrenajearsAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final ArrayList<String> states;
    private final Context ctx;

    public TrenajearsAdapter(Context context, ArrayList<String> states) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
        this.ctx = context;
    }
    @Override
    public TrenajearsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.trenajears_item, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, TrenajearActivity.class);
                //1-таблица Шульте
                //2-поле зрения
                //3-клиновидные таблиц
                //4-Преграды(наложение решетки, нехватка букв, поворот на 90)
                intent.putExtra("TrenType", 1);
                ctx.startActivity(intent);
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrenajearsAdapter.ViewHolder holder, int position) {
        holder.nameView.setText(states.get(position));
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView;
        ViewHolder(View view){
            super(view);
            nameView = view.findViewById(R.id.trenName);
        }
    }
}