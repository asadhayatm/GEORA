
package com.geora.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiFailureResponse implements Parcelable
{

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private FailureResponse.Data data;
    public final static Creator<ApiFailureResponse> CREATOR = new Creator<ApiFailureResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ApiFailureResponse createFromParcel(Parcel in) {
            return new ApiFailureResponse(in);
        }

        public ApiFailureResponse[] newArray(int size) {
            return (new ApiFailureResponse[size]);
        }

    }
    ;

    protected ApiFailureResponse(Parcel in) {
        this.success = ((String) in.readValue((String.class.getClassLoader())));
        this.code = ((int) in.readValue((int.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.data = ((FailureResponse.Data) in.readValue((FailureResponse.Data.class.getClassLoader())));
    }

    public ApiFailureResponse() {
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FailureResponse.Data getData() {
        return data;
    }

    public void setData(FailureResponse.Data data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(code);
        dest.writeValue(message);
        dest.writeValue(data);
    }

    public int describeContents() {
        return  0;
    }

}
