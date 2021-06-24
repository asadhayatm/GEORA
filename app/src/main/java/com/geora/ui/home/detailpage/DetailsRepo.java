package com.geora.ui.home.detailpage;

import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.data.api.ApiCall;
import com.geora.data.api.NetworkListener;
import com.geora.model.ApiFailureResponse;
import com.geora.model.FailureResponse;
import com.geora.model.beaconsavedlist.BeaconSaveListModel;
import com.geora.model.commonresponse.CommonResponse;
import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class DetailsRepo {

    public void hitDetailsApi(HashMap<String, Object> params, RichMediatorLiveData<String> mDetailsLiveData) {
        Call<ResponseBody> call = DataManager.getInstance().hitCampaignDetailApi(params);

        ApiCall.getInstance().hitService(call, 1, new NetworkListener() {
            @Override
            public void onSuccess(int responseCode, String response, int requestCode) {
                mDetailsLiveData.setValue(response);
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
                mDetailsLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                mDetailsLiveData.setError(t);
            }
        });
    }

    public void hitReportApi(HashMap<String, Object> params, RichMediatorLiveData<String> mReportLiveData) {
        Call<ResponseBody> call = DataManager.getInstance().hitCampaignReportApi(params);

        ApiCall.getInstance().hitService(call, 1, new NetworkListener() {
            @Override
            public void onSuccess(int responseCode, String response, int requestCode) {
                mReportLiveData.setValue(response);
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
                mReportLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                mReportLiveData.setError(t);
            }
        });
    }
}
