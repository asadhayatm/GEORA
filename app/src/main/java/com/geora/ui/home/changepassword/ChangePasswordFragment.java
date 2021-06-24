package com.geora.ui.home.changepassword;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.geora.R;
import com.geora.base.BaseFragment;
import com.geora.model.FailureResponse;
import com.geora.model.commonresponse.CommonResponse;
import com.geora.model.request.User;
import com.geora.util.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class ChangePasswordFragment extends BaseFragment {


    @BindView(R.id.et_old)
    EditText etOld;
    @BindView(R.id.et_new)
    EditText etNew;
    @BindView(R.id.et_confirm)
    EditText etConfirm;
    Unbinder unbinder;

    /**
     * A {@link ChangePasswordViewModel} object to handle all the actions and business logic of login
     */
    private ChangePasswordViewModel mChangePasswordViewModel;

    /**
     * A {@link IChangePasswordHost} object to interact with the host{@link HomeActivity}
     * if any action has to be performed from the host.
     */
    private IChangePasswordHost mChangePasswordHost;

    /**
     * This method is used to get the instance of this fragment
     *
     * @return new instance of {@link ChangePasswordFragment}
     */
    public static ChangePasswordFragment getInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IChangePasswordHost) {
            mChangePasswordHost = (IChangePasswordHost) context;
        } else
            throw new IllegalStateException("host must implement IChangePasswordHost");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mChangePasswordViewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel.class);
        mChangePasswordViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        mChangePasswordViewModel.getChangePasswordLiveData().observe(this, new Observer<CommonResponse>() {
            @Override
            public void onChanged(@Nullable CommonResponse successResponse) {
                hideProgressDialog();
                if (successResponse != null) {
                    //send back
                    if (AppUtils.checkUserValid(getActivity(), successResponse.getCODE(), successResponse.getMESSAGE())) {
                        showToastLong(getString(R.string.password_changed_success));
                        mChangePasswordHost.navigateToHomeFragment();
                    }
                }
            }
        });
        mChangePasswordViewModel.getValidateLiveData().observe(this, new Observer<FailureResponse>() {
            @Override
            public void onChanged(@Nullable FailureResponse failureResponse) {
                hideProgressDialog();
                if (failureResponse != null)
                    showToastLong(failureResponse.getErrorMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.bt_submit)
    public void onViewClicked() {
        User user = new User();
        user.setPassword(etNew.getText().toString().trim());
//        user.setOldPassword(etOld.getText().toString().trim());
//        user.setConfirmPassword(etConfirm.getText().toString().trim());
        showProgressDialog();
        mChangePasswordViewModel.onSubmitClicked(user);
    }

    /**
     * This interface is used to interact with the host {@link }
     */
    public interface IChangePasswordHost {

        void navigateToHomeFragment();
    }
}

