package com.geora.ui.notification;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.base.RichMediatorLiveData;
import com.geora.model.FailureResponse;
import com.geora.model.commonresponse.CommonResponse;
import com.geora.model.notificationresponse.NotificationResponse;

public class NotificationViewModel extends ViewModel {
    private RichMediatorLiveData<NotificationResponse> mNotificationLiveData;
    private RichMediatorLiveData<CommonResponse> mClearListLiveData;
    private Observer<FailureResponse> mFailureObserver;
    private Observer<Throwable> mErrorObserver;
    private MutableLiveData<FailureResponse> mValidateLiveData;

    private NotificationRepo mNotificationRepo = new NotificationRepo();

    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureObserver;

        initLiveData();
    }

    private void initLiveData() {
        if (mNotificationLiveData == null)
            mNotificationLiveData = new RichMediatorLiveData<NotificationResponse>() {
                @Override
                protected Observer<FailureResponse> getFailureObserver() {
                    return mFailureObserver;
                }

                @Override
                protected Observer<Throwable> getErrorObserver() {
                    return mErrorObserver;
                }
            };

        if (mClearListLiveData == null)
            mClearListLiveData = new RichMediatorLiveData<CommonResponse>() {
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


    public RichMediatorLiveData<NotificationResponse> getActivityLiveData() {
        return mNotificationLiveData;
    }

    public RichMediatorLiveData<CommonResponse> getClearListLiveData() {
        return mClearListLiveData;
    }

    public void gettingMyActivityData(int pageNo)
    {
        mNotificationRepo.hitNotificationListingAPI(mNotificationLiveData, pageNo);
    }

    public void hitClearListData()
    {
        mNotificationRepo.hitClearListingAPI(mClearListLiveData);
    }

    public MutableLiveData<FailureResponse> getValidationLiveData() {
        return mValidateLiveData;
    }
}
