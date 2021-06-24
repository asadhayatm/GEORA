package com.geora.ui.businessuser;

import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.model.FailureResponse;
import com.geora.model.businessuserresponse.BusinessUserResponse;
import com.geora.model.feedbackresponse.FeedbackResponse;

import java.util.HashMap;

public class BusinessUserRepo {
    public void hitBusinessUserApi(final RichMediatorLiveData<BusinessUserResponse> formsLiveData, HashMap<String, String> params) {
        DataManager.getInstance().hitBusinessUserApi(params).enqueue(new NetworkCallback<BusinessUserResponse>() {
            @Override
            public void onSuccess(BusinessUserResponse businessUserResponse) {
                formsLiveData.setValue(businessUserResponse);
//                Log.d("Log", myActivityModel.toString());
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                formsLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                formsLiveData.setError(t);
            }
        });
    }

    public void setStatus(Integer isBusinessUser) {
        DataManager.getInstance().saveBusinessUserStatus(isBusinessUser);
    }
}
