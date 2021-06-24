package com.geora.ui.splash;

import androidx.lifecycle.ViewModel;

public class SplashViewModel extends ViewModel {

    private SplashRepo mSplashRepo = new SplashRepo();


    public String getAccessTokenFromPref() {
        return mSplashRepo.getAccessToken();
    }
}
