/*
package com.geora.ui.home;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.proximity_sdk.api.ProximityZoneContext;
import com.geora.R;
import com.geora.base.BaseFragment;
import com.geora.customviews.DialogForDonateAmount;
import com.geora.data.DataManager;
import com.geora.data.constants.AppConstantClass;
import com.geora.estimote.ProximityContentManager;
import com.geora.listeners.BeaconListCallback;
import com.geora.listeners.DialogCallback;
import com.geora.listeners.RecycleListener;
import com.geora.model.beaconsdata.BeaconsDataList;
import com.geora.model.beaconsdata.Datum;
import com.geora.model.commonresponse.CommonResponse;
import com.geora.socket.SocketCallback;
import com.geora.socket.SocketConstant;
import com.geora.socket.SocketManager;
import com.geora.ui.cardlist.CardListActivity;
import com.geora.util.ActivityUtils;
import com.geora.util.AppUtils;
import com.google.gson.Gson;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

import static com.geora.GeoraClass.cloudCredentials;

public class HomeFragmentTinderView extends BaseFragment implements CardStackListener, BeaconListCallback, RecycleListener, SocketCallback {


    @BindView(R.id.card_stack_view)
    CardStackView cardStackView;
    @BindView(R.id.tv_recall)
    TextView tvRecall;
    @BindView(R.id.iv_recall)
    ImageView ivRecall;
    @BindView(R.id.tv_events)
    TextView tvEvents;
    @BindView(R.id.tv_sales)
    TextView tvSales;
    @BindView(R.id.tv_promotion)
    TextView tvPromotion;
    @BindView(R.id.tv_fundraising)
    TextView tvFundraising;
    @BindView(R.id.tv_no_offers)
    TextView tvNoOffers;
    @BindView(R.id.rl_no_item)
    RelativeLayout rlNoItem;
    private Unbinder unbinder;
    private Context mContext;
    private CardStackAdapter adapter;

    private CardStackLayoutManager manager;
    private ProximityContentManager proximityContentManager;
    private List<Datum> list = new LinkedList<Datum>();

    private boolean isEvent = false, isPromotions = false, isSales = false, isFundraising = false, isAll = false;


    */
/**
     * A {@link HomeViewModel} object to handle all the actions and business logic
     *//*

    private HomeViewModel mHomeViewModel;

    */
/**
     * A {@link HomeFragmentTinderView.IHomeHost} object to interact with the host{@link HomeActivity}
     * if any action has to be performed from the host.
     *//*

    private HomeFragmentTinderView.IHomeHost mHomeHost;

    */
/**
     * This method is used to return the instance of this fragment
     *
     * @return new instance of {@link HomeFragmentTinderView}
     *//*

    public static HomeFragmentTinderView getInstance() {
        return new HomeFragmentTinderView();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragmentTinderView.IHomeHost) {
            mHomeHost = (HomeFragmentTinderView.IHomeHost) context;
        } else
            throw new IllegalStateException("Host must implement IHomeHost");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getContext();
        ((HomeActivity) mContext).setupToolbar(0, "Browse");

        return view;
    }

    private void connectWebSocket(SocketCallback callback) {
        SocketManager.getInstance().setSocketCallbackListener(callback);
    }

    @Override
    public void onResume() {
        super.onResume();
        connectWebSocket(this);
        checkingForRequirments();
    }

    @Override
    public void onPause() {
        super.onPause();
        connectWebSocket(null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupAdapter(list);
        //initializing view model
        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mHomeViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        //observing log out live data
        mHomeViewModel.getLogOutLiveData().observe(this, new Observer<CommonResponse>() {
            @Override
            public void onChanged(@Nullable CommonResponse successResponse) {
                hideProgressDialog();
                if (successResponse != null) {
                    if (AppUtils.checkUserValid(getActivity(), successResponse.getCODE(), successResponse.getMESSAGE())) {
                        //host send to login
                        showToastLong(getString(R.string.log_out_success));
                        mHomeHost.logOutSuccess();
                    }
                }
            }
        });
        SetCategories();
    }


    private void setupAdapter(List<Datum> list) {
        List<Datum> newList = new LinkedList<Datum>();
        newList.addAll(list);
        adapter = null;
        adapter = new CardStackAdapter(newList, getContext());
        if (manager == null) {
            manager = new CardStackLayoutManager(getContext(), this);
            manager.setStackFrom(StackFrom.Right);
        }
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.getAdapter().notifyDataSetChanged();
        if (adapter.getItemCount() > 0) {
            manager.setVisibleCount(adapter.getItemCount());
        }
        adapter.setClickListener(this);

    }

    private void SetCategories() {
        if (mHomeViewModel.getEventStatus().equals("1")) {
            isEvent = true;
            setSelectedOrUnselectedView(true, AppConstantClass.CATEGORY.EVENT);
        } else {
            setSelectedOrUnselectedView(false, AppConstantClass.CATEGORY.EVENT);
            isEvent = false;
        }

        if (mHomeViewModel.getSalesStatus().equals("1")) {
            isSales = true;
            setSelectedOrUnselectedView(true, AppConstantClass.CATEGORY.SALES);
        } else {
            setSelectedOrUnselectedView(false, AppConstantClass.CATEGORY.SALES);
            isSales = false;
        }

        if (mHomeViewModel.getPromotionStatus().equals("1")) {
            setSelectedOrUnselectedView(true, AppConstantClass.CATEGORY.PROMOTION);
            isPromotions = true;
        } else {
            setSelectedOrUnselectedView(false, AppConstantClass.CATEGORY.PROMOTION);
            isPromotions = false;
        }

        if (mHomeViewModel.getFundRaisingStatus().equals("1")) {
            setSelectedOrUnselectedView(true, AppConstantClass.CATEGORY.FUNDRAISING);
            isFundraising = true;
        } else {
            setSelectedOrUnselectedView(false, AppConstantClass.CATEGORY.FUNDRAISING);
            isFundraising = false;
        }
        if (mHomeViewModel.getAllStatus().equals("1")) {
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

    private void setUnSelectedEvent(TextView tvEvents) {
        tvEvents.setTextColor(getResources().getColor(R.color.warm_grey));
        tvEvents.setBackground(getResources().getDrawable(R.drawable.warm_gray_border_round_rect));

    }

    private void setSelectedEvent(TextView tvcategory) {
        tvcategory.setTextColor(getResources().getColor(R.color.butterscotch));
        tvcategory.setBackground(getResources().getDrawable(R.drawable.yellow_border_round_rect));
    }


    @OnClick({R.id.tv_events, R.id.tv_sales, R.id.tv_promotion, R.id.tv_fundraising, R.id.iv_recall})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_recall:
                recallLeftIton();
                break;
            case R.id.tv_events:
                isAll = false;
                isEvent = !isEvent;
                setSelectedOrUnselectedView(isEvent, AppConstantClass.CATEGORY.EVENT);
                break;
            case R.id.tv_sales:
                isAll = false;
                isSales = !isSales;
                setSelectedOrUnselectedView(isSales, AppConstantClass.CATEGORY.SALES);
                break;
            case R.id.tv_promotion:
                isAll = false;
                isPromotions = !isPromotions;
                setSelectedOrUnselectedView(isPromotions, AppConstantClass.CATEGORY.PROMOTION);
                break;
            case R.id.tv_fundraising:
                isAll = false;
                isFundraising = !isFundraising;
                setSelectedOrUnselectedView(isFundraising, AppConstantClass.CATEGORY.FUNDRAISING);
                break;
        }
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

        if (ratio == 1 && adapter.list.size() == 1) {
            if (adapter.list.size() > 0) {
                if (direction.name().equals("Left"))
                    leftSwipe();
                else
                    rightSwipe();

//            list.remove(0);
                adapter.list.remove(0);
//            list.clear();
//            list.addAll(adapter.list);
            }
            if (adapter.list.size() == 0)
                showNoData();
        }


    }

    @Override
    public void onCardSwiped(Direction direction) {
        if (adapter.list.size() > 0) {
            if (direction.name().equals("Left"))
                leftSwipe();
            else
                rightSwipe();

//            list.remove(0);
            adapter.list.remove(0);
//            list.clear();
//            list.addAll(adapter.list);
        }
        if (adapter.list.size() == 0)
            showNoData();
    }


    // socket for right swipe item
    private void rightSwipe() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, "update");
            jsonObject.put(SocketConstant.SOCKEYKEYS.IS_SUBSCRIBED_USER, false);
            jsonObject.put(SocketConstant.SOCKEYKEYS.BEACON_ID, adapter.list.get(0).getBeaconId());
            jsonObject.put(SocketConstant.SOCKEYKEYS.CAMP_ID, adapter.list.get(0).getCampId());
            jsonObject.put(SocketConstant.SOCKEYKEYS.ACTION, "accepted");
            jsonObject.put(SocketConstant.SOCKEYKEYS.SEARCH, "");
            jsonObject.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());
            if (isInternetAvailable())
                SocketManager.getInstance().sendData(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    //socket for left swipe
    private void leftSwipe() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, "update");
            jsonObject.put(SocketConstant.SOCKEYKEYS.IS_SUBSCRIBED_USER, false);
            jsonObject.put(SocketConstant.SOCKEYKEYS.BEACON_ID, adapter.list.get(0).getBeaconId());
            jsonObject.put(SocketConstant.SOCKEYKEYS.CAMP_ID, adapter.list.get(0).getCampId());
            jsonObject.put(SocketConstant.SOCKEYKEYS.ACTION, "rejected");
            jsonObject.put(SocketConstant.SOCKEYKEYS.SEARCH, "");
            jsonObject.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());
            if (isInternetAvailable())
                SocketManager.getInstance().sendData(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    //socket for  recall the last left swipe item
    private void recallLeftIton() {
        String filter = getFilterData();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, SocketConstant.SOCKEYKEYS.DIRECT_RECALL);
            jsonObject.put(SocketConstant.SOCKEYKEYS.SEARCH, "");
            jsonObject.put(SocketConstant.SOCKEYKEYS.FILTER, filter);
            jsonObject.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());
            if (isInternetAvailable())
                SocketManager.getInstance().sendData(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tvRecall.setTextColor(mContext.getResources().getColor(R.color.white__40));
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

    private void showNoData() {
        cardStackView.setVisibility(View.GONE);
        rlNoItem.setVisibility(View.VISIBLE);
    }

    private void startProximityContentManager() {
        if (proximityContentManager == null) {
            proximityContentManager = new ProximityContentManager(mContext, this, cloudCredentials);
            proximityContentManager.start();
        }
    }

    private void checkingForRequirments() {


        RequirementsWizardFactory
                .createEstimoteRequirementsWizard()
                .fulfillRequirements(((Activity) mContext),
                        new Function0<Unit>() {
                            @Override
                            public Unit invoke() {
                                Log.d("app", "requirements fulfilled");
                                startProximityContentManager();
                                return null;
                            }
                        },
                        new Function1<List<? extends Requirement>, Unit>() {
                            @Override
                            public Unit invoke(List<? extends Requirement> requirements) {
                                Log.e("app", "requirements missing: " + requirements);
                                return null;
                            }
                        },
                        new Function1<Throwable, Unit>() {
                            @Override
                            public Unit invoke(Throwable throwable) {
                                Log.e("app", "requirements error: " + throwable);
                                return null;
                            }
                        });

    }

    @Override
    public void onBeaconList(Set<? extends ProximityZoneContext> contexts) {
        // wil get the beacon list in my range
        sendBeaconsToServer(contexts);
    }

    private void sendBeaconsToServer(Set<? extends ProximityZoneContext> contexts) {
        String filter = getFilterData();

        for(ProximityZoneContext proximityZoneContext : contexts){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, SocketConstant.SOCKEYKEYS.NOTIFICATION);
                jsonObject.put(SocketConstant.SOCKEYKEYS.IS_SUBSCRIBED_USER, false);
                jsonObject.put(SocketConstant.SOCKEYKEYS.BEACON_ID, proximityZoneContext.getDeviceId());
                jsonObject.put(SocketConstant.SOCKEYKEYS.FILTER, filter);
                jsonObject.put(SocketConstant.SOCKEYKEYS.SEARCH, "");
                jsonObject.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());
                if (isInternetAvailable())
                    SocketManager.getInstance().sendData(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onMessage(final String msg) {
        final BeaconsDataList beaconsDataList = new Gson().fromJson(msg, BeaconsDataList.class);
        if (beaconsDataList.getCode() == 423 || beaconsDataList.getCode() == 403) {
                showToastShort(beaconsDataList.getMessage());
                logout(mContext);
         } else {
            if (beaconsDataList.getActionType().equalsIgnoreCase(SocketConstant.SOCKEYKEYS.NOTIFICATION)) {
                addCampDatToList(beaconsDataList.getData());
            } else if (beaconsDataList.getActionType().equalsIgnoreCase(SocketConstant.SOCKEYKEYS.DIRECT_RECALL)) {
                addRecallItemToList(beaconsDataList.getData());
            }
        }
    }


    private void addCampDatToList(final List<Datum> data) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (data != null && data.size() > 0)
                    addDataToAdpterAndNotify(data);
            }
        });
    }

    private void hideNoData() {
        cardStackView.setVisibility(View.VISIBLE);
        rlNoItem.setVisibility(View.GONE);
    }

    private void addRecallItemToList(List<Datum> data) {
        final List<Datum> newList = new LinkedList<>();
        newList.addAll(data);
        newList.add(0, data.get(0));
        if (newList.size() > 0) {

            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addDataToAdpterAndNotify(newList);
                }
            });
        }
    }

    private void addDataToAdpterAndNotify(List<Datum> newList) {
        if (adapter != null) {
            if (newList.size() == 0)
                showNoData();
            else
                hideNoData();
            List<Datum> newDataList = new LinkedList<>();
            for (int i = 0; i < newList.size(); i++) {
                Datum datum = newList.get(i);
                if (adapter.list.size() > 0) {
                    boolean ifExist = false;
                    for (Datum beaconData : adapter.list) {
                        if (beaconData.getCampId().equals(datum.getCampId())) {
                            ifExist = true;
                            break;
                        }
                    }
                    if (ifExist == false) {
                        newDataList.add(newList.get(i));
                        adapter.list.add(datum);
                    }
                } else {
                    newDataList.add(datum);
                    adapter.list.add(datum);
                }
            }
            //list.addAll(newList);
            list.addAll(newDataList);
            adapter.notifyDataSetChanged();
        }
    }

    private boolean containsData(Datum datum) {
        for (Datum beaconData : list) {
            if (beaconData.getCampId() == datum.getCampId())
                return true;
        }
        return false;
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {


    }

    @Override
    public void onFailure(Exception ex) {
        Log.i("", "");

    }

    @Override
    public void onItemClicked(View view, String s, int pos) {
        if (view.getId() == R.id.rl_main)
            if (list.size() > pos) {
                onCardItemClick(pos);
            } else if (view.getId() == R.id.tv_button) {
                if (adapter.list.size() > pos) {
                    onCardItemClick(pos);
//            onButtonClicked(pos);
                }
            }
    }

    private void onButtonClicked(int pos) {
        if (list.get(pos).getCampType() == AppConstantClass.CAMPTYPE.FUNDRAISING)
            OpenDonationBox(pos);
        else if (list.get(pos).getCampType() == AppConstantClass.CAMPTYPE.SALES) {

        }

    }

    private void OpenDonationBox(final int pos) {
        new DialogForDonateAmount(mContext, new DialogCallback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSubmit(View view, String result, int s) {
                if (!result.isEmpty())
                    moveToCardListActivity(pos, result);
                else
                    showSnackBar(((HomeActivity) mContext).getParentLayout(), "Add amount to donate", true);
            }
        }).show();
    }

    private void moveToCardListActivity(int pos, String amount) {
        Intent intent = new Intent(mContext, CardListActivity.class);
        intent.putExtra(AppConstantClass.APIConstant.AMOUNT, amount);
        startActivity(intent);
    }

    private void onCardItemClick(int pos) {
        Bundle bundle = new Bundle();
        bundle.putString(SocketConstant.SOCKEYKEYS.BEACON_ID, list.get(pos).getBeaconId());
        bundle.putString(SocketConstant.SOCKEYKEYS.CAMP_ID, list.get(pos).getCampId().toString());
        bundle.putString(SocketConstant.SOCKEYKEYS.CAMP_TYPE, list.get(pos).getCampType().toString());
        bundle.putString(SocketConstant.SOCKEYKEYS.FILTER, getFilterData());
        ActivityUtils.startDetailsActivity(mContext, bundle);
    }

    */
/**
     * This interface is used to interact with the host {@link HomeActivity}
     *//*

    public interface IHomeHost {

        void openChangePasswordFragment();

        void logOutSuccess();

        void drawerOpenAClosed();
    }



    @Override
    public void onItemLongClicked(String s, int pos) {

    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
*/
