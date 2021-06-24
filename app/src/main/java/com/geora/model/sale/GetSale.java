
package com.geora.model.sale;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSale implements Serializable
{

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
    @SerializedName("sale_type")
    @Expose
    private Integer saleType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("get_color")
    @Expose
    private List<GetColor> getColor = null;
    private final static long serialVersionUID = 7154043413176821843L;

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

    public Integer getSaleType() {
        return saleType;
    }

    public void setSaleType(Integer saleType) {
        this.saleType = saleType;
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

    public List<GetColor> getGetColor() {
        return getColor;
    }

    public void setGetColor(List<GetColor> getColor) {
        this.getColor = getColor;
    }

}
