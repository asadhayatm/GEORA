package com.geora.ui.addcard;

import android.app.DatePickerDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseActivity;
import com.geora.model.CardModel;
import com.geora.model.FailureResponse;
import com.geora.model.cardlist.CardListModel;
import com.geora.util.AppUtils;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddCardActivity extends BaseActivity {
    @BindView(R.id.tv_add_card)
    TextView tvAddCard;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_check)
    ImageView ivCheck;
    @BindView(R.id.et_card_no)
    EditText etCardNo;
    @BindView(R.id.et_expiry_date)
    TextView etExpiryDate;
    @BindView(R.id.et_cvv)
    EditText etCvv;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.main)
    RelativeLayout main;

    private Unbinder unbinder;
    private boolean isChecked = false;
    private String yearStr = "0", monthStr = "0";
    private AddCardViewModel mAddCardViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        unbinder = ButterKnife.bind(this);
        tvTitle.setText(getResources().getString(R.string.add_new_card));


        mAddCardViewModel = ViewModelProviders.of(this).get(AddCardViewModel.class);
        mAddCardViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        //check for device token from repo, if it is not present then ask for token

        mAddCardViewModel.getAddCardModelLiveData().observe(this, new Observer<CardListModel>() {
            @Override
            public void onChanged(@Nullable CardListModel addCardModel) {
                hideProgressDialog();
                if (addCardModel != null) {
                    if (AppUtils.checkUserValid(AddCardActivity.this, addCardModel.getCode(), addCardModel.getMessage())) {
                        showSnackBar(main, getResources().getString(R.string.card_addeed_successfully), false);
                        finishAfterTransition();
                    }
                }

            }
        });

        //observing validation live data
        mAddCardViewModel.getValidationLiveData().observe(this, new Observer<FailureResponse>() {
            @Override
            public void onChanged(@Nullable FailureResponse failureResponse) {
                hideProgressDialog();
                if (failureResponse != null) {
                    showSnackBar(main, failureResponse.getErrorMessage().toString(), true);
                }
            }
        });
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_add_card;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.et_expiry_date, R.id.tv_add_card, R.id.iv_check, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishAfterTransition();
                break;
            case R.id.et_expiry_date:
                openDatePicker();
                break;
            case R.id.tv_add_card:
                if (isInternetAvailable()) {
                    showProgressDialog();
                    CardModel cardModel = new CardModel();
                    cardModel.setCardno(etCardNo.getText().toString());
                    cardModel.setCvv(etCvv.getText().toString());
                    cardModel.setName(etName.getText().toString());
                    cardModel.setMonth(Integer.parseInt(monthStr));
                    cardModel.setYear(Integer.parseInt(yearStr));
                    cardModel.setDate(etExpiryDate.getText().toString());
                    mAddCardViewModel.submutClicked(cardModel, isInternetAvailable());
                } else {
                    showSnackBar(main, getResources().getString(R.string.no_internet_connection), true);
                }
                break;
            case R.id.iv_check:
                if (isChecked) {
                    ivCheck.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_selected));
                    isChecked = false;
                } else {
                    ivCheck.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_unselected));
                    isChecked = true;
                }

                break;
        }
    }

    private void openDatePicker() {
        hideKeyboard();
        // Get Current Date
        final Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(Long.parseLong(DataManager.getInstance().getDob()));
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);
        Date date = new Date();

        DatePickerDialog monthDatePickerDialog = new DatePickerDialog(this,
                R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yearStr = (year + "").substring(2, 4);
                monthStr = (month + 1) + "";
                etExpiryDate.setText(monthStr + " / " + yearStr);
            }
        }, mYear, mMonth, mDay) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                View view = getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android"));
                if (view != null)
                    view.setVisibility(View.GONE);
            }
        };
        monthDatePickerDialog.getDatePicker().setMinDate(date.getTime());

        monthDatePickerDialog.setTitle("Select Expiry");
        monthDatePickerDialog.show();
    }

    @Override
    protected void onFailure(FailureResponse failureResponse) {
        super.onFailure(failureResponse);
        //showSnackBar(main, failureResponse.getErrorMessage().toString(), true);
        if (failureResponse.getErrorCode() == 422) {
            showToastLong(getResources().getString(R.string.something_went_wrong));
//            logout(this);
        }
        if (failureResponse.getErrorCode() == 401) {
            showToastLong(getResources().getString(R.string.user_is_blocked_by_admin));
            logout(this);
        }
    }

    @Override
    protected void onErrorOccurred(Throwable throwable) {
        super.onErrorOccurred(throwable);
        //showSnackBar(main, throwable.getMessage().toString(), true);

    }
}
