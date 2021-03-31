package com.example.simple_voca;

import android.content.Intent;
import android.os.Bundle;

import com.example.Items.ListItem;
import com.example.database.VocaDatabase;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    public static VocaDatabase vocaDatabase;
    public static ArrayList<ListItem> vocaList = new ArrayList<>();

    public static int wordChangedTime = 60;
    public static int LastTime;
    public static int CurrTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        vocaDatabase = new VocaDatabase(getApplicationContext(), "voca", null, 1);
        vocaDatabase.makeList(vocaList);


        Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
