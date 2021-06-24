package com.geora.ui.home;

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
import com.geora.model.beaconsdata.Datum;
import com.geora.util.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.geora.util.DateTimeUtils.timeStampToEndCamp;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    private Context context;
    private RecycleListener clicklistener = null;
    public List<Datum> list;

    public CardStackAdapter(List<Datum> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CardStackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new CardStackAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardStackAdapter.ViewHolder holder, int position) {

        String startDate, endDate;
        holder.tvName.setText(list.get(position).getCampTitle());
        ImageUtils.setImage(context, list.get(position).getImages(), holder.ivImage, ContextCompat.getDrawable(context, R.drawable.ic_no_offer_browse_graphic));
        holder.tvAddress.setText(list.get(position).getLocation());
        if (list.get(position).getCampStartDate() == null)
            startDate = "N/A";
        else
            startDate = timeStampToEndCamp(list.get(position).getCampStartDate().toString());
        if (list.get(position).getCampEndDate() == null)
            endDate = "N/A";
        else
            endDate = timeStampToEndCamp(list.get(position).getCampEndDate().toString());
        holder.tvDate.setText(startDate + "-" + endDate);

        if (list.get(position).getProductPrice() == null)
            holder.tvPrice.setText("N/A");
        else
            holder.tvPrice.setText("$" + String.format("%.2f",list.get(position).getProductPrice()));
        if (list.get(position).getDiscountedPrice() == null)
            holder.tvDisPrice.setText("N/A");
        else
            holder.tvDisPrice.setText("$" + String.format("%.2f",list.get(position).getDiscountedPrice()));
        if (list.get(position).getCampType() == AppConstantClass.CAMPTYPE.PROMOTION) {
            if (list.get(position).getAppTitle() == null) {
                holder.tvButton.setVisibility(View.GONE);
            } else {
                holder.tvButton.setVisibility(View.VISIBLE);
                if (list.get(position).getAppTitle().equals(AppConstantClass.PROMOTION_TYPE.DOWNLOAD)) {
                    holder.tvButton.setText(context.getResources().getString(R.string.download));
                } else {
                    holder.tvButton.setText(context.getResources().getString(R.string.details));
                }
            }
            holder.tvDisPrice.setVisibility(View.GONE);
            holder.tvPrice.setVisibility(View.GONE);
        } else if (list.get(position).getCampType() == AppConstantClass.CAMPTYPE.FUNDRAISING) {
            holder.tvDisPrice.setVisibility(View.GONE);
            holder.tvPrice.setVisibility(View.GONE);
            holder.tvButton.setText(context.getResources().getString(R.string.donate));
        } else if (list.get(position).getCampType() == AppConstantClass.CAMPTYPE.SALES) {
            holder.tvDisPrice.setVisibility(View.VISIBLE);
            holder.tvPrice.setVisibility(View.VISIBLE);
            if (list.get(position).getSaleType() != null && list.get(position).getSaleType() == 1)
                holder.tvButton.setText(context.getResources().getString(R.string.buy));
            else
                holder.tvButton.setText(context.getResources().getString(R.string.directions));
        } else if (list.get(position).getCampType() == AppConstantClass.CAMPTYPE.EVENT) {
            holder.tvDisPrice.setVisibility(View.GONE);
            holder.tvPrice.setVisibility(View.GONE);
            holder.tvButton.setText(context.getResources().getString(R.string.book));
        }
        if(list.get(position).getCampType() == AppConstantClass.CAMPTYPE.SALES){
             if (list.get(position).getProductPrice() == null)
                holder.tvPrice.setText("N/A");
            else
                holder.tvPrice.setText("$" + String.format("%.2f",list.get(position).getProductPrice()));
            if (list.get(position).getDiscountedPrice() == null)
                holder.tvDisPrice.setText("");
            else
                holder.tvDisPrice.setText("$" + String.format("%.2f",list.get(position).getDiscountedPrice()));

        }





    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_button)
        TextView tvButton;
        @BindView(R.id.tv_dis_price)
        TextView tvDisPrice;
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.rl_main)
        RelativeLayout rlMain;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            tvButton.setOnClickListener(this);
            rlMain.setOnClickListener(this);
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
