
package com.geora.model.myactivity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderColors implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("color_name")
    @Expose
    private String colorName;
    @SerializedName("color_hex_code")
    @Expose
    private String colorHexCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getColorHexCode() {
        return colorHexCode;
    }

    public void setColorHexCode(String colorHexCode) {
        this.colorHexCode = colorHexCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.colorName);
        dest.writeString(this.colorHexCode);
    }

    public OrderColors() {
    }

    protected OrderColors(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.colorName = in.readString();
        this.colorHexCode = in.readString();
    }

    public static final Parcelable.Creator<OrderColors> CREATOR = new Parcelable.Creator<OrderColors>() {
        @Override
        public OrderColors createFromParcel(Parcel source) {
            return new OrderColors(source);
        }

        @Override
        public OrderColors[] newArray(int size) {
            return new OrderColors[size];
        }
    };
}
