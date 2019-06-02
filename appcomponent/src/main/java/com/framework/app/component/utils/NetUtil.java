package com.framework.app.component.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

public class NetUtil extends BroadcastReceiver {
	// 侦听网络状态的变化
	private static NetOk netok;
	public static boolean onNetOkCalled = false;// 防止多次调用

	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
		boolean wifi = false;
		boolean mobile = false;
		for (NetworkInfo info : infos) {
			if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI
					&& !info.isConnected()) {
				wifi = true;
			}
			if (info != null
					&& info.getType() == ConnectivityManager.TYPE_MOBILE
					&& !info.isConnected()) {
				mobile = true;
			}
		}
		if (wifi && mobile) {
			return;
		}

		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			// 判断当前网络是否已经连接
			if (info.getState() == NetworkInfo.State.CONNECTED) {
				// Toast.makeText( context, "ok", Toast.LENGTH_SHORT ).show();
				if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
					if (netok != null && !onNetOkCalled) {
						onNetOkCalled = true;
						netok.onNetOk();
					}
				}
			}
		}
	}

	/* 判断当前网络连接是否是CMWap */
	public static boolean isCmwap(Context context) {

		if (context == null) {
			return false;
		}

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (cm == null) {

			return false;
		}

		NetworkInfo info = cm.getActiveNetworkInfo();

		if (info == null) {

			return false;

		}

		String extraInfo = info.getExtraInfo();

		// 工具类，判断是否为空及null

		if (TextUtils.isEmpty(extraInfo) || (extraInfo.length() < 3)) {

			return false;

		}

		if (extraInfo.toLowerCase().indexOf("wap") > 0) {

			return true;

		}

		return false;

	}

	public static boolean checkNet(Context context) {// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}

		} catch (Exception e) {
			Log.v("------------net------", e.toString());
		}
		Toast.makeText(context,
				"当前网络连接失败",
				Toast.LENGTH_SHORT).show();
		return false;
	}

	public static boolean isNetEnable(Context context) {// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}

		} catch (Exception e) {
			Log.v("------------net------", e.toString());
		}
		return false;
	}

	// 从一个URL获取图片
	public static BitmapDrawable getImageFromURL(URL url) {
		BitmapDrawable bd = null;
		try {
			// 创建连接
			HttpURLConnection hc = (HttpURLConnection) url.openConnection();
			// 获取数据
			bd = new BitmapDrawable(hc.getInputStream());
			// 关闭连接
			hc.disconnect();
		} catch (Exception e) {
		}

		return bd;
	}

	// 获取外网IP
	public static String GetNetIp() {
		URL infoUrl = null;
		InputStream inStream = null;
		try {

			infoUrl = new URL("http://iframe.ip138.com/ic.asp");
			// infoUrl = new URL("http://www.baidu.com");
			URLConnection connection = infoUrl.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inStream = httpConnection.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inStream, "utf-8"));
				StringBuilder strber = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null)
					strber.append(line + "\n");
				inStream.close();

				// 从反馈的结果中提取出IP地址
				int start = strber.indexOf("[");
				int end = strber.indexOf("]", start + 1);
				line = strber.substring(start + 1, end);
				return line;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return getLocalIpAddress();
	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			// Log.e(TAG, ex.toString());
		}
		return "";
	}

	public static String getNetAccessName(Context mContext) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return info.getTypeName();
					} else {
						return null;
					}
				}
			}

		} catch (Exception e) {
			Log.v("------------net------", e.toString());
		}
		return null;

	}

	public static void setNetListener(NetOk netOk) {
		netok = netOk;
	}

	public interface NetOk {
		public void onNetOk();
	}

	/**
	 * make true current connect service is wifi
	 * 
	 * @param mContext
	 * @return
	 */
	public static boolean isWifi(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}
	
}
