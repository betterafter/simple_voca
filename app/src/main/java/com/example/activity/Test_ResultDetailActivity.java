package com.example.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.adapter.TestResultViewAdapter;
import com.example.simple_voca.R;
import com.example.simple_voca.TestAnswer;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Test_ResultDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TestResultViewAdapter viewAdapter;
    private String category_name;
    private String test_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result_detail);


        // 리사이클러뷰를 이용해 오답 노트를 만들어보기.
        // 물론 리사이클러뷰 뿐만 아니라 리니어 레이아웃을 사용해도 좋고, 사용할 수 있는 모든 방법을 동원하면 됨
        // 여기까지 하는데 꽤 어려울 것이라 생각은 해서 모르면 찾는데 시간 오래 쓰지말고 같이해도 좋음
        Intent intent = getIntent();
        ArrayList<TestAnswer> answers = (ArrayList<TestAnswer>)intent.getSerializableExtra("answers");
        category_name = intent.getStringExtra("category_name");
        test_type = intent.getStringExtra("test_type");

        recyclerView = findViewById(R.id.test_result_detail_recyclerView);
        viewAdapter = new TestResultViewAdapter(answers, getApplicationContext(), category_name, test_type);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(viewAdapter);
    }
}
