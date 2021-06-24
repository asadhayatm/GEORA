
package com.geora.model.myactivity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FundData implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("fund_id")
    @Expose
    private Integer fundId;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("payment_log")
    @Expose
    private String paymentLog;
    @SerializedName("payment_date")
    @Expose
    private Integer paymentDate;
    @SerializedName("get_fund")
    @Expose
    private GetFund getFund;

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

    public Integer getFundId() {
        return fundId;
    }

    public void setFundId(Integer fundId) {
        this.fundId = fundId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
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
        return paymentDate == null ? 0 : paymentDate;
    }

    public void setPaymentDate(Integer paymentDate) {
        this.paymentDate = paymentDate;
    }

    public GetFund getGetFund() {
        return getFund;
    }

    public void setGetFund(GetFund getFund) {
        this.getFund = getFund;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.userId);
        dest.writeValue(this.fundId);
        dest.writeValue(this.amount);
        dest.writeString(this.paymentStatus);
        dest.writeString(this.transactionId);
        dest.writeString(this.paymentLog);
        dest.writeValue(this.paymentDate);
        dest.writeParcelable(this.getFund, flags);
    }

    public FundData() {
    }

    protected FundData(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fundId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.amount = (Double) in.readValue(Integer.class.getClassLoader());
        this.paymentStatus = in.readString();
        this.transactionId = in.readString();
        this.paymentLog = in.readString();
        this.paymentDate = (Integer) in.readValue(Integer.class.getClassLoader());
        this.getFund = in.readParcelable(GetFund.class.getClassLoader());
    }

    public static final Parcelable.Creator<FundData> CREATOR = new Parcelable.Creator<FundData>() {
        @Override
        public FundData createFromParcel(Parcel source) {
            return new FundData(source);
        }

        @Override
        public FundData[] newArray(int size) {
            return new FundData[size];
        }
    };
}
