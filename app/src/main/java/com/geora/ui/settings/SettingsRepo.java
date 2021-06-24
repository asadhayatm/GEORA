package com.geora.ui.settings;

import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.FailureResponse;
import com.geora.model.signup.SignupModel;

import java.util.HashMap;

public class SettingsRepo {
    public void logoutApi(final RichMediatorLiveData<SignupModel> mResetPasswordLiveData) {
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.CLIENT_ID, DataManager.getInstance().getClientId());

        DataManager.getInstance().hitLogoutAPI(params).enqueue(new NetworkCallback<SignupModel>() {
            @Override
            public void onSuccess(SignupModel userResponse) {
                DataManager.getInstance().clearPreferences();
                mResetPasswordLiveData.setValue(userResponse);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                mResetPasswordLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                mResetPasswordLiveData.setError(t);
            }
        });
    }

    public void UpdateNotification(RichMediatorLiveData<SignupModel> mProfileSettingLiveData, boolean isChecked) {
        hitUpdateProfileSettingApi(mProfileSettingLiveData, isChecked, null);
    }

    private void hitUpdateProfileSettingApi(final RichMediatorLiveData<SignupModel> mProfileSettingLiveData, boolean isChecked, final Integer range) {
        DataManager.getInstance().hitUpdateProfileSettingApi(createPayloadForProfileSetting(isChecked, range)).enqueue(new NetworkCallback<SignupModel>() {
            @Override
            public void onSuccess(SignupModel signupModel) {
                mProfileSettingLiveData.setValue(signupModel);
                if (signupModel.getData().getProximityRange() != null)
                    DataManager.getInstance().setProximityRange(signupModel.getData().getProximityRange());
                else
                    DataManager.getInstance().setNotificationStatus(signupModel.getData().getNotificationSubscriptionStatus());
            }


            @Override
            public void onFailure(FailureResponse failureResponse) {
                mProfileSettingLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                mProfileSettingLiveData.setError(t);
            }
        });
    }

    private HashMap<String, Object> createPayloadForProfileSetting(boolean isChecked, Integer range) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (range != null) {
            hashMap.put(AppConstantClass.APIConstant.PROXIMITYRANGE, range);
            hashMap.put(AppConstantClass.APIConstant.ACTION, 1);

        } else {
            if (isChecked)
                hashMap.put(AppConstantClass.APIConstant.NOTIFICATIONFLAG, AppConstantClass.ENABLE);
            else
                hashMap.put(AppConstantClass.APIConstant.NOTIFICATIONFLAG, AppConstantClass.DISABLE);
            hashMap.put(AppConstantClass.APIConstant.ACTION, 0);

        }
        return hashMap;
    }

    public void updateProfileProximity(RichMediatorLiveData<SignupModel> mProfileSettingLiveData, int proximityRange) {
        hitUpdateProfileSettingApi(mProfileSettingLiveData, false, proximityRange);
    }

    public String getNotificationStatus() {
        return DataManager.getInstance().getNotificationStatus();
    }

    public String getProximityRange() {
        return DataManager.getInstance().getProximityRange();
    }

    public boolean getIsSocailLogin() {
        return DataManager.getInstance().isSocailLogin();
    }
}
