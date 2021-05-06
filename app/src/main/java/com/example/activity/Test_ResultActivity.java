package com.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.simple_voca.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import com.example.simple_voca.TestAnswer;

public class Test_ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        // 아이디어는 동적으로 이미지뷰와 텍스트뷰를 만들어서 결과의 그림과 같은 형태로 만드는 것.
        // 그리드뷰를 이용해도 상관없고, 리니어 레이아웃으로 테이블 형태를 만들어도 상관 없음.

        // activity_test_result에서 버튼을 하나 만들고, 해당 버튼을 누르면 intent를 이용하여 오답으로 넘어갈 수 있게 만들기
        Intent intent = getIntent();
        ArrayList<TestAnswer> answers = (ArrayList<TestAnswer>)intent.getSerializableExtra("answers");

        GridLayout g = findViewById(R.id.test_girdlayout);
        Button b = findViewById(R.id.test_result_detail);

        for(int i=0;i<answers.size();i++){
            TextView t = new TextView(this);
            if(answers.get(i).isCorrect())
                t.setText("O");
            else
                t.setText("X");
            g.addView(t);
        }

        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Test_ResultActivity.this, Test_ResultDetailActivity.class);
                intent.putExtra("answers", answers);
                startActivity(intent);
            }
        });
    }
}
