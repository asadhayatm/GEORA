package com.geora.ui.home.homelistview;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geora.R;
import com.geora.data.constants.AppConstantClass;
import com.geora.listeners.RecycleListener;
import com.geora.model.beaconsavedlist.Datum;
import com.geora.util.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.geora.util.DateTimeUtils.timeStampToEndCamp;
import static com.geora.util.DateTimeUtils.timeStampToStartCamp;

public class HomeListViewAdapter extends RecyclerView.Adapter<HomeListViewAdapter.ViewHolder> {
    public List<Datum> list;
    private Context context;
    private int fragmentId;
    private RecycleListener clicklistener = null;

    public HomeListViewAdapter(List<Datum> list, Context context, int fragmentId) {
        this.fragmentId = fragmentId;
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeListViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_new_placeholder, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull HomeListViewAdapter.ViewHolder holder, int position) {
        try {

            Datum model = list.get(position);
            ImageUtils.setImage(context, model.getImages(), holder.ivPlaceholder, ContextCompat.getDrawable(context, R.drawable.ic_no_offer_browse_graphic));
            holder.ivCross.setVisibility(View.VISIBLE);
            holder.tvAmount.setVisibility(View.GONE);
            holder.tvPrice.setVisibility(View.GONE);
            holder.tvAddress.setText(model.getCampAddress() + "," + model.getLocation());
            holder.tvName.setText(model.getCampTitle());
            holder.tvDateOfEvent.setText(timeStampToStartCamp(model.getCampStartDate().toString()) + "-" + timeStampToEndCamp(model.getCampEndDate().toString()));
            if (model.getCampType() == AppConstantClass.CAMPTYPE.PROMOTION) {
                holder.tvDirection.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_directions, 0, 0, 0);

                holder.tvDirection.setText(context.getResources().getString(R.string.directions));
            } else if (model.getCampType() == AppConstantClass.CAMPTYPE.FUNDRAISING) {
                holder.tvDirection.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                holder.tvDirection.setText(context.getResources().getString(R.string.donate));
            } else if (model.getCampType() == AppConstantClass.CAMPTYPE.SALES) {
                if (model.getSaleType() != null && model.getSaleType() == 1) {
                    holder.tvDirection.setText(context.getResources().getString(R.string.buy));
                    holder.tvDirection.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                } else {
                    holder.tvDirection.setText(context.getResources().getString(R.string.directions));
                    holder.tvDirection.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_directions, 0, 0, 0);

                }
            } else if (model.getCampType() == AppConstantClass.CAMPTYPE.EVENT) {
                holder.tvDirection.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                holder.tvDirection.setText(context.getResources().getString(R.string.book));
            }
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
        @BindView(R.id.main)
        RelativeLayout main;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ivCross.setOnClickListener(this);
            main.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clicklistener != null) {
                clicklistener.onItemClicked(v, String.valueOf(fragmentId), getAdapterPosition());
            }
        }
    }

    public void setClickListener(RecycleListener clicklistener) {
        this.clicklistener = clicklistener;
    }
}





