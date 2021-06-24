package com.geora.ui.settings;

import android.app.Activity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseFragment;
import com.geora.customviews.DialogForConfirmation;
import com.geora.data.constants.AppConstantClass;
import com.geora.listeners.DialogCallback;
import com.geora.model.FailureResponse;
import com.geora.model.signup.SignupModel;
import com.geora.ui.home.HomeActivity;
import com.geora.ui.onboard.OnBoardActivity;
import com.geora.util.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingsFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {
    @BindView(R.id.tv_signout)
    TextView tvSignout;
    @BindView(R.id.rl_notification)
    RelativeLayout rlNotification;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.rl_diatance)
    RelativeLayout rlDiatance;
    @BindView(R.id.rl_change_password)
    RelativeLayout rlChangePassword;
    @BindView(R.id.switch_notification)
    Switch switchNotification;
    @BindView(R.id.sb_proximity)
    SeekBar sbProximity;
    @BindView(R.id.tv_update)
    TextView tvUpdate;

    private SettingsViewMOdel mSettingsViewMOdel;
    private Context mContext;
    private ISettingsHost mHost;
    private Unbinder unbinder;
    private int mProximityRange;
    private boolean isNotificationSet;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getContext();
        ((HomeActivity) mContext).setupToolbar(4, mContext.getResources().getString(R.string.settings));

        //initializing view model & setting listeners
        mSettingsViewMOdel = ViewModelProviders.of(this).get(SettingsViewMOdel.class);
        mSettingsViewMOdel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        initViewsAndVariables();
        observeLiveData();
        if (mSettingsViewMOdel.getIsSocailLogin())
            rlChangePassword.setVisibility(View.GONE);
        else
            rlChangePassword.setVisibility(View.VISIBLE);

        return view;
    }

    /**
     * initialize view and variables
     */
    private void initViewsAndVariables() {
          isNotificationSet=true;
          tvUpdate.setVisibility(View.GONE);
          mProximityRange=0;
        //set notification status
        if ((mSettingsViewMOdel.getNotificationStatus() != null)) {
            if (mSettingsViewMOdel.getNotificationStatus().equalsIgnoreCase(AppConstantClass.ENABLE))
                switchNotification.setChecked(true);
            else
                switchNotification.setChecked(false);
        }
        //set proximity range which was selected by user previously
        if (mSettingsViewMOdel.getProximityRange() != null) {
            tvDistance.setText(String.format("%s miles", mSettingsViewMOdel.getProximityRange()));
            sbProximity.setProgress(Integer.valueOf(mSettingsViewMOdel.getProximityRange()));
        }
        sbProximity.setOnSeekBarChangeListener(this);
        switchNotification.setOnCheckedChangeListener(this);
    }


//observe live data
    private void observeLiveData() {
        mSettingsViewMOdel.getLogoutLiveData().observe(this, new Observer<SignupModel>() {
            @Override
            public void onChanged(@Nullable SignupModel successResponse) {
                //password is reset successfully
                if (successResponse != null) {
                    if (AppUtils.checkUserValid(getActivity(), successResponse.getCode(), successResponse.getMessage())) {
                        hideProgressDialog();
                        startActivity(new Intent(mContext, OnBoardActivity.class));
                        ((Activity) mContext).finishAfterTransition();
                    }
                }
            }
        });

        mSettingsViewMOdel.getProfileSettingLiveData().observe(this, new Observer<SignupModel>() {
            @Override
            public void onChanged(@Nullable SignupModel signupModel) {
                hideProgressDialog();
                if (signupModel != null) {
                    if (AppUtils.checkUserValid(getActivity(), signupModel.getCode(), signupModel.getMessage())) {
                        if (signupModel.getData().getNotificationSubscriptionStatus() != null) {
                            isNotificationSet = true;
                            if (signupModel.getData().getNotificationSubscriptionStatus().equalsIgnoreCase(AppConstantClass.ENABLE))
                                switchNotification.setChecked(true);
                            else
                                switchNotification.setChecked(false);

                        } else if (signupModel.getData().getProximityRange() != null) {
                            tvUpdate.setVisibility(View.GONE);
                            sbProximity.setSecondaryProgress(Integer.valueOf(signupModel.getData().getProximityRange()));
                            tvDistance.setText(String.format("%s miles", signupModel.getData().getProximityRange()));
                        }
                    }
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.tv_signout, R.id.rl_change_password,R.id.tv_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_signout:
                openConfirmationDialog();
                /*showProgressDialog();
                mSettingsViewMOdel.onSubmitClicked();*/
                break;
            case R.id.rl_change_password:
                mHost.moveToChangePasswordScreen();
                break;
            case R.id.tv_update:
                showProgressDialog();
                //hit api to update proximity range setting
                mSettingsViewMOdel.updateProfileProximity(mProximityRange);
                //ActivityUtils.scheduleJob(mContext.getApplicationContext());
                break;


        }
    }

    /**
     * show confirmation dialog to user for logout
     */
    private void openConfirmationDialog() {
        new DialogForConfirmation(mContext, new DialogCallback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSubmit(View view, String result, int s) {
                if (result.equalsIgnoreCase("yes")) {
                     showProgressDialog();
                mSettingsViewMOdel.onSubmitClicked();
                }
            }
        },getString(R.string.s_are_you_sure_you_want_to_signout)).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ISettingsHost) {
            mHost = (ISettingsHost) context;
        } else throw new IllegalStateException("Host must implement ISettingsHost");
    }

    @Override
    protected void onErrorOccurred(Throwable throwable) {
        super.onErrorOccurred(throwable);
        hideProgressDialog();
    }

    @Override
    protected void onFailure(FailureResponse failureResponse) {
        super.onFailure(failureResponse);
        hideProgressDialog();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //hit api to update notification setting
        if (isNotificationSet) {
            isNotificationSet=false;
            mSettingsViewMOdel.updateNotification(isChecked);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mProximityRange=progress;
        tvDistance.setText(String.format("%s miles", String.valueOf(progress)));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        tvUpdate.setVisibility(View.VISIBLE);

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    public interface ISettingsHost {

        void moveToChangePasswordScreen();
    }
}
