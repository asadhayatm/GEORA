
package com.geora.model.businessuserresponse;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable
{

    @SerializedName("is_business_user")
    @Expose
    private Integer isBusinessUser;
    public final static Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    }
    ;

    protected Data(Parcel in) {
        this.isBusinessUser = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public Data() {
    }

    public Integer getIsBusinessUser() {
        return isBusinessUser;
    }

    public void setIsBusinessUser(Integer isBusinessUser) {
        this.isBusinessUser = isBusinessUser;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(isBusinessUser);
    }

    public int describeContents() {
        return  0;
    }

}
