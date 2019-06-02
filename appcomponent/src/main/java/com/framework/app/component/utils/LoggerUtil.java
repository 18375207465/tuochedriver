package com.framework.app.component.utils;

import android.util.Log;

import com.framework.app.component.utils.logger.Logger;

/**
 * Logger工具类
 * 
 * @ClassName: Logger.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-12-8 上午10:49:30
 */
public class LoggerUtil {

	/**
	 * 控制需要打的log
	 * 
	 * 5 全部显示
	 * 
	 * 0不显示
	 */
	private static final int LOG_LEVEL = 5;

	public static void d(String tag, String msg) {
		if (LOG_LEVEL > 1) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (LOG_LEVEL > 2) {
			Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (LOG_LEVEL > 3) {
			Log.w(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (LOG_LEVEL > 4) {
			Log.e(tag, msg);
		}
	}

	public static void LogJson(String message) {
		Logger.init();
		Logger.json(message);
	}

}
