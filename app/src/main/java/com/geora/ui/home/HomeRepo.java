package com.geora.ui.home;

import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.data.api.ApiCall;
import com.geora.data.api.NetworkListener;
import com.geora.model.ApiFailureResponse;
import com.geora.model.FailureResponse;
import com.geora.model.beaconlist.BeaconListModel;
import com.geora.model.beaconsdata.BeaconsDataList;
import com.geora.model.commonresponse.CommonResponse;
import com.geora.model.otpverification.OTPVerificatonModel;
import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class HomeRepo {

    /**
     * This method is used to hit log out api
     *
     * @param logOutLiveData live data object
     */
    public void userLogOut(final RichMediatorLiveData<CommonResponse> logOutLiveData) {
        DataManager.getInstance().hitLogOutApi().enqueue(new NetworkCallback<CommonResponse>() {
            @Override
            public void onSuccess(CommonResponse successResponse) {
                if (successResponse != null) {
                    logOutLiveData.setValue(successResponse);
                    DataManager.getInstance().clearPreferences();
                }
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                logOutLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                logOutLiveData.setError(t);
            }
        });

    }
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

    /**
     * function to get campaigns
     * @param params
     * @param mCampLiveData
     */
    public void hitGetCampApi(HashMap<String, Object> params, final RichMediatorLiveData<BeaconsDataList> mCampLiveData) {

        Call<ResponseBody> call = DataManager.getInstance().hitGetCampApi(params);

        ApiCall.getInstance().hitService(call, 1, new NetworkListener() {
            @Override
            public void onSuccess(int responseCode, String response, int requestCode) {
                BeaconsDataList beaconsDataList = new Gson().fromJson(response, BeaconsDataList.class);
                mCampLiveData.setValue(beaconsDataList);
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
                mCampLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                mCampLiveData.setError(t);
            }
        });

    }

    /**
     * function to get campaigns
     * @param params
     * @param mCampLiveData
     */
    public void hitSavedCampApi(HashMap<String, Object> params, final RichMediatorLiveData<BeaconsDataList> mCampLiveData) {

        Call<ResponseBody> call = DataManager.getInstance().hitSavedCampApi(params);

        ApiCall.getInstance().hitService(call, 1, new NetworkListener() {
            @Override
            public void onSuccess(int responseCode, String response, int requestCode) {
                BeaconsDataList beaconsDataList = new Gson().fromJson(response, BeaconsDataList.class);
                mCampLiveData.setValue(beaconsDataList);
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
                mCampLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                mCampLiveData.setError(t);
            }
        });

    }

    /**
     * function to get campaigns
     * @param params
     * @param mRecallLiveData
     */
    public void hitRecallApi(HashMap<String, Object> params, final RichMediatorLiveData<BeaconsDataList> mRecallLiveData) {

        Call<ResponseBody> call = DataManager.getInstance().hitRecallApi(params);

        ApiCall.getInstance().hitService(call, 1, new NetworkListener() {
            @Override
            public void onSuccess(int responseCode, String response, int requestCode) {
                BeaconsDataList beaconsDataList = new Gson().fromJson(response, BeaconsDataList.class);
                mRecallLiveData.setValue(beaconsDataList);
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
                mRecallLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                mRecallLiveData.setError(t);
            }
        });

    }

    /**
     * function to get campaigns
     * @param params
     * @param mSwipeLiveData
     */
    public void hitSwipeApi(HashMap<String, Object> params, final RichMediatorLiveData<CommonResponse> mSwipeLiveData) {

        Call<ResponseBody> call = DataManager.getInstance().hitSwipeApi(params);

        ApiCall.getInstance().hitService(call, 1, new NetworkListener() {
            @Override
            public void onSuccess(int responseCode, String response, int requestCode) {
                CommonResponse commonResponse = new Gson().fromJson(response, CommonResponse.class);
                mSwipeLiveData.setValue(commonResponse);
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
                mSwipeLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                mSwipeLiveData.setError(t);
            }
        });

    }

    public void hitBeaconApi(HashMap<String, Object> params, RichMediatorLiveData<BeaconListModel> mBeaconLiveData) {

        Call<ResponseBody> call = DataManager.getInstance().hitAllBeaconsApi(params);

        ApiCall.getInstance().hitService(call, 1, new NetworkListener() {
            @Override
            public void onSuccess(int responseCode, String response, int requestCode) {
                BeaconListModel beaconListModel = new Gson().fromJson(response, BeaconListModel.class);
                mBeaconLiveData.setValue(beaconListModel);
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
                mBeaconLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                mBeaconLiveData.setError(t);
            }
        });
    }
}
