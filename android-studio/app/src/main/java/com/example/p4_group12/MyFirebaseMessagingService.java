package com.example.p4_group12;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.Tag;
import com.example.p4_group12.Interface.AdvertisementViewActivity;
import com.example.p4_group12.Interface.GlobalVariables;
import com.example.p4_group12.Interface.HomeActivity;
import com.example.p4_group12.Interface.LoginActivity;
import com.example.p4_group12.database.API;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String CANAL = "NotifCanal";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String myMessage = remoteMessage.getData().get("body"); // message received from Firebase
        String notificationTitle = remoteMessage.getData().get("title");
        int advertisement_id = Integer.parseInt(remoteMessage.getData().get("notification_id")); // Use this to launch the right intent

        Log.d("FirebaseMessage", "message received : " + myMessage);

        // création de la notif visuel
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CANAL); //construit une nouvelle notif
        notificationBuilder.setContentTitle(notificationTitle);//titre de la notif
        notificationBuilder.setContentText(myMessage); //contenue de la notif
        notificationBuilder.setAutoCancel(true); // undisplay the notification
        // icone de la notif
        notificationBuilder.setSmallIcon(R.drawable.ic_baseline_notifications_active);


        //action : diriger le user vers une activity quand il click sur la notif
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        Advertisement add = API.getInstance().getAdvertisment(advertisement_id);
        Intent advertisementView = new Intent(getApplicationContext(), AdvertisementViewActivity.class);
        advertisementView.putExtra("ClickedAdvertisement", add);
        int i = 0;
        for (Tag tag : add.getTags()) {
            advertisementView.putExtra("tag"+i, tag);
            i++;
        }
        advertisementView.putExtra("Number of tags", i);
        advertisementView.putExtra("contactable", 1);

        PendingIntent pendingIntent = PendingIntent.getActivities(getApplicationContext(), 0, new Intent[] {intent, advertisementView}, PendingIntent.FLAG_CANCEL_CURRENT);
        // ajout de l'action
        notificationBuilder.setContentIntent(pendingIntent);


        //vibration quand recoit une notif
        long[] vibrationPattern = {500, 1000};
        notificationBuilder.setVibrate(vibrationPattern);


        // envoyer une notif
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // à partir de android Oreo --> besoin d'un channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = getString(R.string.notification_channel_id);
            String channelTitle = getString(R.string.notification_channel_title);
            String channelDescription = getString(R.string.notification_channel_desc);
            NotificationChannel channel = new NotificationChannel(channelId, channelTitle, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channelDescription);
            notificationManager.createNotificationChannel(channel);
            notificationBuilder.setChannelId(channelId);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        if(GlobalVariables.getUser() != null){
            API.getInstance().sendToken(GlobalVariables.getUser().getEmail(), s);
        }
    }
}
