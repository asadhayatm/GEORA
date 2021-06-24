package com.geora.ui.changepassword;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import android.util.Patterns;

import com.geora.R;
import com.geora.base.RichMediatorLiveData;
import com.geora.constants.AppConstants;
import com.geora.model.FailureResponse;
import com.geora.model.ResetPasswordModel;
import com.geora.util.ResourceUtils;

public class ChangePasswordViewModel extends ViewModel {
    private Observer<FailureResponse> mFailureObserver;

    private Observer<Throwable> mErrorObserver;

    private RichMediatorLiveData<ResetPasswordModel> mChangePasswordLiveData;

    private MutableLiveData<FailureResponse> mValidateLiveData;

    //initializing repository class
    private ChangePasswordRepo mChangePasswordRepo = new ChangePasswordRepo();


    //set error observer & failure observer instances
    public void setGenericListeners(Observer<Throwable> errorObserver, Observer<FailureResponse> failureResponseObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureResponseObserver;

        initLiveData();
    }

    /**
     * Initializing all the live data objects
     */
    private void initLiveData() {
        if (mChangePasswordLiveData == null) {
            mChangePasswordLiveData = new RichMediatorLiveData<ResetPasswordModel>() {
                @Override
                protected Observer<FailureResponse> getFailureObserver() {
                    return mFailureObserver;
                }

                @Override
                protected Observer<Throwable> getErrorObserver() {
                    return mErrorObserver;
                }
            };

            if (mValidateLiveData == null)
                mValidateLiveData = new MutableLiveData<>();
        }
    }

    /**
     * This method gives the live data object to {@link ChangePasswordActivity}
     *
     * @return {@link #mChangePasswordLiveData}
     */
    public RichMediatorLiveData<ResetPasswordModel> getChangepasswordLiveData() {
        return mChangePasswordLiveData;
    }

    /**
     * This method gives the validation live data to {@link ChangePasswordActivity}
     *
     * @return {@link #mValidateLiveData}
     */
    public MutableLiveData<FailureResponse> getValidateLiveData() {
        return mValidateLiveData;
    }

    public void onSubmitViewClick(String oldPassword, String newPassword) {
        if (checkValidations(oldPassword, newPassword)) {
            mChangePasswordRepo.hitChangePasswordApi(mChangePasswordLiveData, oldPassword, newPassword);
        }
    }

    private boolean checkValidations(String oldPassword, String newPassword) {
        if (oldPassword.isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.OLD_PASSWORD_EMPTY, ResourceUtils.getInstance().getString(R.string.please_enter_old_pass)
            ));
            return false;
        } else if (oldPassword.length() < 6) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.INVALID_PASSWORD, ResourceUtils.getInstance().getString(R.string.h_password_lenght)
            ));
            return false;
        } else if (newPassword.isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.NEW_PASSWORD_EMPTY, ResourceUtils.getInstance().getString(R.string.new_valid)
            ));
            return false;
        } else if (newPassword.length() < 6) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.INVALID_PASSWORD, ResourceUtils.getInstance().getString(R.string.h_password_lenght)
            ));
            return false;
        }
        return true;
    }

    public void onUpdateEmailAddress(String email) {
        if (checkEmailValidations(email)) {
            mChangePasswordRepo.hitUpdateEmailAddressApi(mChangePasswordLiveData, email);
        }
    }

    private boolean checkEmailValidations(String email) {
        if (email.isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.EMAIL_EMPTY, ResourceUtils.getInstance().getString(R.string.s_enter_email)));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mValidateLiveData.setValue(new FailureResponse(AppConstants.UIVALIDATIONS.INVALID_EMAIL, ResourceUtils.getInstance().getString(R.string.s_please_enter_valid_email_address)));
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mValidateLiveData.setValue(new FailureResponse(AppConstants.UIVALIDATIONS.INVALID_EMAIL,ResourceUtils.getInstance().getString(R.string.s_please_enter_valid_email_address)));
            return false;
        }
        return true;
    }
}
