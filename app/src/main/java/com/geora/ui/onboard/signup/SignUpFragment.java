package com.geora.ui.onboard.signup;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dnitinverma.amazons3library.AmazonS3;
import com.dnitinverma.amazons3library.interfaces.AmazonCallback;
import com.dnitinverma.amazons3library.model.MediaBean;
import com.geora.R;
import com.geora.base.BaseFragment;
import com.geora.constants.AppConstants;
import com.geora.customviews.DialogForUpload;
import com.geora.data.constants.AppConstantClass;
import com.geora.listeners.DialogCallback;
import com.geora.model.CountryCode;
import com.geora.model.FailureResponse;
import com.geora.model.request.User;
import com.geora.model.signup.SignupModel;
import com.geora.ui.onboard.OnBoardActivity;
import com.geora.util.AppUtils;
import com.geora.util.ImageUtils;
import com.geora.util.PermissionUtils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import appinventiv.com.imagecropper.cicularcropper.CropImageView;
import appinventiv.com.imagecropper.cicularcropper.ImageCropper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static appinventiv.com.imagecropper.cicularcropper.ImageCropper.CROP_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.geora.constants.AppConstants.REQUEST_PERMISSION_CAMERA;
import static com.geora.constants.AppConstants.REQUEST_PERMISSION_GALLEY;
import static com.geora.constants.AppConstants.TAKE_PICTURE;
import static com.geora.util.DateTimeUtils.DOBtoSendFormatDate;
import static com.geora.util.DateTimeUtils.dobToTimeStampForPicker;
import static com.geora.util.DateTimeUtils.parseDOBDate;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class SignUpFragment extends BaseFragment implements AmazonCallback {

    @BindView(R.id.et_fullname)
    EditText etFullname;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_date)
    TextView etDate;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.iv_user_image)
    CircleImageView ivUserImage;
    @BindView(R.id.iv_edit_iamge)
    ImageView ivEditIamge;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.iv_eye)
    ImageView ivEye;
    @BindView(R.id.tv_cc)
    TextView tvCc;
    @BindView(R.id.main)
    ConstraintLayout main;

    private Unbinder unbinder;
    private Context mContext;
    private Uri mUri;
    private String filePath = "";
    private boolean dialogVisible = false;
    private AmazonS3 mAmazonS3;
    private boolean hidePass = true;
    private String userImage = "";


    /**
     * A {@link SignUpViewModel} object to handle all the actions and business logic of sign up
     */
    private SignUpViewModel mSignUpViewModel;

    /**
     * A {@link ISignUpHost} object to interact with the host{@link OnBoardActivity}
     * if any action has to be performed from the host.
     */
    private ISignUpHost mSignUpHost;

    /**
     * This method is used to get the instance of this fragment
     *
     * @return new instance of {@link SignUpFragment}
     */
    public static SignUpFragment getInstance() {
        return new SignUpFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ISignUpHost) {
            mSignUpHost = (ISignUpHost) context;
        } else
            throw new IllegalStateException("Host must implement ISignUpHost");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getContext();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvCc.setText(AppUtils.getUserCountryCode(mContext));
        mSignUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        mSignUpViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        mSignUpViewModel.getSignUpLiveData().observe(this, new Observer<SignupModel>() {
            @Override
            public void onChanged(@Nullable SignupModel userResponse) {
                hideProgressDialog();
                if (AppUtils.checkUserValid(getActivity(), userResponse.getCode(), userResponse.getMessage())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConstantClass.APIConstant.PHONE, etMobile.getText().toString().trim());
                    bundle.putString(AppConstantClass.APIConstant.COUNTRYCODE, tvCc.getText().toString().trim());
                    bundle.putString(AppConstantClass.SCREEN, SignUpFragment.class.getSimpleName());
                    mSignUpHost.openOTPScreen(bundle);
                }
            }
        });
        mSignUpViewModel.getValidationLiveData().observe(this, new Observer<FailureResponse>() {
            @Override
            public void onChanged(@Nullable FailureResponse failureResponse) {
                hideProgressDialog();
                if (failureResponse != null)
                    showSnackBar(main, failureResponse.getErrorMessage().toString(), true);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void uploadSuccess(MediaBean bean) {
        progressBar.setVisibility(View.GONE);
        userImage = bean.getServerUrl();
        ImageUtils.setImage(mContext, bean.getServerUrl(), ivUserImage, ContextCompat.getDrawable(mContext, R.drawable.ic_placeholder));

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

    public void countryCodeSelected(Bundle bundle) {
        if (bundle != null) {
            CountryCode countryCode = bundle.getParcelable(AppConstants.BundleConstants.COUNTRY_DATA);
            tvCc.setText("+" + countryCode.getCountryCode());
        }
    }

    /**
     * This interface is used to interact with the host {@link OnBoardActivity}
     */
    public interface ISignUpHost {

        void openCountryCode();


        void removeCurrentFrgment();

        void openOTPScreen(Bundle bundle);
    }

    @OnClick({R.id.tv_cc, R.id.iv_back, R.id.et_date, R.id.tv_submit, R.id.iv_eye, R.id.iv_edit_iamge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_date:
                openDatePicker();
                break;
            case R.id.tv_cc:
                mSignUpHost.openCountryCode();
                break;
            case R.id.iv_back:
                mSignUpHost.removeCurrentFrgment();
                break;
            case R.id.tv_submit:
                if (isInternetAvailable()) {
                    showProgressDialog();
                    User user = new User();
                    user.setFullName(etFullname.getText().toString().trim());
                    user.setPassword(etPassword.getText().toString().trim());
                    user.setEmail(etEmail.getText().toString().trim());
                    user.setPhone(etMobile.getText().toString().trim());
                    user.setDob(DOBtoSendFormatDate(etDate.getText().toString().trim()));
                    user.setImage(userImage);
                    user.setCountryCode(tvCc.getText().toString());
                    user.setMethod("normal_signup");
                    user.setType("normal");
                    mSignUpViewModel.userSignUp(user);
                } else {
                    showToastLong(mContext.getResources().getString(R.string.no_internet_connection));
                }
                break;
            case R.id.iv_edit_iamge:
                openChooseImageDialog();
                break;
            case R.id.iv_eye:
                if (hidePass) {
                    ivEye.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_eye_selected));
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    hidePass = false;
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivEye.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_eye_unselected));
                    hidePass = true;
                }
                break;
        }
    }

    private void openDatePicker() {
        hideKeyboard();
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(dobToTimeStampForPicker(etDate.getText().toString()));
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -13);
        cal.add(Calendar.DATE, -1);
        Date date = cal.getTime();
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        etDate.setText(parseDOBDate(i + "-" + (i1 + 1) + "-" + i2));

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(date.getTime());

        datePickerDialog.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void openCamera() {
        // checking for the permission of camera
        if (PermissionUtils.checkMultiplePermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})) {
            // firing intent to camera with cropper
            Intent cameraIntent = ImageCropper.getCameraIntent(mContext, null);
            if (cameraIntent.resolveActivity(mContext.getPackageManager()) != null) {
                File myDir = new File(Environment.getExternalStorageDirectory().toString() + "/CaptureImageDemo");
                if (!myDir.exists()) {
                    myDir.mkdir();
                }

                String fname = "ImageCrop-" + System.currentTimeMillis() + ".jpg";
                File file = new File(myDir, fname);
                filePath = file.getAbsolutePath();
                mUri = Uri.fromFile(file);
                ImageCropper.startCaptureImageActivity((Activity) mContext, TAKE_PICTURE, CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void openGallery() {
        // checking for the gallery permissions
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            File myDir = new File(Environment.getExternalStorageDirectory().toString() + "/CaptureImageDemo");
            if (!myDir.exists()) {
                myDir.mkdir();
            }

            String fname = "ImageCrop-" + System.currentTimeMillis() + ".jpg";
            File file = new File(myDir, fname);
            filePath = file.getAbsolutePath();
            mUri = Uri.fromFile(file);
            ImageCropper.startPickImageFromGalleryActivity((Activity) mContext, REQUEST_PERMISSION_GALLEY, CROP_IMAGE_ACTIVITY_REQUEST_CODE);
        } else {
            // request for permissions
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_GALLEY);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (String permission : permissions) {
            if (permission.equalsIgnoreCase(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (ActivityCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !shouldShowRequestPermissionRationale(permission)) {
                        // User selected the Never Ask Again Option

                        /*if (!dialogVisible) {
                            dialogVisible = true;
                            moveToSettingsDialog();
                        }*/
                        if (requestCode == REQUEST_PERMISSION_GALLEY) {
                            showToastLong("You have denied Permission.\n Go to settings and allow permission ");
                        }
                    }
                }
            } else if (permission.equalsIgnoreCase(Manifest.permission.CAMERA) || permission.equalsIgnoreCase(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (PermissionUtils.checkMultiplePermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})) {
//                    openCamera();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !shouldShowRequestPermissionRationale(permission)) {
                        // User selected the Never Ask Again Option
                        if (requestCode == REQUEST_PERMISSION_CAMERA) {
                            showToastLong("You have denied Permission.\n Go to settings and allow permission ");
                        }

                       /* if (!dialogVisible) {
                            dialogVisible = true;
                            moveToSettingsDialog();
                        }*/
                    }
                }
            }
        }
    }


    /**
     * opening the dialog to choose image will pick from the gallery or camera
     */
    private void openChooseImageDialog() {
        dialogVisible = false;
        new DialogForUpload(mContext, new DialogCallback() {
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PERMISSION_GALLEY) {
                ImageCropper.activity(data.getData()).setGuidelines(CropImageView.Guidelines.OFF).
                        setCropShape(CropImageView.CropShape.OVAL).
                        setBorderLineColor(Color.WHITE).
                        setBorderCornerColor(Color.TRANSPARENT).
                        setAspectRatio(80, 80).setBorderLineThickness(5).
                        setOutputUri(mUri).
                        setAutoZoomEnabled(true).start(getActivity());
            } else if (requestCode == TAKE_PICTURE) {
                ImageCropper.activity(ImageCropper.getCapturedImageURI()).
                        setGuidelines(CropImageView.Guidelines.OFF).
                        setCropShape(CropImageView.CropShape.OVAL).
                        setBorderLineColor(Color.WHITE).
                        setBorderCornerColor(Color.TRANSPARENT).
                        setAspectRatio(80, 80).
                        setOutputUri(mUri).
                        setAutoZoomEnabled(true).
                        start(getActivity());

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
            mAmazonS3.setActivity(getActivity());
            mAmazonS3.setCallback(this);
        }
    }

    @Override
    protected void onFailure(FailureResponse failureResponse) {
        super.onFailure(failureResponse);
        hideProgressDialog();
    }

    @Override
    protected void onErrorOccurred(Throwable throwable) {
        super.onErrorOccurred(throwable);
        hideProgressDialog();
    }
}
