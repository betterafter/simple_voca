package com.danerdaner.simple_voca;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.danerdaner.activity.ScreenOnActivity;

public class wordChangeBroadcastReceiver extends BroadcastReceiver {

    private static PowerManager.WakeLock sCpuWakeLock;
    VocaForegroundServiceUpdater vocaForegroundServiceUpdater;

    public wordChangeBroadcastReceiver(){}

    public wordChangeBroadcastReceiver(VocaForegroundServiceUpdater vocaForegroundServiceUpdater){
        this.vocaForegroundServiceUpdater = vocaForegroundServiceUpdater;
    }


    @Override
    public void onReceive(Context context, Intent intent) {


        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){


            vocaForegroundServiceUpdater.wordChanger();
            Intent i = new Intent(context, ScreenOnActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(context, 0, i, 0);
            try{
                pendingIntent.send();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
