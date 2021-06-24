package com.geora.ui.DetailsActivity.PromotionDetail;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseFragment;
import com.geora.customviews.DialogForDonateAmount;
import com.geora.customviews.DialogForValidate;
import com.geora.data.constants.AppConstantClass;
import com.geora.listeners.DialogCallback;
import com.geora.listeners.PagerCallBack;
import com.geora.model.ChargeAmountModel;
import com.geora.model.myactivity.FundData;
import com.geora.model.myactivity.GetImage_;
import com.geora.ui.DetailsActivity.ImageViewPagerAdapter;
import com.geora.ui.DetailsActivity.MyDetailsActivity;
import com.geora.ui.cardlist.CardListActivity;
import com.geora.ui.test.TestActivity;
import com.geora.util.DateTimeUtils;
import com.geora.util.ImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class PromotionDetailFragment extends BaseFragment {

    @BindView(R.id.tv_fund_type)
    TextView tvFundType;

    @BindView(R.id.tv_address)
    TextView tvAddress;

    @BindView(R.id.progress_amt_donated)
    ProgressBar progressBar;

    @BindView(R.id.tv_ends_on_date)
    TextView tvEndsOnDate;

    @BindView(R.id.tv_amount_donated_data)
    TextView tvAmountDonatedData;

    @BindView(R.id.tv_fund_raised_data)
    TextView tvFundRaisedData;

    @BindView(R.id.tv_fund_taget_data)
    TextView tvFundTargetData;

    @BindView(R.id.tv_fund_desc_data)
    TextView tvFundDescData;

    @BindView(R.id.iv_promotion_image)
    ImageView ivPromotionImage;

    @BindView(R.id.tv_donate)
    TextView tvDonate;
    @BindView(R.id.main)
    RelativeLayout main;

    @BindView(R.id.vp_images)
    ViewPager vpImages;
    @BindView(R.id.pageIndicatorView)
    CircleIndicator pageIndicatorView;

    private Unbinder unbinder;
    private FundData fundData = new FundData();
    private Activity mActivity;
    private String[] allImagesList;
    private ImageViewPagerAdapter mAdapter;
    private String allImages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotion_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        mActivity = getActivity();
        Bundle bundle = ((MyDetailsActivity) Objects.requireNonNull(getActivity())).getBundle();
        fundData = bundle.getParcelable("object");
        if (fundData != null)
            settingViews();
        // Inflate the layout for this fragment
        return view;
    }

    private void settingViews() {
        tvFundType.setText(fundData.getGetFund().getGetCampaign().getCampTitle());
        tvAddress.setText(fundData.getGetFund().getGetCampaign().getCampAddress());
        tvEndsOnDate.append(" " + DateTimeUtils.timeStampToEndCamp(fundData.getGetFund().getGetCampaign().getCampEndDate().toString()));

        if (System.currentTimeMillis() < fundData.getGetFund().getGetCampaign().getCampEndDate()) {
            tvDonate.setBackground(getResources().getDrawable(R.drawable.drawable_rect_circular_warm_gray));
        }
        progressBar.setMax(fundData.getGetFund().getFundTarget());
        progressBar.setProgress(fundData.getGetFund().getFundRaised().intValue());
        tvAmountDonatedData.setText(getString(R.string.doller) + String.format(Locale.US, "%.2f", fundData.getAmount()));
        ImageUtils.setImage(getContext(), fundData.getGetFund().getGetCampaign().getGetDefaultImage().getImageUrl(), ivPromotionImage, getResources().getDrawable(R.drawable.placeholder_medium));
        tvFundDescData.setText(fundData.getGetFund().getGetCampaign().getCampDesc());
        tvFundRaisedData.setText(getString(R.string.doller) + String.format(Locale.US, "%.2f", fundData.getGetFund().getFundRaised()));
        tvFundTargetData.setText(getString(R.string.doller) + fundData.getGetFund().getFundTarget().toString());
        setupAdapter();

    }

    @OnClick({R.id.tv_donate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_donate:
                if (fundData != null && fundData.getGetFund().getGetCampaign().getCampStatus() == 2) {
                    new DialogForValidate(mActivity, new DialogCallback() {
                        @Override
                        public void onSubmit(View view, String result, int stars) {
                            mActivity.finish();
                        }
                    }, getString(R.string.campain_not_active), getString(R.string.okay)).show();
                    return;
                }
                OpenDonationBox();
                break;
        }
    }



    private void setupAdapter() {
        List<GetImage_> fundImages = fundData.getGetFund().getGetCampaign().getGetImage();
        List<String> images = new ArrayList<>();
        for (int i = 0; i < fundImages.size(); i++) {
            images.add(fundImages.get(i).getImageUrl());
        }
        allImages = String.valueOf(images);
        allImages = allImages.replace("[", "");
        allImages = allImages.replace("]", "");
        allImages = allImages.replace(" ", "");
        allImagesList = allImages.split(",");
        mAdapter = new ImageViewPagerAdapter(mActivity, allImagesList, new PagerCallBack() {
            @Override
            public void onItemClicked() {
                int pos = vpImages.getCurrentItem();
                Intent intent = new Intent(mActivity, TestActivity.class);
                intent.putExtra(AppConstantClass.DATA, allImages);
                intent.putExtra(AppConstantClass.POSITION, pos);
                startActivity(intent);
            }
        });
        vpImages.setAdapter(mAdapter);
        if (allImagesList.length > 1)
            pageIndicatorView.setViewPager(vpImages);
    }


    private void OpenDonationBox() {
        new DialogForDonateAmount(mActivity, new DialogCallback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSubmit(View view, String result, int s) {
                if (!result.isEmpty()) {
                    moveToCardListActivity(result);
                } else
                    showSnackBar(main, "Add amount to donate", true);
            }
        }).show();
    }

    private void moveToCardListActivity(String amount) {
        ChargeAmountModel chargeAmountModel = new ChargeAmountModel();
        chargeAmountModel.setCampType(2 + "");
        chargeAmountModel.setAmount(amount);
        chargeAmountModel.setBusnissUserId(fundData.getGetFund().getGetCampaign().getBusinessUserId().toString());
        chargeAmountModel.setFundId(fundData.getFundId().toString());
        Intent intent = new Intent(mActivity, CardListActivity.class);
        intent.putExtra(AppConstantClass.CHARGEDATA, chargeAmountModel);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }
}
