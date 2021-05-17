package com.danerdaner.simple_voca;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import com.danerdaner.activity.LoadingActivity;

import androidx.core.app.NotificationCompat;

public class VocaForegroundServiceUpdater {

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder notification;
    private PendingIntent pendingIntent;

    private Context context;

    public VocaForegroundServiceUpdater(NotificationManager mNotificationManager,
                                        NotificationCompat.Builder notification, Context context,
                                        PendingIntent pendingIntent){
        this.mNotificationManager = mNotificationManager;
        this.notification = notification;
        this.context = context;
        this.pendingIntent = pendingIntent;
    }

    public void wordChanger(){

        int idx = (int)(Math.random() * LoadingActivity.vocaList.size());
        String[] data = LoadingActivity.vocaDatabase.getWordChangerString(idx, LoadingActivity.vocaList);


        notification
                = new NotificationCompat.Builder(context, "channel")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(data[0])
                .setContentIntent(pendingIntent)
                .setContentText(data[1]);

        // id 값은 0보다 큰 양수가 들어가야 한다.
        mNotificationManager.notify(1, notification.build());
    }
}
