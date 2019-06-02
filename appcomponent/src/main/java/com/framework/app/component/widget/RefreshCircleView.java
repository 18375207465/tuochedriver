package com.framework.app.component.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.framework.app.component.R;
import com.framework.app.component.utils.LoggerUtil;

/**
 * 下拉刷新的圆环
 * 
 * @author chongchong
 * @version 4.0.0
 * @date 2015-12-7
 */
public class RefreshCircleView extends View {

	private Paint mArcPaint;
	private float mRadius;
	private int mStartSweepValue;
	private int mCurrentAngle;
	private int mCurrentPercent;
	private int mCircleY;
	private int mCircleX;
	private RectF mArcRectF;
	private float mTargetPercent;

	public RefreshCircleView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.PercentageRing);
		// 中间圆的半径 默认为60
		mRadius = typedArray.getInt(R.styleable.PercentageRing_radius, 30);
		// 最后一定要调用这个 释放掉TypedArray
		typedArray.recycle();
		initView();
	}

	public RefreshCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 自定义属性 values/attr
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.PercentageRing);
		// 中间圆的半径 默认为60
		mRadius = typedArray.getInt(R.styleable.PercentageRing_radius, 30);
		// 最后一定要调用这个 释放掉TypedArray
		typedArray.recycle();
		initView();
	}

	public RefreshCircleView(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		mStartSweepValue = -90;
		mCurrentAngle = 0;
		mCurrentPercent = 0;
		mArcPaint = new Paint();
		mArcPaint.setAntiAlias(true);
		mArcPaint.setColor(0xff660000);
		mArcPaint.setStyle(Paint.Style.STROKE);
		mArcPaint.setStrokeWidth((float) (0.075 * mRadius));
	}

	// 当wrap_content的时候，view的大小根据半径大小改变，但最大不会超过屏幕
	private int measure(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = (int) (1.075 * mRadius * 2);
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;

	}

	// 主要是测量wrap_content时候的宽和高，因为宽高一样，只需要测量一次宽即可，高等于宽
	@SuppressLint("DrawAllocation")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measure(widthMeasureSpec),
				measure(widthMeasureSpec));
		mCircleX = getMeasuredWidth() / 2;
		mCircleY = getMeasuredHeight() / 2;
		// 如果半径大于圆心横坐标，需要手动缩小半径的值，否则就画到外面去了
		if (mRadius > mCircleX) {
			// 设置半径大小为圆心横坐标到原点的距离
			mRadius = mCircleX;
			mRadius = (int) (mCircleX - 0.075 * mRadius);
			// 重新设置外圆环宽度
			mArcPaint.setStrokeWidth((float) (0.075 * mRadius));
		}
		// 画中心园的外接矩形，用来画圆环用
		mArcRectF = new RectF(mCircleX - mRadius, mCircleY - mRadius, mCircleX
				+ mRadius, mCircleY + mRadius);
	}

	// 开始画中间圆、文字和外圆环
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mCurrentAngle = (int) (mTargetPercent * 360);
		// 画圆环
		canvas.drawArc(mArcRectF, mStartSweepValue, mCurrentAngle, false,
				mArcPaint);
		// // 判断当前百分比是否小于设置目标的百分比
		// if (mCurrentPercent < mTargetPercent) {
		// // 当前百分比+1
		// mCurrentPercent += 1;
		// // 当前角度+360
		// mCurrentAngle += 3.6;
		// // 每10ms重画一次
		// postInvalidateDelayed(10);
		// }

	}

	// 设置目标的百分比
	public void setTargetPercent(float percent) {
		LoggerUtil.e("setTargetPercent: ", "" + percent);
		this.mTargetPercent = percent;
		postInvalidateDelayed(10);
	}

}
