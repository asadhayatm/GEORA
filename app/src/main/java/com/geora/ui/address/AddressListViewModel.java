package com.geora.ui.address;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.base.RichMediatorLiveData;
import com.geora.model.FailureResponse;
import com.geora.model.ResetPasswordModel;
import com.geora.model.addresslist.AddressListModel;
import com.geora.model.signup.SignupModel;


public class AddressListViewModel extends ViewModel {
    private RichMediatorLiveData<AddressListModel> mAddressListLiveData;
    private RichMediatorLiveData<SignupModel> mDefaultAddressLiveData;
    private RichMediatorLiveData<ResetPasswordModel> mDeleteAddressLiveData;
    private Observer<FailureResponse> mFailureObserver;
    private Observer<Throwable> mErrorObserver;
    private AddressListRepo mAddressListRepo = new AddressListRepo();

    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureObserver;

        initLiveData();
    }

    private void initLiveData() {
        if (mAddressListLiveData == null) {
            mAddressListLiveData = new RichMediatorLiveData<AddressListModel>() {
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
        if (mDeleteAddressLiveData == null) {
            mDeleteAddressLiveData = new RichMediatorLiveData<ResetPasswordModel>() {
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
        if (mDefaultAddressLiveData == null) {
            mDefaultAddressLiveData = new RichMediatorLiveData<SignupModel>() {
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
     * This method gives the  live data object
     *
     * @return {@link #mAddressListLiveData}
     */
    public RichMediatorLiveData<AddressListModel> getAddressListLiveData() {
        return mAddressListLiveData;
    }

    /**
     * This method gives the  live data object
     *
     * @return {@link #mDeleteAddressLiveData}
     */
    public RichMediatorLiveData<ResetPasswordModel> getDeleteAddressLiveData() {
        return mDeleteAddressLiveData;
    }

    /**
     * This method gives the  live data object
     *
     * @return {@link #mDefaultAddressLiveData}
     */
    public RichMediatorLiveData<SignupModel> getDefaultAddressLiveData() {
        return mDefaultAddressLiveData;
    }


    public void hitGetListingApi(boolean isInternet, int limt) {
        if (isInternet) {
            mAddressListRepo.hitAddressListApi(mAddressListLiveData, limt);
        }
    }

    public void hitDeleteAddress(boolean isInternet, String id) {
        if (isInternet) {
            mAddressListRepo.hitDeleteAddressApi(mDeleteAddressLiveData, id);
        }
    }

    public void hitDefaultApi(boolean isInternet, String id) {
        if (isInternet) {
            mAddressListRepo.hitDefaultAddressApi(mDefaultAddressLiveData, id);
        }
    }

}