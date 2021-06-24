package com.geora.util;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;

import java.io.File;

/**
 * Created by admin1 on 5/6/17.  for   download  file  in  separate   thread
 */

public class DownloadFileIntentService extends IntentService {

    public static final String ACTION_DOWNLOAD = "download_pdf";
    public static final String DOWNLOAD_DIRECTORY = "Geora";
    public static final String EXTRA_URL = "url";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public DownloadFileIntentService() {
        super("DownloadFileIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWNLOAD.equals(action)) {
                final String url = intent.getStringExtra(EXTRA_URL);
                Log.e("Service", url);
                downloadFile(url);
            }
        }

    }


    /**
     * this method  is  used for  download file
     * @param uRl
     */
    public void downloadFile(final String uRl) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/" + DOWNLOAD_DIRECTORY);

        if (!direct.exists()) {
            direct.mkdirs();
        }

        final DownloadManager mgr = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(true)
                .setTitle("Phone Storage/" + DOWNLOAD_DIRECTORY + "/" + uRl.substring(uRl.lastIndexOf('/') + 1, uRl.length()))
                .setDescription(Environment.getExternalStorageDirectory() + "/" + DOWNLOAD_DIRECTORY + "/" + uRl.substring(uRl.lastIndexOf('/') + 1, uRl.length()))
                .setVisibleInDownloadsUi(true)
                .setDestinationInExternalPublicDir("/" + DOWNLOAD_DIRECTORY, uRl.substring(uRl.lastIndexOf('/') + 1, uRl.length()));

        mgr.enqueue(request);
    }
}
