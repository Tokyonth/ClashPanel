package com.tokyonth.clashpanel.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokyonth.clashpanel.R;
import com.tokyonth.clashpanel.bean.clash.LogsBean;

import java.util.ArrayList;

public class LogsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<LogsBean> logsBeanArrayList;

    public LogsAdapter(ArrayList<LogsBean> logsBeanArrayList) {
        this.logsBeanArrayList = logsBeanArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_adapter_logs,parent,false);
        return new LogsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LogsBean logsBean = logsBeanArrayList.get(position);
        if (holder instanceof LogsViewHolder) {
            ((LogsViewHolder) holder).time.setText(logsBean.getTime());
            ((LogsViewHolder) holder).type.setText(logsBean.getType());
            ((LogsViewHolder) holder).payload.setText(logsBean.getPayload());
        }
    }

    @Override
    public int getItemCount() {
        return logsBeanArrayList.size();
    }

    static class LogsViewHolder extends RecyclerView.ViewHolder {

        TextView time;
        TextView payload;
        TextView type;

        LogsViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.tv_logs_time);
            payload = itemView.findViewById(R.id.tv_logs_payload);
            type = itemView.findViewById(R.id.tv_logs_type);
        }

    }

}
