package com.danerdaner.activity;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.danerdaner.simple_voca.ImageSerializer;
import com.danerdaner.simple_voca.R;
import com.danerdaner.simple_voca.wordChangeBroadcastReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class ScreenOnActivity extends AppCompatActivity {

    private TextView s_time_time;
    private TextView s_time_parser;
    private TextView s_time_minute;
    private TextView s_date;
    private TextView s_week;

    private TextView word;
    private TextView mean;
    private TextView announce;
    private TextView example;
    private TextView example_mean;
    private TextView memo;
    private ImageView imageView;

    private TimeThread timeThread;
    private Button screen_on_unlock_button;

    public static boolean isTimeThreadStop = false;

    private String SCREEN_ON_ACTIVITY_NAME = "ScreenOnActivity";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isTimeThreadStop = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            keyguardManager.requestDismissKeyguard(this, null);
        }
        else{
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        }

        setContentView(R.layout.activity_screen_on);

        s_time_time = findViewById(R.id.screen_on_time);
        s_time_parser = findViewById(R.id.screen_on_time_parser);
        s_time_minute = findViewById(R.id.screen_on_minuite);

        s_date = findViewById(R.id.screen_on_date);
        s_week = findViewById(R.id.screen_on_week);
        try {
            s_week.setText("(" + getWeek() + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }

        word = findViewById(R.id.screen_on_word);
        mean = findViewById(R.id.screen_on_mean);
        announce = findViewById(R.id.screen_on_announce);
        example = findViewById(R.id.screen_on_example);
        example_mean = findViewById(R.id.screen_on_example_mean);
        memo = findViewById(R.id.screen_on_memo);
        imageView = findViewById(R.id.screen_on_image);

        screen_on_unlock_button = findViewById(R.id.screen_on_unlock_button);
        screen_on_unlock_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        int RandomIdx = (int)(Math.random() * LoadingActivity.vocaList.size());

        String[] data = LoadingActivity.vocaDatabase.getWordChangerString(RandomIdx, LoadingActivity.vocaList);
        word.setText(data[0]);
        mean.setText(data[1]);
        announce.setText(data[2]);
        example.setText(data[3]);
        example_mean.setText(data[4]);
        memo.setText(data[5]);
        imageView.setImageDrawable(new BitmapDrawable(
                getResources(), ImageSerializer.PackSerializedToImage(data[6])));

//        if(timeThread == null) System.out.println("timeThread null");
//        else System.out.println("timeThread not null");
//
//        if(timeThread == null) {
//            timeThread = new TimeThread();
//            timeThread.start();
//        }

        TimeThread t = new TimeThread();
        t.start();
    }

    private class TimeThread extends Thread{
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void run(){
            try {
                while (!isTimeThreadStop) {

                    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> info = manager.getRunningTasks(1);
                    ComponentName componentName= info.get(0).topActivity;
                    String ActivityName = componentName.getShortClassName();

                    System.out.println(ActivityName);
                    System.out.println(getApplicationContext().getClass().getName());

                    if(!ActivityName.contains(SCREEN_ON_ACTIVITY_NAME)
                            || wordChangeBroadcastReceiver.ScreenOnActivityStop){
                        moveTaskToBack(true);						// 태스크를 백그라운드로 이동
                        finishAndRemoveTask();                              // 액티비티 종료 + 태스크 리스트에서 지우기
                        isTimeThreadStop = true;
                        timeThread = null;
                        break;
                    }


                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String getDate = dateFormat.format(date);

                    String d = getDate.substring(0, 10);
                    String t = getDate.substring(11, 13);
                    String p = getDate.substring(13, 14);
                    String m = getDate.substring(14, 16);

                    System.out.println(d);
                    System.out.println(t);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            s_date.setText(d);
                            s_time_time.setText(t);
                            s_time_parser.setText(p);
                            s_time_minute.setText(m);

                            if(s_time_parser.getVisibility() == View.VISIBLE)
                                s_time_parser.setVisibility(View.INVISIBLE);
                            else
                                s_time_parser.setVisibility(View.VISIBLE);
                        }
                    });
                    sleep(1000);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private String getWeek() throws Exception {

        String day = "";

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String getDate = dateFormat.format(date);
        Date nDate = dateFormat.parse(getDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);

        switch (dayNum) {
            case 1:
                day = "일";
                break;
            case 2:
                day = "월";
                break;
            case 3:
                day = "화";
                break;
            case 4:
                day = "수";
                break;
            case 5:
                day = "목";
                break;
            case 6:
                day = "금";
                break;
            case 7:
                day = "토";
                break;

        }

        return day;
    }



    @Override
    protected void onPause() {
        super.onPause();
        timeThread = null;
        //finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isTimeThreadStop = true;
        timeThread = null;
        //finish();
    }


}
