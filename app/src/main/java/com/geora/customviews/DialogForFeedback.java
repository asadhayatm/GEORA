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
import com.geora.listeners.RateDialogCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogForFeedback extends Dialog {

    @BindView(R.id.iv_rate)
    ImageView ivRate;
    @BindView(R.id.tv_label_rate_us)
    TextView tvLabelRateUs;
    @BindView(R.id.tv_rating_messag)
    TextView tvRatingMessag;
    @BindView(R.id.rb_rate)
    RatingBar rbRate;
    @BindView(R.id.tv_title)
    EditText tvTitle;
    @BindView(R.id.tv_description)
    EditText tvDescription;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    private Context context;
    private RateDialogCallback dialogCallback;
    private String text = "";


    public DialogForFeedback(Context context, RateDialogCallback dialogCallback) {
        super(context);
        this.context = context;
        this.text = text;
        this.dialogCallback = dialogCallback;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_rate);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        //set view for feedback
        tvLabelRateUs.setText("Feedback");
        ivRate.setImageResource(R.drawable.ic_feedback_graphic);
        tvRatingMessag.setText("Help us know what issues should we focus on to improve your experience with Geora.");
        rbRate.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvDescription.setVisibility(View.VISIBLE);

    }

    @OnClick({R.id.tv_cancel, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_submit:
                dialogCallback.onSubmit(tvSubmit, tvTitle.getText().toString().trim(), tvDescription.getText().toString().trim(), 0.0);
                break;
        }

    }


}