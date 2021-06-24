package com.geora.ui.deeplink;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.geora.data.DataManager;
import com.geora.ui.onboard.OnBoardActivity;
import com.geora.ui.splash.SplashActivity;


public class UrlActivity extends AppCompatActivity {

    private final String TAG = UrlActivity.class.getSimpleName();
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initial shared preference


        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            if (uri != null) {
                String path = uri.getPath();
                String url = uri.toString();
                Log.e("pathchhhkkkk", path + " " + url);
                if (uri.getQueryParameter("type").equalsIgnoreCase("810")) {
                    String token = uri.getQueryParameter("token");
                    if (token != null)
                        DataManager.getInstance().saveToken(token);
//                    bundle.putString("type", uri.getQueryParameter("type"));
                    Intent intent1 = new Intent(this, OnBoardActivity.class);
                    intent1.putExtra("type", "810");
                    startActivity(intent1);
                    finish();

                } else {
                    startWithSplash();
                }

            } else
                startWithSplash();

        } else {
            startWithSplash();

        }

    }


    private void startWithSplash() {

        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }


}
