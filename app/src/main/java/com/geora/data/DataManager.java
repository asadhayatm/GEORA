package com.geora.data;

import android.content.Context;

import com.geora.GeoraClass;
import com.geora.constants.AppConstants;
import com.geora.data.api.ApiManager;
import com.geora.data.constants.AppConstantClass;
import com.geora.data.preferences.PreferenceManager;
import com.geora.model.ProfileInfo.UserProfileModel;
import com.geora.model.ResetPasswordModel;
import com.geora.model.TokenModel;
import com.geora.model.addaddress.AddAddressModel;
import com.geora.model.addresslist.AddressListModel;
import com.geora.model.businessuserresponse.BusinessUserResponse;
import com.geora.model.cardlist.CardListModel;
import com.geora.model.changred.FundraisedChangeModel;
import com.geora.model.chargeresponse.ChargeResponse;
import com.geora.model.commonresponse.CommonResponse;
import com.geora.model.createaddress.CreateAddressModel;
import com.geora.model.createaddress.Data;
import com.geora.model.editprofile.EditProfileResponse;
import com.geora.model.feedbackresponse.FeedbackResponse;
import com.geora.model.formresponse.FormModel;
import com.geora.model.fundraisedmodel.FundraisedModel;
import com.geora.model.myactivity.MyActivityModel;
import com.geora.model.orderresponse.OrderModel;
import com.geora.model.otpverification.OTPVerificatonModel;
import com.geora.model.otpverification.UserData;
import com.geora.model.request.Reset;
import com.geora.model.request.User;
import com.geora.model.request.UserResponse;
import com.geora.model.signup.SignupModel;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class DataManager {

    private static DataManager instance;
    private ApiManager apiManager;
    private PreferenceManager mPrefManager;


    private DataManager(Context context) {
        //Initializing SharedPreference object
        mPrefManager = PreferenceManager.getInstance(context);


    }

    /**
     * Returns the single instance of {@link DataManager} if
     * {@link #init(Context)} is called first
     *
     * @return instance
     */
    public static DataManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Call init() before getInstance()");
        }
        return instance;
    }

    /**
     * Method used to create an instance of {@link DataManager}
     *
     * @param context of the application passed from the {@link GeoraClass}
     * @return instance if it is null
     */
    public synchronized static DataManager init(Context context) {
        if (instance == null) {
            instance = new DataManager(context);
        }
        return instance;
    }



    /**
     * Method to initialize {@link ApiManager} class
     */
    public void initApiManager() {
        apiManager = ApiManager.getInstance();
    }


    public Call<UserResponse> hitLoginApi(User user) {
        return apiManager.hitLoginApi(user);
    }

    public String getRefreshToken() {
        return mPrefManager.getString(AppConstants.PreferenceConstants.REFRESH_TOKEN);
    }

    public String getUserName() {
        return mPrefManager.getString(AppConstants.PreferenceConstants.USER_NAME);
    }

    public String getUserId() {
        return mPrefManager.getString(AppConstantClass.APIConstant.USERID);
    }

    public void saveAccessToken(String accessToken) {
        mPrefManager.putString(AppConstants.PreferenceConstants.ACCESS_TOKEN, accessToken);
    }

    public void saveToken(String accessToken) {
        mPrefManager.putString(AppConstants.PreferenceConstants.TOKEN, accessToken);
    }

    public void saveRefreshToken(String refreshToken) {
        mPrefManager.putString(AppConstants.PreferenceConstants.REFRESH_TOKEN, refreshToken);
    }

    public String getAccessToken() {
        return mPrefManager.getString(AppConstants.PreferenceConstants.ACCESS_TOKEN);
    }

    public String getToken() {
        return mPrefManager.getString(AppConstants.PreferenceConstants.TOKEN);
    }

    public String getClientId() {
        return mPrefManager.getString(AppConstantClass.APIConstant.CLIENT_ID);
    }

    public String getFullName() {
        return mPrefManager.getString(AppConstantClass.APIConstant.NAME);
    }

    public boolean isSocailLogin() {
        return mPrefManager.getBoolean(AppConstantClass.APIConstant.ISSOCIALLOGIN);
    }

    public String getProfileIMage() {
        return mPrefManager.getString(AppConstantClass.APIConstant.IMAGE);
    }

    public String getMobileNo() {
        return mPrefManager.getString(AppConstantClass.APIConstant.PHONE);
    }

    public String getCountryCode() {
        return mPrefManager.getString(AppConstantClass.APIConstant.COUNTRYCODE);
    }

    public String getAllActive() {
        return mPrefManager.getString(AppConstantClass.APIConstant.ALLACTIVE);
    }

    public String getEventActive() {
        return mPrefManager.getString(AppConstantClass.APIConstant.EVENTACTIVE);
    }

    public String getFundRaisingActive() {
        return mPrefManager.getString(AppConstantClass.APIConstant.FUNDRAISINGACTIVE);
    }

    public String getSalesActive() {
        return mPrefManager.getString(AppConstantClass.APIConstant.SALEACTIVE);
    }

    public String getPromotionsActive() {
        return mPrefManager.getString(AppConstantClass.APIConstant.PROMOTIONSACTIVE);
    }


    public Call<SignupModel> hitSignUpApi(HashMap<String, String> user) {
        return apiManager.hitSignUpApi(user);
    }

    public Call<ResponseBody> hitOTPVerificatopnApi(HashMap<String, String> user) {
        return apiManager.hitOTPVerificatopnApi(user);
    }

    public Call<ResponseBody> hitOTPResendApi(HashMap<String, String> user) {
        return apiManager.hitOTPResendApi(user);
    }

    public Call<CreateAddressModel> hitCreateAddressApi(HashMap<String, String> user) {
        return apiManager.hitCreateAddressApi(user);
    }

    public Call<ResponseBody> hitLoginUserApi(HashMap<String, String> user) {
        return apiManager.hitLoginUserApi(user);
    }

    public Call<SignupModel> hitResetAPI(HashMap<String, String> user) {
        return apiManager.hitResetAPI(user);
    }

    public Call<ResponseBody> hitSocialLoginApi(HashMap<String, String> user) {
        return apiManager.hitSocialLoginApi(user);
    }

    public Call<ResponseBody> hitDobApi(HashMap<String, String> user) {
        return apiManager.hitDobApi(user);
    }

    public Call<ResponseBody> hitGetTokenApi(HashMap<String, String> user) {
        return apiManager.hitGetTokenApi(user);
    }

    public Call<ResponseBody> hitNotificationApi(HashMap<String, String> user) {
        return apiManager.hitNotificationApi(user);
    }

    public Call<ResponseBody> hitDeviceTokenApi(HashMap<String, String> user) {
        return apiManager.hitDeviceTokenApi(user);
    }

    public void saveUserDetails(UserData user) {
        if (!user.getFacebookId().isEmpty() || !user.getGoogleId().isEmpty()) {
            mPrefManager.putBoolean(AppConstantClass.APIConstant.ISSOCIALLOGIN, true);
        } else {
            mPrefManager.putBoolean(AppConstantClass.APIConstant.ISSOCIALLOGIN, false);
        }
        //save user name differently
        mPrefManager.putString(AppConstantClass.APIConstant.USERNAME, user.getUsername());
        mPrefManager.putString(AppConstantClass.APIConstant.USERID, user.getId().toString());
        mPrefManager.putString(AppConstantClass.APIConstant.NAME, user.getFirstName());
        mPrefManager.putString(AppConstantClass.APIConstant.CLIENT_ID, user.getClientId());
        mPrefManager.putString(AppConstantClass.APIConstant.PHONE, user.getPhone());
        mPrefManager.putString(AppConstantClass.APIConstant.COUNTRYCODE, user.getCountryCode());
        mPrefManager.putString(AppConstantClass.APIConstant.EMAIL, user.getEmail());
        mPrefManager.putString(AppConstantClass.APIConstant.ISEMAILVARIFIED, user.getIsVerifiedEmail());
        mPrefManager.putString(AppConstantClass.APIConstant.ISPHONEVERIFIED, user.getIsVerifiedPhone());
        mPrefManager.putString(AppConstantClass.APIConstant.IMAGE, user.getProfileImage());
        mPrefManager.putString(AppConstantClass.APIConstant.DOB, String.valueOf(user.getDob()));
        //save categories
        mPrefManager.putString(AppConstantClass.APIConstant.ALLACTIVE, user.getAllActive() + "");
        mPrefManager.putString(AppConstantClass.APIConstant.EVENTACTIVE, user.getEventActive() + "");
        mPrefManager.putString(AppConstantClass.APIConstant.FUNDRAISINGACTIVE, user.getFundraisingActive() + "");
        mPrefManager.putString(AppConstantClass.APIConstant.PROMOTIONSACTIVE, user.getPromotionsActive() + "");
        mPrefManager.putString(AppConstantClass.APIConstant.SALEACTIVE, user.getSalesActive() + "");
        mPrefManager.putString(AppConstantClass.APIConstant.NOTIFICATIONFLAG, user.getNotificationSubscriptionStatus());
        mPrefManager.putString(AppConstantClass.APIConstant.PROXIMITYRANGE, user.getProximityRange() + "");

    }

    public Call<SignupModel> hitForgotPasswordApi(HashMap<String, String> user) {
        return apiManager.hitForgotPasswordApi(user);
    }

    public Call<SignupModel> hitLogoutAPI(HashMap<String, String> user) {
        return apiManager.hitLogoutAPI(user);
    }

    public Call<CardListModel> hitAddCardApi(HashMap<String, String> user) {
        return apiManager.hitAddCardApi(user);
    }

    public Call<CommonResponse> hitResetPasswordApi(Reset reset) {
        return apiManager.hitResetPasswordApi(reset);
    }


    public Call<CommonResponse> hitLogOutApi() {
        return apiManager.hitLogOutPassword();
    }

    public void clearPreferences() {
        mPrefManager.clearAllPrefs();
    }

    public Call<CommonResponse> hitChangePasswordApi(User user) {
        return apiManager.hitChangePasswordApi(user);
    }

    public Call<CardListModel> hitCardListAPI() {
        return apiManager.hitCardListAPI();
    }

    public Call<AddressListModel> hitAddressListApi() {
        return apiManager.hitAddressListApi();
    }

    public Call<ResetPasswordModel> hitDefaultCardApi(HashMap<String, String> user) {
        return apiManager.hitDefaultCardApi(user);
    }

    public Call<SignupModel> hitDefaultAddressApi(HashMap<String, String> user) {
        return apiManager.hitDefaultAddressApi(user);
    }

    public Call<ResetPasswordModel> hitDeleteAddressApi(HashMap<String, String> user) {
        return apiManager.hitDeleteAddressApi(user);
    }

    public Call<ResetPasswordModel> hitDeleteCardApi(HashMap<String, String> user) {
        return apiManager.hitDeleteCardApi(user);
    }
    public Call<FundraisedChangeModel> hitFundRaisedAPI(HashMap<String, String> data) {
        return apiManager.hitFundRaisedAPI(data);
    }
    public Call<ResponseBody> hitChargeSaleAPI(HashMap<String, String> data) {
        return apiManager.hitChargeSaleAPI(data);
    }
    public Call<FundraisedChangeModel> hitFormDAtaAPI(HashMap<String, String> data) {
        return apiManager.hitFormDAtaAPI(data);
    }
    public Call<FeedbackResponse> hitFeedbackApi(HashMap<String, String> data) {
        return apiManager.hitFeedbackApi(data);
    }
    public Call<BusinessUserResponse> hitBusinessUserApi(HashMap<String, String> data) {
        return apiManager.hitBusinessUserApi(data);
    }

    public Call<OrderModel> hitOrderListApi(Integer page)
    {
         return apiManager.hitOrderListingApi(page);
    }

    public Call<ResponseBody> hitNotificationListingAPI(HashMap<String, String> params)
    {
         return apiManager.hitNotificationListingAPI(params);
    }

    public Call<ResponseBody> hitClearListingAPI(HashMap<String, String> params)
    {
         return apiManager.hitClearListingAPI(params);
    }

    public Call<FundraisedModel> hitFundListingApi(Integer page)
    {
        return apiManager.hitFundListingApi(page);
    }

    public Call<FormModel> hitEventListingApi(Integer page)
    {
        return apiManager.hitEventListingApi(page);
    }

    public Call<UserProfileModel> hitProfileInfoApi()
    {
        return apiManager.hitProfileInfoApi();
    }
    public Call<ResponseBody> hitActivityListingApi(Integer page)
    {
        return apiManager.hitActivityListingApi(page);
    }


    public String getDateOfBirth() {
        return mPrefManager.getString(AppConstantClass.APIConstant.DOB);
    }

    public String getEmailId() {
        return mPrefManager.getString(AppConstantClass.APIConstant.EMAIL);
    }

    public String isEmailIdVerified() {
        return mPrefManager.getString(AppConstantClass.APIConstant.ISEMAILVARIFIED);
    }

    public Call<EditProfileResponse> hitEditProfileApi(HashMap<String, String> payloadForEditProfile) {
        return apiManager.hitEditProfileApi(payloadForEditProfile);
    }

    public void saveUpdatedProfileData(EditProfileResponse editProfileResponse) {
        mPrefManager.putString(AppConstantClass.APIConstant.USERNAME, editProfileResponse.getData().getUpdateProfile().getUsername());
        mPrefManager.putString(AppConstantClass.APIConstant.NAME, editProfileResponse.getData().getUpdateProfile().getFirstName());
        mPrefManager.putString(AppConstantClass.APIConstant.CLIENT_ID, editProfileResponse.getData().getUpdateProfile().getClientId());
        mPrefManager.putString(AppConstantClass.APIConstant.COUNTRYCODE, editProfileResponse.getData().getUpdateProfile().getCountryCode());
        mPrefManager.putString(AppConstantClass.APIConstant.IMAGE, editProfileResponse.getData().getUpdateProfile().getProfileImage());
        mPrefManager.putString(AppConstantClass.APIConstant.DOB, String.valueOf(editProfileResponse.getData().getUpdateProfile().getDob()));

    }

    public void saveCategories(Data user) {
        mPrefManager.putString(AppConstantClass.APIConstant.ALLACTIVE, user.getAllActive() + "");
        mPrefManager.putString(AppConstantClass.APIConstant.EVENTACTIVE, user.getEventActive() + "");
        mPrefManager.putString(AppConstantClass.APIConstant.FUNDRAISINGACTIVE, user.getFundraisingActive() + "");
        mPrefManager.putString(AppConstantClass.APIConstant.PROMOTIONSACTIVE, user.getPromotionsActive() + "");
        mPrefManager.putString(AppConstantClass.APIConstant.SALEACTIVE, user.getSalesActive() + "");
    }

    public Call<ResetPasswordModel> hitProfilePasswordChangeApi(HashMap<String, String> changePasswordPayload) {
        return apiManager.hitProfilePasswordChangeApi(changePasswordPayload);
    }

    public Call<AddAddressModel> hitAddAddressApi(HashMap<String, String> hashMap) {
        return apiManager.hitAddAddressApi(hashMap);
    }

    public Call<SignupModel> hitUpdateProfileSettingApi(HashMap<String, Object> payloadForProfileSetting) {
        return apiManager.hitUpdateProfileSettingApi(payloadForProfileSetting);
    }

    public Call<ResponseBody> hitGetCampApi(HashMap<String, Object> params) {
        return apiManager.hitGetCampApi(params);
    }

    public Call<ResponseBody> hitSavedCampApi(HashMap<String, Object> params) {
        return apiManager.hitSavedCampApi(params);
    }

    public Call<ResponseBody> hitRecallApi(HashMap<String, Object> params) {
        return apiManager.hitRecallApi(params);
    }

    public Call<ResponseBody> hitSwipeApi(HashMap<String, Object> params) {
        return apiManager.hitSwipeApi(params);
    }

    public Call<ResponseBody> hitCampaignDetailApi(HashMap<String, Object> params) {
        return apiManager.hitCampaignDetailApi(params);
    }

    public Call<ResponseBody> hitCampaignReportApi(HashMap<String, Object> params) {
        return apiManager.hitCampaignReportApi(params);
    }

    public Call<ResponseBody> hitAllBeaconsApi(HashMap<String, Object> params) {
        return apiManager.hitAllBeaconsApi(params);
    }

    public Call<ResponseBody> hitCampSavedListApi(HashMap<String, Object> params) {
        return apiManager.hitcampSavedListApi(params);
    }

    public Call<ResponseBody> hitRemoveSaveListAPI(String id) {
        return apiManager.hitRemoveSaveListAPI(id);
    }



    public void setProximityRange(String proximityRange) {
        mPrefManager.putString(AppConstantClass.APIConstant.PROXIMITYRANGE, proximityRange);
    }

    public String getProximityRange() {
        return mPrefManager.getString(AppConstantClass.APIConstant.PROXIMITYRANGE);
    }

    public void setNotificationStatus(String notificationSubscriptionStatus) {
        mPrefManager.putString(AppConstantClass.APIConstant.NOTIFICATIONFLAG, notificationSubscriptionStatus);
    }

    public String getNotificationStatus() {
        return mPrefManager.getString(AppConstantClass.APIConstant.NOTIFICATIONFLAG);
    }

    public void setEventAction(String eventAction) {
        mPrefManager.putString(AppConstantClass.APIConstant.EVENTACTIVE, eventAction);

    }

    public void setPromotionAction(String promotionAction) {
        mPrefManager.putString(AppConstantClass.APIConstant.PROMOTIONSACTIVE, promotionAction);
    }

    public void seteFundRaisingAction(String FundRaisingAction) {
        mPrefManager.putString(AppConstantClass.APIConstant.FUNDRAISINGACTIVE, FundRaisingAction);
    }

    public void setSalesAction(String salesAction) {
        mPrefManager.putString(AppConstantClass.APIConstant.SALEACTIVE, salesAction);
    }

    public Call<ResetPasswordModel> hitResendEmailApi(HashMap<String, String> resendEmailObject) {
        return apiManager.hitResendEmailApi(resendEmailObject);
    }

    public void setAllAction(String eventAction) {
        mPrefManager.putString(AppConstantClass.APIConstant.ALLACTIVE, eventAction);
    }

    public void saveBusinessUserStatus(int status) {
        mPrefManager.putInt(AppConstantClass.APIConstant.ISBUSINESSUSER, status);
    }

    public int getBusinessUserStatus() {
        return mPrefManager.getInt(AppConstantClass.APIConstant.ISBUSINESSUSER);
    }

    public void setDeviceTokenStatus(String deviceToken) {
        mPrefManager.putString(AppConstantClass.APIConstant.DEVICETOKEN, deviceToken);
    }

    public String getDeviceTokenStatus() {
        return mPrefManager.getString(AppConstantClass.APIConstant.DEVICETOKEN);
    }
}