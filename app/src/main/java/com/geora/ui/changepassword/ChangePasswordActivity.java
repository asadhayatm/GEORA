package com.geora.ui.changepassword;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseActivity;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.FailureResponse;
import com.geora.model.ResetPasswordModel;
import com.geora.util.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivMenu;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_old_password)
    EditText etOldPassword;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.iv_old_password_eye)
    ImageView ivOldPasswordEye;
    @BindView(R.id.iv_new_password_eye)
    ImageView ivNewPasswordEye;
    private ChangePasswordViewModel mChangePasswordViewModel;
    private boolean hideOldPass = true;
    private boolean hideNewPass = true;
    private boolean mForUpdateEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChangePasswordViewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel.class);
        mChangePasswordViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        observeLiveData();
        getIntentData();
        initViewsAndVariables();
        ButterKnife.bind(this);
    }

    private void getIntentData() {
        if (getIntent() != null && getIntent().hasExtra(AppConstantClass.APIConstant.ACTION) && getIntent().getStringExtra(AppConstantClass.APIConstant.ACTION).equals("updateEmail")) {
            mForUpdateEmail = true;
        }
    }

    private void initViewsAndVariables() {
        if (mForUpdateEmail) {
            etNewPassword.setVisibility(View.GONE);
            etOldPassword.setHint(R.string.email_address);
            ivNewPasswordEye.setVisibility(View.GONE);
            ivOldPasswordEye.setVisibility(View.GONE);
            etOldPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mail, 0, 0, 0);
            tvTitle.setText(R.string.s_change_email);
            etOldPassword.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        } else {
            etOldPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etNewPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
            tvTitle.setText(R.string.change_password);
        }

    }

    private void observeLiveData() {
        mChangePasswordViewModel.getChangepasswordLiveData().observe(this, new Observer<ResetPasswordModel>() {
            @Override
            public void onChanged(@Nullable ResetPasswordModel changePasswordResponse) {
                hideProgressDialog();
                if (AppUtils.checkUserValid(ChangePasswordActivity.this, changePasswordResponse.getCode(), changePasswordResponse.getMessage())) {
                    if (mForUpdateEmail)
                        showToastLong("Verification mail send successfully");
                    else
                        showToastLong("Password changed successfully");
                    finishAfterTransition();

                }
            }
        });
        mChangePasswordViewModel.getValidateLiveData().observe(this, new Observer<FailureResponse>() {
            @Override
            public void onChanged(@Nullable FailureResponse failureResponse) {
                hideProgressDialog();
                if (failureResponse != null)
                    showToastLong(failureResponse.getErrorMessage());
            }
        });
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_change_password;
    }


    @OnClick({R.id.iv_back, R.id.tv_submit, R.id.iv_old_password_eye, R.id.iv_new_password_eye})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_submit:
                showProgressDialog();
                if (mForUpdateEmail)
                    mChangePasswordViewModel.onUpdateEmailAddress(etOldPassword.getText().toString());
                else
                    mChangePasswordViewModel.onSubmitViewClick(etOldPassword.getText().toString().trim(), etNewPassword.getText().toString().trim());
                break;
            case R.id.iv_old_password_eye:
                if (hideOldPass) {
                    ivOldPasswordEye.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_selected));
                    etOldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    hideOldPass = false;
                } else {
                    etOldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivOldPasswordEye.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_unselected));
                    hideOldPass = true;
                }
                break;
            case R.id.iv_new_password_eye:
                if (hideNewPass) {
                    ivNewPasswordEye.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_selected));
                    etNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    hideNewPass = false;
                } else {
                    etNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivNewPasswordEye.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_unselected));
                    hideNewPass = true;
                }
                break;
        }
    }
    @Override
    protected void onFailure(FailureResponse failureResponse) {
        super.onFailure(failureResponse);
        hideProgressDialog();
        if (failureResponse.getErrorCode() == 422) {
            showToastLong(getResources().getString(R.string.something_went_wrong));
//            logout(this);
        }
        if (failureResponse.getErrorCode() == 401) {
            showToastLong(getResources().getString(R.string.user_is_blocked_by_admin));
            logout(this);
        }
    }

}
