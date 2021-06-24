package com.geora.customviews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.geora.R;
import com.geora.listeners.DialogCallback;
import com.geora.listeners.RecycleListener;
import com.geora.model.sale.GetColor;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogForColors extends Dialog implements RecycleListener {

    @BindView(R.id.rv_colors)
    RecyclerView rvColors;

    private Context context;
    private DialogCallback dialogCallback;
    private String text = "";
    private List<GetColor> colors;


    public DialogForColors(Context context, DialogCallback dialogCallback, List<GetColor> colors) {
        super(context);
        this.context = context;
        this.colors = colors;
        this.dialogCallback = dialogCallback;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.db_open_color);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        //set view for feedback
        setupAdapter();
    }

    private void setupAdapter() {
        ColorDialogAdapter adapterImage = new ColorDialogAdapter(colors);
        rvColors.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvColors.setAdapter(adapterImage);
        adapterImage.setClickListener(this);
        adapterImage.notifyDataSetChanged();
    }
/*
    @OnClick({R.id.tv_cancel, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                break;
            case R.id.tv_submit:
                break;
        }
        dismiss();

    }*/


    @Override
    public void onItemClicked(View view, String s, int pos) {
        dialogCallback.onSubmit(view, colors.get(pos).getColorHexCode(), 0);
        dismiss();
    }

    @Override
    public void onItemLongClicked(String s, int pos) {

    }
}