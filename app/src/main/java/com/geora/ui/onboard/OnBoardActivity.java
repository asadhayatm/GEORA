package com.geora.ui.onboard;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.geora.R;
import com.geora.base.BaseActivity;
import com.geora.data.constants.AppConstantClass;
import com.geora.ui.home.HomeActivity;
import com.geora.ui.onboard.address.CreateProfileFragment;
import com.geora.ui.onboard.countrycode.CountryCodeFragment;
import com.geora.ui.onboard.forgotpassword.ForgotPasswordFragment;
import com.geora.ui.onboard.login.LoginFragment;
import com.geora.ui.onboard.otp.OTPVerificationFragment;
import com.geora.ui.onboard.reset.ResetPasswordFragment;
import com.geora.ui.onboard.signup.SignUpFragment;

public class OnBoardActivity extends BaseActivity implements LoginFragment.ILoginHost,
        SignUpFragment.ISignUpHost, ForgotPasswordFragment.IForgotPasswordHost, OTPVerificationFragment.IOTPVerificationHost,
        ResetPasswordFragment.IResetPasswordHost, CreateProfileFragment.ICreateAddressHost, CountryCodeFragment.ICountryCodeHost {

    private boolean isFromEditProfile = false;
    private String typw = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        addFragmentWithBackstack(R.id.onboard_container,new  CreateProfileFragment(), SignUpFragment.class.getSimpleName());
        typw = getIntent().getStringExtra("type");
        showInitialFragment();
    }

    /**
     * deep linking to reset password and sending the token from that link to
     * {@link ResetPasswordFragment} reset the password.
     *
     * @param
     */
    private void checkIntent() {
        getSupportFragmentManager().beginTransaction().add(R.id.onboard_container, new LoginFragment()).addToBackStack(LoginFragment.class.getSimpleName()).commit();
        //adding reset password fragment with user id to reset the password
        moveToResetPassword();


    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_onboard;
    }

    /**
     * Method to show initial fragment
     */
    private void showInitialFragment() {
        Intent intent = getIntent();
        if (typw != null && typw.equalsIgnoreCase("810")) {
            // This function is called here because if the user selects forgot password, then a link to reset his/her
            // password is sent to their email address.Through deep linking, this OnBoardActivity gets the link in
            // intent object and we have to maintain that flow also. So, in this case intent will not be null and we
            // will add ResetPasswordFragment
            checkIntent();
        } else if (intent.hasExtra(AppConstantClass.EDITPROFILEDATA) && intent.getBundleExtra(AppConstantClass.EDITPROFILEDATA) != null) {
            isFromEditProfile = true;
            openOTPScreenFromEditProfile(intent.getBundleExtra(AppConstantClass.EDITPROFILEDATA));
        } else
            //if intent data is null , then it will add the LoginFragment
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.onboard_container, new LoginFragment())
                    .addToBackStack(LoginFragment.class.getSimpleName())
                    .commit();
//            addFragment(R.id.onboard_container, new LoginFragment(), LoginFragment.class.getSimpleName());
    }

    private void openOTPScreenFromEditProfile(Bundle bundle) {
        OTPVerificationFragment otpVerificationFragment = new OTPVerificationFragment();
        otpVerificationFragment.setArguments(bundle);
        addFragment(R.id.onboard_container, otpVerificationFragment, OTPVerificationFragment.class.getSimpleName());

    }

    @Override
    public void showSignUpFragment() {
        addFragmentWithBackstack(R.id.onboard_container, SignUpFragment.getInstance(), SignUpFragment.class.getSimpleName());
    }

    @Override
    public void steerToHomeActivity() {
        startActivity(new Intent(this, HomeActivity.class));
        finishAfterTransition();
    }

    @Override
    public void openCountryCode() {
        addFragmentWithBackstack(R.id.onboard_container, new CountryCodeFragment(), CountryCodeFragment.class.getSimpleName());

    }
    

    @Override
    public void removeCurrentFrgment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.onboard_container);
        if (fragment instanceof ResetPasswordFragment) {
            getSupportFragmentManager().popBackStackImmediate(LoginFragment.class.getSimpleName(), 0);
        } else
            getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void moveToResetPassword() {
        addFragmentWithBackstack(R.id.onboard_container, new ResetPasswordFragment(), ResetPasswordFragment.class.getSimpleName());
    }

    @Override
    public void moveToCreateAddresss() {
        addFragmentWithBackstack(R.id.onboard_container, new CreateProfileFragment(), CreateProfileFragment.class.getSimpleName());

    }

    @Override
    public void moveToProfileScreen() {
        onBackPressed();
    }

    @Override
    public void openOTPScreen(Bundle bundle) {
        OTPVerificationFragment otpVerificationFragment = new OTPVerificationFragment();
        otpVerificationFragment.setArguments(bundle);
        addFragmentWithBackstack(R.id.onboard_container, otpVerificationFragment, OTPVerificationFragment.class.getSimpleName());
    }

    @Override
    public void navigateToCreateProfile() {
        CreateProfileFragment createProfileFragment = new CreateProfileFragment();
        addFragmentWithBackstack(R.id.onboard_container, createProfileFragment, CreateProfileFragment.class.getSimpleName());
    }

    @Override
    public void showForgotPasswordFragment() {
        addFragmentWithBackstack(R.id.onboard_container, ForgotPasswordFragment.getInstance(), ForgotPasswordFragment.class.getSimpleName());
    }

    @Override
    public void navigateToLoginFragment() {
        getSupportFragmentManager().popBackStackImmediate(LoginFragment.class.getSimpleName(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.onboard_container);
        if (fragment instanceof SignUpFragment)
            fragment.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.onboard_container);
        if (fragment != null) {
            if (fragment instanceof OTPVerificationFragment) {
                if (isFromEditProfile)
                    finishAfterTransition();
                else
                    popFragment();
            } else if (fragment instanceof SignUpFragment) {
                popFragment();
            } else if (fragment instanceof ResetPasswordFragment) {
                getSupportFragmentManager().popBackStackImmediate(LoginFragment.class.getSimpleName(), 0);
            } else if (fragment instanceof ForgotPasswordFragment) {
                popFragment();
            } else {
                super.onBackPressed();
            }
        }
        finish();
    }

    @Override
    public void closeCurrentFragmentWithData(Bundle bundle) {
        popFragment();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.onboard_container);
        if (bundle != null) {
            if (fragment instanceof SignUpFragment) {
                ((SignUpFragment) fragment).countryCodeSelected(bundle);
            } else if (fragment instanceof ForgotPasswordFragment) {
                ((ForgotPasswordFragment) fragment).countryCodeSelected(bundle);
            } else if (fragment instanceof LoginFragment) {
                ((LoginFragment) fragment).countryCodeSelected(bundle);
            }
        }
    }

    @Override
    public void closeCurrentFragment() {
        popFragment();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        typw = intent.getStringExtra("type");
        showInitialFragment();
    }
}
