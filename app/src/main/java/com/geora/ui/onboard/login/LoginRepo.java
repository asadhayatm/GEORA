package com.geora.ui.onboard.login;

import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.data.api.ApiCall;
import com.geora.data.api.NetworkListener;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.FailureResponse;
import com.geora.model.TokenModel;
import com.geora.model.ApiFailureResponse;
import com.geora.model.otpverification.Data;
import com.geora.model.otpverification.OTPVerificatonModel;
import com.geora.model.request.User;
import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.geora.util.DateTimeUtils.bdayToTimeStamp;

public class LoginRepo {

    public void hitLoginApi(final RichMediatorLiveData<OTPVerificatonModel> liveData, User user) {
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.EMAIL, user.getEmail());
        params.put(AppConstantClass.APIConstant.PASSWORD, user.getPassword());
        params.put(AppConstantClass.APIConstant.COUNTRYCODE, user.getCountryCode());
        if (user.getEmail().contains("@"))
            params.put(AppConstantClass.APIConstant.TYPE, "email");
        else
            params.put(AppConstantClass.APIConstant.TYPE, "phone");

        Call<ResponseBody> call = DataManager.getInstance().hitLoginUserApi(params);

        ApiCall.getInstance().hitService(call, 1, new NetworkListener() {
            @Override
            public void onSuccess(int responseCode, String response, int requestCode) {
                OTPVerificatonModel userResponse = new Gson().fromJson(response, OTPVerificatonModel.class);
                if (responseCode == 224) {
                    liveData.setValue(userResponse);
                } else {
                    DataManager.getInstance().saveBusinessUserStatus(userResponse.getData().isBusinessUser());
                    saveUserToPreference(userResponse.getData());
                    getToken(liveData, userResponse);
                }
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
        DataManager.getInstance().hitLoginUserApi(params).enqueue(new NetworkCallback<OTPVerificatonModel>() {
            @Override
            public void onSuccess(OTPVerificatonModel userResponse) {
                DataManager.getInstance().saveBusinessUserStatus(userResponse.getData().isBusinessUser());
                saveUserToPreference(userResponse.getData());
                getToken(liveData, userResponse);
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

    private void getToken(final RichMediatorLiveData<OTPVerificatonModel> liveData, final OTPVerificatonModel userData) {
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

    public void saveDeviceToken(String deviceToken) {
        //save device token to shared preference using data manager

    }

    public void hitSocialLoginApi(final RichMediatorLiveData<OTPVerificatonModel> loginLiveData, final User user) {
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.EMAIL, user.getEmail());
        params.put(AppConstantClass.APIConstant.METHOD, user.getMethod());
        params.put(AppConstantClass.APIConstant.DEVICETYPE, "android");
        if (user.getType().equalsIgnoreCase("facebook")) {
            params.put(AppConstantClass.APIConstant.FACEBOOKID, user.getFacebookId());
            params.put(AppConstantClass.APIConstant.SOCIALID, user.getFacebookId());
            params.put(AppConstantClass.APIConstant.PASSWORD, user.getFacebookId());

        } else {
            params.put(AppConstantClass.APIConstant.GOOGLEID, user.getGoogleId());
            params.put(AppConstantClass.APIConstant.SOCIALID, user.getGoogleId());
            params.put(AppConstantClass.APIConstant.PASSWORD, user.getGoogleId());

        }
        params.put(AppConstantClass.APIConstant.IMAGE, user.getImage());
        params.put(AppConstantClass.APIConstant.TYPE, user.getType());
        params.put(AppConstantClass.APIConstant.NAME, user.getFullName());

        Call<ResponseBody> call = DataManager.getInstance().hitSocialLoginApi(params);

        ApiCall.getInstance().hitService(call, 1, new NetworkListener() {
            @Override
            public void onSuccess(int responseCode, String response, int requestCode) {
                OTPVerificatonModel userResponse = new Gson().fromJson(response, OTPVerificatonModel.class);
                DataManager.getInstance().saveBusinessUserStatus(userResponse.getData().isBusinessUser());
                saveUserToPreference(userResponse.getData());
                if(userResponse.getData().isNewUser() || userResponse.getData().getUserData().getDob() == null) {
//                if(userResponse.getData().isNewUser()) {
                    loginLiveData.setValue(userResponse);
                }else {
                    getToken(loginLiveData, userResponse);
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
                loginLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                loginLiveData.setError(t);
            }
        });
/*
        DataManager.getInstance().hitSocialLoginApi(params).enqueue(new NetworkCallback<OTPVerificatonModel>() {
            @Override
            public void onSuccess(OTPVerificatonModel userResponse) {
                DataManager.getInstance().saveBusinessUserStatus(userResponse.getData().isBusinessUser());
                saveUserToPreference(userResponse.getData());
                getToken(loginLiveData, userResponse);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                loginLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                loginLiveData.setError(t);
            }
        });
*/
    }


    public void hitSaveDobApi(final RichMediatorLiveData<OTPVerificatonModel> mDobLiveData, String dob, final OTPVerificatonModel user) {
        HashMap<String, String> params = new HashMap<>();

        params.put(AppConstantClass.APIConstant.DOB, bdayToTimeStamp(dob) + "");
        params.put(AppConstantClass.APIConstant.TOKEN, user.getData().getUserData().getClientId());

        Call<ResponseBody> call = DataManager.getInstance().hitDobApi(params);

        ApiCall.getInstance().hitService(call, 1, new NetworkListener() {
            @Override
            public void onSuccess(int responseCode, String response, int requestCode) {
                OTPVerificatonModel userResponse = new Gson().fromJson(response, OTPVerificatonModel.class);
                user.getData().setNewUser(false);
                getToken(mDobLiveData, user);
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
                mDobLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                mDobLiveData.setError(t);
            }
        });
    }
}
