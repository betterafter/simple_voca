package com.danerdaner.activity;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    private static boolean isNavigationButtonTouched = false;

    public static RecyclerView.SmoothScroller smoothScroller;
    private static HorizontalScrollView navigationScrollView;

    private TextView CategoryTitle;
    private TextView CategorySubTitle;


    public LinearLayout main_voca_parent_page_list;
    public static HorizontalScrollView main_voca_page_list;
    public static LinearLayout main_voca_page_list_layout;
    public boolean isIndexerAppear;

    public static RecyclerView main_recyclerView;
    private GridView main_gridView;



    public static VocaRecyclerViewAdapter vocaRecyclerViewAdapter;
    public static VocaGridViewAdapter vocaGridViewAdapter;

    public static TextToSpeech tts;


    public static MediaPlayer player;

    private static int firstVisibleItemPosition;
    private static int lastVisibleItemPosition;


    private BottomNavigationView bottomNavigationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // ?????? ??????????????? ??? ??????
        bottomNavigationView = findViewById(R.id.main_voca_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.action_category:
                        Intent intent1 = new Intent(MainActivity.this, Category_MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.action_add:
                        Intent intent = new Intent(MainActivity.this, AddEditVocaActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_setting:
                        Intent intent2 = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent2);
                        break;
                }

                return false;
            }
        });



        isNavigationButtonTouched = false;


        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // ????????? ????????????.
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });




        mainActivity = this;

        CategoryTitle = findViewById(R.id.main_category_name);
        CategorySubTitle = findViewById(R.id.main_category_subtitle);


        // ?????????????????? ????????? ??????
        main_recyclerView = findViewById(R.id.main_recyclerview);
        vocaRecyclerViewAdapter = new VocaRecyclerViewAdapter(LoadingActivity.vocaList, getApplicationContext(), MainActivity.this);
        main_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        main_recyclerView.setAdapter(vocaRecyclerViewAdapter);

        ItemTouchHelperCallback itemTouchHelperCallback = new ItemTouchHelperCallback(getApplicationContext());
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchhelper.attachToRecyclerView(main_recyclerView);


        // ????????? ???????????? ???????????? ?????????
        main_voca_page_list = findViewById(R.id.main_voca_page_list);
        main_voca_page_list_layout = findViewById(R.id.main_voca_page_list_layout);
        main_voca_parent_page_list = findViewById(R.id.main_voca_page_parent_list);

        isIndexerAppear = false;
        main_voca_parent_page_list.setVisibility(View.GONE);

        ImageButton indexer = findViewById(R.id.indexer);
        indexer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isIndexerAppear) {
                    main_voca_parent_page_list.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.indexer_move_appear);
                    main_voca_parent_page_list.startAnimation(animation);
                    isIndexerAppear = true;
                }
                else{
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.indexer_move_disappear);
                    main_voca_parent_page_list.startAnimation(animation);
                    main_voca_parent_page_list.setVisibility(View.GONE);
                    isIndexerAppear = false;
                }
            }
        });

        // ???????????? ??????
        player = new MediaPlayer();


        // ?????? ??? ????????? ?????? ?????? ?????? ..................................................................
        ImageButton visible_button = findViewById(R.id.main_mean_visible_button);
        visible_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // ??? ?????? ??? ??? ??? ????????? ?????? ????????? ???????????? ?????? ?????????
                if(ItemTouchHelperCallback.viewHolders.size() > 0) {
                    ItemTouchHelperCallback.viewHolders.remove(0);
                }

                // ?????? ??? ????????????
                if(WORD_MEAN_VISIBLE){
                    WORD_MEAN_VISIBLE = false;
                    visible_button.setImageDrawable(
                            ContextCompat.getDrawable(getApplicationContext(), R.drawable.outline_visibility_off_24)
                    );
                }
                // ?????? ??? ?????????
                else{
                    WORD_MEAN_VISIBLE = true;
                    visible_button.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.outline_visibility_24));
                }

                vocaRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
        //..........................................................................................



        navigationScrollView = findViewById(R.id.main_voca_page_list);
    }


    @Override
    protected void onResume() {
        super.onResume();

        System.out.println(LoadingActivity.SELECTED_CATEGORY_NAME);
        LoadingActivity.vocaDatabase.makeList(LoadingActivity.vocaList);
        vocaRecyclerViewAdapter.notifyDataSetChanged();

        CategoryTitle.setText(LoadingActivity.SELECTED_CATEGORY_NAME);
        CategorySubTitle.setText(LoadingActivity.SELECTED_CATEGORY_SUBTITLE);

        isIndexerAppear = false;
        main_voca_parent_page_list.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        selectedNumber = 0;

        // ??? ????????? ?????? ????????? ???????????? ?????? ?????????
        if (ItemTouchHelperCallback.viewHolders.size() > 0) {
            vocaRecyclerViewAdapter.
                    notifyItemChanged(ItemTouchHelperCallback.viewHolders.get(0).getViewHolder().getAdapterPosition());
            ItemTouchHelperCallback.viewHolders.remove(0);
        }

        main_recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                firstVisibleItemPosition
                        = ((LinearLayoutManager) main_recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                lastVisibleItemPosition
                        = ((LinearLayoutManager) main_recyclerView.getLayoutManager()).findLastVisibleItemPosition();

                MakeListPager();


                onRecyclerViewScrollListener(main_recyclerView);
                main_recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }











    // ????????????????????? ??? navigation list (??????)
    public static void MakeListPager(){

        main_voca_page_list_layout.removeAllViews();
        selectedButtons.clear();

        smoothScroller = new LinearSmoothScroller(mainActivity.getApplicationContext()) {
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

        int listSize = lastVisibleItemPosition - firstVisibleItemPosition;

        int len = 0;
        if(LoadingActivity.vocaList.size() % (listSize + 1) == 0){
            len = LoadingActivity.vocaList.size() / (listSize + 1);
        }
        else{
            len = LoadingActivity.vocaList.size() / (listSize + 1) + 1;
        }

        for(int i = 0; i < len; i++){

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

            // ?????? ?????? ?????????
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    isNavigationButtonTouched = true;
                    vocaRecyclerViewAdapter.notifyDataSetChanged();

                    // ??????????????? ?????? ?????? ??? ????????? -----------------------------------------------------

                    // ?????? ?????? ?????? ??? ?????????
                    selectedButtons.get(selectedNumber)
                            .setTextColor(mainActivity.getResources().getColor(R.color.backgroundBlack));
                    selectedButtons.get(selectedNumber)
                            .getBackground().setColorFilter(mainActivity.getResources().getColor(R.color.white),
                            PorterDuff.Mode.SRC_IN);

                    int prevSelectedNumber = selectedNumber;
                    // ?????? ?????? ????????? ??????
                    for(int i = 0; i < selectedButtons.size(); i++){
                        if(button == selectedButtons.get(i)){
                            selectedNumber = i; break;
                        }
                    }
                    if(selectedNumber >= selectedButtons.size())
                        selectedNumber = selectedButtons.size() - 1;

                    // ?????? ?????? ??? ?????????
                    selectedButtons.get(selectedNumber).setTextColor(mainActivity.getResources().getColor(R.color.white));
                    selectedButtons
                            .get(selectedNumber).getBackground().setColorFilter(mainActivity.getResources().getColor(R.color.mainBlue),
                            PorterDuff.Mode.SRC_IN);
                    ///////////////////////////////////////////////////////////////////////////////



                    // ????????? ?????? smoothScrollToPosition ?????? ???????????? ????????? ????????? ????????? ?????? ???????????? ??????????????? ???????????? ??? ??? ??????
                    if(Math.abs(prevSelectedNumber - selectedNumber) <= 5) {
                        smoothScroller.setTargetPosition(temp * (listSize + 1));
                        ((LinearLayoutManager) main_recyclerView.getLayoutManager()).startSmoothScroll(smoothScroller);
                    }
                    else
                        ((LinearLayoutManager)main_recyclerView.getLayoutManager()).
                                scrollToPositionWithOffset(temp * (listSize + 1), 0);
                }
            });

            selectedButtons.add(button);
            main_voca_page_list_layout.addView(button);
        }

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                (width / 7) * 4, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        main_voca_page_list_layout.setLayoutParams(params);
//
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                (width / 7) * 4, (width / 7)
        );
        //params2.gravity = Gravity.CENTER;
        //main_voca_page_list.getParent().
        params2.gravity = Gravity.BOTTOM;
        main_voca_page_list.setLayoutParams(params2);

    }











    public static void onRecyclerViewScrollListener(RecyclerView recyclerView){

        recyclerView.clearOnScrollListeners();
        selectedNumber = 0;
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

            boolean isFirstTouchedToDrag = true;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == recyclerView.SCROLL_STATE_IDLE){
                    isNavigationButtonTouched = false;
                    isFirstTouchedToDrag = true;
                }
                else if(newState == recyclerView.SCROLL_STATE_DRAGGING){
                    LoadingActivity.vocaDatabase.makeList(LoadingActivity.vocaList);
                    vocaRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (selectedButtons.size() > 0) {

                    if (!isNavigationButtonTouched) {

                        int firstVisibleItemPosition
                                = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

                        int lastVisibleItemPosition
                                = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();


                        try {
                            // ?????? ?????? ?????? ??? ?????????
                            selectedButtons.get(selectedNumber)
                                    .setTextColor(mainActivity.getResources().getColor(R.color.backgroundBlack));
                            selectedButtons.get(selectedNumber)
                                    .getBackground().setColorFilter(mainActivity.getResources().getColor(R.color.white),
                                    PorterDuff.Mode.SRC_IN);

                            selectedNumber = firstVisibleItemPosition / 4;
                            if(selectedNumber >= selectedButtons.size())
                                selectedNumber = selectedButtons.size() - 1;

                            // ?????? ?????? ??? ?????????
                            selectedButtons.get(selectedNumber).setTextColor(mainActivity.getResources().getColor(R.color.white));
                            selectedButtons.get(selectedNumber).getBackground().
                                    setColorFilter(mainActivity.getResources().getColor(R.color.mainBlue), PorterDuff.Mode.SRC_IN);


                            if (lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1) {

                                // ?????? ?????? ?????? ??? ?????????
                                selectedButtons.get(selectedNumber)
                                        .setTextColor(mainActivity.getResources().getColor(R.color.backgroundBlack));
                                selectedButtons.get(selectedNumber)
                                        .getBackground().setColorFilter(mainActivity.getResources().getColor(R.color.white),
                                        PorterDuff.Mode.SRC_IN);

                                selectedNumber = ((LinearLayout) navigationScrollView.getChildAt(0)).getChildCount() - 1;

                                // ?????? ?????? ??? ?????????
                                selectedButtons.get(selectedNumber).setTextColor(mainActivity.getResources().getColor(R.color.white));
                                selectedButtons.get(selectedNumber).getBackground().
                                        setColorFilter(mainActivity.getResources().getColor(R.color.mainBlue), PorterDuff.Mode.SRC_IN);

                            }

                            if (selectedNumber % 5 == 0) {
                                float position = ((LinearLayout) navigationScrollView.getChildAt(0)).getChildAt(selectedNumber).getX();
                                navigationScrollView.scrollTo((int) position, 0);
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }


            }
        };
        recyclerView.addOnScrollListener(onScrollListener);
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TTS ????????? ??????????????? ????????? ???????????? ??????????????? ????????????.
        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}