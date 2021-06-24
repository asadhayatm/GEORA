package com.geora.data.api;

import android.widget.Toast;

import com.geora.GeoraClass;
import com.geora.R;
import com.geora.util.ResourceUtils;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ApiCall.java
 * This singleton class is used to hit service and handle response at single place.
 *  @author Appinvetiv
 *  @version 1.0
 *  @since 1.0
 */
public class ApiCall {
    private static ApiCall apiCall = null;
    private final String LOG_TAG = "Network Connection";


    /**
     * This method is used to provide the instance of Network Connection Class
     * @return instance of Network Connection Class
     */
    public static ApiCall getInstance() {
        if (apiCall == null) {
            return new ApiCall();
        } else {
            return apiCall;
        }
    }


    /**
     * This class is used to hit the service and handle their responses
     * @param bodyCall - retrofit call
     * @param requestCode
     * @param networkListener
     */
    public void hitService(Call<ResponseBody> bodyCall, final int requestCode, final NetworkListener networkListener) {
        bodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        String resp = response.body().string();
                        int responseCode = new JSONObject(resp.trim()).optInt("code");
                        if (networkListener != null)
                            networkListener.onSuccess(responseCode, resp.trim(), requestCode);
                    } else if (response.errorBody() != null && networkListener != null)
                        networkListener.onFailure(response.errorBody().string(), requestCode);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(GeoraClass.getContext(), ResourceUtils.getInstance().getString(R.string.json_parsing_error).toString(), Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(networkListener != null) {
                    networkListener.onError(t);
                }
            }
        });
    }
}


