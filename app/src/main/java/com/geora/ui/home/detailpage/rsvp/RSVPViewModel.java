package com.geora.ui.home.detailpage.rsvp;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.base.RichMediatorLiveData;
import com.geora.model.FailureResponse;
import com.geora.model.changred.FundraisedChangeModel;

public class RSVPViewModel extends ViewModel {
    private RichMediatorLiveData<FundraisedChangeModel> mFundRaisedChangedLiveData;
    private Observer<FailureResponse> mFailureObserver;
    private Observer<Throwable> mErrorObserver;
    private RSVPRepo mRsvpRepo = new RSVPRepo();

    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureObserver;

        initLiveData();
    }

    private void initLiveData() {
        if (mFundRaisedChangedLiveData == null) {
            mFundRaisedChangedLiveData = new RichMediatorLiveData<FundraisedChangeModel>() {
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

    public RichMediatorLiveData<FundraisedChangeModel> getFundRaisedCharhedLiveData() {
        return mFundRaisedChangedLiveData;
    }


    public void hitRSVPData(String data, String id, String busnissid, int campId) {
        mRsvpRepo.hitSendFormDataAPI(mFundRaisedChangedLiveData, data, id, busnissid, campId);

    }
}
