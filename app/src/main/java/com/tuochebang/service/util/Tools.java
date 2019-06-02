package com.tuochebang.service.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.framework.app.component.utils.DateUtil;
import com.framework.app.component.utils.ToastUtil;
import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.cache.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class Tools {
    public static final String BAIDU_MAP_PACKAGENAME = "com.baidu.BaiduMap";
    public static final String FILE_ROOT_URL = "/base/";
    public static final String GAODE_MAP_PACKAGENAME = "com.autonavi.minimap";
    public static final String PICTURE_PATH = "/base//image/";
    public static final String TENCENT_MAP_PACKAGENAME = "com.tencent.map";

    public static String getFriendlyTime(long time) {
        return DateUtil.friendlyTime(String.valueOf(time));
    }

    public static void call(Activity activity, String phone) {
        callPhone(activity, phone);
    }

    public static void showToast(Context context, String msg) {
        ToastUtil.showMessage(context, msg);
    }

    public static void openUrl(Activity activity, String url, boolean inApp) {
        if (!inApp) {
            activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
        }
    }

    public static int getCurrentVersionCode(Context context) {
        int i = 0;
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return i;
        }
    }

    public static String getCurrentVersionName(Context context) {
        String versionName = "";
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return versionName;
        }
    }

    public static boolean isEmail(String email) {
        Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        if (email == null || email.trim().length() == 0) {
            return false;
        }
        return emailer.matcher(email).matches();
    }

    public static boolean isMobileNum(String mobiles) {
        return Pattern.compile("^(1)\\d{10}$").matcher(mobiles).matches();
    }

    public static void callPhone(Activity activity, String phone) {
        activity.startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:" + phone)));
    }

    public static void sendMessage(Activity activity, String phone, String msg) {
        Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + phone));
        intent.putExtra("sms_body", msg);
        activity.startActivity(intent);
    }

    public static String getPngName(String prefix) {
        return prefix + ".png";
    }

    public static String createImagePath(Context context) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            try {
                return FileUtil.createSDFile("/base//image/", getPngName(DateUtil.getStringDateLong())).getPath();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "在存储卡中创建图片失败", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "存储卡已取出，拍照、相册功能暂不可用", Toast.LENGTH_LONG).show();
            return null;
        }
        return null;
    }

    public static void deleteCacheImages() {
        FileUtil.deleteFile(new File(FileUtil.getSDPath() + "/base//image/"));
    }

    @SuppressLint("WrongConstant")
    public static boolean isGpsEnable(Context context) {
        boolean enable = false;
        if (context != null) {
            try {
                enable = ((LocationManager) context.getSystemService(RequestParameters.SUBRESOURCE_LOCATION)).isProviderEnabled(GeocodeSearch.GPS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return enable;
    }

    @SuppressLint("WrongConstant")
    public static void startGpsSettingActivity(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.settings.LOCATION_SOURCE_SETTINGS");
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        List<PackageInfo> packageInfos = context.getPackageManager().getInstalledPackages(0);
        if (packageInfos == null) {
            return false;
        }
        for (int i = 0; i < packageInfos.size(); i++) {
            if (((PackageInfo) packageInfos.get(i)).packageName.contains(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static int getSDKVersionNumber() {
        try {
            return Integer.valueOf(VERSION.SDK).intValue();
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @SuppressLint("WrongConstant")
    private static void installApk(String apkPath, Context context) {
        Intent i = new Intent("android.intent.action.VIEW");
        i.setDataAndType(Uri.parse("file://" + apkPath), "application/vnd.android.package-archive");
        i.addFlags(268435456);
        context.startActivity(i);
    }

    public static void installApkForAsset(Context context, String name) {
        try {
            InputStream stream = context.getAssets().open(name);
            if (stream == null) {
                return;
            }
            File f = new File("/mnt/sdcard/sm/");
            if (!f.exists()) {
                f.mkdir();
            }
            String apkPath = "/mnt/sdcard/sm/test.apk";
            File file = new File(apkPath);
            file.createNewFile();
            FileUtil.writeStreamToFile(stream, file);
            installApk(apkPath, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint({"HardwareIds", "WrongConstant"})
    public static String getImei() {
        if (ActivityCompat.checkSelfPermission(MyApplication.getInstance(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return ((TelephonyManager) MyApplication.getInstance().getSystemService("phone")).getDeviceId();
        }
        return " ";
    }

    @SuppressLint("WrongConstant")
    public static void copy(String content, Context context) {
        ((ClipboardManager) context.getSystemService("clipboard")).setText(content.trim());
    }

    @SuppressLint("WrongConstant")
    public static String paste(Context context) {
        return ((ClipboardManager) context.getSystemService("clipboard")).getText().toString().trim();
    }

    public static void removeDuplicateWithOrder(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Object element : list) {
            if (set.add(element)) {
                newList.add(element);
            }
        }
        list.clear();
        list.addAll(newList);
        System.out.println(" remove duplicate " + list);
    }

    public static int getNumFromIndex(int num, int index) {
        char[] asC = String.valueOf(num).toCharArray();
        for (int i = 0; i < asC.length; i++) {
            System.out.println(asC[i]);
            if (i == index) {
                return Integer.parseInt(asC[i] + "");
            }
        }
        return 0;
    }

    public static String getShowMoneyString(double money) {
        String showStr;
        if (money % 100.0d == 0.0d) {
            showStr = new DecimalFormat("0").format(money);
        } else {
            showStr = new DecimalFormat("0.00").format(money);
        }
        return "¥" + showStr;
    }

    public static String listToString(List<String> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }
}
