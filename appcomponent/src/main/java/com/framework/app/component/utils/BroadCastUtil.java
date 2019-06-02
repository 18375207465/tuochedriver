package com.framework.app.component.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

/**
 * 本地广播发送工具类
 * 
 * @ClassName: BroadCastUtil.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-12-8 下午12:52:02
 */
public class BroadCastUtil {

	/**
	 * 发送本地广播(不带Bundle数据)
	 * 
	 * @param context
	 * @param action
	 */
	public static void sendBroadCast(Context context, String action) {
		Intent intent = new Intent();
		intent.setAction(action);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}

	/**
	 * 发送本地广播(带Bundle数据)
	 * 
	 * @param context
	 * @param action
	 * @param extras
	 */
	public static void sendBroadCast(Context context, String action, Bundle extras) {
		Intent intent = new Intent();
		intent.setAction(action);
		intent.putExtras(extras);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}

}
