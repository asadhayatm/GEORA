package com.geora.ui.onboard.forgotpassword;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseFragment;
import com.geora.constants.AppConstants;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.CountryCode;
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
public class ForgotPasswordFragment extends BaseFragment {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_cc)
    TextView tvCc;
    @BindView(R.id.main)
    ConstraintLayout main;

    private Unbinder unbinder;
    private Context mContext;
    /**
     * A {@link ForgotPasswordViewModel} object to handle all the actions and business logic
     */
    private ForgotPasswordViewModel mForgotPasswordViewModel;

    /**
     * A {@link IForgotPasswordHost} object to interact with the host{@link OnBoardActivity}
     * if any action has to be performed from the host.
     */
    private IForgotPasswordHost mForgotPasswordHost;

    /**
     * This method returns the instance of this fragment
     *
     * @return instance of {@link ForgotPasswordFragment}
     */
    public static ForgotPasswordFragment getInstance() {
        return new ForgotPasswordFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IForgotPasswordHost) {
            mForgotPasswordHost = (IForgotPasswordHost) context;
        } else
            throw new IllegalStateException("host must implement ILoginHost");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvCc.setText(AppUtils.getUserCountryCode(mContext));
        mForgotPasswordViewModel = ViewModelProviders.of(this).get(ForgotPasswordViewModel.class);
        mForgotPasswordViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        mForgotPasswordViewModel.getForgotPasswordLiveData().observe(this, new Observer<SignupModel>() {
            @Override
            public void onChanged(@Nullable SignupModel successResponse) {
                if (successResponse != null) {
                    hideProgressDialog();
                    if (AppUtils.checkUserValid(getActivity(), successResponse.getCode(), successResponse.getMessage())) {
                        showSnackBar(main, getString(R.string.reset_password_link_sent), false);
                        if (etEmail.getText().toString().contains("@")) {
                            mForgotPasswordHost.navigateToLoginFragment();
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString(AppConstantClass.APIConstant.PHONE, etEmail.getText().toString());
                            bundle.putString(AppConstantClass.APIConstant.COUNTRYCODE, tvCc.getText().toString());
                            bundle.putString(AppConstantClass.SCREEN, ForgotPasswordFragment.class.getSimpleName());
                            mForgotPasswordHost.openOTPScreen(bundle);
                        }
//                    mForgotPasswordHost.navigateToLoginFragment();
                    }
                }
            }
        });
        mForgotPasswordViewModel.getValidationLiveData().observe(this, new Observer<FailureResponse>() {
            @Override
            public void onChanged(@Nullable FailureResponse failureResponse) {
                hideProgressDialog();
                if (failureResponse != null)
                    showSnackBar(main, failureResponse.getErrorMessage().toString(), true);
            }
        });
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 6 && s.toString().matches("[0-9]+")) {
                    tvCc.setVisibility(View.VISIBLE);
                    etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    tvCc.setVisibility(View.GONE);
                    etEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mail, 0, 0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvCc.setText(AppUtils.getUserCountryCode(mContext));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.iv_back, R.id.tv_cc, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                if (isInternetAvailable()) {
                    showProgressDialog();
                    mForgotPasswordViewModel.onSubmitClicked(etEmail.getText().toString().trim(), tvCc.getText().toString());

                } else {
                    showSnackBar(main, mContext.getResources().getString(R.string.no_internet_connection), true);

                }
                break;
            case R.id.tv_cc:
                mForgotPasswordHost.openCountryCode();
                break;
            case R.id.iv_back:
                mForgotPasswordHost.removeCurrentFrgment();
                break;
        }
    }


    public void countryCodeSelected(Bundle bundle) {
        if (bundle != null) {
            CountryCode countryCode = bundle.getParcelable(AppConstants.BundleConstants.COUNTRY_DATA);
            tvCc.setText("+" + countryCode.getCountryCode());
        }
    }


    /**
     * This interface is used to interact with the host {@link OnBoardActivity}
     */
    public interface IForgotPasswordHost {

        void navigateToLoginFragment();

        void removeCurrentFrgment();

        void openCountryCode();

        void openOTPScreen(Bundle bundle);
    }
}
