package com.geora.customviews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.geora.R;
import com.geora.listeners.DialogCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogForValidate extends Dialog {

    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.tv_yes)
    TextView tvYes;
    private Context context;
    private DialogCallback dialogCallback;
    private String message;
    private String option;


    public DialogForValidate(Context context, DialogCallback dialogCallback, String message, String option) {
        super(context);
        this.context = context;
        this.message = message;
        this.option = option;
        this.dialogCallback = dialogCallback;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirmation);
        ButterKnife.bind(this);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        tvMessage.setText(message);
        tvNo.setVisibility(View.GONE);
        tvYes.setText(option);

    }


    @OnClick({R.id.tv_no, R.id.tv_yes})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_no:
                dialogCallback.onSubmit(view,tvNo.getText().toString(),0);
                break;
            case R.id.tv_yes:
                dialogCallback.onSubmit(view,tvYes.getText().toString(),0);
                break;
        }
        dismiss();
    }
}


