/**
 * @file XListViewHeader.java
 * @create Apr 18, 2012 5:22:27 PM
 * @author Maxwin
 * @description XListView's header
 */
package com.framework.app.component.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.framework.app.component.R;
import com.framework.app.component.utils.LoggerUtil;

public class XListViewHeader extends LinearLayout {

	private final int ROTATE_ANIM_DURATION = 180;

	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_REFRESHING = 2;

	private LinearLayout mContainer;
	// private ImageView mArrowImageView;
	// private ProgressBar mProgressBar;
	private ImageView mProgressBar;
	// private TextView mHintTextView;
	private int mState = STATE_NORMAL;

	private Animation mRotateUpAnim;
	private Animation mRotateDownAnim;

	private RefreshCircleView mCircleView;

	private AnimationDrawable animDrawable;

	public XListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public XListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		// 初始情况，设置下拉刷新view高度为0
		LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT, 0);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.xlistview_header, null);
		addView(mContainer, lp);
		setGravity(Gravity.BOTTOM);

		mCircleView = (RefreshCircleView) findViewById(R.id.refresh_circle);
		// mHintTextView = (TextView)
		// findViewById(R.id.xlistview_header_hint_textview);
		mProgressBar = (ImageView) findViewById(R.id.xlistview_header_progressbar);

		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
	}

	/**
	 * 设置显示状态
	 * 
	 * @param state
	 * @param present
	 */
	public void setState(int state, float present) {
		// if (state == mState)
		// return;

		if (state == STATE_REFRESHING) {// 显示进度
			// mArrowImageView.clearAnimation();
			mCircleView.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.VISIBLE);
			animDrawable = (AnimationDrawable) mProgressBar.getDrawable();
			animDrawable.start();
		} else {// 显示箭头图片
			mCircleView.setVisibility(View.VISIBLE);
			animDrawable = (AnimationDrawable) mProgressBar.getDrawable();
			animDrawable.stop();
			mProgressBar.setVisibility(View.GONE);
		}

		switch (state) {
		case STATE_NORMAL:
			LoggerUtil.e("XListViewHeader STATE_READY: ", "" + present);
			mCircleView.setTargetPercent(present);
			if (mState == STATE_READY) {
				// mArrowImageView.startAnimation(mRotateDownAnim);
			}
			if (mState == STATE_REFRESHING) {
				// mArrowImageView.clearAnimation();
			}
			// mHintTextView.setText(R.string.xlistview_header_hint_normal);
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				// mArrowImageView.clearAnimation();
				// mArrowImageView.startAnimation(mRotateUpAnim);
				// mHintTextView.setText(R.string.xlistview_header_hint_ready);
			}
			break;
		case STATE_REFRESHING:
			// mHintTextView.setText(R.string.xlistview_header_hint_refreshing);
			break;
		default:
		}

		mState = state;
	}

	public void setState(int state) {
		if (state == mState)
			return;

		if (state == STATE_REFRESHING) {// 显示进度
			// mArrowImageView.clearAnimation();
			mCircleView.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.VISIBLE);
			animDrawable = (AnimationDrawable) mProgressBar.getDrawable();
			animDrawable.start();
		} else {// 显示箭头图片
			mCircleView.setVisibility(View.VISIBLE);
			animDrawable = (AnimationDrawable) mProgressBar.getDrawable();
			animDrawable.stop();
			mProgressBar.setVisibility(View.GONE);

		}

		switch (state) {
		case STATE_NORMAL:
			if (mState == STATE_READY) {
				// mArrowImageView.startAnimation(mRotateDownAnim);
			}
			if (mState == STATE_REFRESHING) {
				// mArrowImageView.clearAnimation();
			}
			// mHintTextView.setText(R.string.xlistview_header_hint_normal);
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				// mArrowImageView.clearAnimation();
				// mArrowImageView.startAnimation(mRotateUpAnim);
				// mHintTextView.setText(R.string.xlistview_header_hint_ready);
			}
			break;
		case STATE_REFRESHING:
			// mHintTextView.setText(R.string.xlistview_header_hint_refreshing);
			break;
		default:
		}

		mState = state;
	}

	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LayoutParams lp = (LayoutParams) mContainer
				.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	public int getVisiableHeight() {
		return mContainer.getHeight();
	}
}
