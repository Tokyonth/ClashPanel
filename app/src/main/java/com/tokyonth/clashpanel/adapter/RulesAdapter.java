package com.tokyonth.clashpanel.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokyonth.clashpanel.R;
import com.tokyonth.clashpanel.bean.clash.rules.RulesBean;

public class RulesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RulesBean rulesBeans;

    public RulesAdapter() {

    }

    public void setData(RulesBean rulesBeans) {
        this.rulesBeans = rulesBeans;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_adapter_rules,parent,false);
        return new RulesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  RulesViewHolder) {
            ((RulesViewHolder) holder).payload.setText(rulesBeans.getRules().get(position).getPayload());
            ((RulesViewHolder) holder).proxy.setText(rulesBeans.getRules().get(position).getProxy());
            ((RulesViewHolder) holder).type.setText(rulesBeans.getRules().get(position).getType());
        }
    }

    @Override
    public int getItemCount() {
        return rulesBeans == null ? 0 : rulesBeans.getRules().size();
    }

    static class RulesViewHolder extends RecyclerView.ViewHolder {

        TextView proxy;
        TextView payload;
        TextView type;

        RulesViewHolder(View itemView) {
            super(itemView);
            proxy = itemView.findViewById(R.id.tv_rules_proxy);
            payload = itemView.findViewById(R.id.tv_rules_payload);
            type = itemView.findViewById(R.id.tv_rules_type);
        }

    }

}
