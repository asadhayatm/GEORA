package com.geora.customviews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geora.R;
import com.geora.listeners.DialogCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DialogForDonateAmount extends Dialog {

    private Context context;
    private DialogCallback dialogCallback;
    private String text = "";

    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_donate)
    TextView tvDonate;


    public DialogForDonateAmount(Context context, DialogCallback dialogCallback) {
        super(context);
        this.context = context;
        this.text = text;
        this.dialogCallback = dialogCallback;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.db_donation);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        etAmount.addTextChangedListener(new TextWatcher(){
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (etAmount.getText().toString().matches("^0"))
                {
                    // Not allowed
                    Toast.makeText(context, "not allowed", Toast.LENGTH_LONG).show();
                    etAmount.setText("");
                }
            }
            @Override
            public void afterTextChanged(Editable arg0) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });

    }

    @OnClick({R.id.tv_donate, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                break;
            case R.id.tv_donate:
                dialogCallback.onSubmit(view, etAmount.getText().toString(), 0);

                break;
        }
        dismiss();
    }
}
