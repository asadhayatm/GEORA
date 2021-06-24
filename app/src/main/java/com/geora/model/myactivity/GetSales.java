
package com.geora.model.myactivity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSales implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("camp_id")
    @Expose
    private Integer campId;
    @SerializedName("total_stock")
    @Expose
    private Integer totalStock;
    @SerializedName("available_stock")
    @Expose
    private Integer availableStock;
    @SerializedName("product_price")
    @Expose
    private Double productPrice;
    @SerializedName("discounted_price")
    @Expose
    private Double discountedPrice;
    @SerializedName("find_campaign")
    @Expose
    private FindCampaign findCampaign;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCampId() {
        return campId;
    }

    public void setCampId(Integer campId) {
        this.campId = campId;
    }

    public Integer getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Integer totalStock) {
        this.totalStock = totalStock;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(Double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public FindCampaign getFindCampaign() {
        return findCampaign;
    }

    public void setFindCampaign(FindCampaign findCampaign) {
        this.findCampaign = findCampaign;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.campId);
        dest.writeValue(this.totalStock);
        dest.writeValue(this.availableStock);
        dest.writeValue(this.productPrice);
        dest.writeValue(this.discountedPrice);
        dest.writeParcelable(this.findCampaign, flags);
    }

    public GetSales() {
    }

    private GetSales(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.campId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalStock = (Integer) in.readValue(Integer.class.getClassLoader());
        this.availableStock = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productPrice = (Double) in.readValue(Integer.class.getClassLoader());
        this.discountedPrice = (Double) in.readValue(Integer.class.getClassLoader());
        this.findCampaign = in.readParcelable(FindCampaign.class.getClassLoader());
    }

    public static final Parcelable.Creator<GetSales> CREATOR = new Parcelable.Creator<GetSales>() {
        @Override
        public GetSales createFromParcel(Parcel source) {
            return new GetSales(source);
        }

        @Override
        public GetSales[] newArray(int size) {
            return new GetSales[size];
        }
    };
}
