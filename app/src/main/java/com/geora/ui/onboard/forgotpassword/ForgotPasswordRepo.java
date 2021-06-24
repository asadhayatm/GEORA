package com.geora.ui.onboard.forgotpassword;


import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.FailureResponse;
import com.geora.model.commonresponse.CommonResponse;
import com.geora.model.signup.Data;
import com.geora.model.signup.SignupModel;

import java.util.HashMap;

import static com.geora.util.DateTimeUtils.bdayToTimeStamp;

public class ForgotPasswordRepo {

    public void hitForgotPassword(final RichMediatorLiveData<SignupModel> forgotLiveData, String email, String countryCOde) {
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.EMAIL, email);
        params.put(AppConstantClass.APIConstant.COUNTRYCODE, "+" + countryCOde);
        if (email.contains("@"))
            params.put(AppConstantClass.APIConstant.TYPE, "email");
        else
            params.put(AppConstantClass.APIConstant.TYPE, "phone");


        DataManager.getInstance().hitForgotPasswordApi(params).enqueue(new NetworkCallback<SignupModel>() {
            @Override
            public void onSuccess(SignupModel userResponse) {
                //save data in preference
                saveUserToPreference(userResponse.getData());
                forgotLiveData.setValue(userResponse);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                forgotLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                forgotLiveData.setError(t);
            }
        });
    }

    private void saveUserToPreference(Data data) {
        DataManager.getInstance().saveToken(data.getToken());
    }
}