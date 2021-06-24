
package com.geora.model.sale;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSize implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("color_id")
    @Expose
    private Integer colorId;
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
    private final static long serialVersionUID = 7669385877643708900L;


    private  boolean isSelected;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getColorId() {
        return colorId;
    }

    public void setColorId(Integer colorId) {
        this.colorId = colorId;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
