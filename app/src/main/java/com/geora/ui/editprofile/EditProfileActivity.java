package com.geora.ui.editprofile;


import android.Manifest;
import android.app.DatePickerDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dnitinverma.amazons3library.AmazonS3;
import com.dnitinverma.amazons3library.interfaces.AmazonCallback;
import com.dnitinverma.amazons3library.model.MediaBean;
import com.geora.R;
import com.geora.base.BaseActivity;
import com.geora.constants.AppConstants;
import com.geora.customviews.DialogForUpload;
import com.geora.data.constants.AppConstantClass;
import com.geora.listeners.DialogCallback;
import com.geora.model.CountryCode;
import com.geora.model.FailureResponse;
import com.geora.model.editprofile.EditProfileResponse;
import com.geora.model.request.User;
import com.geora.ui.onboard.OnBoardActivity;
import com.geora.ui.onboard.countrycode.CountryCodeFragment;
import com.geora.util.AppUtils;
import com.geora.util.DateTimeUtils;
import com.geora.util.ImageUtils;
import com.geora.util.PermissionUtils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import appinventiv.com.imagecropper.cicularcropper.CropImageView;
import appinventiv.com.imagecropper.cicularcropper.ImageCropper;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static appinventiv.com.imagecropper.cicularcropper.ImageCropper.CROP_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.geora.constants.AppConstants.REQUEST_PERMISSION_GALLEY;
import static com.geora.constants.AppConstants.TAKE_PICTURE;
import static com.geora.util.DateTimeUtils.DOBtoSendFormatDate;
import static com.geora.util.DateTimeUtils.dobToTimeStampForPicker;
import static com.geora.util.DateTimeUtils.parseDOBDate;

public class EditProfileActivity extends BaseActivity implements AmazonCallback, CountryCodeFragment.ICountryCodeHost {
    @BindView(R.id.iv_back)
    ImageView ivMenu;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.tv_user_email_id)
    TextView tvUserEmailId;
    @BindView(R.id.et_mobile_number)
    EditText etMobileNumber;
    @BindView(R.id.tv_dob)
    TextView tvDOB;
    @BindView(R.id.tv_submit)
    TextView tvSave;
    @BindView(R.id.guidelineStart)
    Guideline guidelineStart;
    @BindView(R.id.guidelineEnd)
    Guideline guidelineEnd;
    @BindView(R.id.iv_not_verified)
    ImageView ivNotVerified;
    @BindView(R.id.iv_open_calendar)
    ImageView ivOpenCalendar;
    @BindView(R.id.tv_title)
    TextView tvTittle;
    @BindView(R.id.iv_user_image)
    CircleImageView ivUserImage;
    @BindView(R.id.iv_edit_iamge)
    ImageView ivEditIamge;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.tv_cc)
    TextView tvCc;
    @BindView(R.id.fl_add_address)
    FrameLayout flAddAddress;
    private EditProfileViewModel mEditProfileViewModel;
    private Uri mUri;
    private String filePath = "";
    private AmazonS3 mAmazonS3;
    private String mUserImage = "";
    private boolean isCameraOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEditProfileViewModel = ViewModelProviders.of(this).get(EditProfileViewModel.class);
        mEditProfileViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        tvCc.setText(AppUtils.getUserCountryCode(this));
        observeLiveData();
        setViewsForEditProfile();
    }

    private void observeLiveData() {
        mEditProfileViewModel.getEditProfileLiveData().observe(this, new Observer<EditProfileResponse>() {
            @Override
            public void onChanged(@Nullable EditProfileResponse editProfileResponse) {
                hideProgressDialog();
                if (editProfileResponse != null) {
                    if (AppUtils.checkUserValid(EditProfileActivity.this, editProfileResponse.getCode(), editProfileResponse.getMessage())) {
                        if (editProfileResponse.getData().getToken() != null) {
                            Intent intent = new Intent(EditProfileActivity.this, OnBoardActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(AppConstantClass.APIConstant.COUNTRYCODE, editProfileResponse.getData().getCountryCode());
                            bundle.putString(AppConstantClass.APIConstant.PHONE, editProfileResponse.getData().getPhone());
                            bundle.putString(AppConstantClass.SCREEN, EditProfileActivity.class.getSimpleName());
                            intent.putExtra(AppConstantClass.EDITPROFILEDATA, bundle);
                            startActivity(intent);
                            finishAfterTransition();
                        } else {
                            onBackPressed();
                        }
                    }
                }
            }
        });
        mEditProfileViewModel.getValidateLiveData().observe(this, new Observer<FailureResponse>() {
            @Override
            public void onChanged(@Nullable FailureResponse failureResponse) {
                hideProgressDialog();
                if (failureResponse != null)
                    showToastLong(failureResponse.getErrorMessage());
            }
        });
    }

    /**
     * set data on the edit views from  prefernces.
     */
    private void setViewsForEditProfile() {

        tvTittle.setText(R.string.s_edit_profile);
        if (mEditProfileViewModel.getName() != null) {
            etUserName.setText(mEditProfileViewModel.getName());
            etUserName.setSelection(etUserName.getText().length());

        }

        if (mEditProfileViewModel.isEmailIdVerified().equals(AppConstantClass.VERIFIED))
            ivNotVerified.setVisibility(View.GONE);
        else
            ivNotVerified.setVisibility(View.VISIBLE);


        if (mEditProfileViewModel.getEmailId() != null)
            tvUserEmailId.setText(mEditProfileViewModel.getEmailId());

        if (mEditProfileViewModel.getMobileNumber() != null)
            etMobileNumber.setText(mEditProfileViewModel.getMobileNumber());

        if (mEditProfileViewModel.getDOB() != null)
            tvDOB.setText(DateTimeUtils.timeStampToString(mEditProfileViewModel.getDOB()));
        if (mEditProfileViewModel.getCountryCode() != null && !mEditProfileViewModel.getCountryCode().equalsIgnoreCase(""))
            tvCc.setText(mEditProfileViewModel.getCountryCode());

        if (mEditProfileViewModel.getProfileImage() != null) {
            mUserImage = mEditProfileViewModel.getProfileImage();
            Glide.with(this).load(mUserImage)
                    .asBitmap().centerCrop().placeholder(R.drawable.ic_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivUserImage);
        }

    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_edit_profile;
    }


    @OnClick({R.id.iv_back, R.id.tv_submit, R.id.iv_open_calendar, R.id.tv_dob, R.id.iv_edit_iamge, R.id.tv_cc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                super.onBackPressed();
                break;
            case R.id.tv_submit:
                if (isInternetAvailable()) {
                    showProgressDialog();
                    mEditProfileViewModel.onSaveChangesViewClicked(getEditProfileData());
                } else {
                    showToastLong(getResources().getString(R.string.no_internet_connection));
                }
                break;
            case R.id.tv_dob:
            case R.id.iv_open_calendar:
                openDatePicker();
                break;
            case R.id.iv_edit_iamge:

                openChooseImageDialog();
                break;
            case R.id.tv_cc:
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_add_address);
                if (!(fragment instanceof CountryCodeFragment))
                    addFragment(R.id.fl_add_address, new CountryCodeFragment(), CountryCodeFragment.class.getSimpleName());
                break;


        }
    }

    /**
     * opening the dialog to choose image will pick from the gallery or camera
     */
    private void openChooseImageDialog() {

        new DialogForUpload(this, new DialogCallback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSubmit(View view, String result, int s) {
                if (result.equalsIgnoreCase("camera")) {
                    openCamera();
                } else {
                    openGallery();
                }
            }
        }).show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void openCamera() {
        // checking for the permission of camera
        if (PermissionUtils.checkMultiplePermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}) && !isCameraOpened) {
            // firing intent to camera with cropper
            Intent cameraIntent = ImageCropper.getCameraIntent(this, null);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                isCameraOpened = true;
                File myDir = new File(Environment.getExternalStorageDirectory().toString() + "/CaptureImageDemo");
                if (!myDir.exists()) {
                    myDir.mkdir();
                }

                String fname = "ImageCrop-" + System.currentTimeMillis() + ".jpg";
                File file = new File(myDir, fname);
                filePath = file.getAbsolutePath();
                mUri = Uri.fromFile(file);
                ImageCropper.startCaptureImageActivity(this, TAKE_PICTURE, CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_PERMISSION_GALLEY);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void openGallery() {
        // checking for the gallery permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            File myDir = new File(Environment.getExternalStorageDirectory().toString() + "/CaptureImageDemo");
            if (!myDir.exists()) {
                myDir.mkdir();
            }

            String fname = "ImageCrop-" + System.currentTimeMillis() + ".jpg";
            File file = new File(myDir, fname);
            filePath = file.getAbsolutePath();
            mUri = Uri.fromFile(file);
            ImageCropper.startPickImageFromGalleryActivity(this, REQUEST_PERMISSION_GALLEY, CROP_IMAGE_ACTIVITY_REQUEST_CODE);
        } else {
            // request for permissions
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_GALLEY);
        }
    }

    /**
     * get edit profile data that will send as response payload for edit profile api
     *
     * @return edit profile data
     */
    private User getEditProfileData() {
        User user = new User();
        user.setEmail(tvUserEmailId.getText().toString().trim());
        user.setDob(DOBtoSendFormatDate(tvDOB.getText().toString().trim()));
        user.setFullName(etUserName.getText().toString().trim());
        user.setPhone(etMobileNumber.getText().toString().trim());
        user.setImage(mUserImage);
        user.setCountryCode(tvCc.getText().toString());
        //user.setImage(ivUserImage.toString());
        if (!mEditProfileViewModel.getMobileNumber().equalsIgnoreCase(etMobileNumber.getText().toString())) {
            user.setIsNumberEdited(1);
        } else
            user.setIsNumberEdited(0);
        return user;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (String permission : permissions) {
            if (permission.equalsIgnoreCase(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !shouldShowRequestPermissionRationale(permission)) {
                        // User selected the Never Ask Again Option

                    }
                }
            } else if (permission.equalsIgnoreCase(Manifest.permission.CAMERA) || permission.equalsIgnoreCase(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (PermissionUtils.checkMultiplePermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})) {
//                    openCamera();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !shouldShowRequestPermissionRationale(permission)) {
                        // User selected the Never Ask Again Option

                    }
                }
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isCameraOpened = false;
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PERMISSION_GALLEY) {
                ImageCropper.activity(data.getData()).setGuidelines(CropImageView.Guidelines.OFF).
                        setCropShape(CropImageView.CropShape.OVAL).
                        setBorderLineColor(Color.WHITE).
                        setBorderCornerColor(Color.TRANSPARENT).
                        setAspectRatio(80, 80).setBorderLineThickness(5).
                        setOutputUri(mUri).
                        setAutoZoomEnabled(true).start(this);
            } else if (requestCode == TAKE_PICTURE) {
                ImageCropper.activity(ImageCropper.getCapturedImageURI()).
                        setGuidelines(CropImageView.Guidelines.OFF).
                        setCropShape(CropImageView.CropShape.OVAL).
                        setBorderLineColor(Color.WHITE).
                        setBorderCornerColor(Color.TRANSPARENT).
                        setAspectRatio(80, 80).
                        setOutputUri(mUri).
                        setAutoZoomEnabled(true).
                        start(this);

            } else if (requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                progressBar.setVisibility(View.VISIBLE);
                initializeAmazonS3(); // initializing the amazon serivices
//                pbUploadProfile.setVisibility(View.VISIBLE);
                startUpload();
            }

        }

    }

    /**
     * uploading the @filePath to the amazon server
     */
    private void startUpload() {
        MediaBean bean = addDataInBean(filePath);
        mAmazonS3.upload(bean);
    }

    /*
     * adding the upload image data to the mediabean
     * */
    private MediaBean addDataInBean(String path) {
        MediaBean bean = new MediaBean();
        bean.setName("Profile");
        bean.setMediaPath(path);
        return bean;
    }

    /*
     * initializing the Amazon server
     * */
    private void initializeAmazonS3() {
        if (mAmazonS3 == null) {
            mAmazonS3 = new AmazonS3();
            mAmazonS3.setActivity(this);
            mAmazonS3.setCallback(this);
        }
    }

    @Override
    protected void onErrorOccurred(Throwable throwable) {
        super.onErrorOccurred(throwable);
        hideProgressDialog();
    }


    //open date picker so that user can edit his/her dob
    private void openDatePicker() {
        hideKeyboard();
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(dobToTimeStampForPicker(tvDOB.getText().toString()));
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -13);
        cal.add(Calendar.DATE, -1);
        Date date = cal.getTime();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        tvDOB.setText(parseDOBDate(i + "-" + (i1 + 1) + "-" + i2));


                    }
                }, mYear, mMonth, mDay);


        datePickerDialog.getDatePicker().setMaxDate(date.getTime());
        datePickerDialog.show();
    }

    @Override
    public void uploadSuccess(MediaBean bean) {
        progressBar.setVisibility(View.GONE);
        mUserImage = bean.getServerUrl();
        ImageUtils.setImage(this, bean.getServerUrl(), ivUserImage, ContextCompat.getDrawable(this, R.drawable.ic_placeholder));

    }

    @Override
    public void uploadFailed(MediaBean bean) {
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void uploadProgress(MediaBean bean) {

    }

    @Override
    public void uploadError(Exception e, MediaBean imageBean) {
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void closeCurrentFragmentWithData(Bundle bundle) {
        removeFragmnet();
        if (bundle != null) {
            CountryCode countryCode = bundle.getParcelable(AppConstants.BundleConstants.COUNTRY_DATA);
            tvCc.setText("+" + countryCode.getCountryCode());
        }
    }

    private void removeFragmnet() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_add_address);
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @Override
    public void closeCurrentFragment() {
        removeFragmnet();

    }

    @Override
    protected void onFailure(FailureResponse failureResponse) {
        super.onFailure(failureResponse);
        hideProgressDialog();
        showToastShort(failureResponse.getErrorMessage());
        if (failureResponse.getErrorCode() == 422) {
            showToastLong(getResources().getString(R.string.something_went_wrong));
//            logout(this);
        }
        if (failureResponse.getErrorCode() == 401) {
            showToastLong(getResources().getString(R.string.user_is_blocked_by_admin));
            logout(this);
        }
    }
}
