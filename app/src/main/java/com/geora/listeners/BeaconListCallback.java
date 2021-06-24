package com.geora.listeners;

import android.view.View;

import com.estimote.proximity_sdk.api.ProximityZoneContext;

import java.util.List;
import java.util.Set;

public interface BeaconListCallback {
    void onBeaconList( Set<? extends ProximityZoneContext> contexts);

}
