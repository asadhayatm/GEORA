package com.geora.customviews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.geora.R;
import com.geora.listeners.DialogCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogForPaymentSuccessfull extends Dialog {

    @BindView(R.id.tv_ok)
    TextView tvOk;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    private Context context;
    private DialogCallback dialogCallback;
    private String message = "";
    private String text = "";


    public DialogForPaymentSuccessfull(Context context, String message, DialogCallback dialogCallback) {
        super(context);
        this.context = context;
        this.message = message;
        this.text = text;
        this.dialogCallback = dialogCallback;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.db_payment_successfull);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        tvMessage.setText(message);


    }

    @OnClick({R.id.tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_ok:
                dialogCallback.onSubmit(view, "", 0);
                break;
        }
        dismiss();

    }


}