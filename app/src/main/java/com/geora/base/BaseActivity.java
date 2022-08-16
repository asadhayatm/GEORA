package com.geora.base;

import android.app.ProgressDialog;
import androidx.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.geora.R;
import com.geora.data.DataManager;
import com.geora.model.FailureResponse;
import com.geora.ui.onboard.OnBoardActivity;
import com.geora.util.AppUtils;
import com.geora.util.ResourceUtils;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    private Observer<Throwable> errorObserver;
    private Observer<FailureResponse> failureResponseObserver;
    private Observer<Boolean> loadingStateObserver;
    private RelativeLayout baseContainer;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        baseContainer = findViewById(R.id.rl_base_container);
        setLayout();
        ButterKnife.bind(this);
        initObservers();
    }

    private void initObservers() {
        errorObserver = new Observer<Throwable>() {
            @Override
            public void onChanged(@Nullable Throwable throwable) {
                onErrorOccurred(throwable);
            }
        };

        failureResponseObserver = new Observer<FailureResponse>() {
            @Override
            public void onChanged(@Nullable FailureResponse failureResponse) {
                onFailure(failureResponse);
            }
        };

        /**
         * experimental
         * */
        loadingStateObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                onLoadingStateChanged(aBoolean);
            }
        };
    }

    public Observer<Throwable> getErrorObserver() {
        return errorObserver;
    }

    public Observer<FailureResponse> getFailureResponseObserver() {
        return failureResponseObserver;
    }

    public Observer<Boolean> getLoadingStateObserver() {
        return loadingStateObserver;
    }

    protected void onLoadingStateChanged(boolean aBoolean) {

    }

    protected void onFailure(FailureResponse failureResponse) {
        hideProgressDialog();
        if (AppUtils.checkUserValid(this, failureResponse.getErrorCode(), failureResponse.getErrorMessage().toString())) {
            showToastShort(failureResponse.getErrorMessage());
            Log.e("onError: ", failureResponse.getErrorMessage() + "   " + failureResponse.getErrorCode());
        }
    }

    protected void onErrorOccurred(Throwable throwable) {
        showToastShort(throwable.getMessage());
        hideProgressDialog();
        Log.e("onErrorOccurred: ", throwable.getMessage());
    }

    /**
     * Method is used to set the layout in the Base Activity.
     * Layout params of the inserted child is match parent
     */
    private void setLayout() {
        if (getResourceId() != -1) {
            removeLayout();
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                    , RelativeLayout.LayoutParams.MATCH_PARENT);

            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            if (layoutInflater != null) {
                View view = layoutInflater.inflate(getResourceId(), null);
                baseContainer.addView(view, layoutParams);
            }
        }
    }


    /**
     * hides keyboard onClick anywhere besides edit text
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * Method used to get unique device id
     */
    public String getDeviceId() {
        return AppUtils.getUniqueDeviceId(this);
    }


    /**
     * Method is used by the sub class for passing the id of the layout ot be inflated in the relative layout
     *
     * @return id of the resource to be inflated
     */
    protected abstract int getResourceId();


    public void addFragment(int layoutResId, BaseFragment fragment, String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null)
            getSupportFragmentManager().beginTransaction()
                    .add(layoutResId, fragment, tag)
                    .commit();
    }

    public void addFragmentWithBackstack(int layoutResId, BaseFragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .add(layoutResId, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }


    public void replaceFragment(int layoutResId, BaseFragment fragment, String tag) {
//        if (getSupportFragmentManager().findFragmentByTag(tag) == null)
            getSupportFragmentManager().beginTransaction()

                    .replace(layoutResId, fragment, tag)
                    .commit();
    }

    public void replaceFragmentWithBackstack(int layoutResId, BaseFragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(layoutResId, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    public void replaceFragmentWithBackstackWithStateLoss(int layoutResId, BaseFragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(layoutResId, fragment, tag)
                .addToBackStack(tag)
                .commitAllowingStateLoss();
    }

    public void addFragmentWithBackstackWithPop(int layoutResId, BaseFragment fragment, String tag) {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .add(layoutResId, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }


    /**
     * This method is used to remove the view already present as a child in relative layout.
     */
    private void removeLayout() {
        if (baseContainer.getChildCount() >= 1)
            baseContainer.removeAllViews();
    }

    public void showToastLong(CharSequence message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void showToastShort(CharSequence message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this, R.style.MyTheme);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }


    public void showUnknownRetrofitError() {
        hideProgressDialog();
        showToastLong(ResourceUtils.getInstance().getString(R.string.something_went_wrong));
    }

    public void showNoNetworkError() {
        hideProgressDialog();
        showToastLong(ResourceUtils.getInstance().getString(R.string.no_internet));
    }

    public void hideKeyboard() {
        AppUtils.hideKeyboard(this);
    }


    public void popFragment() {
        if (getSupportFragmentManager() != null) {
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    public void logout(Context context) {
        DataManager.getInstance().clearPreferences();
        finishAffinity();

        Intent intent=new Intent(context,OnBoardActivity.class);
        startActivity(intent);
    }

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public void showSnackBar(View parentView, String message, boolean isError) {
        Snackbar snack = Snackbar.make(parentView, message, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        if (isError)
            view.setBackgroundColor(ContextCompat.getColor(this, R.color.orangey_red));
        else
            view.setBackgroundColor(getResources().getColor(R.color.viridian));
        view.setLayoutParams(params);
        snack.show();
    }
}
