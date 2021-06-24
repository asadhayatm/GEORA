package com.geora.ui.home;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseActivity;
import com.geora.constants.AppConstants;
import com.geora.customviews.DialogForConfirmation;
import com.geora.data.DataManager;
import com.geora.listeners.RecycleListener;
import com.geora.model.beaconsavedlist.BeaconSaveListModel;
import com.geora.model.beaconsavedlist.Datum;
import com.geora.socket.SocketCallback;
import com.geora.socket.SocketConstant;
import com.geora.ui.home.homelistview.HomeListViewAdapter;
import com.geora.ui.home.homelistview.HomeListViewViewModel;
import com.geora.util.ActivityUtils;
import com.geora.util.AppUtils;
import com.google.gson.Gson;

import org.java_websocket.handshake.ServerHandshake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SavedItemSearchActivity extends BaseActivity implements RecycleListener, SocketCallback {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rv_searched)
    RecyclerView rvSearched;
    @BindView(R.id.main)
    RelativeLayout main;
    @BindView(R.id.rl_no_item)
    RelativeLayout rlNoItem;
    @BindView(R.id.iv_no_data)
    ImageView ivNoData;
    @BindView(R.id.tv_title_card)
    TextView tvTitleCard;
    @BindView(R.id.tv_des)
    TextView tvDes;

    private Unbinder unbinder;
    private HomeListViewAdapter mAdapter;
    private List<Datum> list;
    private int deletedPos = -1;
    private HomeListViewViewModel mHomeListViewViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        list = new ArrayList<>();
        mHomeListViewViewModel = ViewModelProviders.of(this).get(HomeListViewViewModel.class);
        mHomeListViewViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        setObservers();
        setNoData();
        setUpAdapter();
    }

    private void setNoData() {
        ivNoData.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_offer_browse_graphic));
        tvTitleCard.setText(getResources().getString(R.string.no_offers));
        tvDes.setText(getResources().getString(R.string.seems_like_there_are_no_offers_available_in_this_area_right_now_click_on_map_view_to_see_offers_in_other_places));
    }


    @Override
    protected int getResourceId() {
        return R.layout.activity_saved_item_search;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        SocketManager.getInstance().setSocketCallbackListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        SocketManager.getInstance().setSocketCallbackListener(null);

    }

    @OnClick({R.id.iv_back, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishAfterTransition();
                break;
            case R.id.iv_search:
                if (isInternetAvailable())
                    searchButtonClicked();
                else
                    showSnackBar(main, getString(R.string.no_internet_connection), true);
                break;
        }
    }

    private void searchButtonClicked() {
        showProgressDialog();
        /*JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, SocketConstant.SOCKEYKEYS.SAVED);
            jsonObject.put(SocketConstant.SOCKEYKEYS.FILTER, "1,2,3,4");
            jsonObject.put(SocketConstant.SOCKEYKEYS.SEARCH, etSearch.getText().toString());
            jsonObject.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());
            SocketManager.getInstance().sendData(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        HashMap<String, Object> params = new HashMap<>();

        params.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, SocketConstant.SOCKEYKEYS.SAVED);
        params.put(SocketConstant.SOCKEYKEYS.FILTER, "1,2,3,4");
        params.put(SocketConstant.SOCKEYKEYS.SEARCH, etSearch.getText().toString());
        params.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());

        if (isInternetAvailable())
            mHomeListViewViewModel.hitBeaconSaveDataApi(params);
        else {
            showToastLong(getResources().getString(R.string.no_internet_connection));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }

    //set adapter for home list view
    private void setUpAdapter() {

        mAdapter = new HomeListViewAdapter(list, this, 0);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rvSearched.getContext(), LinearLayoutManager.VERTICAL, false);
        rvSearched.setLayoutManager(linearLayoutManager);
        rvSearched.setAdapter(mAdapter);
        mAdapter.setClickListener(this);
    }

    @Override
    public void onItemClicked(View view, String s, int pos) {
        switch (view.getId()) {
            case R.id.iv_cross:
                if (isInternetAvailable())
                    deleteItemFromSAvedList(pos);
                else
                    showSnackBar(main,getString(R.string.no_internet_connection),true);
//                list.remove(pos);
//                mAdapter.notifyDataSetChanged();
                break;
            case R.id.main:
                moveToDetailActivity(pos);
                break;
        }
    }

    private void deleteItemFromSAvedList(int pos) {

        new DialogForConfirmation(this, (view, result, s) -> {
            hideProgressDialog();
            if (result.equalsIgnoreCase("yes")) {
                removeCampHitToServer(pos);
                deletedPos = pos;
            }
        }, getString(R.string.are_you_sure_you_want_to_remove_camp)).show();

    }
    private void removeCampHitToServer(int pos) {

        showProgressDialog();
        /*JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, SocketConstant.SOCKEYKEYS.DELETE_ITEM);
            jsonObject.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());
            jsonObject.put(SocketConstant.SOCKEYKEYS.SAVED_ID, list.get(pos).getId());
            deletedPos = pos;
            SocketManager.getInstance().sendData(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        if (isInternetAvailable())
            mHomeListViewViewModel.hitBeaconDeleteItemApi(list.get(pos).getId().toString());
        else {
            showToastLong(getResources().getString(R.string.no_internet_connection));
        }
    }

    private void moveToDetailActivity(int pos) {
        Bundle bundle = new Bundle();
        bundle.putString(SocketConstant.SOCKEYKEYS.CAMP_ID, mAdapter.list.get(pos).getCampId().toString());
        bundle.putString(SocketConstant.SOCKEYKEYS.CAMP_TYPE, mAdapter.list.get(pos).getCampType().toString());
        bundle.putString(SocketConstant.SOCKEYKEYS.FILTER, "1,2,3,4");
        bundle.putString(SocketConstant.SOCKEYKEYS.BEACON_ID, mAdapter.list.get(pos).getBeaconId());
        ActivityUtils.startDetailsActivity(this, bundle);
    }

    @Override
    public void onItemLongClicked(String s, int pos) {

    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override
    public void onMessage(String msg) {

        final BeaconSaveListModel beaconSaveListModel = new Gson().fromJson(msg, BeaconSaveListModel.class);
        if (AppUtils.checkUserValid(this, beaconSaveListModel.getCode(), beaconSaveListModel.getMessage())) {
            if (beaconSaveListModel.getCode() == 423 || beaconSaveListModel.getCode() == 403) {
                showToastShort(beaconSaveListModel.getMessage());
                logout(this);
            } else {
                if (beaconSaveListModel.getActionType().equalsIgnoreCase(SocketConstant.SOCKEYKEYS.SAVED))
                    addDataToMainList(beaconSaveListModel.getData());
                else if (beaconSaveListModel.getActionType().equalsIgnoreCase(SocketConstant.SOCKEYKEYS.DELETE_ITEM))
                    refershList();
            }
        }
    }


    private void setObservers() {
        mHomeListViewViewModel.getBeaconSaveListLiveData().observe(this, beaconSaveListModel -> {
            hideProgressDialog();
            if (AppUtils.checkUserValid(SavedItemSearchActivity.this, beaconSaveListModel.getCode(), beaconSaveListModel.getMessage())) {
                if (beaconSaveListModel.getCode() == 423 || beaconSaveListModel.getCode() == 403) {
                    showToastShort(beaconSaveListModel.getMessage());
                    logout(this);
                } else {
                    addDataToMainList(beaconSaveListModel.getData());
                }
            }
        });
        mHomeListViewViewModel.getBeaconDeleteItemLiveData().observe(this, commonResponse -> {
            hideProgressDialog();
            if (AppUtils.checkUserValid(SavedItemSearchActivity.this, commonResponse.getCODE(), commonResponse.getMESSAGE())) {
                if (commonResponse.getCODE() == 423 || commonResponse.getCODE() == 403) {
                    showToastShort(commonResponse.getMESSAGE());
                    logout(this);
                } else {
                    refershList();
                }
            }
        });

    }

    private void refershList() {
        runOnUiThread(() -> {
            hideProgressDialog();
            removeData();
        });


    }

    private void removeData() {
        if (deletedPos != -1) {
            list.remove(deletedPos);
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
        list.clear();
        newList.addAll(list);

        for (int i = 0; i < data.size(); i++) {
            Datum datum = data.get(i);
            for (Datum beaconData : newList) {
                if (!beaconData.getCampId().equals(datum.getCampId()))
                    newList.add(datum);
            }
        }
        if (newList.size() == 0 && data.size() != 0)
            newList.addAll(data);
        runOnUiThread(() -> {
            hideProgressDialog();
            addDataToAdpterAndNotify(newList);
        });
    }

    private void addDataToAdpterAndNotify(List<Datum> newList) {
        if (mAdapter != null) {
            if (newList.size() == 0)
                showNoData();
            else
                hideNoData();
            rvSearched.getRecycledViewPool().clear();
            list.clear();
            list.addAll(newList);
//            mAdapter.list.addAll(newList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void hideNoData() {
        rvSearched.setVisibility(View.VISIBLE);
        rlNoItem.setVisibility(View.GONE);
    }

    private void showNoData() {
        rlNoItem.setVisibility(View.VISIBLE);
        rvSearched.setVisibility(View.GONE);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        runOnUiThread(() -> {
            hideProgressDialog();
            if (deletedPos != -1)
                deletedPos = -1;

        });
    }

    @Override
    public void onError(Exception ex) {
        runOnUiThread(() -> {
            hideProgressDialog();
            if (deletedPos != -1)
                deletedPos = -1;
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AppConstants.REQUEST_DETAIL_SCREEN && resultCode == RESULT_OK && data != null && data.getExtras() != null) {
            int campId = data.getExtras().getInt(SocketConstant.SOCKEYKEYS.CAMP_ID, 0);
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
}
