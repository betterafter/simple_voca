package com.danerdaner.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.danerdaner.fragment.TutorialFragment_1;
import com.danerdaner.fragment.TutorialFragment_2;
import com.danerdaner.fragment.TutorialFragment_3;
import com.danerdaner.fragment.TutorialFragment_4;
import com.danerdaner.fragment.TutorialFragment_5;
import com.danerdaner.simple_voca.R;
import com.danerdaner.simple_voca.TutorialChecker;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class TutorialViewPagerActivity extends AppCompatActivity {

    private TextView next_button;
    /**
     * 프레그먼트 출력
     */
    private ViewPager mPager;

    /**
     * 프레그먼트 컨트롤러
     */
    private PagerAdapter pagerAdapter;
    private DotsIndicator dotsIndicator;


    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;
    private static final int ACTION_MANAGE_READ_REQUEST_CODE = 2;


    @RequiresApi(api = Build.VERSION_CODES.O)
    // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_view_pager);

        next_button = findViewById(R.id.tutorial_next_button);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        if(TutorialChecker.checkUserUseTutorial(getApplicationContext())){
            mPager.setVisibility(View.GONE);
        }
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position == pagerAdapter.getCount() - 1){
                    next_button.setText("DONE");
                }
                else next_button.setText("NEXT");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });



        dotsIndicator = findViewById(R.id.dots_indicator);
        dotsIndicator.setViewPager(mPager);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우

            int permission_read = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
            int permission_write = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if(permission_read == PackageManager.PERMISSION_DENIED ||
                    permission_write == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, ACTION_MANAGE_READ_REQUEST_CODE);
            }
            else{
                if(TutorialChecker.checkUserUseTutorial(getApplicationContext())){
                    mPager.setVisibility(View.GONE);
                    Intent intent = new Intent(TutorialViewPagerActivity.this, LoadingActivity.class);
                    intent.putExtra("isFirstStart", false);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();

        getPermission();
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        /**
         * 페이지의 수
         */
        private final int NUM_PAGES = 5;

        private Fragment[] fragment = new Fragment[NUM_PAGES];


        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            /*
            이 부분을 지우고, fragment변수의 각 인덱스에서 표시될 프레그먼트를 넣습니다.
            NUM_PAGES는 이 프레그먼트의 총 개수로 사용하시면 됩니다.
            ex ):
            예를 들어 AddWordTutorialFragment라는 프레그먼트 클래스를 만들었고,
            5번째 슬라이더에 표시되는 것을 원하신다면,
            fragment[4] = new AddWordTutorialFragment();
            와 같이 작성 가능합니다.

            인덱스 순서대로, 0부터 출력됩니다.
            아래 부분의 초기화 처럼 작성하시면 됩니다.
            아래에서는 현재, 마땅한 프레그먼트가 존재하지 않아서,
            테스트로 만든 ScreenSlidePageFragment를 사용하여 0~4를 초기화 하고 있습니다.
            */

            fragment[0] = new TutorialFragment_1();
            fragment[1] = new TutorialFragment_2();
            fragment[2] = new TutorialFragment_3();
            fragment[3] = new TutorialFragment_4();
            fragment[4] = new TutorialFragment_5();
        }

        @Override
        public Fragment getItem(int position) {
            return fragment[position];
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onTutorialClick(View view){

        if(mPager.getCurrentItem() == pagerAdapter.getCount() - 1){
            mPager.setVisibility(View.GONE);
            TutorialChecker.processUserUseTutorial(getApplicationContext());
            Intent intent = new Intent(TutorialViewPagerActivity.this, LoadingActivity.class);
            intent.putExtra("isFirstStart", true);
            startActivity(intent);
            finish();
        }
        else{
            mPager.setCurrentItem(mPager.getCurrentItem() + 1);
        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            //super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }
}
