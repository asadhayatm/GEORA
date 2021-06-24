package com.geora.util;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.geora.Service.TestJobService;
import com.geora.constants.AppConstants;
import com.geora.data.constants.AppConstantClass;
import com.geora.ui.home.detailpage.DetailsActivity;
import com.google.android.gms.maps.model.LatLng;

public class ActivityUtils {

    private static JobScheduler jobScheduler;

    public static void startDetailsActivity(AppCompatActivity mActivity, Bundle bundle) {
        Intent intent = new Intent(mActivity, DetailsActivity.class);
        intent.putExtra(AppConstantClass.DATA, bundle);
        mActivity.startActivityForResult(intent, AppConstants.REQUEST_DETAIL_SCREEN);
    }

    public static void moveToGoogleMaps(Context context, LatLng fron, LatLng to) {
        String uri = "http://maps.google.com/maps?saddr=" + fron.latitude + "," +
                fron.longitude
                + "&daddr=" + to.latitude + "," +
                to.longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void scheduleJob(Context context) {

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                // Bluetooth is not enable :)
                ComponentName serviceComponent = new ComponentName(context, TestJobService.class);
                JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
                builder.setMinimumLatency(3 * 1000); // wait at least
                builder.setOverrideDeadline(10 * 1000); // maximum delay
                //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
                //builder.setRequiresDeviceIdle(true); // device should be idle
                //builder.setRequiresCharging(false); // we don't care if the device is charging or not
                if (jobScheduler == null) {
                    jobScheduler = context.getSystemService(JobScheduler.class);
                }else {
                    jobScheduler.cancelAll();
                }
                jobScheduler.schedule(builder.build());
            }
        }


    }
}
