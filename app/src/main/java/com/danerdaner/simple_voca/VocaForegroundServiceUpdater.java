package com.danerdaner.simple_voca;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.danerdaner.activity.LoadingActivity;

import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

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

        int idx = (int)(Math.random() * LoadingActivity.lockVocaList.size());
        String[] data = LoadingActivity.vocaDatabase.getWordChangerString(idx, LoadingActivity.lockVocaList);

        BitmapDrawable bd
                = (BitmapDrawable) (ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_main_2, null));
        Bitmap bitmap = bd.getBitmap();
        // Notification 세팅
        notification
                = new NotificationCompat.Builder(context, "channel")
                .setSmallIcon(R.mipmap.icon_service_black)
                .setLargeIcon(bitmap)
                .setContentTitle(data[0] + "  [" + data[2] + "]")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentText(data[1]);

        // id 값은 0보다 큰 양수가 들어가야 한다.
        mNotificationManager.notify(1, notification.build());
    }
}
