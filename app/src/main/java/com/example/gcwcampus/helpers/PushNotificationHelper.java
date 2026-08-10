package com.example.gcwcampus.helpers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.gcwcampus.R;
import com.example.gcwcampus.activities.DashboardActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class PushNotificationHelper extends FirebaseMessagingService {

    String userRole;
    NotificationManager notificationManager;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Intent intent = new Intent(this, DashboardActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"Admin");

        builder.setContentTitle(Objects.requireNonNull(message.getNotification()).getTitle());
        builder.setContentText(message.getNotification().getBody());
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.splash_logo);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);

        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "admin_channel";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Admin Notifications",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);

            notificationManager.notify(1,builder.build());
        }
    }

    @Override
    public void onNewToken (@NonNull String token){
        super.onNewToken(token);
        if (userRole!=null && (userRole.equals("Faculty") || userRole.equals("Student"))){
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("tokens").child("Faculty");
            databaseReference.setValue(token);
        }
    }

}