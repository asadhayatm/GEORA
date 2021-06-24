package com.geora.ui.home.detailpage;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.geora.R;
import com.geora.listeners.RecycleListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FormAdapter extends RecyclerView.Adapter<FormAdapter.ViewHolder> {

    private Context context;
    private RecycleListener clicklistener = null;
    public String[] list;
    private List<HashMap<Integer, EditText>> hashMap = new ArrayList<>();

    public FormAdapter(String[] list, Context context) {
        this.list = list;
        this.context = context;
    }

    public List<HashMap<Integer, EditText>> getHashMap() {
        return hashMap;
    }

    @NonNull
    @Override
    public FormAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_form, parent, false);
        return new FormAdapter.ViewHolder(view);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull FormAdapter.ViewHolder holder, int position) {
        holder.tvData.setHint(list[position]);
        HashMap<Integer, EditText> data = new HashMap<Integer, EditText>();
        data.put(position, holder.tvData);
        hashMap.add(data);
    }

    @Override
    public int getItemCount() {
        return list.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_data)
        EditText tvData;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
