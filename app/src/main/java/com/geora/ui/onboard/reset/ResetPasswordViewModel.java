package com.geora.ui.onboard.reset;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.R;
import com.geora.base.RichMediatorLiveData;
import com.geora.constants.AppConstants;
import com.geora.model.FailureResponse;
import com.geora.model.signup.SignupModel;
import com.geora.util.ResourceUtils;

public class ResetPasswordViewModel extends ViewModel {

    private RichMediatorLiveData<SignupModel> mResetPasswordLiveData;
    private Observer<FailureResponse> mFailureObserver;
    private Observer<Throwable> mErrorObserver;
    private MutableLiveData<FailureResponse> mValidateLiveData;

    private ResetPasswordRepo mResetPasswordRepo = new ResetPasswordRepo();

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

        if (mValidateLiveData == null)
            mValidateLiveData = new MutableLiveData<>();
    }

    public RichMediatorLiveData<SignupModel> getmResetPasswordLiveData() {
        return mResetPasswordLiveData;
    }

    /**
     * This method is used to check the validations and pass the data to the
     * {@link ResetPasswordRepo} to get the response
     *
     * @param password contains the params of the request
     */
    public void onSubmitClicked(String password) {
        if (checkValidation(password)) {
            mResetPasswordRepo.resetPassword(mResetPasswordLiveData, password);
        }
    }

    /**
     * This method is used to check the validations
     *
     * @return false if any validation fails otherwise true
     */
    private boolean checkValidation(String password) {
        if (password.isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.NEW_PASSWORD_EMPTY, ResourceUtils.getInstance()
                    .getString(R.string.new_password_empty)
            ));
            return false;
        } else if (password.length() < 6) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.INVALID_PASSWORD, ResourceUtils.getInstance()
                    .getString(R.string.enter_valid_password)
            ));
            return false;
        }
        return true;
    }

    public MutableLiveData<FailureResponse> getValidationLiveData() {
        return mValidateLiveData;

    }
}
