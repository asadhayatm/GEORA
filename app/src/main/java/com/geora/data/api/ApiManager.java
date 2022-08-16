package com.geora.data.api;


import android.content.Intent;
import android.util.Log;

import com.geora.data.DataManager;
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
import com.geora.model.editprofile.EditProfileResponse;
import com.geora.model.feedbackresponse.FeedbackResponse;
import com.geora.model.formresponse.FormModel;
import com.geora.model.fundraisedmodel.FundraisedModel;
import com.geora.model.myactivity.MyActivityModel;
import com.geora.model.orderresponse.OrderModel;
import com.geora.model.otpverification.OTPVerificatonModel;
import com.geora.model.refreshtoken.RefreshTokenResponse;
import com.geora.model.request.Reset;
import com.geora.model.request.User;
import com.geora.model.request.UserResponse;
import com.geora.model.signup.SignupModel;
import com.geora.util.CustomAuthenticator;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.QueryMap;

/**
 * Created by appinventiv on 27/3/18.
 */

public class ApiManager {

    private static final ApiManager instance = new ApiManager();
    private ApiInterface apiClient, authenticatedApiClient;
    private ApiInterface  authenticatedApiClientPreLogin;
    private OkHttpClient.Builder httpClient;
    private OkHttpClient.Builder httpClientPreLogin;

    private ApiManager() {
        apiClient = getRetrofitService();
        httpClient = getHttpClient();
        httpClientPreLogin = getHttpClientPreLogin();
        authenticatedApiClient = getAuthenticatedRetrofitService();
        authenticatedApiClientPreLogin = getAuthenticatedRetrofitServicePreLogin();
    }

    private HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);


    public static ApiManager getInstance() {
        return instance;
    }

    private static ApiInterface getRetrofitService() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiInterface.ENDPOINT)
                .build();

        return retrofit.create(ApiInterface.class);
    }

    private ApiInterface getAuthenticatedRetrofitServicePreLogin() {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(ApiInterface.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = retrofitBuilder.client(httpClientPreLogin.build()).build();
        return retrofit.create(ApiInterface.class);
    }

    private ApiInterface getAuthenticatedRetrofitService() {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(ApiInterface.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = retrofitBuilder.client(httpClient.build()).build();
        return retrofit.create(ApiInterface.class);
    }

    /**
     * Method to create {@link OkHttpClient} builder by adding required headers in the {@link Request}
     *
     * @return OkHttpClient object
     */
    private OkHttpClient.Builder getHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder;
                    requestBuilder = original.newBuilder()
                            .header("Authorization", "Bearer " +
                                    DataManager.getInstance().getAccessToken())
                            .method(original.method(), original.body());
                    Request request = requestBuilder.build();
                    Response response = chain.proceed(request);
                    Log.e("Response =", response.message());
                    return response;
                })
                .authenticator(new CustomAuthenticator())
                .readTimeout(30000, TimeUnit.MILLISECONDS)
                .writeTimeout(20000, TimeUnit.MILLISECONDS);
    }

    /**
     * Method to create {@link OkHttpClient} builder by adding required headers in the {@link Request}
     *
     * @return OkHttpClient object
     */
    private OkHttpClient.Builder getHttpClientPreLogin() {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder requestBuilder;
                        requestBuilder = original.newBuilder()
                                .header("Authorization", "Basic YWRtaW46MTIzNDU=")
                                .method(original.method(), original.body());
                        Request request = requestBuilder.build();
                        Response response = chain.proceed(request);
                        Log.e("Response =", response.message());
                        return response;
                    }
                })
                .authenticator(new CustomAuthenticator())
                .readTimeout(30000, TimeUnit.MILLISECONDS)
                .writeTimeout(20000, TimeUnit.MILLISECONDS);
    }


    public Call<UserResponse> hitLoginApi(User user) {
        return authenticatedApiClientPreLogin.login(user);
    }

    public Call<RefreshTokenResponse> refreshToken(HashMap<String, String> params) {
        return authenticatedApiClient.refreshToken(params);
    }

    public Call<SignupModel> hitSignUpApi(HashMap<String, String> user) {
        return authenticatedApiClientPreLogin.signUp(user);
    }

    public Call<ResponseBody> hitOTPVerificatopnApi(HashMap<String, String> user) {
        return authenticatedApiClientPreLogin.hitOTPVerificatopnApi(user);
    }

    public Call<ResponseBody> hitOTPResendApi(HashMap<String, String> user) {
        return authenticatedApiClientPreLogin.hitOTPResendApi(user);
    }

    public Call<CreateAddressModel> hitCreateAddressApi(HashMap<String, String> user) {
        return authenticatedApiClient.hitCreateAddressApi(user);
    }

    public Call<ResponseBody> hitLoginUserApi(HashMap<String, String> user) {
        return authenticatedApiClientPreLogin.hitLoginUserApi(user);
    }

    public Call<SignupModel> hitResetAPI(HashMap<String, String> user) {
        return authenticatedApiClientPreLogin.hitResetAPI(user);
    }

    public Call<ResponseBody> hitSocialLoginApi(HashMap<String, String> user) {
        return authenticatedApiClientPreLogin.hitSocialLoginApi(user);
    }

    public Call<ResponseBody> hitDobApi(HashMap<String, String> user) {
        return authenticatedApiClientPreLogin.hitDobApi(user);
    }

    public Call<ResponseBody> hitGetTokenApi(HashMap<String, String> user) {
        return authenticatedApiClientPreLogin.hitGetTokenApi(user);
    }

    public Call<SignupModel> hitForgotPasswordApi(HashMap<String, String> user) {
        return authenticatedApiClientPreLogin.hitForgotPasswordApi(user);
    }

    public Call<SignupModel> hitLogoutAPI(HashMap<String, String> user) {
        return authenticatedApiClientPreLogin.hitLogoutAPI(user);
    }

    public Call<CardListModel> hitAddCardApi(HashMap<String, String> user) {
        return authenticatedApiClient.hitAddCardApi(user);
    }

    public Call<ResetPasswordModel> hitDefaultCardApi(HashMap<String, String> user) {
        return authenticatedApiClient.hitDefaultCardApi(user);
    }


    public Call<SignupModel> hitDefaultAddressApi(HashMap<String, String> user) {
        return authenticatedApiClient.hitDefaultAddressApi(user);
    }

    public Call<ResetPasswordModel> hitDeleteCardApi(HashMap<String, String> user) {
        return authenticatedApiClient.hitDeleteCardApi(user);
    }

    public Call<ResetPasswordModel> hitDeleteAddressApi(HashMap<String, String> user) {
        return authenticatedApiClient.hitDeleteAddressApi(user);
    }

    public Call<FundraisedChangeModel> hitFundRaisedAPI(HashMap<String, String> user) {
        return authenticatedApiClient.hitFundRaisedAPI(user);
    }

    public Call<ResponseBody> hitChargeSaleAPI(HashMap<String, String> user) {
        return authenticatedApiClient.hitChargeSaleAPI(user);
    }
    public Call<FundraisedChangeModel> hitFormDAtaAPI(HashMap<String, String> user) {
        return authenticatedApiClient.hitFormDAtaAPI(user);
    }

    public Call<CommonResponse> hitResetPasswordApi(Reset reset) {
        return authenticatedApiClient.resetPassword(reset);
    }

    public Call<CommonResponse> hitLogOutPassword() {
        return authenticatedApiClient.logOut();
    }

    public Call<CommonResponse> hitChangePasswordApi(User user) {
        return authenticatedApiClient.changePassword(user);
    }

    public Call<CardListModel> hitCardListAPI() {
        return authenticatedApiClient.hitCardListAPI();
    }

    public Call<AddressListModel> hitAddressListApi() {
        return authenticatedApiClient.hitAddressListApi();
    }

    public Call<EditProfileResponse> hitEditProfileApi(HashMap<String, String> payloadForEditProfile) {
        return authenticatedApiClient.hitEditProfileApi(payloadForEditProfile);
    }

    public Call<ResetPasswordModel> hitProfilePasswordChangeApi(HashMap<String, String> changePasswordPayload) {
        return authenticatedApiClient.hitProfilePasswordChangeApi(changePasswordPayload);
    }

    public Call<AddAddressModel> hitAddAddressApi(HashMap<String, String> hashMap) {
        return authenticatedApiClient.hitAddAddressApi(hashMap);
    }

    public Call<SignupModel> hitUpdateProfileSettingApi(HashMap<String, Object> payloadForProfileSetting) {
    return authenticatedApiClient.hitUpdateProfileSettingApi(payloadForProfileSetting);
    }

    public Call<ResetPasswordModel> hitResendEmailApi(HashMap<String, String> resendEmailObject) {
        return authenticatedApiClient.hitResendEmailApi(resendEmailObject);
    }
    public Call<FeedbackResponse> hitFeedbackApi(HashMap<String, String> params) {
        return authenticatedApiClient.hitFeedbackApi(params);
    }
    public Call<BusinessUserResponse> hitBusinessUserApi(HashMap<String, String> params) {
        return authenticatedApiClient.hitBusinessUserApi(params);
    }
    public Call<ResponseBody> hitNotificationApi(HashMap<String, String> params) {
        return authenticatedApiClient.hitNotificationApi(params);
    }
    public Call<ResponseBody> hitDeviceTokenApi(HashMap<String, String> params) {
        return authenticatedApiClient.hitDeviceTokenApi(params);
    }

    public Call<OrderModel> hitOrderListingApi(Integer page)
    {
        return  authenticatedApiClient.hitOrderListingAPI(page);
    }

    public Call<ResponseBody> hitNotificationListingAPI(HashMap<String, String> params)
    {
        return  authenticatedApiClient.hitNotificationListingAPI(params);
    }

    public Call<ResponseBody> hitClearListingAPI(HashMap<String, String> params)
    {
        return  authenticatedApiClient.hitClearListingAPI(params);
    }

    public Call<FundraisedModel> hitFundListingApi(Integer page)
    {
        return authenticatedApiClient.hitFundListingApi(page);
    }

    public Call<FormModel> hitEventListingApi(Integer page)
    {
        return authenticatedApiClient.hitEventListingApi(page);
    }

    public Call<UserProfileModel> hitProfileInfoApi()
    {
        return authenticatedApiClient.hitProfileInfoApi();
    }
    public Call<ResponseBody> hitActivityListingApi(Integer page)
    {
        return authenticatedApiClient.hitActivityListingApi(page);
    }

    public Call<ResponseBody> hitGetCampApi(HashMap<String, Object> params) {
        return authenticatedApiClient.hitGetCampApi(params);
    }

    public Call<ResponseBody> hitSavedCampApi(HashMap<String, Object> params) {
        return authenticatedApiClient.hitSavedCampApi(params);
    }

    public Call<ResponseBody> hitRecallApi(HashMap<String, Object> params) {
        return authenticatedApiClient.hitRecallApi(params);
    }

    public Call<ResponseBody> hitSwipeApi(HashMap<String, Object> params) {
        return authenticatedApiClient.hitSwipeApi(params);
    }

    public Call<ResponseBody> hitCampaignDetailApi(HashMap<String, Object> params) {
        return authenticatedApiClient.hitCampaignDetailApi(params);
    }

    public Call<ResponseBody> hitCampaignReportApi(HashMap<String, Object> params) {
        return authenticatedApiClient.hitCampaignReportApi(params);
    }

    public Call<ResponseBody> hitAllBeaconsApi(HashMap<String, Object> params) {
        return authenticatedApiClient.hitAllBeaconsApi(params);
    }

    public Call<ResponseBody> hitcampSavedListApi(HashMap<String, Object> params) {
        return authenticatedApiClient.hitcampSavedListApi(params);
    }

    public Call<ResponseBody> hitRemoveSaveListAPI(String id) {
        return authenticatedApiClient.hitRemoveSaveListAPI(id);
    }
}
