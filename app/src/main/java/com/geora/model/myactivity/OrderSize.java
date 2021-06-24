
package com.geora.model.myactivity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderSize implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("total_stock")
    @Expose
    private Integer totalStock;
    @SerializedName("available_stock")
    @Expose
    private Integer availableStock;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("skuID")
    @Expose
    private String skuID;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSkuID() {
        return skuID;
    }

    public void setSkuID(String skuID) {
        this.skuID = skuID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.totalStock);
        dest.writeValue(this.availableStock);
        dest.writeString(this.size);
        dest.writeString(this.skuID);
    }

    public OrderSize() {
    }

    protected OrderSize(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalStock = (Integer) in.readValue(Integer.class.getClassLoader());
        this.availableStock = (Integer) in.readValue(Integer.class.getClassLoader());
        this.size = in.readString();
        this.skuID = in.readString();
    }

    public static final Parcelable.Creator<OrderSize> CREATOR = new Parcelable.Creator<OrderSize>() {
        @Override
        public OrderSize createFromParcel(Parcel source) {
            return new OrderSize(source);
        }

        @Override
        public OrderSize[] newArray(int size) {
            return new OrderSize[size];
        }
    };
}
