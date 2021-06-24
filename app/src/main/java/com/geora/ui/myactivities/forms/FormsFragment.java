package com.geora.ui.myactivities.forms;

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
import com.geora.model.formresponse.FormModel;
import com.geora.model.myactivity.EventData;
import com.geora.ui.DetailsActivity.MyDetailsActivity;
import com.geora.ui.myactivities.MyActivityFragment;
import com.geora.util.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FormsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RecycleListener, MyActivityFragment.FormsFragmentCallback {
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.tv_title_card)
    TextView tvTitle;
    @BindView(R.id.tv_des)
    TextView tvDesc;
    @BindView(R.id.ly_no_data)
    RelativeLayout layoutNoData;
    @BindView(R.id.sr)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.iv_no_data)
    ImageView ivNoDataImage;
    private FormViewAdapter mAdapter;
    private Unbinder unbinder;
    private Context mContext;
    private List<EventData> list = new ArrayList<>();
    private OnRefreshCallback refreshCallback;
    private FormViewModel mFundraisedViewModel;
    private Observer<FormModel> observer;
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoading = true;
    private boolean isLastPage = false;
    private int pageNo = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forms, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getContext();

        mFundraisedViewModel = ViewModelProviders.of(this).get(FormViewModel.class);
        mFundraisedViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());

        setObserver();

        swipeRefresh.setOnRefreshListener(this);
        //setting adapter
        setUpAdapter();
        setListener();
        noDataScreen();
        return view;
    }

    /**
     * method setting no data screen
     */
    private void noDataScreen() {
        if (list == null || list.size() == 0) {
            layoutNoData.setVisibility(View.VISIBLE);
            tvDesc.setText(getString(R.string.seems_that_there_no_form_availabe));
            tvTitle.setText(getString(R.string.no_form));
            ivNoDataImage.setImageDrawable(mContext.getDrawable(R.drawable.ic_no_offer_browse_graphic));
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
                            mFundraisedViewModel.gettingFormData(pageNo);
                        }
                        else
                            showSnackBar(rvList, mContext.getResources().getString(R.string.no_internet_connection), true);                    }
                }
            }
        });
    }


    private void setObserver() {
        observer = new Observer<FormModel>() {

            @Override
            public void onChanged(@Nullable FormModel model) {
                if (model != null) {
                    if (AppUtils.checkUserValid(getActivity(), model.getCode(), model.getMessage())) {
                        isLoading = false;
                        isLastPage = (int) model.getData().getLastPage() == (int) model.getData().getCurrentPage();
                        onFormsTabSelected(model.getData().getData(), (int) model.getData().getCurrentPage());
                    }

                }
            }
        };
        mFundraisedViewModel.getActivityLiveData().observe(this, observer);
    }

    public void removeObserver() {
        if (mFundraisedViewModel.getActivityLiveData().hasObservers())
            mFundraisedViewModel.getActivityLiveData().removeObserver(observer);
    }


    /**
     * method setting adapter for forms tab section
     */
    private void setUpAdapter() {
        mAdapter = new FormViewAdapter(list);
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        removeObserver();
    }

    @Override
    public void onItemClicked(View view, String s, int pos) {
            Intent intent = new Intent(getContext(), MyDetailsActivity.class);
            EventData eventData = list.get(pos);
            Bundle bundle = new Bundle();
            bundle.putParcelable("object", eventData);
            intent.putExtra(AppConstants.HomeListViewConstants.ID, AppConstants.HomeListViewConstants.FORMS_FRAGMENT);
            intent.putExtra(AppConstants.HomeListViewConstants.TITLE, AppConstants.HomeListViewConstants.FORM_DETAILS);
            intent.putExtra("data", bundle);
            mContext.startActivity(intent);
    }

    @Override
    public void onItemLongClicked(String s, int pos) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFormsTabSelected(List<EventData> eventDataList, int pageNo) {
        isLoading = false;
        if (eventDataList != null && eventDataList.size() > 0) {
            layoutNoData.setVisibility(View.GONE);
            if (pageNo == 1) list.clear();
            list.addAll(eventDataList);
            mAdapter.notifyDataSetChanged();
        } else {
            noDataScreen();
        }
    }


    /**
     * method getting instance of OnRefreshCallback
     *
     * @param refreshCallback
     */
    public void setRefreshListener(OnRefreshCallback refreshCallback) {
        this.refreshCallback = refreshCallback;
    }

    @Override
    public void onRefresh() {
        if (refreshCallback != null) {
            pageNo = 1;
            isLastPage = false;
            refreshCallback.onRefreshView();
            swipeRefresh.setRefreshing(false);
        }
    }
}
