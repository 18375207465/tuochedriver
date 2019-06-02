package com.framework.app.component.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Adapter - 通用PagerAdapter
 * 
 * @ClassName: CommonPagerAdapter
 * 
 * @author Kevin.Zhang
 * 
 * @date 2015-2-9 上午11:25:57
 */
public class CommonPagerAdapter extends PagerAdapter {

	private List<View> mViewList;

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = mViewList.get(position);
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		View view = mViewList.get(position);
		container.removeView(view);
	}

	@Override
	public int getCount() {
		return mViewList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	public void setViewList(List<View> viewList) {
		mViewList = viewList;
	}
}
