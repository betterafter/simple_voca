package com.example.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.Items.ListItem;
import com.example.database.VocaDatabase;
import com.example.simple_voca.R;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    public static VocaDatabase vocaDatabase;
    public static ArrayList<ListItem> vocaList = new ArrayList<>();

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






        Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
