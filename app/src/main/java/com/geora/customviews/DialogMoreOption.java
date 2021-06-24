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
import android.widget.TextView;

import com.geora.R;
import com.geora.listeners.DialogCallback;
import com.geora.ui.cardlist.CardListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DialogMoreOption extends Dialog {

    private Context context;
    private DialogCallback dialogCallback;
    private String screen = "";
    private boolean isDefault;
    private int type;

    @BindView(R.id.tv_default)
    TextView tvDefault;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.v1)
    View v1;
    @BindView(R.id.v2)
    View v2;


    public DialogMoreOption(Context context, DialogCallback dialogCallback, String screen, boolean isDefault, int type) {
        super(context);
        this.context = context;
        this.screen = screen;
        this.isDefault = isDefault;
        this.dialogCallback = dialogCallback;
        this.type = type;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.db_more_options);
        ButterKnife.bind(this);
        if (type == 1){
            v1.setVisibility(View.GONE);
            tvDefault.setVisibility(View.GONE);
        }else {
            if (isDefault) {
                v1.setVisibility(View.GONE);
                tvDefault.setVisibility(View.GONE);
            }
        }
        if (screen.equalsIgnoreCase(CardListActivity.class.getSimpleName())) {
            v2.setVisibility(View.GONE);
            tvEdit.setVisibility(View.GONE);
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

    }

    @OnClick({R.id.tv_edit, R.id.tv_cancel, R.id.tv_default, R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_default:
                dialogCallback.onSubmit(view, tvDefault.getText().toString(), 0);
                break;
            case R.id.tv_delete:
                dialogCallback.onSubmit(view, tvDelete.getText().toString(), 0);
                break;
            case R.id.tv_cancel:
                dialogCallback.onSubmit(view, tvCancel.getText().toString(), 0);
                break;
            case R.id.tv_edit:
                dialogCallback.onSubmit(view, tvEdit.getText().toString(), 0);
                break;
        }
        dismiss();
    }
}
