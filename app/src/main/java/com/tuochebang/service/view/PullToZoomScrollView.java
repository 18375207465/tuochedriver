package com.tuochebang.service.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import com.nineoldandroids.view.ViewHelper;

public class PullToZoomScrollView extends ScrollView {
    private static final String TAG = PullToZoomScrollView.class.getSimpleName();
    private int initZoomHeight;
    private int initZoomWidth;
    private float lastY;
    private ViewGroup mZoomView;

    public PullToZoomScrollView(Context context) {
        super(context);
    }

    public PullToZoomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setZoomView(ViewGroup v) {
        this.mZoomView = v;
        this.initZoomHeight = this.mZoomView.getLayoutParams().height;
        this.initZoomWidth = this.mZoomView.getLayoutParams().width;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 0) {
        }
        if (ev.getAction() == 2) {
            if (!isScollToTop() || this.lastY >= ev.getY()) {
                this.lastY = ev.getY();
            } else {
                this.lastY = ev.getY();
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.mZoomView.getLayoutParams();
                params.height += 8;
                float scale = ((float) params.height) / ((float) this.initZoomHeight);
                if (((double) scale) > 1.5d) {
                    return true;
                }
                this.mZoomView.setLayoutParams(params);
                ViewHelper.setScaleX(this.mZoomView, scale);
                ViewHelper.setScaleY(this.mZoomView, scale);
                return true;
            }
        }
        if (ev.getAction() == 1) {
            reSetViewToInitState();
        }
        if (getScrollY() == 0) {
        }
        return super.onTouchEvent(ev);
    }

    private void reSetViewToInitState() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.mZoomView.getLayoutParams();
        params.height = this.initZoomHeight;
        params.width = this.initZoomWidth;
        this.mZoomView.setLayoutParams(params);
        ViewHelper.setScaleX(this.mZoomView, 1.0f);
        ViewHelper.setScaleY(this.mZoomView, 1.0f);
    }

    private boolean isScollToTop() {
        return getScrollY() == 0;
    }
}
