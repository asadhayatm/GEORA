package com.geora.ui.about;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.base.RichMediatorLiveData;
import com.geora.model.FailureResponse;
import com.geora.model.feedbackresponse.FeedbackResponse;

import java.util.HashMap;

public class AboutViewModel extends ViewModel {
    private RichMediatorLiveData<FeedbackResponse> mFeedbackLiveData;
    private Observer<FailureResponse> mFailureObserver;
    private Observer<Throwable> mErrorObserver;
    private MutableLiveData<FailureResponse> mValidateLiveData;

    private AboutRepo mAboutRepo =new AboutRepo();

    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureObserver;

        initLiveData();
    }

    private void initLiveData() {
        if (mFeedbackLiveData == null)
            mFeedbackLiveData = new RichMediatorLiveData<FeedbackResponse>() {
                @Override
                protected Observer<FailureResponse> getFailureObserver() {
                    return mFailureObserver;
                }

                @Override
                protected Observer<Throwable> getErrorObserver() {
                    return mErrorObserver;
                }
            };

        if (mValidateLiveData == null)
            mValidateLiveData = new MutableLiveData<>();
    }


    public RichMediatorLiveData<FeedbackResponse> getFeedbackLiveData() {
        return mFeedbackLiveData;
    }

    public void hitFeedbackApi(HashMap<String, String> params)
    {
        mAboutRepo.hitFeedbackApi(mFeedbackLiveData, params);
    }

    public MutableLiveData<FailureResponse> getValidationLiveData() {
        return mValidateLiveData;
    }
}
