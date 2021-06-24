package com.geora.ui.notification;

import android.util.Log;

import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.data.api.ApiCall;
import com.geora.data.api.NetworkListener;
import com.geora.model.ApiFailureResponse;
import com.geora.model.FailureResponse;
import com.geora.model.commonresponse.CommonResponse;
import com.geora.model.notificationresponse.NotificationResponse;
import com.geora.model.orderresponse.OrderModel;
import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class NotificationRepo {

    public void hitNotificationListingAPI(final RichMediatorLiveData<NotificationResponse> formsLiveData, int pageNo) {

        HashMap<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(pageNo));

        Call<ResponseBody> call = DataManager.getInstance().hitNotificationListingAPI(params);

        ApiCall.getInstance().hitService(call, 1, new NetworkListener() {
            @Override
            public void onSuccess(int responseCode, String response, int requestCode) {
                Log.d("Log", response);
                NotificationResponse notificationResponse = new Gson().fromJson(response, NotificationResponse.class);
                formsLiveData.setValue(notificationResponse);
            }

            @Override
            public void onFailure(String response, int requestCode) {
                FailureResponse failureResponse = new FailureResponse();
                try {
                    ApiFailureResponse apiFailureResponse = new Gson().fromJson(response, ApiFailureResponse.class);
                    if (apiFailureResponse.getCode() == 0) {
                        failureResponse = new Gson().fromJson(response, FailureResponse.class);
                    }else {
                        failureResponse.setErrorCode(apiFailureResponse.getCode());
                        failureResponse.setErrorMessage(apiFailureResponse.getMessage());
                        failureResponse.setData(apiFailureResponse.getData());
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    failureResponse = new Gson().fromJson(response, FailureResponse.class);
                }
                formsLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                formsLiveData.setError(t);
            }
        });
    }

    public void hitClearListingAPI(final RichMediatorLiveData<CommonResponse> formsLiveData) {

        HashMap<String, String> params = new HashMap<>();

        Call<ResponseBody> call = DataManager.getInstance().hitClearListingAPI(params);

        ApiCall.getInstance().hitService(call, 1, new NetworkListener() {
            @Override
            public void onSuccess(int responseCode, String response, int requestCode) {
                Log.d("Log", response);
                CommonResponse commonResponse = new Gson().fromJson(response, CommonResponse.class);
                formsLiveData.setValue(commonResponse);
            }

            @Override
            public void onFailure(String response, int requestCode) {
                FailureResponse failureResponse = new FailureResponse();
                try {
                    ApiFailureResponse apiFailureResponse = new Gson().fromJson(response, ApiFailureResponse.class);
                    if (apiFailureResponse.getCode() == 0) {
                        failureResponse = new Gson().fromJson(response, FailureResponse.class);
                    }else {
                        failureResponse.setErrorCode(apiFailureResponse.getCode());
                        failureResponse.setErrorMessage(apiFailureResponse.getMessage());
                        failureResponse.setData(apiFailureResponse.getData());
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    failureResponse = new Gson().fromJson(response, FailureResponse.class);
                }
                formsLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                formsLiveData.setError(t);
            }
        });
    }
}
