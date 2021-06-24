package com.geora.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ChargeAmountModel implements Parcelable {

    private String fundId, amount, campType, busnissUserId,saleId,colorId,addressId,qty,sizeId;
    private String colourCode;
    private String size;
    private String availableStock;

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public String getBusnissUserId() {
        return busnissUserId;
    }

    public void setBusnissUserId(String busnissUserId) {
        this.busnissUserId = busnissUserId;
    }

    public String getFundId() {
        return fundId;
    }

    public String getCampType() {
        return campType;
    }

    public void setCampType(String campType) {
        this.campType = campType;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public ChargeAmountModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fundId);
        dest.writeString(this.amount);
        dest.writeString(this.campType);
        dest.writeString(this.busnissUserId);
        dest.writeString(this.saleId);
        dest.writeString(this.colorId);
        dest.writeString(this.addressId);
        dest.writeString(this.qty);
        dest.writeString(this.sizeId);
        dest.writeString(this.size);
        dest.writeString(this.colourCode);
        dest.writeString(this.availableStock);
    }

    protected ChargeAmountModel(Parcel in) {
        this.fundId = in.readString();
        this.amount = in.readString();
        this.campType = in.readString();
        this.busnissUserId = in.readString();
        this.saleId = in.readString();
        this.colorId = in.readString();
        this.addressId = in.readString();
        this.qty = in.readString();
        this.sizeId = in.readString();
        this.size = in.readString();
        this.colourCode = in.readString();
        this.availableStock = in.readString();
    }

    public static final Creator<ChargeAmountModel> CREATOR = new Creator<ChargeAmountModel>() {
        @Override
        public ChargeAmountModel createFromParcel(Parcel source) {
            return new ChargeAmountModel(source);
        }

        @Override
        public ChargeAmountModel[] newArray(int size) {
            return new ChargeAmountModel[size];
        }
    };

    public void setColourCode(String colourCode) {
        this.colourCode = colourCode;
    }

    public String getColourCode() {
        return colourCode;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setAvailableStock(String availableStock) {
        this.availableStock = availableStock;
    }

    public String getAvailableStock() {
        return availableStock == null ? "0" : availableStock;
    }
}
