package com.geora.ui.myactivities;

import android.app.Activity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.geora.R;
import com.geora.adapters.ViewPagerAdapter;
import com.geora.base.BaseFragment;
import com.geora.data.constants.AppConstantClass;
import com.geora.listeners.OnRefreshCallback;
import com.geora.model.FailureResponse;
import com.geora.model.myactivity.EventData;
import com.geora.model.myactivity.FundData;
import com.geora.model.myactivity.MyActivityModel;
import com.geora.model.myactivity.SalesData;
import com.geora.ui.home.HomeActivity;
import com.geora.ui.myactivities.forms.FormsFragment;
import com.geora.ui.myactivities.fundraised.FundraisedFragment;
import com.geora.ui.myactivities.orders.OrdersFragment;
import com.geora.util.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MyActivityFragment extends BaseFragment implements OnRefreshCallback {

    @BindView(R.id.vp_my_activity)
    ViewPager vpMyActivity;
    @BindView(R.id.tb)
    TabLayout tb;
    @BindView(R.id.main)
    FrameLayout main;
    private OrdersFragment ordersFragment = new OrdersFragment();
    private FundraisedFragment fundraisedFragment = new FundraisedFragment();
    private FormsFragment formsFragment = new FormsFragment();
    private Unbinder unbinder;
    private Activity mActivity;
    private SparseArray<Fragment> fragments;
    private MyActivityViewModel mActivityViewModel = new MyActivityViewModel();
    private ViewPagerAdapter viewPagerAdapter;
    private Observer<MyActivityModel> observer;
    private int pageNo = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_my_activity, container, false);
        unbinder = ButterKnife.bind(this, view);
        mActivity = getActivity();

        ((HomeActivity) mActivity).setupToolbar(4, mActivity.getResources().getString(R.string.my_activities));
        initVariables();
        setupViewpagerAndAdapter();
        ordersFragment.setRefreshListener(this);
        fundraisedFragment.setRefreshListener(this);
        formsFragment.setRefreshListener(this);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivityViewModel = ViewModelProviders.of(this).get(MyActivityViewModel.class);
        mActivityViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        if (isInternetAvailable())
            mActivityViewModel.gettingMyActivityData(pageNo);
        else
            showSnackBar(main, mActivity.getResources().getString(R.string.no_internet_connection), true);
        setObserver();
    }

    private void setObserver() {
        observer = new Observer<MyActivityModel>() {

            @Override
            public void onChanged(@Nullable MyActivityModel model) {
                if (model != null) {
                    if (AppUtils.checkUserValid(getActivity(), model.getCode(), model.getMessage())) {
                        ordersFragment.onOrderedTabSelected(model.getData().getSales().getData(), pageNo);
                        formsFragment.onFormsTabSelected(model.getData().getEvent().getData(), pageNo);
                        fundraisedFragment.onFundraisedTabSelected(model.getData().getFund().getData(), pageNo);
                    }
                }

            }
        };
        mActivityViewModel.getActivityLiveData().observe(this, observer);
    }

    public void removeObserver() {
        if (mActivityViewModel.getActivityLiveData().hasObservers())
            mActivityViewModel.getActivityLiveData().removeObserver(observer);
    }


    private void initVariables() {
        mActivity = getActivity();
        fragments = new SparseArray<>();

        fragments.put(1, ordersFragment);
        fragments.put(2, fundraisedFragment);
        fragments.put(3, formsFragment);

    }


    /**
     * Method to set up viewpager and adapter....
     */


    private void setupViewpagerAndAdapter() {
        String from = "";
        if (getArguments() != null && getArguments().containsKey(AppConstantClass.FROM)) {
            from = getArguments().getString(AppConstantClass.FROM, "");
        }
        tb.addTab(tb.newTab().setText(getResources().getStringArray(R.array.my_activity_names)[0]));
        tb.addTab(tb.newTab().setText(getResources().getStringArray(R.array.my_activity_names)[1]));
        tb.addTab(tb.newTab().setText(getResources().getStringArray(R.array.my_activity_names)[2]));
        viewPagerAdapter = new ViewPagerAdapter(getContext(), getChildFragmentManager(), fragments, getResources().getStringArray(R.array.my_activity_names));
        vpMyActivity.setAdapter(viewPagerAdapter);
        tb.setupWithViewPager(vpMyActivity);
        vpMyActivity.setOffscreenPageLimit(3);

        if (from.equalsIgnoreCase("CardListFund")) {
            vpMyActivity.setCurrentItem(1);
        }
        else if (from.equalsIgnoreCase("Event")) {
            vpMyActivity.setCurrentItem(2);
        }else {
            vpMyActivity.setCurrentItem(0);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        removeObserver();
    }

    @Override
    public void onRefreshView() {
        pageNo = 1;
        mActivityViewModel.gettingMyActivityData(pageNo);
    }

    public interface OrderFragmentCallback {
        void onOrderedTabSelected(List<SalesData> salesDataList, int pageNo);
    }

    public interface FundraisedFragmentCallback {
        void onFundraisedTabSelected(List<FundData> fundDataList, int pageNo);
    }

    public interface FormsFragmentCallback {
        void onFormsTabSelected(List<EventData> eventDataList, int pageNo);
    }

    @Override
    protected void onFailure(FailureResponse failureResponse) {
        super.onFailure(failureResponse);
        hideProgressDialog();
//        if (failureResponse.getErrorCode() == 422)
//            logout(mActivity);
    }

    public void hitPaginationApi() {
        ++pageNo;
        mActivityViewModel.gettingMyActivityData(pageNo);
    }
}
