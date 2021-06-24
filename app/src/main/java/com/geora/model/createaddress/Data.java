
package com.geora.model.createaddress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("all_active")
    @Expose
    private String allActive;
    @SerializedName("sales_active")
    @Expose
    private String salesActive;
    @SerializedName("promotions_active")
    @Expose
    private String promotionsActive;
    @SerializedName("fundraising_active")
    @Expose
    private String fundraisingActive;
    @SerializedName("event_active")
    @Expose
    private String eventActive;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("is_address_edited")
    @Expose
    private String isAddressEdited;

    public String getAllActive() {
        return allActive;
    }

    public void setAllActive(String allActive) {
        this.allActive = allActive;
    }

    public String getSalesActive() {
        return salesActive;
    }

    public void setSalesActive(String salesActive) {
        this.salesActive = salesActive;
    }

    public String getPromotionsActive() {
        return promotionsActive;
    }

    public void setPromotionsActive(String promotionsActive) {
        this.promotionsActive = promotionsActive;
    }

    public String getFundraisingActive() {
        return fundraisingActive;
    }

    public void setFundraisingActive(String fundraisingActive) {
        this.fundraisingActive = fundraisingActive;
    }

    public String getEventActive() {
        return eventActive;
    }

    public void setEventActive(String eventActive) {
        this.eventActive = eventActive;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsAddressEdited() {
        return isAddressEdited;
    }

    public void setIsAddressEdited(String isAddressEdited) {
        this.isAddressEdited = isAddressEdited;
    }

}
