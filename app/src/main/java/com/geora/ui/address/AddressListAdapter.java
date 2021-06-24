package com.geora.ui.address;

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
import com.geora.model.addresslist.Datum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder> {
    private Context context;
    private RecycleListener clicklistener = null;
    public List<Datum> list;
    public int defaultCardPos, screen;

    public AddressListAdapter(List<Datum> list, Context context, int screem) {
        this.list = list;
        this.context = context;
        this.screen = screem;
        defaultCardPos =-1;
    }


    @Override
    public AddressListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (list.get(position).getDeliveryStatus() == 1) {
            defaultCardPos = position;
            if (screen == 1) {
                holder.tvSetAsDefault.setVisibility(View.VISIBLE);
                holder.tvDeliver.setVisibility(View.GONE);
            } else {
                holder.tvSetAsDefault.setVisibility(View.GONE);
                holder.tvDeliver.setVisibility(View.VISIBLE);
            }
            holder.ivSelected.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_radio_btn_selected));
        } else {
            holder.tvSetAsDefault.setVisibility(View.GONE);
            holder.tvDeliver.setVisibility(View.GONE);
            holder.ivSelected.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_radio_btn_unselected));
        }
        holder.tvName.setText(list.get(position).getFullName());
        holder.tvName.setText(list.get(position).getFullName().substring(0, 1).toUpperCase() + list.get(position).getFullName().substring(1));

        if (list.get(position).getMobileNo() != null || list.get(position).getMobileNo().isEmpty()) {
            holder.tvMobile.setText(list.get(position).getMobileNo());
        } else {
            holder.tvMobile.setVisibility(View.GONE);
        }
        if (list.get(position).getAddressType() == null || list.get(position).getAddressType().isEmpty()) {
            holder.tvCategory.setVisibility(View.GONE);
        } else {
            holder.tvCategory.setText(list.get(position).getAddressType().substring(0, 1).toUpperCase() + list.get(position).getAddressType().substring(1));
        }
        holder.tvLocation.setText(list.get(position).getFormattedAddress());

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
        @BindView(R.id.iv_more)
        ImageView ivMore;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_category)
        TextView tvCategory;
        @BindView(R.id.tv_location)
        TextView tvLocation;
        @BindView(R.id.tv_mobile)
        TextView tvMobile;
        @BindView(R.id.tv_deliver)
        TextView tvDeliver;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ivMore.setOnClickListener(this);
            tvDeliver.setOnClickListener(this);
            itemView.setOnClickListener(this);
            ivSelected.setOnClickListener(this);
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