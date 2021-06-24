package com.geora.ui.address;

import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.FailureResponse;
import com.geora.model.ResetPasswordModel;
import com.geora.model.addresslist.AddressListModel;
import com.geora.model.cardlist.CardListModel;
import com.geora.model.signup.SignupModel;

import java.util.HashMap;

public class AddressListRepo {
    public void hitAddressListApi(final RichMediatorLiveData<AddressListModel> mCardListLiveData, int limt) {

        DataManager.getInstance().hitAddressListApi().enqueue(new NetworkCallback<AddressListModel>() {
            @Override
            public void onSuccess(AddressListModel chargerDropModel) {
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

    public void hitDefaultAddressApi(final RichMediatorLiveData<SignupModel> defaultCardLiveData, String id) {

        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.ADDRESSID, id);


        DataManager.getInstance().hitDefaultAddressApi(params).enqueue(new NetworkCallback<SignupModel>() {
            @Override
            public void onSuccess(SignupModel userResponse) {
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

    public void hitDeleteAddressApi(final RichMediatorLiveData<ResetPasswordModel> defaultCardLiveData, String id) {

        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.ADDRESSID, id);


        DataManager.getInstance().hitDeleteAddressApi(params).enqueue(new NetworkCallback<ResetPasswordModel>() {
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
}
