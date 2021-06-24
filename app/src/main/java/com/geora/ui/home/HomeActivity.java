package com.geora.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.proximity_sdk.api.ProximityZoneContext;
import com.geora.R;
import com.geora.base.BaseActivity;
import com.geora.base.BaseFragment;
import com.geora.constants.AppConstants;
import com.geora.data.DataManager;
import com.geora.data.constants.AppConstantClass;
import com.geora.estimote.ProximityContentManager;
import com.geora.listeners.BeaconListCallback;
import com.geora.socket.SocketConstant;
import com.geora.ui.about.AboutFragment;
import com.geora.ui.businessuser.BusinessUserFragment;
import com.geora.ui.changepassword.ChangePasswordActivity;
import com.geora.ui.editprofile.EditProfileActivity;
import com.geora.ui.home.homelistview.HomeListViewFragment;
import com.geora.ui.home.map.MapViewFragment;
import com.geora.ui.home.profile.ProfileFragment;
import com.geora.ui.myactivities.MyActivityFragment;
import com.geora.ui.notification.NotificationActivity;
import com.geora.ui.settings.SettingsFragment;
import com.geora.ui.staticpages.StaticPagesActivity;
import com.geora.util.ActivityUtils;
import com.geora.util.AppUtils;
import com.geora.util.ImageUtils;
import com.google.android.gms.maps.model.LatLng;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

import static com.geora.GeoraClass.cloudCredentials;

public class HomeActivity extends BaseActivity implements HomeFragment.IHomeHost, ProfileFragment.IProfileHost, SettingsFragment.ISettingsHost,
        AboutFragment.IAboutHost, BeaconListCallback {

    @BindView(R.id.drawer)
    DuoDrawerLayout drawer;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_settings)
    TextView tvSettings;
    @BindView(R.id.tv_home)
    TextView tvHome;
    @BindView(R.id.tv_my_activity)
    TextView tvMyActivity;
    @BindView(R.id.tv_go_premium)
    TextView tvGoPremium;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @BindView(R.id.iv_profile_pic)
    ImageView ivProfilePic;
    @BindView(R.id.iv_settings)
    ImageView ivSettings;
    @BindView(R.id.iv_about)
    ImageView ivAbout;
    @BindView(R.id.iv_go)
    ImageView ivGo;
    @BindView(R.id.iv_my_activity)
    ImageView ivMyActivity;
    @BindView(R.id.iv_home)
    ImageView ivHome;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_view_profile)
    TextView tvViewProfile;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_home_toolbar)
    RelativeLayout rlHomeToolbar;
    @BindView(R.id.iv_notification)
    ImageView ivNotification;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_become_busniss)
    TextView tvBecomeBusiness;


    private Handler handler;
    private Runnable runnable;
    private int DRAWER_CLOSE_DELAY = 80;
    private LatLng selectedLatLong = new LatLng(28.1213, 77.232323);

    private CardStackLayoutManager manager;
    private ProximityContentManager proximityContentManager;

    private Set<? extends ProximityZoneContext> contexts = null;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SocketManager.init();
        handler = new Handler();
        DataManager.getInstance().setDeviceTokenStatus(AppUtils.getDeviceToken(this));
        AppUtils.hitDeviceTokenApi();
        //checkingForRequirments();
        tvGoPremium.setVisibility(View.GONE);
        handleNotification();
    }

    private void handleNotification() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            String type = getIntent().getExtras().getString(AppConstants.NOTIFICATION_TYPE);
            String campId = getIntent().getExtras().getString(SocketConstant.SOCKEYKEYS.CAMP_ID);
            String campType = getIntent().getExtras().getString(SocketConstant.SOCKEYKEYS.CAMP_TYPE);
            String beaconId = getIntent().getExtras().getString(SocketConstant.SOCKEYKEYS.BEACON_ID);
            if (type != null) {
                if (type.equals("811")) {
                    addFragmentWithBackstack(R.id.container, new HomeFragment(), HomeFragment.class.getSimpleName());
                    rightSIdeClicked();
                    Bundle bundle = new Bundle();
                    bundle.putString(SocketConstant.SOCKEYKEYS.CAMP_ID, campId);
                    bundle.putString(SocketConstant.SOCKEYKEYS.CAMP_TYPE, campType);
                    bundle.putString(SocketConstant.SOCKEYKEYS.BEACON_ID, beaconId);
                    ActivityUtils.startDetailsActivity(this, bundle);
                }else if (type.equals("812")) {
                    moveToMyActivities(null);
                    Bundle bundle = new Bundle();
                    bundle.putString(SocketConstant.SOCKEYKEYS.CAMP_ID, campId);
                    bundle.putString(SocketConstant.SOCKEYKEYS.CAMP_TYPE, campType);
                    bundle.putString(SocketConstant.SOCKEYKEYS.BEACON_ID, beaconId);
                    ActivityUtils.startDetailsActivity(this, bundle);
                }else {
                    addFragmentWithBackstack(R.id.container, new HomeFragment(), HomeFragment.class.getSimpleName());
                }
            }else {
                addFragmentWithBackstack(R.id.container, new HomeFragment(), HomeFragment.class.getSimpleName());
            }
        }else {
            addFragmentWithBackstack(R.id.container, new HomeFragment(), HomeFragment.class.getSimpleName());
        }
    }

   /* public LatLng getSelectedLatlong() {
        return selectedLatLong;
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        handleDrawer();
        tvName.setText(DataManager.getInstance().getFullName());
        ImageUtils.setImage(this, DataManager.getInstance().getProfileIMage(), ivProfilePic, ContextCompat.getDrawable(this, R.drawable.ic_placeholder));
        checkBusinessStatus();
    }

    /**
     * function to check user business status
     */

    public void checkBusinessStatus() {
        if (DataManager.getInstance().getBusinessUserStatus() == 0) {
            tvBecomeBusiness.setVisibility(View.VISIBLE);
        }else {
            tvBecomeBusiness.setVisibility(View.GONE);
        }
    }

    public View getParentLayout() {
        return drawer;
    }


    // setting the toolbar
    public void setupToolbar(int isHome, String title) {
        if (isHome == 1) {
            ivLeft.setVisibility(View.VISIBLE);
            ivRight.setVisibility(View.VISIBLE);
            rlHomeToolbar.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
            tvAddress.setVisibility(View.GONE);
            ivNotification.setVisibility(View.VISIBLE);
            ivSearch.setVisibility(View.VISIBLE);
        } else if (isHome == 0) {
            ivLeft.setVisibility(View.VISIBLE);
            ivRight.setVisibility(View.VISIBLE);
            rlHomeToolbar.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
            tvAddress.setVisibility(View.GONE);

            ivNotification.setVisibility(View.VISIBLE);
            ivSearch.setVisibility(View.GONE);
        } else if (isHome == 2) {
            ivLeft.setVisibility(View.VISIBLE);
            ivRight.setVisibility(View.VISIBLE);
            rlHomeToolbar.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
            tvAddress.setVisibility(View.VISIBLE);
            ivSearch.setVisibility(View.GONE);
        } else {
            rlHomeToolbar.setVisibility(View.GONE);
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
            ivLeft.setVisibility(View.GONE);
            ivRight.setVisibility(View.GONE);
            ivSearch.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_home;
    }

    @OnClick({R.id.tv_become_busniss, R.id.iv_menu, R.id.iv_left, R.id.iv_right, R.id.tv_home, R.id.tv_my_activity, R.id.tv_go_premium, R.id.tv_view_profile,
            R.id.iv_search, R.id.iv_notification, R.id.tv_about, R.id.tv_settings})
    public void onViewClicked(View view) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        switch (view.getId()) {
            case R.id.tv_settings:
                moveToSettingsScreen(fragment);
                break;
            case R.id.tv_go_premium:
                drawer.closeDrawer();
                showToastLong("Under Development");
                break;
            case R.id.iv_left:
                leftClicked();
                break;
            case R.id.iv_search:
                startActivity(new Intent(this, SavedItemSearchActivity.class));
                break;
            case R.id.iv_right:
                rightSIdeClicked();
                break;
            case R.id.iv_menu:
                drawerOpenAClosed();
                break;
            case R.id.tv_become_busniss:
                becomeBusinessUserScreen(fragment);
                break;
            case R.id.iv_notification:
                startActivity(new Intent(this, NotificationActivity.class));

                break;
            case R.id.tv_my_activity:
                moveToMyActivities(fragment);
                break;
            case R.id.tv_about:
                moveToAboutScreen(fragment);
                break;
            case R.id.tv_home:
                moveToHomeScreen(fragment);
                break;
            case R.id.tv_view_profile:
                setProfileFragment(fragment);

                break;

        }
    }

    private void setProfileFragment(Fragment fragment) {
        runnable = new Runnable() {
            @Override
            public void run() {
                drawer.closeDrawer();
            }
        };
        handler.postDelayed(runnable, DRAWER_CLOSE_DELAY);
        if (!(fragment instanceof ProfileFragment)) {
            replaceFragment(R.id.container, ProfileFragment.getInstance(), ProfileFragment.class.getSimpleName());
            setNoSelectedTab();

        }
    }

    private void becomeBusinessUserScreen(Fragment fragment) {
        runnable = new Runnable() {
            @Override
            public void run() {
                drawer.closeDrawer();
            }
        };
        handler.postDelayed(runnable, DRAWER_CLOSE_DELAY);
        if (!(fragment instanceof BusinessUserFragment)) {
            replaceFragment(R.id.container, new BusinessUserFragment(), BusinessUserFragment.class.getSimpleName());
            setNoSelectedTab();
        }
    }

    private void setNoSelectedTab() {
        tvHome.setTextColor(getResources().getColor(R.color.warm_grey));
        tvHome.setBackground(null);
        tvHome.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_unselected, 0, 0, 0);
        ivHome.setVisibility(View.GONE);

        tvAbout.setTextColor(getResources().getColor(R.color.warm_grey));
        tvAbout.setBackground(null);
        tvAbout.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_about_unselected, 0, 0, 0);
        ivAbout.setVisibility(View.GONE);

        tvGoPremium.setTextColor(getResources().getColor(R.color.warm_grey));
        tvGoPremium.setBackground(null);
        ivGo.setVisibility(View.GONE);

        tvMyActivity.setTextColor(getResources().getColor(R.color.warm_grey));
        tvMyActivity.setBackground(null);
        tvMyActivity.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_activities_unselected, 0, 0, 0);
        ivMyActivity.setVisibility(View.GONE);

        tvSettings.setTextColor(getResources().getColor(R.color.warm_grey));
        tvSettings.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_settings_unselected, 0, 0, 0);
        tvSettings.setBackground(null);
        ivSettings.setVisibility(View.GONE);
    }

    private void moveToSettingsScreen(Fragment fragment) {
        runnable = new Runnable() {
            @Override
            public void run() {
                drawer.closeDrawer();
            }
        };
        handler.postDelayed(runnable, DRAWER_CLOSE_DELAY);
        if (!(fragment instanceof SettingsFragment)) {
            ivHome.setVisibility(View.GONE);
            tvHome.setTextColor(getResources().getColor(R.color.warm_grey));
            tvHome.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_unselected, 0, 0, 0);
            tvHome.setBackground(null);

            ivAbout.setVisibility(View.GONE);
            tvAbout.setTextColor(getResources().getColor(R.color.warm_grey));
            tvAbout.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_about_unselected, 0, 0, 0);
            tvAbout.setBackground(null);

            tvGoPremium.setTextColor(getResources().getColor(R.color.warm_grey));
            tvGoPremium.setBackground(null);
            ivGo.setVisibility(View.GONE);

            tvMyActivity.setTextColor(getResources().getColor(R.color.warm_grey));
            tvMyActivity.setBackground(null);
            ivMyActivity.setVisibility(View.GONE);
            tvMyActivity.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_activities_unselected, 0, 0, 0);

            tvSettings.setTextColor(getResources().getColor(R.color.White));
            tvSettings.setBackground(getResources().getDrawable(R.drawable.side_menu_selected));
            tvSettings.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_settings_selected, 0, 0, 0);
            ivSettings.setVisibility(View.VISIBLE);

            replaceFragment(R.id.container, new SettingsFragment(), SettingsFragment.class.getSimpleName());
        }
    }

    private void moveToAboutScreen(Fragment fragment) {
        runnable = new Runnable() {
            @Override
            public void run() {
                drawer.closeDrawer();
            }
        };
        handler.postDelayed(runnable, DRAWER_CLOSE_DELAY);
        if (!(fragment instanceof AboutFragment)) {
            tvHome.setTextColor(getResources().getColor(R.color.warm_grey));
            tvHome.setBackground(null);
            tvHome.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_unselected, 0, 0, 0);
            ivHome.setVisibility(View.GONE);

            tvAbout.setTextColor(getResources().getColor(R.color.White));
            tvAbout.setBackground(getResources().getDrawable(R.drawable.side_menu_selected));
            tvAbout.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_about_selected, 0, 0, 0);
            ivAbout.setVisibility(View.VISIBLE);

            tvGoPremium.setTextColor(getResources().getColor(R.color.warm_grey));
            tvGoPremium.setBackground(null);
            ivGo.setVisibility(View.GONE);

            tvMyActivity.setTextColor(getResources().getColor(R.color.warm_grey));
            tvMyActivity.setBackground(null);
            tvMyActivity.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_activities_unselected, 0, 0, 0);
            ivMyActivity.setVisibility(View.GONE);

            tvSettings.setTextColor(getResources().getColor(R.color.warm_grey));
            tvSettings.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_settings_unselected, 0, 0, 0);
            tvSettings.setBackground(null);
            ivSettings.setVisibility(View.GONE);
            replaceFragment(R.id.container, AboutFragment.getInstance(), AboutFragment.class.getSimpleName());
        }
    }

    public void moveToHomeScreen(Fragment fragment) {
        runnable = new Runnable() {
            @Override
            public void run() {
                drawer.closeDrawer();
            }
        };
        handler.postDelayed(runnable, DRAWER_CLOSE_DELAY);

        if (!(fragment instanceof HomeFragment)) {
            tvHome.setTextColor(getResources().getColor(R.color.White));
            tvHome.setBackground(getResources().getDrawable(R.drawable.side_menu_selected));
            tvHome.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_selected, 0, 0, 0);
            ivHome.setVisibility(View.VISIBLE);

            tvAbout.setTextColor(getResources().getColor(R.color.warm_grey));
            tvAbout.setBackground(null);
            tvAbout.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_about_unselected, 0, 0, 0);
            ivAbout.setVisibility(View.GONE);

            tvGoPremium.setTextColor(getResources().getColor(R.color.warm_grey));
            tvGoPremium.setBackground(null);
            ivGo.setVisibility(View.GONE);

            tvMyActivity.setTextColor(getResources().getColor(R.color.warm_grey));
            tvMyActivity.setBackground(null);
            tvMyActivity.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_activities_unselected, 0, 0, 0);
            ivMyActivity.setVisibility(View.GONE);

            tvSettings.setTextColor(getResources().getColor(R.color.warm_grey));
            tvSettings.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_settings_unselected, 0, 0, 0);
            tvSettings.setBackground(null);
            ivSettings.setVisibility(View.GONE);

            ivRight.setImageDrawable(getResources().getDrawable(R.drawable.ic_black_list_view));
            ivLeft.setImageDrawable(getResources().getDrawable(R.drawable.ic_black_map_pin));
            replaceFragment(R.id.container, new HomeFragment(), HomeFragment.class.getSimpleName());
        }
    }

    private void moveToMyActivities(Fragment fragment) {
        runnable = new Runnable() {
            @Override
            public void run() {
                drawer.closeDrawer();
            }
        };
        handler.postDelayed(runnable, DRAWER_CLOSE_DELAY);

        if (!(fragment instanceof MyActivityFragment)) {
            ivHome.setVisibility(View.GONE);
            tvHome.setTextColor(getResources().getColor(R.color.warm_grey));
            tvHome.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_unselected, 0, 0, 0);
            tvHome.setBackground(null);

            ivAbout.setVisibility(View.GONE);
            tvAbout.setTextColor(getResources().getColor(R.color.warm_grey));
            tvAbout.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_about_unselected, 0, 0, 0);
            tvAbout.setBackground(null);

            tvGoPremium.setTextColor(getResources().getColor(R.color.warm_grey));
            tvGoPremium.setBackground(null);
            ivGo.setVisibility(View.GONE);

            tvMyActivity.setTextColor(getResources().getColor(R.color.White));
            tvMyActivity.setBackground(getResources().getDrawable(R.drawable.side_menu_selected));
            ivMyActivity.setVisibility(View.VISIBLE);
            tvMyActivity.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_activities_selected, 0, 0, 0);

            tvSettings.setTextColor(getResources().getColor(R.color.warm_grey));
            tvSettings.setBackground(null);
            tvSettings.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_settings_unselected, 0, 0, 0);
            ivSettings.setVisibility(View.GONE);
            replaceFragment(R.id.container, new MyActivityFragment(), MyActivityFragment.class.getSimpleName());
        }
    }

    private void rightSIdeClicked() {
        if (ivRight.getDrawable().getConstantState() == (ContextCompat.getDrawable(this, R.drawable.ic_black_map_pin).getConstantState())) {
            if (ivLeft.getDrawable().getConstantState() == (ContextCompat.getDrawable(this, R.drawable.ic_card_view_black).getConstantState())) {
                ivRight.setImageDrawable(getResources().getDrawable(R.drawable.ic_black_list_view));
                addFragmentWithBackstackWithPop(R.id.container, new MapViewFragment(), MapViewFragment.class.getSimpleName());
            } else {
                ivRight.setImageDrawable(getResources().getDrawable(R.drawable.ic_card_view_black));
                addFragmentWithBackstack(R.id.container, new MapViewFragment(), MapViewFragment.class.getSimpleName());
            }

//            replaceFragment(R.id.container, new MapViewFragment(), MapViewFragment.class.getSimpleName());

            // map frgamnet

        } else if (ivRight.getDrawable().getConstantState() == (ContextCompat.getDrawable(this, R.drawable.ic_card_view_black).getConstantState())) {
            if (ivLeft.getDrawable().getConstantState() == (ContextCompat.getDrawable(this, R.drawable.ic_black_list_view).getConstantState())) {
                ivRight.setImageDrawable(getResources().getDrawable(R.drawable.ic_black_map_pin));
                if (getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getSimpleName()) == null) {
                    addFragmentWithBackstack(R.id.container, new HomeFragment(), HomeFragment.class.getSimpleName());
                } else {
                    popFragment();
                }
                setupToolbar(0, "Browse");
            } else {
                ivRight.setImageDrawable(getResources().getDrawable(R.drawable.ic_black_list_view));
                if (getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getSimpleName()) == null) {
                    addFragmentWithBackstack(R.id.container, new HomeFragment(), HomeFragment.class.getSimpleName());
                } else {
                    popFragment();
                }
                setupToolbar(0, "Browse");
            }
//            replaceFragment(R.id.container, new HomeFragment(), HomeFragment.class.getSimpleName());

            //tinder view Fragment
        } else if (ivRight.getDrawable().getConstantState() == (ContextCompat.getDrawable(this, R.drawable.ic_black_list_view).getConstantState())) {
            if (ivLeft.getDrawable().getConstantState() == (ContextCompat.getDrawable(this, R.drawable.ic_black_map_pin).getConstantState())) {
                ivRight.setImageDrawable(getResources().getDrawable(R.drawable.ic_card_view_black));
                addFragmentWithBackstack(R.id.container, new HomeListViewFragment(), HomeListViewFragment.class.getSimpleName());
            } else {
                ivRight.setImageDrawable(getResources().getDrawable(R.drawable.ic_black_map_pin));
                addFragmentWithBackstackWithPop(R.id.container, new HomeListViewFragment(), HomeListViewFragment.class.getSimpleName());
            }
//            replaceFragment(R.id.container, new HomeListViewFragment(), HomeListViewFragment.class.getSimpleName());

            // list view fragment
        }
    }

    private void leftClicked() {
        if (ivLeft.getDrawable().getConstantState() == (ContextCompat.getDrawable(this, R.drawable.ic_black_map_pin).getConstantState())) {
            if (ivRight.getDrawable().getConstantState() == (ContextCompat.getDrawable(this, R.drawable.ic_card_view_black).getConstantState())) {
                ivLeft.setImageDrawable(getResources().getDrawable(R.drawable.ic_black_list_view));
                addFragmentWithBackstackWithPop(R.id.container, new MapViewFragment(), MapViewFragment.class.getSimpleName());
            } else {
                ivLeft.setImageDrawable(getResources().getDrawable(R.drawable.ic_card_view_black));
                addFragmentWithBackstack(R.id.container, new MapViewFragment(), MapViewFragment.class.getSimpleName());
            }
//            replaceFragment(R.id.container, new MapViewFragment(), MapViewFragment.class.getSimpleName());

            // map fragment will open

        } else if (ivLeft.getDrawable().getConstantState() == (ContextCompat.getDrawable(this, R.drawable.ic_card_view_black).getConstantState())) {
            if (ivRight.getDrawable().getConstantState() == (ContextCompat.getDrawable(this, R.drawable.ic_black_list_view).getConstantState())) {
                ivLeft.setImageDrawable(getResources().getDrawable(R.drawable.ic_black_map_pin));
                if (getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getSimpleName()) == null) {
                    addFragmentWithBackstack(R.id.container, new HomeFragment(), HomeFragment.class.getSimpleName());
                } else {
                    popFragment();
                }
                setupToolbar(0, "Browse");
            } else {
                ivLeft.setImageDrawable(getResources().getDrawable(R.drawable.ic_black_list_view));
                if (getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getSimpleName()) == null) {
                    addFragmentWithBackstack(R.id.container, new HomeFragment(), HomeFragment.class.getSimpleName());
                } else {
                    popFragment();
                }
                setupToolbar(0, "Browse");
            }
//            replaceFragment(R.id.container, new HomeFragment(), HomeFragment.class.getSimpleName());

            // tinder list fragment
        } else if (ivLeft.getDrawable().getConstantState() == (ContextCompat.getDrawable(this, R.drawable.ic_black_list_view).getConstantState())) {
            if (ivRight.getDrawable().getConstantState() == (ContextCompat.getDrawable(this, R.drawable.ic_black_map_pin).getConstantState())) {
                ivLeft.setImageDrawable(getResources().getDrawable(R.drawable.ic_card_view_black));
                addFragmentWithBackstack(R.id.container, new HomeListViewFragment(), HomeListViewFragment.class.getSimpleName());
            } else {
                ivLeft.setImageDrawable(getResources().getDrawable(R.drawable.ic_black_map_pin));
                addFragmentWithBackstackWithPop(R.id.container, new HomeListViewFragment(), HomeListViewFragment.class.getSimpleName());
            }
//            replaceFragment(R.id.container, new HomeListViewFragment(), HomeListViewFragment.class.getSimpleName());

            // list view fragment
        }
    }

    private void handleDrawer() {
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
                drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();
    }

    @Override
    public void openChangePasswordFragment() {

    }

    @Override
    public void logOutSuccess() {

    }

    @Override
    public void drawerOpenAClosed() {
        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            drawer.openDrawer();
        }
    }

    @Override
    public void steerToEditProfile() {
        startActivity(new Intent(this, EditProfileActivity.class));
    }

    @Override
    public void steerToUpdateEmail() {
        Intent intent = new Intent(HomeActivity.this, ChangePasswordActivity.class);
        intent.putExtra(AppConstantClass.APIConstant.ACTION, "updateEmail");
        startActivity(intent);
    }

    @Override
    public void moveToChangePasswordScreen() {
        startActivity(new Intent(HomeActivity.this, ChangePasswordActivity.class));

    }

    //open static pages
    @Override
    public void openUrl(String pageName) {
        Intent intent = new Intent(HomeActivity.this, StaticPagesActivity.class);
        intent.putExtra(AppConstantClass.STATIC_PAGES.PAGE, pageName);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if(count == 0){
            Toast.makeText(HomeActivity.this,"Press again to exit", Toast.LENGTH_SHORT).show();
            count++;
        }else if(count == 1){
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    private void checkingForRequirments() {
        RequirementsWizardFactory
                .createEstimoteRequirementsWizard()
                .fulfillRequirements((this),
                        new Function0<Unit>() {
                            @Override
                            public Unit invoke() {
                                Log.d("app", "requirements fulfilled");
                                startProximityContentManager();
                                //ActivityUtils.scheduleJob(getApplicationContext());
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

    private void startProximityContentManager() {
        if (proximityContentManager == null) {
            proximityContentManager = new ProximityContentManager(this, this, cloudCredentials);
            proximityContentManager.start();
        }
    }

    @Override
    public void onBeaconList(Set<? extends ProximityZoneContext> contexts) {
        this.contexts = contexts;
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
//        if (fragment instanceof HomeFragment)
//            ((HomeFragment) fragment).onBeaconList(contexts);
//        else if (fragment instanceof HomeListViewFragment)
//            ((HomeListViewFragment) fragment).onBeaconList(contexts);

    }

    public  Set<? extends ProximityZoneContext> getContextsList() {
        return contexts;
    }

    public void setAddress(String result) {
        tvAddress.setText(result);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent != null && intent.getExtras()!= null) {
            String from = intent.getExtras().getString(AppConstantClass.FROM, "");
            if (from.equalsIgnoreCase("CardListFund") || from.equalsIgnoreCase("CardListPay") || from.equalsIgnoreCase("Event")) {
                BaseFragment fragment = new MyActivityFragment();
                Bundle bundle = new Bundle();
                bundle.putString(AppConstantClass.FROM, from);
                fragment.setArguments(bundle);

                ivHome.setVisibility(View.GONE);
                tvHome.setTextColor(getResources().getColor(R.color.warm_grey));
                tvHome.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_unselected, 0, 0, 0);
                tvHome.setBackground(null);

                tvMyActivity.setTextColor(getResources().getColor(R.color.White));
                tvMyActivity.setBackground(getResources().getDrawable(R.drawable.side_menu_selected));
                ivMyActivity.setVisibility(View.VISIBLE);
                tvMyActivity.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_activities_selected, 0, 0, 0);

//                replaceFragment(R.id.container, fragment, MyActivityFragment.class.getSimpleName());
                if (getSupportFragmentManager() != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment, MyActivityFragment.class.getSimpleName())
                            .commit();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AppConstants.REQUEST_DETAIL_SCREEN && resultCode == RESULT_OK && data != null && data.getExtras() != null) {
            int campId = data.getExtras().getInt(SocketConstant.SOCKEYKEYS.CAMP_ID, 0);
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
            if (fragment instanceof HomeFragment) {
                ((HomeFragment) fragment).removeReportDeal(campId);
            }
            if (fragment instanceof HomeListViewFragment) {
                ((HomeListViewFragment) fragment).removeReportDeal(campId);
            }
        }
    }
}
