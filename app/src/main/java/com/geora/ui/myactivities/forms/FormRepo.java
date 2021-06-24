package com.geora.ui.myactivities.forms;

import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.model.FailureResponse;
import com.geora.model.formresponse.FormModel;
import com.geora.model.orderresponse.OrderModel;

public class FormRepo {
    public void hitEventListingApi(final RichMediatorLiveData<FormModel> formsLiveData, int pageNo) {
        DataManager.getInstance().hitEventListingApi(pageNo).enqueue(new NetworkCallback<FormModel>() {
            @Override
            public void onSuccess(FormModel orderModel) {
                formsLiveData.setValue(orderModel);
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
}
