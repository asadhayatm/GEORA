
package com.geora.model.beaconlist;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BeaconListModel {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("action_type")
    @Expose
    private String actionType;
    @SerializedName("camp_type")
    @Expose
    private Integer campType;
    @SerializedName("beaconID")
    @Expose
    private Object beaconID;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Integer getCampType() {
        return campType;
    }

    public void setCampType(Integer campType) {
        this.campType = campType;
    }

    public Object getBeaconID() {
        return beaconID;
    }

    public void setBeaconID(Object beaconID) {
        this.beaconID = beaconID;
    }

}
