package com.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.simple_voca.R;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Category_AddActivity extends AppCompatActivity {

    TextInputEditText category_add_name;
    TextInputEditText category_add_content;

    Button category_add_add_button;

    private String category_name_str;
    private String category_content_str;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add);

        Intent intent = getIntent();
        if(intent != null && intent.getStringExtra("type") != null &&  intent.getStringExtra("type").equals("edit")){
            category_name_str = intent.getStringExtra("category_name");
            category_content_str = intent.getStringExtra("category_content");
        }

        System.out.println(category_name_str);
        System.out.println(category_content_str);


        category_add_name = findViewById(R.id.category_add_name);
        category_add_content = findViewById(R.id.category_add_content);

        if(category_name_str != null && !category_name_str.equals(""))
            category_add_name.setText(category_name_str);

        if(category_content_str != null && !category_content_str.equals(""))
            category_add_content.setText(category_content_str);

        category_add_add_button = findViewById(R.id.category_add_add_button);
        category_add_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = category_add_name.getText().toString();
                String content = category_add_content.getText().toString();

                //진짜로 저장할 것인지 물어보는 다이얼로그 추가할 것
                LoadingActivity.categoryDatabase.insert(name, content);
                LoadingActivity.categoryDatabase.makeList(LoadingActivity.categoryList);

                Intent intent = new Intent(Category_AddActivity.this, Category_MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        if(intent != null && intent.getStringExtra("type") != null && intent.getStringExtra("type").equals("edit")){
            category_name_str = intent.getStringExtra("category_name");
            category_content_str = intent.getStringExtra("category_content");
        }

        if(category_name_str != null && !category_name_str.equals(""))
            category_add_name.setText(category_name_str);

        if(category_content_str != null && !category_content_str.equals(""))
            category_add_content.setText(category_content_str);

    }
}
