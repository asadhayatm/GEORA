package com.geora.ui.home.detailpage.rsvp;

import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.FailureResponse;
import com.geora.model.changred.FundraisedChangeModel;

import java.util.HashMap;

public class RSVPRepo {
    public void hitSendFormDataAPI(final RichMediatorLiveData<FundraisedChangeModel> fundRaisedChangedLiveData, String data, String id, String busnissid, int campId) {
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.BUSNISS_USER_IS, busnissid);
        params.put(AppConstantClass.APIConstant.EVENTID, id);
        params.put(AppConstantClass.DATA, data);
        params.put(AppConstantClass.APIConstant.CAMP_ID,String.valueOf(campId));


        DataManager.getInstance().hitFormDAtaAPI(params).enqueue(new NetworkCallback<FundraisedChangeModel>() {
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
}
