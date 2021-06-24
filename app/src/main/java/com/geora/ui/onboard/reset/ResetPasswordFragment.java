package com.geora.ui.onboard.reset;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseFragment;
import com.geora.constants.AppConstants;
import com.geora.model.FailureResponse;
import com.geora.model.signup.SignupModel;
import com.geora.ui.onboard.OnBoardActivity;
import com.geora.util.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class ResetPasswordFragment extends BaseFragment {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.iv_visibility)
    ImageView ivVisibility;
    @BindView(R.id.main)
    ConstraintLayout main;

    private Unbinder unbinder;
    private boolean hidePass = true;
    private Context mContext;


    /**
     * A {@link IResetPasswordHost} object to interact with the host{@link OnBoardActivity}
     * if any action has to be performed from the host.
     */
    private IResetPasswordHost mResetPasswordHost;

    /**
     * A {@link ResetPasswordViewModel} object to handle all the actions and business logic of reset password
     */
    private ResetPasswordViewModel mResetPasswordViewModel;

    /**
     * This id is coming from deep linking url to reset the user's password.We will save it to send it
     * in api call
     */
    private String mUserId;

    /**
     * This method gives the instance of this fragment
     *
     * @param userId coming from the host {@link OnBoardActivity}
     * @return new instance of {@link ResetPasswordFragment}
     */
    public static ResetPasswordFragment getInstance(String userId) {
        ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.TOKEN, userId);
        resetPasswordFragment.setArguments(bundle);
        return resetPasswordFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IResetPasswordHost) {
            mResetPasswordHost = (IResetPasswordHost) context;
        } else
            throw new IllegalStateException("Host must implement IResetPasswordHost");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getting user id from the arguments
        if (getArguments() != null) {
            mUserId = getArguments().getString(AppConstants.TOKEN);
        }
        //initializing view model & setting listeners
        mResetPasswordViewModel = ViewModelProviders.of(this).get(ResetPasswordViewModel.class);
        mResetPasswordViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        mResetPasswordViewModel.getmResetPasswordLiveData().observe(this, new Observer<SignupModel>() {
            @Override
            public void onChanged(@Nullable SignupModel successResponse) {
                //password is reset successfully
                if (successResponse != null) {
                    hideProgressDialog();
                    if (AppUtils.checkUserValid(getActivity(), successResponse.getCode(), successResponse.getMessage())) {
                        showSnackBar(main, getString(R.string.password_reset_success), false);
                        mResetPasswordHost.navigateToLoginFragment();
                    }
                }
            }
        });
        mResetPasswordViewModel.getValidationLiveData().observe(this, new Observer<FailureResponse>() {
            @Override
            public void onChanged(@Nullable FailureResponse failureResponse) {
                hideProgressDialog();
                if (failureResponse != null)
                    showSnackBar(main, failureResponse.getErrorMessage().toString(), true);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * This interface is used to interact with the host {@link OnBoardActivity}
     */
    public interface IResetPasswordHost {

        void navigateToLoginFragment();

        void removeCurrentFrgment();
    }

    @OnClick({R.id.tv_submit, R.id.iv_back, R.id.iv_visibility})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                if (isInternetAvailable()) {
                    mResetPasswordViewModel.onSubmitClicked(etPassword.getText().toString().trim());
                } else {
                    showSnackBar(main, mContext.getResources().getString(R.string.no_internet_connection), true);

                }
                break;
            case R.id.iv_back:
                mResetPasswordHost.removeCurrentFrgment();
                break;

            case R.id.iv_visibility:
                if (hidePass) {
                    ivVisibility.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_eye_selected));
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    hidePass = false;
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivVisibility.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_eye_unselected));
                    hidePass = true;
                }
                break;
        }
    }
}
