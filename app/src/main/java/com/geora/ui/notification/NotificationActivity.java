package com.geora.ui.notification;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseActivity;
import com.geora.listeners.RecycleListener;
import com.geora.model.FailureResponse;
import com.geora.model.commonresponse.CommonResponse;
import com.geora.model.notificationresponse.Datum;
import com.geora.model.notificationresponse.NotificationResponse;
import com.geora.socket.SocketConstant;
import com.geora.util.ActivityUtils;
import com.geora.util.AppUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NotificationActivity extends BaseActivity implements RecycleListener {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.sr)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.tv_no_notification)
    TextView tvNoNotification;
    @BindView(R.id.tv_des)
    TextView tvDes;
    @BindView(R.id.rl_no_data)
    RelativeLayout rlNoData;
    private NotificationAdapter mAdapter;
    private Unbinder unbinder;
    private NotificationViewModel mNotificationViewModel;
    private ArrayList<Datum> mNotificationList;
    private int pageNo = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        unbinder = ButterKnife.bind(this);
        tvTitle.setText(getResources().getString(R.string.notifications));
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(getResources().getString(R.string.clear_all));
        tvRight.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));

        mNotificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        mNotificationViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());

        setObserver();
        //setting adapter
        setUpSwipeRefresh();
        setUpAdapter();
        setListener();
        //setting views for no data screen
        noDataScreen();
        if (isInternetAvailable()) {
            isLoading = true;
            pageNo = 1;
            isLastPage = false;
            swipeRefresh.setRefreshing(false);
            showProgressDialog();
            mNotificationViewModel.gettingMyActivityData(pageNo);
        } else {
            showSnackBar(rvList, getResources().getString(R.string.no_internet_connection), true);
        }
    }



    /**
     * function to set listener on views
     */
    private void setListener() {
        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >=
                            totalItemCount - 4 && firstVisibleItemPosition >= 0) {
                        if (isInternetAvailable()) {
                            isLoading = true;
                            ++pageNo;
                            mNotificationViewModel.gettingMyActivityData(pageNo);
                        } else {
                            showSnackBar(rvList, getResources().getString(R.string.no_internet_connection), true);
                        }
                    }
                }
            }
        });
    }
    private void setObserver() {
        mNotificationList = new ArrayList<>();

        mNotificationViewModel.getActivityLiveData().observe(this, new Observer<NotificationResponse>() {
            @Override
            public void onChanged(@Nullable NotificationResponse model) {
                isLoading = false;
                hideProgressDialog();
                swipeRefresh.setRefreshing(false);
                if (model != null) {
                    if (AppUtils.checkUserValid(NotificationActivity.this, model.getCode(), model.getMessage())) {
                        isLoading = false;
                        isLastPage = (int) model.getData().getLastPage() == (int) model.getData().getCurrentPage();
                        if (model.getData().getCurrentPage() == 1) {
                            mNotificationList.clear();
                        }
                        mNotificationList.addAll(model.getData().getData());
                        mAdapter.notifyDataSetChanged();
                        noDataScreen();
                    }
                }

            }
        });

        mNotificationViewModel.getClearListLiveData().observe(this, new Observer<CommonResponse>() {
            @Override
            public void onChanged(@Nullable CommonResponse model) {
                isLoading = false;
                hideProgressDialog();
                swipeRefresh.setRefreshing(false);
                if (model != null) {
                   if (model.getCODE() == 200 ||model.getCODE() == 201) {
                       mNotificationList.clear();
                       mAdapter.notifyDataSetChanged();
                       noDataScreen();
                   }
                }

            }
        });
    }

    /**
     * setting no data screen
     */
    private void noDataScreen() {
        if (mNotificationList == null || mNotificationList.size() == 0) {
            rlNoData.setVisibility(View.VISIBLE);
            tvDes.setText(getString(R.string.sorry_but_there_are_no_notifications_for_you_right_now));
            tvTitle.setText(getString(R.string.no_notifications));
            tvRight.setVisibility(View.GONE);

        }else {
            rlNoData.setVisibility(View.GONE);
            tvRight.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void setUpSwipeRefresh() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isLoading = true;
                pageNo = 1;
                isLastPage = false;
                swipeRefresh.setRefreshing(true);
                mNotificationViewModel.gettingMyActivityData(pageNo);
            }
        });
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_notification;
    }

    //set adapter for home list view
    private void setUpAdapter() {
        mAdapter = new NotificationAdapter(this, mNotificationList, this);
        linearLayoutManager = new LinearLayoutManager(rvList.getContext(), LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setAdapter(mAdapter);
    }

    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishAfterTransition();
                break;
            case R.id.tv_right:
                if (isInternetAvailable()) {
                    showProgressDialog();
                    mNotificationViewModel.hitClearListData();
                } else {
                    showSnackBar(rvList, getResources().getString(R.string.no_internet_connection), true);
                }
                break;
        }
    }

    @Override
    public void onItemClicked(View view, String s, int pos) {
        Bundle bundle = new Bundle();
        bundle.putString(SocketConstant.SOCKEYKEYS.CAMP_ID, String.valueOf(mNotificationList.get(pos).getCampId()));
        bundle.putString(SocketConstant.SOCKEYKEYS.CAMP_TYPE, String.valueOf(mNotificationList.get(pos).getCampType() == 0 ? 4 : mNotificationList.get(pos).getCampType()));
        bundle.putString(SocketConstant.SOCKEYKEYS.BEACON_ID, mNotificationList.get(pos).getBeaconId());
        ActivityUtils.startDetailsActivity(this, bundle);
    }

    @Override
    public void onItemLongClicked(String s, int pos) {

    }

    @Override
    protected void onFailure(FailureResponse failureResponse) {
        super.onFailure(failureResponse);
        isLoading = false;
        hideProgressDialog();
        swipeRefresh.setRefreshing(false);

    }

    @Override
    protected void onErrorOccurred(Throwable throwable) {
        super.onErrorOccurred(throwable);
        isLoading = false;
        hideProgressDialog();
        swipeRefresh.setRefreshing(false);
    }
}
