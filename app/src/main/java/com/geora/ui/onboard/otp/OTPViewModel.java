package com.geora.ui.onboard.otp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.R;
import com.geora.base.RichMediatorLiveData;
import com.geora.constants.AppConstants;
import com.geora.model.FailureResponse;
import com.geora.model.otpverification.OTPVerificatonModel;
import com.geora.model.request.User;
import com.geora.model.signup.SignupModel;
import com.geora.ui.onboard.login.LoginFragment;
import com.geora.util.ResourceUtils;

public class OTPViewModel extends ViewModel {

    private RichMediatorLiveData<OTPVerificatonModel> mOtpLiveData;
    private RichMediatorLiveData<SignupModel> mOtpResentPasswordLiveData;
    private RichMediatorLiveData<SignupModel> mOtpResendPasswordLiveData;
    private Observer<FailureResponse> mFailureObserver;
    private Observer<Throwable> mErrorObserver;
    private MutableLiveData<FailureResponse> mValidateLiveData;


    private OTPRepo mOtpRepo = new OTPRepo();


    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureObserver;

        initLiveData();
    }

    private void initLiveData() {
        if (mOtpLiveData == null) {
            mOtpLiveData = new RichMediatorLiveData<OTPVerificatonModel>() {
                @Override
                protected Observer<FailureResponse> getFailureObserver() {
                    return mFailureObserver;
                }

                @Override
                protected Observer<Throwable> getErrorObserver() {
                    return mErrorObserver;
                }
            };
        } if (mOtpResentPasswordLiveData == null) {
            mOtpResentPasswordLiveData = new RichMediatorLiveData<SignupModel>() {
                @Override
                protected Observer<FailureResponse> getFailureObserver() {
                    return mFailureObserver;
                }

                @Override
                protected Observer<Throwable> getErrorObserver() {
                    return mErrorObserver;
                }
            };
        }if (mOtpResendPasswordLiveData == null) {
            mOtpResendPasswordLiveData = new RichMediatorLiveData<SignupModel>() {
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
     * This method gives the login live data object to {@link OTPVerificationFragment}
     *
     * @return {@link #mOtpLiveData}
     */
    public RichMediatorLiveData<OTPVerificatonModel> getOTPLiveData() {
        return mOtpLiveData;
    }
    public RichMediatorLiveData<SignupModel> getOTPResentPasswordLiveData() {
        return mOtpResentPasswordLiveData;
    }public RichMediatorLiveData<SignupModel> getOTPResendPasswordLiveData() {
        return mOtpResendPasswordLiveData;
    }

    /**
     * Method used to hit login api after checking validations
     *
     * @param user contains all the params of the request
     */
    public void otpSendButtonClicked(User user) {
        if (checkValidation(user)) {
            if (user.getType().equalsIgnoreCase("resend")) {
                mOtpRepo.hitOtpResend(mOtpResentPasswordLiveData, user);
            } else {
                //showProgress
                mOtpRepo.hitOtpVerification(mOtpLiveData, user);
            }
        }
    }

    /**
     * Method to check validation
     *
     * @param user
     * @return
     */
    private boolean checkValidation(User user) {
        if (user.getOtp().isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.OTP, ResourceUtils.getInstance().getString(R.string.enter_otp)));
            return false;
        } else if (user.getOtp().length() != 6) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.OTP_LENGTH, ResourceUtils.getInstance().getString(R.string.otp_must_be_6_digit)));
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

    public void otpReSendButtonClicked(User user) {
        mOtpRepo.hitOtpResend(mOtpResendPasswordLiveData, user);

    }
}
