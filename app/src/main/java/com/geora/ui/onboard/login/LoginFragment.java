package com.geora.ui.onboard.login;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.geora.R;
import com.geora.base.BaseFragment;
import com.geora.constants.AppConstants;
import com.geora.customviews.DialogForDob;
import com.geora.data.constants.AppConstantClass;
import com.geora.listeners.DialogCallback;
import com.geora.model.CountryCode;
import com.geora.model.FailureResponse;
import com.geora.model.otpverification.OTPVerificatonModel;
import com.geora.model.request.User;
import com.geora.ui.onboard.OnBoardActivity;
import com.geora.ui.onboard.signup.SignUpFragment;
import com.geora.util.AppUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.geora.util.DateTimeUtils.DOBtoSendFormatDate;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class LoginFragment extends BaseFragment implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_lets_go)
    TextView tvLetsGo;
    @BindView(R.id.tv_forgot)
    TextView tvForgot;
    @BindView(R.id.iv_fb)
    ImageView ivFb;
    @BindView(R.id.iv_google)
    ImageView ivGoogle;
    @BindView(R.id.tv_sign_up)
    TextView tvSignUp;
    @BindView(R.id.iv_visibility)
    ImageView ivVisibility;
    @BindView(R.id.tv_cc)
    TextView tvCc;
    @BindView(R.id.main)
    ConstraintLayout main;

    Unbinder unbinder;
    private Context mContext;
    private int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;
    private boolean hidePass = true;


    /**
     * A {@link ILoginHost} object to interact with the host{@link OnBoardActivity}
     * if any action has to be performed from the host.
     */
    private ILoginHost mLoginHost;

    /**
     * A {@link LoginViewModel} object to handle all the actions and business logic of login
     */
    private LoginViewModel mLoginViewModel;
    private GoogleSignInOptions gso;

    public static LoginFragment getInstance() {
        return new LoginFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ILoginHost) {
            mLoginHost = (ILoginHost) context;
        } else
            throw new IllegalStateException("host must implement ILoginHost");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getContext();
        registerFbCallBacks();
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 6 && s.toString().matches("[0-9]+")) {
                    tvCc.setVisibility(View.VISIBLE);
                    etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    tvCc.setVisibility(View.GONE);
                    etEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mail, 0, 0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvCc.setText(AppUtils.getUserCountryCode(mContext));
        mLoginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        mLoginViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        //check for device token from repo, if it is not present then ask for token
        //getDeviceToken();

        //observing login live data
        mLoginViewModel.getLoginLiveData().observe(this, new Observer<OTPVerificatonModel>() {
            @Override
            public void onChanged(@Nullable final OTPVerificatonModel userResponse) {
                if (userResponse != null) {
                    hideProgressDialog();
                    if (AppUtils.checkUserValid(getActivity(), userResponse.getCode(), userResponse.getMessage())) {
                        if (userResponse.getCode() == 224) {
                            Bundle bundle = new Bundle();
                            bundle.putString(AppConstantClass.APIConstant.PHONE, userResponse.getData().getPhone());
                            bundle.putString(AppConstantClass.APIConstant.COUNTRYCODE, userResponse.getData().getCountryCode());
                            bundle.putString(AppConstantClass.SCREEN, SignUpFragment.class.getSimpleName());
                            mLoginHost.openOTPScreen(bundle);
                        }else {
                            if (userResponse.getData().isNewUser() || userResponse.getData().getUserData().getDob() == null) {
                                if ((userResponse.getData().getUserData().getGoogleId() != null && !userResponse.getData().getUserData().getGoogleId().equals(""))
                                        || (userResponse.getData().getUserData().getFacebookId() != null && !userResponse.getData().getUserData().getFacebookId().equals(""))) {
                                    new DialogForDob(mContext, new DialogCallback() {
                                        @Override
                                        public void onSubmit(View view, String result, int stars) {
                                            showProgressDialog();
                                            mLoginViewModel.saveDob(DOBtoSendFormatDate(result.trim()), userResponse);
                                        }
                                    }).show();
                                }else {
                                    mLoginHost.navigateToCreateProfile();
                                }
                            } else {
                                if (userResponse.getData().getUserData().getIsVerifiedPhone().equalsIgnoreCase("verified"))
                                    mLoginHost.steerToHomeActivity();
                                else if (!userResponse.getData().isNewUser()) {
                                    mLoginHost.steerToHomeActivity();
                                } else if (!userResponse.getData().getUserData().getFacebookId().isEmpty() || !userResponse.getData().getUserData().getGoogleId().isEmpty()) {
                                    mLoginHost.moveToCreateAddresss();
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString(AppConstantClass.APIConstant.PHONE, userResponse.getData().getUserData().getPhone());
                                    bundle.putString(AppConstantClass.APIConstant.COUNTRYCODE, userResponse.getData().getUserData().getCountryCode());
                                    bundle.putString(AppConstantClass.SCREEN, LoginFragment.class.getSimpleName());
                                    mLoginHost.openOTPScreen(bundle);
                                }
                            }

                        }
                    }
                }
            }
        });


        mLoginViewModel.getDobLiveData().observe(this, new Observer<OTPVerificatonModel>() {
            @Override
            public void onChanged(@Nullable OTPVerificatonModel response) {
                hideProgressDialog();
                if (response != null) {
                    mLoginHost.navigateToCreateProfile();
                }
            }
        });

        //observing validation live data
        mLoginViewModel.getValidationLiveData().observe(this, new Observer<FailureResponse>() {
            @Override
            public void onChanged(@Nullable FailureResponse failureResponse) {
                hideProgressDialog();
                if (failureResponse != null)
                    showToastLong(failureResponse.getErrorMessage());

            }
        });

    }

    /**
     * This method is used to get device token from fire base
     */
    private void getDeviceToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(getActivity(), new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String deviceToken = instanceIdResult.getToken();
                mLoginViewModel.saveDeviceToken(deviceToken);
            }
        });
    }

    @Override
    protected void onErrorOccurred(Throwable throwable) {
        super.onErrorOccurred(throwable);
        hideProgressDialog();
        //showSnackBar(main, throwable.getMessage(), true);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.tv_cc, R.id.iv_visibility, R.id.tv_forgot, R.id.iv_fb, R.id.iv_google, R.id.tv_lets_go, R.id.tv_sign_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forgot:
                mLoginHost.showForgotPasswordFragment();
                break;
            case R.id.tv_sign_up:
                mLoginHost.showSignUpFragment();
                break;
            case R.id.tv_cc:
                mLoginHost.openCountryCode();
                break;
            case R.id.iv_google:
                googleLogin();
                break;
            case R.id.iv_fb:
                LoginManager.getInstance().logOut();
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
                break;
            case R.id.tv_lets_go:
                if (isInternetAvailable()) {
                    showProgressDialog();
                    User user = new User();
                    user.setEmail(etEmail.getText().toString().trim());
                    user.setPassword(etPassword.getText().toString().trim());
                    user.setCountryCode(tvCc.getText().toString());
                    mLoginViewModel.loginButtonClicked(user);
                } else {
                    showToastLong(mContext.getResources().getString(R.string.no_internet_connection));
                }
                break;
            case R.id.iv_visibility:
                if (hidePass) {
                    ivVisibility.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_eye_selected));
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    hidePass = false;
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivVisibility.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_eye_unselected));
                    hidePass = true;
                }
                break;
        }
    }

    private void googleLogin() {
        if (gso == null) {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
    public interface ILoginHost {
        void showSignUpFragment();

        void openCountryCode();

        void steerToHomeActivity();

        void showForgotPasswordFragment();

        void moveToCreateAddresss();

        void openOTPScreen(Bundle bundle);

        void navigateToCreateProfile();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            try {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            }catch (Exception e) {
                e.printStackTrace();
            }

            if (result != null && result.isSuccess() && isInternetAvailable() && result.getSignInAccount() != null) {
                GoogleSignInAccount acct = result.getSignInAccount();
                User user = new User();
                user.setEmail(acct.getEmail());
                user.setFullName(acct.getDisplayName());
                user.setImage(acct.getPhotoUrl() + "");
                user.setGoogleId(acct.getId());
                user.setType("google");
                user.setMethod("social_signup");
                mLoginViewModel.socialLogin(user);
                mGoogleApiClient.disconnect();

            } else {
                Toast.makeText(getActivity(), "There was a trouble signing in-Please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
       /* if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
            mGoogleApiClient.clearDefaultAccountAndReconnect();

        }*/
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage((FragmentActivity) mContext);
            mGoogleApiClient.disconnect();

        }
    }

    private void registerFbCallBacks() {
        FacebookSdk.setApplicationId(mContext.getResources().getString(R.string.facebook_key));
        FacebookSdk.sdkInitialize(getActivity());
        if (FacebookSdk.isInitialized()) {
            mCallbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // Facebook token
                    LoginManager.getInstance().logOut();
                    loginResult.getAccessToken();
                    handleFacebookLoginData(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    hideProgressDialog();
                }

                @Override
                public void onError(FacebookException exception) {
                    hideProgressDialog();
                    exception.printStackTrace();
                }
            });
        }
    }

    private void handleFacebookLoginData(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            // Application code

                            String id = object.getString("id");
                            String name = object.getString("name");
                            String image_url = "http://graph.facebook.com/" + id + "/picture?type=large";
                            String email = object.getString("email");
                            if (email != null && !email.isEmpty()) {
                                User user = new User();
                                user.setEmail(email);
                                user.setFullName(name);
                                user.setImage(image_url);
                                user.setFacebookId(id);
                                user.setType("facebook");
                                user.setMethod("social_signup");
                                mLoginViewModel.socialLogin(user);
                            } else {
                                showToastShort(getString(R.string.s_email_id_required));
                            }
//

                        } catch (Exception e) {
                            hideProgressDialog();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onFailure(FailureResponse failureResponse) {
        super.onFailure(failureResponse);
        hideProgressDialog();
        //showSnackBar(main, failureResponse.getErrorMessage().toString(), true);
        if (failureResponse.getErrorCode() == 424) {
            Bundle bundle = new Bundle();
            bundle.putString(AppConstantClass.APIConstant.PHONE, failureResponse.getData().getPhone());
            bundle.putString(AppConstantClass.APIConstant.COUNTRYCODE, failureResponse.getData().getCountry_code());
            bundle.putString(AppConstantClass.SCREEN, SignUpFragment.class.getSimpleName());
            mLoginHost.openOTPScreen(bundle);
        }
    }

}
