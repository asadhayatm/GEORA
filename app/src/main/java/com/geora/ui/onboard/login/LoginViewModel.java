package com.geora.ui.onboard.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.R;
import com.geora.base.RichMediatorLiveData;
import com.geora.constants.AppConstants;
import com.geora.model.FailureResponse;
import com.geora.model.otpverification.OTPVerificatonModel;
import com.geora.model.request.User;
import com.geora.util.ResourceUtils;

public class LoginViewModel extends ViewModel {

    private RichMediatorLiveData<OTPVerificatonModel> mLoginLiveData;
    private RichMediatorLiveData<OTPVerificatonModel> mDobLiveData;
    private Observer<FailureResponse> mFailureObserver;
    private Observer<Throwable> mErrorObserver;
    private MutableLiveData<FailureResponse> mValidateLiveData;
    private LoginRepo mLoginRepo = new LoginRepo();

    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureObserver;

        initLiveData();
    }

    private void initLiveData() {
        if (mLoginLiveData == null) {
            mLoginLiveData = new RichMediatorLiveData<OTPVerificatonModel>() {
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
        if (mDobLiveData == null) {
            mDobLiveData = new RichMediatorLiveData<OTPVerificatonModel>() {
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

        if (mValidateLiveData == null) {
            mValidateLiveData = new MutableLiveData<>();
        }
    }

    /**
     * This method gives the login live data object to {@link LoginFragment}
     *
     * @return {@link #mLoginLiveData}
     */
    public RichMediatorLiveData<OTPVerificatonModel> getLoginLiveData() {
        return mLoginLiveData;
    }


    public RichMediatorLiveData<OTPVerificatonModel> getDobLiveData() {
        return mDobLiveData;
    }

    /**
     * Method used to hit login api after checking validations
     *
     * @param user contains all the params of the request
     */
    public void loginButtonClicked(User user) {
        if (checkValidation(user)) {
            //showProgress
            mLoginRepo.hitLoginApi(mLoginLiveData, user);
        }
    } public void saveDob(String dob, OTPVerificatonModel user) {
            //showProgress
            mLoginRepo.hitSaveDobApi(mDobLiveData, dob, user);
    }

    /**
     * Method to check validation
     *
     * @param user
     * @return
     */
    private boolean checkValidation(User user) {
        if (user.getEmail().isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.EMAIL_EMPTY, ResourceUtils.getInstance().getString(R.string.enter_email)));
            return false;
        }

        else if (user.getPassword().isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.PASSWORD_EMPTY, ResourceUtils.getInstance().getString(R.string.enter_password)
            ));
            return false;
        } else if (user.getPassword().length() < 6) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.INVALID_PASSWORD, ResourceUtils.getInstance().getString(R.string.enter_valid_password)
            ));
            return false;
        }
        return true;
    }

    /**
     * This method gives the validation live data object to {@link LoginFragment}
     *
     * @return {@link #mValidateLiveData}
     */
    public MutableLiveData<FailureResponse> getValidationLiveData() {
        return mValidateLiveData;
    }

    public void saveDeviceToken(String deviceToken) {
        mLoginRepo.saveDeviceToken(deviceToken);
    }

    public void socialLogin(User user) {
        mLoginRepo.hitSocialLoginApi(mLoginLiveData, user);

    }
}
