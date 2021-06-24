package com.geora.locationlibrary;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.Toast;

import com.geora.locationlibrary.interfaces.LocationsCallback;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by appinventiv on 12/9/17, modified by "Harsh Gaur" on 02/01/2019.
 */

public class RCLocation {
    public static final int FINE_LOCATION_PERMISSIONS = 0x001;
    private final int REQUEST_CHECK_SETTINGS = 0x002;
    private final long UPDATE_INTERVAL_IN_MILLISECONDS = 10 * 1000;//10 seconds
    private final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5 * 1000;//10 seconds
    private Activity mActivity;
    private LocationsCallback locationsCallback;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;


    /*
     *  Initialize activity instance
     */
    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    /*
     *  Initialize location callback
     */
    public void setCallback(LocationsCallback locationsCallback) {
        this.locationsCallback = locationsCallback;
    }


    /*
     * Method to check permission
     * */
    public void startLocation() {
        if (hasPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION, FINE_LOCATION_PERMISSIONS)) {
            buildFusedLocationClient();
            createLocationRequestAndLocationCallback();
            buildLocationSettingsRequest();
        }
    }


    /**
     * Builds a FusedLocationClient
     */
    private void buildFusedLocationClient() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity);
    }

    /**
     * Sets up the rcLocation request. Android has two rcLocation request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current rcLocation. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused RCLocation Provider API returns rcLocation updates that are
     * accurate to within a few feet.
     * <p>
     * These settings are appropriate for mapping applications that show real-time rcLocation
     * updates.
     */
    private void createLocationRequestAndLocationCallback() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                // Sets the desired interval for active rcLocation updates. This interval is
                // inexact. You may not receive updates at all if no rcLocation sources are available, or
                // you may receive them slower than requested. You may also receive updates faster than
                // requested if other applications are requesting rcLocation at a faster interval.
                .setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
                // Sets the fastest rate for active rcLocation updates. This interval is exact, and your
                // application will never receive updates faster than this value.
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
                .setSmallestDisplacement(10);//set smallest displacement to change lat long
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        mCurrentLocation = location;
                        locationsCallback.setLocation(mCurrentLocation);
                    }
                }
            }
        };
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed rcLocation settings.
     */
    private void buildLocationSettingsRequest() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);//this make sure dialog is always visible
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(mActivity).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    startLocationUpdates();

                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(mActivity, REQUEST_CHECK_SETTINGS);
                                break;
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.

                            break;
                    }
                }
            }
        });
    }

    /*
     * Method to check permissions
     * */
    private boolean hasPermission(Activity context, String permission, int reqId) {
        int result = ContextCompat.checkSelfPermission(context, permission);
        if (result == PackageManager.PERMISSION_GRANTED) return true;
        else {
            ActivityCompat.requestPermissions(context, new String[]{permission}, reqId);
            return false;
        }
    }

    public void setPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case FINE_LOCATION_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    buildFusedLocationClient();
                    createLocationRequestAndLocationCallback();
                    buildLocationSettingsRequest();
                } else if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissions[0])) {//has selected do not allow allow
                    showAllowPermissionFromSettingDialog(mActivity);
                } else {
                    Toast.makeText(mActivity, "Permission Denied", Toast.LENGTH_SHORT).show();
                    //hasPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION, FINE_LOCATION_PERMISSIONS)
                }
                break;
        }
    }

    public void setActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                if (resultCode == Activity.RESULT_OK)
                    buildLocationSettingsRequest();
                break;
        }
    }

    /**
     * Requests rcLocation updates from the FusedLocationApi.
     */
    @SuppressWarnings({"MissingPermission"})//because permission case has been handled perfectly
    protected void startLocationUpdates() {
/*
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback,
                            null */
/* Looper *//*
);
                } else {
                    mCurrentLocation = location;
                    locationsCallback.setLocation(mCurrentLocation);
                }
            }
        });
*/
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);


    }


    /*
     * Method to get address from lat and long
     * */
    public void getAddress() {
        if (mCurrentLocation != null)
            new ReverseGeocodingTask(mActivity, mCurrentLocation, locationsCallback).execute();
        else
            Toast.makeText(mActivity, "Please get rcLocation first", Toast.LENGTH_SHORT).show();
    }

    /*
     * Method to get rcLocation
     * */
    public void getLatAndLang(String address) {
        new DirectGeocodingTask(mActivity, address, locationsCallback).execute();
    }

    /*
     * Method to disconnect mGoogleApiClient
     * */
    public void disconnect() {
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
//            Toast.makeText(mActivity, "Disconnected", Toast.LENGTH_SHORT).show();
        }
//            Toast.makeText(mActivity, "Not connected", Toast.LENGTH_SHORT).show();
    }

    /**
     * This method is used to show permission allow from Setting
     * Please add this in utils or as per your requirement
     */
    public void showAllowPermissionFromSettingDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Enable RCLocation Permission");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", activity.getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                activity.finishAfterTransition();
            }
        });
        builder.create().show();
    }


}
