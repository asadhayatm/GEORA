package com.geora.ui.onboard.address;

import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.AddressModel;
import com.geora.model.FailureResponse;
import com.geora.model.createaddress.CreateAddressModel;
import com.geora.model.otpverification.Data;
import com.geora.model.otpverification.OTPVerificatonModel;
import com.geora.model.request.User;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CreateAddressRepo {
    public void hitSaveAddress(final RichMediatorLiveData<CreateAddressModel> createAddressLiveData, AddressModel addressModel) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppConstantClass.APIConstant.FLATNO, addressModel.getFlatNo());
            jsonObject.put(AppConstantClass.APIConstant.CITY, addressModel.getCity());
            jsonObject.put(AppConstantClass.APIConstant.STATE, addressModel.getState());
            jsonObject.put(AppConstantClass.APIConstant.STREETNAME, addressModel.getStreet());
            jsonObject.put(AppConstantClass.APIConstant.FORMATEDADDRESS, addressModel.getFormattedAddress());
            jsonObject.put(AppConstantClass.APIConstant.LAT, addressModel.getLat());
            jsonObject.put(AppConstantClass.APIConstant.LNG, addressModel.getLng());
            jsonObject.put(AppConstantClass.APIConstant.PINCODE, addressModel.getPincode());
            jsonObject.put(AppConstantClass.APIConstant.ADDRESSTYPE, AppConstantClass.APIConstant.HOME);
            jsonObject.put(AppConstantClass.APIConstant.ACTION, AppConstantClass.APIConstant.SAVE);
            jsonObject.put(AppConstantClass.APIConstant.FULLNAME, DataManager.getInstance().getFullName());
            jsonObject.put(AppConstantClass.APIConstant.COUNTRYCODE, DataManager.getInstance().getCountryCode());
            jsonObject.put(AppConstantClass.APIConstant.MOBILENO, DataManager.getInstance().getMobileNo());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.ADDRESS, jsonObject.toString());
        params.put(AppConstantClass.APIConstant.ISADDRESSEDITTED, 1 + "");

        params.put(AppConstantClass.APIConstant.ALLACTIVE, addressModel.getAll() + "");
        params.put(AppConstantClass.APIConstant.SALEACTIVE, addressModel.getSales() + "");
        params.put(AppConstantClass.APIConstant.PROMOTIONSACTIVE, addressModel.getPromotion() + "");
        params.put(AppConstantClass.APIConstant.FUNDRAISINGACTIVE, addressModel.getFundraising() + "");
        params.put(AppConstantClass.APIConstant.EVENTACTIVE, addressModel.getEvents() + "");


        DataManager.getInstance().hitCreateAddressApi(params).enqueue(new NetworkCallback<CreateAddressModel>() {
            @Override
            public void onSuccess(CreateAddressModel userResponse) {
                DataManager.getInstance().saveCategories(userResponse.getData());
                createAddressLiveData.setValue(userResponse);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                createAddressLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                createAddressLiveData.setError(t);
            }
        });
    }
}
