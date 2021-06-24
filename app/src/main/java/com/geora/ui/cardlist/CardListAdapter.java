package com.geora.ui.cardlist;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geora.R;
import com.geora.listeners.RecycleListener;
import com.geora.model.cardlist.Datum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {
    private Context context;
    private RecycleListener clicklistener = null;
    public List<Datum> list;
    public int defaultCardPos = -1;

    public CardListAdapter(List<Datum> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public CardListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (list.get(position).getDefaultSource()) {
            defaultCardPos = position;
            holder.tvSetAsDefault.setVisibility(View.VISIBLE);
            holder.ivSelected.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_radio_btn_selected));
        } else {
            holder.tvSetAsDefault.setVisibility(View.GONE);
            holder.ivSelected.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_radio_btn_unselected));
        }
        holder.tvCardNo.setText("****   ****   ****   " + list.get(position).getLast4());
        holder.tvExpDate.setText(list.get(position).getExpMonth() + "/" + list.get(position).getExpYear());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_set_as_default)
        TextView tvSetAsDefault;
        @BindView(R.id.iv_selected)
        ImageView ivSelected;
        @BindView(R.id.tv_card_no)
        TextView tvCardNo;
        @BindView(R.id.tv_exp_date)
        TextView tvExpDate;
        @BindView(R.id.iv_more)
        ImageView ivMore;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ivMore.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clicklistener != null) {
                clicklistener.onItemClicked(view, "", getAdapterPosition());

            }
        }

    }

    public void setClickListener(RecycleListener clicklistener) {
        this.clicklistener = clicklistener;
    }

}