package com.dnitinverma.amazons3library.model;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;

/**
 * Created by appinventiv on 7/9/17.
 */

public class MediaBean {
    private String name;
    private int progress = 0;
    private TransferObserver mObserver;
    private String serverUrl = "";
    private String isSucess = "0";
    private String mediaPath;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public TransferObserver getmObserver() {
        return mObserver;
    }

    public void setmObserver(TransferObserver mObserver) {
        this.mObserver = mObserver;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
