package com.geora.ui.home.detailpage;

import android.Manifest;
import android.annotation.SuppressLint;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geora.R;
import com.geora.base.BaseActivity;
import com.geora.customviews.DialogForColors;
import com.geora.customviews.DialogForDonateAmount;
import com.geora.customviews.DialogForReport;
import com.geora.customviews.DialogForValidate;
import com.geora.data.DataManager;
import com.geora.data.constants.AppConstantClass;
import com.geora.listeners.RecycleListener;
import com.geora.model.ChargeAmountModel;
import com.geora.model.EventModel;
import com.geora.model.FundRaisingModel;
import com.geora.model.ProductDetailModel;
import com.geora.model.PromootionModel;
import com.geora.model.sale.GetColor;
import com.geora.model.sale.GetImage;
import com.geora.model.sale.GetSize;
import com.geora.model.sale.SaleModel;
import com.geora.socket.SocketCallback;
import com.geora.socket.SocketConstant;
import com.geora.ui.DetailsActivity.ImageViewPagerAdapter;
import com.geora.ui.DetailsActivity.WebViewActivity;
import com.geora.ui.address.AddressListActivity;
import com.geora.ui.cardlist.CardListActivity;
import com.geora.ui.home.detailpage.rsvp.RSVPActivity;
import com.geora.ui.test.TestActivity;
import com.geora.util.AppUtils;
import com.geora.util.ImageUtils;
import com.google.gson.Gson;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.relex.circleindicator.CircleIndicator;

import static com.geora.util.DateTimeUtils.timeStampToEndCamp;
import static com.geora.util.DateTimeUtils.timeStampToEndCampWithTime;
import static com.geora.util.DateTimeUtils.timeStampToStartCamp;
import static com.geora.util.DateTimeUtils.timeStampToStratCampWithTime;

public class DetailsActivity extends BaseActivity implements SocketCallback, RecycleListener {

    @BindView(R.id.rl_images)
    RelativeLayout rlImages;
    /*@BindView(R.id.rl_amount)
    RelativeLayout rlAmount;*/
    @BindView(R.id.rl_direction)
    RelativeLayout rlDirection;
    @BindView(R.id.rl_size)
    RelativeLayout rlSize;
    @BindView(R.id.rl_detail)
    RelativeLayout rlDetail;
    @BindView(R.id.rl_funds)
    RelativeLayout rlFunds;
    @BindView(R.id.rl_des)
    RelativeLayout rlDes;
    @BindView(R.id.rl_time)
    RelativeLayout rlTime;
    @BindView(R.id.rl_rsvp)
    RelativeLayout rlRsvp;
    @BindView(R.id.rl_colors)
    RelativeLayout rlColors;
    @BindView(R.id.tv_button)
    TextView tvButton;
    @BindView(R.id.tv_link)
    TextView tvLink;
    @BindView(R.id.tv_rsvp_text)
    TextView tvRsvpText;
    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.tv_time_text)
    TextView tvTimeText;
    @BindView(R.id.tv_read_more)
    TextView tvReadMore;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.tv_des)
    TextView tvDes;
    @BindView(R.id.tv_target)
    TextView tvTarget;
    @BindView(R.id.progress_amt_donated)
    ProgressBar progressAmtDonated;
    @BindView(R.id.tv_collected)
    TextView tvCollected;
    @BindView(R.id.tv_date_of_event)
    TextView tvDateOfEvent;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_dis_price)
    TextView tvDisPrice;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_price_target)
    TextView tvPriceTarget;
    @BindView(R.id.t_name)
    TextView tName;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.main)
    RelativeLayout main;
    @BindView(R.id.rv_size)
    RecyclerView rvSize;
    @BindView(R.id.rv_images)
    RecyclerView rvImages;
    @BindView(R.id.tv_size_)
    TextView tvSize;
    @BindView(R.id.iv_one)
    TextView ivOne;
    @BindView(R.id.iv_two)
    TextView ivTwo;
    @BindView(R.id.iv_three)
    TextView ivThree;
    @BindView(R.id.tv_no_of_color)
    TextView tvNoOfColor;
    @BindView(R.id.tv_download)
    TextView tvDownload;
    @BindView(R.id.vp_images)
    ViewPager vpImages;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.pageIndicatorView)
    CircleIndicator pageIndicatorView;

    private Unbinder unbinder;
    private String beaconId = "", filter = "";
    private String eventId = "", busnissId = "";
    private int campId, campType;
    private double lat, lng;
    private String formData = "", image;
    private SizeAdapter adapterSize;
    private ImagesAdapter adapterImage;
    private int prePos = 0;
    private List<GetColor> colorList = new LinkedList<>();
    private ChargeAmountModel chargeAmountModel = new ChargeAmountModel();
    private String allImages;
    private String selectedColor;
    private int selectedSize;
    private ImageViewPagerAdapter mAdapter;
    private String[] allImagesList;
    private String downloadUrl = "";
    private String promotionalUrl = "";
    private DetailsViewModel mDetailsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDetailsViewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);
        mDetailsViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        setObservers();

        getWindow().getSharedElementEnterTransition().setDuration(700);
        getWindow().getSharedElementReturnTransition().setDuration(700)
                .setInterpolator(new DecelerateInterpolator());

        Bundle bundle = getIntent().getBundleExtra(AppConstantClass.DATA);
        if (bundle != null)
            getRequiredDataFromBundle(bundle);
        setViewAccordingToCampId();
        getCampDetailsSocket();
    }

    private void setupAdapter() {
        allImages = allImages.replace("[", "");
        allImages = allImages.replace("]", "");
        allImagesList = allImages.split(",");
        mAdapter = new ImageViewPagerAdapter(this, allImagesList, () -> {
            int pos = vpImages.getCurrentItem();
            Intent intent = new Intent(DetailsActivity.this, TestActivity.class);
            intent.putExtra(AppConstantClass.DATA, allImages);
            intent.putExtra(AppConstantClass.POSITION, pos);
            startActivity(intent);
        });
        vpImages.setAdapter(mAdapter);
        if (allImagesList.length > 1)
            pageIndicatorView.setViewPager(vpImages);
    }

    private void connectWebSocket(SocketCallback callback) {
//        SocketManager.getInstance().setSocketCallbackListener(callback);
    }

    @Override
    public void onResume() {
        super.onResume();
//        connectWebSocket(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        connectWebSocket(null);
    }

    private void reportCamp(String message) {
        showProgressDialog();
        HashMap<String, Object> params = new HashMap<>();
        params.put(SocketConstant.SOCKEYKEYS.CAMP_ID, campId);
        params.put(SocketConstant.SOCKEYKEYS.CAMP_TYPE, campType);
        params.put(SocketConstant.SOCKEYKEYS.MESSAGE, message);
        params.put(SocketConstant.SOCKEYKEYS.BEACON_ID, beaconId);
        mDetailsViewModel.hitReportApi(params);
    }

    private void getCampDetailsSocket() {
        showProgressDialog();
        /*JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, SocketConstant.SOCKEYKEYS.DETAIL);
            jsonObject.put(SocketConstant.SOCKEYKEYS.IS_SUBSCRIBED_USER, false);
            jsonObject.put(SocketConstant.SOCKEYKEYS.CAMP_ID, campId);
            jsonObject.put(SocketConstant.SOCKEYKEYS.CAMP_TYPE, campType);
            jsonObject.put(SocketConstant.SOCKEYKEYS.BEACON_ID, beaconId);
            jsonObject.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());
            SocketManager.getInstance().sendData(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        HashMap<String, Object> params = new HashMap<>();

        params.put(SocketConstant.SOCKEYKEYS.ACTION_TYPE, SocketConstant.SOCKEYKEYS.DETAIL);
        params.put(SocketConstant.SOCKEYKEYS.IS_SUBSCRIBED_USER, false);
        params.put(SocketConstant.SOCKEYKEYS.CAMP_ID, campId);
        params.put(SocketConstant.SOCKEYKEYS.CAMP_TYPE, campType);
        params.put(SocketConstant.SOCKEYKEYS.BEACON_ID, beaconId);
        params.put(SocketConstant.SOCKEYKEYS.USER_ID, DataManager.getInstance().getUserId());

        if (isInternetAvailable())
            mDetailsViewModel.hitDetailsApi(params);
        else {
            showToastLong(getResources().getString(R.string.no_internet_connection));
        }
    }

    private void setViewAccordingToCampId() {
        chargeAmountModel.setCampType(campType + "");
        if (campType == AppConstantClass.CAMPTYPE.PROMOTION) {
            tvButton.setVisibility(View.GONE);
            setFieldsVisibityAccordingToCampType(false, false, true, false, false, true);
            tvButton.setText(getResources().getString(R.string.details));

        } else if (campType == AppConstantClass.CAMPTYPE.FUNDRAISING) {
            tvButton.setVisibility(View.VISIBLE);
            tvButton.setText(getResources().getString(R.string.donate));
            setFieldsVisibityAccordingToCampType(false, true, false, false, false, false);

        } else if (campType == AppConstantClass.CAMPTYPE.SALES) {
            setFieldsVisibityAccordingToCampType(true, false, false, true, false, false);
            tvButton.setText(getResources().getString(R.string.buy));

        } else if (campType == AppConstantClass.CAMPTYPE.EVENT) {
            setFieldsVisibityAccordingToCampType(false, false, true, false, true, true);
            tvButton.setVisibility(View.GONE);
        }
    }

    private void setFieldsVisibityAccordingToCampType(boolean images, boolean funds, boolean time, boolean size, boolean rsvp, boolean location) {
        if (images) rlImages.setVisibility(View.VISIBLE);
        else rlImages.setVisibility(View.GONE);

        if (funds) rlFunds.setVisibility(View.VISIBLE);
        else rlFunds.setVisibility(View.GONE);

        if (rsvp) rlRsvp.setVisibility(View.VISIBLE);
        else rlRsvp.setVisibility(View.GONE);

        if (time) rlTime.setVisibility(View.VISIBLE);
        else rlTime.setVisibility(View.GONE);

        if (size) rlSize.setVisibility(View.VISIBLE);
        else rlSize.setVisibility(View.GONE);

        if (location) {
            rlDirection.setVisibility(View.VISIBLE);
            tvPriceTarget.setVisibility(View.GONE);
            tvDisPrice.setVisibility(View.GONE);
            tvLeft.setVisibility(View.GONE);
            tvPrice.setVisibility(View.GONE);
            //rlAmount.setVisibility(View.GONE);
        } else {
            rlDirection.setVisibility(View.GONE);
            //rlAmount.setVisibility(View.VISIBLE);
            tvPriceTarget.setVisibility(View.VISIBLE);
            tvDisPrice.setVisibility(View.VISIBLE);
            tvLeft.setVisibility(View.VISIBLE);
            tvPrice.setVisibility(View.VISIBLE);

            if (campType == AppConstantClass.CAMPTYPE.SALES) {
                tvLeft.setVisibility(View.VISIBLE);
                tvPrice.setVisibility(View.VISIBLE);
            } else {
                tvLeft.setVisibility(View.GONE);
                tvPrice.setVisibility(View.GONE);
            }

        }
    }


    private void OpenDonationBox() {
        new DialogForDonateAmount(this, (view, result, s) -> {
            if (!result.isEmpty() && Double.parseDouble(result) > 0) {
                    chargeAmountModel.setAmount(result);
                moveToCardListActivity(chargeAmountModel);
            } else
                showSnackBar(main, "Add amount to donate", true);
        }).show();
    }

    private void moveToCardListActivity(ChargeAmountModel chanrgeModel) {
        chargeAmountModel.setCampType(campType + "");
        Intent intent = new Intent(this, CardListActivity.class);
        intent.putExtra(AppConstantClass.CHARGEDATA, chanrgeModel);
        startActivity(intent);
    }

    @OnClick({R.id.rl_rsvp, R.id.rl_colors, R.id.rl_direction, R.id.tv_button, R.id.iv_image, R.id.view_overlay, R.id.iv_back, R.id.tv_read_more, R.id.tv_download, R.id.iv_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_read_more:
                if(tvReadMore.getText().toString().equalsIgnoreCase("Read More")) {
                    tvDescription.setMaxLines(1000);
                    tvReadMore.setText("Read Less");
                }else {
                    tvDescription.setMaxLines(7);
                    tvReadMore.setText("Read More");
                }
                break;
            case R.id.tv_button:
                if (campType == AppConstantClass.CAMPTYPE.FUNDRAISING)
                    OpenDonationBox();
                else if (campType == AppConstantClass.CAMPTYPE.SALES) {
                    if(Integer.parseInt(chargeAmountModel.getAvailableStock())>0) {
                        moveToAddressList();
                    }else {
                        Toast.makeText(DetailsActivity.this,"Product stock is over",Toast.LENGTH_SHORT).show();
                    }
                } else if (campType == AppConstantClass.CAMPTYPE.EVENT) {
                    if (!formData.isEmpty())
                        createRSVPForm();
                } else if (campType == AppConstantClass.CAMPTYPE.PROMOTION) {
                    if (promotionalUrl != null && !promotionalUrl.isEmpty()) {
                        startActivity(new Intent(this, WebViewActivity.class)
                                .putExtra(AppConstantClass.URL, promotionalUrl));
                    }
                }
                break;
            case R.id.iv_back:
                backPressed();
                break;
            case R.id.rl_colors:
                openDialogForcolor();
                break;
            case R.id.rl_direction:
                moveToGoogleMap();
                break;
            case R.id.iv_image:
            case R.id.view_overlay:
                Intent intent = new Intent(this, TestActivity.class);
                intent.putExtra(AppConstantClass.DATA, allImages);
//                ActivityOptions options = ActivityOptions
//                        .makeSceneTransitionAnimation(this, ivImage, "robot");
                startActivity(intent);
                break;
            case R.id.rl_rsvp:
                if (tvButton.getVisibility() == View.GONE && campType == AppConstantClass.CAMPTYPE.EVENT)
                    openFormUrl(tvLink.getText().toString());
                break;
            case R.id.iv_more:
                showMenuPopup();
                break;
            case R.id.tv_download:
                if (downloadUrl == null || downloadUrl.isEmpty()) {
//                    showToastShort(getString(R.string.no_file));
                    return;
                }
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
                }else {
                    AppUtils.startDownloadService(this, downloadUrl);
                }
                break;
        }
    }

    /**
     * function to show the menu popup
     */
    private void showMenuPopup() {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(this, ivMore);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.detail_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_report:

                    new DialogForReport(this, (view, result, stars) -> {
                            reportCamp(result);

                        /*showToastShort("Camp reported");
                        Intent intent = new Intent();
                        intent.putExtra(SocketConstant.SOCKEYKEYS.CAMP_ID, campId);
                        setResult(RESULT_OK, intent);
                        finish();*/

                    }).show();

                    return true;
                default:
                    Toast.makeText(DetailsActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
            }
            return true;
        });

        popup.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            AppUtils.startDownloadService(this, downloadUrl);
        }
    }

    private void moveToGoogleMap() {
        try {

            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", lat, lng, tvLocation.getText().toString());
            Intent intentMap = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intentMap.setPackage("com.google.android.apps.maps");
            startActivity(intentMap);
        } catch (Exception e) {
            showSnackBar(main, "Google maps is missing or something went wrong", true);

        }
    }

    private void moveToAddressList() {
        if (tvButton.getText().toString().equalsIgnoreCase(getString(R.string.directions)))
            moveToGoogleMap();
        else {

            ProductDetailModel model = new ProductDetailModel();
            model.setDec(tvDescription.getText().toString());
            model.setName(tName.getText().toString());
            if (tvDisPrice.getText().toString().equals("N/A")) {
                model.setPrice(tvPrice.getText().toString());
            }else {
                model.setPrice(tvDisPrice.getText().toString());
            }
            model.setImage(image);
            model.setTotalPrice(tvPrice.getText().toString());
            if (tvDisPrice.getText().toString().equals("N/A")) {
                model.setDiscount("0");
            }else {
                model.setDiscount(String.valueOf(Double.parseDouble(tvPrice.getText().toString().replace("$", ""))
                        - Double.parseDouble(tvDisPrice.getText().toString().replace("$", ""))));

            }
            Intent intent = new Intent(this, AddressListActivity.class);
            intent.putExtra(AppConstantClass.DATA, 2);
            intent.putExtra(AppConstantClass.PRODUCTDATA, model);
            intent.putExtra(AppConstantClass.CHARGEDATA, chargeAmountModel);
            startActivity(intent);
        }
    }

    private void openDialogForcolor() {
        new DialogForColors(this, (view, result, s) -> chnageColorOfProduct(result), colorList).show();
    }

    private void chnageColorOfProduct(String colorCode) {


        for (int i = 0; i < colorList.size(); i++) {
            if (colorCode.equalsIgnoreCase(colorList.get(i).getColorHexCode())) {
                chargeAmountModel.setColorId(colorList.get(i).getId().toString());
                chargeAmountModel.setColourCode(colorList.get(i).getColorHexCode());
                chargeAmountModel.setAvailableStock(String.valueOf(colorList.get(i).getAvailableStock()));

                if (colorList.get(i).getGetSizes() != null && colorList.get(i).getGetSizes().size()>0) {
                    rlSize.setVisibility(View.VISIBLE);
                } else
                    rlSize.setVisibility(View.GONE);

                try {
                    setSizeAdapter(colorList.get(i).getGetSizes());
                } catch (Exception e) {

                }

                GetColor mColor = colorList.get(i);
                colorList.remove(i);
                colorList.add(0, mColor);
                setupColor(colorList);
            }
        }
    }

    private void createRSVPForm() {
        Intent intent = new Intent(this, RSVPActivity.class);
        intent.putExtra(SocketConstant.SOCKEYKEYS.DATA, formData);
        intent.putExtra(AppConstantClass.APIConstant.BUSNISS_USER_IS, busnissId);
        intent.putExtra(AppConstantClass.APIConstant.EVENTID, eventId);
        intent.putExtra(AppConstantClass.APIConstant.CAMP_ID,campId);
        startActivity(intent);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }


    private void getRequiredDataFromBundle(Bundle bundle) {
        campId = Integer.parseInt(bundle.getString(SocketConstant.SOCKEYKEYS.CAMP_ID));
        campType = Integer.parseInt(bundle.getString(SocketConstant.SOCKEYKEYS.CAMP_TYPE));
        beaconId = bundle.getString(SocketConstant.SOCKEYKEYS.BEACON_ID, "");
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_details;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override
    public void onMessage(String msg) {
        parsingJsonData(msg);
    }


    private void setObservers() {
        mDetailsViewModel.getDetailsLiveData().observe(this, response -> {
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.optInt("code");
                    String message = jsonObject.optString("message");
                    if (AppUtils.checkUserValid(this, code, message)) {
                        if (code == 423 || code == 403) {
                            showToastShort(message);
                            logout(this);
                        } else {
                            parsingJsonData(response);
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
        });
        mDetailsViewModel.getReportLiveData().observe(this, response -> {
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.optInt("code");
                    String message = jsonObject.optString("message");
                    if (AppUtils.checkUserValid(this, code, message)) {
                        if (code == 423 || code == 403) {
                            showToastShort(message);
                            logout(this);
                        } else if (code == 200 || code == 201){
                            showToastShort(message);
                            Intent intent = new Intent();
                            intent.putExtra(SocketConstant.SOCKEYKEYS.CAMP_ID, campId);
                            setResult(RESULT_OK, intent);
                            finish();
                        }else {
                            showToastShort(message);
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
        });
    }

    private void parsingJsonData(String msg) {
        runOnUiThread(() -> hideProgressDialog());
        final JSONObject jsonObject;
        try {
            Log.i("Response::::", msg);
            jsonObject = new JSONObject(msg);
            int code = jsonObject.optInt("code");
            if (code == 408) {
                runOnUiThread(() -> new DialogForValidate(DetailsActivity.this, (view, result, stars) -> finish(), jsonObject.optString("message"), getString(R.string.okay)).show());
            }else if (code == 403 || code == 421) {
                runOnUiThread(() -> {
                    Toast.makeText(DetailsActivity.this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                    AppUtils.logoutUser(DetailsActivity.this);
                });
            }else {
                if (jsonObject.optInt("code") == 410) {
                    DetailsActivity.this.runOnUiThread(() -> {
                        showToastShort(getResources().getString(R.string.Business_is_blocked));
                        finish();
                    });
                    showToastShort(getResources().getString(R.string.Business_is_blocked));
                    finish();
                } else if (jsonObject.optInt("code") == 423) {
                    DetailsActivity.this.runOnUiThread(() -> {
                        showToastShort(getResources().getString(R.string.user_is_blocked_by_admin));
                        logout(DetailsActivity.this);
                    });

                } else {
                    JSONObject data = new JSONObject(jsonObject.optString(SocketConstant.SOCKEYKEYS.DATA));
                    if (data.optInt(SocketConstant.SOCKEYKEYS.CAMP_TYPE) == 1) {
                        final PromootionModel promootionModel = new Gson().fromJson(jsonObject.optString(SocketConstant.SOCKEYKEYS.DATA), PromootionModel.class);

                        runOnUiThread(() -> {
                            updateDataInUIForPromotions(promootionModel);
                            vpImages.setVisibility(View.VISIBLE);
                            pageIndicatorView.setVisibility(View.VISIBLE);
                            ivImage.setVisibility(View.GONE);
                        });
                    } else if (data.optInt(SocketConstant.SOCKEYKEYS.CAMP_TYPE) == 2) {
                        final FundRaisingModel fundRaisingModel = new Gson().fromJson(jsonObject.optString(SocketConstant.SOCKEYKEYS.DATA), FundRaisingModel.class);

                        runOnUiThread(() -> {
                            updateDataInUI(fundRaisingModel);
                            vpImages.setVisibility(View.VISIBLE);
                            pageIndicatorView.setVisibility(View.VISIBLE);
                            ivImage.setVisibility(View.GONE);
                        });

                    } else if (data.optInt(SocketConstant.SOCKEYKEYS.CAMP_TYPE) == 3) {
                        final SaleModel saleModel = new Gson().fromJson(jsonObject.optString(SocketConstant.SOCKEYKEYS.DATA), SaleModel.class);

                        runOnUiThread(() -> {
                            updateDataUIForSale(saleModel);
                            vpImages.setVisibility(View.GONE);
                            pageIndicatorView.setVisibility(View.GONE);
                            ivImage.setVisibility(View.VISIBLE);
                        });

                    } else if (data.optInt(SocketConstant.SOCKEYKEYS.CAMP_TYPE) == 4) {
                        final EventModel eventModel = new Gson().fromJson(jsonObject.optString(SocketConstant.SOCKEYKEYS.DATA), EventModel.class);

                        runOnUiThread(() -> {
                            updateDataInUIForEvent(eventModel);
                            vpImages.setVisibility(View.VISIBLE);
                            pageIndicatorView.setVisibility(View.VISIBLE);
                            ivImage.setVisibility(View.GONE);
                        });

                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateDataUIForSale(SaleModel saleModel) {
        if (saleModel.getCampExpire() != null && saleModel.getCampExpire() == 0)
            campExpired();
        else {
            if (saleModel.getGetSale().getSaleType() != null && saleModel.getGetSale().getSaleType() == 1)
                tvButton.setText(getResources().getString(R.string.buy));
            else
                tvButton.setText(getResources().getString(R.string.directions));
            if (saleModel.getGetImage() != null && saleModel.getGetImage().size() > 0) {
                image = saleModel.getGetImage().get(0).getImageUrl();
            }else {
                image = "";
            }
            if (saleModel.getGetImage() != null) {
                for (int i=0; i<saleModel.getGetImage().size(); i++) {
                    if (i != 0) allImages += ",";
                    allImages += saleModel.getGetImage().get(0).getImageUrl();

                }
            }
            chargeAmountModel.setSaleId(saleModel.getGetSale().getId().toString());
            chargeAmountModel.setBusnissUserId(saleModel.getBusinessUserId().toString());
            ImageUtils.setImage(this, image, ivImage, getResources().getDrawable(R.drawable.placeholder_medium));
            tName.setText(saleModel.getCampTitle());
            tvLocation.setText(saleModel.getLocation() + "," + saleModel.getLocation());
            tvDateOfEvent.setText(timeStampToEndCamp(String.valueOf(saleModel.getCampEndDate())));
            tvDescription.setText(saleModel.getCampDesc());
            checkReadMore();
            if (saleModel.getGetSale().getDiscountedPrice() == null) {
//                tvDisPrice.setText("N/A");
                tvDisPrice.setText("$" + String.format("%.2f",saleModel.getGetSale().getProductPrice().doubleValue()));
                tvPrice.setVisibility(View.GONE);
            }
            else {
                tvDisPrice.setText("$" + String.format("%.2f",saleModel.getGetSale().getDiscountedPrice().doubleValue()));
                tvPrice.setVisibility(View.VISIBLE);
                tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            tvPrice.setText("$" +String.format("%.2f",saleModel.getGetSale().getProductPrice().doubleValue()));
            if (saleModel.getGetSale().getGetColor() != null && saleModel.getGetSale().getGetColor().size() > 0) {
                rlColors.setVisibility(View.VISIBLE);
                tvNoOfColor.setVisibility(View.VISIBLE);
                chargeAmountModel.setColorId(saleModel.getGetSale().getGetColor().get(0).getId().toString());
                chargeAmountModel.setColourCode(saleModel.getGetSale().getGetColor().get(0).getColorHexCode());
                chargeAmountModel.setAvailableStock(saleModel.getGetSale().getGetColor().get(0).getAvailableStock().toString());
                if (saleModel.getGetSale().getGetColor().get(0).getGetSizes() != null && saleModel.getGetSale().getGetColor().get(0).getGetSizes().size() > 0) {
                    rlSize.setVisibility(View.VISIBLE);
                    try {
                        setSizeAdapter(saleModel.getGetSale().getGetColor().get(0).getGetSizes());
                    } catch (Exception e) {
                        rlSize.setVisibility(View.GONE);
                    }
                } else {
                    rlSize.setVisibility(View.GONE);
                    chargeAmountModel.setAvailableStock(String.valueOf(saleModel.getGetSale().getGetColor().get(0).getAvailableStock()));
                    tvLeft.setText(String.format(getResources().getString(R.string.only_left_in_stock), String.valueOf(saleModel.getGetSale().getGetColor().get(0).getAvailableStock())));
                }

            }else {
                rlColors.setVisibility(View.GONE);
                tvNoOfColor.setVisibility(View.GONE);
                rlSize.setVisibility(View.GONE);
                chargeAmountModel.setAvailableStock(saleModel.getGetSale().getAvailableStock().toString());
                tvLeft.setText(String.format(getResources().getString(R.string.only_left_in_stock), chargeAmountModel.getAvailableStock()));
            }
            colorList.addAll(saleModel.getGetSale().getGetColor());
            setImageAdapter(saleModel.getGetImage());
            setupColor(saleModel.getGetSale().getGetColor());
            downloadUrl = saleModel.getDownloadUrl();
            if (downloadUrl == null || downloadUrl.isEmpty()) {
                tvDownload.setVisibility(View.GONE);
            }else {
                tvDownload.setVisibility(View.VISIBLE);
            }
        }
        hideProgressDialog();
    }

    private void campExpired() {
        showToastShort(getResources().getString(R.string.camp_has_been_expired));
        finishAfterTransition();
    }

    @SuppressLint("StringFormatMatches")
    private void setupColor(List<GetColor> colors) {
        if (colors.size() > 2) {
            ivOne.setBackgroundColor(android.graphics.Color.parseColor(colors.get(0).getColorHexCode()));
            ivTwo.setBackgroundColor(android.graphics.Color.parseColor(colors.get(1).getColorHexCode()));
            ivThree.setBackgroundColor(android.graphics.Color.parseColor(colors.get(2).getColorHexCode()));
        } else if (colors.size() > 1) {
            ivOne.setBackgroundColor(android.graphics.Color.parseColor(colors.get(0).getColorHexCode()));
            ivTwo.setBackgroundColor(android.graphics.Color.parseColor(colors.get(1).getColorHexCode()));
            ivThree.setVisibility(View.GONE);
        } else if (colors.size() > 0) {
            if(!colors.get(0).getColorHexCode().equalsIgnoreCase("")) {
                ivOne.setBackgroundColor(android.graphics.Color.parseColor(colors.get(0).getColorHexCode()));
            }
            ivTwo.setVisibility(View.GONE);
            ivThree.setVisibility(View.GONE);
        }
        tvNoOfColor.setText(String.format(getResources().getString(R.string.colors), colors.size()));

    }

    private void setSizeAdapter(List<GetSize> sizes) {
        List<GetSize> newList = new LinkedList();
        newList.addAll(sizes);
        if (adapterSize == null) {
            adapterSize = new SizeAdapter(newList, this);
            if (newList.size() > 0) {
                newList.get(0).setSelected(true);
                for(int i =0;i<newList.size(); i++){
                    if(i>0){
                        newList.get(i).setSelected(false);
                    }
                }
                chargeAmountModel.setSizeId(newList.get(0).getId().toString());
                chargeAmountModel.setSize(newList.get(0).getSize());
            }
            rvSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvSize.setAdapter(adapterSize);
            adapterSize.setClickListener(this);
            adapterSize.notifyDataSetChanged();
        } else {
            adapterSize.list.clear();
            if (newList.size() > 0) {
                newList.get(0).setSelected(true);
                chargeAmountModel.setSizeId(newList.get(0).getId().toString());
                chargeAmountModel.setSize(newList.get(0).getSize());
                for(int i =0;i<newList.size(); i++){
                    if(i>0){
                        newList.get(i).setSelected(false);
                    }
                }
            }
            adapterSize.list.addAll(newList);
            adapterSize.notifyDataSetChanged();
        }

        if (sizes.size() > 0 && sizes.get(0).getSize() != null) {
            tvLeft.setText(String.format(getResources().getString(R.string.only_left_in_stock), sizes.get(0).getAvailableStock().toString()));
            chargeAmountModel.setAvailableStock(sizes.get(0).getAvailableStock().toString());
            tvSize.setText(String.format(getResources().getString(R.string.select_size), sizes.get(0).getSize()));
        } else {
            tvSize.setText(String.format(getResources().getString(R.string.select_size), ""));
            tvLeft.setText(String.format(getResources().getString(R.string.only_left_in_stock), chargeAmountModel.getAvailableStock()));

        }
    }

    private void setImageAdapter(List<GetImage> images) {
        adapterImage = new ImagesAdapter(images, this);
        rvImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvImages.setAdapter(adapterImage);
        adapterImage.setClickListener(this);
        adapterImage.notifyDataSetChanged();
    }

    private void openFormUrl(String url) {
        if (url.startsWith("http://"))
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        else
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + url)));
    }

    private void updateDataInUIForPromotions(PromootionModel promootionModel) {
        if (promootionModel.getCamp_expire() != null && promootionModel.getCamp_expire() == 0)
            campExpired();
        else {
            image = promootionModel.getImages();
            allImages = promootionModel.getAllImages();
            ImageUtils.setImage(this, promootionModel.getImages(), ivImage, getResources().getDrawable(R.drawable.placeholder_medium));
            tName.setText(promootionModel.getCampTitle());
            tvLocation.setText(promootionModel.getCampAddress() + "," + promootionModel.getLocation());
            tvDateOfEvent.setText(timeStampToEndCamp(String.valueOf(promootionModel.getCampEndDate())));
            tvDescription.setText(promootionModel.getCampDesc());
            checkReadMore();
            tvText.setText(timeStampToStartCamp(String.valueOf(promootionModel.getCampStartDate())) + "-" + timeStampToEndCamp(String.valueOf(promootionModel.getCampEndDate())));
            lat = promootionModel.getCampLat();
            lng = promootionModel.getCampLng();
            setupAdapter();
            downloadUrl = promootionModel.getDownloadUrl();
            if (downloadUrl == null || downloadUrl.isEmpty()) {
                tvDownload.setVisibility(View.GONE);
            }else {
                tvDownload.setVisibility(View.VISIBLE);
            }
            promotionalUrl = promootionModel.getPromoUrl();
            if (promotionalUrl != null && !promotionalUrl.isEmpty()) {
                tvButton.setVisibility(View.VISIBLE);
                tvButton.setText(getResources().getString(R.string.details));
            }else {
                tvButton.setVisibility(View.GONE);
            }
        }
        hideProgressDialog();
    }

    private void updateDataInUIForEvent(EventModel eventModel) {
        if (eventModel.getCamp_expire() != null && eventModel.getCamp_expire() == 0)
            campExpired();
        else {
            image = eventModel.getImages();
            allImages = eventModel.getAllImages();

            eventId = eventModel.getEventId().toString();
            busnissId = eventModel.getBusinessUserId().toString();
            ImageUtils.setImage(this, eventModel.getImages(), ivImage, getResources().getDrawable(R.drawable.placeholder_medium));
            tName.setText(eventModel.getCampTitle());
            tvLocation.setText(eventModel.getCampAddress() + "," + eventModel.getLocation());
            tvDateOfEvent.setText(timeStampToEndCamp(String.valueOf(eventModel.getCampEndDate())));
            tvDescription.setText(eventModel.getCampDesc());
            checkReadMore();
            tvText.setText(timeStampToStratCampWithTime(String.valueOf(eventModel.getCampStartDate())) + " to " + timeStampToEndCampWithTime(String.valueOf(eventModel.getCampEndDate())));
            if (eventModel.getRsvpType() == 1) {
                tvLink.setText(eventModel.getRsvpData());
                tvLink.setTextColor(getResources().getColor(R.color.com_facebook_blue));
                tvButton.setVisibility(View.GONE);
            } else {
                formData = eventModel.getRsvpData();
                rlRsvp.setVisibility(View.GONE);
                tvButton.setVisibility(View.VISIBLE);
                tvButton.setText(getResources().getString(R.string.book));
            }
            lat = eventModel.getCampLat();
            lng = eventModel.getCampLng();
            setupAdapter();
            downloadUrl = eventModel.getDownloadUrl();
            if (downloadUrl == null || downloadUrl.isEmpty()) {
                tvDownload.setVisibility(View.GONE);
            }else {
                tvDownload.setVisibility(View.VISIBLE);
            }
        }
        hideProgressDialog();

    }

    private void updateDataInUI(FundRaisingModel fundRaisingModel) {
        if (fundRaisingModel.getCamp_expire() != null && fundRaisingModel.getCamp_expire() == 0)
            campExpired();
        else {
            image = fundRaisingModel.getImages();
            allImages = fundRaisingModel.getAllImages();

            chargeAmountModel.setBusnissUserId(fundRaisingModel.getBusinessUserId().toString());
            ImageUtils.setImage(this, fundRaisingModel.getImages(), ivImage, getResources().getDrawable(R.drawable.placeholder_medium));
            tName.setText(fundRaisingModel.getCampTitle());
            tvLocation.setText(fundRaisingModel.getCampAddress() + "," + fundRaisingModel.getLocation());
//            tvDateOfEvent.setText(timeStampToStartCamp(String.valueOf(fundRaisingModel.getCampStartDate())) + "-" + timeStampToEndCamp(String.valueOf(fundRaisingModel.getCampEndDate())));
            tvDateOfEvent.setText(timeStampToEndCamp(String.valueOf(fundRaisingModel.getCampEndDate())));
            tvPriceTarget.setVisibility(View.GONE);
            tvDisPrice.setVisibility(View.GONE);
            tvDisPrice.setText("$" + String.format("%.2f",fundRaisingModel.getFundTarget()));
            tvCollected.setText("$" + fundRaisingModel.getFundRaised());
            tvTarget.setText("$" + fundRaisingModel.getFundTarget());
            tvDescription.setText(fundRaisingModel.getCampDesc());
            checkReadMore();
            lat = fundRaisingModel.getCampLat();
            lng = fundRaisingModel.getCampLng();
            chargeAmountModel.setFundId(fundRaisingModel.getFundId().toString());
            progressAmtDonated.setMax(fundRaisingModel.getFundTarget().intValue());
            progressAmtDonated.setProgress(fundRaisingModel.getFundRaised().intValue());
            setupAdapter();
            downloadUrl = fundRaisingModel.getDownloadUrl();
            if (downloadUrl == null || downloadUrl.isEmpty()) {
                tvDownload.setVisibility(View.GONE);
            }else {
                tvDownload.setVisibility(View.VISIBLE);
            }
        }
        hideProgressDialog();
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        hideProgressDialog();
    }

    @Override
    public void onError(Exception ex) {
        hideProgressDialog();
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    private void backPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_rsvp);
        if (fragment instanceof RSVPFormFragment) {
            getSupportFragmentManager().popBackStack();
            setTitle("Details");
        } else finishAfterTransition();
    }

    @Override
    public void onItemClicked(View view, String s, int pos) {
        switch (view.getId()) {
            case R.id.tv_size:
                sizeChanged(pos);
                break;
            case R.id.image:
                imagesChanges(pos);
                break;
        }
    }

    private void imagesChanges(int pos) {
        setHaderImage(adapterImage.list.get(pos).getImageUrl());
    }

    private void sizeChanged(int pos) {
        if(adapterSize.list.size()>prePos) {
            adapterSize.list.get(prePos).setSelected(false);
        }
        for(int i =0; i<adapterSize.list.size();i++){
            if (i==pos){
                adapterSize.list.get(i).setSelected(true);
            }else {
                adapterSize.list.get(i).setSelected(false);
            }
        }
        chargeAmountModel.setSizeId(adapterSize.list.get(pos).getId().toString());
        chargeAmountModel.setSize(adapterSize.list.get(pos).getSize());
        chargeAmountModel.setAvailableStock(String.valueOf(adapterSize.list.get(pos).getAvailableStock()));
        prePos = pos;
        tvLeft.setText(String.format(getResources().getString(R.string.only_left_in_stock), adapterSize.list.get(pos).getAvailableStock().toString()));
        tvSize.setText(String.format(getResources().getString(R.string.select_size), adapterSize.list.get(pos).getSize()));
        adapterSize.notifyDataSetChanged();
    }

    private void setHaderImage(String image) {
        ImageUtils.setImage(this, image, ivImage, getResources().getDrawable(R.drawable.placeholder_medium));
    }

    @Override
    public void onItemLongClicked(String s, int pos) {

    }

    /*
    Method to check line count snd set read more
     */

    private void checkReadMore(){
        tvDescription.post(() -> {

            int lineCount    = tvDescription.getLineCount();
            if(lineCount >7){
                tvReadMore.setVisibility(View.VISIBLE);
            }else {
                tvReadMore.setVisibility(View.GONE);
            }
        });
    }
}
