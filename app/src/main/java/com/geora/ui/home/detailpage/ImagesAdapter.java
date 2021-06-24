package com.geora.ui.home.detailpage;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.geora.R;
import com.geora.listeners.RecycleListener;
import com.geora.model.sale.GetImage;
import com.geora.util.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    private Context context;
    private RecycleListener clicklistener = null;
    public List<GetImage> list;

    public ImagesAdapter(List<GetImage> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ImagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImagesAdapter.ViewHolder(view);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull ImagesAdapter.ViewHolder holder, int position) {
        ImageUtils.setImage(context, list.get(position).getImageUrl(), holder.image, null);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image)
        ImageView image;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            image.setOnClickListener(this);
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
