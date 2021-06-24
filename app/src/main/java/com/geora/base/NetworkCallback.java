package com.geora.base;

import com.geora.model.FailureResponse;
import com.geora.model.commonresponse.CommonResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class NetworkCallback<T> implements Callback<T> {

    public static final int AUTH_FAILED = 99;
    public static final int NO_INTERNET = 9;

    public abstract void onSuccess(T t);

    public abstract void onFailure(FailureResponse failureResponse);

    public abstract void onError(Throwable t);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            T res = response.body();
            onSuccess(response.body());
        } else {
            FailureResponse failureErrorBody = getFailureErrorBody(call.request().url().url().getFile(), response);
            onFailure(failureErrorBody);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (t instanceof SocketTimeoutException || t instanceof UnknownHostException) {
            FailureResponse failureResponseForNoNetwork = getFailureResponseForNoNetwork();
            onFailure(failureResponseForNoNetwork);
        } else {
            onError(t);
        }
    }

    private FailureResponse getFailureResponseForNoNetwork() {
        FailureResponse failureResponse = new FailureResponse();
        failureResponse.setErrorMessage("Please Check Your Internet Connection");
        failureResponse.setErrorCode(NO_INTERNET);
        return failureResponse;
    }

    /**
     * Create your custom failure response out of server response
     * Also save Url for any further use
     */
    private FailureResponse getFailureErrorBody(String url, Response<T> errorBody) {
        FailureResponse failureResponse = new FailureResponse();
        if (errorBody.code() == 500) {
            failureResponse.setErrorCode(errorBody.code());
            failureResponse.setErrorMessage(errorBody.message());
            return failureResponse;
        }
        else {
            CommonResponse commonResponse = new CommonResponse();
            JsonObject jsonObject;
            try {
                JSONObject json = new JSONObject(errorBody.errorBody().string());
                commonResponse.setMESSAGE(json.optString("message"));
                commonResponse.setCODE(json.optInt("code"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {


            }

            failureResponse.setErrorMessage(commonResponse.getMESSAGE());
            failureResponse.setErrorCode(commonResponse.getCODE());
            return failureResponse;
        }
    }
}
