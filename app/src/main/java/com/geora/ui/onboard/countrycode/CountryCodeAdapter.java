package com.geora.ui.onboard.countrycode;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geora.R;
import com.geora.listeners.RecycleListener;
import com.geora.model.CountryCode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CountryCodeAdapter extends RecyclerView.Adapter<CountryCodeAdapter.ViewHolder> {
    private Context context;
    private RecycleListener clicklistener = null;
    public List<CountryCode> list;

    public CountryCodeAdapter(List<CountryCode> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public CountryCodeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvCountryCode.setText(list.get(position).getCountryName() + "(+" + list.get(position).getCountryCode() + ")");

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_country_code)
        TextView tvCountryCode;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
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