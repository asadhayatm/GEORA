package com.dnitinverma.amazons3library.interfaces;



import com.dnitinverma.amazons3library.model.MediaBean;

/**
 * Created by Rajat on 21-02-2017.
 */

public interface AmazonCallback {

    void uploadSuccess( MediaBean bean);
    void uploadFailed(MediaBean bean);
    void uploadProgress( MediaBean bean);
    void uploadError(Exception e, MediaBean imageBean);
}
