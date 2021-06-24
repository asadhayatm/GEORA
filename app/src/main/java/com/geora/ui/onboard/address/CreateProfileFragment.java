package com.geora.ui.onboard.address;

import android.Manifest;
import android.app.Activity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseFragment;
import com.geora.locationlibrary.RCLocation;
import com.geora.locationlibrary.interfaces.LocationsCallback;
import com.geora.model.AddressModel;
import com.geora.model.FailureResponse;
import com.geora.model.createaddress.CreateAddressModel;
import com.geora.util.AppUtils;
import com.geora.util.PermissionUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.geora.util.AppUtils.checkGPSStatus;

public class CreateProfileFragment extends BaseFragment implements LocationsCallback {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.tv_current_address)
    TextView tvCurrentAddress;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_building)
    EditText tvBuilding;
    @BindView(R.id.tv_street)
    EditText tvStreet;
    @BindView(R.id.et_city)
    EditText etCity;
    @BindView(R.id.et_pincode)
    EditText etPincode;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.tv_promotion)
    TextView tvPromotion;
    @BindView(R.id.tv_fundraising)
    TextView tvFundraising;
    @BindView(R.id.tv_events)
    TextView tvEvents;
    @BindView(R.id.tv_sales)
    TextView tvSales;
    @BindView(R.id.tv_save_address)
    TextView tvSaveAddress;
    @BindView(R.id.main)
    RelativeLayout main;

    private Unbinder unbinder;
    private ICreateAddressHost mCreateAddress;
    private Context mContext;
    private RCLocation location;
    private int PLACE_PICKER_REQUEST = 1;
    private Address mAddress;
    private CreateAddressViewModel mCreateAddressViewModel;
    //    private int category = 0;
    private int all = 1, sale = 0, promotion = 0, events = 0, fundraising = 0;
    private AddressModel address = new AddressModel();
    private boolean isClicked = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getContext();
        initializeLocation();

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCreateAddressViewModel = ViewModelProviders.of(this).get(CreateAddressViewModel.class);
        mCreateAddressViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        //check for device token from repo, if it is not present then ask for token
        //getDeviceToken();

        //observing login live data
        mCreateAddressViewModel.getCreateProfileLivedata().observe(this, new Observer<CreateAddressModel>() {
            @Override
            public void onChanged(@Nullable CreateAddressModel userResponse) {
                if (userResponse != null) {
                    if (AppUtils.checkUserValid(getActivity(), userResponse.getCode(), userResponse.getMessage())) {
//                        isClicked = false;
                        hideProgressDialog();
                        mCreateAddress.steerToHomeActivity();
                    }
                }
            }
        });

        //observing validation live data
        mCreateAddressViewModel.getValidationLiveData().observe(this, new Observer<FailureResponse>() {
            @Override
            public void onChanged(@Nullable FailureResponse failureResponse) {
                isClicked = false;
                hideProgressDialog();
                if (failureResponse != null)
                    showSnackBar(main, failureResponse.getErrorMessage().toString(), true);

            }
        });


    }


    /*
     * initializing the location
     *
     * */
    private void initializeLocation() {
        if (location == null) {
            location = new RCLocation();
            location.setActivity((Activity) mContext);
            location.setCallback(this);
        }
    }

    @OnClick({R.id.tv_skip, R.id.tv_sales, R.id.tv_save_address, R.id.tv_events, R.id.tv_fundraising, R.id.tv_current_address, R.id.tv_promotion, R.id.tv_all, R.id.tv_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_current_address:
                currentAddressButtonClicked();
                break;
            case R.id.tv_save_address:
                if (!isClicked) {
                    isClicked = true;
                    saveBottonClicked();
                }
                break;
            case R.id.tv_skip:
                mCreateAddress.steerToHomeActivity();
                break;
            case R.id.tv_all:
                if (all == 0) {
                    all = 1;
                    events = 0;
                    sale = 0;
                    promotion = 0;
                    fundraising = 0;
                } else {
                    all = 0;
                }
                setAllFildes();
                break;
            case R.id.tv_fundraising:
                if (fundraising == 0) {
                    fundraising = 1;
                    all = 0;
                } else {
                    fundraising = 0;
                }
                setAllFildes();
                break;
            case R.id.tv_events:
                if (events == 0) {
                    events = 1;
                    all = 0;
                } else {
                    events = 0;
                }
                setAllFildes();
                break;
            case R.id.tv_sales:
                if (sale == 0) {
                    sale = 1;
                    all = 0;
                } else {
                    sale = 0;
                }
                setAllFildes();

                break;
            case R.id.tv_promotion:
                if (promotion == 0) {
                    promotion = 1;
                    all = 0;
                } else {
                    promotion = 0;
                }
                setAllFildes();
                break;
            case R.id.tv_address:
                /*PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                Intent intent = null;
                try {
                    intent = intentBuilder.build(getActivity());
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

                startActivityForResult(intent, PLACE_PICKER_REQUEST);*/
              openAutocomplete();
                break;
        }
    }

    /**
     * function to changed click
     */
    private void changedClicked() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isClicked = false;
            }
        }, 2000);
    }

    private void setAllFildes() {
        if (fundraising == 0) {
            tvFundraising.setBackground(mContext.getResources().getDrawable(R.drawable.yellow_border_rect));
            tvFundraising.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        } else {
            tvFundraising.setBackground(mContext.getResources().getDrawable(R.drawable.yellow_border_rect_filed));
            tvFundraising.setTextColor(mContext.getResources().getColor(R.color.black_two_));
        }

        if (events == 0) {
            tvEvents.setBackground(mContext.getResources().getDrawable(R.drawable.yellow_border_rect));
            tvEvents.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        } else {
            tvEvents.setBackground(mContext.getResources().getDrawable(R.drawable.yellow_border_rect_filed));
            tvEvents.setTextColor(mContext.getResources().getColor(R.color.black_two_));
        }

        if (promotion == 0) {
            tvPromotion.setBackground(mContext.getResources().getDrawable(R.drawable.yellow_border_rect));
            tvPromotion.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        } else {
            tvPromotion.setBackground(mContext.getResources().getDrawable(R.drawable.yellow_border_rect_filed));
            tvPromotion.setTextColor(mContext.getResources().getColor(R.color.black_two_));
        }

        if (sale == 0) {
            tvSales.setBackground(mContext.getResources().getDrawable(R.drawable.yellow_border_rect));
            tvSales.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        } else {
            tvSales.setBackground(mContext.getResources().getDrawable(R.drawable.yellow_border_rect_filed));
            tvSales.setTextColor(mContext.getResources().getColor(R.color.black_two_));
        }
        if (sale == 0 && fundraising == 0 && promotion == 0 && events == 0) {
            tvAll.setBackground(mContext.getResources().getDrawable(R.drawable.yellow_border_rect_filed));
            tvAll.setTextColor(mContext.getResources().getColor(R.color.black_two_));
        } else {
            tvAll.setBackground(mContext.getResources().getDrawable(R.drawable.yellow_border_rect));
            tvAll.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
    }

    private void saveBottonClicked() {
        address.setAll(all);
        address.setEvents(events);
        address.setPromotion(promotion);
        address.setSales(sale);
        address.setFundraising(fundraising);
        if (isInternetAvailable()) {

            showProgressDialog();
            mCreateAddressViewModel.saveAddressButtonClicked(address);
        } else {
            showSnackBar(main, mContext.getResources().getString(R.string.no_internet_connection), true);
        }
    }

    private void currentAddressButtonClicked() {
        if (checkGPSStatus(mContext)) {
            location.startLocation();
        } else {
            showSnackBar(main, mContext.getResources().getString(R.string.please_check_your_gps), true);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (String permission : permissions) {
            if (permission.equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (PermissionUtils.checkMultiplePermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION})) {
                    location.startLocation();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ICreateAddressHost) {
            mCreateAddress = (ICreateAddressHost) context;
        } else
            throw new IllegalStateException("host must implement Host");
    }

    @Override
    public void setLocation(Location mCurrentLocation) {
        location.getAddress();
    }

    @Override
    public void setAddress(String result) {
        location.getLatAndLang(result);
    }

    @Override
    public void setLatAndLong(Address location) {
        if (isAdded() && location != null) {
            mAddress = location;
            tvAddress.setText(updateAddress("" + location.getPremises() + "," + location.getSubLocality() + "," + location.getSubAdminArea() + "," + location.getLocality() + "," + location.getCountryName()));
            etCity.setText(updateAddress("" + location.getAdminArea()));
            etPincode.setText(updateAddress("" + location.getPostalCode()));
            tvBuilding.setText(updateAddress("" + location.getPremises() + "," + location.getSubLocality()));
            tvStreet.setText(updateAddress("" + location.getLocality()));
            address.setFlatNo(updateAddress("" + mAddress.getPremises() + "," + mAddress.getSubLocality()));
            address.setCity(updateAddress("" + mAddress.getAdminArea()));
            address.setStreet(updateAddress("" + mAddress.getSubAdminArea()));
            address.setState(updateAddress("" + mAddress.getLocality()));
            address.setPincode(updateAddress("" + mAddress.getPostalCode()));
            address.setCity(updateAddress("" + mAddress.getAdminArea()));
            address.setLat(mAddress.getLatitude());
            address.setLng(mAddress.getLongitude());
            address.setCountryCode(updateAddress("" + AppUtils.getCountryDialCode(mContext)));
            address.setFormattedAddress(updateAddress("" + location.getPremises() + "," + location.getSubLocality() + "," + location.getSubAdminArea() + "," + location.getLocality() + "," + location.getCountryName()));

        }
    }

    @Override
    public void disconnect() {

    }

    public interface ICreateAddressHost {
        void steerToHomeActivity();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
//                final Place place = PlacePicker.getPlace(data, mContext);
                final Place place = Autocomplete.getPlaceFromIntent(data);
                LatLng coordinates = place.getLatLng(); // Get the coordinates from your place
                Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
                List<Address> addresses = null; // Only retrieve 1 address
                try {
                    addresses = geocoder.getFromLocation(
                            coordinates.latitude,
                            coordinates.longitude,
                            1);

                    mAddress = addresses.get(0);
                    if (place.getAddress() != null && !place.getAddress().equals("")) {
                        tvAddress.setText(updateAddress("" + place.getAddress()));
                    } else {
                        tvAddress.setText(updateAddress("" + mAddress.getPremises() + "," + mAddress.getSubLocality() + "," + mAddress.getSubAdminArea() + "," + mAddress.getLocality() + "," + mAddress.getCountryName()));
                    }
                    etCity.setText(updateAddress("" + mAddress.getAdminArea()));
                    etPincode.setText(updateAddress("" + mAddress.getPostalCode()));
                    tvBuilding.setText(updateAddress("" + mAddress.getPremises() + "," + mAddress.getSubLocality()));
                    tvStreet.setText(updateAddress("" + mAddress.getLocality()));
                    address.setFlatNo(updateAddress("" + mAddress.getPremises() + "," + mAddress.getSubLocality()));
                    address.setCity(updateAddress("" + mAddress.getAdminArea()));
                    address.setStreet(updateAddress("" + mAddress.getSubAdminArea()));
                    address.setState(updateAddress("" + mAddress.getLocality()));
                    address.setPincode(updateAddress("" + mAddress.getPostalCode()));
                    address.setCity(updateAddress("" + mAddress.getAdminArea()));
                    address.setLat(mAddress.getLatitude());
                    address.setLng(mAddress.getLongitude());
                    address.setCountryCode(updateAddress("" + AppUtils.getCountryDialCode(mContext)));
                    if (place.getAddress() != null && !place.getAddress().equals("")) {
                        address.setFormattedAddress(updateAddress("" + place.getAddress()));
                    } else {
                        address.setFormattedAddress(updateAddress("" + mAddress.getPremises() + "," + mAddress.getSubLocality() + "," + mAddress.getSubAdminArea() + "," + mAddress.getLocality() + "," + mAddress.getCountryName()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private String updateAddress(String address) {
        return address.replaceAll("null,", "").replaceAll("null", "");
    }


    /**
     * function to open place autocomplete
     */
    private void openAutocomplete() {
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields;
        fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
// Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(mContext);
        CreateProfileFragment.this.startActivityForResult(intent, PLACE_PICKER_REQUEST);
    }

    @Override
    protected void onFailure(FailureResponse failureResponse) {
        isClicked = false;
        super.onFailure(failureResponse);
        hideProgressDialog();
        //showSnackBar(main, failureResponse.getErrorMessage().toString(), true);

    }

    @Override
    protected void onErrorOccurred(Throwable throwable) {
        isClicked = false;
        super.onErrorOccurred(throwable);
        hideProgressDialog();
        //showSnackBar(main, throwable.getMessage(), true);

    }
}
