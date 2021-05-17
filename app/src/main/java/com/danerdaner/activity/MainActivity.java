package com.danerdaner.activity;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.danerdaner.adapter.VocaGridViewAdapter;
import com.danerdaner.adapter.VocaRecyclerViewAdapter;
import com.danerdaner.simple_voca.ItemTouchHelperCallback;
import com.danerdaner.simple_voca.R;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import static android.speech.tts.TextToSpeech.ERROR;

public class MainActivity extends AppCompatActivity {

    public static MainActivity mainActivity;
    public static boolean WORD_MEAN_VISIBLE = true;

    public static ArrayList<Button> selectedButtons = new ArrayList<>();
    public static int selectedNumber = 0;
    private static boolean isNavigationButtonTouched = true;


    private final int PARENT_VIEW = 0;
    private final int CHILD_VIEW = 1;


    private ImageButton main_add_word_button;
    private ImageButton main_category_button;
    private HorizontalScrollView navigationScrollView;

    private TextView CategoryTitle;
    private TextView CategorySubTitle;


    public static HorizontalScrollView main_voca_page_list;
    public static LinearLayout main_voca_page_list_layout;
    public static RecyclerView main_recyclerView;
    private GridView main_gridView;
    //private ViewPager2 viewPager2;



    public static VocaRecyclerViewAdapter vocaRecyclerViewAdapter;
    public static VocaGridViewAdapter vocaGridViewAdapter;
    //private VocaViewPagerAdapter viewPagerAdapter;

    public static TextToSpeech tts;


    public static MediaPlayer player;

    private boolean isRecyclerViewActivated = true;

    //private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });




        mainActivity = this;

        CategoryTitle = findViewById(R.id.main_category_name);
        CategorySubTitle = findViewById(R.id.main_category_subtitle);

        main_category_button = findViewById(R.id.main_category_button);
        main_category_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Category_MainActivity.class);
                startActivity(intent);
            }
        });

        // 단어 추가 버튼 기능
        main_add_word_button = findViewById(R.id.main_add_word_button);
        main_add_word_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditVocaActivity.class);
                startActivity(intent);
            }
        });


        // 셔플 기능
//        main_swap_button = findViewById(R.id.main_swap_button);
//        main_swap_button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Collections.shuffle(LoadingActivity.vocaList);
//                vocaGridViewAdapter.notifyDataSetChanged();
//                vocaRecyclerViewAdapter.notifyDataSetChanged();
//            }
//        });


        // 리사이클러뷰 어뎁터 생성
        main_recyclerView = findViewById(R.id.main_recyclerview);
        vocaRecyclerViewAdapter = new VocaRecyclerViewAdapter(LoadingActivity.vocaList, getApplicationContext());
        main_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        main_recyclerView.setAdapter(vocaRecyclerViewAdapter);

        ItemTouchHelperCallback itemTouchHelperCallback = new ItemTouchHelperCallback(getApplicationContext());
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchhelper.attachToRecyclerView(main_recyclerView);


        // 리스트 페이지가 몇개인지 만들기
        main_voca_page_list = findViewById(R.id.main_voca_page_list);
        main_voca_page_list_layout = findViewById(R.id.main_voca_page_list_layout);


        // 그리드뷰 생성
        player = new MediaPlayer();

        main_gridView = (GridView)findViewById(R.id.gridView);

        vocaGridViewAdapter = new VocaGridViewAdapter(LoadingActivity.vocaList, getApplicationContext());
        main_gridView.setAdapter(vocaGridViewAdapter);



        // 단어 뜻 가리기 버튼 기능 구현 ..................................................................
        ImageButton visible_button = findViewById(R.id.main_mean_visible_button);
        visible_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 눈 클릭 할 때 각 아이템 메뉴 옵션들 보이는거 전부 없애기
                if(ItemTouchHelperCallback.viewHolders.size() > 0) {
                    ItemTouchHelperCallback.viewHolders.remove(0);
                }

                // 단어 뜻 안보이기
                if(WORD_MEAN_VISIBLE){
                    WORD_MEAN_VISIBLE = false;
                    visible_button.setImageDrawable(
                            ContextCompat.getDrawable(getApplicationContext(), R.drawable.outline_visibility_off_24)
                    );
                    LoadingActivity.vocaDatabase.makeEmptyMeanList(LoadingActivity.vocaList);
                }
                // 단어 뜻 보이기
                else{
                    WORD_MEAN_VISIBLE = true;
                    visible_button.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.outline_visibility_24));
                    LoadingActivity.vocaDatabase.makeList(LoadingActivity.vocaList);
                }

                //vocaRecyclerViewAdapter.notifyDataSetChanged();
                vocaRecyclerViewAdapter.notifyItemRangeChanged(0, LoadingActivity.vocaList.size());
            }
        });
        //..........................................................................................


        onRecyclerViewScrollListener(main_recyclerView);
        navigationScrollView = findViewById(R.id.main_voca_page_list);



    }






    @Override
    protected void onStart() {
        super.onStart();
    }





    @Override
    protected void onResume() {
        super.onResume();
        vocaRecyclerViewAdapter.notifyDataSetChanged();
        selectedNumber = 0;
        CategoryTitle.setText(LoadingActivity.SELECTED_CATEGORY_NAME);
        CategorySubTitle.setText(LoadingActivity.SELECTED_CATEGORY_SUBTITLE);

        // 각 아이템 메뉴 옵션들 보이는거 전부 없애기
        if(ItemTouchHelperCallback.viewHolders.size() > 0) {
            vocaRecyclerViewAdapter.
                    notifyItemChanged(ItemTouchHelperCallback.viewHolders.get(0).getViewHolder().getAdapterPosition());
            ItemTouchHelperCallback.viewHolders.remove(0);
        }

        MakeListPager();
    }





    // 리사이클러뷰일 때 navigation list (목차)
    public static void MakeListPager(){

        main_voca_page_list_layout.removeAllViews();
        selectedButtons.clear();

        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(mainActivity.getApplicationContext()) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };

        Display display = mainActivity.getWindowManager().getDefaultDisplay();  // in Activity
        /* getActivity().getWindowManager().getDefaultDisplay() */ // in Fragment
        Point size = new Point();
        display.getRealSize(size); // or getSize(size)
        int width = size.x;
        int height = size.y;

        for(int i = 0; i < (double)LoadingActivity.vocaList.size() / 4; i++){

            final int temp = i;
            Button button = new Button(mainActivity.getApplicationContext());
            button.setText(Integer.toString(i + 1));

            if(i == 0){
                button.setTextColor(mainActivity.getResources().getColor(R.color.white));
                button.getBackground().setColorFilter(mainActivity.getResources().getColor(R.color.mainBlue),
                        PorterDuff.Mode.SRC_IN);


            }
            else{
                button.setTextColor(mainActivity.getResources().getColor(R.color.backgroundBlack));
                button.getBackground().setColorFilter(mainActivity.getResources().getColor(R.color.white),
                        PorterDuff.Mode.SRC_IN);
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    width / 7, width / 7
            );
            button.setLayoutParams(params);

            // 버튼 클릭 이벤트
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    isNavigationButtonTouched = true;

                    LoadingActivity.vocaDatabase.makeList(LoadingActivity.vocaList);
                    vocaRecyclerViewAdapter.notifyDataSetChanged();

                    // 네비게이션 목차 버튼 색 바꾸기 -----------------------------------------------------

                    // 이전 선택 버튼 색 바꾸기
                    selectedButtons.get(selectedNumber)
                            .setTextColor(mainActivity.getResources().getColor(R.color.backgroundBlack));
                    selectedButtons.get(selectedNumber)
                            .getBackground().setColorFilter(mainActivity.getResources().getColor(R.color.white),
                            PorterDuff.Mode.SRC_IN);

                    int prevSelectedNumber = selectedNumber;
                    // 선택 버튼 포지션 저장
                    for(int i = 0; i < selectedButtons.size(); i++){
                        if(button == selectedButtons.get(i)){
                            selectedNumber = i; break;
                        }
                    }

                    // 선택 버튼 색 바꾸기
                    selectedButtons.get(selectedNumber).setTextColor(mainActivity.getResources().getColor(R.color.white));
                    selectedButtons
                            .get(selectedNumber).getBackground().setColorFilter(mainActivity.getResources().getColor(R.color.mainBlue),
                            PorterDuff.Mode.SRC_IN);
                    ///////////////////////////////////////////////////////////////////////////////



                    // 이렇게 하면 smoothScrollToPosition 써서 인덱싱이 제대로 안되는 문제를 신경 안쓰고도 스무스하게 움직이게 할 수 있음
                    if(Math.abs(prevSelectedNumber - selectedNumber) <= 5) {
                        smoothScroller.setTargetPosition(temp * 4);
                        ((LinearLayoutManager) main_recyclerView.getLayoutManager()).startSmoothScroll(smoothScroller);
                    }
                    else
                        ((LinearLayoutManager)main_recyclerView.getLayoutManager()).
                                scrollToPositionWithOffset(temp * 4, 0);
                }
            });

            selectedButtons.add(button);
            main_voca_page_list_layout.addView(button);
        }

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                (width / 7) * 5, ViewGroup.LayoutParams.MATCH_PARENT
        );
        main_voca_page_list_layout.setLayoutParams(params);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                (width / 7) * 5, 0
        );
        //params2.gravity = Gravity.CENTER;
        params2.gravity = Gravity.CENTER_HORIZONTAL;
        params2.weight = 1;
        main_voca_page_list.setLayoutParams(params2);
    }





    public void onRecyclerViewScrollListener(RecyclerView recyclerView){

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == recyclerView.SCROLL_STATE_IDLE){
                    isNavigationButtonTouched = false;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 스크롤 할 때 각 아이템 메뉴 옵션들 보이는거 전부 없애기
                if(ItemTouchHelperCallback.viewHolders.size() > 0) {
                    recyclerView.getAdapter().
                            notifyItemChanged(ItemTouchHelperCallback.viewHolders.get(0).getViewHolder().getAdapterPosition());
                    ItemTouchHelperCallback.viewHolders.remove(0);
                }

                if (!isNavigationButtonTouched) {

                    int firstVisibleItemPosition
                            = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

                    int lastVisibleItemPosition
                            = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();


                    // 이전 선택 버튼 색 바꾸기
                    selectedButtons.get(selectedNumber)
                            .setTextColor(mainActivity.getResources().getColor(R.color.backgroundBlack));
                    selectedButtons.get(selectedNumber)
                            .getBackground().setColorFilter(mainActivity.getResources().getColor(R.color.white),
                            PorterDuff.Mode.SRC_IN);

                    selectedNumber = firstVisibleItemPosition / 4;

                    // 선택 버튼 색 바꾸기
                    selectedButtons.get(selectedNumber).setTextColor(mainActivity.getResources().getColor(R.color.white));
                    selectedButtons.get(selectedNumber).getBackground().
                            setColorFilter(mainActivity.getResources().getColor(R.color.mainBlue), PorterDuff.Mode.SRC_IN);


                    if (lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1) {

                        // 이전 선택 버튼 색 바꾸기
                        selectedButtons.get(selectedNumber)
                                .setTextColor(mainActivity.getResources().getColor(R.color.backgroundBlack));
                        selectedButtons.get(selectedNumber)
                                .getBackground().setColorFilter(mainActivity.getResources().getColor(R.color.white),
                                PorterDuff.Mode.SRC_IN);

                        selectedNumber = ((LinearLayout) navigationScrollView.getChildAt(0)).getChildCount() - 1;

                        // 선택 버튼 색 바꾸기
                        selectedButtons.get(selectedNumber).setTextColor(mainActivity.getResources().getColor(R.color.white));
                        selectedButtons.get(selectedNumber).getBackground().
                                setColorFilter(mainActivity.getResources().getColor(R.color.mainBlue), PorterDuff.Mode.SRC_IN);

                    }

                    if (selectedNumber % 5 == 0) {
                        float position = ((LinearLayout) navigationScrollView.getChildAt(0)).getChildAt(selectedNumber).getX();
                        navigationScrollView.scrollTo((int) position, 0);
                    }
                }
            }
        });
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TTS 객체가 남아있다면 실행을 중지하고 메모리에서 제거한다.
        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}