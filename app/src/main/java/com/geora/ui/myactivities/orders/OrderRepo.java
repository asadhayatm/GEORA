package com.geora.ui.myactivities.orders;

import android.util.Log;

import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.model.FailureResponse;
import com.geora.model.myactivity.MyActivityModel;
import com.geora.model.orderresponse.OrderModel;

public class OrderRepo {
    public void hitOrderListing(final RichMediatorLiveData<OrderModel> formsLiveData, int pageNo) {
        DataManager.getInstance().hitOrderListApi(pageNo).enqueue(new NetworkCallback<OrderModel>() {
            @Override
            public void onSuccess(OrderModel orderModel) {
//                Log.d("Log", orderModel.toString());
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
