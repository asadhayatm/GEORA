
package com.geora.model.otpverification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("user_data")
    @Expose
    private UserData userData;

    @SerializedName("isNewUser")
    @Expose
    private boolean isNewUser;

    @SerializedName("is_business_user")
    @Expose
    private Integer isBusinessUser;

    @SerializedName("phone")
    private String phone;

    @SerializedName("country_code")
    private String countryCode;

    @SerializedName("token")
    private String token;

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public boolean isNewUser() {
        return isNewUser;
    }

    public void setNewUser(boolean newUser) {
        isNewUser = newUser;
    }

    public Integer isBusinessUser() {
        return isBusinessUser;
    }

    public void setBusinessUser(Integer businessUser) {
        isBusinessUser = businessUser;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
