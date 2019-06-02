/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package com.framework.app.component.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.app.component.R;
import com.framework.app.component.widget.circleprogress.CircularProgressView;

public class XListViewFooter extends LinearLayout {

    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_LOADING = 2;

    private Context mContext;

    private View mContentView;
    // private View mProgressBar;
    private ImageView mArrowImageView;
    private CircularProgressView mProgressBar;
    private TextView mHintView;

    public XListViewFooter(Context context) {
        super(context);
        initView(context);
    }

    public XListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setState(int state) {
        if (state == STATE_READY) {
            // mArrowImageView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
            mHintView.setText(R.string.xlistview_footer_hint_ready);
        } else if (state == STATE_LOADING) {
            // mArrowImageView.setVisibility(View.GONE);
            mHintView.setText(R.string.xlistview_footer_hint_loading);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            //mArrowImageView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
            mHintView.setText(R.string.xlistview_footer_hint_normal);
        }
    }

    public void setBottomMargin(int height) {
        if (height < 0)
            return;
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.bottomMargin = height;
        mContentView.setLayoutParams(lp);
    }

    public int getBottomMargin() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        return lp.bottomMargin;
    }

    /**
     * normal status
     */
    public void normal() {
        mHintView.setText(R.string.xlistview_footer_hint_normal);
        mProgressBar.setVisibility(View.GONE);
        mProgressBar.stopAnimation();
    }

    /**
     * loading status
     */
    public void loading() {
        mHintView.setText(R.string.xlistview_footer_hint_loading);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.startAnimation();
    }

    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = 0;
        mContentView.setLayoutParams(lp);
    }

    /**
     * show footer
     */
    public void show() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mContentView.setLayoutParams(lp);
    }

    private void initView(Context context) {
        mContext = context;
        LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.xlistview_footer, null);
        addView(moreView);
        moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mContentView = moreView.findViewById(R.id.xlistview_footer_content);
        mArrowImageView = (ImageView) moreView.findViewById(R.id.xlistview_footer_arrow);
        mProgressBar = (CircularProgressView) moreView.findViewById(R.id.xlistview_footer_progressbar);
        mHintView = (TextView) moreView.findViewById(R.id.xlistview_footer_hint_textview);
    }

}
