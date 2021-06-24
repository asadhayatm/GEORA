package com.geora.ui.addaddress;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.R;
import com.geora.base.RichMediatorLiveData;
import com.geora.constants.AppConstants;
import com.geora.model.AddressModel;
import com.geora.model.FailureResponse;
import com.geora.model.addaddress.AddAddressModel;
import com.geora.ui.addcard.AddCardActivity;
import com.geora.util.ResourceUtils;

public class AddAddressViewModel extends ViewModel {
    private RichMediatorLiveData<AddAddressModel> mAddAddressModelLiveData;
    private Observer<FailureResponse> mFailureObserver;
    private Observer<Throwable> mErrorObserver;
    private MutableLiveData<FailureResponse> mValidateLiveData;
    private AddAddressRepo addAddressRepo = new AddAddressRepo();

    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureObserver;

        initLiveData();
    }

    private void initLiveData() {
        if (mAddAddressModelLiveData == null) {
            mAddAddressModelLiveData = new RichMediatorLiveData<AddAddressModel>() {
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
     * This method gives the live data object
     *
     * @return {@link #mAddAddressModelLiveData}
     */
    public RichMediatorLiveData<AddAddressModel> getAddAddressModelLiveData() {
        return mAddAddressModelLiveData;
    }


    /**
     * Method to check validation
     *
     * @param addressModel
     * @return
     */
    private boolean checkValidation(AddressModel addressModel, boolean isInternetAvailable) {
        if (addressModel.getFlatNo() == null || addressModel.getFlatNo().isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.FLATNO, ResourceUtils.getInstance().getString(R.string.enter_flat_no)));
            return false;
        } else if (addressModel.getCity() == null || addressModel.getCity().isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.CITY, ResourceUtils.getInstance().getString(R.string.enter_city)));
            return false;
        } else if (addressModel.getCountryCode().isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.CODE_EMPTY, ResourceUtils.getInstance().getString(R.string.select_country)));
            return false;
        } else if (addressModel.getName().isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.NAME_EMPTY, ResourceUtils.getInstance().getString(R.string.enter_name)));
            return false;
        } else if (addressModel.getMobileNo().isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.PHONE_EMPTY, ResourceUtils.getInstance().getString(R.string.enter_phone)));
            return false;
        } else if (addressModel.getAddressType() == null || addressModel.getAddressType().isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.ADDRESSTYPE, ResourceUtils.getInstance().getString(R.string.select_address_type)));
            return false;
        } else if (addressModel.getState() == null || addressModel.getState().isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.STATE, ResourceUtils.getInstance().getString(R.string.enter_state)));
            return false;
        }
        return true;
    }


    /**
     * This method gives the validation live data object to {@link AddCardActivity}
     *
     * @return {@link #mValidateLiveData}
     */
    public MutableLiveData<FailureResponse> getValidationLiveData() {
        return mValidateLiveData;
    }

    public void submutClicked(AddressModel addressModel, boolean internetAvailable) {
        if (checkValidation(addressModel, internetAvailable)) {
            addAddressRepo.hitAddAddressApi(mAddAddressModelLiveData, addressModel);
        }
    }
}
