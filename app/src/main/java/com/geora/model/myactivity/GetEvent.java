
package com.geora.model.myactivity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetEvent implements Parcelable {

    @SerializedName("event_id")
    @Expose
    private Integer eventId;
    @SerializedName("camp_id")
    @Expose
    private Integer campId;
    @SerializedName("rsvp_data")
    @Expose
    private String rsvpData;
    @SerializedName("rsvp_type")
    @Expose
    private Integer rsvpType;
    @SerializedName("get_campaign")
    @Expose
    private GetCampaign_ getCampaign;

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getCampId() {
        return campId;
    }

    public void setCampId(Integer campId) {
        this.campId = campId;
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

    public GetCampaign_ getGetCampaign() {
        return getCampaign;
    }

    public void setGetCampaign(GetCampaign_ getCampaign) {
        this.getCampaign = getCampaign;
    }

    public GetEvent() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.eventId);
        dest.writeValue(this.campId);
        dest.writeString(this.rsvpData);
        dest.writeValue(this.rsvpType);
        dest.writeParcelable(this.getCampaign, flags);
    }

    private GetEvent(Parcel in) {
        this.eventId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.campId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.rsvpData = in.readString();
        this.rsvpType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.getCampaign = in.readParcelable(GetCampaign_.class.getClassLoader());
    }

    public static final Parcelable.Creator<GetEvent> CREATOR = new Parcelable.Creator<GetEvent>() {
        @Override
        public GetEvent createFromParcel(Parcel source) {
            return new GetEvent(source);
        }

        @Override
        public GetEvent[] newArray(int size) {
            return new GetEvent[size];
        }
    };
}
