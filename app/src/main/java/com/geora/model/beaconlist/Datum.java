
package com.geora.model.beaconlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("beacon_id")
    @Expose
    private String beaconId;
    @SerializedName("business_user_id")
    @Expose
    private Integer businessUserId;

    @SerializedName("business_user_name")
    @Expose
    private String businessUserName;
    @SerializedName("beacon_title")
    @Expose
    private String beaconTitle;
    @SerializedName("beacon_location")
    @Expose
    private String beaconLocation;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("validate_date")
    @Expose
    private Object validateDate;

    public String getBusinessUserName() {
        return businessUserName;
    }

    public void setBusinessUserName(String businessUserName) {
        this.businessUserName = businessUserName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public Integer getBusinessUserId() {
        return businessUserId;
    }

    public void setBusinessUserId(Integer businessUserId) {
        this.businessUserId = businessUserId;
    }

    public String getBeaconTitle() {
        return beaconTitle;
    }

    public void setBeaconTitle(String beaconTitle) {
        this.beaconTitle = beaconTitle;
    }

    public String getBeaconLocation() {
        return beaconLocation;
    }

    public void setBeaconLocation(String beaconLocation) {
        this.beaconLocation = beaconLocation;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Object getValidateDate() {
        return validateDate;
    }

    public void setValidateDate(Object validateDate) {
        this.validateDate = validateDate;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
