package com.example.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.simple_voca.R;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Test_TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_test);

        Intent intent = getIntent();
        ArrayList<String[]> TestList = (ArrayList<String[]>) intent.getSerializableExtra("list");






        // 테스트를 다 봤으면 Intent를 이용해 Test_ResultActivity로 넘어갈 것.
    }
}
