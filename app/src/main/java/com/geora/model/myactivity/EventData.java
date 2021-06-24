
package com.geora.model.myactivity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventData implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("event_id")
    @Expose
    private Integer eventId;
    @SerializedName("subscription_data")
    @Expose
    private String subscriptionData;
    @SerializedName("subscription_date")
    @Expose
    private Integer subscriptionDate;
    @SerializedName("get_event")
    @Expose
    private GetEvent getEvent;

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

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getSubscriptionData() {
        return subscriptionData;
    }

    public void setSubscriptionData(String subscriptionData) {
        this.subscriptionData = subscriptionData;
    }

    public Integer getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(Integer subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public GetEvent getGetEvent() {
        return getEvent;
    }

    public void setGetEvent(GetEvent getEvent) {
        this.getEvent = getEvent;
    }

    public EventData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.userId);
        dest.writeValue(this.eventId);
        dest.writeString(this.subscriptionData);
        dest.writeValue(this.subscriptionDate);
        dest.writeParcelable(this.getEvent,flags);
    }

    private EventData(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.eventId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.subscriptionData = in.readString();
        this.subscriptionDate = (Integer) in.readValue(Integer.class.getClassLoader());
        this.getEvent = in.readParcelable(GetEvent.class.getClassLoader());
    }

    public static final Parcelable.Creator<EventData> CREATOR = new Parcelable.Creator<EventData>() {
        @Override
        public EventData createFromParcel(Parcel source) {
            return new EventData(source);
        }

        @Override
        public EventData[] newArray(int size) {
            return new EventData[size];
        }
    };
}
