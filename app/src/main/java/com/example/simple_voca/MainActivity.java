package com.example.simple_voca;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private Button main_add_word_button;
    private RecyclerView main_recyclerView;
    private VocaRecyclerViewAdapter adapter;
    private ItemTouchHelper itemTouchHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 리사이클러뷰 생성
        main_recyclerView = findViewById(R.id.main_recyclerview);
        main_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰 어뎁터 생성
        adapter = new VocaRecyclerViewAdapter(LoadingActivity.vocaList);
        main_recyclerView.setAdapter(adapter);

        ItemTouchHelperCallback itemTouchHelperCallback = new ItemTouchHelperCallback();
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchhelper.attachToRecyclerView(main_recyclerView);

        main_add_word_button = findViewById(R.id.main_add_word_button);
        main_add_word_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddVocaActivity.class);
                startActivity(intent);
            }
        });

        // 서비스 인텐트 생성 후 서비스 실행. 이 때 오레오 이전 버전과 이후 버전에서 서비스를 시작하는 방식이 조금 다르다.
        Intent serviceIntent = new Intent(MainActivity.this, VocaForegroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForegroundService(serviceIntent);
        else startService(serviceIntent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}