
package com.geora.model.myactivity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFund implements Parcelable {

    @SerializedName("fund_id")
    @Expose
    private Integer fundId;
    @SerializedName("camp_id")
    @Expose
    private Integer campId;
    @SerializedName("fund_target")
    @Expose
    private Integer fundTarget;
    @SerializedName("fund_raised")
    @Expose
    private Double fundRaised;
    @SerializedName("fund_type")
    @Expose
    private Integer fundType;
    @SerializedName("get_campaign")
    @Expose
    private GetCampaign getCampaign;

    public Integer getFundId() {
        return fundId;
    }

    public void setFundId(Integer fundId) {
        this.fundId = fundId;
    }

    public Integer getCampId() {
        return campId;
    }

    public void setCampId(Integer campId) {
        this.campId = campId;
    }

    public Integer getFundTarget() {
        return fundTarget;
    }

    public void setFundTarget(Integer fundTarget) {
        this.fundTarget = fundTarget;
    }

    public Double getFundRaised() {
        return fundRaised;
    }

    public void setFundRaised(Double fundRaised) {
        this.fundRaised = fundRaised;
    }

    public Integer getFundType() {
        return fundType;
    }

    public void setFundType(Integer fundType) {
        this.fundType = fundType;
    }

    public GetCampaign getGetCampaign() {
        return getCampaign;
    }

    public void setGetCampaign(GetCampaign getCampaign) {
        this.getCampaign = getCampaign;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.fundId);
        dest.writeValue(this.campId);
        dest.writeValue(this.fundTarget);
        dest.writeValue(this.fundRaised);
        dest.writeValue(this.fundType);
        dest.writeParcelable(this.getCampaign, flags);
    }

    public GetFund() {
    }

    protected GetFund(Parcel in) {
        this.fundId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.campId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fundTarget = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fundRaised = (Double) in.readValue(Integer.class.getClassLoader());
        this.fundType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.getCampaign = in.readParcelable(GetCampaign.class.getClassLoader());
    }

    public static final Parcelable.Creator<GetFund> CREATOR = new Parcelable.Creator<GetFund>() {
        @Override
        public GetFund createFromParcel(Parcel source) {
            return new GetFund(source);
        }

        @Override
        public GetFund[] newArray(int size) {
            return new GetFund[size];
        }
    };
}
