package com.geora.ui.onboard.address;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.base.RichMediatorLiveData;
import com.geora.constants.AppConstants;
import com.geora.model.AddressModel;
import com.geora.model.FailureResponse;
import com.geora.model.createaddress.CreateAddressModel;

public class CreateAddressViewModel extends ViewModel {

    private RichMediatorLiveData<CreateAddressModel> mCreateAddressLiveData;
    private Observer<FailureResponse> mFailureObserver;
    private Observer<Throwable> mErrorObserver;
    private MutableLiveData<FailureResponse> mValidateLiveData;


    private CreateAddressRepo mCreateAddressRepo = new CreateAddressRepo();


    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureObserver;

        initLiveData();
    }

    private void initLiveData() {
        if (mCreateAddressLiveData == null) {
            mCreateAddressLiveData = new RichMediatorLiveData<CreateAddressModel>() {
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

        if (mValidateLiveData == null) {
            mValidateLiveData = new MutableLiveData<>();
        }
    }

    /**
     * This method gives the create Addree live data object to {@link CreateProfileFragment}
     *
     * @return {@link #mCreateAddressLiveData}
     */
    public RichMediatorLiveData<CreateAddressModel> getCreateProfileLivedata() {
        return mCreateAddressLiveData;
    }

    /**
     * Method used to hit Create Address api after checking validations
     *
     * @param addressModel contains all the params of the request
     */
    public void saveAddressButtonClicked(AddressModel addressModel) {
        if (checkValidation(addressModel)) {
            //showProgress
            mCreateAddressRepo.hitSaveAddress(mCreateAddressLiveData, addressModel);
        }
    }

    /**
     * Method to check validation
     *
     * @param addressModel
     * @return
     */
    private boolean checkValidation(AddressModel addressModel) {
        if (addressModel.getLat() == null || addressModel.getLng() == null || addressModel.getFlatNo() == null || addressModel.getFormattedAddress() == null
                || addressModel.getState() == null || addressModel.getStreet() == null || addressModel.getPincode() == null) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.OTP, "All fields are required"));
            return false;
        }

        return true;
    }

    /**
     * This method gives the validation live data object to {@link CreateProfileFragment}
     *
     * @return {@link #mValidateLiveData}
     */
    public MutableLiveData<FailureResponse> getValidationLiveData() {
        return mValidateLiveData;
    }

}
