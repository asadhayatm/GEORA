package com.geora.ui.myactivities.fundraised;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.base.RichMediatorLiveData;
import com.geora.model.FailureResponse;
import com.geora.model.fundraisedmodel.FundraisedModel;

public class FundraisedViewModel extends ViewModel {
    private RichMediatorLiveData<FundraisedModel> mOrderLiveData;
    private Observer<FailureResponse> mFailureObserver;
    private Observer<Throwable> mErrorObserver;
    private MutableLiveData<FailureResponse> mValidateLiveData;

    private FundraisedRepo mFundraisedRepo =new FundraisedRepo();

    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureObserver;

        initLiveData();
    }

    private void initLiveData() {
        if (mOrderLiveData == null)
            mOrderLiveData = new RichMediatorLiveData<FundraisedModel>() {
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


    public RichMediatorLiveData<FundraisedModel> getActivityLiveData() {
        return mOrderLiveData;
    }

    public void gettingFundData(int pageNo)
    {
        mFundraisedRepo.hitFundListingApi(mOrderLiveData, pageNo);
    }

    public MutableLiveData<FailureResponse> getValidationLiveData() {
        return mValidateLiveData;
    }
}
