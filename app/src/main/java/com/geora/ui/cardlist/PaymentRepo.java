package com.geora.ui.cardlist;

import android.util.Log;

import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.data.api.ApiCall;
import com.geora.data.api.NetworkListener;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.ApiFailureResponse;
import com.geora.model.ChargeAmountModel;
import com.geora.model.FailureResponse;
import com.geora.model.ResetPasswordModel;
import com.geora.model.cardlist.CardListModel;
import com.geora.model.changred.FundraisedChangeModel;
import com.geora.model.chargeresponse.ChargeResponse;
import com.geora.model.otpverification.OTPVerificatonModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class PaymentRepo {
    public void hitCardList(final RichMediatorLiveData<CardListModel> mCardListLiveData, int limt) {

        DataManager.getInstance().hitCardListAPI().enqueue(new NetworkCallback<CardListModel>() {
            @Override
            public void onSuccess(CardListModel chargerDropModel) {
                //save data in preference
                mCardListLiveData.setValue(chargerDropModel);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                mCardListLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                mCardListLiveData.setError(t);
            }
        });
    }

    public void hitDefaultCardApi(final RichMediatorLiveData<ResetPasswordModel> defaultCardLiveData, String id) {

        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.CARDID, id);


        DataManager.getInstance().hitDefaultCardApi(params).enqueue(new NetworkCallback<ResetPasswordModel>() {
            @Override
            public void onSuccess(ResetPasswordModel userResponse) {
                //save data in preference
                defaultCardLiveData.setValue(userResponse);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                defaultCardLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                defaultCardLiveData.setError(t);
            }
        });
    }

    public void hitDeleteCardApi(final RichMediatorLiveData<ResetPasswordModel> defaultCardLiveData, String id) {

        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.CARDID, id);


        DataManager.getInstance().hitDeleteCardApi(params).enqueue(new NetworkCallback<ResetPasswordModel>() {
            @Override
            public void onSuccess(ResetPasswordModel userResponse) {
                //save data in preference
                defaultCardLiveData.setValue(userResponse);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                defaultCardLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                defaultCardLiveData.setError(t);
            }
        });
    }

    public void hitChargeApiForFund(final RichMediatorLiveData<FundraisedChangeModel> fundRaisedChangedLiveData, ChargeAmountModel chargeAmountModel) {
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.BUSNISS_USER_IS, chargeAmountModel.getBusnissUserId());
        params.put(AppConstantClass.APIConstant.FUND_ID, chargeAmountModel.getFundId());
        params.put(AppConstantClass.APIConstant.AMOUNT, chargeAmountModel.getAmount());


        DataManager.getInstance().hitFundRaisedAPI(params).enqueue(new NetworkCallback<FundraisedChangeModel>() {
            @Override
            public void onSuccess(FundraisedChangeModel userResponse) {
                //save data in preference
                fundRaisedChangedLiveData.setValue(userResponse);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                fundRaisedChangedLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                fundRaisedChangedLiveData.setError(t);
            }
        });
    }

    public void hitChargeSaleAPI(final RichMediatorLiveData<ChargeResponse> fundRaisedChangedLiveData, ChargeAmountModel chargeAmountModel) {
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.BUSNISS_USER_IS, chargeAmountModel.getBusnissUserId());
        params.put(AppConstantClass.APIConstant.AMOUNT, chargeAmountModel.getAmount());
        params.put(AppConstantClass.APIConstant.SALEID, chargeAmountModel.getSaleId());
        if(chargeAmountModel.getColorId() == null){
            params.put(AppConstantClass.APIConstant.COLORID, "0");
        }else {
            params.put(AppConstantClass.APIConstant.COLORID, chargeAmountModel.getColorId());
        }
        params.put(AppConstantClass.APIConstant.ADDRESS_ID, chargeAmountModel.getAddressId());
        params.put(AppConstantClass.APIConstant.QTY, chargeAmountModel.getQty());
        if(chargeAmountModel.getSizeId() == null){
            params.put(AppConstantClass.APIConstant.SIZEID, "0");
        }else {
            params.put(AppConstantClass.APIConstant.SIZEID, chargeAmountModel.getSizeId());
        }

        Call<ResponseBody> call = DataManager.getInstance().hitChargeSaleAPI(params);
        ApiCall.getInstance().hitService(call, 1, new NetworkListener() {
            @Override
            public void onSuccess(int responseCode, String response, int requestCode) {
                ChargeResponse chargeResponse = new Gson().fromJson(response, ChargeResponse.class);
                fundRaisedChangedLiveData.setValue(chargeResponse);
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
                fundRaisedChangedLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                fundRaisedChangedLiveData.setError(t);
            }
        });

    }
}
