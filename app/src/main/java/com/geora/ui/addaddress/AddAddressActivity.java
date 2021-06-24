package com.geora.ui.addaddress;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.geora.R;
import com.geora.base.BaseActivity;
import com.geora.constants.AppConstants;
import com.geora.data.constants.AppConstantClass;
import com.geora.locationlibrary.RCLocation;
import com.geora.locationlibrary.interfaces.LocationsCallback;
import com.geora.model.AddressModel;
import com.geora.model.CountryCode;
import com.geora.model.FailureResponse;
import com.geora.model.addaddress.AddAddressModel;
import com.geora.model.addresslist.Datum;
import com.geora.ui.onboard.countrycode.CountryCodeFragment;
import com.geora.util.AppUtils;
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

import static com.geora.util.AppUtils.checkGPSStatus;

public class AddAddressActivity extends BaseActivity implements CountryCodeFragment.ICountryCodeHost, LocationsCallback {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_building)
    EditText tvBuilding;
    @BindView(R.id.tv_state)
    EditText tvState;
    @BindView(R.id.et_city)
    EditText etCity;
    @BindView(R.id.et_pincode)
    EditText etPincode;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_cc)
    TextView tvCc;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.rb_other)
    RadioButton rbOther;
    @BindView(R.id.rb_work)
    RadioButton rbWork;
    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.tv_save_address)
    TextView tvSaveAddress;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.main)
    RelativeLayout main;


    private Datum addressListModel;
    private Unbinder unbinder;
    private AddAddressViewModel mAddAddressViewModel;

    private int PLACE_PICKER_REQUEST = 1;
    private Address mAddress;
    private AddressModel address = new AddressModel();
    private String type = "";
    private boolean isLoading = false;
    private RCLocation location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_address);
        unbinder = ButterKnife.bind(this);
        tvCc.setText(AppUtils.getUserCountryCode(this));
        addressListModel = getIntent().getParcelableExtra(AppConstantClass.APIConstant.ADDRESS);
        type = getIntent().getStringExtra(AppConstantClass.APIConstant.TYPE);
        tvTitle.setText(getResources().getString(R.string.add_new_address));
        initializeLocation();
        if (type != null && type.equals("edit")) {
            tvTitle.setText(getResources().getString(R.string.edit_address));
        }
        radioButtonListener();
        if (addressListModel != null) {
            setDefaultData(addressListModel);
        }

        mAddAddressViewModel = ViewModelProviders.of(this).get(AddAddressViewModel.class);
        mAddAddressViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        //check for device token from repo, if it is not present then ask for token

        mAddAddressViewModel.getAddAddressModelLiveData().observe(this, new Observer<AddAddressModel>() {
            @Override
            public void onChanged(@Nullable AddAddressModel addressModel) {
//                isLoading = false;
                hideProgressDialog();
                if (addressModel != null) {
                    if (AppUtils.checkUserValid(AddAddressActivity.this, addressModel.getCode(), addressModel.getMessage())) {
                        if (addressListModel == null)
                            showSnackBar(main, getResources().getString(R.string.address_added_successfully), false);
                        else
                            showSnackBar(main, getResources().getString(R.string.address_edited_successfully), false);
                        finishAfterTransition();
                    }
                }
            }
        });

        //observing validation live data
        mAddAddressViewModel.getValidationLiveData().observe(this, new Observer<FailureResponse>() {
            @Override
            public void onChanged(@Nullable FailureResponse failureResponse) {
                if (failureResponse != null) {
                    isLoading = false;
                    hideProgressDialog();
                    showSnackBar(main, failureResponse.getErrorMessage().toString(), true);
                }
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
            location.setActivity(this);
            location.setCallback(this);
        }
    }


    private void setDefaultData(Datum addressListModel) {
        tvAddress.setText(addressListModel.getFormattedAddress());
        etCity.setText(addressListModel.getCity());
        etPincode.setText(addressListModel.getPinCode() + "");
        tvBuilding.setText(addressListModel.getFlatNo());
        tvState.setText(addressListModel.getState());
        etName.setText(addressListModel.getFullName());
        etMobile.setText(addressListModel.getMobileNo());
        if (addressListModel.getCountryCode() != null && !addressListModel.getCountryCode().equals(""))
            tvCc.setText(addressListModel.getCountryCode());
        if (addressListModel.getAddressType().equalsIgnoreCase(getResources().getString(R.string._home))) {
            rbHome.setChecked(true);
            rbOther.setChecked(false);
            rbWork.setChecked(false);
            address.setAddressType(getResources().getString(R.string._home));
        } else if (addressListModel.getAddressType().equalsIgnoreCase(getResources().getString(R.string._work))) {
            rbHome.setChecked(false);
            rbOther.setChecked(false);
            rbWork.setChecked(true);
            address.setAddressType(getResources().getString(R.string._work));
        } else {
            rbHome.setChecked(false);
            rbOther.setChecked(true);
            rbWork.setChecked(false);
            address.setAddressType(getResources().getString(R.string._other));
        }
        address.setAddressType(addressListModel.getAddressType());

    }

    private void radioButtonListener() {
        address.setAddressType(getResources().getString(R.string._home));
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_home) {
                    address.setAddressType(getResources().getString(R.string._home));
                } else if (checkedId == R.id.rb_other) {
                    address.setAddressType(getResources().getString(R.string._other));
                } else if (checkedId == R.id.rb_work) {
                    address.setAddressType(getResources().getString(R.string._work));
                }
            }
        });
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_ad_address;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_cc, R.id.tv_address, R.id.tv_save_address, R.id.tv_current_address, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_current_address:
                currentAddressButtonClicked();
                break;
                case R.id.tv_save_address:
                if (!isLoading) {
                    isLoading = true;
                    if (isInternetAvailable()) {
                        showProgressDialog();
                        address.setName(etName.getText().toString());
                        address.setMobileNo(etMobile.getText().toString());
                        address.setCountryCode(tvCc.getText().toString());
                        address.setFlatNo(tvBuilding.getText().toString());
                        address.setCity(etCity.getText().toString());
                        address.setState(tvState.getText().toString());
                        address.setFormattedAddress(tvAddress.getText().toString());
                        address.setPincode(etPincode.getText().toString());
                        if (addressListModel != null) {
                            address.setAddressId(addressListModel.getId() + "");
                            address.setAction(getResources().getString(R.string._edit));
                        } else {
                            address.setAction(getResources().getString(R.string._save));
                        }
                        mAddAddressViewModel.submutClicked(address, isInternetAvailable());

                    } else {
                        isLoading = false;
                        showSnackBar(main, getResources().getString(R.string.no_internet_connection), true);
                    }
                }
                break;
            case R.id.tv_cc:
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_add_address);
                if (!(fragment instanceof CountryCodeFragment))
                    addFragment(R.id.fl_add_address, new CountryCodeFragment(), CountryCodeFragment.class.getSimpleName());
                break;
            case R.id.iv_back:
                finishAfterTransition();
                break;

            case R.id.tv_address:
                /*PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                Intent intent = null;
                try {
                    intent = intentBuilder.build(this);
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


    private void currentAddressButtonClicked() {
        if (checkGPSStatus(this)) {
            location.startLocation();
        } else {
            showSnackBar(main, getResources().getString(R.string.please_check_your_gps), true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK & data!= null) {
//                final Place place = PlacePicker.getPlace(data, this);
                final Place place = Autocomplete.getPlaceFromIntent(data);
                LatLng coordinates = place.getLatLng(); // Get the coordinates from your place
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null; // Only retrieve 1 address
                try {
                    addresses = geocoder.getFromLocation(
                            coordinates.latitude,
                            coordinates.longitude,
                            1);

                    mAddress = addresses.get(0);
                    if (mAddress != null) {
                        if (place.getAddress() != null && !place.getAddress().equals("")) {
                            tvAddress.setText(updateAddress("" + place.getAddress()));
                        } else {
                            tvAddress.setText(updateAddress("" + mAddress.getPremises() + "," + mAddress.getSubLocality() + "," + mAddress.getSubAdminArea() + "," + mAddress.getLocality() + "," + mAddress.getCountryName()));
                        }
                        etCity.setText(updateAddress("" + mAddress.getLocality()));
                        etPincode.setText(updateAddress("" + mAddress.getPostalCode()));
                        tvBuilding.setText(updateAddress("" + mAddress.getPremises() + "," + mAddress.getSubLocality()));
                        tvState.setText(updateAddress("" + mAddress.getAdminArea()));
                        address.setStreet(updateAddress("" + mAddress.getSubAdminArea()));
                        address.setLat(mAddress.getLatitude());
                        address.setLng(mAddress.getLongitude());
                        if (place.getAddress() != null && !place.getAddress().equals("")) {
                            address.setFormattedAddress(updateAddress("" + place.getAddress()));
                        } else {
                            address.setFormattedAddress(updateAddress("" + mAddress.getPremises() + "," + mAddress.getSubLocality() + "," + mAddress.getSubAdminArea() + "," + mAddress.getLocality() + "," + mAddress.getCountryName()));
                        }
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
                .build(this);
        startActivityForResult(intent, PLACE_PICKER_REQUEST);
    }

    @Override
    public void closeCurrentFragmentWithData(Bundle bundle) {
        removeFragmnet();
        if (bundle != null) {
            CountryCode countryCode = bundle.getParcelable(AppConstants.BundleConstants.COUNTRY_DATA);
            tvCc.setText("+" + countryCode.getCountryCode());
        }

    }

    @Override
    public void closeCurrentFragment() {
        removeFragmnet();
    }

    private void removeFragmnet() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_add_address);
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @Override
    protected void onFailure(FailureResponse failureResponse) {
        isLoading = false;
        hideProgressDialog();
        super.onFailure(failureResponse);
        //showSnackBar(main, failureResponse.getErrorMessage().toString(), true);
        if (failureResponse.getErrorCode() == 422) {
            showToastLong(getResources().getString(R.string.something_went_wrong));
//            logout(this);
        }
        if (failureResponse.getErrorCode() == 401) {
            showToastLong(getResources().getString(R.string.user_is_blocked_by_admin));
            logout(this);
        }
    }

    @Override
    protected void onErrorOccurred(Throwable throwable) {
        isLoading = false;
        hideProgressDialog();
        super.onErrorOccurred(throwable);
        //showSnackBar(main, throwable.getMessage(), true);

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
        if (location != null) {
            mAddress = location;
            tvAddress.setText(updateAddress("" + location.getPremises() + "," + location.getSubLocality() + "," + location.getSubAdminArea() + "," + location.getLocality() + "," + location.getCountryName()));
            etCity.setText(updateAddress("" + location.getAdminArea()));
            etPincode.setText(updateAddress("" + location.getPostalCode()));
            tvBuilding.setText(updateAddress("" + location.getPremises() + "," + location.getSubLocality()));
            tvState.setText(updateAddress("" + location.getLocality()));
            address.setFlatNo(updateAddress("" + mAddress.getPremises() + "," + mAddress.getSubLocality()));
            address.setCity(updateAddress("" + mAddress.getAdminArea()));
            address.setStreet(updateAddress("" + mAddress.getSubAdminArea()));
            address.setState(updateAddress("" + mAddress.getLocality()));
            address.setPincode(updateAddress("" + mAddress.getPostalCode()));
            address.setCity(updateAddress("" + mAddress.getAdminArea()));
            address.setLat(mAddress.getLatitude());
            address.setLng(mAddress.getLongitude());
            address.setCountryCode(updateAddress("" + AppUtils.getCountryDialCode(this)));
            address.setFormattedAddress(updateAddress("" + location.getPremises() + "," + location.getSubLocality() + "," + location.getSubAdminArea() + "," + location.getLocality() + "," + location.getCountryName()));

        }
    }

    @Override
    public void disconnect() {

    }
}
