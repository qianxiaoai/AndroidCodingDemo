package cn.lovexiaoai.androidcodingdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.lovexiaoai.androidcodingdemo.R;
import cn.lovexiaoai.androidcodingdemo.widget.PagerSlidingTabStrip;

/**
 * Created by qianxiaoai on 2018/8/14
 */
public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private List<String> mStringList = new ArrayList<>(Arrays.asList("常用", "记录", "自定义", "其他"));
    private List<Fragment> fragments;// 页面

    private MainPagerAdapter mainPagerAdapter;


    private ViewPager.OnPageChangeListener mOnPageChangeListener = new android.support.v4.view.ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.addOnPageChangeListener(this);

        viewPager.addOnPageChangeListener(mOnPageChangeListener);


        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new TwoFragment());
        fragments.add(new ThreeFragment());
        fragments.add(new FourFragment());

        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainPagerAdapter);

        PagerSlidingTabStrip indicator = (PagerSlidingTabStrip) findViewById(R.id.indicator);

        indicator.setDividerColorResource(android.R.color.transparent);
        indicator.setIndicatorColorResource(R.color.xiaoai_color_3fbfbd);
        indicator.setIndicatorHeight(4);
        indicator.setUnderlineColorResource(android.R.color.transparent);
        indicator.setShouldExpand(true);
        indicator.setDividerPadding(24);
        indicator.setTextColorResource(R.color.xiaoai_color_000000);
        indicator.setIndicatorSelectTextColorResource(R.color.xiaoai_color_3fbfbd);
        indicator.setTextSize(14);
        indicator.setTabPaddingLeftRight(12);


        indicator.setViewPager(viewPager);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class MainPagerAdapter extends FragmentPagerAdapter {

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mStringList.get(position);
        }
    }
}
