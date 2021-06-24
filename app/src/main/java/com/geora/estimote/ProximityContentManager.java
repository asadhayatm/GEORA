package com.geora.estimote;

import android.content.Context;

import androidx.core.app.NotificationCompat;

import android.util.Log;
import android.widget.Toast;

import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;
import com.estimote.proximity_sdk.api.ProximityObserver;
import com.estimote.proximity_sdk.api.ProximityObserverBuilder;
import com.estimote.proximity_sdk.api.ProximityZone;
import com.estimote.proximity_sdk.api.ProximityZoneBuilder;
import com.estimote.proximity_sdk.api.ProximityZoneContext;
import com.geora.data.DataManager;
import com.geora.listeners.BeaconListCallback;
import com.geora.util.AppUtils;

import java.util.Set;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class ProximityContentManager {

    private final Context context;
    private final EstimoteCloudCredentials cloudCredentials;
    private final BeaconListCallback beaconListCallback;
    private ProximityObserver.Handler proximityObserverHandler;

    public ProximityContentManager(Context context, BeaconListCallback beaconListCallback, EstimoteCloudCredentials cloudCredentials) {
        this.context = context;
        this.cloudCredentials = cloudCredentials;
        this.beaconListCallback = beaconListCallback;
    }

    public void start() {

        ProximityObserver proximityObserver = new ProximityObserverBuilder(context, cloudCredentials)
                .onError(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        Log.e("app", "proximity observer error: " + throwable);
//                        Toast.makeText(context, "proximity observer error: " + throwable, Toast.LENGTH_LONG).show();
                        return null;
                    }
                })
                .withLowLatencyPowerMode()
                .withAnalyticsReportingDisabled()
                .withEstimoteSecureMonitoringDisabled()
                .build();

//        Toast.makeText(context, "proximity observer start", Toast.LENGTH_LONG).show();;

/*
        ProximityZone zone = new ProximityZoneBuilder()
//                .forTag("chetan-sharma-s-proximity--8qt"
                .forTag("derrick-vavidtech-com-s-yo-n9s") // development
//                .forTag("beacon-proximity-test-c3w")// client
                .inCustomRange(Double.parseDouble(DataManager.getInstance().getProximityRange() == null ? "5" : DataManager.getInstance().getProximityRange()))
                .onEnter(new Function1<ProximityZoneContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityZoneContext proximityZoneContext) {
                        HashSet<ProximityZoneContext> set = new HashSet<>();
                        set.add(proximityZoneContext);
                        beaconListCallback.onBeaconList(set);
                        for (ProximityZoneContext prox : set) {
                            String device_id = prox.getDeviceId();
                            Toast.makeText(context, "Beacons detected " +device_id, Toast.LENGTH_LONG).show();
                        }
                        Log.e("Check beacons:", "beacons detected");
                        return null;
                    }
                })
                .onExit(new Function1<ProximityZoneContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityZoneContext proximityZoneContext) {
//                        Toast.makeText(context,"Beacons removed",Toast.LENGTH_LONG).show();
                        Log.e("Check beacons:", "beacons removed");
                        return null;
                    }
                })
                .build();
*/
        ProximityZone zone = new ProximityZoneBuilder()
//                .forTag("chetan-sharma-s-proximity--8qt"
                .forTag("derrick-vavidtech-com-s-yo-n9s")
                .inCustomRange(Double.parseDouble(DataManager.getInstance().getProximityRange() == null ? "5" : DataManager.getInstance().getProximityRange()))
                .onContextChange(new Function1<Set<? extends ProximityZoneContext>, Unit>() {
                    @Override
                    public Unit invoke(Set<? extends ProximityZoneContext> contexts) {
                        beaconListCallback.onBeaconList(contexts);
                        for (ProximityZoneContext prox : contexts) {
                            String device_id = prox.getDeviceId();
//                            Toast.makeText(context, "Beacons detected " +device_id, Toast.LENGTH_LONG).show();
                            Log.e("Check beacons:", "Beacons detected::: " + device_id);
//                            Toast.makeText(context, "Beacons detected::: " + device_id, Toast.LENGTH_LONG).show();;
                        }
                        Log.e("Check beacons:", "beacons detected");
//                        Toast.makeText(context, "Beacons detected", Toast.LENGTH_LONG).show();;
                        return null;
                    }
                })
                .build();

        proximityObserverHandler = proximityObserver.startObserving(zone);
    }

    public void stop() {
        if (proximityObserverHandler != null)
            proximityObserverHandler.stop();
    }

}
