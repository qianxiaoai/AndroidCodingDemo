package cn.lovexiaoai.androidcodingdemo.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity

import java.util.ArrayList
import java.util.Arrays

import cn.lovexiaoai.androidcodingdemo.R
import cn.lovexiaoai.androidcodingdemo.widget.PagerSlidingTabStrip

/**
 * Created by qianxiaoai on 2018/8/14
 */
class MainActivity2 : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private val mStringList = ArrayList(Arrays.asList("常用", "记录", "自定义", "其他"))
    private var fragments: MutableList<Fragment>? = null// 页面

    private var mainPagerAdapter: MainPagerAdapter? = null


    private val mOnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {

        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewPager = findViewById(R.id.view_pager) as ViewPager

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        viewPager.addOnPageChangeListener(this)

        viewPager.addOnPageChangeListener(mOnPageChangeListener)


        fragments = ArrayList()
        fragments!!.add(HomeFragment())
        fragments!!.add(TwoFragment())
        fragments!!.add(ThreeFragment())
        fragments!!.add(FourFragment())

        mainPagerAdapter = MainPagerAdapter(supportFragmentManager)
        viewPager.adapter = mainPagerAdapter

        val indicator = findViewById(R.id.indicator) as PagerSlidingTabStrip

        indicator.setDividerColorResource(android.R.color.transparent)
        indicator.setIndicatorColorResource(R.color.xiaoai_color_3fbfbd)
        indicator.indicatorHeight = 4
        indicator.setUnderlineColorResource(android.R.color.transparent)
        indicator.shouldExpand = true
        indicator.dividerPadding = 24
        indicator.setTextColorResource(R.color.xiaoai_color_000000)
        indicator.indicatorSelectTextColorResource = R.color.xiaoai_color_3fbfbd
        indicator.textSize = 14
        indicator.tabPaddingLeftRight = 12


        indicator.setViewPager(viewPager)

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    internal inner class MainPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return fragments!![position]
        }

        override fun getCount(): Int {
            return fragments!!.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mStringList[position]
        }
    }
}
