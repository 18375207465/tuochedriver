package com.framework.app.component.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

/**
 * 通用工具类
 * 
 * @ClassName: ComUtil.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2015-1-15 下午2:18:14
 */
public class ComUtil {

    /**
     * 获取本地ip地址
     * 
     * @return
     */
    public String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface intf = en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前版本标示号
     * 
     * @param mContext
     * @return
     */
    public static int getCurrentVersionCode(Context mContext) {
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取当前版本号
     * 
     * @param mContext
     * @return
     */
    public static String getCurrentVersionName(Context mContext) {
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 是否挂载了SD卡
     * 
     * @return
     */
    public static boolean isHaveExternalStorage() {
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     * 
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 拨打电话
     * 
     * @param activity
     * @param phone
     */
    public static void callPhone(Activity activity, String phone) {
        Uri uri = Uri.parse("tel:" + phone);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        activity.startActivity(intent);
    }

    /**
     * 发送短信
     * 
     * @param activity
     * @param phone
     * @param msg
     */
    public static void sendMessage(Activity activity, String phone, String msg) {
        Uri uri = Uri.parse("smsto:" + phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", msg);
        activity.startActivity(intent);
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNumber(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][34578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }
    }

    /**
     * 验证字符串是否为全数字
     * 
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

}
