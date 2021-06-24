package com.geora.customviews;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geora.R;
import com.geora.listeners.DialogCallback;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.geora.util.DateTimeUtils.dobToTimeStampForPicker;
import static com.geora.util.DateTimeUtils.parseDOBDate;


public class DialogForDob extends Dialog {

    @BindView(R.id.tv_enter)
    TextView tvEnter;
    @BindView(R.id.tv_dob)
    TextView tvDob;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    private Context context;
    private DialogCallback dialogCallback;
    private String text = "";


    public DialogForDob(Context context, DialogCallback dialogCallback) {
        super(context);
        this.context = context;
        this.text = text;
        this.dialogCallback = dialogCallback;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.db_dob);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

    }

    @OnClick({R.id.tv_dob, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_dob:
                openDatePicker();
                break;
            case R.id.tv_submit:
                dialogCallback.onSubmit(view, tvDob.getText().toString(), 0);
                dismiss();
                break;
        }
    }


    //open date picker so that user can edit his/her dob
    private void openDatePicker() {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(dobToTimeStampForPicker(tvDob.getText().toString()));
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -13);
        cal.add(Calendar.DATE, -1);
        Date date = cal.getTime();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        tvDob.setText(parseDOBDate(i + "-" + (i1 + 1) + "-" + i2));
                    }
                }, mYear, mMonth, mDay);


        datePickerDialog.getDatePicker().setMaxDate(date.getTime());
        datePickerDialog.show();
    }

}
