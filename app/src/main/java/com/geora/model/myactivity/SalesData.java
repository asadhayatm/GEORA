
package com.geora.model.myactivity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalesData implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("sales_id")
    @Expose
    private Integer salesId;
    @SerializedName("color_id")
    @Expose
    private Integer colorId;
    @SerializedName("size_id")
    @Expose
    private Integer sizeId;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("formattedAddress")
    @Expose
    private String formattedAddress;
    @SerializedName("address_type")
    @Expose
    private String addressType;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNumber;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("delivery_end_date")
    @Expose
    private Integer deliveryEndDate;
    @SerializedName("qty")
    @Expose
    private Integer qty;

    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("payment_log")
    @Expose
    private String paymentLog;
    @SerializedName("payment_date")
    @Expose
    private Integer paymentDate;
    @SerializedName("order_status")
    @Expose
    private int orderStatus;
    @SerializedName("get_sales")
    @Expose
    private GetSales getSales;
    @SerializedName("order_colors")
    @Expose
    private OrderColors orderColors;
    @SerializedName("order_size")
    @Expose
    private OrderSize orderSize;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSalesId() {
        return salesId;
    }

    public void setSalesId(Integer salesId) {
        this.salesId = salesId;
    }

    public Integer getColorId() {
        return colorId;
    }

    public void setColorId(Integer colorId) {
        this.colorId = colorId;
    }

    public Integer getSizeId() {
        return sizeId;
    }

    public void setSizeId(Integer sizeId) {
        this.sizeId = sizeId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentLog() {
        return paymentLog;
    }

    public void setPaymentLog(String paymentLog) {
        this.paymentLog = paymentLog;
    }

    public Integer getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Integer paymentDate) {
        this.paymentDate = paymentDate;
    }

    public GetSales getGetSales() {
        return getSales;
    }

    public void setGetSales(GetSales getSales) {
        this.getSales = getSales;
    }

    public OrderColors getOrderColors() {
        return orderColors;
    }

    public void setOrderColors(OrderColors orderColors) {
        this.orderColors = orderColors;
    }

    public OrderSize getOrderSize() {
        return orderSize;
    }

    public void setOrderSize(OrderSize orderSize) {
        this.orderSize = orderSize;
    }

    public void setFormattedAddress(String formattedAddress){
        this.formattedAddress = formattedAddress;
    }
    public String getFormattedAddress(){
        return formattedAddress;
    }
    public void setAddressType(String addressType){
        this.addressType = addressType;
    }
    public String getAddressType(){
        return addressType;
    }
    public void setCountryCode(String countryCode){
        this.countryCode = countryCode;
    }
    public String getCountryCode(){
        return countryCode;
    }
    public void setMobileNumber(String mobileNumber){
        this.mobileNumber = mobileNumber;
    }
    public String getMobileNumber(){
        return mobileNumber;
    }
    public void setDeliveryEndDate(Integer deliveryEndDate){
        this.deliveryEndDate = deliveryEndDate;
    }
    public Integer getDeliveryEndDate(){
        return deliveryEndDate;
    }
    public void setQty(Integer qty){
        this.qty = qty;
    }
    public Integer getQty(){
        return qty;
    }

    public void setFullName(String fullName){
        this.fullName = fullName;
    }
    public String getFullName(){
        return fullName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.userId);
        dest.writeValue(this.salesId);
        dest.writeValue(this.colorId);
        dest.writeValue(this.sizeId);
        dest.writeValue(this.amount);
        dest.writeString(this.paymentStatus);
        dest.writeString(this.transactionId);
        dest.writeString(this.paymentLog);
        dest.writeValue(this.paymentDate);
        dest.writeValue(this.orderStatus);
        dest.writeParcelable(this.getSales, flags);
        dest.writeParcelable(this.orderColors, flags);
        dest.writeParcelable(this.orderSize, flags);
        dest.writeString(this.formattedAddress);
        dest.writeString(this.addressType);
        dest.writeString(this.countryCode);
        dest.writeString(this.mobileNumber);
        dest.writeValue(this.deliveryEndDate);
        dest.writeValue(this.qty);
        dest.writeString(this.fullName);
    }

    public SalesData() {
    }

    protected SalesData(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.salesId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.colorId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.sizeId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.amount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.paymentStatus = in.readString();
        this.transactionId = in.readString();
        this.paymentLog = in.readString();
        this.paymentDate = (Integer) in.readValue(Integer.class.getClassLoader());
        this.orderStatus = (int) in.readValue(Integer.class.getClassLoader());
        this.getSales = in.readParcelable(GetSales.class.getClassLoader());
        this.orderColors = in.readParcelable(OrderColors.class.getClassLoader());
        this.orderSize = in.readParcelable(OrderSize.class.getClassLoader());
        this.formattedAddress = in.readString();
        this.addressType = in.readString();
        this.countryCode = in.readString();
        this.mobileNumber = in.readString();
        this.deliveryEndDate = (Integer) in.readValue(Integer.class.getClassLoader());
        this.qty = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fullName = in.readString();
    }

    public static final Parcelable.Creator<SalesData> CREATOR = new Parcelable.Creator<SalesData>() {
        @Override
        public SalesData createFromParcel(Parcel source) {
            return new SalesData(source);
        }

        @Override
        public SalesData[] newArray(int size) {
            return new SalesData[size];
        }
    };

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}
