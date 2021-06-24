
package com.geora.model.businessuserresponse;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BusinessUserResponse implements Parcelable
{

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("message")
    @Expose
    private String message;
    public final static Creator<BusinessUserResponse> CREATOR = new Creator<BusinessUserResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public BusinessUserResponse createFromParcel(Parcel in) {
            return new BusinessUserResponse(in);
        }

        public BusinessUserResponse[] newArray(int size) {
            return (new BusinessUserResponse[size]);
        }

    }
    ;

    protected BusinessUserResponse(Parcel in) {
        this.success = ((String) in.readValue((String.class.getClassLoader())));
        this.code = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.data = ((Data) in.readValue((Data.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    public BusinessUserResponse() {
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(code);
        dest.writeValue(data);
        dest.writeValue(message);
    }

    public int describeContents() {
        return  0;
    }

}
