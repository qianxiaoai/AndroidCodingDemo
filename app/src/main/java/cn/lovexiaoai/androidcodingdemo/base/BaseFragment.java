package cn.lovexiaoai.androidcodingdemo.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import cn.lovexiaoai.androidcodingdemo.utils.LogUtil;

/**
 * Created by tangh on 14-4-21.
 */
public abstract class BaseFragment extends Fragment {

	/**
	 * 全局activity
	 */
	protected FragmentActivity mContext;

	protected FragmentManager mFragmentManager;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFragmentManager = getChildFragmentManager();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (getContentView() != 0) {
			return inflater.inflate(getContentView(), container, false);
		}
		return getContentLayout();
	}

    /**
     * 沉浸式颜色
     * @param color
     */
    public void setStatusBarColorRes(@ColorRes int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setStatusBarColor(getResources().getColor(color));
    }

    /**
     * 沉浸式颜色
     * @param color
     */
    public void setStatusBarColor(@ColorInt int color){
		if (mContext != null &&Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mContext.getWindow().setStatusBarColor(color);
		}
    }

	public abstract int getContentView();

	public View getContentLayout() {
		return null;
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtil.d(((Object) this).getClass().getSimpleName(),"onPause");
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtil.d(((Object) this).getClass().getSimpleName(),"onResume");
	}

	@Override
	public void onStart() {
		super.onStart();
		LogUtil.d(((Object) this).getClass().getSimpleName(),"onStart");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		LogUtil.d(((Object) this).getClass().getSimpleName(),"onDetach");
	}

	@Override
	public void onStop() {
		super.onStop();
		LogUtil.d(((Object) this).getClass().getSimpleName(),"onStop");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.d(((Object) this).getClass().getSimpleName(),"onDestroy");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		LogUtil.d(((Object) this).getClass().getSimpleName(),"onDestroyView");
	}

	/**
	 * 将resid布局替换成fragment
	 *
	 * @param resid
	 * @param fragment
	 */
	public void replace(int resid, Fragment fragment) {
		mFragmentManager.beginTransaction().replace(resid, fragment).commitAllowingStateLoss();
	}

	/**
	 * @param resid
	 * @param fragment
	 */
	public void add(int resid, Fragment fragment) {
		mFragmentManager.beginTransaction().add(resid, fragment).commitAllowingStateLoss();
	}

	/**
	 * @param resid
	 * @param fragment
	 */
	public void add(int resid, Fragment fragment, String tag) {
		mFragmentManager.beginTransaction().add(resid, fragment, tag).commitAllowingStateLoss();
	}

	/**
	 * 显示fragment
	 *
	 * @param fragment
	 */
	public void show(Fragment fragment) {
		mFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss();
	}

	/**
	 * 隐藏fragment
	 *
	 * @param fragment
	 */
	public void hide(Fragment fragment) {
		mFragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss();
	}

	/**
	 * 显示fragment(带动画)
	 *
	 * @param fragment
	 */
	public void showWithAnim(Fragment fragment, int enter, int exit) {
		mFragmentManager.beginTransaction().setCustomAnimations(enter, exit).show(fragment).commitAllowingStateLoss();
	}

	/**
	 * 隐藏fragment(带动画)
	 *
	 * @param fragment
	 */
	public void hideWithAnim(Fragment fragment, int enter, int exit) {
		mFragmentManager.beginTransaction().setCustomAnimations(enter, exit).hide(fragment).commitAllowingStateLoss();
	}

	/**
	 * 异步Task访问资源前判断Fragment是否存在，且处于可交互状态
	 * 避免：java.lang.IllegalStateException: Fragment XXX not attached to Activity
	 *
	 * @return
	 */
	protected boolean isUIStateLost() {
		return getActivity() == null || isRemoving() || isDetached();
	}

	/**
	 * 返回当前展示的fragment 用于页面承载ViewPager
	 * @param list
	 * @param position
	 * @return
	 */
	public BaseFragment getCurPagerFragment(List<Fragment> list, int position) {
		if (list != null && !list.isEmpty()) {
			Fragment fragment = list.get(position);
			if (fragment != null && fragment instanceof BaseFragment) {
				return (BaseFragment) fragment;
			}
		}
		return null;
	}

}
