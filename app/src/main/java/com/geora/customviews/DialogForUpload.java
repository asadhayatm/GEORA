package com.geora.customviews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.geora.R;
import com.geora.listeners.DialogCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DialogForUpload extends Dialog {

    private Context context;
    private DialogCallback dialogCallback;
    private String text = "";

    @BindView(R.id.ll_camera)
    LinearLayout llCamera;
    @BindView(R.id.ll_gallry)
    LinearLayout llGallry;


    public DialogForUpload(Context context, DialogCallback dialogCallback) {
        super(context);
        this.context = context;
        this.text = text;
        this.dialogCallback = dialogCallback;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.db_upload_image);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

    }

    @OnClick({R.id.ll_gallry, R.id.ll_camera})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_camera:
                dialogCallback.onSubmit(view, "camera", 0);
                break;
            case R.id.ll_gallry:
                dialogCallback.onSubmit(view, "gallery", 0);
                break;
        }
        dismiss();
    }
}
