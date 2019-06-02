package com.framework.app.component.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.framework.app.component.R;
import com.framework.app.component.widget.circleprogress.CircularProgressView;

/**
 * HTTP请求页面基本数据时显示的DataLoadingView
 *
 * @author Xun.Zhang
 * @ClassName: DataLoadingView.java
 * @date 2014-12-8 上午11:47:34
 */
public class DataLoadingView extends RelativeLayout {

    /**
     * 数据加载中的布局
     */
    private ViewGroup mLayoutDataLoading;

    /**
     * 数据加载失败的布局
     */
    private ViewGroup mLayoutDataLoadFailed;

    /**
     * 数据加载失败的错误信息
     */
    private TextView mTxtViewErrorInfo;

    /**
     * 重新加载按钮
     */
    private TextView mTxtViewReload;

    /**
     * 加载中的动画
     */
    private AnimationDrawable mAnimationDrawable;


    CircularProgressView mImageAnim;

    public DataLoadingView(Context context) {
        this(context, null);
    }

    public DataLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DataLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }


    private void initViews(Context context) {
        View rootLayout = LayoutInflater.from(context).inflate(R.layout.widget_data_loading_view, this);
        mLayoutDataLoading = (ViewGroup) rootLayout.findViewById(R.id.ll_network_loading);
        mLayoutDataLoadFailed = (ViewGroup) rootLayout.findViewById(R.id.ll_loading_failed);
        mTxtViewErrorInfo = (TextView) mLayoutDataLoadFailed.findViewById(R.id.txt_view_error);
        mTxtViewReload = (TextView) mLayoutDataLoadFailed.findViewById(R.id.txt_view_reload);
        mTxtViewReload.setVisibility(View.GONE);
        mImageAnim = (CircularProgressView) rootLayout.findViewById(R.id.iv_image_anim);
        mImageAnim.setColor(context.getResources().getColor(R.color.common_puple));
        mAnimationDrawable = (AnimationDrawable) mImageAnim.getBackground();
        // 禁用Touch事件
        rootLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });
    }

    /**
     * 数据加载中
     */
    public void showDataLoading() {
        this.setVisibility(View.VISIBLE);
        mLayoutDataLoading.setVisibility(View.VISIBLE);
        //mAnimationDrawable.start();
        mImageAnim.startAnimation();
        mLayoutDataLoadFailed.setVisibility(View.GONE);
    }

    /**
     * 数据加载成功
     */
    public void showDataLoadSuccess() {
        this.setVisibility(View.GONE);
        //mAnimationDrawable.stop();
        mImageAnim.stopAnimation();
    }

    /**
     * 显示数据加载失败的原因
     */
    public void showDataLoadFailed(String strReason) {
        this.setVisibility(View.VISIBLE);
        mLayoutDataLoading.setVisibility(View.GONE);
        mLayoutDataLoadFailed.setVisibility(View.VISIBLE);
        mTxtViewErrorInfo.setText(strReason);
        // mAnimationDrawable.stop();
        mImageAnim.stopAnimation();
    }

    /**
     * 设置重新加载按钮点击事件
     *
     * @param onClickListener
     */
    public void setOnReloadClickListener(OnClickListener onClickListener) {
        mTxtViewReload.setVisibility(View.VISIBLE);
        mTxtViewReload.setOnClickListener(onClickListener);
    }

}
