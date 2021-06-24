package com.geora.ui.about;

import android.util.Log;

import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.model.FailureResponse;
import com.geora.model.feedbackresponse.FeedbackResponse;
import com.geora.model.myactivity.MyActivityModel;

import java.util.HashMap;

import okhttp3.ResponseBody;

public class AboutRepo {
    public void hitFeedbackApi(final RichMediatorLiveData<FeedbackResponse> formsLiveData, HashMap<String, String> params) {
        DataManager.getInstance().hitFeedbackApi(params).enqueue(new NetworkCallback<FeedbackResponse>() {
            @Override
            public void onSuccess(FeedbackResponse feedbackResponse) {
                formsLiveData.setValue(feedbackResponse);
//                Log.d("Log", myActivityModel.toString());
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
    }
}
