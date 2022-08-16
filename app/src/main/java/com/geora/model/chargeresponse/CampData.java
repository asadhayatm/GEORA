
package com.geora.model.chargeresponse;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CampData implements Serializable
{

    @SerializedName("camp_id")
    @Expose
    private Integer campId;
    @SerializedName("camp_title")
    @Expose
    private String campTitle;
    @SerializedName("camp_type")
    @Expose
    private Integer campType;
    @SerializedName("camp_start_date")
    @Expose
    private String campStartDate;
    @SerializedName("camp_end_date")
    @Expose
    private String campEndDate;
    @SerializedName("created_timestamp")
    @Expose
    private Integer createdTimestamp;
    @SerializedName("updated_timestamp")
    @Expose
    private Integer updatedTimestamp;
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
    private Object phoneCode;
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
    @SerializedName("age_group")
    @Expose
    private Integer ageGroup;
    @SerializedName("group_type")
    @Expose
    private Integer groupType;
    @SerializedName("promo_url")
    @Expose
    private Object promoUrl;
    @SerializedName("camp_file")
    @Expose
    private String campFile;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("get_image")
    @Expose
    private List<GetImage> getImage = null;

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

    public String getCampStartDate() {
        return campStartDate;
    }

    public void setCampStartDate(String campStartDate) {
        this.campStartDate = campStartDate;
    }

    public String getCampEndDate() {
        return campEndDate;
    }

    public void setCampEndDate(String campEndDate) {
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

    public Object getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(Object phoneCode) {
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

    public Integer getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(Integer ageGroup) {
        this.ageGroup = ageGroup;
    }

    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    public Object getPromoUrl() {
        return promoUrl;
    }

    public void setPromoUrl(Object promoUrl) {
        this.promoUrl = promoUrl;
    }

    public String getCampFile() {
        return campFile;
    }

    public void setCampFile(String campFile) {
        this.campFile = campFile;
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

    public List<GetImage> getGetImage() {
        return getImage;
    }

    public void setGetImage(List<GetImage> getImage) {
        this.getImage = getImage;
    }


    public Integer getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Integer createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Integer getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(Integer updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }
}
