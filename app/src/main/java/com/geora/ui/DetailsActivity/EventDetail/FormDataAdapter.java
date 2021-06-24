package com.geora.ui.DetailsActivity.EventDetail;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geora.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


class FormDataAdapter extends RecyclerView.Adapter<FormDataAdapter.FormDataViewHolder> {
    private List<FormDataValue> list;

    public FormDataAdapter(List<FormDataValue> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public FormDataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_form_data_layout, viewGroup, false);
        return new FormDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FormDataViewHolder holder, int i) {
        holder.tvKey.setText(list.get(i).getKey());
        holder.tvValue.setText(list.get(i).getValue());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FormDataViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_key)
        TextView tvKey;
        @BindView(R.id.tv_value)
        TextView tvValue;

        FormDataViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
