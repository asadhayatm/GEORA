package com.geora.ui.onboard.forgotpassword;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.R;
import com.geora.base.RichMediatorLiveData;
import com.geora.constants.AppConstants;
import com.geora.model.FailureResponse;
import com.geora.model.signup.SignupModel;
import com.geora.util.ResourceUtils;

public class ForgotPasswordViewModel extends ViewModel {

    private RichMediatorLiveData<SignupModel> forgotLiveData;
    private Observer<FailureResponse> failureObserver;
    private Observer<Throwable> errorObserver;
    private MutableLiveData<FailureResponse> validateLiveData;

    private ForgotPasswordRepo forgotPasswordRepo = new ForgotPasswordRepo();

    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureObserver) {
        this.errorObserver = errorObserver;
        this.failureObserver = failureObserver;

        initLiveData();
    }

    private void initLiveData() {
        if (forgotLiveData == null) {
            forgotLiveData = new RichMediatorLiveData<SignupModel>() {
                @Override
                protected Observer<FailureResponse> getFailureObserver() {
                    return failureObserver;
                }

                @Override
                protected Observer<Throwable> getErrorObserver() {
                    return errorObserver;
                }
            };

            if (validateLiveData == null)
                validateLiveData = new MutableLiveData<>();
        }
    }


    public RichMediatorLiveData<SignupModel> getForgotPasswordLiveData() {
        return forgotLiveData;
    }


    public void onSubmitClicked(String email,String countryCOde) {
        if (checkValidation(email)) {
            forgotPasswordRepo.hitForgotPassword(forgotLiveData, email,countryCOde);
        }

    }


    private boolean checkValidation(String email) {
        if (email.isEmpty()) {
            validateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.EMAIL_EMPTY, ResourceUtils.getInstance().getString(R.string.enter_email)
            ));
            return false;
        }
        return true;
    }

    public MutableLiveData<FailureResponse> getValidationLiveData() {
        return validateLiveData;
    }
}
