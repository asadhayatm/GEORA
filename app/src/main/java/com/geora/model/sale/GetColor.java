
package com.geora.model.sale;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetColor implements Serializable
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
    @SerializedName("total_stock")
    @Expose
    private Integer totalStock;
    @SerializedName("available_stock")
    @Expose
    private Integer availableStock;
    @SerializedName("sale_id")
    @Expose
    private Integer saleId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("get_sizes")
    @Expose
    private List<GetSize> getSizes = null;
    private final static long serialVersionUID = 558726551775680389L;

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

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<GetSize> getGetSizes() {
        return getSizes;
    }

    public void setGetSizes(List<GetSize> getSizes) {
        this.getSizes = getSizes;
    }

}
