package com.geora.ui.checkout;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseActivity;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.ChargeAmountModel;
import com.geora.model.ProductDetailModel;
import com.geora.model.addresslist.Datum;
import com.geora.ui.cardlist.CardListActivity;
import com.geora.util.ImageUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class CheckoutActivity extends BaseActivity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_size)
    TextView tvSize;
    @BindView(R.id.tv_size_value)
    TextView tvSizeValue;
    @BindView(R.id.tv_des)
    TextView tvDes;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.tv_minus)
    TextView tvMinus;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_paid_amount)
    TextView tvPaidAmount;
    @BindView(R.id.tv_total_price_data)
    TextView tvTotalPriceData;
    @BindView(R.id.tv_paid_amount_data)
    TextView tvPaidAmountData;
    @BindView(R.id.tv_person_name)
    TextView tvPersonName;
    @BindView(R.id.tv_address_type)
    TextView tvAddressType;
    @BindView(R.id.tv_address_data)
    TextView tvAddressData;
    @BindView(R.id.tv_phone_no)
    TextView tvPhoneNo;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.tv_product_image)
    ImageView tvProductImage;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_color)
    TextView tvColor;
    @BindView(R.id.tv_read_more)
    TextView tvReadMore;
    @BindView(R.id.tv_total_discount)
    TextView tvTotalDiscount;
    @BindView(R.id.tv_discount)
    TextView tvDiscount;

    @BindView(R.id.iv_product_color)
    ImageView ivProductColor;

    private ProductDetailModel model;
    private Datum address;
    private int noOfItem = 1;
    private double amount;
    private ChargeAmountModel chargeAmountModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = getIntent().getParcelableExtra(AppConstantClass.PRODUCTDATA);
        address = getIntent().getParcelableExtra(AppConstantClass.APIConstant.ADDRESS);
        chargeAmountModel = getIntent().getParcelableExtra(AppConstantClass.CHARGEDATA);
        tvTitle.setText("Checkout");
        setProductInfor();
        setAddressData();
        setSizeAndColorData();

    }

    private void setSizeAndColorData(){
        if(chargeAmountModel.getSize() != null){
            if(!chargeAmountModel.getSize().equalsIgnoreCase("")){
                tvSize.setVisibility(View.VISIBLE);
                tvSizeValue.setVisibility(View.VISIBLE);
                tvSizeValue.setText(chargeAmountModel.getSize());
            }else {
                tvSize.setVisibility(View.GONE);
                tvSizeValue.setVisibility(View.GONE);
            }

        }else {
            tvSize.setVisibility(View.GONE);
            tvSizeValue.setVisibility(View.GONE);
        }
        if(chargeAmountModel.getColourCode() != null){
            if(!chargeAmountModel.getColourCode().equalsIgnoreCase("")){
                tvColor.setVisibility(View.VISIBLE);
                ivProductColor.setVisibility(View.VISIBLE);
                ivProductColor.setColorFilter(android.graphics.Color.parseColor(chargeAmountModel.getColourCode()));

            }else {
                tvColor.setVisibility(View.GONE);
                ivProductColor.setVisibility(View.GONE);
            }
        }else {
            tvColor.setVisibility(View.GONE);
            ivProductColor.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.tv_minus, R.id.tv_plus, R.id.tv_next,R.id.iv_back,R.id.tv_read_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_minus:

                if (noOfItem > 1) {
                    noOfItem--;
                }
                setDataAccordingToNoOfItem();
                break;
            case R.id.tv_plus:
                if(chargeAmountModel.getAvailableStock() != null){
                    if(!chargeAmountModel.getAvailableStock().equalsIgnoreCase("")){
                        if(Integer.parseInt(chargeAmountModel.getAvailableStock()) > noOfItem){
                            noOfItem++;
                            setDataAccordingToNoOfItem();
                        }
                    }
                }

                break;
            case R.id.tv_next:
                moveToCardListActivity(amount);
                break;
            case R.id.iv_back:
                finishAfterTransition();
                break;
            case R.id.tv_read_more:
                if(tvReadMore.getText().toString().equalsIgnoreCase("Read More")) {
                    tvDes.setMaxLines(1000);
                    tvReadMore.setText("Read Less");
                }else {
                    tvDes.setMaxLines(2);
                    tvReadMore.setText("Read More");
                }
                    break;
        }
    }

    private void setDataAccordingToNoOfItem() {
        tvNo.setText(noOfItem + "");
        tvPaidAmountData.setText(("$" + amount * noOfItem) + "");
        tvTotalPrice.setText("Total Price (" + noOfItem + " item)");
        if (model != null) {
            tvTotalPriceData.setText("$" + (Double.parseDouble(model.getTotalPrice().replace("$", "")) * noOfItem));
            if (model.getDiscount() != null && !model.getDiscount().replace("$", "").equals("")
                    && Double.parseDouble(model.getDiscount().replace("$", "")) != 0) {
                tvTotalDiscount.setText("$" + (Double.parseDouble(model.getDiscount().replace("$", "")) * noOfItem));
                tvDiscount.setVisibility(View.VISIBLE);
                tvTotalDiscount.setVisibility(View.VISIBLE);
            }else {
                tvDiscount.setVisibility(View.GONE);
                tvTotalDiscount.setVisibility(View.GONE);
            }
        }
    }

    private void moveToCardListActivity(double amount) {
        chargeAmountModel.setQty(noOfItem + "");
        chargeAmountModel.setAmount((amount * noOfItem) + "");
        Intent intent = new Intent(this, CardListActivity.class);
        intent.putExtra(AppConstantClass.CHARGEDATA, chargeAmountModel);
        startActivity(intent);
    }

    private void setProductInfor() {
        if (model != null) {
            amount = Double.parseDouble(model.getPrice().replace("$", ""));
            tvName.setText(model.getName());
            tvDes.setText(model.getDec());
            checkReadMore();
            tvTotalPriceData.setText(model.getTotalPrice());
            if (model.getDiscount() != null && !model.getDiscount().equals("") && Double.parseDouble(model.getDiscount()) != 0) {
                tvTotalDiscount.setText("$" + String.format("%.2f",Double.parseDouble(model.getDiscount().replace("$",""))));
                tvDiscount.setVisibility(View.VISIBLE);
                tvTotalDiscount.setVisibility(View.VISIBLE);
            }else {
                tvDiscount.setVisibility(View.GONE);
                tvTotalDiscount.setVisibility(View.GONE);
            }
            tvPaidAmountData.setText("$"+String.format("%.2f",Double.parseDouble(model.getPrice().replace("$",""))));
            ImageUtils.setImage(this, model.getImage(), tvProductImage, null);
        }
    }

    private void setAddressData() {
        if (address != null) {
            tvPersonName.setText(address.getFullName());
            tvAddressType.setText(address.getAddressType());
            tvAddressData.setText(address.getFormattedAddress());
            tvPhoneNo.setText(  address.getCountryCode() + "-" + address.getMobileNo());

        }
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_checkout;
    }

     /*
    Method to check line count snd set read more
     */

    private void checkReadMore(){
        tvDes.post(new Runnable() {

            @Override
            public void run() {

                int lineCount    = tvDes.getLineCount();
                if(lineCount >2){
                    tvReadMore.setVisibility(View.VISIBLE);
                }else {
                    tvReadMore.setVisibility(View.GONE);
                }
            }
        });
    }
}
