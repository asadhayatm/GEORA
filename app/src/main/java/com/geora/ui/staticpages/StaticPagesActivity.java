package com.geora.ui.staticpages;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseActivity;
import com.geora.data.constants.AppConstantClass;

import butterknife.BindView;
import butterknife.OnClick;

public class StaticPagesActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivMenu;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
    }

    /**
     * get data from intent
     */
    private void getIntentData() {
        if (getIntent() != null && getIntent().hasExtra(AppConstantClass.STATIC_PAGES.PAGE)) {
            loadWebUrl(getIntent().getStringExtra(AppConstantClass.STATIC_PAGES.PAGE));
        }
    }

    /**
     * load url on the basis on page title
     *
     * @param pageTittle page name that will load on webview
     */
    private void loadWebUrl(String pageTittle) {
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });
        switch (pageTittle) {
            case AppConstantClass.STATIC_PAGES.ABOUT_US:
//                webview.loadUrl("http://georaapp.com/about-us");
                webview.loadUrl("http://www.georaapp.com/about");
                tvTitle.setText(R.string.s_about_us);
                break;
            case AppConstantClass.STATIC_PAGES.TERMSANDCONDITION:
                webview.loadUrl("https://georaapp.com/term/condition");
                tvTitle.setText(R.string.s_terms_and_condition);
                break;
            case AppConstantClass.STATIC_PAGES.PRIVACYPOLICY:
                webview.loadUrl("https://www.georaapp.com/privacy");
                tvTitle.setText(R.string.s_privacy_policy);
                break;
        }

    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_static_pages;
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finishAfterTransition();
    }

}
