package com.example.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;

import com.example.adapter.VocaGridViewAdapter;
import com.example.adapter.VocaRecyclerViewAdapter;
import com.example.simple_voca.ItemTouchHelperCallback;
import com.example.simple_voca.R;
import com.example.simple_voca.VocaForegroundService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private ImageButton main_add_word_button;
    private ImageButton main_change_list_type_button;

    private RecyclerView main_recyclerView;
    private GridView main_gridView;

    private VocaRecyclerViewAdapter vocaRecyclerViewAdapter;
    private VocaGridViewAdapter vocaGridViewAdapter;

    private ItemTouchHelper itemTouchHelper;


    public static MediaPlayer player;

    private boolean isRecyclerViewActivated = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_change_list_type_button = findViewById(R.id.main_change_style);

        // 단어 추가 버튼 기능
        main_add_word_button = findViewById(R.id.main_add_word_button);
        main_add_word_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddVocaActivity.class);
                startActivity(intent);
            }
        });

        // FrameLayout에 GridView와 RecyclerView를 같이 넣어서 버튼을 눌렀을 때 전환이 되게 만듬.
        // 리사이클러뷰 생성
        main_recyclerView = findViewById(R.id.main_recyclerview);
        main_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰 어뎁터 생성
        vocaRecyclerViewAdapter = new VocaRecyclerViewAdapter(LoadingActivity.vocaList, getApplicationContext());
        main_recyclerView.setAdapter(vocaRecyclerViewAdapter);

        ItemTouchHelperCallback itemTouchHelperCallback = new ItemTouchHelperCallback(getApplicationContext());
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchhelper.attachToRecyclerView(main_recyclerView);




        // 그리드뷰 생성
        player = new MediaPlayer();

        main_gridView = (GridView)findViewById(R.id.gridView);

        vocaGridViewAdapter = new VocaGridViewAdapter(LoadingActivity.vocaList, getApplicationContext());
        main_gridView.setAdapter(vocaGridViewAdapter);



        // 서비스 인텐트 생성 후 서비스 실행. 이 때 오레오 이전 버전과 이후 버전에서 서비스를 시작하는 방식이 조금 다르다.
        Intent serviceIntent = new Intent(MainActivity.this, VocaForegroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForegroundService(serviceIntent);
        else startService(serviceIntent);
    }

    public void onFrameLayoutChangedClick(View view){
        if(isRecyclerViewActivated){
            main_recyclerView.setVisibility(View.GONE);
            main_gridView.setVisibility(View.VISIBLE);
            isRecyclerViewActivated = false;
            main_change_list_type_button.setImageDrawable(getResources().getDrawable(R.drawable.baseline_view_list_24));
        }
        else {
            main_recyclerView.setVisibility(View.VISIBLE);
            main_gridView.setVisibility(View.GONE);
            isRecyclerViewActivated = true;
            main_change_list_type_button.setImageDrawable(getResources().getDrawable(R.drawable.baseline_view_module_20));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        vocaRecyclerViewAdapter.notifyDataSetChanged();
    }
}