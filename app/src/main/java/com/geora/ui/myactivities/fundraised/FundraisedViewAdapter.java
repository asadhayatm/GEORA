package com.geora.ui.myactivities.fundraised;

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
import com.geora.model.myactivity.FundData;
import com.geora.util.DateTimeUtils;
import com.geora.util.ImageUtils;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FundraisedViewAdapter extends RecyclerView.Adapter<FundraisedViewAdapter.FundraisedViewHolder> {
    private List<FundData> list;
    private Context mContext;
    private RecycleListener clicklistener = null;

    public FundraisedViewAdapter(List<FundData> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public FundraisedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_new_placeholder, parent, false);
        mContext = parent.getContext();
        return new FundraisedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FundraisedViewHolder holder, int i) {
        holder.tvPrice.setVisibility(View.VISIBLE);
        holder.tvAmount.setVisibility(View.VISIBLE);
        holder.ivCross.setVisibility(View.GONE);


        holder.tvName.setText(list.get(i).getGetFund().getGetCampaign().getCampTitle());
        holder.tvAddress.setText(list.get(i).getGetFund().getGetCampaign().getLocation());
        holder.tvPrice.setText(mContext.getResources().getString(R.string.doller)+String.format(Locale.US, "%.2f", list.get(i).getAmount()));
        ImageUtils.setImage(mContext, list.get(i).getGetFund().getGetCampaign().getGetDefaultImage().getImageUrl(), holder.ivPlaceholder, mContext.getResources().getDrawable(R.drawable.placeholder_medium));
        holder.tvDirection.setText(mContext.getResources().getString(R.string.donate));
        holder.tvDirection.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        holder.tvDateOfEvent.setText("End on : " + DateTimeUtils.timeStampToEndCamp(list.get(i).getGetFund().getGetCampaign().getCampEndDate().toString()));
//        holder.tvDateOfEvent.setText(DateTimeUtils.timeStampToEndCamp(list.get(i).getPaymentDate().toString()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class FundraisedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

        Unbinder unbinder;
        FundraisedViewHolder(View itemView) {
            super(itemView);
            unbinder=ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clicklistener != null) {
                clicklistener.onItemClicked(v, "data", getAdapterPosition());
            }
        }
    }
    public void setClickListener(RecycleListener clicklistener) {
        this.clicklistener = clicklistener;
    }
}
