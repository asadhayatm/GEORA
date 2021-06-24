package com.geora.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geora.R;
import com.geora.listeners.RecycleListener;
import com.geora.model.sale.GetColor;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ColorDialogAdapter extends RecyclerView.Adapter<ColorDialogAdapter.ViewHolder> {

    private Context context;
    private RecycleListener clicklistener = null;
    public List<GetColor> list;

    public ColorDialogAdapter(List<GetColor> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public ColorDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false);
        return new ColorDialogAdapter.ViewHolder(view);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull ColorDialogAdapter.ViewHolder holder, int position) {
        holder.civColor.setCardBackgroundColor(android.graphics.Color.parseColor(list.get(position).getColorHexCode()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.civ_color)
        CardView civColor;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            civColor.setOnClickListener(this);
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
