package com.framework.app.component.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.app.component.R;

public class CustomeProgressDialog extends Dialog {
	private Context mContent;
	private TextView tipTextView;
	private ImageView spaceshipImage;
	//private Animation hyperspaceJumpAnimation;
	private TextView mTxtMsg;
	private String mMessage;
	private AnimationDrawable anim;
	public CustomeProgressDialog(Context context) {
		super(context, R.style.loading_dialog);
		mContent = context;
	}

	public CustomeProgressDialog(Context context, int theme) {
		super(context, theme);
	}
	
	public void setMessage(String msg){
		if(mTxtMsg!=null){
			mTxtMsg.setText(msg);
		}
		mMessage=msg;
	}

	@Override
	public void show() {
		if (isShowing()) {
			return;
		}
//		if(spaceshipImage!=null){
//			anim.start();
//		}
		super.show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setCanceledOnTouchOutside(false);
		setContentView(R.layout.loading_dialog);
		LinearLayout layout = (LinearLayout) findViewById(R.id.dialog_view);// 加载布局
		spaceshipImage = (ImageView) findViewById(R.id.img);
		mTxtMsg = (TextView) findViewById(R.id.tipTextView);
		mTxtMsg.setText(mMessage);
		anim = (AnimationDrawable) spaceshipImage.getDrawable();
		anim.start();
		setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局

	}
	
	@Override
	public void dismiss() {
		anim.stop();
		super.dismiss();
	}


}