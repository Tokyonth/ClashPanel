package com.tokyonth.clashpanel.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tokyonth.clashpanel.R;
import com.tokyonth.clashpanel.bean.SettingsItemBean;
import com.tokyonth.clashpanel.view.BurnRoundView;

import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SettingsItemBean> list;
    private OnItemClick onItemCommonClick;
    private Activity activity;

    public SettingsAdapter(Activity activity, List<SettingsItemBean> list) {
        this.activity = activity;
        this.list = list;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemCommonClick = onItemClick;
    }

    public interface OnItemClick {
        void onCommonClick(View view, int pos);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_settings,parent,false);
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, "cover");
        return new CommonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        SettingsItemBean bean = list.get(position);
        if (holder instanceof CommonViewHolder) {
            ((CommonViewHolder) holder).title.setText(bean.getTitle());
            ((CommonViewHolder) holder).icon.setBurnSrc(bean.getIcon(), Color.parseColor(bean.getColor()));
            if (list.get(position).getSub() == null) {
                ((CommonViewHolder) holder).sub.setVisibility(View.GONE);
            } else {
                ((CommonViewHolder) holder).sub.setText(bean.getSub());
            }
            ((CommonViewHolder) holder).cardView.setOnClickListener(v -> onItemCommonClick.onCommonClick(v, position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    static class CommonViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView sub;
        private CardView cardView;
        private BurnRoundView icon;

        CommonViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.settings_item_title);
            sub = itemView.findViewById(R.id.settings_item_sub);
            cardView = itemView.findViewById(R.id.common_content_card);
            icon = itemView.findViewById(R.id.brv_settings_item);
        }

    }

}
