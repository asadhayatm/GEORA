package com.geora.ui.home.profile;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.base.RichMediatorLiveData;
import com.geora.model.FailureResponse;
import com.geora.model.ProfileInfo.UserProfileModel;
import com.geora.model.ResetPasswordModel;
import com.geora.model.createaddress.CreateAddressModel;



public class ProfileViewModel extends ViewModel {
    private RichMediatorLiveData<ResetPasswordModel> mResendEmailLiveData;
    private RichMediatorLiveData<CreateAddressModel> mUpdateCategoriesLiveData;
    private RichMediatorLiveData<UserProfileModel> mProfileInfoLiveData;

    private Observer<FailureResponse> failureResponseObserver;
    private Observer<Throwable> errorObserver;



    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureObserver) {
        this.errorObserver = errorObserver;
        this.failureResponseObserver = failureObserver;

        initLiveData();

    }

    private void initLiveData() {
        if (mResendEmailLiveData == null) {
            mResendEmailLiveData = new RichMediatorLiveData<ResetPasswordModel>() {
                @Override
                protected Observer<FailureResponse> getFailureObserver() {
                    return failureResponseObserver;
                }

                @Override
                protected Observer<Throwable> getErrorObserver() {
                    return errorObserver;
                }
            };
        }
        if (mUpdateCategoriesLiveData == null) {
            mUpdateCategoriesLiveData = new RichMediatorLiveData<CreateAddressModel>() {
                @Override
                protected Observer<FailureResponse> getFailureObserver() {
                    return failureResponseObserver;
                }

                @Override
                protected Observer<Throwable> getErrorObserver() {
                    return errorObserver;
                }
            };
        }
        if (mProfileInfoLiveData == null) {
            mProfileInfoLiveData = new RichMediatorLiveData<UserProfileModel>() {
                @Override
                protected Observer<FailureResponse> getFailureObserver() {
                    return failureResponseObserver;
                }

                @Override
                protected Observer<Throwable> getErrorObserver() {
                    return errorObserver;
                }
            };
        }


    }


    public RichMediatorLiveData<UserProfileModel> getProfileInfoLiveData() {
        return mProfileInfoLiveData;
    }
    public RichMediatorLiveData<ResetPasswordModel> getResetPasswordLiveData() {
        return mResendEmailLiveData;
    }

    public RichMediatorLiveData<CreateAddressModel> getUpdateCategoriesLiveData() {
        return mUpdateCategoriesLiveData;
    }

    private ProfileRepo mRepo = new ProfileRepo();

    public String getName() {
        return mRepo.getName();
    }

    public String getDateOfBirth() {
        return mRepo.getDateOfBirth();
    }

    public String getMobileNumber() {
        return mRepo.getMobileNumber();
    }

    public String getProfileImage() {
        return mRepo.getProfileImage();
    }

    public String getEmailId() {
        return mRepo.getEmailId();
    }

    public String isEmailIdVerified() {
        return mRepo.isEmailVerified();
    }

    public String getIsAllActive() {
        return mRepo.getIsAllActive();
    }

    public String getEventActive() {
        return mRepo.getEventActive();
    }

    public String getFundRaisingActive() {
        return mRepo.getFundRaisingActive();
    }

    public String getSalesActive() {
        return mRepo.getSalesActive();
    }

    public String getPromotionsActive() {
        return mRepo.getPromotionsActive();
    }

    public void setEventAction(String eventAction) {
        mRepo.setEventAction(eventAction);
    }

    public void setPromotionAction(String promotionAction) {
        mRepo.setPromotionAction(promotionAction);
    }

    public void setFundRaisingAction(String fundRaisingAction) {
        mRepo.setFundRaisingAction(fundRaisingAction);
    }

    public void setSalesAction(String salesAction) {
        mRepo.setSalesAction(salesAction);
    }

    public void setAllActionActive() {
        mRepo.setAllActionActive();
    }


    public void onResendViewClick() {
        mRepo.onHitResendEmailApi(mResendEmailLiveData);
    }

    public void onUpdateCategories(boolean mAllActive, boolean mEventActive, boolean mPromotionActive, boolean mSalesActive, boolean mFundRaisingActive) {
    mRepo.updateUserCategories(mUpdateCategoriesLiveData,mAllActive,mEventActive,mPromotionActive,mSalesActive,mFundRaisingActive);
    }

    public void gettingUserProfile() {
       mRepo.hitProfileInfo(mProfileInfoLiveData);
    }
}



