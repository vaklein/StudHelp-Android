package com.example.p4_group12;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.p4_group12.Interface.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String CANAL = "NotifCanal";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String myMessage = remoteMessage.getNotification().getBody(); // message received from Firebase
        Log.d("FirebaseMessage", "message receive : " + myMessage);

        //action : diriger le user vers une activity quand il click sur la notif
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class); // TODO : set to the activity we want
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        // création de la notif visuel
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CANAL); //construit une nouvelle notif
        notificationBuilder.setContentTitle("nouvelle notif");//titre de la notif
        notificationBuilder.setContentText(myMessage); //contenue de la notif

        // ajout de l'action
        notificationBuilder.setContentIntent(pendingIntent);

        //vibration quand recoit une notif
        long[] vibrationPattern = {500, 1000};
        notificationBuilder.setVibrate(vibrationPattern);

        // icone de la notif
        notificationBuilder.setSmallIcon(R.drawable.ic_logo_discord); //TODO : set to the logo af the application

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
        notificationManager.notify(1, notificationBuilder.build());
    }
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("NEW_TOKEN",s);
    }
}
