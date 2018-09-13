package com.example.android.alarmapp.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.android.alarmapp.Alarm;
import com.example.android.alarmapp.R;

import java.util.ArrayList;

/**
 * Created by Afnan A. A. Abed on 9/8/2018.
 */

public class AlarmService extends IntentService {

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("Service", "Service Created");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.v("Service", "Service onHandle");

        Notification.Builder notification = new Notification.Builder(this);
//        NotificationChannel channel = new NotificationChannel("channel_id",
//                "Alarm Notification Channel", NotificationManager.IMPORTANCE_DEFAULT);
        notification.setContentTitle("Alarm Notification");
        notification.setContentText("The time has come");
        notification.setSmallIcon(R.drawable.ic_launcher_background);
        notification.setPriority(Notification.PRIORITY_DEFAULT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notification.setSound(alarmSound);
//        notification.setOnlyAlertOnce(true);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(1, notification.build());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("Service", "Service Destroyed");
    }
}
