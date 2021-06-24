package com.geora.ui.myactivities.orders;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geora.R;
import com.geora.listeners.RecycleListener;
import com.geora.model.myactivity.SalesData;
import com.geora.util.DateTimeUtils;
import com.geora.util.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrdersViewAdapter extends RecyclerView.Adapter<OrdersViewAdapter.OrdersViewHolder> {
    private List<SalesData> list;
    private Context mContext;
    private RecycleListener clicklistener = null;


    public OrdersViewAdapter(List<SalesData> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_new_placeholder, parent, false);
        mContext = parent.getContext();
        return new OrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int i) {
        holder.tvPrice.setVisibility(View.VISIBLE);
        holder.tvAmount.setVisibility(View.GONE);
        holder.ivCross.setVisibility(View.GONE);
        holder.tvDirection.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        holder.tvAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        ImageUtils.setImage(mContext, list.get(i).getGetSales().getFindCampaign().getGetDefaultImage().getImageUrl(), holder.ivPlaceholder, mContext.getResources().getDrawable(R.drawable.placeholder_medium));
        holder.tvName.setText(list.get(i).getGetSales().getFindCampaign().getCampTitle());
        holder.tvPrice.setText(mContext.getResources().getString(R.string.doller) + String.format("%.2f", Double.valueOf(list.get(i).getAmount())));
        holder.tvDirection.setText(mContext.getResources().getString(R.string.view_details));
//        holder.tvDateOfEvent.setText(DateTimeUtils.timeStampToString(list.get(i).getGetSales().getFindCampaign().getCampEndDate().toString()));
        holder.tvDateOfEvent.setText(DateTimeUtils.timeStampToString(list.get(i).getPaymentDate().toString()));

//        holder.tvAddress.setText(list.get(i).getGetSales().toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setClickListener(RecycleListener clicklistener) {
        this.clicklistener = clicklistener;
    }

    public class OrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_placeholder)
        ImageView ivPlaceholder;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_date_of_event)
        TextView tvDateOfEvent;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_direction)
        TextView tvDirection;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.iv_cross)
        ImageView ivCross;
        @BindView(R.id.tv_amount)
        TextView tvAmount;


        OrdersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clicklistener != null) {
                clicklistener.onItemClicked(v, "data", getAdapterPosition());
            }
        }
    }
}

