package com.geora.locationlibrary.interfaces;



import android.location.Address;
import android.location.Location;


public interface LocationsCallback {
    void setLocation(Location mCurrentLocation);
    void setAddress(String result);
    void setLatAndLong(Address location);
    void disconnect();
}
