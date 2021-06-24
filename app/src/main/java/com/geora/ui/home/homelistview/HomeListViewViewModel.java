package com.geora.ui.home.homelistview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.base.RichMediatorLiveData;
import com.geora.model.FailureResponse;
import com.geora.model.beaconsavedlist.BeaconSaveListModel;
import com.geora.model.commonresponse.CommonResponse;

import java.util.HashMap;

public class HomeListViewViewModel extends ViewModel {

    private Observer<Throwable> mErrorObserver;

    private Observer<FailureResponse> mFailureObserver;

    private RichMediatorLiveData<BeaconSaveListModel> mSaveListLiveData;
    private RichMediatorLiveData<CommonResponse> mDeleteItemLiveData;

    private HomeListViewRepo mRepo=new HomeListViewRepo();

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
        if (mSaveListLiveData == null) {
            mSaveListLiveData = new RichMediatorLiveData<BeaconSaveListModel>() {
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
        if (mDeleteItemLiveData == null) {
            mDeleteItemLiveData = new RichMediatorLiveData<CommonResponse>() {
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

    public String getEventStatus() {
        return mRepo.getIsEventActive();
    }

    public String getSalesStatus() {
        return mRepo.getIsSalesActive();
    }
    public String getPromotionStatus() {
        return mRepo.getIsPromotionActive();
    }
    public String getFundRaisingStatus() {
        return mRepo.getIsFundRaisingActive();
    }

    public String getAllStatus() {
        return mRepo.getAllStatus();
    }

    public LiveData<BeaconSaveListModel> getBeaconSaveListLiveData() {
        return mSaveListLiveData;
    }

    public LiveData<CommonResponse> getBeaconDeleteItemLiveData() {
        return mDeleteItemLiveData;
    }

    public void hitBeaconSaveDataApi(HashMap<String, Object> params) {
        mRepo.hitBeaconSaveDataApi(params, mSaveListLiveData);
    }

    public void hitBeaconDeleteItemApi(String id) {
        mRepo.hitBeaconDeleteItemApi(id, mDeleteItemLiveData);
    }
}
