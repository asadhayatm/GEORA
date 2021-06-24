package com.geora.ui.myactivities;

import android.util.Log;

import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.data.api.ApiCall;
import com.geora.data.api.NetworkListener;
import com.geora.model.ApiFailureResponse;
import com.geora.model.FailureResponse;
import com.geora.model.myactivity.MyActivityModel;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class MyActivityRepo {
    public void hitActivityListing(final RichMediatorLiveData<MyActivityModel> formsLiveData, int pageNo) {

        Call<ResponseBody> call = DataManager.getInstance().hitActivityListingApi(pageNo);

        ApiCall.getInstance().hitService(call, 101, new NetworkListener() {
            @Override
            public void onSuccess(int responseCode, String response, int requestCode) {
                MyActivityModel myActivityModel = new Gson().fromJson(response, MyActivityModel.class);
                Log.i("Response:::::::", response);
                formsLiveData.setValue(myActivityModel);
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
                }            }

            @Override
            public void onError(Throwable t) {
                formsLiveData.setError(t);
            }
        });

/*
        DataManager.getInstance().hitActivityListingApi(pageNo).enqueue(new NetworkCallback<MyActivityModel>() {
            @Override
            public void onSuccess(MyActivityModel myActivityModel) {
                Log.i("Response:::::::", new Gson().toJson(myActivityModel));
                formsLiveData.setValue(myActivityModel);
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
*/
    }
}
