package com.geora.ui.home;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.base.RichMediatorLiveData;
import com.geora.model.FailureResponse;
import com.geora.model.beaconlist.BeaconListModel;
import com.geora.model.beaconsdata.BeaconsDataList;
import com.geora.model.commonresponse.CommonResponse;

import java.util.HashMap;

public class HomeViewModel extends ViewModel {

    private Observer<Throwable> mErrorObserver;

    private Observer<FailureResponse> mFailureObserver;

    private RichMediatorLiveData<CommonResponse> mLogOutLiveData;
    private RichMediatorLiveData<BeaconsDataList> mCampLiveData;
    private RichMediatorLiveData<BeaconsDataList> mRecallLiveData;
    private RichMediatorLiveData<CommonResponse> mSwipeLiveData;
    private RichMediatorLiveData<BeaconListModel> mBeaconLiveData;

    //Initializing repository class
    private HomeRepo mHomeRepo = new HomeRepo();

    //saving error & failure observers instance
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
        if (mLogOutLiveData == null) {
            mLogOutLiveData = new RichMediatorLiveData<CommonResponse>() {
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
        if (mCampLiveData == null) {
            mCampLiveData = new RichMediatorLiveData<BeaconsDataList>() {
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
        if (mRecallLiveData == null) {
            mRecallLiveData = new RichMediatorLiveData<BeaconsDataList>() {
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
        if (mSwipeLiveData == null) {
            mSwipeLiveData = new RichMediatorLiveData<CommonResponse>() {
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
        if (mBeaconLiveData == null) {
            mBeaconLiveData = new RichMediatorLiveData<BeaconListModel>() {
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

    /**
     * This method gives the log out live data object to {@link HomeFragment}
     *
     * @return {@link #mLogOutLiveData}
     */
    public RichMediatorLiveData<CommonResponse> getLogOutLiveData() {
        return mLogOutLiveData;
    }

    public RichMediatorLiveData<BeaconsDataList> getCampLiveData() {
        return mCampLiveData;
    }

    public RichMediatorLiveData<BeaconsDataList> getRecallLiveData() {
        return mRecallLiveData;
    }

    public RichMediatorLiveData<CommonResponse> getSwipeLiveData() {
        return mSwipeLiveData;
    }

    public RichMediatorLiveData<BeaconListModel> getBeaconLiveData() {
        return mBeaconLiveData;
    }

    /**
     * This method is used on the click of log out button
     */
    public void loginButtonClicked() {
        mHomeRepo.userLogOut(mLogOutLiveData);
    }

    public String getEventStatus() {
        return mHomeRepo.getIsEventActive();
    }

    public String getSalesStatus() {
        return mHomeRepo.getIsSalesActive();
    }
    public String getPromotionStatus() {
        return mHomeRepo.getIsPromotionActive();
    }
    public String getFundRaisingStatus() {
        return mHomeRepo.getIsFundRaisingActive();
    }

    public String getAllStatus() {
        return mHomeRepo.getAllStatus();
    }

    /**
     * function to get campaigns
     * @param params
     */
    public void hitGetCampApi(HashMap<String, Object> params) {
        mHomeRepo.hitGetCampApi(params, mCampLiveData);
    }
    /**
     * function to get campaigns
     * @param params
     */
    public void hitSavedCampApi(HashMap<String, Object> params) {
        mHomeRepo.hitSavedCampApi(params, mCampLiveData);
    }
    /**
     * function to get campaigns
     * @param params
     */
    public void hitRecallApi(HashMap<String, Object> params) {
        mHomeRepo.hitRecallApi(params, mRecallLiveData);
    }
    /**
     * function to get campaigns
     * @param params
     */
    public void hitSwipeApi(HashMap<String, Object> params) {
        mHomeRepo.hitSwipeApi(params, mSwipeLiveData);
    }
    /**
     * function to get campaigns
     * @param params
     */
    public void hitBeaconApi(HashMap<String, Object> params) {
        mHomeRepo.hitBeaconApi(params, mBeaconLiveData);
    }
}
