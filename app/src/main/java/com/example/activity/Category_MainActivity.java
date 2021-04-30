package com.example.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.simple_voca.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Category_MainActivity extends AppCompatActivity {

    ImageButton category_main_add_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_main);




        // 카테고리 추가 버튼
        category_main_add_button = findViewById(R.id.category_main_add_button);
        category_main_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Category_MainActivity.this, Category_AddActivity.class);
                startActivity(intent);
            }
        });


        makeCategoryList();
    }

    public void makeCategoryList(){

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_category_main_item, null);

        LinearLayout linearLayout = findViewById(R.id.category_main_category_list);

        for(int i = 0; i < LoadingActivity.categoryList.size(); i++){


            View view = inflater.inflate(R.layout.activity_category_main_item, null);
            CardView cardView = view.findViewById(R.id.category_main_category_cardview);
            TextView category_name = view.findViewById(R.id.category_main_category_name);
            TextView category_subtitle = view.findViewById(R.id.category_main_category_subtitle);
            TextView category_all_word = view.findViewById(R.id.category_main_category_all_word);
            TextView category_remind_word = view.findViewById(R.id.category_main_category_remind_word);
            TextView category_important_word = view.findViewById(R.id.category_main_category_important_word);
            ImageButton select_button = view.findViewById(R.id.category_main_category_select_button);

            String categoryName = LoadingActivity.categoryDatabase.getCategoryName(i);

            category_name.setText(categoryName);
            if(categoryName.equals(LoadingActivity.SELECTED_CATEGORY_NAME)){
                cardView.setCardBackgroundColor(getResources().getColor(R.color.mainBlue));
                category_name.setTextColor(getResources().getColor(R.color.white));
                select_button.setBackgroundColor(getResources().getColor(R.color.mainBlue));
                category_subtitle.setTextColor(getResources().getColor(R.color.white));
            }
            category_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoadingActivity.SELECTED_CATEGORY_NAME = categoryName;
                    LoadingActivity.vocaDatabase.makeList(LoadingActivity.vocaList);

                    Intent intent = new Intent(Category_MainActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            category_subtitle.setText(LoadingActivity.categoryDatabase.getCategorySubTitle(i));
            category_all_word.setText(Integer.toString(LoadingActivity.vocaDatabase.getCategoryAllWordSize(categoryName)) + "개");
            category_remind_word.setText(Integer.toString(LoadingActivity.vocaDatabase.getCategoryRemindedWordSize(categoryName)) + "개");
            category_important_word.setText(Integer.toString(LoadingActivity.vocaDatabase.getCategoryImportantWordSize(categoryName)) + "개");

            linearLayout.addView(view);
        }


    }

}
