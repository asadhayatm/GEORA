package com.geora.ui.myactivities;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.base.RichMediatorLiveData;
import com.geora.model.FailureResponse;
import com.geora.model.myactivity.MyActivityModel;

public class MyActivityViewModel extends ViewModel {
    private RichMediatorLiveData<MyActivityModel> mActivityLiveData;
    private Observer<FailureResponse> mFailureObserver;
    private Observer<Throwable> mErrorObserver;
    private MutableLiveData<FailureResponse> mValidateLiveData;

    private MyActivityRepo myActivityRepo=new MyActivityRepo();

    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureObserver;

        initLiveData();
    }

    private void initLiveData() {
        if (mActivityLiveData == null)
            mActivityLiveData = new RichMediatorLiveData<MyActivityModel>() {
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


    public RichMediatorLiveData<MyActivityModel> getActivityLiveData() {
        return mActivityLiveData;
    }

    public void gettingMyActivityData(int pageNo)
    {
        myActivityRepo.hitActivityListing(mActivityLiveData, pageNo);
    }

    public MutableLiveData<FailureResponse> getValidationLiveData() {
        return mValidateLiveData;
    }
}
