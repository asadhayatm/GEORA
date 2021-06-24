
package com.geora.model.addresslist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("formattedAddress")
    @Expose
    private String formattedAddress;
    @SerializedName("flat_no")
    @Expose
    private String flatNo;
    @SerializedName("stree_name")
    @Expose
    private String streeName;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("pin_code")
    @Expose
    private Integer pinCode;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("address_type")
    @Expose
    private String addressType;
    @SerializedName("delivery_status")
    @Expose
    private Integer deliveryStatus;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getStreeName() {
        return streeName;
    }

    public void setStreeName(String streeName) {
        this.streeName = streeName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getPinCode() {
        return pinCode;
    }

    public void setPinCode(Integer pinCode) {
        this.pinCode = pinCode;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public Integer getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(Integer deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.userId);
        dest.writeString(this.formattedAddress);
        dest.writeString(this.flatNo);
        dest.writeString(this.streeName);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeValue(this.pinCode);
        dest.writeString(this.lat);
        dest.writeString(this.lng);
        dest.writeString(this.fullName);
        dest.writeString(this.countryCode);
        dest.writeString(this.mobileNo);
        dest.writeString(this.addressType);
        dest.writeValue(this.deliveryStatus);
        dest.writeString(this.status);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
    }

    public Datum() {
    }

    protected Datum(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.formattedAddress = in.readString();
        this.flatNo = in.readString();
        this.streeName = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.pinCode = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lat = in.readString();
        this.lng = in.readString();
        this.fullName = in.readString();
        this.countryCode = in.readString();
        this.mobileNo = in.readString();
        this.addressType = in.readString();
        this.deliveryStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.status = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
    }

    public static final Creator<Datum> CREATOR = new Creator<Datum>() {
        @Override
        public Datum createFromParcel(Parcel source) {
            return new Datum(source);
        }

        @Override
        public Datum[] newArray(int size) {
            return new Datum[size];
        }
    };
}
