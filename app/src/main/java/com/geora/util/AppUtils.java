package com.geora.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.geora.GeoraClass;
import com.geora.R;
import com.geora.base.NetworkCallback;
import com.geora.customviews.DialogForValidate;
import com.geora.data.DataManager;
import com.geora.data.constants.AppConstantClass;
import com.geora.listeners.DialogCallback;
import com.geora.model.FailureResponse;
import com.geora.ui.onboard.OnBoardActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.ResponseBody;

public class AppUtils {
    public static void hideKeyboard(Activity context) {
        try {
            // use application level context to avoid unnecessary leaks.
            InputMethodManager inputManager = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showKeyboard(Activity context) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(context.getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("HardwareIds")
    public static String getUniqueDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * Gets the version name of the application. For e.g. 1.9.3
     **/
    public static String getApplicationVersionNumber(Context context) {
        String versionName = null;
        if (context == null) {
            return versionName;
        }
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * Gets the version code of the application. For e.g. Maverick Meerkat or 2013050301
     **/
    public static int getApplicationVersionCode(Context ctx) {
        int versionCode = 0;
        try {
            versionCode = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * Gets the version number of the Android OS For e.g. 2.3.4 or 4.1.2
     **/
    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    //Check GPS Status true/false
    public static boolean checkGPSStatus(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return statusOfGPS;
    }

    ;

    public static String getCountryDialCode(Context context) {
        String contryId = null;
        String contryDialCode = null;

        TelephonyManager telephonyMngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        contryId = telephonyMngr.getSimCountryIso().toUpperCase();
        String[] arrContryCode = context.getResources().getStringArray(R.array.DialingCountryCode);
        for (int i = 0; i < arrContryCode.length; i++) {
            String[] arrDial = arrContryCode[i].split(",");
            if (arrDial[1].trim().equals(contryId.trim())) {
                contryDialCode = arrDial[0];
                break;
            }
        }
        return contryDialCode;
    }

    /**
     * method to dim the dialog background
     *
     * @param confirmationDialog Dialog whose background is to be altered
     */
    public static void dimDialogBackground(Dialog confirmationDialog) {
        if (confirmationDialog.getWindow() != null) {
            confirmationDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            WindowManager.LayoutParams lp = confirmationDialog.getWindow().getAttributes();
            lp.dimAmount = 0.8f;
            confirmationDialog.setCanceledOnTouchOutside(true);
            confirmationDialog.getWindow().setAttributes(lp);
        }
    }

    /**
     * function to start download
     * @param mContext
     * @param downloadLink
     */
    public static void startDownloadService(Context mContext, String downloadLink) {
        Intent newIntent = new Intent(mContext, DownloadFileIntentService.class);
        newIntent.setAction(DownloadFileIntentService.ACTION_DOWNLOAD);
        newIntent.putExtra(DownloadFileIntentService.EXTRA_URL, downloadLink);
        mContext.startService(newIntent);
        Toast.makeText(mContext, mContext.getString(R.string.download_started), Toast.LENGTH_LONG).show();
    }

    /**
     * method to get the user country code
     * @param context
     * @return
     */
    public static String getUserCountryCode(Context context) {
        String locale = "";
        String countryCode = "";
        String countryListJsonData = null;
        try {
            InputStream is = context.getAssets().open("country_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            countryListJsonData = new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (countryCode.equals("")) {
            locale = getUserCountry(context);
            countryCode = checkCountryCodeFromIsoCode(countryListJsonData, locale);
        }

        if (countryCode.equals("")) {
            //todo.... change when it updated
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = context.getResources().getConfiguration().getLocales().get(0).getCountry();
            } else {
                locale = context.getResources().getConfiguration().locale.getCountry();
            }
            countryCode = checkCountryCodeFromIsoCode(countryListJsonData, locale);

        }
        return countryCode.equals("") ? "+1" : countryCode;
    }

    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     * @param context Context reference to get the TelephonyManager instance from
     * @return country code or null
     */
    private static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry;
            if (tm != null) {
                simCountry = tm.getSimCountryIso();
                if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                    return simCountry.toLowerCase(Locale.US);
                } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                    String networkCountry = tm.getNetworkCountryIso();
                    if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                        return networkCountry.toLowerCase(Locale.US);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * check country code from isocode
     *
     * @param countryListJsonData
     * @param locale
     * @return
     */
    public static String checkCountryCodeFromIsoCode(String countryListJsonData, String locale) {
        String countryCode = "";
        if (countryListJsonData != null && locale != null) {
            try {
                JSONArray jsonArray = new JSONArray(countryListJsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject dataCountry = jsonArray.getJSONObject(i);
                    if (dataCountry.getString("ISOCode").equalsIgnoreCase(locale)) {
                        countryCode = "+" + String.valueOf(dataCountry.getInt("CountryCode"));
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return countryCode;
    }

    /**
     * function to check user validation
     *
     * @param mActivity
     * @param code
     * @param message
     * @return
     */
    public static boolean checkUserValid(final Activity mActivity, Integer code, final String message) {
        if (code == 403 || code == 421)  {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
                    if (!(mActivity instanceof OnBoardActivity)) {
                        new DialogForValidate(mActivity, new DialogCallback() {
                            @Override
                            public void onSubmit(View view, String result, int stars) {
                                logoutUser(mActivity);
                            }
                        }, message, mActivity.getString(R.string.sign_out)).show();
                    }
                }
            });
            return false;
        }else {
            return true;
        }
    }

    /**
     * function to logout user
     * @param mActivity
     */
    public static void logoutUser(Activity mActivity) {
        DataManager.getInstance().clearPreferences();
        mActivity.finishAffinity();

        Intent intent=new Intent(mActivity, OnBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(intent);
    }

    /**
     * hit notification api
     * @param beaconId
     */
    public static void hitNotificationApi(String beaconId) {
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.BEACON_ID, beaconId);
        DataManager.getInstance().hitNotificationApi(params).enqueue(new NetworkCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody responseBody) {

            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    /**
     * hit notification api
     */
    public static void hitDeviceTokenApi() {
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.DEVICETOKEN, DataManager.getInstance().getDeviceTokenStatus());
        params.put(AppConstantClass.APIConstant.DEVICETYPE, "android");
        DataManager.getInstance().hitDeviceTokenApi(params).enqueue(new NetworkCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody responseBody) {

            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    /**
     * function to get device token
     * @param activity
     * @return
     */
    public static String getDeviceToken(final Activity activity) {
        String deviceToken = DataManager.getInstance().getDeviceTokenStatus();
        if (deviceToken == null || deviceToken.equals("")) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
                String refreshedToken = instanceIdResult.getToken();
                Log.e("newToken",refreshedToken);
                DataManager.getInstance().setDeviceTokenStatus(refreshedToken);
            })
            .addOnFailureListener(e -> {
                e.printStackTrace();
            });
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return deviceToken;
    }
}
