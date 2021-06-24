package com.geora.ui.home.homelistview;


import android.app.Activity;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseFragment;
import com.geora.customviews.DialogForConfirmation;
import com.geora.data.DataManager;
import com.geora.data.constants.AppConstantClass;
import com.geora.listeners.RecycleListener;
import com.geora.model.FailureResponse;
import com.geora.model.beaconsavedlist.BeaconSaveListModel;
import com.geora.model.beaconsavedlist.Datum;
import com.geora.socket.SocketCallback;
import com.geora.socket.SocketConstant;
//import com.geora.socket.SocketManager;
import com.geora.ui.home.HomeActivity;
import com.geora.util.ActivityUtils;
import com.geora.util.AppUtils;
import com.google.gson.Gson;

import org.java_websocket.handshake.ServerHandshake;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeListViewFragment extends BaseFragment implements SocketCallback, RecycleListener {


    @BindView(R.id.rv_list_view)
    RecyclerView rvListView;
    @BindView(R.id.tv_events)
    TextView tvEvents;
    @BindView(R.id.tv_promotion)
    TextView tvPromotion;
    @BindView(R.id.tv_fundraising)
    TextView tvFundraising;
    @BindView(R.id.tv_sales)
    TextView tvSales;
    @BindView(R.id.ll_categories)
    LinearLayout llCategories;
    @BindView(R.id.rl_no_item)
    RelativeLayout rlNoItem;
    @BindView(R.id.iv_no_data)
    ImageView ivNoData;
    @BindView(R.id.tv_title_card)
    TextView tvTitleCard;
    @BindView(R.id.tv_des)
    TextView tvDes;
    @BindView(R.id.main)
    ConstraintLayout main;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private Unbinder unbinder;
    private AppCompatActivity mActivity;
    private List<Datum> list;
    private int fragmentId = 4;
    private int deletedPos = -1;

    private HomeListViewAdapter mAdapter;
    private HomeListViewViewModel mHomeListViewViewModel;

    private boolean isEvent = false, isPromotions = false, isSales = false, isFundraising = false, isAll = false;
    private boolean isLoading = false;


    public HomeListViewFragment() {
        // Required empty public constructor
    }

    private void setNoData() {
        ivNoData.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_no_offer_listing_graphic));
        tvTitleCard.setText(getString(R.string.no_saved_offers));
        tvDes.setText(getString(R.string.sorry_no_offer_till_now));
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_list_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        mActivity = (AppCompatActivity) getActivity();
        ((HomeActivity) mActivity).setupToolbar(1, getString(R.string.saved_campaigns));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = new LinkedList<>();
        mHomeListViewViewModel = ViewModelProviders.of(this).get(HomeListViewViewModel.class);
        mHomeListViewViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        setObservers();
        intitViewsAndVariables();

        showProgressDialog();
        getSavedDataFormServer();
    }

    private void intitViewsAndVariables() {
        setUpAdapter();
        SetCategories();
        setNoData();
        refreshLayout.setOnRefreshListener(() -> {
            if (!isLoading) {
                refreshLayout.setRefreshing(true);
                getSavedDataFormServer();
            }else {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void SetCategories() {
        if (mHomeListViewViewModel.getEventStatus().equals("1")) {
            isEvent = true;
            setSelectedOrUnselectedView(true, AppConstantClass.CATEGORY.EVENT);
        } else {
            setSelectedOrUnselectedView(false, AppConstantClass.CATEGORY.EVENT);
            isEvent = false;
        }

        if (mHomeListViewViewModel.getSalesStatus().equals("1")) {
            isSales = true;
            setSelectedOrUnselectedView(true, AppConstantClass.CATEGORY.SALES);
        } else {
            setSelectedOrUnselectedView(false, AppConstantClass.CATEGORY.SALES);
            isSales = false;
        }

        if (mHomeListViewViewModel.getPromotionStatus().equals("1")) {
            setSelectedOrUnselectedView(true, AppConstantClass.CATEGORY.PROMOTION);
            isPromotions = true;
        } else {
            setSelectedOrUnselectedView(false, AppConstantClass.CATEGORY.PROMOTION);
            isPromotions = false;
        }

        if (mHomeListViewViewModel.getFundRaisingStatus().equals("1")) {
            setSelectedOrUnselectedView(true, AppConstantClass.CATEGORY.FUNDRAISING);
            isFundraising = true;
        } else {
            setSelectedOrUnselectedView(false, AppConstantClass.CATEGORY.FUNDRAISING);
            isFundraising = false;
        }
        if (mHomeListViewViewModel.getAllStatus().equals("1")) {
            isAll = true;
            isFundraising = true;
            isPromotions = true;
            isEvent = true;
            isSales = true;
            setSelectedOrUnselectedView(false, AppConstantClass.CATEGORY.ALLACTIVE);
        } else {
            isAll = false;
        }
    }


    private void setSelectedOrUnselectedView(boolean isActive, String category) {
        switch (category) {
            case AppConstantClass.CATEGORY.EVENT:
                if (isActive)
                    setSelectedEvent(tvEvents);
                else
                    setUnSelectedEvent(tvEvents);
                break;
            case AppConstantClass.CATEGORY.SALES:
                if (isActive)
                    setSelectedEvent(tvSales);
                else
                    setUnSelectedEvent(tvSales);
                break;
            case AppConstantClass.CATEGORY.PROMOTION:
                if (isActive)
                    setSelectedEvent(tvPromotion);
                else
                    setUnSelectedEvent(tvPromotion);
                break;
            case AppConstantClass.CATEGORY.FUNDRAISING:
                if (isActive)
                    setSelectedEvent(tvFundraising);
                else
                    setUnSelectedEvent(tvFundraising);
                break;
            case AppConstantClass.CATEGORY.ALLACTIVE:
                setSelectedEvent(tvEvents);
                setSelectedEvent(tvSales);
                setSelectedEvent(tvPromotion);
                setSelectedEvent(tvFundraising);
                break;
        }
    }


    @OnClick({R.id.tv_events, R.id.tv_sales, R.id.tv_promotion, R.id.tv_fundraising})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_events:
                isAll = false;
                isEvent = !isEvent;
                setSelectedOrUnselectedView(isEvent, AppConstantClass.CATEGORY.EVENT);
                showProgressDialog();
                getSavedDataFormServer();

                break;
            case R.id.tv_sales:
                isAll = false;
                isSales = !isSales;
                setSelectedOrUnselectedView(isSales, AppConstantClass.CATEGORY.SALES);
                showProgressDialog();
                getSavedDataFormServer();

                break;
            case R.id.tv_promotion:
                isAll = false;
                isPromotions = !isPromotions;
                setSelectedOrUnselectedView(isPromotions, AppConstantClass.CATEGORY.PROMOTION);
                showProgressDialog();
                getSavedDataFormServer();


                break;
            case R.id.tv_fundraising:
                isAll = false;
                isFundraising = !isFundraising;
                setSelectedOrUnselectedView(isFundraising, AppConstantClass.CATEGORY.FUNDRAISING);
                showProgressDialog();
                getSavedDataFormServer();


                break;
        }
    }

    private void setUnSelectedEvent(TextView tvEvents) {
        tvEvents.setTextColor(getResources().getColor(R.color.warm_grey));
        tvEvents.setBackground(getResources().getDrawable(R.drawable.warm_gray_border_round_rect));

    }

    private void setSelectedEvent(TextView tvcategory) {
        tvcategory.setTextColor(getResources().getColor(R.color.butterscotch));
        tvcategory.setBackground(getResources().getDrawable(R.drawable.yellow_border_round_rect));

    }

    //set adapter for home list view
    private void setUpAdapter() {

        mAdapter = new HomeListViewAdapter(list, mActivity, fragmentId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rvListView.getContext(), LinearLayoutManager.VERTICAL, false);
        rvListView.setLayoutManager(linearLayoutManager);
        rvListView.setAdapter(mAdapter);
        mAdapter.setClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgressDialog();
//        unbinder.unbind();
    }


    private void connectWebSocket(SocketCallback callback) {
//        SocketManager.getInstance().setSocketCallbackListener(callback);
    }

    @Override
    public void onResume() {
        super.onResume();
//        connectWebSocket(this);
//        getSavedDataFormServer();
    }

    private void getSavedDataFormServer() {
        /*JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, SocketConstant.SOCKEYKEYS.SAVED);
            jsonObject.put(SocketConstant.SOCKEYKEYS.FILTER, getFilterData());
            jsonObject.put(SocketConstant.SOCKEYKEYS.SEARCH, "");
            jsonObject.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());
            if (isInternetAvailable()) {
                showProgressDialog();
                SocketManager.getInstance().sendData(jsonObject.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        HashMap<String, Object> params = new HashMap<>();

        params.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, SocketConstant.SOCKEYKEYS.SAVED);
        params.put(SocketConstant.SOCKEYKEYS.FILTER, getFilterData());
        params.put(SocketConstant.SOCKEYKEYS.SEARCH, "");
        params.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());

        if (isInternetAvailable()) {
            isLoading = true;
            mHomeListViewViewModel.hitBeaconSaveDataApi(params);
        }
        else {
            isLoading = false;
            refreshLayout.setRefreshing(false);
            hideProgressDialog();
            showToastLong(mActivity.getResources().getString(R.string.no_internet_connection));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        connectWebSocket(null);

    }


    // getting the latest filter selected by user
    private String getFilterData() {
        String filter = "";
        if (isPromotions)
            filter = "1";
        if (isFundraising)
            filter = filter + ",2";
        if (isSales)
            filter = filter + ",3";
        if (isEvent)
            filter = filter + ",4";
        if (isAll)
            filter = "1,2,3,4";
        return filter;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
    }

    @Override
    public void onMessage(String msg) {
        if (isAdded()) {
            isLoading = false;
            hideProgressDialog();
            refreshLayout.setRefreshing(false);
            final BeaconSaveListModel beaconSaveListModel = new Gson().fromJson(msg, BeaconSaveListModel.class);
            if (AppUtils.checkUserValid(getActivity(), beaconSaveListModel.getCode(), beaconSaveListModel.getMessage())) {
                if (beaconSaveListModel.getCode() == 423 || beaconSaveListModel.getCode() == 403) {
                    showToastShort(beaconSaveListModel.getMessage());
                    logout(mActivity);
                }else {
                    if (beaconSaveListModel.getActionType().equalsIgnoreCase(SocketConstant.SOCKEYKEYS.SAVED))
                        addDataToMainList(beaconSaveListModel.getData());
                    else if (beaconSaveListModel.getActionType().equalsIgnoreCase(SocketConstant.SOCKEYKEYS.DELETE_ITEM))
                        refershList();
                }
            }
        }
    }

    private void setObservers() {
        mHomeListViewViewModel.getBeaconSaveListLiveData().observe(this, beaconSaveListModel -> {
            if (isAdded()) {
                isLoading = false;
                hideProgressDialog();
                refreshLayout.setRefreshing(false);
                if (AppUtils.checkUserValid(getActivity(), beaconSaveListModel.getCode(), beaconSaveListModel.getMessage())) {
                    if (beaconSaveListModel.getCode() == 423 || beaconSaveListModel.getCode() == 403) {
                        showToastShort(beaconSaveListModel.getMessage());
                        logout(mActivity);
                    }else {
                        addDataToMainList(beaconSaveListModel.getData());
                    }
                }
            }
        });
        mHomeListViewViewModel.getBeaconDeleteItemLiveData().observe(this, commonResponse -> {
            if (isAdded()) {
                isLoading = false;
                hideProgressDialog();
                refreshLayout.setRefreshing(false);
                if (AppUtils.checkUserValid(getActivity(), commonResponse.getCODE(), commonResponse.getMESSAGE())) {
                    if (commonResponse.getCODE() == 423 || commonResponse.getCODE() == 403) {
                        showToastShort(commonResponse.getMESSAGE());
                        logout(mActivity);
                    }else {
                        refershList();
                    }
                }
            }
        });

    }

    @Override
    protected void onErrorOccurred(Throwable throwable) {
        super.onErrorOccurred(throwable);
        refreshLayout.setRefreshing(false);
        isLoading = false;
    }

    @Override
    protected void onFailure(FailureResponse failureResponse) {
        super.onFailure(failureResponse);
        refreshLayout.setRefreshing(false);
        isLoading = false;
    }

    private void refershList() {
        ((HomeActivity) mActivity).runOnUiThread(() -> {
            hideProgressDialog();
            if (isAdded()) {
                removeData();
            }
        });


    }


    private void removeData() {
        if (deletedPos != -1) {
            list.remove(deletedPos);
            rvListView.getRecycledViewPool().clear();

            mAdapter.notifyDataSetChanged();
            deletedPos = -1;
            if (list.size() == 0)
                showNoData();
            else
                hideNoData();
        }
    }

    private void addDataToMainList(List<Datum> data) {

        final List<Datum> newList = new LinkedList<>();
//        newList.addAll(list);

        for (int i = 0; i < data.size(); i++) {
            Datum datum = data.get(i);
            for (Datum beaconData : newList) {
                if (!beaconData.getCampId().equals(datum.getCampId()))
                    newList.add(datum);
            }
        }
        if (newList.size() == 0 && data.size() != 0)
            newList.addAll(data);
        ((Activity) mActivity).runOnUiThread(() -> {
            hideProgressDialog();
            if (isAdded()) {
                addDataToAdpterAndNotify(newList);
            }
        });
    }

    private void addDataToAdpterAndNotify(List<Datum> newList) {
        if (mAdapter != null) {
            if (newList.size() == 0)
                showNoData();
            else
                hideNoData();
            rvListView.getRecycledViewPool().clear();
            list.clear();
            list.addAll(newList);
            rvListView.getRecycledViewPool().clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    private void hideNoData() {
        rvListView.setVisibility(View.VISIBLE);
        rlNoItem.setVisibility(View.GONE);
    }

    private void showNoData() {
        rlNoItem.setVisibility(View.VISIBLE);
        rvListView.setVisibility(View.GONE);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        ((HomeActivity) mActivity).runOnUiThread(() -> {
            hideProgressDialog();
            if (isAdded()) {
                if (deletedPos != -1)
                    deletedPos = -1;
            }
        });
    }

    @Override
    public void onError(Exception ex) {
        ((HomeActivity) mActivity).runOnUiThread(() -> {
            hideProgressDialog();
            if (isAdded()) {
                if (deletedPos != -1)
                    deletedPos = -1;
            }
        });
    }

    @Override
    public void onItemClicked(View view, String s, int pos) {
        switch (view.getId()) {
            case R.id.iv_cross:
                if (isInternetAvailable())
                    deleteItemFromSAvedList(pos);
                else
                    showSnackBar(main, getString(R.string.no_internet_connection), true);
                break;
            case R.id.main:
                moveToDetailActivity(pos);
                break;
        }
    }

    private void moveToDetailActivity(int pos) {
        Bundle bundle = new Bundle();
        bundle.putString(SocketConstant.SOCKEYKEYS.CAMP_ID, mAdapter.list.get(pos).getCampId().toString());
        bundle.putString(SocketConstant.SOCKEYKEYS.CAMP_TYPE, mAdapter.list.get(pos).getCampType().toString());
        bundle.putString(SocketConstant.SOCKEYKEYS.FILTER, getFilterData());
        bundle.putString(SocketConstant.SOCKEYKEYS.BEACON_ID, mAdapter.list.get(pos).getBeaconId());
        ActivityUtils.startDetailsActivity(mActivity, bundle);
    }

    private void deleteItemFromSAvedList(final int pos) {

        new DialogForConfirmation(mActivity, (view, result, s) -> {
            hideProgressDialog();
            if (isAdded()) {
                if (result.equalsIgnoreCase("yes")) {
                    showProgressDialog();
                    removeCampHitToServer(pos);
                    deletedPos = pos;
                }
            }
        }, getString(R.string.are_you_sure_you_want_to_remove_camp)).show();

    }

    private void removeCampHitToServer(int pos) {
        showProgressDialog();
       /* JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, SocketConstant.SOCKEYKEYS.DELETE_ITEM);
            jsonObject.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());
            jsonObject.put(SocketConstant.SOCKEYKEYS.SAVED_ID, list.get(pos).getId());
            deletedPos = pos;
            SocketManager.getInstance().sendData(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        if (isInternetAvailable()) {
            isLoading = true;
            mHomeListViewViewModel.hitBeaconDeleteItemApi(list.get(pos).getId().toString());
        }
        else {
            isLoading = false;
            showToastLong(mActivity.getResources().getString(R.string.no_internet_connection));
        }
    }

    @Override
    public void onItemLongClicked(String s, int pos) {

    }

    /**
     * remove reported camp
     * @param campId
     */
    public void removeReportDeal(int campId) {
        for (int i=0; i<list.size(); i++) {
            if (list.get(i).getCampId() == campId) {
                list.remove(i);
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
        if (list.size() == 0)
            showNoData();
        else
            hideNoData();
    }
}
