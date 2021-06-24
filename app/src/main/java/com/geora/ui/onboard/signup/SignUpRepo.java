package com.geora.ui.onboard.signup;

import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.FailureResponse;
import com.geora.model.request.User;
import com.geora.model.request.UserResponse;
import com.geora.model.signup.Data;
import com.geora.model.signup.SignupModel;

import java.util.HashMap;

import static com.geora.util.DateTimeUtils.bdayToTimeStamp;

public class SignUpRepo {

    public void hitSignUpApi(final RichMediatorLiveData<SignupModel> signUpLiveData, User user) {

        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.EMAIL, user.getEmail());
        params.put(AppConstantClass.APIConstant.NAME, user.getFullName());
        params.put(AppConstantClass.APIConstant.METHOD, user.getMethod());
        params.put(AppConstantClass.APIConstant.PASSWORD, user.getPassword());
        params.put(AppConstantClass.APIConstant.COUNTRYCODE, user.getCountryCode());
        params.put(AppConstantClass.APIConstant.PHONE, user.getPhone());
        params.put(AppConstantClass.APIConstant.DEVICETYPE, "android");
        params.put(AppConstantClass.APIConstant.TYPE, user.getType());
        params.put(AppConstantClass.APIConstant.IMAGE, user.getImage());
        params.put(AppConstantClass.APIConstant.DOB, bdayToTimeStamp(user.getDob()) + "");


        DataManager.getInstance().hitSignUpApi(params).enqueue(new NetworkCallback<SignupModel>() {
            @Override
            public void onSuccess(SignupModel userResponse) {
                //save data in preference
                DataManager.getInstance().saveBusinessUserStatus(userResponse.getData().isBusinessUser());
                saveUserToPreference(userResponse.getData());
                signUpLiveData.setValue(userResponse);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                signUpLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                signUpLiveData.setError(t);
            }
        });

    }

    private void saveUserToPreference(Data user) {
        if (user != null) {
            DataManager.getInstance().saveToken(user.getToken());
//            DataManager.getInstance().saveRefreshToken(user.getRefreshToken());
//            DataManager.getInstance().saveUserDetails(user);
        }
    }
}
