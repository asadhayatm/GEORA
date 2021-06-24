package com.geora.ui.home.changepassword;


import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.model.FailureResponse;
import com.geora.model.commonresponse.CommonResponse;
import com.geora.model.request.User;

public class ChangePasswordRepo {

    /**
     * This method is used to hit the change password api
     *
     * @param changePasswordLiveData live data object
     * @param user                   contains all the request params
     */
    public void changePassword(final RichMediatorLiveData<CommonResponse> changePasswordLiveData, User user) {
        DataManager.getInstance().hitChangePasswordApi(user).enqueue(new NetworkCallback<CommonResponse>() {
            @Override
            public void onSuccess(CommonResponse successResponse) {
                changePasswordLiveData.setValue(successResponse);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                changePasswordLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                changePasswordLiveData.setError(t);
            }
        });
    }
}
