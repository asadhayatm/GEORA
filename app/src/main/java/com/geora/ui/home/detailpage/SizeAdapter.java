package com.geora.ui.home.detailpage;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geora.R;
import com.geora.listeners.RecycleListener;
import com.geora.model.sale.GetSize;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.ViewHolder> {

    private Context context;
    private RecycleListener clicklistener = null;
    public List<GetSize> list;

    public SizeAdapter(List<GetSize> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public SizeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_size, parent, false);
        return new SizeAdapter.ViewHolder(view);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull SizeAdapter.ViewHolder holder, int position) {
        holder.tvSize.setText(list.get(position).getSize());
        if (list.get(position).isSelected()) {
            holder.tvSize.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.tvSize.setBackground(context.getResources().getDrawable(R.drawable.yellow_border_rect));
        } else {
            holder.tvSize.setTextColor(context.getResources().getColor(R.color.warm_grey));
            holder.tvSize.setBackground(context.getResources().getDrawable(R.drawable.warm_gray_border_round_rect_));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_size)
        TextView tvSize;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvSize.setOnClickListener(this);
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
