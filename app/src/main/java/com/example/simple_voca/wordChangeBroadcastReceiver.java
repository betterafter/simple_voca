package com.example.simple_voca;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.activity.LoadingActivity;
import com.example.activity.ScreenOnActivity;

public class wordChangeBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){

            int RandomIdx = (int)(Math.random() * LoadingActivity.vocaList.size());

            VocaForegroundService.vocaForegroundServiceUpdater.wordChanger(RandomIdx);

            Intent i = new Intent(context, ScreenOnActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("RandomIdx", RandomIdx);
            context.startActivity(i);
        }
    }
}
