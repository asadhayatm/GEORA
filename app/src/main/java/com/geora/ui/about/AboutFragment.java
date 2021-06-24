package com.geora.ui.about;


import android.app.Dialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseFragment;
import com.geora.customviews.DialogForFeedback;
import com.geora.customviews.DialogForRateUs;
import com.geora.data.constants.AppConstantClass;
import com.geora.listeners.RateDialogCallback;
import com.geora.model.feedbackresponse.FeedbackResponse;
import com.geora.ui.home.HomeActivity;
import com.geora.util.AppUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends BaseFragment {


    @BindView(R.id.tv_about_us)
    TextView tvAboutUs;
    @BindView(R.id.tv_term_and_condition)
    TextView tvTermAndCondition;
    @BindView(R.id.tv_privacy_policy)
    TextView tvPrivacyPolicy;
    @BindView(R.id.tv_share_app)
    TextView tvShareApp;
    @BindView(R.id.tv_rate_us)
    TextView tvRateUs;

    private Unbinder unbinder;
    private IAboutHost mHost;
    private Context mContext;
    private Dialog mRatingDialog;
    private double mRateStars = 0;
    private AboutViewModel mAboutViewModel;
    private Observer<FeedbackResponse> observer;
    private DialogForFeedback feedbackDialog;
    private boolean isShown = false;

    public AboutFragment() {
        // Required empty public constructor
    }

    public static AboutFragment getInstance() {
        return new AboutFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getContext();
        ((HomeActivity) mContext).setupToolbar(4, mContext.getResources().getString(R.string.about));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAboutViewModel = ViewModelProviders.of(this).get(AboutViewModel.class);
        mAboutViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        setObserver();
    }


    private void setObserver() {
        observer = new Observer<FeedbackResponse>() {

            @Override
            public void onChanged(@Nullable FeedbackResponse model) {
                hideProgressDialog();
                if (model != null) {
                    if (AppUtils.checkUserValid(getActivity(), model.getCode(), model.getMessage())) {
                        feedbackDialog.dismiss();
                    }
                }

            }
        };
        mAboutViewModel.getFeedbackLiveData().observe(this, observer);
    }

    public void removeObserver() {
        if (mAboutViewModel.getFeedbackLiveData().hasObservers())
            mAboutViewModel.getFeedbackLiveData().removeObserver(observer);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        removeObserver();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IAboutHost) {
            mHost = (IAboutHost) context;
        } else throw new IllegalStateException("Host must implement IAboutHost");
    }

    @OnClick({R.id.tv_about_us, R.id.tv_term_and_condition, R.id.tv_privacy_policy, R.id.tv_share_app, R.id.tv_rate_us})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_about_us:
                mHost.openUrl(AppConstantClass.STATIC_PAGES.ABOUT_US);
                break;
            case R.id.tv_term_and_condition:
                mHost.openUrl(AppConstantClass.STATIC_PAGES.TERMSANDCONDITION);
                break;
            case R.id.tv_privacy_policy:
                mHost.openUrl(AppConstantClass.STATIC_PAGES.PRIVACYPOLICY);
                break;
            case R.id.tv_share_app:
                if (!isShown) {
                    isShown = true;
                    setLoadingStatus();
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My App Name");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.app_name));
                    startActivity(Intent.createChooser(sharingIntent, "Share app via"));
                }
                break;
            case R.id.tv_rate_us:
                openRateUsDialog();
                break;
        }
    }

    /**
     * function to set loading status
     */
    private void setLoadingStatus() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isShown = false;
            }
        },1500);
    }

    //open dialog for rating
    private void openRateUsDialog() {

        new DialogForRateUs(mContext, new RateDialogCallback() {
            @Override
            public void onSubmit(View view, String title, String description, double rating) {
                mRateStars = rating;
                if (mRateStars < 4) {
                    openFeedbackDialog();
                } else {
                    rateInPlayStore();
                }
            }
        }).show();
    }

    private void rateInPlayStore() {
        Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
        }
    }

    private void openFeedbackDialog() {
        feedbackDialog = new DialogForFeedback(mContext, new RateDialogCallback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSubmit(View view, String title, String description, double rating) {
                if (title.isEmpty()) {
                    showToastShort(getString(R.string.enter_title));
                    return;
                }
                if (description.isEmpty()) {
                    showToastShort(getString(R.string.enter_description));
                    return;
                }
                HashMap<String, String> params = new HashMap<>();
                params.put(AppConstantClass.TITLE, title);
                params.put(AppConstantClass.MESSAGE, description);
                params.put(AppConstantClass.RATING, String.valueOf(mRateStars));
                if (isInternetAvailable()) {
                    showProgressDialog();
                    mAboutViewModel.hitFeedbackApi(params);
                }
                else
                    showSnackBar(tvAboutUs, mContext.getResources().getString(R.string.no_internet_connection), true);
            }
        });
        feedbackDialog.show();
    }

    public interface IAboutHost {

        void openUrl(String aboutUs);
    }
}
