package com.danerdaner.simple_voca;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;

import com.danerdaner.activity.LoadingActivity;
import com.danerdaner.activity.MainActivity;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

public class VocaForegroundService extends Service {

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder notification;

    private BroadcastReceiver mReceiver;
    public VocaForegroundServiceUpdater vocaForegroundServiceUpdater;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // PendingIntent를 이용하면 포그라운드 서비스 상태에서 알림을 누르면 앱의 MainActivity를 다시 열게 된다.
        Intent ScreenOnIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent
                = PendingIntent.getActivity(this, 0, ScreenOnIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                //= PendingIntent.getActivity(this, 0, ScreenOnIntent, 0);
        // 오래오 윗버젼일 때는 아래와 같이 채널을 만들어 Notification과 연결해야 한다.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel", "voca",
                    NotificationManager.IMPORTANCE_DEFAULT);

            // Notification과 채널 연걸
            mNotificationManager = ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));
            mNotificationManager.createNotificationChannel(channel);

            String[] data = new String[]{
                    "단어가 없습니다.", "단어를 저장해주세요."
            };
            if(!LoadingActivity.lockVocaList.isEmpty()) {
                data = LoadingActivity.vocaDatabase.getWordChangerString(0, LoadingActivity.lockVocaList);
            }

            BitmapDrawable bd
                    = (BitmapDrawable) (ResourcesCompat.getDrawable(getResources(), R.drawable.icon_main_2, null));
            Bitmap bitmap = bd.getBitmap();
            // Notification 세팅
            notification
                    = new NotificationCompat.Builder(getApplicationContext(), "channel")
                    .setSmallIcon(R.mipmap.icon_service_black)
                    .setLargeIcon(bitmap)
                    .setContentTitle(data[0] + "  [" + data[2] + "]")
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentText(data[1]);

            // id 값은 0보다 큰 양수가 들어가야 한다.
            mNotificationManager.notify(1, notification.build());
            // foreground에서 시작
            startForeground(1, notification.build());
        }

        vocaForegroundServiceUpdater = new VocaForegroundServiceUpdater(mNotificationManager,
                notification, getApplicationContext(), pendingIntent);

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new wordChangeBroadcastReceiver(vocaForegroundServiceUpdater);
        registerReceiver(mReceiver, filter);

        return START_STICKY;
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
