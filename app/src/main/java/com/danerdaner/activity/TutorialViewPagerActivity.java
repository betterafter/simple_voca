package com.danerdaner.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.danerdaner.simple_voca.R;
import com.danerdaner.simple_voca.ScreenSlidePageFragment;

public class TutorialViewPagerActivity extends AppCompatActivity {

    /**
     * 프레그먼트 출력
     */
    private ViewPager mPager;

    /**
     * 프레그먼트 컨트롤러
     */
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_view_pager);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
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

            fragment[0] = new ScreenSlidePageFragment();
            fragment[1] = new ScreenSlidePageFragment();
            fragment[2] = new ScreenSlidePageFragment();
            fragment[3] = new ScreenSlidePageFragment();
            fragment[4] = new ScreenSlidePageFragment();
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
}