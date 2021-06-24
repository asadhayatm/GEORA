package com.geora.base;

import androidx.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.View;

import com.geora.model.FailureResponse;
import com.geora.util.AppUtils;

import java.util.Objects;


public class BaseFragment extends Fragment {

    private Observer<Throwable> errorObserver;
    private Observer<FailureResponse> failureResponseObserver;
    private Observer<Boolean> loadingStateObserver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        loadingStateObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null)
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
        if (AppUtils.checkUserValid(getActivity(), failureResponse.getErrorCode(), failureResponse.getErrorMessage().toString())) {
            showToastShort(failureResponse.getErrorMessage());
            Log.e("onError: ", failureResponse.getErrorMessage() + "   " + failureResponse.getErrorCode());
        }
    }

    protected void onErrorOccurred(Throwable throwable) {
        hideProgressDialog();
        showToastShort(throwable.getMessage());
        Log.e("onErrorOccurred: ", throwable.getMessage());
    }

    public void showToastLong(CharSequence message) {
        if (getActivity() != null) {
            ((BaseActivity) Objects.requireNonNull(getActivity())).showToastLong(message);
        }
    }

    public void showToastShort(CharSequence message) {
        if (getActivity() != null) {
            ((BaseActivity) Objects.requireNonNull(getActivity())).showToastShort(message);
        }
    }

    public void showProgressDialog() {
        if (getActivity() != null) {
            ((BaseActivity) Objects.requireNonNull(getActivity())).showProgressDialog();
        }
    }

    public void hideProgressDialog() {
        if (getActivity() != null) {
            ((BaseActivity) Objects.requireNonNull(getActivity())).hideProgressDialog();
        }
    }


    public String getDeviceId() {
        if (getActivity() != null) {
            return ((BaseActivity) Objects.requireNonNull(getActivity())).getDeviceId();
        }
        return "";
    }

    public void hideKeyboard() {
        if (getActivity() != null) {
            ((BaseActivity) Objects.requireNonNull(getActivity())).hideKeyboard();
        }
    }

    public void showNoNetworkError() {
        if (getActivity() != null) {
            ((BaseActivity) Objects.requireNonNull(getActivity())).showNoNetworkError();
        }
    }

    public void logout(Context context) {
        if (getActivity() != null) {
            ((BaseActivity) Objects.requireNonNull(getActivity())).logout(context);
        }
    }

    public void showSnackBar(View parentView, String message, boolean isError) {
        if (getActivity() != null) {
            ((BaseActivity) Objects.requireNonNull(getActivity())).showSnackBar(parentView, message, isError);
        }
    }

    public void popFragment() {
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStackImmediate();
        }
    }

    public boolean isInternetAvailable() {
        if (getActivity() != null) {
            return ((BaseActivity) Objects.requireNonNull(getActivity())).isInternetAvailable();
        }
        return false;
    }

}
