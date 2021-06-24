package com.geora.ui.splash;


import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.geora.R;
import com.geora.base.BaseActivity;
import com.geora.ui.home.HomeActivity;
import com.geora.ui.onboard.OnBoardActivity;
import com.geora.util.AppUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends BaseActivity {

    private static final int SPLASH_TIME_OUT = 2000;
    /**
     * A {@link SplashViewModel} object to handle all the actions and business logic of splash
     */
    private SplashViewModel mSplashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSplashViewModel = ViewModelProviders.of(this).get(SplashViewModel.class);

        AppUtils.getDeviceToken(this);
        showSplashScreen();
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSplashScreen() {
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSplashViewModel.getAccessTokenFromPref() != null) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, OnBoardActivity.class));
                }
                finishAfterTransition();
            }
        }, SPLASH_TIME_OUT);
    }


    @Override
    protected int getResourceId() {
        return R.layout.activity_splash;
    }
}
