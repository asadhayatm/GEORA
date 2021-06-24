
package com.geora.model.chargeresponse;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderColors implements Serializable
{

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

}
