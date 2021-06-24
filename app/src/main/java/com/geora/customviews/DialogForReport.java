package com.geora.customviews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geora.R;
import com.geora.listeners.DialogCallback;
import com.geora.util.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DialogForReport extends Dialog {

    @BindView(R.id.tv_enter)
    TextView tvEnter;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.et_message)
    EditText etMessage;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_report)
    TextView tvReport;
    private Context context;
    private DialogCallback dialogCallback;


    public DialogForReport(Context context, DialogCallback dialogCallback) {
        super(context);
        this.context = context;
        this.dialogCallback = dialogCallback;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.db_report);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @OnClick({R.id.tv_cancel, R.id.tv_report})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_report:
                if (etMessage.getText().toString().trim().isEmpty()) {
                    Toast.makeText(context, context.getString(R.string.please_enter_reason), Toast.LENGTH_SHORT).show();
                }else {
                    dialogCallback.onSubmit(view, etMessage.getText().toString(), 0);
                    dismiss();
                }
                break;
        }
    }
}
