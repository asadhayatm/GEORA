package com.geora.model.signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("delivery_status")
    @Expose
    private Integer deliveryStatus;
    @SerializedName("logout_status")
    @Expose
    private String logoutStatus;

    @SerializedName("proximity_range")
    @Expose
    private String proximityRange;

    @SerializedName("notification_subscription_status")
    @Expose
    private String notificationSubscriptionStatus;

    @SerializedName("is_business_user")
    @Expose
    private Integer isBusinessUser;


    public String getLogoutStatus() {
        return logoutStatus;
    }

    public void setLogoutStatus(String logoutStatus) {
        this.logoutStatus = logoutStatus;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(Integer deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getProximityRange() {
        return proximityRange;
    }

    public void setProximityRange(String proximityRange) {
        this.proximityRange = proximityRange;
    }
    public String getNotificationSubscriptionStatus() {
        return notificationSubscriptionStatus;
    }

    public void setNotificationSubscriptionStatus(String notificationSubscriptionStatus) {
        this.notificationSubscriptionStatus = notificationSubscriptionStatus;
    }

    public Integer isBusinessUser() {
        return isBusinessUser;
    }

    public void setBusinessUser(Integer businessUser) {
        isBusinessUser = businessUser;
    }
}
