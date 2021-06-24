package com.geora.data.api;



import com.geora.model.ProfileInfo.UserProfileModel;
import com.geora.model.ResetPasswordModel;
import com.geora.model.addaddress.AddAddressModel;
import com.geora.model.addresslist.AddressListModel;
import com.geora.model.businessuserresponse.BusinessUserResponse;
import com.geora.model.cardlist.CardListModel;
import com.geora.model.changred.FundraisedChangeModel;
import com.geora.model.commonresponse.CommonResponse;
import com.geora.model.createaddress.CreateAddressModel;
import com.geora.model.editprofile.EditProfileResponse;
import com.geora.model.feedbackresponse.FeedbackResponse;
import com.geora.model.formresponse.FormModel;
import com.geora.model.fundraisedmodel.FundraisedModel;
import com.geora.model.orderresponse.OrderModel;
import com.geora.model.refreshtoken.RefreshTokenResponse;
import com.geora.model.request.Reset;
import com.geora.model.request.User;
import com.geora.model.request.UserResponse;
import com.geora.model.signup.SignupModel;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by appinventiv on 27/3/18.
 */

public interface ApiInterface {

       //String ENDPOINT = "http://georastg.appinventive.com/";
    String ENDPOINT = "https://api.georaapp.com/";
    //String ENDPOINT = "http://georadev.appinventive.com/";
    //String ENDPOINT = "http://georatestingapi.appinventive.com/";


    @POST("login")
    Call<UserResponse> login(@Body User user);

    @POST("change-password")
    Call<CommonResponse> changePassword(@Body User user);

    @POST("reset-password")
    Call<CommonResponse> resetPassword(@Body Reset reset);

    @FormUrlEncoded
    @POST("refresh")
    Call<RefreshTokenResponse> refreshToken(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/users/reset-link")
    Call<SignupModel> hitForgotPasswordApi(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/users/logout")
    Call<SignupModel> hitLogoutAPI(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/add-card")
    Call<CardListModel> hitAddCardApi(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/add-default-source")
    Call<ResetPasswordModel> hitDefaultCardApi(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/profile-update-delivery")
    Call<SignupModel> hitDefaultAddressApi(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/delete-card")
    Call<ResetPasswordModel> hitDeleteCardApi(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/profile-delete-address")
    Call<ResetPasswordModel> hitDeleteAddressApi(@FieldMap HashMap<String, String> params);

    @PUT("logout")
    Call<CommonResponse> logOut();


    @FormUrlEncoded
    @POST("api/users")
    Call<SignupModel> signUp(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/users/verify-otp")
    Call<ResponseBody> hitOTPVerificatopnApi(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/users/verify-otp")
    Call<ResponseBody> hitOTPResendApi(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/users/login")
    Call<ResponseBody> hitLoginUserApi(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/users/forgot-password")
    Call<SignupModel> hitResetAPI(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/users/social-login")
    Call<ResponseBody> hitSocialLoginApi(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/update-dob")
    Call<ResponseBody> hitDobApi(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/oauth/access_token")
    Call<ResponseBody> hitGetTokenApi(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/profile-save")
    Call<CreateAddressModel> hitCreateAddressApi(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/profile-edit")
    Call<EditProfileResponse> hitEditProfileApi(@FieldMap HashMap<String, String> payloadForEditProfile);

    @FormUrlEncoded
    @POST("api/profile-password-change")
    Call<ResetPasswordModel> hitProfilePasswordChangeApi(@FieldMap HashMap<String, String> changePasswordPayload);

    @FormUrlEncoded
    @POST("api/profile-address-create")
    Call<AddAddressModel> hitAddAddressApi(@FieldMap HashMap<String, String> changePasswordPayload);

    @FormUrlEncoded
    @POST("api/profile-setting-edit-notification")
    Call<SignupModel> hitUpdateProfileSettingApi(@FieldMap HashMap<String, Object> payloadForProfileSetting);


    @GET("api/get-card")
    Call<CardListModel> hitCardListAPI();

    @GET("api/profile-address-get")
    Call<AddressListModel> hitAddressListApi();

    @FormUrlEncoded
    @POST("api/user-resend-email")
    Call<ResetPasswordModel> hitResendEmailApi(@FieldMap HashMap<String, String> resendEmailobject);


    @FormUrlEncoded
    @POST("api/raise-fund")
    Call<FundraisedChangeModel> hitFundRaisedAPI(@FieldMap HashMap<String, String> resendEmailobject);


    @FormUrlEncoded
    @POST("api/charge-customer")
    Call<ResponseBody> hitChargeSaleAPI(@FieldMap HashMap<String, String> resendEmailobject);


    @FormUrlEncoded
    @POST("api/subscribe-event")
    Call<FundraisedChangeModel> hitFormDAtaAPI(@FieldMap HashMap<String, String> resendEmailobject);


    @FormUrlEncoded
    @POST("api/user-feedback")
    Call<FeedbackResponse> hitFeedbackApi(@FieldMap HashMap<String, String> params);


    @FormUrlEncoded
    @POST("api/send-camp-push")
    Call<ResponseBody> hitNotificationApi(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/create-notification-arn")
    Call<ResponseBody> hitDeviceTokenApi(@FieldMap HashMap<String, String> params);


    @GET("api/order-listing")
    Call<OrderModel> hitOrderListingAPI(@Query("page") Integer page);


    @GET("api/notification-list")
    Call<ResponseBody> hitNotificationListingAPI(@QueryMap HashMap<String, String> params);

    @GET("api/fund-listing")
    Call<FundraisedModel> hitFundListingApi(@Query("page") Integer page);

    @GET("api/event-listing")
    Call<FormModel> hitEventListingApi(@Query("page") Integer page);

    @GET("api/profile-info")
    Call<UserProfileModel> hitProfileInfoApi();

    @GET("api/activity-listing")
    Call<ResponseBody> hitActivityListingApi(@Query("page") Integer page);

    @GET("api/check-business-user")
    Call<BusinessUserResponse> hitBusinessUserApi(@QueryMap HashMap<String, String> params);

    @GET("api/home-api")
    Call<ResponseBody> hitGetCampApi(@QueryMap HashMap<String, Object> params);

    @GET("api/home-api-saved")
    Call<ResponseBody> hitSavedCampApi(@QueryMap HashMap<String, Object> params);

    @GET("api/recall-notification")
    Call<ResponseBody> hitRecallApi(@QueryMap HashMap<String, Object> params);

    @FormUrlEncoded
    @POST("api/swipe-action")
    Call<ResponseBody> hitSwipeApi(@FieldMap HashMap<String, Object> params);

    @GET("api/get-campaign-detail")
    Call<ResponseBody> hitCampaignDetailApi(@QueryMap HashMap<String, Object> params);

    @FormUrlEncoded
    @POST("api/user-report-camp")
    Call<ResponseBody> hitCampaignReportApi(@FieldMap HashMap<String, Object> params);

    @GET("api/all-beacons")
    Call<ResponseBody> hitAllBeaconsApi(@QueryMap HashMap<String, Object> params);

    @GET("api/saved-list")
    Call<ResponseBody> hitcampSavedListApi(@QueryMap HashMap<String, Object> params);




    @DELETE("api/delete-notification-list")
    Call<ResponseBody> hitClearListingAPI(@QueryMap HashMap<String, String> params);

    @DELETE("api/remove-saved-item/{id}")
    Call<ResponseBody> hitRemoveSaveListAPI(@Path("id") String id);
}
