package com.geora.ui.notification;

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
import com.geora.listeners.RecycleListener;
import com.geora.model.notificationresponse.Datum;
import com.geora.util.DateTimeUtils;
import com.geora.util.ImageUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {


    private final ArrayList<Datum> mNotificationList;
    private final RecycleListener mRecycleListener;
    private final Context mContext;

    public NotificationAdapter(Context mContext, ArrayList<Datum> mNotificationList, RecycleListener mRecycleListener) {
        this.mContext = mContext;
        this.mNotificationList = mNotificationList;
        this.mRecycleListener = mRecycleListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageUtils.setImage(mContext, mNotificationList.get(position).getImage(), holder.ivImage, ContextCompat.getDrawable(mContext, R.drawable.placeholder_medium));
        holder.tvNotification.setText(mNotificationList.get(position).getMessage());
        holder.tvTime.setText(DateTimeUtils.getTimeCount(mNotificationList.get(position).getCreatedAt().toString()));
    }

    @Override
    public int getItemCount() {
        return mNotificationList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_notification)
        TextView tvNotification;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.rl_root_view)
        RelativeLayout rlRootView;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        @OnClick({R.id.rl_root_view})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.rl_root_view:
                    mRecycleListener.onItemClicked(rlRootView, "", getAdapterPosition());
                    break;
            }
        }
    }
}


