package com.geora.ui.cardlist;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseActivity;
import com.geora.constants.AppConstants;
import com.geora.customviews.DialogForConfirmation;
import com.geora.customviews.DialogForPaymentSuccessfull;
import com.geora.customviews.DialogMoreOption;
import com.geora.data.constants.AppConstantClass;
import com.geora.listeners.DialogCallback;
import com.geora.listeners.RecycleListener;
import com.geora.model.ChargeAmountModel;
import com.geora.model.FailureResponse;
import com.geora.model.ResetPasswordModel;
import com.geora.model.cardlist.CardListModel;
import com.geora.model.cardlist.Datum;
import com.geora.model.changred.FundraisedChangeModel;
import com.geora.model.chargeresponse.ChargeResponse;
import com.geora.model.chargeresponse.Data;
import com.geora.ui.DetailsActivity.MyDetailsActivity;
import com.geora.ui.addcard.AddCardActivity;
import com.geora.ui.home.HomeActivity;
import com.geora.util.AppUtils;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.geora.GeoraClass.getContext;

public class CardListActivity extends BaseActivity implements RecycleListener {
    @BindView(R.id.rv_card_list)
    RecyclerView rvCardList;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fl_add_card)
    FrameLayout flAddCard;
    @BindView(R.id.fl_add)
    FrameLayout flAdd;
    @BindView(R.id.rl_no_card)
    RelativeLayout rlNoCard;
    @BindView(R.id.sr)
    SwipeRefreshLayout sr;
    @BindView(R.id.iv_no_data)
    ImageView ivNoData;
    @BindView(R.id.tv_title_card)
    TextView tvTitleCard;
    @BindView(R.id.tv_des)
    TextView tvDes;
    @BindView(R.id.tv_new_card)
    TextView tvNewCard;
    @BindView(R.id.main)
    RelativeLayout main;

    private Unbinder unbinder;
    private List<Datum> cardList;
    private CardListAdapter adapter;
    private PaymentViewModel mPaymentViewModel;
    private int action = 0;// 1--> for delete   2--> for default
    private int pos;
    private String amount, campType;
    private ChargeAmountModel chargeAmountModel;
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        unbinder = ButterKnife.bind(this);
        chargeAmountModel = getIntent().getParcelableExtra(AppConstantClass.CHARGEDATA);

        getDataFromChargeModel();
        setPayNoewButton();
        tvTitle.setText(getResources().getString(R.string.payment));
        cardList = new LinkedList<>();
        setNoData();
        setUpRecyclerView();

        setObserveres();

        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                sr.setRefreshing(false);
                mPaymentViewModel.hitGetListingApi(isInternetAvailable(), 1);

            }
        });
    }

    private void setObserveres() {
        mPaymentViewModel = ViewModelProviders.of(this).get(PaymentViewModel.class);
        mPaymentViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        mPaymentViewModel.hitGetListingApi(isInternetAvailable(), 1);
        showProgressDialog();
        mPaymentViewModel.getCardListLiveData().observe(this, new Observer<CardListModel>() {
            @Override
            public void onChanged(@Nullable final CardListModel cardListModel) {
                hideProgressDialog();
                if (AppUtils.checkUserValid(CardListActivity.this, cardListModel.getCode(), cardListModel.getMessage())) {
                    sr.setRefreshing(false);

                    if (cardListModel != null) {
                        cardList.clear();
                        adapter.notifyDataSetChanged();

                        cardList.addAll(cardListModel.getData());
                        adapter.notifyDataSetChanged();
                        if (cardListModel.getData().size() == 0 && chargeAmountModel == null) {
                            rlNoCard.setVisibility(View.VISIBLE);
                            rvCardList.setVisibility(View.GONE);
                        } else {
                            rlNoCard.setVisibility(View.GONE);
                            rvCardList.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        mPaymentViewModel.getDefaultCardLiveData().

                observe(this, new Observer<ResetPasswordModel>() {
                    @Override
                    public void onChanged(@Nullable final ResetPasswordModel cardListModel) {
                        hideProgressDialog();
                        if (cardListModel != null) {
                            if (action == 2) {
                                adapter.list.get(adapter.defaultCardPos).setDefaultSource(false);
                                adapter.list.get(pos).setDefaultSource(true);
                                adapter.defaultCardPos = pos;
                                adapter.notifyDataSetChanged();

                            } else if (action == 1) {
                                showSnackBar(main, getResources().getString(R.string.card_has_been_deleted_successfully), false);
                                adapter.list.remove(pos);
                                adapter.notifyDataSetChanged();
                                if (adapter.list.size() == 0 && (amount == null || amount.isEmpty())) {
                                    rvCardList.setVisibility(View.GONE);
                                    rlNoCard.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    }
                });

        mPaymentViewModel.getFundRaisedCharhedLiveData().
                observe(this, new Observer<FundraisedChangeModel>() {
                    @Override
                    public void onChanged(@Nullable final FundraisedChangeModel cardListModel) {
                        hideProgressDialog();
                        paymentSuccessful(1, null);
                    }
                });

        mPaymentViewModel.getChargeResponseLiveData().
                observe(this, new Observer<ChargeResponse>() {
                    @Override
                    public void onChanged(@Nullable final ChargeResponse cardListModel) {
                        hideProgressDialog();
                        paymentSuccessful(2, cardListModel);
                    }
                });
    }

    private void getDataFromChargeModel() {
        if (chargeAmountModel != null) {
            amount = chargeAmountModel.getAmount();
            campType = chargeAmountModel.getCampType();
        }
    }

    // checking to pay or add
    private void setPayNoewButton() {
        if (amount != null && !amount.isEmpty())
            changeButtonToPay();
    }

    // chnage add button to pay button and show new add card button
    private void changeButtonToPay() {
        flAddCard.setBackground(getDrawable(R.drawable.yellow_oval));
        tvNewCard.setTextColor(getResources().getColor(R.color.black));
        tvNewCard.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        tvNewCard.setText(String.format(getResources().getString(R.string.pay), String.format("%.2f",Double.parseDouble(amount))));
        tvNewCard.setText("$"+String.format("%.2f",Double.parseDouble(amount)));
        flAdd.setVisibility(View.VISIBLE);
        rlNoCard.setVisibility(View.GONE);

    }


    // setting no data data
    private void setNoData() {
        ivNoData.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_card_graphic));
        tvTitleCard.setText(getResources().getString(R.string.no_saved_cards));
        tvDes.setText(getResources().getString(R.string.don_t_worry_add_card_now_or_add_it_later_during_payment_process));
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_card_list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @OnClick({R.id.fl_add, R.id.fl_add_card, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_add_card:
                if (amount == null || amount.isEmpty())
                    addCard();
                else
                    payClicked();
                break;
            case R.id.fl_add:
                addCard();
                break;
            case R.id.iv_back:
                finishAfterTransition();
                break;
        }
    }

    private void payClicked() {
        if (isInternetAvailable())
            chargeAmountFromUser();
    }

    private void chargeAmountFromUser() {
        if (adapter.list.size() == 0) {
            showSnackBar(main, "Please Add card first", true);
        }
        else {
            showProgressDialog();
            mPaymentViewModel.hitChargeData(chargeAmountModel);
        }
    }

    private void paymentSuccessful(final int type, final ChargeResponse cardListModel) {
        new DialogForPaymentSuccessfull(this, getString(type == 1 ? R.string.thank_you_for_donation : R.string.thank_you_for_payment_your_order_has_been_successfully_placed_with_us),  new DialogCallback() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSubmit(View view, String result, int s) {
                switch (type) {
                    case 1:
                        Intent intent = new Intent(CardListActivity.this, HomeActivity.class);
                        intent.putExtra(AppConstantClass.FROM, "CardListFund");
                        startActivity(intent);
                        finishAfterTransition();
                        break;
                    case 2:
                        Intent mIntent = new Intent(CardListActivity.this, MyDetailsActivity.class);
                        mIntent.putExtra(AppConstants.HomeListViewConstants.ID, AppConstants.HomeListViewConstants.ORDERS_FRAGMENT);
                        mIntent.putExtra(AppConstants.HomeListViewConstants.TITLE, AppConstants.HomeListViewConstants.ORDER_DETAILS);
                        Bundle bundle = new Bundle();
                        Data salesData = cardListModel.getData();
                        bundle.putSerializable("object", salesData);
                        bundle.putInt("id", 1);
                        mIntent.putExtra("data", bundle);
                        startActivityForResult(mIntent, AppConstantClass.PAYMENT_REQUEST);
                        break;
                }
            }
        }).show();
    }

    // starting add card API
    private void addCard() {
        startActivityForResult(new Intent(this, AddCardActivity.class), 101);
    }


    // setting data into recyclerview
    private void setUpRecyclerView() {
        adapter = new CardListAdapter(cardList, getContext());
        rvCardList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter.notifyDataSetChanged();
        rvCardList.setAdapter(adapter);
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
                        makeCardDefault(pos);
                    } else if (result.equalsIgnoreCase(getResources().getString(R.string.delete))) {
                        openConfirmationDialog();
                    }
                }
            }, CardListActivity.class.getSimpleName(), pos == adapter.defaultCardPos,type).show();
        }

    }

    //show confirmation dialog to delete the card
    private void openConfirmationDialog() {
        new DialogForConfirmation(this, new DialogCallback() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSubmit(View view, String result, int s) {
                if (result.equalsIgnoreCase(getString(R.string.s_yes))) {
                    deleteCard(pos);
                }
            }
        }, getString(R.string.s_are_you_sure_you_want_to_delete_this_card)).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && mPaymentViewModel != null) {
            sr.setRefreshing(true);
            mPaymentViewModel.hitGetListingApi(isInternetAvailable(), 1);
        }
        if (requestCode == AppConstantClass.PAYMENT_REQUEST) {
            Intent intent = new Intent(CardListActivity.this, HomeActivity.class);
            intent.putExtra(AppConstantClass.FROM, "CardListPay");
            startActivity(intent);
            finishAfterTransition();
        }
    }

    private void deleteCard(int pos) {
        if (isInternetAvailable()) {
            showProgressDialog();
            action = 1;
            mPaymentViewModel.hitDegaultCardApi(cardList.get(pos).getId(), true);
        } else {
            showSnackBar(main, getResources().getString(R.string.no_internet_connection), true);
        }
    }

    private void makeCardDefault(int pos) {
        if (isInternetAvailable()) {
            showProgressDialog();
            action = 2;
            mPaymentViewModel.hitDegaultCardApi(cardList.get(pos).getId(), false);
        } else {
            showSnackBar(main, getResources().getString(R.string.no_internet_connection), true);
        }
    }

    @Override
    public void onItemLongClicked(String s, int pos) {

    }

    @Override
    protected void onErrorOccurred(Throwable throwable) {
        super.onErrorOccurred(throwable);
        sr.setRefreshing(false);
        hideProgressDialog();
        //showSnackBar(main, throwable.getMessage(), true);
    }

    @Override
    protected void onFailure(FailureResponse failureResponse) {
        super.onFailure(failureResponse);
        sr.setRefreshing(false);
        hideProgressDialog();
        //showSnackBar(main, failureResponse.getErrorMessage().toString(), true);
        if (failureResponse.getErrorCode() == 422) {
            showToastLong(getResources().getString(R.string.something_went_wrong));
//            logout(this);
        }
        if (failureResponse.getErrorCode() == 401) {
            showToastLong(getResources().getString(R.string.user_is_blocked_by_admin));
            logout(this);
        }
    }


}
