package com.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.simple_voca.R;

import androidx.appcompat.app.AppCompatActivity;

public class ScreenOnActivity extends AppCompatActivity {

    private TextView word;
    private TextView mean;
    private TextView announce;
    private TextView example;
    private TextView example_mean;
    private TextView memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_on);

        word = findViewById(R.id.screen_on_word);
        mean = findViewById(R.id.screen_on_mean);
        announce = findViewById(R.id.screen_on_announce);
        example = findViewById(R.id.screen_on_example);
        example_mean = findViewById(R.id.screen_on_example_mean);
        memo = findViewById(R.id.screen_on_memo);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        int RandomIdx = intent.getIntExtra("RandomIdx", 0);
        String[] data = LoadingActivity.vocaDatabase.getWordChangerString(RandomIdx, LoadingActivity.vocaList);
        word.setText(data[0]);
        mean.setText(data[1]);
        announce.setText(data[2]);
        example.setText(data[3]);
        example_mean.setText(data[4]);
        memo.setText(data[5]);
    }
}
