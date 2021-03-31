package com.example.simple_voca;

import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

public class VocaForegroundServiceUpdater {

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder notification;

    private Context context;

    public VocaForegroundServiceUpdater(NotificationManager mNotificationManager,
                                        NotificationCompat.Builder notification, Context context){
        this.mNotificationManager = mNotificationManager;
        this.notification = notification;
        this.context = context;
    }

    public void wordChanger(int idx){

        String[] data = LoadingActivity.vocaDatabase.getWordChangerString(idx, LoadingActivity.vocaList);

        notification
                = new NotificationCompat.Builder(context, "channel")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(data[0])
                .setContentText(data[1]);

        // id 값은 0보다 큰 양수가 들어가야 한다.
        mNotificationManager.notify(1, notification.build());
    }
}
