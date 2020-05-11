package com.tokyonth.clashpanel.adapter;

import com.tokyonth.clashpanel.R;
import com.tokyonth.clashpanel.bean.SubscriptionBean;
import com.tokyonth.clashpanel.utils.store.SPUtils;
import com.tokyonth.clashpanel.view.BurnRoundView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SubscriptionBean> beanList;
    private OnItemClickListener clickListener;
    private OnItemDelClickListener delClickListener;
    private OnItemSubscriptionSelected itemSubscriptionSelected;
    private boolean isMultiSelectMode = false;
    private int previous = -1;

    public SubscriptionAdapter(List<SubscriptionBean> beanList) {
        this.beanList = beanList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_subscription, parent, false);

        final SubscriptViewHolder holder = new SubscriptViewHolder(view);
        holder.rb_is_subscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSubscriptionSelected.onItemSubscriptClick(v, holder.getAdapterPosition());
                previous = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });

        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (beanList != null) {
            ((SubscriptViewHolder) holder).tv_name.setText(beanList.get(position).getName());
            ((SubscriptViewHolder) holder).tv_url.setText(beanList.get(position).getUrl());
            ((SubscriptViewHolder) holder).roundView.setColor(Color.parseColor(beanList.get(position).getColor()), false);
            ((SubscriptViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(v, position);
                }
            });
            if (isMultiSelectMode) {
                ((SubscriptViewHolder) holder).ll_del.setVisibility(View.VISIBLE);
              //  ((SubscriptViewHolder) holder).rb_is_subscript.setVisibility(View.GONE);
                ((SubscriptViewHolder) holder).ll_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delClickListener.onItemDelClick(v, position);
                    }
                });
            } else {
                ((SubscriptViewHolder) holder).ll_del.setVisibility(View.GONE);
              //  ((SubscriptViewHolder) holder).rb_is_subscript.setVisibility(View.VISIBLE);
            }
            if (position == previous) {
                ((SubscriptViewHolder) holder).rb_is_subscript.setChecked(true);
            } else {
                ((SubscriptViewHolder) holder).rb_is_subscript.setChecked(false);
            }
            if (beanList.get(position).getId() == (int)SPUtils.getData("SelectedId", 0)) {
                ((SubscriptViewHolder) holder).rb_is_subscript.setChecked(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (beanList != null) {
            return beanList.size();
        }
        return 0;
    }

    public void setDelMode(boolean b) {
        this.isMultiSelectMode = b;
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        if (getItemCount() > 0 && position < getItemCount()) {
            beanList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    class SubscriptViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView tv_url;
        private RadioButton rb_is_subscript;
        private CardView cardView;
        private BurnRoundView roundView;
        private LinearLayout ll_del;

        SubscriptViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.subscript_item_title);
            tv_url = itemView.findViewById(R.id.subscript_item_url);
            rb_is_subscript = itemView.findViewById(R.id.rb_is_subscript);
            cardView = itemView.findViewById(R.id.card_subscript);
            roundView = itemView.findViewById(R.id.color_round_view);
            ll_del = itemView.findViewById(R.id.ll_del_subscription_item);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public interface OnItemDelClickListener {
        void onItemDelClick(View view, int position);
    }

    public void setOnItemDelListener(OnItemDelClickListener listener) {
        this.delClickListener = listener;
    }

    public interface OnItemSubscriptionSelected {
        void onItemSubscriptClick(View view, int position);
    }

    public void setOnItemSubscriptSelected(OnItemSubscriptionSelected listener) {
        this.itemSubscriptionSelected = listener;
    }

}
