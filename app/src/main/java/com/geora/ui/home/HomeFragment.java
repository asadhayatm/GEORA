package com.geora.ui.home;


import android.app.Activity;
import androidx.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.geora.locationlibrary.RCLocation;
import com.geora.locationlibrary.interfaces.LocationsCallback;
import com.geora.model.beaconsdata.BeaconsDataList;
import com.geora.model.beaconsdata.Datum;
import com.geora.socket.SocketCallback;
import com.geora.socket.SocketConstant;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.geora.GeoraClass.cloudCredentials;


// 1 => promo, 2 => fund, 3 => product, 4 => event

/**
 * A simple {@link BaseFragment} subclass.
 */
public class HomeFragment extends BaseFragment implements CardStackListener, BeaconListCallback, RecycleListener, SocketCallback, LocationsCallback {
    @BindView(R.id.card_stack_view)
    CardStackView cardStackView;
    @BindView(R.id.iv_recall)
    ImageView ivRecall;
    @BindView(R.id.tv_recall)
    TextView tvRecall;
    @BindView(R.id.rl_no_item)
    RelativeLayout rlNoItem;
    @BindView(R.id.tv_events)
    TextView tvEvents;
    @BindView(R.id.tv_promotion)
    TextView tvPromotion;
    @BindView(R.id.tv_fundraising)
    TextView tvFundraising;
    @BindView(R.id.tv_no_offers)
    TextView tvNoOffers;
    @BindView(R.id.tv_sales)
    TextView tvSales;

    private Unbinder unbinder;
    private AppCompatActivity mActivity;
    private CardStackAdapter adapter;

    private CardStackLayoutManager manager;
    private ProximityContentManager proximityContentManager;

    private boolean isEvent = false, isPromotions = false, isSales = false, isFundraising = false, isAll = false;
    private String filter = "";
    private String lastItem = "";
    private List<Datum> list = new LinkedList<Datum>();
    private List<Datum> tempList = new LinkedList<Datum>();

    private BroadcastReceiver mReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isAdded()) {
                ArrayList<Object> arr = new ArrayList();
                arr.addAll(Collections.singletonList(intent.getExtras().get("Beaconslist")));
                if (arr.size() > 0) {
                    for (int i = 0; i < ((Object[]) arr.get(0)).length; i++) {
                        String beaconId = ((ProximityZoneContext) ((Object[]) arr.get(0))[i]).getDeviceId();
                        //sendBeaconsToServer(beaconId);
                    }
                }
            }
        }
    };
    /**
     * A {@link HomeViewModel} object to handle all the actions and business logic
     */
    private HomeViewModel mHomeViewModel;

    /**
     * A {@link IHomeHost} object to interact with the host{@link HomeActivity}
     * if any action has to be performed from the host.
     */
    private IHomeHost mHomeHost;
    private RCLocation location;
    private double latitude = 0.0, longitude = 0.0;

    /**
     * This method is used to return the instance of this fragment
     *
     * @return new instance of {@link HomeFragment}
     */
    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        //LocalBroadcastManager.getInstance(mActivity).registerReceiver(mReciever, new IntentFilter("detect_broadcast_beacons"));
    }

    @Override
    public void onStop() {
        //LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(mReciever);
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IHomeHost) {
            mHomeHost = (IHomeHost) context;
        } else
            throw new IllegalStateException("Host must implement IHomeHost");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        mActivity = (AppCompatActivity) getActivity();
        ((HomeActivity) mActivity).setupToolbar(0, "Browse");


        return view;
    }


    private void setupAdapter(List<Datum> list) {
        List<Datum> newList = new LinkedList<Datum>();
        newList.addAll(list);
        adapter = null;
        adapter = new CardStackAdapter(newList, getContext());
        if (manager == null) {
            manager = new CardStackLayoutManager(getContext(), this);
            manager.setStackFrom(StackFrom.Right);
            manager.setOverlayInterpolator(new LinearInterpolator());
        }
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.getAdapter().notifyDataSetChanged();
        if (adapter.getItemCount() > 0) {
            manager.setVisibleCount(adapter.getItemCount());
        }
        adapter.setClickListener(this);

    }

    private void connectWebSocket(SocketCallback callback) {
//        SocketManager.getInstance().setSocketCallbackListener(callback);
    }

    @Override
    public void onResume() {
        super.onResume();
//        connectWebSocket(this);
        if (latitude != 0 && latitude != 0) {
//            sendDataServer();
            checkingForRequirments();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
//        connectWebSocket(null);
//        if (proximityContentManager != null) {
//            proximityContentManager.stop();
//        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupAdapter(list);
        //initializing view model
        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mHomeViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        setObservers();
        //observing log out live data
        initializeLocation();
        SetCategories();
        if (latitude != 0 && latitude != 0) {
            sendDataServer();
            checkingForRequirments();
        }
    }


    /*
     * initializing the location
     *
     * */
    private void initializeLocation() {
        if (location == null) {
            location = new RCLocation();
            location.setActivity((Activity) mActivity);
            location.setCallback(this);
        }
        location.startLocation();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
        if (isAdded()) {

            if (ratio == 1 && adapter.list.size() == 1) {
                if (adapter.list.size() > 0) {
                    if (direction.name().equalsIgnoreCase("Left"))
                        leftSwipe(manager.getTopPosition() - 1);
                    else if (direction.name().equalsIgnoreCase("Right"))
                        rightSwipe(manager.getTopPosition() - 1);

//            list.remove(0);
//                adapter.list.remove(0);
//            list.clear();
//            list.addAll(adapter.list);
                }
//            showToastLong(adapter.list.size() + " ==== " + manager.getTopPosition());
                if (adapter.list.size() == manager.getTopPosition())
                    showNoData();
            }
        }

    }

    @Override
    public void onCardSwiped(Direction direction) {
        if (isAdded()) {

            if (adapter.list.size() > 0) {
                if (direction.name().equalsIgnoreCase("Left"))
                    leftSwipe(manager.getTopPosition() - 1);
                else if (direction.name().equalsIgnoreCase("Right"))
                    rightSwipe(manager.getTopPosition() - 1);

//            list.remove(0);
//            adapter.list.remove(0);
//            list.clear();
//            list.addAll(adapter.list);
            }
//        showToastLong(adapter.list.size() + " ==== " + manager.getTopPosition());
            if (adapter.list.size() == manager.getTopPosition())
                showNoData();
        }
    }


    // socket for right swipe item
    private void rightSwipe(int topPosition) {
//        JSONObject jsonObject = new JSONObject();
        /*try {
            jsonObject.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, "update");
            jsonObject.put(SocketConstant.SOCKEYKEYS.IS_SUBSCRIBED_USER, false);
            jsonObject.put(SocketConstant.SOCKEYKEYS.BEACON_ID, adapter.list.get(topPosition).getBeaconId());
            jsonObject.put(SocketConstant.SOCKEYKEYS.CAMP_ID, adapter.list.get(topPosition).getCampId());
            jsonObject.put(SocketConstant.SOCKEYKEYS.ACTION, "accepted");
            jsonObject.put(SocketConstant.SOCKEYKEYS.SEARCH, "");
            jsonObject.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());
            if (isInternetAvailable())
                SocketManager.getInstance().sendData(jsonObject.toString());
            adapter.list.get(topPosition).setRightSwipe(true);
            if (tempList.size() > 0) tempList.remove(0);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        try {
            HashMap<String, Object> params = new HashMap<>();

            params.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, "update");
            params.put(SocketConstant.SOCKEYKEYS.IS_SUBSCRIBED_USER, false);
            params.put(SocketConstant.SOCKEYKEYS.BEACON_ID, adapter.list.get(topPosition).getBeaconId());
            params.put(SocketConstant.SOCKEYKEYS.CAMP_ID, adapter.list.get(topPosition).getCampId());
            params.put(SocketConstant.SOCKEYKEYS.ACTION, "accepted");
            params.put(SocketConstant.SOCKEYKEYS.SEARCH, "");
            params.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());

            if (isInternetAvailable())
                mHomeViewModel.hitSwipeApi(params);
            else {
                showToastLong(mActivity.getResources().getString(R.string.no_internet_connection));
            }
            adapter.list.get(topPosition).setRightSwipe(true);
            if (tempList.size() > 0) tempList.remove(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //socket for left swipe
    private void leftSwipe(int topPosition) {
        /*JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, "update");
            jsonObject.put(SocketConstant.SOCKEYKEYS.IS_SUBSCRIBED_USER, false);
            jsonObject.put(SocketConstant.SOCKEYKEYS.BEACON_ID, adapter.list.get(topPosition).getBeaconId());
            jsonObject.put(SocketConstant.SOCKEYKEYS.CAMP_ID, adapter.list.get(topPosition).getCampId());
            jsonObject.put(SocketConstant.SOCKEYKEYS.ACTION, "rejected");
            jsonObject.put(SocketConstant.SOCKEYKEYS.SEARCH, "");
            jsonObject.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());
            if (isInternetAvailable())
                SocketManager.getInstance().sendData(jsonObject.toString());
            if (tempList.size() > 0) tempList.remove(0);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        try {
            HashMap<String, Object> params = new HashMap<>();

            params.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, "update");
            params.put(SocketConstant.SOCKEYKEYS.IS_SUBSCRIBED_USER, false);
            params.put(SocketConstant.SOCKEYKEYS.BEACON_ID, adapter.list.get(topPosition).getBeaconId());
            params.put(SocketConstant.SOCKEYKEYS.CAMP_ID, adapter.list.get(topPosition).getCampId());
            params.put(SocketConstant.SOCKEYKEYS.ACTION, "rejected");
            params.put(SocketConstant.SOCKEYKEYS.SEARCH, "");
            params.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());

            if (isInternetAvailable())
                mHomeViewModel.hitSwipeApi(params);
            else {
                showToastLong(mActivity.getResources().getString(R.string.no_internet_connection));
            }
            if (tempList.size() > 0) tempList.remove(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //socket for  recall the last left swipe item
    private void recallLeftIton() {
        String filter = getFilterData();
//        JSONObject jsonObject = new JSONObject();
        /*try {
            jsonObject.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, SocketConstant.SOCKEYKEYS.DIRECT_RECALL);
            jsonObject.put(SocketConstant.SOCKEYKEYS.SEARCH, "");
            jsonObject.put(SocketConstant.SOCKEYKEYS.FILTER, filter);
            jsonObject.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());
            if (isInternetAvailable())
                SocketManager.getInstance().sendData(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        HashMap<String, Object> params = new HashMap<>();

        params.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, SocketConstant.SOCKEYKEYS.DIRECT_RECALL);
        params.put(SocketConstant.SOCKEYKEYS.SEARCH, "");
        params.put(SocketConstant.SOCKEYKEYS.FILTER, filter);
        params.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());

        if (isInternetAvailable())
            mHomeViewModel.hitRecallApi(params);
        else {
            showToastLong(mActivity.getResources().getString(R.string.no_internet_connection));
        }

        tvRecall.setTextColor(mActivity.getResources().getColor(R.color.white__40));
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

    private void startProximityContentManager() {
        if (proximityContentManager == null) {
            proximityContentManager = new ProximityContentManager(mActivity, this, cloudCredentials);
            proximityContentManager.start();
        }
//        showToastLong("Latitude : " + latitude + " ::::: longitude : " + longitude);

    }

    private void checkingForRequirments() {


        RequirementsWizardFactory
                .createEstimoteRequirementsWizard()
                .fulfillRequirements(((Activity) mActivity),
                        () -> {
                            if (isAdded()) {
                                Log.d("app", "requirements fulfilled");
//                                Toast.makeText(mActivity, "requirements fulfilled", Toast.LENGTH_LONG).show();
                                startProximityContentManager();
                            }
                            return null;
                        },
                        requirements -> {
                            if (isAdded()) {
                                Log.e("app", "requirements missing: " + requirements);
//                                Toast.makeText(mActivity, "requirements missing: " + requirements, Toast.LENGTH_LONG).show();
                            }
                            return null;
                        },
                        throwable -> {
                            if (isAdded()) {
                                Log.e("app", "requirements error: " + throwable);
//                                Toast.makeText(mActivity, "requirements error: " + throwable, Toast.LENGTH_LONG).show();
                            }
                            return null;
                        });

    }

    @Override
    public void onBeaconList(Set<? extends ProximityZoneContext> contexts) {
        // wil get the beacon list in my range
        if (isAdded()) {
            sendBeaconsToServer(contexts);
        }
    }

    private void sendBeaconsToServer(Set<? extends ProximityZoneContext> contexts) {
        String filter = getFilterData();

        for (ProximityZoneContext proximityZoneContext : contexts) {
//            JSONObject jsonObject = new JSONObject();
            /*try {
                jsonObject.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, SocketConstant.SOCKEYKEYS.NOTIFICATION);
                jsonObject.put(SocketConstant.SOCKEYKEYS.IS_SUBSCRIBED_USER, false);
                jsonObject.put(SocketConstant.SOCKEYKEYS.BEACON_ID, proximityZoneContext.getDeviceId());
                jsonObject.put(SocketConstant.SOCKEYKEYS.FILTER, filter);
                jsonObject.put(SocketConstant.SOCKEYKEYS.SEARCH, "");
                jsonObject.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());
                jsonObject.put(SocketConstant.SOCKEYKEYS.LAT, latitude);
                jsonObject.put(SocketConstant.SOCKEYKEYS.LNG, longitude);
                if (isInternetAvailable())
                    SocketManager.getInstance().sendData(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

            HashMap<String, Object> params = new HashMap<>();

            params.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, SocketConstant.SOCKEYKEYS.NOTIFICATION);
            params.put(SocketConstant.SOCKEYKEYS.IS_SUBSCRIBED_USER, false);
            params.put(SocketConstant.SOCKEYKEYS.BEACON_ID, proximityZoneContext.getDeviceId());
            params.put(SocketConstant.SOCKEYKEYS.FILTER, filter);
            params.put(SocketConstant.SOCKEYKEYS.SEARCH, "");
            params.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());
            params.put(SocketConstant.SOCKEYKEYS.LAT, latitude);
            params.put(SocketConstant.SOCKEYKEYS.LNG, longitude);

            if (isInternetAvailable())
                mHomeViewModel.hitGetCampApi(params);
            else {
                showToastLong(mActivity.getResources().getString(R.string.no_internet_connection));
            }

        }


    }

    private void sendDataServer() {
        String filter = getFilterData();

        JSONObject jsonObject = new JSONObject();
        /*try {
            jsonObject.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, SocketConstant.SOCKEYKEYS.SEND_NOTIFICATION);
//            jsonObject.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, SocketConstant.SOCKEYKEYS.NOTIFICATION);
            jsonObject.put(SocketConstant.SOCKEYKEYS.BEACON_ID, "");
//            jsonObject.put(SocketConstant.SOCKEYKEYS.BEACON_ID, "184891aa26d5a3431e118cfeeac0463c");
            jsonObject.put(SocketConstant.SOCKEYKEYS.IS_SUBSCRIBED_USER, false);
            jsonObject.put(SocketConstant.SOCKEYKEYS.FILTER, filter);
            jsonObject.put(SocketConstant.SOCKEYKEYS.SEARCH, "");
            jsonObject.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());
            jsonObject.put(SocketConstant.SOCKEYKEYS.LAT, latitude);
            jsonObject.put(SocketConstant.SOCKEYKEYS.LNG, longitude);
            if (isInternetAvailable())
                SocketManager.getInstance().sendData(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }*/


        HashMap<String, Object> params = new HashMap<>();

        params.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, SocketConstant.SOCKEYKEYS.SEND_NOTIFICATION);
//        params.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, SocketConstant.SOCKEYKEYS.NOTIFICATION);
        params.put(SocketConstant.SOCKEYKEYS.BEACON_ID, "");
//        params.put(SocketConstant.SOCKEYKEYS.BEACON_ID, "c2a343018ed4c5805a1451a171b9cf1e");
        params.put(SocketConstant.SOCKEYKEYS.IS_SUBSCRIBED_USER, false);
        params.put(SocketConstant.SOCKEYKEYS.FILTER, filter);
        params.put(SocketConstant.SOCKEYKEYS.SEARCH, "");
        params.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());
        params.put(SocketConstant.SOCKEYKEYS.LAT, latitude);
        params.put(SocketConstant.SOCKEYKEYS.LNG, longitude);

        if (isInternetAvailable()) {
            mHomeViewModel.hitSavedCampApi(params);
//            mHomeViewModel.hitGetCampApi(params);
        } else {
            showToastLong(mActivity.getResources().getString(R.string.no_internet_connection));
        }
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
    }

    @Override
    public void onMessage(final String msg) {
        if (isAdded()) {
            final BeaconsDataList beaconsDataList = new Gson().fromJson(msg, BeaconsDataList.class);
            if (AppUtils.checkUserValid(getActivity(), beaconsDataList.getCode(), beaconsDataList.getMessage())) {
                if (beaconsDataList.getCode() == 423 || beaconsDataList.getCode() == 403) {
                    showToastShort(beaconsDataList.getMessage());
                    logout(mActivity);
                } else {
                    if (beaconsDataList.getActionType().equalsIgnoreCase(SocketConstant.SOCKEYKEYS.NOTIFICATION)
                            || beaconsDataList.getActionType().equalsIgnoreCase(SocketConstant.SOCKEYKEYS.SEND_NOTIFICATION)) {
                        addCampDatToList(beaconsDataList.getData());
                    } else if (beaconsDataList.getActionType().equalsIgnoreCase(SocketConstant.SOCKEYKEYS.DIRECT_RECALL)) {
                        addRecallItemToList(beaconsDataList.getData());
                    }
                }
            }
        }
    }


    private void setObservers() {
        mHomeViewModel.getLogOutLiveData().observe(this, successResponse -> {
            if (isAdded()) {
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
        mHomeViewModel.getCampLiveData().observe(this, beaconsDataList -> {
            if (isAdded()) {
                hideProgressDialog();
                if (AppUtils.checkUserValid(getActivity(), beaconsDataList.getCode(), beaconsDataList.getMessage())) {
                    if (beaconsDataList.getCode() == 423 || beaconsDataList.getCode() == 403) {
                        showToastShort(beaconsDataList.getMessage());
                        logout(mActivity);
                    } else {
                        addCampDatToList(beaconsDataList.getData());
                    }
                }
            }
        });
        mHomeViewModel.getRecallLiveData().observe(this, beaconsDataList -> {
            if (isAdded()) {
                hideProgressDialog();
                if (AppUtils.checkUserValid(getActivity(), beaconsDataList.getCode(), beaconsDataList.getMessage())) {
                    if (beaconsDataList.getCode() == 423 || beaconsDataList.getCode() == 403) {
                        showToastShort(beaconsDataList.getMessage());
                        logout(mActivity);
                    } else {
                        if (beaconsDataList.getData().size() > 0) {
                            addRecallItemToList(beaconsDataList.getData());
                        }
                    }
                }
            }
        });
        mHomeViewModel.getSwipeLiveData().observe(this, recallResponse -> {
            if (isAdded()) {
                hideProgressDialog();
                if (recallResponse != null) {
                    if (AppUtils.checkUserValid(getActivity(), recallResponse.getCODE(), recallResponse.getMESSAGE())) {
                        //host send to login
                        Log.d("Swipe:::::: ", recallResponse.getMESSAGE());
                    }
                }
            }
        });
    }

    private void addCampDatToList(final List<Datum> data) {
        /*final List<Datum> newList = new LinkedList<>();
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
        ((Activity) mActivity).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addDataToAdpterAndNotify(data);
            }
        });*/
        ((Activity) mActivity).runOnUiThread(() -> {
            if (isAdded()) {
                if (data != null && data.size() > 0)
                    addDataToAdpterAndNotify(data, SocketConstant.SOCKEYKEYS.NOTIFICATION);
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

            ((Activity) mActivity).runOnUiThread(() -> {
                if (isAdded()) {
                    addDataToAdpterAndNotify(newList, SocketConstant.SOCKEYKEYS.DIRECT_RECALL);
                }
            });
        }
    }

    private void addDataToAdpterAndNotify(List<Datum> newList, String type) {
        if (adapter != null) {
            adapter.list.clear();
            adapter.notifyDataSetChanged();
            if (newList.size() == 0)
                showNoData();
            else
                hideNoData();
            List<Datum> newDataList = new LinkedList<>();
            for (int i = 0; i < newList.size(); i++) {
                Datum datum = newList.get(i);
                if (tempList.size() > 0) {
                    boolean ifExist = false;
                    for (Datum beaconData : tempList) {
                        if (beaconData.getCampId().equals(datum.getCampId())) {
                            ifExist = true;
                            break;
                        }
                    }
                    if (ifExist == false) {
                        newDataList.add(newList.get(i));
                        if (type == SocketConstant.SOCKEYKEYS.DIRECT_RECALL)
                            tempList.add(0, datum);
                        else
                            tempList.add(datum);

                    }
                } else {
                    newDataList.add(datum);
                    if (type == SocketConstant.SOCKEYKEYS.DIRECT_RECALL)
                        tempList.add(0, datum);
                    else
                        tempList.add(datum);
                }
            }
            //list.addAll(newList);
            list.clear();
            list.addAll(tempList);
            adapter.list.addAll(tempList);
            setupAdapter(tempList);
        }
    }

    private boolean containsData(Datum datum) {
        for (Datum beaconData : list) {
            if (beaconData.getCampId().equals(datum.getCampId()))
                return true;
        }
        return false;
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {


    }

    @Override
    public void onError(Exception ex) {
        Log.i("", "");

    }

    @Override
    public void onItemClicked(View view, String s, int pos) {
        if (isAdded()) {
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
    }

    private void onButtonClicked(int pos) {
        if (list.get(pos).getCampType() == AppConstantClass.CAMPTYPE.FUNDRAISING)
            OpenDonationBox(pos);
        else if (list.get(pos).getCampType() == AppConstantClass.CAMPTYPE.SALES) {

        }

    }

    private void OpenDonationBox(final int pos) {
        new DialogForDonateAmount(mActivity, new DialogCallback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSubmit(View view, String result, int s) {
                if (isAdded()) {
                    if (!result.isEmpty())
                        moveToCardListActivity(pos, result);
                    else
                        showSnackBar(((HomeActivity) mActivity).getParentLayout(), "Add amount to donate", true);
                }
            }
        }).show();
    }

    private void moveToCardListActivity(int pos, String amount) {
        Intent intent = new Intent(mActivity, CardListActivity.class);
        intent.putExtra(AppConstantClass.APIConstant.AMOUNT, amount);
        startActivity(intent);
    }

    private void onCardItemClick(int pos) {
        Bundle bundle = new Bundle();
        bundle.putString(SocketConstant.SOCKEYKEYS.BEACON_ID, list.get(pos).getBeaconId());
        bundle.putString(SocketConstant.SOCKEYKEYS.CAMP_ID, list.get(pos).getCampId().toString());
        bundle.putString(SocketConstant.SOCKEYKEYS.CAMP_TYPE, list.get(pos).getCampType().toString());
        bundle.putString(SocketConstant.SOCKEYKEYS.FILTER, getFilterData());
        bundle.putString(SocketConstant.SOCKEYKEYS.BEACON_ID, list.get(pos).getBeaconId());
        ActivityUtils.startDetailsActivity(mActivity, bundle);
    }


    @Override
    public void onItemLongClicked(String s, int pos) {

    }

    @Override
    public void setLocation(Location mCurrentLocation) {
        if (isAdded()) {
            location.disconnect();
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();
            if (latitude != 0 && latitude != 0) {
                sendDataServer();
                checkingForRequirments();
            }
        }
    }

    @Override
    public void setAddress(String result) {

    }

    @Override
    public void setLatAndLong(Address location) {

    }

    @Override
    public void disconnect() {

    }

    /**
     * remove reported camp
     * @param campId
     */
    public void removeReportDeal(int campId) {
        for (int i=0; i<adapter.list.size(); i++) {
            if (adapter.list.get(i).getCampId() == campId) {
                adapter.list.remove(i);
                break;
            }
        }
        for (int i=0; i<tempList.size(); i++) {
            if (tempList.get(i).getCampId() == campId) {
                tempList.remove(i);
                break;
            }
        }
        for (int i=0; i<list.size(); i++) {
            if (list.get(i).getCampId() == campId) {
                list.remove(i);
                break;
            }
        }
        adapter.notifyDataSetChanged();
        if (adapter.list.size() == manager.getTopPosition()) {
            showNoData();
        }
    }

    /**
     * This interface is used to interact with the host {@link HomeActivity}
     */
    public interface IHomeHost {

        void openChangePasswordFragment();

        void logOutSuccess();

        void drawerOpenAClosed();
    }

}
