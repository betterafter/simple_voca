package com.example.activity;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.simple_voca.R;
import com.example.simple_voca.TestAnswer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class Test_ResultActivity extends AppCompatActivity {

    private String category_name;
    private String test_type;

    private int score;
    private TextView test_result_score;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        // 아이디어는 동적으로 이미지뷰와 텍스트뷰를 만들어서 결과의 그림과 같은 형태로 만드는 것.
        // 그리드뷰를 이용해도 상관없고, 리니어 레이아웃으로 테이블 형태를 만들어도 상관 없음.

        // activity_test_result에서 버튼을 하나 만들고, 해당 버튼을 누르면 intent를 이용하여 오답으로 넘어갈 수 있게 만들기
        Intent intent = getIntent();
        ArrayList<TestAnswer> answers = (ArrayList<TestAnswer>)intent.getSerializableExtra("answers");
        category_name = intent.getStringExtra("category_name");
        test_type = intent.getStringExtra("test_type");
        String Category_name = intent.getStringExtra("category_name");

        test_result_score = findViewById(R.id.test_result_score);

        GridLayout g = findViewById(R.id.test_girdlayout);
        Button b = findViewById(R.id.test_result_detail);

        Display display = getWindowManager().getDefaultDisplay();  // in Activity
        /* getActivity().getWindowManager().getDefaultDisplay() */ // in Fragment
        Point size = new Point();
        display.getRealSize(size); // or getSize(size)
        int width = size.x;
        int height = size.y;

        for(int i=0;i<answers.size();i++){

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setPadding(0, 15,0,15);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    width / 5, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 10, 0, 10);

            linearLayout.setLayoutParams(params);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);


            TextView text = new TextView(this);
            ImageView image = new ImageView(this);

            text.setText(Integer.toString(i + 1));
            text.setGravity(View.TEXT_ALIGNMENT_CENTER);
            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            text.setTypeface(text.getTypeface(), Typeface.BOLD);
            text.setTextSize(20);

            if(answers.get(i).isCorrect()) text.setTextColor(ResourcesCompat.getColor(getResources(), R.color.mainBlue, null));
            else text.setTextColor(ResourcesCompat.getColor(getResources(), R.color.mainRed, null));

            if(answers.get(i).isCorrect()) {
                score += 5;
                image.setImageDrawable(
                        ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_check_circle_24, null));
                image.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.mainBlue, null));
            }
            else {
                image.setImageDrawable(
                        ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_cancel_18, null));
                image.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.mainRed, null));
            }

            linearLayout.addView(image);
            linearLayout.addView(text);
            g.addView(linearLayout);
        }

        test_result_score.setText(Integer.toString(score));

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = simpleDate.format(mDate);

        LoadingActivity.ScoreDatabase.insert(Integer.toString(score), category_name, getTime);

        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Test_ResultActivity.this, Test_ResultDetailActivity.class);
                intent.putExtra("answers", answers);
                intent.putExtra("category_name", category_name);
                intent.putExtra("test_type", test_type);
                startActivity(intent);
            }
        });
    }
}
