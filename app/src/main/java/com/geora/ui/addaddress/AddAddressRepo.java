package com.geora.ui.addaddress;

import android.util.Log;

import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.AddressModel;
import com.geora.model.FailureResponse;
import com.geora.model.addaddress.AddAddressModel;
import com.geora.model.addresslist.AddressListModel;
import com.geora.model.cardlist.CardListModel;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.util.HashMap;

import static com.geora.GeoraClass.getContext;


public class AddAddressRepo {
    public void hitAddAddressApi(final RichMediatorLiveData<AddAddressModel> addAddressModelLiveData, AddressModel addressModel) {
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.FLATNO, addressModel.getFlatNo());
        params.put(AppConstantClass.APIConstant.STREETNAME, addressModel.getStreet() + "");
        params.put(AppConstantClass.APIConstant.STATE, addressModel.getState() + "");
        params.put(AppConstantClass.APIConstant.CITY, addressModel.getCity() + "");
        params.put(AppConstantClass.APIConstant.PINCODE, addressModel.getPincode() + "");
        params.put(AppConstantClass.APIConstant.LAT, addressModel.getLat() + "");
        params.put(AppConstantClass.APIConstant.LNG, addressModel.getLng() + "");
        params.put(AppConstantClass.APIConstant.FULLNAME, addressModel.getName() + "");
        params.put(AppConstantClass.APIConstant.COUNTRYCODE, addressModel.getCountryCode() + "");
        params.put(AppConstantClass.APIConstant.MOBILENO, addressModel.getMobileNo() + "");
        params.put(AppConstantClass.APIConstant.ADDRESSTYPE, addressModel.getAddressType() + "");
        params.put(AppConstantClass.APIConstant.ACTION, addressModel.getAction() + "");
        if (addressModel.getFormattedAddress() == null || addressModel.getFormattedAddress().isEmpty())
            params.put(AppConstantClass.APIConstant.FORMATEDADDRESS, addressModel.getFlatNo() + "");
        else
            params.put(AppConstantClass.APIConstant.FORMATEDADDRESS, addressModel.getFormattedAddress() + "");
        if (addressModel.getAddressId() != null)
            params.put(AppConstantClass.APIConstant.ADDRESSID, addressModel.getAddressId() + "");
        hitAddAddressAPI(addAddressModelLiveData, params);
    }

    private void hitAddAddressAPI(final RichMediatorLiveData<AddAddressModel> addAddressModelLiveData, HashMap<String, String> params) {

        DataManager.getInstance().hitAddAddressApi(params).enqueue(new NetworkCallback<AddAddressModel>() {
            @Override
            public void onSuccess(AddAddressModel userResponse) {
                //save data in preference
                addAddressModelLiveData.setValue(userResponse);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                addAddressModelLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                addAddressModelLiveData.setError(t);
            }
        });
    }

}
