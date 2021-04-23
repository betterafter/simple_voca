package com.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.simple_voca.R;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Test_MainActivity extends AppCompatActivity {

    private LinearLayout important_word_test_button;
    private LinearLayout all_word_test_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);

        // 단어 리스트는 이런 형식으로 만들게 될 거임. 일단 이걸로 테스트해볼 것.
        ArrayList<String[]> SampleList = new ArrayList<>();
        SampleList.add(new String[]{"apple", "사과", "[announce]"});
        SampleList.add(new String[]{"banana", "바나나", "[announce]"});
        SampleList.add(new String[]{"pineapple", "파인애플", "[announce]"});
        SampleList.add(new String[]{"korea", "한국", "[announce]"});
        SampleList.add(new String[]{"usa", "미국", "[announce]"});
        SampleList.add(new String[]{"china", "중국", "[announce]"});
        SampleList.add(new String[]{"japan", "일본", "[announce]"});
        SampleList.add(new String[]{"ipad", "아이패드", "[announce]"});
        SampleList.add(new String[]{"i", "나는", "[announce]"});
        SampleList.add(new String[]{"dice", "주사위", "[announce]"});
        SampleList.add(new String[]{"pen", "펜", "[announce]"});
        SampleList.add(new String[]{"pencil", "연필", "[announce]"});
        SampleList.add(new String[]{"smart", "영리한", "[announce]"});
        SampleList.add(new String[]{"sun", "태양", "[announce]"});
        SampleList.add(new String[]{"moon", "달", "[announce]"});
        SampleList.add(new String[]{"monitor", "모니터", "[announce]"});
        SampleList.add(new String[]{"orange", "오렌지", "[announce]"});
        SampleList.add(new String[]{"speaker", "스피커", "[announce]"});
        SampleList.add(new String[]{"keyborad", "키보드", "[announce]"});
        SampleList.add(new String[]{"mouse", "쥐", "[announce]"});


        important_word_test_button = findViewById(R.id.important_word_test_button);
        important_word_test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Test_MainActivity.this, Test_TestActivity.class);
                intent.putExtra("list", SampleList);
                startActivity(intent);
            }
        });


    }
}
