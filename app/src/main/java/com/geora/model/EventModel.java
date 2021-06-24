
package com.geora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventModel {

    @SerializedName("beacon_id")
    @Expose
    private String beaconId;
    @SerializedName("camp_id")
    @Expose
    private Integer campId;
    @SerializedName("camp_expire")
    @Expose
    private Integer camp_expire;
    @SerializedName("camp_title")
    @Expose
    private String campTitle;
    @SerializedName("camp_type")
    @Expose
    private Integer campType;
    @SerializedName("camp_start_date")
    @Expose
    private Integer campStartDate;
    @SerializedName("camp_end_date")
    @Expose
    private Integer campEndDate;
    @SerializedName("camp_city")
    @Expose
    private Integer campCity;
    @SerializedName("camp_state")
    @Expose
    private String campState;
    @SerializedName("camp_address")
    @Expose
    private String campAddress;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("camp_contact")
    @Expose
    private String campContact;
    @SerializedName("phone_code")
    @Expose
    private Integer phoneCode;
    @SerializedName("camp_lat")
    @Expose
    private Double campLat;
    @SerializedName("camp_lng")
    @Expose
    private Double campLng;
    @SerializedName("camp_desc")
    @Expose
    private String campDesc;
    @SerializedName("business_user_id")
    @Expose
    private Integer businessUserId;
    @SerializedName("camp_status")
    @Expose
    private Integer campStatus;
    @SerializedName("camp_sub_category_id")
    @Expose
    private Integer campSubCategoryId;
    @SerializedName("default_image_id")
    @Expose
    private Integer defaultImageId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("images")
    @Expose
    private String images;
    @SerializedName("all_images")
    @Expose
    private String allImages;
    @SerializedName("event_id")
    @Expose
    private Integer eventId;
    @SerializedName("rsvp_data")
    @Expose
    private String rsvpData;
    @SerializedName("download_url")
    @Expose
    private String downloadUrl;
    @SerializedName("rsvp_type")
    @Expose
    private Integer rsvpType;

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public Integer getCampId() {
        return campId;
    }

    public void setCampId(Integer campId) {
        this.campId = campId;
    }

    public String getCampTitle() {
        return campTitle;
    }

    public void setCampTitle(String campTitle) {
        this.campTitle = campTitle;
    }

    public Integer getCampType() {
        return campType;
    }

    public void setCampType(Integer campType) {
        this.campType = campType;
    }

    public Integer getCampStartDate() {
        return campStartDate;
    }

    public void setCampStartDate(Integer campStartDate) {
        this.campStartDate = campStartDate;
    }

    public Integer getCampEndDate() {
        return campEndDate;
    }

    public void setCampEndDate(Integer campEndDate) {
        this.campEndDate = campEndDate;
    }

    public Integer getCampCity() {
        return campCity;
    }

    public void setCampCity(Integer campCity) {
        this.campCity = campCity;
    }

    public String getCampState() {
        return campState;
    }

    public void setCampState(String campState) {
        this.campState = campState;
    }

    public String getCampAddress() {
        return campAddress;
    }

    public void setCampAddress(String campAddress) {
        this.campAddress = campAddress;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCampContact() {
        return campContact;
    }

    public void setCampContact(String campContact) {
        this.campContact = campContact;
    }

    public Integer getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(Integer phoneCode) {
        this.phoneCode = phoneCode;
    }

    public Double getCampLat() {
        return campLat;
    }

    public void setCampLat(Double campLat) {
        this.campLat = campLat;
    }

    public Double getCampLng() {
        return campLng;
    }

    public void setCampLng(Double campLng) {
        this.campLng = campLng;
    }

    public String getCampDesc() {
        return campDesc;
    }

    public void setCampDesc(String campDesc) {
        this.campDesc = campDesc;
    }

    public Integer getBusinessUserId() {
        return businessUserId;
    }

    public void setBusinessUserId(Integer businessUserId) {
        this.businessUserId = businessUserId;
    }

    public Integer getCampStatus() {
        return campStatus;
    }

    public void setCampStatus(Integer campStatus) {
        this.campStatus = campStatus;
    }

    public Integer getCampSubCategoryId() {
        return campSubCategoryId;
    }

    public void setCampSubCategoryId(Integer campSubCategoryId) {
        this.campSubCategoryId = campSubCategoryId;
    }

    public Integer getDefaultImageId() {
        return defaultImageId;
    }

    public void setDefaultImageId(Integer defaultImageId) {
        this.defaultImageId = defaultImageId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getAllImages() {
        return allImages;
    }

    public void setAllImages(String allImages) {
        this.allImages = allImages;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getRsvpData() {
        return rsvpData;
    }

    public void setRsvpData(String rsvpData) {
        this.rsvpData = rsvpData;
    }

    public Integer getRsvpType() {
        return rsvpType;
    }

    public void setRsvpType(Integer rsvpType) {
        this.rsvpType = rsvpType;
    }

    public Integer getCamp_expire() {
        return camp_expire;
    }

    public void setCamp_expire(Integer camp_expire) {
        this.camp_expire = camp_expire;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
