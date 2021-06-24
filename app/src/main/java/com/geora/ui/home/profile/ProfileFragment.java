package com.geora.ui.home.profile;


import android.app.Dialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.geora.R;
import com.geora.base.BaseFragment;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.FailureResponse;
import com.geora.model.ProfileInfo.UserProfileModel;
import com.geora.model.ResetPasswordModel;
import com.geora.model.createaddress.CreateAddressModel;
import com.geora.ui.address.AddressListActivity;
import com.geora.ui.cardlist.CardListActivity;
import com.geora.ui.home.HomeActivity;
import com.geora.util.AppUtils;
import com.geora.util.DateTimeUtils;
import com.google.android.flexbox.FlexboxLayout;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment {


    @BindView(R.id.tv_label_edit)
    TextView tvLabelEdit;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_email_id)
    TextView tvUserEmailId;
    @BindView(R.id.tv_label_mobile_number)
    TextView tvLabelMobileNumber;
    @BindView(R.id.tv_label_date_of_birth)
    TextView tvLabelDateOfBirth;
    @BindView(R.id.tv_mobile_number)
    TextView tvMobileNumber;
    @BindView(R.id.tv_date_of_birth)
    TextView tvDateOfBirth;
    @BindView(R.id.cl_profile_pic)
    ConstraintLayout clProfilePic;
    @BindView(R.id.iv_user_image)
    CircleImageView ivUserImage;
    @BindView(R.id.iv_edit_iamge)
    ImageView ivEditIamge;
    @BindView(R.id.cl_profile_detail)
    FrameLayout clProfileDetail;
    @BindView(R.id.tv_my_cards)
    TextView tvMyCards;
    @BindView(R.id.tv_my_address)
    TextView tvMyAddress;
    @BindView(R.id.tv_my_categories)
    TextView tvMyCategories;
    @BindView(R.id.tv_label_category_edit)
    TextView tvLabelCategoryEdit;
    @BindView(R.id.guidelineStart)
    Guideline guidelineStart;
    @BindView(R.id.guidelineEnd)
    Guideline guidelineEnd;
    Unbinder unbinder;
    @BindView(R.id.iv_email_not_verified)
    ImageView ivEmailNotVerified;
    @BindView(R.id.fbl_genres)
    FlexboxLayout fblCategories;
    @BindView(R.id.main)
    ScrollView main;

    private IProfileHost mProfileHost;
    private ProfileViewModel mProfileViewModel;
    private LinkedList<String> mCategoryList;
    private Context mContext;

    private boolean isEditting = true;
    private boolean mEventActive;
    private boolean mFundRaisingActive;
    private boolean mSalesActive;
    private boolean mPromotionActive;
    private boolean mAllActive;
    private Dialog emailVerificationDialog;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment getInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getContext();
        ((HomeActivity) mContext).setupToolbar(4, mContext.getResources().getString(R.string.s_my_profile));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProfileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        mProfileViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        observeLiveData();
        initViewsAndVariables();
        setViews();
    }


    @Override
    protected void onErrorOccurred(Throwable throwable) {
        super.onErrorOccurred(throwable);
        hideProgressDialog();
        showToastShort(throwable.getMessage());
    }

    //observe live data
    private void observeLiveData() {
        mProfileViewModel.getProfileInfoLiveData().observe(this, new Observer<UserProfileModel>() {
            @Override
            public void onChanged(@Nullable UserProfileModel userProfileModel) {
                hideProgressDialog();
                if (AppUtils.checkUserValid(getActivity(), userProfileModel.getCode(), userProfileModel.getMessage())) {
                    if (userProfileModel != null) {
                        settingProfileData();
                    }
                }
            }

        });
        mProfileViewModel.getResetPasswordLiveData().observe(this, new Observer<ResetPasswordModel>() {
            @Override
            public void onChanged(@Nullable ResetPasswordModel resetPasswordModel) {
                //password is reset successfully
                if (resetPasswordModel != null) {
                    hideProgressDialog();
                    if (AppUtils.checkUserValid(getActivity(), resetPasswordModel.getCode(), resetPasswordModel.getMessage())) {
                        emailVerificationDialog.dismiss();
                        showToastLong("Verification mail send successfully");
                    }
                }
            }
        });
        mProfileViewModel.getUpdateCategoriesLiveData().observe(this, new Observer<CreateAddressModel>() {
            @Override
            public void onChanged(@Nullable CreateAddressModel createAddressModel) {
                hideProgressDialog();
                //categories set after updation
                if (createAddressModel != null) {
                    if (AppUtils.checkUserValid(getActivity(), createAddressModel.getCode(), createAddressModel.getMessage())) {
                        fblCategories.removeAllViews();
                        mCategoryList.clear();
                        setCategoriesViews();
                        tvLabelCategoryEdit.setText(R.string.edit);

                    }
                }
            }
        });
    }

    //initialize views and variables
    private void initViewsAndVariables() {

        mCategoryList = new LinkedList<>();
    }

    //set views to the fields from preference
    private void setViews() {
        if (mProfileViewModel.getName() != null)
            tvUserName.setText(mProfileViewModel.getName());

        if (mProfileViewModel.getDateOfBirth() != null && !mProfileViewModel.getDateOfBirth().equals("")
                && !mProfileViewModel.getDateOfBirth().equals("null"))
            tvDateOfBirth.setText(DateTimeUtils.timeStampToString(mProfileViewModel.getDateOfBirth()));

        if (mProfileViewModel.getMobileNumber() != null)
            tvMobileNumber.setText(mProfileViewModel.getMobileNumber());

        if (mProfileViewModel.getEmailId() != null)
            tvUserEmailId.setText(mProfileViewModel.getEmailId());


        if (mProfileViewModel.getProfileImage() != null)
            Glide.with(getContext()).load(mProfileViewModel.getProfileImage())
                    .asBitmap().centerCrop().placeholder(R.drawable.ic_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivUserImage);

        if (mProfileViewModel.isEmailIdVerified().equals(AppConstantClass.VERIFIED))
            ivEmailNotVerified.setVisibility(View.GONE);
        else
            ivEmailNotVerified.setVisibility(View.VISIBLE);

        setCategoriesViews();

    }

    private void setCategoriesViews() {
        //if allactive status is true set all categories on the user profile
        if (mProfileViewModel.getIsAllActive().equals("1")) {
            mCategoryList.add(mCategoryList.size(), "Events");
            mCategoryList.add(mCategoryList.size(), "Promotions");
            mCategoryList.add(mCategoryList.size(), "FundRaisings");
            mCategoryList.add(mCategoryList.size(), "Sales");
            setAllActive(false);
        } else {
            mAllActive = false;
            //show event category
            if (mProfileViewModel.getEventActive().equals("1")) {
                mEventActive = true;
                mCategoryList.add(mCategoryList.size(), "Events");
            }
            //show fundraising category
            if (mProfileViewModel.getFundRaisingActive().equals("1")) {
                mFundRaisingActive = true;
                mCategoryList.add(mCategoryList.size(), "FundRaisings");
            }
            //show sales category
            if (mProfileViewModel.getSalesActive().equals("1")) {
                mSalesActive = true;
                mCategoryList.add(mCategoryList.size(), "Sales");
            }
            //show promotions category
            if (mProfileViewModel.getPromotionsActive().equals("1")) {
                mPromotionActive = true;
                mCategoryList.add(mCategoryList.size(), "Promotions");
            }

        }
        //set categories on the view using flex box layout
        if (mCategoryList.size() > 0) {
            fblCategories.setVisibility(View.VISIBLE);
            for (int i = 0; i < mCategoryList.size(); i++)
                fblCategories.addView(categorieViews(mCategoryList.get(i), false));
        }
    }

    //set all categories selected value true
    private void setAllActive(boolean isForEdit) {
        if (isForEdit) {
            mAllActive = true;
            mEventActive = false;
            mFundRaisingActive = false;
            mSalesActive = false;
            mPromotionActive = false;
        } else {
            mAllActive = true;
            mEventActive = true;
            mFundRaisingActive = true;
            mSalesActive = true;
            mPromotionActive = true;
        }


    }

    /**
     * set categories views on the basis of categories status that was previously selected by user
     *
     * @param text      name of the category
     * @param isForEdit boolean to set views on the basis of it
     * @return return view
     */
    private View categorieViews(String text, final boolean isForEdit) {

        final View view = LayoutInflater.from(getContext()).inflate(R.layout.item_flexbox_layout, null);
        final TextView tvSelection = view.findViewById(R.id.tv_category);
        tvSelection.setText(text);
        view.setTag(text);
        if (tvSelection.getText().toString().equals("All")) {
            if (mAllActive) {
                tvSelection.setTextColor(getResources().getColor(R.color.black));
                tvSelection.setBackground(getResources().getDrawable(R.drawable.yellow_border_rect_filed));
            } else {
                tvSelection.setTextColor(getResources().getColor(R.color.butterscotch));
                tvSelection.setBackground(getResources().getDrawable(R.drawable.yellow_border_rect));
            }
        }

        if (tvSelection.getText().toString().equals("Events")) {
            if (mEventActive) {
                tvSelection.setTextColor(getResources().getColor(R.color.black));
                tvSelection.setBackground(getResources().getDrawable(R.drawable.yellow_border_rect_filed));
            } else {
                tvSelection.setTextColor(getResources().getColor(R.color.butterscotch));
                tvSelection.setBackground(getResources().getDrawable(R.drawable.yellow_border_rect));
            }
        }

        if (tvSelection.getText().toString().equals("Promotions")) {
            if (mPromotionActive) {
                tvSelection.setTextColor(getResources().getColor(R.color.black));
                tvSelection.setBackground(getResources().getDrawable(R.drawable.yellow_border_rect_filed));
            } else {
                tvSelection.setTextColor(getResources().getColor(R.color.butterscotch));
                tvSelection.setBackground(getResources().getDrawable(R.drawable.yellow_border_rect));
            }
        }

        if (tvSelection.getText().toString().equals("FundRaisings")) {
            if (mFundRaisingActive) {
                tvSelection.setTextColor(getResources().getColor(R.color.black));
                tvSelection.setBackground(getResources().getDrawable(R.drawable.yellow_border_rect_filed));
            } else {
                tvSelection.setTextColor(getResources().getColor(R.color.butterscotch));
                tvSelection.setBackground(getResources().getDrawable(R.drawable.yellow_border_rect));
            }
        }
        if (tvSelection.getText().toString().equals("Sales")) {
            if (mSalesActive) {
                tvSelection.setTextColor(getResources().getColor(R.color.black));
                tvSelection.setBackground(getResources().getDrawable(R.drawable.yellow_border_rect_filed));
            } else {
                tvSelection.setTextColor(getResources().getColor(R.color.butterscotch));
                tvSelection.setBackground(getResources().getDrawable(R.drawable.yellow_border_rect));
            }
        }

        //set click on the view if fields are editable
        if (isForEdit) {
            tvSelection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvSelection.getText().toString().equals("All")) {
                        for (int i = 0; i < mCategoryList.size(); i++) {
                            removeCategoriesViews();
                            setAllActive(true);
                        }
                    } else if (tvSelection.getText().toString().equalsIgnoreCase("Events")) {
                        if (mEventActive) {
                            mEventActive = false;
                            setUnselectedView(tvSelection);
                        } else {
                            mEventActive = true;
                            mAllActive = false;
                            removeCategoriesViews();
                        }

                    } else if (tvSelection.getText().toString().equalsIgnoreCase("Promotions")) {
                        if (mPromotionActive) {
                            mPromotionActive = false;
                            setUnselectedView(tvSelection);

                        } else {
                            mPromotionActive = true;
                            mAllActive = false;
                            removeCategoriesViews();

                        }
                    } else if (tvSelection.getText().toString().equalsIgnoreCase("FundRaisings")) {
                        if (mFundRaisingActive) {
                            mFundRaisingActive = false;
                            setUnselectedView(tvSelection);

                        } else {
                            mFundRaisingActive = true;
                            mAllActive = false;
                            removeCategoriesViews();
                        }
                    } else if (tvSelection.getText().toString().equalsIgnoreCase("Sales")) {
                        if (mSalesActive) {
                            mSalesActive = false;
                            setUnselectedView(tvSelection);

                        } else {
                            mSalesActive = true;
                            mAllActive = false;
                            removeCategoriesViews();

                        }


                    }


                }
            });
        }
        return view;
    }

    /**
     * change view background when user deselect it
     *
     * @param tvSelection textview whose  bacground is changed
     */
    private void setUnselectedView(TextView tvSelection) {
        tvSelection.setTextColor(getResources().getColor(R.color.butterscotch));
        tvSelection.setBackground(getResources().getDrawable(R.drawable.yellow_border_rect));

    }

    /**
     * remove previous categories views and set updated views
     */
    private void removeCategoriesViews() {
        if (mFundRaisingActive && mEventActive && mSalesActive && mPromotionActive) {
            setAllActive(true);
        }
        fblCategories.removeAllViews();
        mCategoryList.clear();
        addAllCategoriesToList();
        if (mCategoryList.size() > 0) {
            fblCategories.setVisibility(View.VISIBLE);
            for (int i = 0; i < mCategoryList.size(); i++)
                fblCategories.addView(categorieViews(mCategoryList.get(i), true));
        }

    }

    //add all category list for edit view
    private void addAllCategoriesToList() {
        mCategoryList.add(mCategoryList.size(), "All");
        mCategoryList.add(mCategoryList.size(), "Events");
        mCategoryList.add(mCategoryList.size(), "Promotions");
        mCategoryList.add(mCategoryList.size(), "FundRaisings");
        mCategoryList.add(mCategoryList.size(), "Sales");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IProfileHost) {
            mProfileHost = (IProfileHost) context;
        } else
            throw new IllegalStateException("host must implement IProfileHost");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_my_address, R.id.tv_my_cards, R.id.tv_label_edit, R.id.tv_label_category_edit, R.id.iv_email_not_verified})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_label_edit:
                mProfileHost.steerToEditProfile();
                break;
            case R.id.tv_label_category_edit:
                if (isEditting) {
                    tvLabelCategoryEdit.setText(R.string.s_update);
                    isEditting = false;
                    if (mEventActive && mPromotionActive && mSalesActive && mFundRaisingActive) {
                        setAllActive(true);
                    }
                } else {
                    if (!mAllActive && !mEventActive && !mFundRaisingActive && !mPromotionActive && !mSalesActive) {
                        showSnackBar(main, mContext.getResources().getString(R.string.you_need_to_select_atleast_one_category), true);
                    } else {
                        showProgressDialog();
                        mProfileViewModel.onUpdateCategories(mAllActive, mEventActive, mPromotionActive, mSalesActive, mFundRaisingActive);
                        isEditting = true;
                    }
                }
                setViewsForEditCategories();
                break;
            case R.id.tv_my_cards:
                startActivity(new Intent(mContext, CardListActivity.class));
                break;
            case R.id.tv_my_address:
                startActivity(new Intent(mContext, AddressListActivity.class));
                break;
            case R.id.iv_email_not_verified:
                openEmailVerifiationDialog();
                break;
        }
    }

    /**
     * open dialog for email verification user have update  or resend email option via this
     */
    private void openEmailVerifiationDialog() {
        if (getActivity() != null) {
            emailVerificationDialog = new Dialog(getActivity(), R.style.customDialog);
            emailVerificationDialog.setContentView(R.layout.dialog_email_verification);
            AppUtils.dimDialogBackground(emailVerificationDialog);
            final TextView tvUpdate = emailVerificationDialog.findViewById(R.id.tv_update);
            TextView tvResend = emailVerificationDialog.findViewById(R.id.tv_resend);
            TextView tvCancel = emailVerificationDialog.findViewById(R.id.tv_close);


            emailVerificationDialog.show();
            //set listener on update view
            tvUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // move to update screen
                    mProfileHost.steerToUpdateEmail();
                    emailVerificationDialog.dismiss();

                }
            });
            //set listener on resend view
            tvResend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressDialog();
                    mProfileViewModel.onResendViewClick();
                }
            });
            //set listener on cancel view
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emailVerificationDialog.dismiss();
                }
            });


        }
    }

    /**
     * set views for editing categories
     */
    private void setViewsForEditCategories() {
        removeCategoriesViews();

    }

    @Override
    public void onResume() {
        super.onResume();
        showProgressDialog();
        mProfileViewModel.gettingUserProfile();

    }

    private void settingProfileData() {
        if (mProfileViewModel.getName() != null)
            tvUserName.setText(mProfileViewModel.getName());

        if (mProfileViewModel.getDateOfBirth() != null && !mProfileViewModel.getDateOfBirth().equals("")
                && !mProfileViewModel.getDateOfBirth().equals("null"))
            tvDateOfBirth.setText(DateTimeUtils.timeStampToString(mProfileViewModel.getDateOfBirth()));

        if (mProfileViewModel.getMobileNumber() != null)
            tvMobileNumber.setText(mProfileViewModel.getMobileNumber());

        if (mProfileViewModel.getEmailId() != null)
            tvUserEmailId.setText(mProfileViewModel.getEmailId());


        if (mProfileViewModel.getProfileImage() != null)
            Glide.with(getContext()).load(mProfileViewModel.getProfileImage())
                    .asBitmap().centerCrop().placeholder(R.drawable.ic_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivUserImage);

        if (mProfileViewModel.isEmailIdVerified().equals(AppConstantClass.VERIFIED))
            ivEmailNotVerified.setVisibility(View.GONE);
        else
            ivEmailNotVerified.setVisibility(View.VISIBLE);
    }

    public interface IProfileHost {

        void steerToEditProfile();

        void steerToUpdateEmail();
    }

    @Override
    protected void onFailure(FailureResponse failureResponse) {
        super.onFailure(failureResponse);
        hideProgressDialog();
        showToastShort(failureResponse.getErrorMessage());

//        if (failureResponse.getErrorCode() == 422)
//            logout(mContext);
    }
}
