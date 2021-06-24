package com.geora.Service;

import android.app.Activity;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.proximity_sdk.api.ProximityZoneContext;
import com.geora.estimote.ProximityContentManager;
import com.geora.listeners.BeaconListCallback;
import com.geora.util.AppUtils;

import java.util.List;
import java.util.Set;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

import static com.geora.GeoraClass.cloudCredentials;

public class TestJobService extends JobService implements BeaconListCallback {
    //  private static final String TAG = MyJobService.class.getSimpleName();
    private ProximityContentManager proximityContentManager;
    boolean isWorking = false;

    // Called by the Android system when it's time to run the job
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d("pfesssssss", "Job started!");
        isWorking = true;
        // We need 'jobParameters' so we can call 'jobFinished'
        startWorkOnNewThread(jobParameters); // Services do NOT run on a separate thread

        return isWorking;
    }

    private void startWorkOnNewThread(final JobParameters jobParameters) {
        new Thread(new Runnable() {
            public void run() {
                doWork(jobParameters);
            }
        }).start();
    }

    private void checkingForRequirments() {
        RequirementsWizardFactory
                .createEstimoteRequirementsWizard()
                .fulfillRequirements(((Activity) getApplicationContext()),
                        new Function0<Unit>() {
                            @Override
                            public Unit invoke() {
                                Log.d("app", "requirements fulfilled");
                                startProximityContentManager();
                                return null;
                            }
                        },
                        new Function1<List<? extends Requirement>, Unit>() {
                            @Override
                            public Unit invoke(List<? extends Requirement> requirements) {
                                Log.e("app", "requirements missing: " + requirements);
                                return null;
                            }
                        },
                        new Function1<Throwable, Unit>() {
                            @Override
                            public Unit invoke(Throwable throwable) {
                                Log.e("app", "requirements error: " + throwable);
                                return null;
                            }
                        });
    }

    private void startProximityContentManager(){
        proximityContentManager = new ProximityContentManager(this, this, cloudCredentials);
        proximityContentManager.stop();
        proximityContentManager.start();
    }

    private void doWork(JobParameters jobParameters) {
        // 10 seconds of working (1000*10ms)
        for (; ; ) {
            // If the job has been cancelled, stop working; the job will be rescheduled.

            //checkingForRequirments();



            Log.d("pfesssssss", "running!");

            try {
                Thread.sleep(5000);
            } catch (Exception e) {
            }
        }

//        isWorking = true;
//        boolean needsReschedule = false;
//        jobFinished(jobParameters, needsReschedule);
    }

    // Called if the job was cancelled before being finished
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d("pfesssssss", "Job cancelled before being completed.");
        // jobCancelled = true;
        boolean needsReschedule = isWorking;
        jobFinished(jobParameters, needsReschedule);
        return needsReschedule;
    }

    @Override
    public void onBeaconList(Set<? extends ProximityZoneContext> contexts) {

        Intent mIntent = new Intent("detect_broadcast_beacons");

        mIntent.putExtra("Beaconslist",contexts.toArray());
        LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent);

        String beaconId = "00ddd2b920063762994f479751550135";
        AppUtils.hitNotificationApi(beaconId);

    }
}