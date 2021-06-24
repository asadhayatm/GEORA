
package com.geora.model.myactivity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCampaign implements Parcelable {

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
    @SerializedName("get_image")
    @Expose
    private List<GetImage_> getImage = null;
    @SerializedName("get_default_image")
    @Expose
    private GetDefaultImage_ getDefaultImage;

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

    public List<GetImage_> getGetImage() {
        return getImage;
    }

    public void setGetImage(List<GetImage_> getImage) {
        this.getImage = getImage;
    }

    public GetDefaultImage_ getGetDefaultImage() {
        return getDefaultImage;
    }

    public void setGetDefaultImage(GetDefaultImage_ getDefaultImage) {
        this.getDefaultImage = getDefaultImage;
    }

    public GetCampaign() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.campId);
        dest.writeString(this.campTitle);
        dest.writeValue(this.campType);
        dest.writeValue(this.campStartDate);
        dest.writeValue(this.campEndDate);
        dest.writeValue(this.campCity);
        dest.writeString(this.campState);
        dest.writeString(this.campAddress);
        dest.writeString(this.location);
        dest.writeString(this.campContact);
        dest.writeValue(this.phoneCode);
        dest.writeValue(this.campLat);
        dest.writeValue(this.campLng);
        dest.writeString(this.campDesc);
        dest.writeValue(this.businessUserId);
        dest.writeValue(this.campStatus);
        dest.writeValue(this.campSubCategoryId);
        dest.writeValue(this.defaultImageId);
        dest.writeList(this.getImage);
        dest.writeParcelable(this.getDefaultImage, flags);
    }

    protected GetCampaign(Parcel in) {
        this.campId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.campTitle = in.readString();
        this.campType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.campStartDate = (Integer) in.readValue(Integer.class.getClassLoader());
        this.campEndDate = (Integer) in.readValue(Integer.class.getClassLoader());
        this.campCity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.campState = in.readString();
        this.campAddress = in.readString();
        this.location = in.readString();
        this.campContact = in.readString();
        this.phoneCode = (Integer) in.readValue(Integer.class.getClassLoader());
        this.campLat = (Double) in.readValue(Double.class.getClassLoader());
        this.campLng = (Double) in.readValue(Double.class.getClassLoader());
        this.campDesc = in.readString();
        this.businessUserId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.campStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.campSubCategoryId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.defaultImageId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.getImage = new ArrayList<GetImage_>();
        in.readList(this.getImage, GetImage_.class.getClassLoader());
        this.getDefaultImage = in.readParcelable(GetDefaultImage_.class.getClassLoader());
    }

    public static final Parcelable.Creator<GetCampaign> CREATOR = new Parcelable.Creator<GetCampaign>() {
        @Override
        public GetCampaign createFromParcel(Parcel source) {
            return new GetCampaign(source);
        }

        @Override
        public GetCampaign[] newArray(int size) {
            return new GetCampaign[size];
        }
    };
}
