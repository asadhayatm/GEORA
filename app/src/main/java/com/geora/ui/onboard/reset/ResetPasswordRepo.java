package com.geora.ui.onboard.reset;


import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.FailureResponse;
import com.geora.model.ResetPasswordModel;
import com.geora.model.commonresponse.CommonResponse;
import com.geora.model.signup.SignupModel;

import java.util.HashMap;

public class ResetPasswordRepo {


    public void resetPassword(final RichMediatorLiveData<SignupModel> resetPasswordLiveData, String passeord) {

        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.PASSWORD, passeord);
        params.put(AppConstantClass.APIConstant.TOKEN, DataManager.getInstance().getToken());

        DataManager.getInstance().hitResetAPI(params).enqueue(new NetworkCallback<SignupModel>() {
            @Override
            public void onSuccess(SignupModel successResponse) {
                resetPasswordLiveData.setValue(successResponse);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                resetPasswordLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                resetPasswordLiveData.setError(t);
            }
        });

    }
}
