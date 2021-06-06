package com.danerdaner.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.danerdaner.simple_voca.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


        category_add_name = findViewById(R.id.category_add_name);
        category_add_content = findViewById(R.id.category_add_content);

        if(category_name_str != null && !category_name_str.equals(""))
            category_add_name.setText(category_name_str);

        if(category_content_str != null && !category_content_str.equals(""))
            category_add_content.setText(category_content_str);

        category_add_add_button = findViewById(R.id.category_add_add_button);
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

        category_add_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = category_add_name.getText().toString();
                String content = category_add_content.getText().toString();

                if(name.length() <= 0){
                    Toast.makeText(getApplicationContext(),
                            "카테고리 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                content = changeChar(content);

                if(intent != null && intent.getStringExtra("type") != null && intent.getStringExtra("type").equals("edit")){{
                    LoadingActivity.categoryDatabase.update(name, content,
                            category_name_str, category_content_str);
                    LoadingActivity.categoryDatabase.makeList(LoadingActivity.categoryList);
                }}

                else {
                    //진짜로 저장할 것인지 물어보는 다이얼로그 추가할 것
                    LoadingActivity.categoryDatabase.insert(name, content);
                    LoadingActivity.categoryDatabase.makeList(LoadingActivity.categoryList);
                }

                Intent intent = new Intent(Category_AddActivity.this, Category_MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkString(String str, Context context){

        Pattern pattern = Pattern.compile("[ \n!@#$%^&*(),.?\"\':{}|<>]");
        Matcher matcher = pattern.matcher(str);

        if(!matcher.find()) {
            return true;
        }
        else{
            Toast.makeText(context, "카테고리 이름 입력에 특수문자 및 공백을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private String changeChar(String str){

        String res = "";
        for(int i = 0; i < str.length(); i++){
            if(str.charAt(i) == '\''){
                res = res + "\"";
            }
            else res += str.charAt(i);
        }

        return res;
    }

    




    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void category_add_onBackClick(View view){
        Intent intent = new Intent(Category_AddActivity.this, Category_MainActivity.class);
        startActivity(intent);
        finish();
    }
}
