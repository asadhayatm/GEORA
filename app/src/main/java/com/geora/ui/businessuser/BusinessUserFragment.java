package com.geora.ui.businessuser;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.geora.R;
import com.geora.base.BaseFragment;
import com.geora.customviews.DialogForBusinessUser;
import com.geora.listeners.DialogCallback;
import com.geora.model.businessuserresponse.BusinessUserResponse;
import com.geora.socket.SocketConstant;
import com.geora.ui.home.HomeActivity;
import com.geora.util.AppUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BusinessUserFragment extends BaseFragment {
    @BindView(R.id.wv)
    WebView webview;

    private Context mContext;
    private Unbinder unbinder;
    private Observer<BusinessUserResponse> observer;
    private BusinessUserViewModel mBusinessUserViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_business_user, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getContext();
        ((HomeActivity) mContext).setupToolbar(4, mContext.getResources().getString(R.string.become_a_business_user));
        webview.setWebViewClient(new WebClient());
        WebSettings set = webview.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);
        webview.loadUrl(SocketConstant.BECOMEBUSINESSUSERURL.BUSNIESS_URL);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBusinessUserViewModel = ViewModelProviders.of(this).get(BusinessUserViewModel.class);
        mBusinessUserViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        setObserver();
        HashMap<String, String> params = new HashMap<>();
        if (isInternetAvailable()) {
            showProgressDialog();
            mBusinessUserViewModel.hitBusinessUserApi(params);
        } else
            showSnackBar(webview, mContext.getResources().getString(R.string.no_internet_connection), true);

    }



    private void setObserver() {
        observer = new Observer<BusinessUserResponse>() {

            @Override
            public void onChanged(@Nullable BusinessUserResponse model) {
                hideProgressDialog();
                if (model != null) {
                    if (AppUtils.checkUserValid(getActivity(), model.getCode(), model.getMessage())) {
                        mBusinessUserViewModel.setStatus(model.getData().getIsBusinessUser());
                        if (model.getData().getIsBusinessUser() == 1) {
                            new DialogForBusinessUser(mContext, new DialogCallback() {
                                @Override
                                public void onSubmit(View view, String result, int stars) {
                                    if (mContext instanceof HomeActivity) {
                                        ((HomeActivity) mContext).checkBusinessStatus();
                                        ((HomeActivity) mContext).moveToHomeScreen(BusinessUserFragment.this);
                                    }
                                }
                            }).show();
                        }
                    }
                }

            }
        };
        mBusinessUserViewModel.getFeedbackLiveData().observe(this, observer);
    }

    public void removeObserver() {
        if (mBusinessUserViewModel.getFeedbackLiveData().hasObservers())
            mBusinessUserViewModel.getFeedbackLiveData().removeObserver(observer);
    }

    class WebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        removeObserver();
    }
}
