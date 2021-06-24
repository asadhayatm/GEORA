package com.geora.ui.home.homelistview;

import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.data.api.ApiCall;
import com.geora.data.api.NetworkListener;
import com.geora.model.ApiFailureResponse;
import com.geora.model.FailureResponse;
import com.geora.model.beaconlist.BeaconListModel;
import com.geora.model.beaconsavedlist.BeaconSaveListModel;
import com.geora.model.commonresponse.CommonResponse;
import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class HomeListViewRepo {
    public String getIsEventActive() {
        return DataManager.getInstance().getEventActive();
    }
    public String getIsSalesActive() {
        return DataManager.getInstance().getSalesActive();
    }
    public String getIsFundRaisingActive() {
        return DataManager.getInstance().getFundRaisingActive();
    }

    public String getIsPromotionActive() {
        return DataManager.getInstance().getPromotionsActive();
    }

    public String getAllStatus() {
        return DataManager.getInstance().getAllActive();
    }

    public void hitBeaconSaveDataApi(HashMap<String, Object> params, RichMediatorLiveData<BeaconSaveListModel> mSaveListLiveData) {
        Call<ResponseBody> call = DataManager.getInstance().hitCampSavedListApi(params);

        ApiCall.getInstance().hitService(call, 1, new NetworkListener() {
            @Override
            public void onSuccess(int responseCode, String response, int requestCode) {
                BeaconSaveListModel beaconListModel = new Gson().fromJson(response, BeaconSaveListModel.class);
                mSaveListLiveData.setValue(beaconListModel);
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
                mSaveListLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                mSaveListLiveData.setError(t);
            }
        });
    }

    public void hitBeaconDeleteItemApi(String id, RichMediatorLiveData<CommonResponse> mDeleteItemLiveData) {
        Call<ResponseBody> call = DataManager.getInstance().hitRemoveSaveListAPI(id);

        ApiCall.getInstance().hitService(call, 1, new NetworkListener() {
            @Override
            public void onSuccess(int responseCode, String response, int requestCode) {
                CommonResponse commonResponse = new Gson().fromJson(response, CommonResponse.class);
                mDeleteItemLiveData.setValue(commonResponse);
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
                mDeleteItemLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                mDeleteItemLiveData.setError(t);
            }
        });
    }
}
