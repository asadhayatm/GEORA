package com.geora.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.geora.R;
import com.geora.constants.AppConstants;
import com.geora.data.DataManager;
import com.geora.socket.SocketConstant;
import com.geora.ui.home.HomeActivity;
import com.geora.util.AppUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    private static final String TAG = "FirebasePushService";


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
//        PreferenceManager.getInstance(this).putString(AppConstants.PreferenceConstants.DEVICE_TOKEN, s);
        DataManager.getInstance().setDeviceTokenStatus(s);
        AppUtils.hitDeviceTokenApi();

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //handling notification
        sendNotification(remoteMessage.getData());
    }

    //method to get notificationManager
    public NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    //This method is only generating push notification
    private void sendNotification(Map<String, String> data) {
        String type = "0";
        String mTitle = getString(R.string.app_name);
        String message = getString(R.string.app_name);
        String campId = "1";
        String campType = "4";
        String beaconId = "";
        try {
            if (data.get("type") != null)
                type = data.get("type");
            if (data.get("title") != null)
                mTitle = data.get("title");
            if (data.get("notification_message") != null)
                message = data.get("notification_message");
            if (data.get("camp_id") != null)
                campId = data.get("camp_id");
            if (data.get("camp_type") != null)
                campType = data.get("camp_type");
            if (data.get("beacon_id") != null)
                beaconId = data.get("beacon_id");


            int mUniqueId = Integer.parseInt(type);
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(AppConstants.NOTIFICATION_TYPE, type);
            intent.putExtra(SocketConstant.SOCKEYKEYS.CAMP_ID, campId);
            intent.putExtra(SocketConstant.SOCKEYKEYS.CAMP_TYPE, campType);
            intent.putExtra(SocketConstant.SOCKEYKEYS.BEACON_ID, beaconId);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, mUniqueId, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationManager notificationManager = getNotificationManager();


            if (notificationManager != null) {
                NotificationChannel channel;
                String channelId = "channel_" + String.valueOf(mUniqueId);
                String channelName = "Channel " + channelId;
                String groupId = "notification_channel_group";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
                    // Sets whether notifications posted to this channel should display notification lights
                    channel.enableLights(true);
                    // Sets whether notification posted to this channel should vibrate.
                    channel.enableVibration(true);
                    // Sets the notification light color for notifications posted to this channel
                    channel.setLightColor(Color.GREEN);
                    // Sets whether notifications posted to this channel appear on the locks creen or not
                    channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                    //creating channel group
                    channel.setGroup(groupId);

                    notificationManager.createNotificationChannelGroup(new NotificationChannelGroup(groupId, "notification_channel_group"));
                    notificationManager.createNotificationChannel(channel);
                }

                //Set Notification for other devices
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(this, channelId)
                                .setSmallIcon(getNotificationIcon())
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                                .setContentTitle(mTitle)
                                .setContentText(message)
                                .setAutoCancel(true)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                                .setChannelId(channelId)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setContentIntent(pendingIntent);

                // Set a message count to associate with this notification in the long-press menu.
                // Create a notification and set a number to associate with it.

                Notification notification = notificationBuilder.build();

                notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                notification.flags |=
                        Notification.FLAG_AUTO_CANCEL; //Do not clear  the notification
                notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
                notification.defaults |= Notification.DEFAULT_VIBRATE;//Vibration


//                boolean foreground = new ForegroundCheckTask().execute(this).get();
//                if (!foreground) {
                    notificationManager.notify(mUniqueId, notification);
//                }

                //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                //notificationManager.notify(mUniqueId, notificationBuilder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to set the notification icon on the push notifications for marshmallow
     * or oreo devices
     *
     * @return drawable on the basis of the type of device
     */
    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_app_logo : R.drawable.ic_app_logo;
    }
}
