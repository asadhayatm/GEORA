package com.geora.ui.DetailsActivity.OrderDetail;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseFragment;
import com.geora.model.chargeresponse.Data;
import com.geora.model.myactivity.SalesData;
import com.geora.ui.DetailsActivity.MyDetailsActivity;
import com.geora.util.DateTimeUtils;
import com.geora.util.ImageUtils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailFragment extends BaseFragment {


    private Unbinder unbinder;
    private Context context;
    private SalesData salesData = new SalesData();
    Data data = new Data();
    @BindView(R.id.tv_address_data)
    TextView tvAddress;
    @BindView(R.id.tv_total_price_data)
    TextView tvTotalPriceData;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_paid_amount_data)
    TextView tvPaidAmountData;
    @BindView(R.id.tv_order_name)
    TextView tvOrderName;
    @BindView(R.id.tv_order_no_data)
    TextView tvOrderNoData;
    @BindView(R.id.tv_placed_on_data)
    TextView tvPlacedOnData;
    @BindView(R.id.iv_order_image)
    ImageView ivOrderImage;
    @BindView(R.id.tv_order_size)
    TextView tvOrderSize;
    @BindView(R.id.tv_color)
    TextView tvColor;
    @BindView(R.id.iv_color_image)
    ImageView ivColorImage;
    @BindView(R.id.tv_order_size_value)
    TextView tvOrderSizeValue;
    @BindView(R.id.tv_phone_no)
    TextView tvPhoneNo;
    @BindView(R.id.tv_person_name)
    TextView tvPersonName;
    @BindView(R.id.tv_address_type)
    TextView tvAddressType;
    @BindView(R.id.tv_discount_price_data)
    TextView tvDiscountPriceData;
    @BindView(R.id.tv_discount_price)
    TextView tvDiscountPrice;
    @BindView(R.id.tv_quantity)
    TextView tvQuantity;
    @BindView(R.id.tv_order_status_data)
    TextView tvOrderStatusData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = getActivity();
        int id = 0;
        Bundle bundle = ((MyDetailsActivity) Objects.requireNonNull(getActivity())).getBundle();
        id = bundle.getInt("id", 0);
        if (id == 1) {
            data = (Data) bundle.getSerializable("object");
            if (data != null) {
                settingDetails();
            }
        } else {
            salesData = bundle.getParcelable("object");
            if (salesData != null) {
                settingViews();
            }
        }

        return view;
    }

    //setting the details
    private void settingDetails() {
        //use data variable
        tvAddressType.setText(data.getDeliveryAddress().getAddressType());
        tvPersonName.setText(data.getDeliveryAddress().getFullName());
        tvAddress.setText(data.getDeliveryAddress().getFormattedAddress());
        tvPhoneNo.setText(data.getDeliveryAddress().getCountryCode() + "-" + data.getDeliveryAddress().getMobileNo());
        if (data.getOrderColors() != null) {
            ivColorImage.setVisibility(View.VISIBLE);
            tvColor.setVisibility(View.VISIBLE);
            ivColorImage.setColorFilter(android.graphics.Color.parseColor(data.getOrderColors().getColorHexCode()));
        } else {
            ivColorImage.setVisibility(View.GONE);
            tvColor.setVisibility(View.GONE);
        }
        if (data.getOrderedSize() != null) {
            tvOrderSize.setVisibility(View.VISIBLE);
            tvOrderSizeValue.setVisibility(View.VISIBLE);
            tvOrderSizeValue.setText(data.getOrderedSize().getSize());
        } else {
            tvOrderSizeValue.setVisibility(View.GONE);
            tvOrderSize.setVisibility(View.GONE);
        }
        tvTotalPrice.setText("Total Price(" + data.getQty().toString() + " item)");
        tvOrderNoData.setText(data.getId().toString());
        tvOrderName.setText(data.getCampData().getCampTitle());
        if (data.getCampData() != null && data.getCampData().getGetImage() != null && data.getCampData().getGetImage().size() > 0)
            ImageUtils.setImage(getContext(), data.getCampData().getGetImage().get(0).getImageUrl(), ivOrderImage, getResources().getDrawable(R.drawable.placeholder_medium));
        if (data.getProductPrices().getDiscountedPrice() != null) {
            tvDiscountPriceData.setText(getString(R.string.doller) + String.format("%.2f",(data.getProductPrices().getProductPrice() - data.getProductPrices().getDiscountedPrice()) * Integer.parseInt(data.getQty())));
            tvDiscountPriceData.setVisibility(View.VISIBLE);
            tvDiscountPrice.setVisibility(View.VISIBLE);
        }else {
            tvDiscountPriceData.setText(getString(R.string.doller) + 0.00);
            tvDiscountPriceData.setVisibility(View.GONE);
            tvDiscountPrice.setVisibility(View.GONE);
        }
        tvTotalPriceData.setText(getString(R.string.doller) + String.format("%.2f",data.getProductPrices().getProductPrice() * Integer.parseInt(data.getQty())));
        tvPlacedOnData.setText(DateTimeUtils.orderDateFormat(data.getCreatedAt()));
        if (data.getProductPrices().getDiscountedPrice() != null) {
            tvPaidAmountData.setText(getString(R.string.doller) + String.format("%.2f",data.getProductPrices().getDiscountedPrice() * Integer.parseInt(data.getQty())));
        }else {
            tvPaidAmountData.setText(getString(R.string.doller) + String.format("%.2f",data.getProductPrices().getProductPrice() * Integer.parseInt(data.getQty())));
        }
        tvQuantity.setText(getString(R.string.quantity) + ": " + data.getQty());
        String status = getString(R.string.pending);
        switch (data.getOrderStatus()) {
            case 1:
                status = getString(R.string.pending);
                break;
            case 2:
                status = getString(R.string.processing);
                break;
            case 3:
                status = getString(R.string.out_for_delivery);
                break;
            case 4:
                status = getString(R.string.delivered);
                break;
        }
        tvOrderStatusData.setText(status);

    }

    //setting the views
    private void settingViews() {

        tvPersonName.setText(salesData.getFullName());
        tvAddressType.setText(salesData.getAddressType());
        tvAddress.setText(salesData.getFormattedAddress());
        tvPhoneNo.setText(salesData.getCountryCode() + "-" + salesData.getMobileNumber());
        if (salesData.getOrderSize() != null) {
            if (salesData.getOrderSize().getSize() != null && !salesData.getOrderSize().getSize().equalsIgnoreCase("")) {
                tvOrderSize.setVisibility(View.VISIBLE);
                tvOrderSizeValue.setVisibility(View.VISIBLE);
                tvOrderSizeValue.setText(salesData.getOrderSize().getSize());
            } else {
                tvOrderSizeValue.setVisibility(View.GONE);
                tvOrderSize.setVisibility(View.GONE);
            }
        } else {
            tvOrderSizeValue.setVisibility(View.GONE);
            tvOrderSize.setVisibility(View.GONE);
        }
        tvOrderName.setText(salesData.getGetSales().getFindCampaign().getCampTitle());
        tvTotalPrice.setText("Total Price(" + salesData.getQty().toString() + "item)");
        //tvOrderSizeColor.setText(salesData.getOrderColors().getColorName());
        tvOrderNoData.setText(salesData.getId().toString());
        ImageUtils.setImage(getContext(), salesData.getGetSales().getFindCampaign().getGetDefaultImage().getImageUrl(), ivOrderImage, getResources().getDrawable(R.drawable.placeholder_medium));
        if (salesData.getGetSales().getDiscountedPrice() != null) {
            tvDiscountPriceData.setText(getString(R.string.doller) + String.format("%.2f",(salesData.getGetSales().getProductPrice() - salesData.getGetSales().getDiscountedPrice()) * salesData.getQty()));
            tvDiscountPriceData.setVisibility(View.VISIBLE);
            tvDiscountPrice.setVisibility(View.VISIBLE);
        }else {
            tvDiscountPriceData.setText(getString(R.string.doller) + 0.00);
            tvDiscountPriceData.setVisibility(View.GONE);
            tvDiscountPrice.setVisibility(View.GONE);
        }
        tvTotalPriceData.setText(getString(R.string.doller) + String.format("%.2f",salesData.getGetSales().getProductPrice() * salesData.getQty()));
        tvPlacedOnData.setText(DateTimeUtils.timeStampToEndCamp(salesData.getPaymentDate().toString()));
        if (salesData.getOrderColors() != null) {
            if (salesData.getOrderColors().getColorHexCode() != null && !salesData.getOrderColors().getColorHexCode().equalsIgnoreCase("")) {
                ivColorImage.setVisibility(View.VISIBLE);
                tvColor.setVisibility(View.VISIBLE);
                ivColorImage.setColorFilter(android.graphics.Color.parseColor(salesData.getOrderColors().getColorHexCode()));
            } else {
                ivColorImage.setVisibility(View.GONE);
                tvColor.setVisibility(View.GONE);
            }
        } else {
            ivColorImage.setVisibility(View.GONE);
            tvColor.setVisibility(View.GONE);
        }
       /* if (salesData.getGetSales().getDiscountedPrice() > 0)
            tvDiscountPriceData.setText(getString(R.string.doller) + (salesData.getGetSales().getDiscountedPrice() * salesData.getQty()));
        else
            tvDiscountPriceData.setText(getString(R.string.doller) + "0");*/
        if (salesData.getGetSales().getDiscountedPrice() != null) {
            tvPaidAmountData.setText(getString(R.string.doller) + String.format("%.2f",salesData.getGetSales().getDiscountedPrice() * salesData.getQty()));
        }else {
            tvPaidAmountData.setText(getString(R.string.doller) + String.format("%.2f",salesData.getGetSales().getProductPrice() * salesData.getQty()));
        }
        tvQuantity.setText(getString(R.string.quantity) + ": " + salesData.getQty());
        String status = getString(R.string.pending);
        switch (salesData.getOrderStatus()) {
            case 1:
                status = getString(R.string.pending);
                break;
            case 2:
                status = getString(R.string.processing);
                break;
            case 3:
                status = getString(R.string.out_for_delivery);
                break;
            case 4:
                status = getString(R.string.delivered);
                break;
        }
        tvOrderStatusData.setText(status);
    }

}
