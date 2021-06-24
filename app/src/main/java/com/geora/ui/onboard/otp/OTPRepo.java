package com.geora.ui.onboard.otp;

import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.data.api.ApiCall;
import com.geora.data.api.NetworkListener;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.ApiFailureResponse;
import com.geora.model.FailureResponse;
import com.geora.model.TokenModel;
import com.geora.model.otpverification.Data;
import com.geora.model.otpverification.OTPVerificatonModel;
import com.geora.model.request.User;
import com.geora.model.signup.SignupModel;
import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class OTPRepo {
    public void hitOtpVerification(final RichMediatorLiveData<OTPVerificatonModel> otpLiveData, final User user) {
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.TOKEN, DataManager.getInstance().getToken());
        params.put(AppConstantClass.APIConstant.CLIENTOTP, user.getOtp());
        params.put(AppConstantClass.APIConstant.COUNTRYCODE, user.getCountryCode());
        params.put(AppConstantClass.APIConstant.PHONE, user.getPhone());
        params.put(AppConstantClass.APIConstant.TYPE, user.getType());

        Call<ResponseBody> call = DataManager.getInstance().hitOTPVerificatopnApi(params);

        ApiCall.getInstance().hitService(call, 1, new NetworkListener() {
            @Override
            public void onSuccess(int responseCode, String response, int requestCode) {
                OTPVerificatonModel verificatonModel = new Gson().fromJson(response, OTPVerificatonModel.class);
                //save data in preference
                saveUserToPreference(verificatonModel.getData());
                if (user.getType().equalsIgnoreCase("verify"))
                    getAccessToken(otpLiveData, verificatonModel);
                else{
                    otpLiveData.setValue(verificatonModel);
                }
            }

            @Override
            public void onFailure(String response, int requestCode) {
                FailureResponse failureResponse = new FailureResponse();
                try {
                    ApiFailureResponse apiFailureResponse = new Gson().fromJson(response, ApiFailureResponse.class);
                    if (apiFailureResponse.getCode() == 0) {
                        failureResponse = new Gson().fromJson(response, FailureResponse.class);
                    }else {
                        failureResponse.setErrorCode(apiFailureResponse.getCode());
                        failureResponse.setErrorMessage(apiFailureResponse.getMessage());
                        failureResponse.setData(apiFailureResponse.getData());
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    failureResponse = new Gson().fromJson(response, FailureResponse.class);
                }
                otpLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                otpLiveData.setError(t);
            }
        });

/*
        DataManager.getInstance().hitOTPVerificatopnApi(params).enqueue(new NetworkCallback<OTPVerificatonModel>() {
            @Override
            public void onSuccess(OTPVerificatonModel verificatonModel) {
                //save data in preference
                saveUserToPreference(verificatonModel.getData());
                if (user.getType().equalsIgnoreCase("verify"))
                    getAccessToken(otpLiveData, verificatonModel);
                else{
                    otpLiveData.setValue(verificatonModel);
                }
//                otpLiveData.setValue(verificatonModel);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                otpLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                otpLiveData.setError(t);
            }
        });
*/

    }

    public void hitOtpResend(final RichMediatorLiveData<SignupModel> otpLiveData, User user) {
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.TOKEN, DataManager.getInstance().getToken());
        params.put(AppConstantClass.APIConstant.CLIENTOTP, user.getOtp());
        params.put(AppConstantClass.APIConstant.COUNTRYCODE, user.getCountryCode());
        params.put(AppConstantClass.APIConstant.PHONE, user.getPhone());
        params.put(AppConstantClass.APIConstant.TYPE, user.getType());

        Call<ResponseBody> call = DataManager.getInstance().hitOTPResendApi(params);

        ApiCall.getInstance().hitService(call, 1, new NetworkListener() {
            @Override
            public void onSuccess(int responseCode, String response, int requestCode) {
                SignupModel verificatonModel = new Gson().fromJson(response, SignupModel.class);
                DataManager.getInstance().saveToken(verificatonModel.getData().getToken());//
                otpLiveData.setValue(verificatonModel);
            }

            @Override
            public void onFailure(String response, int requestCode) {
                FailureResponse failureResponse = new FailureResponse();
                try {
                    ApiFailureResponse apiFailureResponse = new Gson().fromJson(response, ApiFailureResponse.class);
                    if (apiFailureResponse.getCode() == 0) {
                        failureResponse = new Gson().fromJson(response, FailureResponse.class);
                    }else {
                        failureResponse.setErrorCode(apiFailureResponse.getCode());
                        failureResponse.setErrorMessage(apiFailureResponse.getMessage());
                        failureResponse.setData(apiFailureResponse.getData());
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    failureResponse = new Gson().fromJson(response, FailureResponse.class);
                }
                otpLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                otpLiveData.setError(t);
            }
        });

/*
        DataManager.getInstance().hitOTPResendApi(params).enqueue(new NetworkCallback<SignupModel>() {
            @Override
            public void onSuccess(SignupModel verificatonModel) {
                //save data in preference
                DataManager.getInstance().saveToken(verificatonModel.getData().getToken());//
                otpLiveData.setValue(verificatonModel);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                otpLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                otpLiveData.setError(t);
            }
        });
*/

    }

    private void getAccessToken(final RichMediatorLiveData<OTPVerificatonModel> liveData, final OTPVerificatonModel userData) {
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.CLIENT_ID, userData.getData().getUserData().getClientId());
        params.put(AppConstantClass.APIConstant.CLIENTSECRET, userData.getData().getUserData().getClientSecret());
        params.put(AppConstantClass.APIConstant.PASSWORD, userData.getData().getUserData().getPassword());
        params.put(AppConstantClass.APIConstant.USERNAME, userData.getData().getUserData().getEmail());
        params.put(AppConstantClass.APIConstant.GRANTTYPE, AppConstantClass.APIConstant.PASSWORD);

        Call<ResponseBody> call = DataManager.getInstance().hitGetTokenApi(params);

        ApiCall.getInstance().hitService(call, 1, new NetworkListener() {
            @Override
            public void onSuccess(int responseCode, String response, int requestCode) {
                TokenModel tokenModel = new Gson().fromJson(response, TokenModel.class);
                DataManager.getInstance().saveAccessToken(tokenModel.getAccessToken());
                liveData.setValue(userData);
            }

            @Override
            public void onFailure(String response, int requestCode) {
                FailureResponse failureResponse = new FailureResponse();
                try {
                    ApiFailureResponse apiFailureResponse = new Gson().fromJson(response, ApiFailureResponse.class);
                    if (apiFailureResponse.getCode() == 424) {
                        DataManager.getInstance().saveAccessToken(apiFailureResponse.getData().getToken());
                    }
                    if (apiFailureResponse.getCode() == 0) {
                        failureResponse = new Gson().fromJson(response, FailureResponse.class);
                    }else {
                        failureResponse.setErrorCode(apiFailureResponse.getCode());
                        failureResponse.setErrorMessage(apiFailureResponse.getMessage());
                        failureResponse.setData(apiFailureResponse.getData());
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    failureResponse = new Gson().fromJson(response, FailureResponse.class);
                }
                liveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                liveData.setError(t);
            }
        });

/*
        DataManager.getInstance().hitGetTokenApi(params).enqueue(new NetworkCallback<TokenModel>() {
            @Override
            public void onSuccess(TokenModel tokenModel) {
                DataManager.getInstance().saveAccessToken(tokenModel.getAccessToken());
                liveData.setValue(userData);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                liveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                liveData.setError(t);
            }
        });
*/
    }

    private void saveUserToPreference(Data user) {
        if (user != null) {
            DataManager.getInstance().saveUserDetails(user.getUserData());
        }
    }
}
