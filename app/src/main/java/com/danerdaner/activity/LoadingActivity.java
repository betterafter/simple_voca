package com.danerdaner.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;

import com.danerdaner.Items.ListItem;
import com.danerdaner.Items.ScoreListItem;
import com.danerdaner.Items.categoryListItem;
import com.danerdaner.database.ScoreDatabase;
import com.danerdaner.database.VocaDatabase;
import com.danerdaner.database.categoryDatabase;
import com.danerdaner.simple_voca.R;
import com.danerdaner.simple_voca.VocaForegroundService;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    public static VocaDatabase vocaDatabase;
    public static categoryDatabase categoryDatabase;
    public static ScoreDatabase ScoreDatabase;

    public static ArrayList<ListItem> vocaList = new ArrayList<>();
    public static ArrayList<categoryListItem> categoryList = new ArrayList<>();
    public static ArrayList<ScoreListItem> categoryTestResultList = new ArrayList<>();
    public static ArrayList<ListItem> lockVocaList = new ArrayList<>();

    public static String SELECTED_CATEGORY_NAME = "전체";
    public static String SELECTED_CATEGORY_SUBTITLE;

    public static int wordChangedTime = 60;
    public static int LastTime;
    public static int CurrTime;

    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;

    public static SharedPreferences sharedPreferences;


    @RequiresApi(api = Build.VERSION_CODES.O)
   // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        getPermission();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String lock_category_name = sharedPreferences.getString("category", "전체");


        vocaDatabase = new VocaDatabase(getApplicationContext(), "voca", null, 2);
        vocaDatabase.makeList(vocaList);
        vocaDatabase.makeList(lockVocaList, lock_category_name);

        categoryDatabase = new categoryDatabase(getApplicationContext(), "category", null, 2);
        if(categoryDatabase.getSize() == 0){
            LoadingActivity.categoryDatabase.insert("전체", "모든 단어를 가지고 있는 단어장입니다.");
        }
        categoryDatabase.makeList(categoryList);
        SELECTED_CATEGORY_SUBTITLE = categoryDatabase.getCategorySubTitle(SELECTED_CATEGORY_NAME);

        ScoreDatabase = new ScoreDatabase(getApplicationContext(), "Score", null, 3);
        //ScoreDatabase.deleteAll();

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(this)) {// 체크

                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
            else{
                Intent serviceIntent = new Intent(LoadingActivity.this, VocaForegroundService.class);
                startForegroundService(serviceIntent);

                Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // TODO 동의를 얻지 못했을 경우의 처리

            } else {
                // 서비스 인텐트 생성 후 서비스 실행. 이 때 오레오 이전 버전과 이후 버전에서 서비스를 시작하는 방식이 조금 다르다.
                Intent serviceIntent = new Intent(LoadingActivity.this, VocaForegroundService.class);
                startForegroundService(serviceIntent);

                Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
