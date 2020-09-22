package com.example.chattestapp.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.chattestapp.Activites.ChatScreen;
import com.example.chattestapp.Utils.ChatUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFireBaseMessaging extends FirebaseMessagingService {

    String senderuseruid;
    String recieveruseruid;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        senderuseruid = remoteMessage.getData().get("senderuseruid");
        recieveruseruid = remoteMessage.getData().get("recieveruseruid");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null && recieveruseruid.equals(currentUser.getUid())) {

            if (!ChatUtils.Chat_UID.equals(senderuseruid)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    sendOreoNotification(remoteMessage);
                } else {
                    sendNotification(remoteMessage);
                }
            }

        }
    }


    private void sendOreoNotification(RemoteMessage remoteMessage) {
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("textMessage");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(senderuseruid.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, ChatScreen.class);
        intent.putExtra("uid", senderuseruid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent,
                defaultSound, icon);

        int i = (int) System.currentTimeMillis();

        oreoNotification.getManager().notify(i, builder.build());

    }


    private void sendNotification(RemoteMessage remoteMessage) {

        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("textMessage");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(senderuseruid.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, ChatScreen.class);
        intent.putExtra("uid", senderuseruid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), Integer.parseInt(icon));
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setLargeIcon(bitmap)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setGroup(body.split(":")[0])
                .setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int i = (int) System.currentTimeMillis();

        noti.notify(i, builder.build());
    }


}
