package com.selfdev.fastreading.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.selfdev.fastreading.Objects.Record;
import com.selfdev.fastreading.R;

import java.util.List;

public class RecordAdapter  extends RecyclerView.Adapter<RecordAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Record> records;

    public RecordAdapter(Context context, List<Record> records) {
        this.records = records;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.record_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecordAdapter.ViewHolder holder, int position) {
        holder.id.setText(String.valueOf(position+1));
        holder.nickName.setText(records.get(position).getNickname());
        String scoreStr = records.get(position).getSpeed()+" слов/минуту\nпонимание: "+records.get(position).getUnderstanding()+"%";
        holder.score.setText(scoreStr);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView id;
        final TextView nickName;
        final TextView score;
        ViewHolder(View view){
            super(view);
            id = view.findViewById(R.id.recordIndex);
            nickName = view.findViewById(R.id.recordNickname);
            score = view.findViewById(R.id.recordNum);
        }
    }
}