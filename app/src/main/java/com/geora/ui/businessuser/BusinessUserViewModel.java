package com.geora.ui.businessuser;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.base.RichMediatorLiveData;
import com.geora.model.FailureResponse;
import com.geora.model.businessuserresponse.BusinessUserResponse;

import java.util.HashMap;

public class BusinessUserViewModel extends ViewModel {
    private RichMediatorLiveData<BusinessUserResponse> mBusinessUserLiveData;
    private Observer<FailureResponse> mFailureObserver;
    private Observer<Throwable> mErrorObserver;
    private MutableLiveData<FailureResponse> mValidateLiveData;

    private BusinessUserRepo mBusinessUserRepo =new BusinessUserRepo();

    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureObserver;

        initLiveData();
    }

    private void initLiveData() {
        if (mBusinessUserLiveData == null)
            mBusinessUserLiveData = new RichMediatorLiveData<BusinessUserResponse>() {
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


    public RichMediatorLiveData<BusinessUserResponse> getFeedbackLiveData() {
        return mBusinessUserLiveData;
    }

    public void hitBusinessUserApi(HashMap<String, String> params)
    {
        mBusinessUserRepo.hitBusinessUserApi(mBusinessUserLiveData, params);
    }

    public MutableLiveData<FailureResponse> getValidationLiveData() {
        return mValidateLiveData;
    }

    public void setStatus(Integer isBusinessUser) {
        mBusinessUserRepo.setStatus(isBusinessUser);
    }
}
