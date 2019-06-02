package com.framework.app.component.widget;

import java.util.ArrayList;
import java.util.List;

import com.framework.app.component.R;
import com.framework.app.component.utils.DensityUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class StepBar extends View {
	private Paint mPaint;// 前景色
	private Paint mBackgroundPaint;// 背景色
	private int mCount = 4;// 总的阶段数
	private int mStep = 1;// 当前阶段
	private float mLeftAndRightMargin = 100;// 左右间距
	private float mTopAndBottomMargin = 50;// 上下间距
	private float mScreenWidh = 720;// 屏幕宽
	private float mRadius = 15;// 圆点半径
	private List<String> mStepTexts = new ArrayList<String>();
	private Paint mTextPaint;// 前景色
	private Paint mTextBackgroundPaint;// 背景色
	private Context mContext;

	public StepBar(Context context) {
		super(context);
		mContext = context;
		init();
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setColor(getResources().getColor(R.color.txt_common_red_color));
		mPaint.setStrokeWidth(5);
		mPaint.setAntiAlias(true);
		mBackgroundPaint = new Paint();
		mBackgroundPaint.setColor(getResources().getColor(
				R.color.common_line_color));
		mBackgroundPaint.setStrokeWidth(5);
		mBackgroundPaint.setAntiAlias(true);
		mTextPaint = new Paint();
		mTextPaint.setColor(getResources().getColor(R.color.txt_title_color));
		mTextPaint.setTextSize(20);
		mTextPaint.setAntiAlias(true);
		mTextBackgroundPaint = new Paint();
		mTextBackgroundPaint.setColor(getResources().getColor(
				R.color.txt_info_color));
		mTextBackgroundPaint.setTextSize(20);
		mTextBackgroundPaint.setAntiAlias(true);
		mStepTexts.add("提交报价");
		mStepTexts.add("等待报价");
		mStepTexts.add("预约成功");
		mStepTexts.add("完成");
		setScreenWidh(DensityUtil.getScreenWidth((Activity) mContext));
	}

	public StepBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public StepBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		init();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		// 背景的线
		canvas.drawLine(mLeftAndRightMargin, mTopAndBottomMargin, mScreenWidh
				- mLeftAndRightMargin, mTopAndBottomMargin, mBackgroundPaint);
		// 背景的点和文字
		for (int i = mStep; i < mCount; i++) {
			canvas.drawCircle(
					mLeftAndRightMargin
							+ ((mScreenWidh - mLeftAndRightMargin - mLeftAndRightMargin) / (mCount - 1))
							* i, mTopAndBottomMargin, mRadius, mBackgroundPaint);
			if (mStepTexts != null && mStepTexts.size() == mCount) {
				canvas.drawText(
						mStepTexts.get(i),
						mLeftAndRightMargin
								+ ((mScreenWidh - mLeftAndRightMargin - mLeftAndRightMargin) / (mCount - 1))
								* i
								- mTextBackgroundPaint.measureText(mStepTexts
										.get(i)) / 2, mTopAndBottomMargin * 2,
						mTextBackgroundPaint);
			}
		}
		// 前景的线
		if (mStep <= 1) {
			canvas.drawLine(
					mLeftAndRightMargin,
					mTopAndBottomMargin,
					mLeftAndRightMargin
							+ (mScreenWidh - mLeftAndRightMargin - mLeftAndRightMargin)
							/ ((mCount - 1) * 2), mTopAndBottomMargin, mPaint);
		} else if (mStep >= mCount) {
			canvas.drawLine(mLeftAndRightMargin, mTopAndBottomMargin,
					mScreenWidh - mLeftAndRightMargin, mTopAndBottomMargin,
					mPaint);
		} else {
			canvas.drawLine(
					mLeftAndRightMargin,
					mTopAndBottomMargin,
					mLeftAndRightMargin
							+ ((mScreenWidh - mLeftAndRightMargin - mLeftAndRightMargin) / ((mCount - 1) * 2))
							* (mStep * 2 - 1), mTopAndBottomMargin, mPaint);
		}
		// 前景的点和文字
		for (int i = 0; i < mStep; i++) {
			canvas.drawCircle(
					mLeftAndRightMargin
							+ ((mScreenWidh - mLeftAndRightMargin - mLeftAndRightMargin) / (mCount - 1))
							* i, mTopAndBottomMargin, mRadius, mPaint);
			if (mStepTexts != null && mStepTexts.size() == mCount) {
				canvas.drawText(
						mStepTexts.get(i),
						mLeftAndRightMargin
								+ ((mScreenWidh - mLeftAndRightMargin - mLeftAndRightMargin) / (mCount - 1))
								* i - mTextPaint.measureText(mStepTexts.get(i))
								/ 2, mTopAndBottomMargin * 2, mTextPaint);
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension((int) mScreenWidh,
				(int) (mTopAndBottomMargin * 2 + mRadius * 2));
	}

	public void setStepTexts(List<String> mStepTexts) {
		this.mStepTexts.clear();
		this.mStepTexts.addAll(mStepTexts);
		invalidate();
	}

	public void setTopAndBottomMargin(float mTopAndBottomMargin) {
		this.mTopAndBottomMargin = mTopAndBottomMargin;
		invalidate();
	}

	public void setLeftAndRightMargin(float mLeftAndRightMargin) {
		this.mLeftAndRightMargin = mLeftAndRightMargin;
		invalidate();
	}

	public void setScreenWidh(float mScreenWidh) {
		this.mScreenWidh = mScreenWidh;
		invalidate();
	}

	public void setCount(int count) {
		mCount = count;
		invalidate();
	}

	public void setStep(int step) {
		mStep = step;
		invalidate();
	}

}
