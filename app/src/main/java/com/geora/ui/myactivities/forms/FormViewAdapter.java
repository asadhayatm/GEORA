package com.geora.ui.myactivities.forms;

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
import com.geora.model.myactivity.EventData;
import com.geora.util.DateTimeUtils;
import com.geora.util.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FormViewAdapter extends RecyclerView.Adapter<FormViewAdapter.FormViewHolder> {
    private List<EventData> list;
    private Context mContext;
    private RecycleListener clicklistener = null;

    public FormViewAdapter(List<EventData> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public FormViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_new_placeholder, parent, false);
        mContext = parent.getContext();
        return new FormViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FormViewHolder holder, int i) {
        holder.tvPrice.setVisibility(View.GONE);
        holder.tvAmount.setVisibility(View.GONE);
        holder.ivCross.setVisibility(View.GONE);

        holder.tvName.setText(list.get(i).getGetEvent().getGetCampaign().getCampTitle());
        holder.tvAddress.setText(list.get(i).getGetEvent().getGetCampaign().getCampAddress());
        holder.tvDirection.setText(mContext.getResources().getString(R.string.view_form));
        ImageUtils.setImage(mContext, list.get(i).getGetEvent().getGetCampaign().getGetDefaultImage().getImageUrl(), holder.ivPlaceholder, mContext.getResources().getDrawable(R.drawable.placeholder_medium));
        holder.tvDirection.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        holder.tvDateOfEvent.setText(DateTimeUtils.timeStampToEndCamp((list.get(i).getSubscriptionDate().toString())));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setClickListener(RecycleListener clicklistener) {
        this.clicklistener = clicklistener;
    }

    public class FormViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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


        FormViewHolder(View itemView) {
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
