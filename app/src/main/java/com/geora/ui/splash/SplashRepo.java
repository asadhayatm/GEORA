package com.geora.ui.splash;


import com.geora.data.DataManager;

public class SplashRepo {


    public String getAccessToken() {
        return DataManager.getInstance().getAccessToken();
    }
}
