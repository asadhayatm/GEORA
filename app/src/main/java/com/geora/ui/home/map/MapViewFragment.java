package com.geora.ui.home.map;

import android.Manifest;
import android.app.Activity;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseFragment;
import com.geora.data.DataManager;
import com.geora.locationlibrary.RCLocation;
import com.geora.locationlibrary.interfaces.LocationsCallback;
import com.geora.model.beaconlist.BeaconListModel;
import com.geora.model.beaconlist.Datum;
import com.geora.socket.SocketCallback;
import com.geora.socket.SocketConstant;
import com.geora.ui.home.HomeActivity;
import com.geora.ui.home.HomeViewModel;
import com.geora.util.AppUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.java_websocket.handshake.ServerHandshake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

import static com.geora.util.ActivityUtils.moveToGoogleMaps;

public class MapViewFragment extends BaseFragment implements OnMapReadyCallback, SocketCallback, GoogleMap.OnMarkerClickListener, LocationsCallback {

    @BindView(R.id.map_view)
    MapView mMapView;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address_)
    TextView tvAddress_;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.tv_direction)
    TextView tvDirection;
    @BindView(R.id.tv_view_details)
    TextView tvViewDetails;
    @BindView(R.id.rl_details)
    RelativeLayout rlDetails;
    @BindView(R.id.iv_info)
    ImageView ivInfo;

    private Unbinder unbinder;
    private Context mContext;
    private GoogleMap mGoogleMap;
    private Handler handler;
    private Runnable runnable;
    private List<Datum> beaconList;
    private int pos = -1;
    private RCLocation location;
    private LatLng latLng = null;
    private HomeViewModel mHomeViewModel;
    private SimpleTooltip tooltip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mHomeViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        mContext = getContext();
        handler = new Handler();
        ((HomeActivity) mContext).setupToolbar(2, "");
        initializeLocation();
        runnable = () -> {
            if (isAdded()) {
                mapInit(savedInstanceState);
            }
        };
        handler.postDelayed(runnable, 10);

        setObservers();

        getBeaconsOnMap();
    }

    private void getBeaconsOnMap() {
//        JSONObject jsonObject = new JSONObject();
        /*try {
            jsonObject.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, SocketConstant.SOCKEYKEYS.INIT);
            jsonObject.put(SocketConstant.SOCKEYKEYS.IS_SUBSCRIBED_USER, false);
            jsonObject.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (isInternetAvailable())
            SocketManager.getInstance().sendData(jsonObject.toString());*/


        HashMap<String, Object> params = new HashMap<>();

        params.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, SocketConstant.SOCKEYKEYS.INIT);
        params.put(SocketConstant.SOCKEYKEYS.IS_SUBSCRIBED_USER, false);
        params.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());

        if (isInternetAvailable())
            mHomeViewModel.hitBeaconApi(params);
        else {
            showToastLong(mContext.getResources().getString(R.string.no_internet_connection));
        }
    }

    private void connectWebSocket(SocketCallback callback) {
//        SocketManager.getInstance().setSocketCallbackListener(callback);
    }

    @Override
    public void onResume() {
        super.onResume();
//        connectWebSocket(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        connectWebSocket(null);
    }

    private void mapInit(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (isAdded()) {
            mGoogleMap = googleMap;
            mGoogleMap.setPadding(50, 50, 50, 50);
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                MapViewFragment.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
                return;
            }else {
                location.startLocation();
            }


//        addMarkerAtCurrentocation();

            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.setMyLocationEnabled(true);
            googleMap.setOnMarkerClickListener(this);
            // Get the button view
            View locationButton = ((View) mMapView.findViewById(1).getParent()).findViewById(2);

            // and next place it, for exemple, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            rlp.setMargins(0, 0, 30, 30);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for (int i = 0, len = permissions.length; i < len; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                // user rejected the permission
                boolean showRationale = shouldShowRequestPermissionRationale(permission);
                if (!showRationale) {
                    showToastLong(getString(R.string.allow_location_permission));
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            } else {
                location.startLocation();
            }
        }
    }

    private void addMarkerAtCurrentocation() {
        if (isAdded()) {
            if (latLng != null) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(15f).build();
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin_location));
                Marker marker = mGoogleMap.addMarker(markerOptions);
                marker.setTag(-1);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (mMapView != null)
            mMapView.onDestroy();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @OnClick({R.id.tv_direction, R.id.iv_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_direction:
                if (pos != -1) {
                    try {
                        moveToGoogleMaps(mContext, latLng, new LatLng(Double.parseDouble(beaconList.get(pos).getLat()), Double.parseDouble(beaconList.get(pos).getLng())));
                    } catch (Exception e) {
                        showSnackBar(((HomeActivity) mContext).getParentLayout(), "Google maps is missing or something went wrong", true);
                    }
                }

                break;
            case R.id.iv_info:
                try {
                    if (tooltip != null && tooltip.isShowing()) {
                        tooltip.dismiss();
                    }
                    else {
                        tooltip = new SimpleTooltip.Builder(mContext)
                                .anchorView(ivInfo)
                                .text(getString(R.string.map_message))
                                .gravity(Gravity.BOTTOM)
                                .animated(true)
                                .transparentOverlay(false)
                                .build();
                        tooltip.show();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }


    @Override
    public void onMessage(String msg) {
        if (isAdded()) {
            final BeaconListModel beaconListModel = new Gson().fromJson(msg, BeaconListModel.class);
            if (AppUtils.checkUserValid(getActivity(), beaconListModel.getCode(), beaconListModel.getMessage())) {
                if (beaconListModel.getCode() == 423 || beaconListModel.getCode() == 403) {
                    showToastShort(beaconListModel.getMessage());
                    logout(mContext);
                } else {
                    if (beaconListModel.getActionType().equalsIgnoreCase(SocketConstant.SOCKEYKEYS.INIT)) {
                        ((Activity) mContext).runOnUiThread(() -> setBeaconsOnMap(beaconListModel.getData()));
                    }
                }
            }
        }

    }


    private void setObservers() {
        mHomeViewModel.getBeaconLiveData().observe(this, beaconListModel -> {
            if (isAdded()) {
                hideProgressDialog();
                if (AppUtils.checkUserValid(getActivity(), beaconListModel.getCode(), beaconListModel.getMessage())) {
                    if (beaconListModel.getCode() == 423 || beaconListModel.getCode() == 403) {
                        showToastShort(beaconListModel.getMessage());
                        logout(mContext);
                    } else {
                        ((Activity) mContext).runOnUiThread(() -> setBeaconsOnMap(beaconListModel.getData()));
                    }
                }
            }
        });

    }

    private void setBeaconsOnMap(List<Datum> data) {
        if (isAdded()) {
            beaconList = new ArrayList<>();
            beaconList.addAll(data);
            if (mGoogleMap != null) {
                if (beaconList.size() != 0) {
                    mGoogleMap.clear();
                    addMarkerAtCurrentocation();
                }
                for (int i = 0; i < data.size(); i++) {
                    Datum beacon = data.get(i);
                    if (beacon.getLng() != null && beacon.getLat() != null) {
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(beacon.getLat()), Double.parseDouble(beacon.getLng())))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_beacon_pin));
                        Marker marker = mGoogleMap.addMarker(markerOptions);
                        marker.setTag(i);
                    }
                }
            }
        }
    }

    /*
     * initializing the location
     *
     * */
    private void initializeLocation() {
        location = new RCLocation();
        location.setActivity((Activity) mContext);
        location.setCallback(this);
//        location.startLocation();
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (isAdded()) {
            int tag = (int) marker.getTag();
            if (tag != -1) {
                showMarkerDetails(tag);
            }
        }
        return true;
    }

    private void showMarkerDetails(int tag) {
        pos = tag;
        rlDetails.setVisibility(View.VISIBLE);
        tvName.setText(beaconList.get(tag).getBusinessUserName());
        tvAddress_.setText(beaconList.get(tag).getBeaconLocation());
        tvCategory.setText(beaconList.get(tag).getCategoryName());
    }

    @Override
    public void setLocation(Location mCurrentLocation) {
        if (isAdded()) {
            latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            location.getAddress();
            addMarkerAtCurrentocation();
            getBeaconsOnMap();
        }
    }

    @Override
    public void setAddress(String result) {
        if (isAdded()) {
            ((HomeActivity) mContext).setAddress(result);
        }
    }

    @Override
    public void setLatAndLong(Address location) {

    }

    @Override
    public void disconnect() {

    }
}
