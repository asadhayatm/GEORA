package com.geora.ui.changepassword;

import android.util.Log;

import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.FailureResponse;
import com.geora.model.ResetPasswordModel;
import com.geora.model.changepassword.ChangePasswordResponse;
import com.geora.model.editprofile.EditProfileResponse;

import java.util.HashMap;

public class ChangePasswordRepo {
    public void hitChangePasswordApi(final RichMediatorLiveData<ResetPasswordModel> mChangePasswordLiveData, String oldPassword, String newPassword) {
        DataManager.getInstance().hitProfilePasswordChangeApi(createChangePasswordPayload(oldPassword,newPassword)).enqueue(new NetworkCallback<ResetPasswordModel>() {
            @Override
            public void onSuccess(ResetPasswordModel changePasswordResponse) {
               mChangePasswordLiveData.setValue(changePasswordResponse);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                mChangePasswordLiveData.setFailure(failureResponse);


            }

            @Override
            public void onError(Throwable t) {
              mChangePasswordLiveData.setError(t);
            }
        });
    }

    private  HashMap<String,String> createChangePasswordPayload(String oldPassword, String newPassword) {
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put(AppConstantClass.APIConstant.OLDPASSWORD,oldPassword);
        hashMap.put(AppConstantClass.APIConstant.NEWPASSWORD,newPassword);
        return hashMap;

    }

    public void hitUpdateEmailAddressApi(final RichMediatorLiveData<ResetPasswordModel> mChangePasswordLiveData, String email) {
        DataManager.getInstance().hitResendEmailApi(createChangeEmailPayload(email)).enqueue(new NetworkCallback<ResetPasswordModel>() {
            @Override
            public void onSuccess(ResetPasswordModel resetPasswordModel) {
                mChangePasswordLiveData.setValue(resetPasswordModel);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
             mChangePasswordLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
            mChangePasswordLiveData.setError(t);
            }
        });
    }

    private HashMap<String,String> createChangeEmailPayload(String email) {
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put(AppConstantClass.APIConstant.ACTIONTYPE,AppConstantClass.CHANGE);
        hashMap.put(AppConstantClass.APIConstant.EMAIL,email);
        return hashMap;
    }
}
