
package com.geora.model.orderresponse;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.geora.model.myactivity.SalesData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable
{

    @SerializedName("current_page")
    @Expose
    private Integer currentPage;
    @SerializedName("data")
    @Expose
    private List<SalesData> data = null;
    @SerializedName("from")
    @Expose
    private Integer from;
    @SerializedName("last_page")
    @Expose
    private Integer lastPage;
    @SerializedName("next_page_url")
    @Expose
    private String nextPageUrl;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("per_page")
    @Expose
    private Integer perPage;
    @SerializedName("prev_page_url")
    @Expose
    private String prevPageUrl;
    @SerializedName("to")
    @Expose
    private Integer to;
    @SerializedName("total")
    @Expose
    private Integer total;
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
        this.currentPage = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.data, (SalesData.class.getClassLoader()));
        this.from = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.lastPage = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.nextPageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.path = ((String) in.readValue((String.class.getClassLoader())));
        this.perPage = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.prevPageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.to = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.total = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public Data() {
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public List<SalesData> getData() {
        return data;
    }

    public void setData(List<SalesData> data) {
        this.data = data;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public String getPrevPageUrl() {
        return prevPageUrl;
    }

    public void setPrevPageUrl(String prevPageUrl) {
        this.prevPageUrl = prevPageUrl;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(currentPage);
        dest.writeList(data);
        dest.writeValue(from);
        dest.writeValue(lastPage);
        dest.writeValue(nextPageUrl);
        dest.writeValue(path);
        dest.writeValue(perPage);
        dest.writeValue(prevPageUrl);
        dest.writeValue(to);
        dest.writeValue(total);
    }

    public int describeContents() {
        return  0;
    }

}
