package com.framework.app.component.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.framework.app.component.R;

/**
 * 控件 - 列表为空视图
 * 
 * @ClassName: XListEmptyView.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2015-2-12 下午5:26:52
 */
public class XListEmptyView extends RelativeLayout {

	private TextView mTvEmtpyInfo;
	private ImageView mIvEmpty;

	public XListEmptyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	public XListEmptyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public XListEmptyView(Context context) {
		super(context);
		initViews(context);
	}

	private void initViews(Context context) {
		View rootLayout = LayoutInflater.from(context).inflate(R.layout.component_xlist_empty_view, this);

		mTvEmtpyInfo = (TextView) rootLayout.findViewById(R.id.tv_empty_info);
		mIvEmpty=(ImageView) rootLayout.findViewById(R.id.iv_empty);
	}

	/**
	 * 设置标题和图标
	 * 
	 * @param emptyInfo
	 */
	public void setEmptyInfo(int resId,String emptyInfo) {
		mTvEmtpyInfo.setText(emptyInfo);
		mIvEmpty.setImageResource(resId);
	}
	
	/**
	 * 设置标题
	 * 
	 * @param emptyInfo
	 */
	public void setEmptyInfo(String emptyInfo) {
		mTvEmtpyInfo.setText(emptyInfo);
	}

}
