package com.geora.ui.settings;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseActivity;
import com.geora.model.FailureResponse;
import com.geora.model.signup.SignupModel;
import com.geora.ui.onboard.OnBoardActivity;
import com.geora.util.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity implements SettingsFragment.ISettingsHost {
    @BindView(R.id.tv_signout)
    TextView tvSignout;

    private SettingsViewMOdel mSettingsViewMOdel;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        //initializing view model & setting listeners
        mSettingsViewMOdel = ViewModelProviders.of(this).get(SettingsViewMOdel.class);
        mSettingsViewMOdel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
          observeLiveData();
    }

    private void observeLiveData() {
        mSettingsViewMOdel.getLogoutLiveData().observe(this, new Observer<SignupModel>() {
            @Override
            public void onChanged(@Nullable SignupModel successResponse) {
                //password is reset successfully
                if (successResponse != null) {
                    hideProgressDialog();
                    if (AppUtils.checkUserValid(SettingsActivity.this, successResponse.getCode(), successResponse.getMessage())) {
                        startActivity(new Intent(mContext, OnBoardActivity.class));
                        // finishAfterTransition();
                    }
                }
            }
        });

    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_settings;
    }


    @OnClick({R.id.tv_signout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_signout:
                showProgressDialog();

                break;
        }
    }

    @Override
    public void moveToChangePasswordScreen() {
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
