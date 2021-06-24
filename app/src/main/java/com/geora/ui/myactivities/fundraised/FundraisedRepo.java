package com.geora.ui.myactivities.fundraised;

import android.util.Log;

import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.model.FailureResponse;
import com.geora.model.fundraisedmodel.FundraisedModel;
import com.geora.model.orderresponse.OrderModel;
import com.google.gson.Gson;

public class FundraisedRepo {
    public void hitFundListingApi(final RichMediatorLiveData<FundraisedModel> formsLiveData, int pageNo) {
        DataManager.getInstance().hitFundListingApi(pageNo).enqueue(new NetworkCallback<FundraisedModel>() {
            @Override
            public void onSuccess(FundraisedModel orderModel) {
                Log.i("Response:::::::", new Gson().toJson(orderModel));
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
