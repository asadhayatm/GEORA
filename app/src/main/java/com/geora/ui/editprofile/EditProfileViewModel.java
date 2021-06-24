package com.geora.ui.editprofile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.R;
import com.geora.base.RichMediatorLiveData;
import com.geora.constants.AppConstants;
import com.geora.model.FailureResponse;
import com.geora.model.editprofile.EditProfileResponse;
import com.geora.model.request.User;
import com.geora.util.ResourceUtils;

public class EditProfileViewModel extends ViewModel {
    private Observer<FailureResponse> mFailureObserver;

    private Observer<Throwable> mErrorObserver;

    private RichMediatorLiveData<EditProfileResponse> mEditProfileLiveData;

    private MutableLiveData<FailureResponse> mValidateLiveData;

    //initializing repository class
    private EditProfileRepository mEditProfileRepo = new EditProfileRepository();


    //set error observer & failure observer instances
    public void setGenericListeners(Observer<Throwable> errorObserver, Observer<FailureResponse> failureResponseObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureResponseObserver;

        initLiveData();
    }

    /**
     * Initializing all the live data objects
     */
    private void initLiveData() {
        if (mEditProfileLiveData == null) {
            mEditProfileLiveData = new RichMediatorLiveData<EditProfileResponse>() {
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
    }

    /**
     * This method gives the live data object to {@link EditProfileActivity}
     *
     * @return {@link #mEditProfileLiveData}
     */
    public RichMediatorLiveData<EditProfileResponse> getEditProfileLiveData() {
        return mEditProfileLiveData;
    }
    /**
     * This method gives the validation live data to {@link EditProfileActivity}
     *
     * @return {@link #mValidateLiveData}
     */
    public MutableLiveData<FailureResponse> getValidateLiveData() {
        return mValidateLiveData;
    }

    public String getName() {
        return mEditProfileRepo.getName();
    }

    public String getEmailId() {
        return mEditProfileRepo.getEmailId();
    }

    public String getMobileNumber() {
        return mEditProfileRepo.getMobileNumber();
    }
    public String getDOB() {
        return mEditProfileRepo.getDOB();
    }

    public String isEmailIdVerified() {
        return mEditProfileRepo.isEmailIdVerified();
    }

    public void onSaveChangesViewClicked(User editProfileData) {
        if (checkValidation(editProfileData))
        mEditProfileRepo.hitEditProfileApi(mEditProfileLiveData,editProfileData);
    }

    private boolean checkValidation(User editProfileData) {
          if (editProfileData.getFullName().isEmpty()){
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.NAME_EMPTY, ResourceUtils.getInstance().getString(R.string.enter_name)));
            return false;
        }
        else if (editProfileData.getPhone().isEmpty()){
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.PHONE_EMPTY, ResourceUtils.getInstance().getString(R.string.enter_phone)));
            return false;
        }
        else if (editProfileData.getCountryCode().isEmpty()){
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.PHONE_EMPTY, ResourceUtils.getInstance().getString(R.string.enter_country_code)));
            return false;
        }

        return true;
    }


    public String getProfileImage() {
        return mEditProfileRepo.getProfileImage();
    }

    public String getCountryCode() {
        return mEditProfileRepo.getCountryCode();
    }
}
