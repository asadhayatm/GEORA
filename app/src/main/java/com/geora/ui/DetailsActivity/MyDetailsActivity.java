package com.geora.ui.DetailsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseActivity;
import com.geora.constants.AppConstants;
import com.geora.ui.DetailsActivity.EventDetail.EventDetailFragment;
import com.geora.ui.DetailsActivity.OrderDetail.OrderDetailFragment;
import com.geora.ui.DetailsActivity.PromotionDetail.PromotionDetailFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MyDetailsActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_main)
    LinearLayout llMain;

    private int mId;
    private String mTitle;
    private Unbinder unbinder;
    private Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_details);
        unbinder = ButterKnife.bind(this);
        getData();
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_my_details;
    }

    /**
     * setting fragment
     */
    private void settingFragment() {
        switch (mId) {
            case AppConstants.HomeListViewConstants.ORDERS_FRAGMENT:
                tvTitle.setText(mTitle);
                addFragment(R.id.fl_details, new OrderDetailFragment(), OrderDetailFragment.class.getSimpleName());
                break;

            case AppConstants.HomeListViewConstants.FORMS_FRAGMENT:
                tvTitle.setText(mTitle);
                addFragment(R.id.fl_details, new EventDetailFragment(), EventDetailFragment.class.getSimpleName());
                break;

            case AppConstants.HomeListViewConstants.FUNDRAISED_FRAGMENT:
                tvTitle.setText(mTitle);
                addFragment(R.id.fl_details, new PromotionDetailFragment(), PromotionDetailFragment.class.getSimpleName());
                break;
        }
    }

    /**
     * getting data from intent
     */
    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            mTitle = intent.getStringExtra(AppConstants.HomeListViewConstants.TITLE);
            mId = intent.getIntExtra(AppConstants.HomeListViewConstants.ID, 0);
            bundle = intent.getBundleExtra("data");
            settingFragment();
        }
    }

    /**
     * methods return bundle to the calling fragment
     *
     * @return
     */
    public Bundle getBundle() {
        return bundle;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishAfterTransition();
                break;
        }
    }

    public View getParentView() {
        return llMain;
    }
}
