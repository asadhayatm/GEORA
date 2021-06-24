package com.geora.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CountryCode implements Parcelable {
    private String countryName, countryCode,countryId;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.countryName);
        dest.writeString(this.countryCode);
        dest.writeString(this.countryId);
    }

    public CountryCode() {
    }

    protected CountryCode(Parcel in) {
        this.countryName = in.readString();
        this.countryCode = in.readString();
        this.countryId = in.readString();
    }

    public static final Creator<CountryCode> CREATOR = new Creator<CountryCode>() {
        @Override
        public CountryCode createFromParcel(Parcel source) {
            return new CountryCode(source);
        }

        @Override
        public CountryCode[] newArray(int size) {
            return new CountryCode[size];
        }
    };
}
