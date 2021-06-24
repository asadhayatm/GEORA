package com.geora;

import android.app.Application;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.Log;

import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;
import com.geora.data.DataManager;
import com.geora.util.ActivityUtils;
import com.geora.util.ResourceUtils;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.FirebaseApp;

import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.util.Locale;

@ReportsCrashes(mailTo = /*"shaifali.pundir@appinventiv.com"*/"sachin.upreti@appinventiv.com",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)

public class GeoraClass extends Application implements LifecycleObserver {
    public static EstimoteCloudCredentials cloudCredentials =
            new EstimoteCloudCredentials("derrick-vavidtech-com-s-yo-n9s", "fd179dea882b9fc45865ed95ae1bcf2f");
//            new EstimoteCloudCredentials("chetan-sharma-s-proximity--8qt", "50310d0e2cde77468d7fe426ed4eb993");


    private static Context context;

    public static Context getContext() {
        return context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        ResourceUtils.init(this);
        FirebaseApp.initializeApp(this);
//        ACRA.init(this);

//        scheduleJob();

        DataManager dataManager = DataManager.init(context);
        dataManager.initApiManager();

        Places.initialize(getApplicationContext(), getString(R.string.google_place_key), Locale.getDefault());

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded() {
//        SocketManager.disconnectSocket();
        Log.d("MyApp", "App in background");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded() {
//        SocketManager.init();
        Log.d("MyApp", "App in foreground");
    }

    /**
     * function to schedule job
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void scheduleJob() {
        ActivityUtils.scheduleJob(this);
    }
}
