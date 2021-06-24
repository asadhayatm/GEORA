package com.geora.ui.home.detailpage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.base.RichMediatorLiveData;
import com.geora.model.FailureResponse;

import java.util.HashMap;

public class DetailsViewModel extends ViewModel {

    private Observer<Throwable> mErrorObserver;

    private Observer<FailureResponse> mFailureObserver;

    private RichMediatorLiveData<String> mDetailsLiveData;
    private RichMediatorLiveData<String> mReportLiveData;

    private DetailsRepo mRepo=new DetailsRepo();

    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureResponseObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureResponseObserver;

        initLiveData();
    }

    /**
     * Method is used to initialize live data objects
     */
    private void initLiveData() {
        if (mDetailsLiveData == null) {
            mDetailsLiveData = new RichMediatorLiveData<String>() {
                @Override
                protected Observer<FailureResponse> getFailureObserver() {
                    return mFailureObserver;
                }

                @Override
                protected Observer<Throwable> getErrorObserver() {
                    return mErrorObserver;
                }
            };
        }
        if (mReportLiveData == null) {
            mReportLiveData = new RichMediatorLiveData<String>() {
                @Override
                protected Observer<FailureResponse> getFailureObserver() {
                    return mFailureObserver;
                }

                @Override
                protected Observer<Throwable> getErrorObserver() {
                    return mErrorObserver;
                }
            };
        }
    }

    public LiveData<String> getDetailsLiveData() {
        return mDetailsLiveData;
    }

    public LiveData<String> getReportLiveData() {
        return mReportLiveData;
    }

    public void hitDetailsApi(HashMap<String, Object> params) {
        mRepo.hitDetailsApi(params, mDetailsLiveData);
    }

    public void hitReportApi(HashMap<String, Object> params) {
        mRepo.hitReportApi(params, mReportLiveData);
    }
}
