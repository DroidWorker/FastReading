package com.selfdev.fastreading.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.selfdev.fastreading.Objects.Info;
import com.selfdev.fastreading.Objects.Record;
import com.selfdev.fastreading.R;

import java.util.List;

public class InfoAdapter  extends RecyclerView.Adapter<InfoAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Info> infos;

    public InfoAdapter(Context context, List<Info> infos) {
        this.infos = infos;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public InfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.info_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InfoAdapter.ViewHolder holder, int position) {
        holder.Vheader.setText(infos.get(position).getHeader());
        holder.Vtext.setText(infos.get(position).getText());
        holder.Vheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.Vtext.getVisibility()==View.GONE)
                    holder.Vtext.setVisibility(View.VISIBLE);
                else
                    holder.Vtext.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView Vheader;
        final TextView Vtext;
        ViewHolder(View view){
            super(view);
            Vheader = view.findViewById(R.id.infoHeader);
            Vtext = view.findViewById(R.id.infoText);
        }
    }
}