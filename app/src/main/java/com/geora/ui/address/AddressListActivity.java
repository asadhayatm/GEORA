package com.geora.ui.address;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseActivity;
import com.geora.customviews.DialogForConfirmation;
import com.geora.customviews.DialogMoreOption;
import com.geora.data.constants.AppConstantClass;
import com.geora.listeners.DialogCallback;
import com.geora.listeners.RecycleListener;
import com.geora.model.ChargeAmountModel;
import com.geora.model.FailureResponse;
import com.geora.model.ProductDetailModel;
import com.geora.model.ResetPasswordModel;
import com.geora.model.addresslist.AddressListModel;
import com.geora.model.addresslist.Datum;
import com.geora.model.signup.SignupModel;
import com.geora.ui.addaddress.AddAddressActivity;
import com.geora.ui.checkout.CheckoutActivity;
import com.geora.util.AppUtils;
;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.geora.GeoraClass.getContext;

public class AddressListActivity extends BaseActivity implements RecycleListener {
    @BindView(R.id.rv_address_list)
    RecyclerView rvAddressList;
    @BindView(R.id.fl_add_address)
    FrameLayout flAddAddress;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rl_no_address)
    RelativeLayout rlNoAddress;
    @BindView(R.id.sr)
    SwipeRefreshLayout sr;
    @BindView(R.id.main)
    RelativeLayout main;

    private List<Datum> addressList;
    private AddressListAdapter adapter;
    private Unbinder unbinder;
    private AddressListViewModel mAddressListViewModel;
    private int pos;
    private String data = "";
    private int screen;
    private ProductDetailModel model;
    private ChargeAmountModel chargeAmountModel;
    private int type = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        unbinder = ButterKnife.bind(this);
        screen = getIntent().getIntExtra(AppConstantClass.DATA, 1);
        addressList = new LinkedList<>();
        if (screen == 1)
            tvTitle.setText(getResources().getString(R.string.my_address));
        else {
            chargeAmountModel = getIntent().getParcelableExtra(AppConstantClass.CHARGEDATA);
            model = getIntent().getParcelableExtra(AppConstantClass.PRODUCTDATA);
            tvTitle.setText(getResources().getString(R.string.select_address));
        }

        setUpRecyclerView();
        swipeListener();
        mAddressListViewModel = ViewModelProviders.of(this).get(AddressListViewModel.class);
        mAddressListViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        showProgressDialog();
        mAddressListViewModel.getAddressListLiveData().observe(this, new Observer<AddressListModel>() {
            @Override
            public void onChanged(@Nullable final AddressListModel addressListModel) {
                hideProgressDialog();
                sr.setRefreshing(false);
                if (addressListModel != null) {
                    if (AppUtils.checkUserValid(AddressListActivity.this, addressListModel.getCode(), addressListModel.getMessage())) {
                        addressList.clear();
                        addressList.addAll(addressListModel.getData());
                        adapter.notifyDataSetChanged();
                        if (addressList.size() == 0) {
                            rlNoAddress.setVisibility(View.VISIBLE);
                            rvAddressList.setVisibility(View.GONE);
                        } else {
                            rlNoAddress.setVisibility(View.GONE);
                            rvAddressList.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
        mAddressListViewModel.getDefaultAddressLiveData().observe(this, new Observer<SignupModel>() {
            @Override
            public void onChanged(@Nullable final SignupModel addressListModel) {
                if (AppUtils.checkUserValid(AddressListActivity.this, addressListModel.getCode(), addressListModel.getMessage())) {
                    setUpRecyclerView();
                    mAddressListViewModel.hitGetListingApi(isInternetAvailable(), 1);
                    hideProgressDialog();
              /*  if (addressListModel != null) {
                    try {
                        adapter.list.get(adapter.defaultCardPos).setDeliveryStatus(0);
                        adapter.list.get(pos).setDeliveryStatus(1);
                    } catch (Exception e) {
                    }
                    adapter.notifyDataSetChanged();
                }*/
                }
            }
        });
        mAddressListViewModel.getDeleteAddressLiveData().observe(this, new Observer<ResetPasswordModel>() {
            @Override
            public void onChanged(@Nullable final ResetPasswordModel addressListModel) {
                hideProgressDialog();
                if (addressListModel != null) {
                    if (AppUtils.checkUserValid(AddressListActivity.this, addressListModel.getCode(), addressListModel.getMessage())) {
                        showSnackBar(main, getResources().getString(R.string.address_has_been_deleted), false);
                        setUpRecyclerView();
                        mAddressListViewModel.hitGetListingApi(isInternetAvailable(), 1);

                        /////
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAddressListViewModel != null) {
            mAddressListViewModel.hitGetListingApi(isInternetAvailable(), 1);
        }
    }

    private void swipeListener() {
        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sr.setRefreshing(false);
                mAddressListViewModel.hitGetListingApi(isInternetAvailable(), 1);
            }
        });
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_address_list;
    }

    // setting country code data into recyclerview
    private void setUpRecyclerView() {
        adapter = new AddressListAdapter(addressList, getContext(), screen);
        rvAddressList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter.notifyDataSetChanged();
        rvAddressList.setAdapter(adapter);
        adapter.setClickListener(this);
    }

    @Override
    public void onItemClicked(View view, String s, final int pos) {
        this.pos = pos;
        if (view.getId() == R.id.iv_more) {
            new DialogMoreOption(this, new DialogCallback() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onSubmit(View view, String result, int s) {
                    if (result.equalsIgnoreCase(getResources().getString(R.string.set_default))) {
                        mAddressListViewModel.hitDefaultApi(isInternetAvailable(), String.valueOf(addressList.get(pos).getId()));
                    } else if (result.equalsIgnoreCase(getResources().getString(R.string.delete))) {
                        openConfirmationDialog();
                    } else if (result.equalsIgnoreCase(getResources().getString(R.string.edit))) {
                        openAddActivityForEdit(pos);
                    }
                }
            }, AddressListActivity.class.getSimpleName(), pos == adapter.defaultCardPos,type).show();
        } else if (view.getId() == R.id.tv_deliver) {
            if (addressList.get(pos).getAddressType() != null && !addressList.get(pos).getAddressType().equals("")) {
                moveToCheckoutScreen(pos);
            }else {
                openAddActivityForEdit(pos);
            }

        }else if(view.getId() == R.id.iv_selected){
            updateDeliveryAddress(pos);
        }
    }

    private void updateDeliveryAddress(int pos){
        for(int i =0; i<addressList.size();i++){
            if(i==pos){
                addressList.get(i).setDeliveryStatus(1);
            }else {
                addressList.get(i).setDeliveryStatus(0);
            }
        }
        adapter.notifyDataSetChanged();
    }


    //show confirmation dialog to delete  address from the list
    private void openConfirmationDialog() {
        new DialogForConfirmation(this, new DialogCallback() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSubmit(View view, String result, int s) {
                if (result.equalsIgnoreCase(getString(R.string.s_yes))) {
                    showProgressDialog();
                    mAddressListViewModel.hitDeleteAddress(isInternetAvailable(), String.valueOf(addressList.get(pos).getId()));
                }
            }
        }, getString(R.string.s_are_you_sure_you_want_to_delete_this_adress)).show();
    }

    private void openAddActivityForEdit(int pos) {
        Intent intent = new Intent(this, AddAddressActivity.class);
        intent.putExtra(AppConstantClass.APIConstant.ADDRESS, addressList.get(pos));
        intent.putExtra(AppConstantClass.APIConstant.TYPE, "edit");
        startActivityForResult(intent, 111);
    }

    @Override
    public void onItemLongClicked(String s, int pos) {

    }

    @OnClick({R.id.fl_add_address, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_add_address:
                startActivityForResult(new Intent(this, AddAddressActivity.class), 111);
                break;
            case R.id.iv_back:
                finishAfterTransition();
                break;
        }
    }

    private void moveToCheckoutScreen(int pos) {
        chargeAmountModel.setAddressId(addressList.get(pos).getId().toString());
        Datum data = adapter.list.get(adapter.defaultCardPos);
        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra(AppConstantClass.APIConstant.ADDRESS, data);
        intent.putExtra(AppConstantClass.PRODUCTDATA, model);
        intent.putExtra(AppConstantClass.CHARGEDATA, chargeAmountModel);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && mAddressListViewModel != null) {
            sr.setRefreshing(true);
            mAddressListViewModel.hitGetListingApi(isInternetAvailable(), 1);
        }
    }


    @Override
    protected void onFailure(FailureResponse failureResponse) {
        super.onFailure(failureResponse);
        //showSnackBar(main, failureResponse.getErrorMessage().toString(), true);
        sr.setRefreshing(false);
        if (failureResponse.getErrorCode() == 422) {
            showToastLong(getResources().getString(R.string.something_went_wrong));
//            logout(this);
        }
        if (failureResponse.getErrorCode() == 401) {
            showToastLong(getResources().getString(R.string.user_is_blocked_by_admin));
            logout(this);
        }

    }

    @Override
    protected void onErrorOccurred(Throwable throwable) {
        super.onErrorOccurred(throwable);
        //showSnackBar(main, throwable.getMessage().toString(), true);
        sr.setRefreshing(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
