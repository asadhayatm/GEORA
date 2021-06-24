package com.geora.ui.myactivities.orders;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseFragment;
import com.geora.constants.AppConstants;
import com.geora.listeners.OnRefreshCallback;
import com.geora.listeners.RecycleListener;
import com.geora.model.FailureResponse;
import com.geora.model.myactivity.SalesData;
import com.geora.model.orderresponse.OrderModel;
import com.geora.ui.DetailsActivity.MyDetailsActivity;
import com.geora.ui.myactivities.MyActivityFragment;
import com.geora.util.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OrdersFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RecycleListener, MyActivityFragment.OrderFragmentCallback {
    @BindView(R.id.rv_list)
    RecyclerView rvList;

    @BindView(R.id.tv_title_card)
    TextView tvTitle;

    @BindView(R.id.tv_des)
    TextView tvDesc;

    @BindView(R.id.sr)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.rl_no_address)
    RelativeLayout layoutNoData;

    @BindView(R.id.iv_no_data)
    ImageView ivNoDataImage;

    private List<SalesData> mList = new ArrayList<>();
    private OrdersViewAdapter mAdapter;
    private Unbinder unbinder;
    private OnRefreshCallback mRefreshCallback;
    private Context mContext;
    private int pageNo = 1;
    private OrderViewModel mOrderViewModel;
    private Observer<OrderModel> observer;
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoading = true;
    private boolean isLastPage = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getContext();

        mOrderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        mOrderViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());

        setObserver();
        //setting adapter
        setUpAdapter();
        setListener();
        swipeRefresh.setOnRefreshListener(this);
        //setting views for no data screen
        noDataScreen();
        return view;
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
                            mOrderViewModel.gettingMyActivityData(pageNo);
                        }
                        else
                            showSnackBar(rvList, mContext.getResources().getString(R.string.no_internet_connection), true);                    }
                }
            }
        });
    }


    private void setObserver() {
        observer = new Observer<OrderModel>() {

            @Override
            public void onChanged(@Nullable OrderModel model) {
                if (model != null) {
                    if (AppUtils.checkUserValid(getActivity(), model.getCode(), model.getMessage())) {
                        isLoading = false;
                        isLastPage = (int) model.getData().getLastPage() == (int) model.getData().getCurrentPage();
                        onOrderedTabSelected(model.getData().getData(), (int) model.getData().getCurrentPage());
                    }
                }

            }
        };
        mOrderViewModel.getActivityLiveData().observe(this, observer);
    }

    public void removeObserver() {
        if (mOrderViewModel.getActivityLiveData().hasObservers())
            mOrderViewModel.getActivityLiveData().removeObserver(observer);
    }

    /**
     * set adapter for my order tab section
     */
    private void setUpAdapter() {
        mAdapter = new OrdersViewAdapter(mList);
        linearLayoutManager = new LinearLayoutManager(rvList.getContext(), LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setAdapter(mAdapter);
        mAdapter.setClickListener(this);

    }

    @Override
    protected void onFailure(FailureResponse failureResponse) {
        super.onFailure(failureResponse);
        swipeRefresh.setRefreshing(false);
    }

    @Override
    protected void onErrorOccurred(Throwable throwable) {
        super.onErrorOccurred(throwable);
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onItemClicked(View view, String s, int pos) {
        Intent intent = new Intent(mContext, MyDetailsActivity.class);
        intent.putExtra(AppConstants.HomeListViewConstants.ID, AppConstants.HomeListViewConstants.ORDERS_FRAGMENT);
        intent.putExtra(AppConstants.HomeListViewConstants.TITLE, AppConstants.HomeListViewConstants.ORDER_DETAILS);
        Bundle bundle = new Bundle();
        SalesData salesData = mList.get(pos);
        bundle.putParcelable("object", salesData);
        intent.putExtra("data", bundle);
        mContext.startActivity(intent);

    }

    @Override
    public void onItemLongClicked(String s, int pos) {

    }

    @Override
    public void onOrderedTabSelected(List<SalesData> salesDataList, int pageNo) {
        isLoading = false;
        if (salesDataList != null && salesDataList.size() > 0) {
            layoutNoData.setVisibility(View.GONE);
            if (pageNo == 1) mList.clear();
            mList.addAll(salesDataList);
            mAdapter.notifyDataSetChanged();
        } else {
            noDataScreen();
        }
    }


    /**
     * setting no data screen
     */
    private void noDataScreen() {
        if (mList == null || mList.size() == 0) {
            layoutNoData.setVisibility(View.VISIBLE);
            tvDesc.setText(getString(R.string.seems_like_there_is_no_activity_perfomed_right_now));
            tvTitle.setText(getString(R.string.no_activity_performed));
            ivNoDataImage.setImageDrawable(mContext.getDrawable(R.drawable.ic_no_offer_browse_graphic));
        }
    }

    /*
    setting instance for OnRefreshCallback
    */
    public void setRefreshListener(OnRefreshCallback refreshCallback) {
        this.mRefreshCallback = refreshCallback;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        removeObserver();

    }

    @Override
    public void onRefresh() {
        if (mRefreshCallback != null) {
            pageNo = 1;
            isLastPage = false;
            mRefreshCallback.onRefreshView();
            swipeRefresh.setRefreshing(false);
        }
    }
}
