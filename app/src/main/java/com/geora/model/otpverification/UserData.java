
package com.geora.model.otpverification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("dob")
    @Expose
    private Long dob;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("google_id")
    @Expose
    private String googleId;
    @SerializedName("facebook_id")
    @Expose
    private String facebookId;
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("registration_mode")
    @Expose
    private String registrationMode;
    @SerializedName("client_secret")
    @Expose
    private String clientSecret;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("user_lat")
    @Expose
    private String userLat;
    @SerializedName("user_lng")
    @Expose
    private String userLng;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("is_verified_email")
    @Expose
    private String isVerifiedEmail;
    @SerializedName("is_verified_phone")
    @Expose
    private String isVerifiedPhone;
    @SerializedName("all_active")
    @Expose
    private Integer allActive;
    @SerializedName("sales_active")
    @Expose
    private Integer salesActive;
    @SerializedName("promotions_active")
    @Expose
    private Integer promotionsActive;
    @SerializedName("fundraising_active")
    @Expose
    private Integer fundraisingActive;
    @SerializedName("event_active")
    @Expose
    private Integer eventActive;
    @SerializedName("is_address_edited")
    @Expose
    private Integer isAddressEdited;
    @SerializedName("notification_subscription_status")
    @Expose
    private String notificationSubscriptionStatus;
    @SerializedName("proximity_range")
    @Expose
    private Integer proximityRange;
    @SerializedName("subscription_id")
    @Expose
    private String subscriptionId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getDob() {
        return dob;
    }

    public void setDob(Long dob) {
        this.dob = dob;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getRegistrationMode() {
        return registrationMode;
    }

    public void setRegistrationMode(String registrationMode) {
        this.registrationMode = registrationMode;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserLat() {
        return userLat;
    }

    public void setUserLat(String userLat) {
        this.userLat = userLat;
    }

    public String getUserLng() {
        return userLng;
    }

    public void setUserLng(String userLng) {
        this.userLng = userLng;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getIsVerifiedEmail() {
        return isVerifiedEmail;
    }

    public void setIsVerifiedEmail(String isVerifiedEmail) {
        this.isVerifiedEmail = isVerifiedEmail;
    }

    public String getIsVerifiedPhone() {
        return isVerifiedPhone;
    }

    public void setIsVerifiedPhone(String isVerifiedPhone) {
        this.isVerifiedPhone = isVerifiedPhone;
    }

    public Integer getAllActive() {
        return allActive;
    }

    public void setAllActive(Integer allActive) {
        this.allActive = allActive;
    }

    public Integer getSalesActive() {
        return salesActive;
    }

    public void setSalesActive(Integer salesActive) {
        this.salesActive = salesActive;
    }

    public Integer getPromotionsActive() {
        return promotionsActive;
    }

    public void setPromotionsActive(Integer promotionsActive) {
        this.promotionsActive = promotionsActive;
    }

    public Integer getFundraisingActive() {
        return fundraisingActive;
    }

    public void setFundraisingActive(Integer fundraisingActive) {
        this.fundraisingActive = fundraisingActive;
    }

    public Integer getEventActive() {
        return eventActive;
    }

    public void setEventActive(Integer eventActive) {
        this.eventActive = eventActive;
    }

    public Integer getIsAddressEdited() {
        return isAddressEdited;
    }

    public void setIsAddressEdited(Integer isAddressEdited) {
        this.isAddressEdited = isAddressEdited;
    }

    public String getNotificationSubscriptionStatus() {
        return notificationSubscriptionStatus;
    }

    public void setNotificationSubscriptionStatus(String notificationSubscriptionStatus) {
        this.notificationSubscriptionStatus = notificationSubscriptionStatus;
    }

    public Integer getProximityRange() {
        return proximityRange;
    }

    public void setProximityRange(Integer proximityRange) {
        this.proximityRange = proximityRange;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

}
