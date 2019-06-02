package com.framework.app.component.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.framework.app.component.R;

/**
 * 通用自定义样式AlertDialog
 * 
 * @ClassName: CommonAlertDialog.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-11 上午9:38:40
 */
public class CommonAlertDialog {

	private AlertDialog mAlertDialog;
	private TextView mTxtViewMessage;
	private Button mBtnPositive;
	private Button mBtnNegative;

	public CommonAlertDialog(Context context) {
		mAlertDialog = new AlertDialog.Builder(context).create();
		mAlertDialog.show();
		Window window = mAlertDialog.getWindow();
		window.setContentView(R.layout.dialog_common_alert);
		mTxtViewMessage = (TextView) window.findViewById(R.id.txt_dialog_message);
		mBtnPositive = (Button) window.findViewById(R.id.btn_dialog_positive);
		mBtnNegative = (Button) window.findViewById(R.id.btn_dialog_negative);
	}

	public void setMessage(String strMessage) {
		mTxtViewMessage.setText(strMessage);
	}

	/**
	 * 设置按钮（确定）
	 * 
	 * @param text
	 * @param listener
	 */
	public void setPositiveButton(int resId, final View.OnClickListener onClickListener) {
		mBtnPositive.setText(resId);
		mBtnPositive.setOnClickListener(onClickListener);
	}

	/**
	 * 设置按钮（否定）
	 * 
	 * @param text
	 * @param listener
	 */
	public void setNegativeButton(int resId, final View.OnClickListener onClickListener) {
		mBtnNegative.setText(resId);
		mBtnNegative.setOnClickListener(onClickListener);
	}

	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		if (null != mAlertDialog) {
			mAlertDialog.dismiss();
		}
	}

	/**
	 * 显示对话框
	 */
	public void show() {
		if (null != mAlertDialog && !mAlertDialog.isShowing()) {
			mAlertDialog.show();
		}
	}

}
