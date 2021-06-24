package com.geora.ui.settings;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.base.RichMediatorLiveData;
import com.geora.model.FailureResponse;
import com.geora.model.signup.SignupModel;
import com.geora.ui.onboard.reset.ResetPasswordRepo;

public class SettingsViewMOdel extends ViewModel {
    private RichMediatorLiveData<SignupModel> mResetPasswordLiveData;
    private RichMediatorLiveData<SignupModel> mProfileSettingLiveData;
    private Observer<FailureResponse> mFailureObserver;
    private Observer<Throwable> mErrorObserver;

    private SettingsRepo mSettingsRepo = new SettingsRepo();

    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureResponseObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureResponseObserver;

        initLiveData();

    }

    private void initLiveData() {
        if (mResetPasswordLiveData == null) {
            mResetPasswordLiveData = new RichMediatorLiveData<SignupModel>() {
                @Override
                protected Observer<FailureResponse> getFailureObserver() {
                    return mFailureObserver;
                }

                @Override
                protected Observer<Throwable> getErrorObserver() {
                    return mErrorObserver;
                }
            };
        }
        if (mProfileSettingLiveData == null) {
            mProfileSettingLiveData = new RichMediatorLiveData<SignupModel>() {
                @Override
                protected Observer<FailureResponse> getFailureObserver() {
                    return mFailureObserver;
                }

                @Override
                protected Observer<Throwable> getErrorObserver() {
                    return mErrorObserver;
                }
            };
        }
    }

    public RichMediatorLiveData<SignupModel> getLogoutLiveData() {
        return mResetPasswordLiveData;
    }

    public RichMediatorLiveData<SignupModel> getProfileSettingLiveData() {
        return mProfileSettingLiveData;
    }

    /**
     * This method is used to check the validations and pass the data to the
     * {@link ResetPasswordRepo} to get the response
     */
    public void onSubmitClicked() {
        mSettingsRepo.logoutApi(mResetPasswordLiveData);
    }

    public void updateNotification(boolean isChecked) {
        mSettingsRepo.UpdateNotification(mProfileSettingLiveData, isChecked);
    }

    public void updateProfileProximity(int proximityRange) {
        mSettingsRepo.updateProfileProximity(mProfileSettingLiveData, proximityRange);
    }

    public String getNotificationStatus() {
        return mSettingsRepo.getNotificationStatus();
    }

    public String getProximityRange() {
        return mSettingsRepo.getProximityRange();
    }

    public boolean getIsSocailLogin() {
       return mSettingsRepo.getIsSocailLogin();
    }
}
