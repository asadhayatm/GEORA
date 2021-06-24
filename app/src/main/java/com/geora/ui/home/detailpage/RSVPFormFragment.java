package com.geora.ui.home.detailpage;

import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseFragment;
import com.geora.socket.SocketConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.geora.GeoraClass.getContext;

public class RSVPFormFragment extends BaseFragment {
    @BindView(R.id.main)
    RelativeLayout main;
    @BindView(R.id.rl_form)
    RecyclerView rvForm;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    private Context mContext;
    private String formData = "";
    private String[] dataList;
    private LinearLayout.LayoutParams lparams;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rsvpform, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getContext();
        ((DetailsActivity) mContext).setTitle("RSVP Form");
        formData = getArguments().getString(SocketConstant.SOCKEYKEYS.DATA);
        dataList = formData.split(",");
        setupAdapter();
        return view;
    }

    private void setupAdapter() {
        FormAdapter adapter = new FormAdapter(dataList, getContext());
        rvForm.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvForm.setAdapter(adapter);
        rvForm.getAdapter().notifyDataSetChanged();
    }

}
