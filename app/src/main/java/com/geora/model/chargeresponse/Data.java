
package com.geora.model.chargeresponse;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable
{

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("sales_id")
    @Expose
    private String salesId;
    @SerializedName("color_id")
    @Expose
    private String colorId;
    @SerializedName("size_id")
    @Expose
    private String sizeId;
    @SerializedName("business_user_id")
    @Expose
    private String businessUserId;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("delivery_id")
    @Expose
    private String deliveryId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("order_status")
    @Expose
    private int orderStatus;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("delivery_address")
    @Expose
    private DeliveryAddress deliveryAddress;
    @SerializedName("order_colors")
    @Expose
    private OrderColors orderColors;
    @SerializedName("ordered_size")
    @Expose
    private OrderedSize orderedSize;
    @SerializedName("product_prices")
    @Expose
    private ProductPrices productPrices;
    @SerializedName("camp_data")
    @Expose
    private CampData campData;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public String getBusinessUserId() {
        return businessUserId;
    }

    public void setBusinessUserId(String businessUserId) {
        this.businessUserId = businessUserId;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public OrderColors getOrderColors() {
        return orderColors;
    }

    public void setOrderColors(OrderColors orderColors) {
        this.orderColors = orderColors;
    }

    public OrderedSize getOrderedSize() {
        return orderedSize;
    }

    public void setOrderedSize(OrderedSize orderedSize) {
        this.orderedSize = orderedSize;
    }

    public ProductPrices getProductPrices() {
        return productPrices;
    }

    public void setProductPrices(ProductPrices productPrices) {
        this.productPrices = productPrices;
    }

    public CampData getCampData() {
        return campData;
    }

    public void setCampData(CampData campData) {
        this.campData = campData;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}
