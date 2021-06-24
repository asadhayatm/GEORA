package com.geora.ui.home.profile;

import android.util.Log;

import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.FailureResponse;
import com.geora.model.ProfileInfo.Data;
import com.geora.model.ProfileInfo.UserProfileModel;
import com.geora.model.ResetPasswordModel;
import com.geora.model.createaddress.CreateAddressModel;

import java.util.HashMap;

public class ProfileRepo {

    public void hitProfileInfo(final RichMediatorLiveData<UserProfileModel> profileInfoLiveData) {
        DataManager.getInstance().hitProfileInfoApi().enqueue(new NetworkCallback<UserProfileModel>() {
            @Override
            public void onSuccess(UserProfileModel userProfileModel) {
                Log.i("Profile", userProfileModel.getData().toString());
                    DataManager.getInstance().saveUserDetails(userProfileModel.getData());
                    profileInfoLiveData.setValue(userProfileModel);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                profileInfoLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                profileInfoLiveData.setError(t);
            }
        });
    }

    public String getName() {
        return DataManager.getInstance().getFullName();
    }

    public String getDateOfBirth() {
        return DataManager.getInstance().getDateOfBirth();
    }

    public String getMobileNumber() {
        return DataManager.getInstance().getMobileNo();
    }

    public String getProfileImage() {
        return DataManager.getInstance().getProfileIMage();
    }

    public String getEmailId() {
        return DataManager.getInstance().getEmailId();
    }

    public String isEmailVerified() {
        return DataManager.getInstance().isEmailIdVerified();
    }

    public String getIsAllActive() {
        return DataManager.getInstance().getAllActive();
    }

    public String getEventActive() {
        return DataManager.getInstance().getEventActive();
    }

    public String getFundRaisingActive() {
        return DataManager.getInstance().getFundRaisingActive();
    }

    public String getPromotionsActive() {
        return DataManager.getInstance().getPromotionsActive();
    }

    public String getSalesActive() {
        return DataManager.getInstance().getSalesActive();
    }

    public void setEventAction(String eventAction) {
        DataManager.getInstance().setEventAction(eventAction);
    }

    public void setPromotionAction(String promotionAction) {
        DataManager.getInstance().setPromotionAction(promotionAction);
    }

    public void setFundRaisingAction(String fundRaisingAction) {
        DataManager.getInstance().seteFundRaisingAction(fundRaisingAction);
    }

    public void setSalesAction(String salesAction) {
        DataManager.getInstance().setSalesAction(salesAction);
    }

    public void setAllActionActive() {
        DataManager.getInstance().setSalesAction("1");
        DataManager.getInstance().seteFundRaisingAction("1");
        DataManager.getInstance().setPromotionAction("1");
        DataManager.getInstance().setEventAction("1");


    }


    public void onHitResendEmailApi(final RichMediatorLiveData<ResetPasswordModel> mResendEmailLiveData) {
        DataManager.getInstance().hitResendEmailApi(CreateResendPasswordPayload()).enqueue(new NetworkCallback<ResetPasswordModel>() {
            @Override
            public void onSuccess(ResetPasswordModel resetPasswordModel) {
                mResendEmailLiveData.setValue(resetPasswordModel);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                mResendEmailLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                mResendEmailLiveData.setError(t);
            }
        });
    }

    private HashMap<String, String> CreateResendPasswordPayload() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstantClass.APIConstant.ACTIONTYPE, AppConstantClass.RESEND);
        return hashMap;
    }

    public void updateUserCategories(final RichMediatorLiveData<CreateAddressModel> mUpdateCategoriesLiveData, boolean mAllActive, boolean mEventActive, boolean mPromotionActive, boolean mSalesActive, boolean mFundRaisingActive) {
        DataManager.getInstance().hitCreateAddressApi(CreateUpdateCategoriesResponse(mAllActive, mEventActive, mPromotionActive, mSalesActive, mFundRaisingActive)).enqueue(new NetworkCallback<CreateAddressModel>() {
            @Override
            public void onSuccess(CreateAddressModel createAddressModel) {
                saveDataInPreference(createAddressModel);
                mUpdateCategoriesLiveData.setValue(createAddressModel);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                mUpdateCategoriesLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                mUpdateCategoriesLiveData.setError(t);
            }
        });
    }


    //save user categories value in preference
    private void saveDataInPreference(CreateAddressModel createAddressModel) {
        DataManager.getInstance().seteFundRaisingAction(String.valueOf(createAddressModel.getData().getFundraisingActive()));
        DataManager.getInstance().setEventAction(String.valueOf(createAddressModel.getData().getEventActive()));

        DataManager.getInstance().setSalesAction(String.valueOf(createAddressModel.getData().getSalesActive()));

        DataManager.getInstance().setPromotionAction(String.valueOf(createAddressModel.getData().getPromotionsActive()));

        DataManager.getInstance().setAllAction(String.valueOf(createAddressModel.getData().getAllActive()));

    }

    private HashMap<String, String> CreateUpdateCategoriesResponse(boolean mAllActive, boolean mEventActive, boolean mPromotionActive, boolean mSalesActive, boolean mFundRaisingActive) {
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.ISADDRESSEDITTED, 0 + "");
        if (mAllActive)
            params.put(AppConstantClass.APIConstant.ALLACTIVE, 1 + "");
        else
            params.put(AppConstantClass.APIConstant.ALLACTIVE, 0 + "");
        if (mEventActive)
            params.put(AppConstantClass.APIConstant.EVENTACTIVE, 1 + "");
        else
            params.put(AppConstantClass.APIConstant.EVENTACTIVE, 0 + "");
        if (mPromotionActive)
            params.put(AppConstantClass.APIConstant.PROMOTIONSACTIVE, 1 + "");
        else
            params.put(AppConstantClass.APIConstant.PROMOTIONSACTIVE, 0 + "");
        if (mSalesActive)
            params.put(AppConstantClass.APIConstant.SALEACTIVE, 1 + "");
        else
            params.put(AppConstantClass.APIConstant.SALEACTIVE, 0 + "");
        if (mFundRaisingActive)
            params.put(AppConstantClass.APIConstant.FUNDRAISINGACTIVE, 1 + "");
        else
            params.put(AppConstantClass.APIConstant.FUNDRAISINGACTIVE, 0 + "");
        return params;
    }
}
