package com.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.simple_voca.R;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Category_MainActivity extends AppCompatActivity {

    TextInputEditText test_category_name;
    Button test_category_share_button;

    Button test_category_test_button;

    ImageButton category_main_add_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_main);


        // 공유기능은 이걸로 시험해볼 것
        test_category_name = findViewById(R.id.category_main_name);
        test_category_share_button = findViewById(R.id.category_main_share_button);



        // 카테고리 추가 버튼
        category_main_add_button = findViewById(R.id.category_main_add_button);
        category_main_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Category_MainActivity.this, Category_AddActivity.class);
                startActivity(intent);
            }
        });


        // 테스트 화면 진입 버튼
        test_category_test_button = findViewById(R.id.category_main_sample_test_button);
        test_category_test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Category_MainActivity.this, Test_MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
