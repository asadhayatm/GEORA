package com.geora.locationlibrary;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.geora.locationlibrary.interfaces.LocationsCallback;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by appinventiv on 12/9/17.
 */

public class ReverseGeocodingTask extends AsyncTask<String, String, String> {
    Context mContext;
    private android.location.Location location;
    private LocationsCallback locationsCallback;

    public ReverseGeocodingTask(Context context, android.location.Location location,LocationsCallback locationsCallback) {
        super();
        mContext = context;
        this.location = location;
        this.locationsCallback = locationsCallback;
    }

    @Override
    protected String doInBackground(String... params) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        String addressText = null;
        List<Address> addresses = null;
        try {
            // Call the synchronous getFromLocation() method by passing in the lat/long values.
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            // Format the first line of address (if available), city, and country name.
         /*   addressText = String.format("%s, %s, %s",
                    address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                    address.getLocality(),
                    address.getCountryName());*/ // by shaifali

            addressText=address.getAddressLine(0);
        }
        return addressText;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        locationsCallback.setAddress(result);
    }
}