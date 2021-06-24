package com.geora.ui.onboard.otp;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseFragment;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.FailureResponse;
import com.geora.model.otpverification.OTPVerificatonModel;
import com.geora.model.request.User;
import com.geora.model.signup.SignupModel;
import com.geora.ui.editprofile.EditProfileActivity;
import com.geora.ui.onboard.forgotpassword.ForgotPasswordFragment;
import com.geora.util.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OTPVerificationFragment extends BaseFragment {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_otp)
    EditText etOTP;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.main)
    ConstraintLayout main;


    private Context mContext;
    private Unbinder unbinder;
    private IOTPVerificationHost mOtpVerificationHost;
    private String mobilNumber = "";
    private OTPViewModel mOTPViewModel;
    private boolean isResend = false;
    private String mCountryCode = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_otpverification, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getContext();
        if (getArguments() != null) {
            mobilNumber = getArguments().getString(AppConstantClass.APIConstant.PHONE);
            mCountryCode = getArguments().getString(AppConstantClass.APIConstant.COUNTRYCODE);
            tvText.setText(mContext.getResources().getString(R.string.please_enter_the_verification_code_sent_to_you_on) + mobilNumber);
        }
        return view;
    }

    public interface IOTPVerificationHost {
        void steerToHomeActivity();

        void removeCurrentFrgment();

        void moveToResetPassword();

        void moveToCreateAddresss();

        void moveToProfileScreen();
    }


    @OnClick({R.id.tv_resend, R.id.iv_back, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                if (isInternetAvailable()) {
                    showProgressDialog();
                    User user = new User();
                    user.setOtp(etOTP.getText().toString().trim());
                    user.setPhone(mobilNumber);
                    if (mCountryCode.contains("+"))
                        user.setCountryCode(mCountryCode);
                    else
                        user.setCountryCode("+" + mCountryCode);

                    if (getArguments().getString(AppConstantClass.SCREEN).equalsIgnoreCase(ForgotPasswordFragment.class.getSimpleName()))
                        user.setType("resend");
                    else if (getArguments().getString(AppConstantClass.SCREEN).equalsIgnoreCase(EditProfileActivity.class.getSimpleName()))
                        user.setType("edit");
                    else
                        user.setType("verify");


                    mOTPViewModel.otpSendButtonClicked(user);

                } else {
                    showSnackBar(main, mContext.getResources().getString(R.string.no_internet_connection), true);

                }
                break;
            case R.id.tv_resend:
                if (isInternetAvailable()) {
                    showProgressDialog();
                    isResend = true;
                    TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
                    String countryCodeValue = tm.getNetworkCountryIso();
                    User user = new User();
                    user.setOtp(etOTP.getText().toString().trim());
                    user.setPhone(mobilNumber);
                    user.setCountryCode(countryCodeValue);
                    user.setType("resend");
                    mOTPViewModel.otpReSendButtonClicked(user);

                } else {
                    showSnackBar(main, mContext.getResources().getString(R.string.no_internet_connection), true);
                }
                break;
            case R.id.iv_back:
                mOtpVerificationHost.removeCurrentFrgment();
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static OTPVerificationFragment getInstance() {
        return new OTPVerificationFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOTPViewModel = ViewModelProviders.of(this).get(OTPViewModel.class);
        mOTPViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        mOTPViewModel.getOTPLiveData().observe(this, new Observer<OTPVerificatonModel>() {
            @Override
            public void onChanged(@Nullable OTPVerificatonModel otpVerificatonModel) {
                hideProgressDialog();
                if (AppUtils.checkUserValid(getActivity(), otpVerificatonModel.getCode(), otpVerificatonModel.getMessage())) {
                    if (getArguments().getString(AppConstantClass.SCREEN).equalsIgnoreCase(ForgotPasswordFragment.class.getSimpleName())) {
                        mOtpVerificationHost.moveToResetPassword();

                    } else if (getArguments().getString(AppConstantClass.SCREEN).equalsIgnoreCase(EditProfileActivity.class.getSimpleName())) {
                        mOtpVerificationHost.moveToProfileScreen();
                    } else {
                        mOtpVerificationHost.moveToCreateAddresss();
                    }
                }
            }
        });
        mOTPViewModel.getOTPResentPasswordLiveData().observe(this, new Observer<SignupModel>() {
            @Override
            public void onChanged(@Nullable SignupModel otpVerificatonModel) {
                hideProgressDialog();
                if (AppUtils.checkUserValid(getActivity(), otpVerificatonModel.getCode(), otpVerificatonModel.getMessage())) {
                    if (getArguments().getString(AppConstantClass.SCREEN).equalsIgnoreCase(ForgotPasswordFragment.class.getSimpleName())) {
                        mOtpVerificationHost.moveToResetPassword();
                    } else if (getArguments().getString(AppConstantClass.SCREEN).equalsIgnoreCase(EditProfileActivity.class.getSimpleName())) {
                        mOtpVerificationHost.moveToProfileScreen();
                    }
                }
            }
        });
        mOTPViewModel.getOTPResendPasswordLiveData().observe(this, new Observer<SignupModel>() {
            @Override
            public void onChanged(@Nullable SignupModel otpVerificatonModel) {
                hideProgressDialog();
                if (AppUtils.checkUserValid(getActivity(), otpVerificatonModel.getCode(), otpVerificatonModel.getMessage())) {
                    showSnackBar(main, mContext.getResources().getString(R.string.otp_resend_successfulyy), false);
                }
            }
        });
        mOTPViewModel.getValidationLiveData().observe(this, new Observer<FailureResponse>() {
            @Override
            public void onChanged(@Nullable FailureResponse failureResponse) {
                hideProgressDialog();
                if (failureResponse != null)
                    showSnackBar(main, failureResponse.getErrorMessage().toString(), true);

            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IOTPVerificationHost) {
            mOtpVerificationHost = (IOTPVerificationHost) context;
        } else
            throw new IllegalStateException("Host must implement mOtpVerificationHost");
    }

}
