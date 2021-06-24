package com.dnitinverma.amazons3library;

import android.app.Activity;
import android.content.Context;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.MultiObjectDeleteException;
import com.dnitinverma.amazons3library.imageutils.ImageCompressor;
import com.dnitinverma.amazons3library.interfaces.AmazonCallback;
import com.dnitinverma.amazons3library.model.MediaBean;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by appinventiv on 7/9/17.
 */

public class AmazonS3 {
    private Activity mActivity;
    private AmazonCallback amazonCallback;
    private TransferUtility mTransferUtility;


    /*
     *  Initialize activity instance
     */
    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    /*
    *  Initialize AmazonS3 callback
    */
    public void setCallback(AmazonCallback amazonCallback) {
        this.amazonCallback = amazonCallback;
    }


    /*
    * Upload video, image and other type file on amazon s3
    * */
    public void upload(final MediaBean mediaBean) {
        File file = new File(mediaBean.getMediaPath());
        if (file.exists()) {
            mTransferUtility = AmazonUtils.getTransferUtility(mActivity);
            if (new ImageFileFilter().accept(file)) {
                ImageCompressor.getDefault(mActivity)
                        .compressToFileAsObservable(file)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<File>() {
                            @Override
                            public void call(File file) {
                                uploadFileOnAmazon(mediaBean, file);
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {

                            }
                        });
            } else
                uploadFileOnAmazon(mediaBean, file);
        } else
            amazonCallback.uploadFailed(mediaBean);
    }

    /**
     * Method to delete the object
     *
     * @param context         context
     * @param fileS3ObjectKey file object to be deleted
     */
    public void deleteFileFromS3(Context context, String fileS3ObjectKey, String versionId) {
        try {
            AmazonS3Client s3client = AmazonUtils.getS3Client(context);
            s3client.deleteObject(new DeleteObjectRequest(AmazonS3Constants.BUCKET, versionId + "_" + fileS3ObjectKey));
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to delete multiple amazon s3 file objects
     *
     * @param context            context
     * @param objectKeyNamesList list of object key names to be deleted from the bucket
     */
    public void deleteMultipleFilesFromS3(Context context, List<String> objectKeyNamesList) {
        try {
            DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(AmazonS3Constants.BUCKET);
            AmazonS3Client s3client = AmazonUtils.getS3Client(context);
            List<DeleteObjectsRequest.KeyVersion> keys = new LinkedList<>();

            for (String objectKeyName :
                    objectKeyNamesList) {
                keys.add(new DeleteObjectsRequest.KeyVersion(objectKeyName));
            }

            multiObjectDeleteRequest.setKeys(keys);

            DeleteObjectsResult delObjRes = s3client.deleteObjects(multiObjectDeleteRequest);
            System.out.format("Successfully deleted all the %s items.\n",
                    delObjRes.getDeletedObjects().size());

        } catch (MultiObjectDeleteException e) {
            for (MultiObjectDeleteException.DeleteError deleteError : e.getErrors()) {
                // Process exception.
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to upload image on amazon.
     *
     * @param mediaBean
     * @param file
     */

    private void uploadFileOnAmazon(MediaBean mediaBean, File file) {
        TransferObserver observer;
        observer = mTransferUtility.upload(AmazonS3Constants.BUCKET, file.getName(), file, CannedAccessControlList.PublicRead);
        observer.setTransferListener(new UploadListener(mediaBean));
        mediaBean.setmObserver(observer);
    }


    public void cancelUploadToS3(MediaBean mediaBean) {
        mTransferUtility.cancel(mediaBean.getId());
    }

    public void pauseUploadToS3(MediaBean mediaBean) {
        mTransferUtility.pause(mediaBean.getId());
    }

    public void resumeUploadToS3(MediaBean mediaBean) {
        mTransferUtility.resume(mediaBean.getId());
    }

    private class UploadListener implements TransferListener {
        private MediaBean mediaBean;

        public UploadListener(MediaBean mediaBean) {
            this.mediaBean = mediaBean;
        }

        // Simply updates the UI list when notified.
        @Override
        public void onError(int id, Exception e) {
            mediaBean.setIsSucess("0");
            mediaBean.setId(id);
            amazonCallback.uploadError(e, mediaBean);
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            int progress = (int) ((double) bytesCurrent * 100 / bytesTotal);
            mediaBean.setId(id);
            mediaBean.setProgress(progress);
            amazonCallback.uploadProgress(mediaBean);
        }


        @Override
        public void onStateChanged(int id, TransferState newState) {
            mediaBean.setId(id);
            if (newState == TransferState.COMPLETED) {
                mediaBean.setIsSucess("1");
                String url = AmazonS3Constants.AMAZON_SERVER_URL + mediaBean.getmObserver().getKey();
                mediaBean.setServerUrl(url);
                amazonCallback.uploadSuccess(mediaBean);
            } else if (newState == TransferState.FAILED) {
                mediaBean.setIsSucess("0");
                amazonCallback.uploadFailed(mediaBean);
            }
        }
    }

    /*
    * Check file is image or not
    * */
    public class ImageFileFilter implements FileFilter {

        private final String[] okFileExtensions = new String[]{"jpg", "png", "gif", "jpeg"};

        public boolean accept(File file) {
            for (String extension : okFileExtensions) {
                if (file.getName().toLowerCase().endsWith(extension)) {
                    return true;
                }
            }
            return false;
        }

    }

}
