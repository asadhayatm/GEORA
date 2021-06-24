package com.geora.ui.myactivities.forms;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.base.RichMediatorLiveData;
import com.geora.model.FailureResponse;
import com.geora.model.formresponse.FormModel;

public class FormViewModel extends ViewModel {
    private RichMediatorLiveData<FormModel> mOrderLiveData;
    private Observer<FailureResponse> mFailureObserver;
    private Observer<Throwable> mErrorObserver;
    private MutableLiveData<FailureResponse> mValidateLiveData;

    private FormRepo mFundraisedRepo =new FormRepo();

    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureObserver;

        initLiveData();
    }

    private void initLiveData() {
        if (mOrderLiveData == null)
            mOrderLiveData = new RichMediatorLiveData<FormModel>() {
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


    public RichMediatorLiveData<FormModel> getActivityLiveData() {
        return mOrderLiveData;
    }

    public void gettingFormData(int pageNo)
    {
        mFundraisedRepo.hitEventListingApi(mOrderLiveData, pageNo);
    }

    public MutableLiveData<FailureResponse> getValidationLiveData() {
        return mValidateLiveData;
    }
}
