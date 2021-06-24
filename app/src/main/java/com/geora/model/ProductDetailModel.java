package com.geora.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductDetailModel implements Parcelable {
    private String image, name, dec, price, totalPrice, discount;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.image);
        dest.writeString(this.name);
        dest.writeString(this.dec);
        dest.writeString(this.price);
        dest.writeString(this.totalPrice);
        dest.writeString(this.discount);
    }

    public ProductDetailModel() {
    }

    protected ProductDetailModel(Parcel in) {
        this.image = in.readString();
        this.name = in.readString();
        this.dec = in.readString();
        this.price = in.readString();
        this.totalPrice = in.readString();
        this.discount = in.readString();
    }

    public static final Creator<ProductDetailModel> CREATOR = new Creator<ProductDetailModel>() {
        @Override
        public ProductDetailModel createFromParcel(Parcel source) {
            return new ProductDetailModel(source);
        }

        @Override
        public ProductDetailModel[] newArray(int size) {
            return new ProductDetailModel[size];
        }
    };

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
