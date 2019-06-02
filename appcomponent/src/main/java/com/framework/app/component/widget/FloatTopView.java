package com.framework.app.component.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * 控件 - 仿大众点评购买条，滚动时浮动到最上面
 *
 * @author Xun.Zhang
 * @ClassName: FloatTopView.java
 * @date 2015-3-30 上午10:41:07
 */
public class FloatTopView extends LinearLayout {

    private View stayView;
    private StayViewListener stayViewListener;
    private ScrollView scrollView;

    private boolean isUp = true;
    private int mAddHeight;

    public void setStayView(View stayview, ScrollView scrollview, StayViewListener stayViewListener) {
        this.stayView = stayview;
        this.scrollView = scrollview;
        this.stayViewListener = stayViewListener;
    }

    public FloatTopView(Context context) {
        super(context);
    }

    public FloatTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void computeScroll() {
        if (null != stayView && null != scrollView && null != stayViewListener) {
            int y = scrollView.getScrollY() + mAddHeight;
            if (isUp) {
                int top = stayView.getTop();
                if (y >= top) {
                    stayViewListener.onStayViewShow();
                    isUp = false;
                }
            }
            if (!isUp) {
                int bottom = stayView.getBottom();
                if (y <= bottom - stayView.getHeight()) {
                    stayViewListener.onStayViewGone();
                    isUp = true;
                }
            }
        }
    }

    public interface StayViewListener {
        public void onStayViewShow();

        public void onStayViewGone();
    }


    public void setmAddHeight(int height) {
        mAddHeight = height;
    }
}