package com.example.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.Items.ListItem;
import com.example.Items.categoryListItem;
import com.example.database.VocaDatabase;
import com.example.database.categoryDatabase;
import com.example.simple_voca.R;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    public static VocaDatabase vocaDatabase;
    public static categoryDatabase categoryDatabase;

    public static ArrayList<ListItem> vocaList = new ArrayList<>();
    public static ArrayList<categoryListItem> categoryList = new ArrayList<>();

    public static String SELECTED_CATEGORY_NAME = "전체";

    public static int wordChangedTime = 60;
    public static int LastTime;
    public static int CurrTime;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        vocaDatabase = new VocaDatabase(getApplicationContext(), "voca", null, 2);
        vocaDatabase.makeList(vocaList);

        categoryDatabase = new categoryDatabase(getApplicationContext(), "category", null, 2);
        if(categoryDatabase.getSize() == 0){
            LoadingActivity.categoryDatabase.insert("전체", "모든 단어를 가지고 있는 단어장입니다.");
        }
        categoryDatabase.makeList(categoryList);



        Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
