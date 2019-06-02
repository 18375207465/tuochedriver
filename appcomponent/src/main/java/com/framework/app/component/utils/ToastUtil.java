package com.framework.app.component.utils;

import android.content.Context;
import android.widget.Toast;

import com.framework.app.component.dialog.CommonNoticeDialog;
import com.framework.app.component.dialog.CommonNoticeDialog.DIALOG_TYPE;
import com.framework.app.component.dialog.CommonNoticeDialog.DialogButtonInterface;

/**
 * Toast显示工具类
 * 
 * @ClassName: ToastUtils.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-12-8 上午10:33:54
 */
public class ToastUtil {

    /**
     * Show Toast in short time by system
     * 
     * @param context
     * @param strMsg
     */
    public static void showMessage(final Context context, final String strMsg) {
        Toast.makeText(context, strMsg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Show Toast in short time by system
     * 
     * @param context
     * @param resId
     */
    public static void showMessage(final Context context, final int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * Show Toast in long time by system
     * 
     * @param context
     * @param resId
     */
    public static void showMessageLong(final Context context, final int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

    /**
     * Show Toast in long time by system
     * 
     * @param context
     * @param strMsg
     */
    public static void showMessageLong(final Context context, final String strMsg) {
        if (context != null) {
            Toast.makeText(context, strMsg, Toast.LENGTH_LONG).show();
        }
    }
    
    
    /**
	 * 显示成功或确认提示框
	 * 
	 * @param msg
	 */
	public static void showSureMsg(Context context,String title, String msg) {
		new CommonNoticeDialog(context, DIALOG_TYPE.SURE, title, msg).show();
	}

	/**
	 * 显示警告信息
	 * 
	 * @param msg
	 */
	public static void showWarnMsg(Context context,String msg) {
		new CommonNoticeDialog(context, DIALOG_TYPE.WARN, "失败", msg).show();
	}
	
	/**
	 * 显示警告信息
	 * 
	 * @param msg
	 */
	public static void showWarnMsg(Context context,String title,String msg) {
		new CommonNoticeDialog(context, DIALOG_TYPE.WARN, title, msg).show();
	}
	
	/**
	 * 显示提示信息
	 * 
	 * @param msg
	 */
	public static void showNoticeMsg(Context context,String msg) {
		new CommonNoticeDialog(context, DIALOG_TYPE.WARN, "提示", msg).show();
	}

	/**
	 * 显示带取消和确定按钮的提示框
	 * 
	 */
	public static void showChoiceDialog(Context context,String title, String content,
			DialogButtonInterface dialogButtonInterface) {
		new CommonNoticeDialog(context, title, content, dialogButtonInterface)
				.show();
	}



}
