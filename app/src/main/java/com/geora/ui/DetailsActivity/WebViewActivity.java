package com.geora.ui.DetailsActivity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.geora.R;
import com.geora.base.BaseActivity;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.businessuserresponse.BusinessUserResponse;
import com.geora.ui.businessuser.BusinessUserViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WebViewActivity extends BaseActivity {
    @BindView(R.id.wv)
    WebView webview;

    private Context mContext;
    private Unbinder unbinder;
    private Observer<BusinessUserResponse> observer;
    private BusinessUserViewModel mBusinessUserViewModel;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        webview.setWebViewClient(new WebClient());
        WebSettings set = webview.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);
        url = "";
        if (getIntent()!= null && getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString(AppConstantClass.URL);
        }
        webview.loadUrl(url);
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_business_user;
    }


    class WebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
