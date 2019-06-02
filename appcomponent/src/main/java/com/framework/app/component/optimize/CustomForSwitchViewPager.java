package com.framework.app.component.optimize;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.framework.app.component.utils.LoggerUtil;

import java.util.HashMap;

/**
 * Created by jsonchong on 16/1/21.
 */
public class CustomForSwitchViewPager extends ViewPager {
    private boolean isLocked = false;
    private HashMap mHeightMap;
    private int mCurrentIndex = 0;

    public CustomForSwitchViewPager(Context context) {
        super(context);
        init();
    }

    public CustomForSwitchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOffscreenPageLimit(3);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            LoggerUtil.e("viewager onMeasure h: ", h + "");
            LoggerUtil.e("viewager getCurrentItem  ", getCurrentItem() + "");
            if (i == getCurrentItem())
                height = h;
        }

        LoggerUtil.e("viewager onMeasure height: ", height + "");
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
//        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public void refreshChildHeight(int index) {
        requestLayout();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isLocked) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isLocked) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    public void toggleLock() {
        isLocked = !isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean isLocked() {
        return isLocked;
    }
}
