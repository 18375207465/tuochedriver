package com.tuochebang.service.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.framework.app.component.utils.DateUtil;
import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.cache.FileUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class AndroidUtil {
    public static final String BAIDU_MAP_PACKAGENAME = "com.baidu.BaiduMap";
    public static final int DELAY = 1000;
    public static final String FILE_ROOT_URL = "/base/";
    public static final String GAODE_MAP_PACKAGENAME = "com.autonavi.minimap";
    public static final String PICTURE_PATH = "/base//image/";
    public static final String TENCENT_MAP_PACKAGENAME = "com.tencent.map";
    private static long lastClickTime = 0;

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

    @SuppressLint({"MissingPermission", "WrongConstant"})
    public static String getImei() {
        return ((TelephonyManager) MyApplication.getInstance().getSystemService("phone")).getDeviceId();
    }

    public static String getChannelName(Context ctx) {
        if (ctx == null) {
            return null;
        }
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager == null) {
                return null;
            }
            @SuppressLint("WrongConstant") ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), 128);
            if (applicationInfo == null || applicationInfo.metaData == null) {
                return null;
            }
            return applicationInfo.metaData.getString("UMENG_CHANNEL");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean hasFroyo() {
        return VERSION.SDK_INT >= 8;
    }

    public static boolean hasGingerbread() {
        return VERSION.SDK_INT >= 9;
    }

    public static boolean hasHoneycomb() {
        return VERSION.SDK_INT >= 11;
    }

    public static boolean hasHoneycombMR1() {
        return VERSION.SDK_INT >= 12;
    }

    public static boolean hasICS() {
        return VERSION.SDK_INT >= 14;
    }

    public static boolean hasJellyBean() {
        return VERSION.SDK_INT >= 16;
    }

    public static boolean hasJellyBeanMr1() {
        return VERSION.SDK_INT >= 17;
    }

    public static boolean hasJellyBeanMr2() {
        return VERSION.SDK_INT >= 18;
    }

    public static boolean hasKitkat() {
        return VERSION.SDK_INT >= 19;
    }

    public static int getSDKVersionInt() {
        return VERSION.SDK_INT;
    }

    public static String getSDKVersion() {
        return VERSION.SDK;
    }

    public static String getReleaseVersion() {
        return VERSION.RELEASE;
    }

    public static boolean isZte() {
        return getDeviceModel().toLowerCase().indexOf("zte") != -1;
    }

    public static boolean isSamsung() {
        return getManufacturer().toLowerCase().indexOf("samsung") != -1;
    }

    public static boolean isHTC() {
        return getManufacturer().toLowerCase().indexOf("htc") != -1;
    }

    public static boolean isDevice(String... devices) {
        String model = getDeviceModel();
        if (devices == null || model == null) {
            return false;
        }
        for (String device : devices) {
            if (model.indexOf(device) != -1) {
                return true;
            }
        }
        return false;
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }

    public static boolean isHoneycombTablet(Context context) {
        return hasHoneycomb() && isTablet(context);
    }

    public static int dipToPX(Context ctx, float dip) {
        return (int) TypedValue.applyDimension(1, dip, ctx.getResources().getDisplayMetrics());
    }

    public static String getCpuInfo() {
        String cpuInfo = "";
        try {
            if (!new File("/proc/cpuinfo").exists()) {
                return cpuInfo;
            }
            BufferedReader localBufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"), 8192);
            cpuInfo = localBufferedReader.readLine();
            localBufferedReader.close();
            if (cpuInfo != null) {
                return cpuInfo.split(":")[1].trim().split(" ")[0];
            }
            return cpuInfo;
        } catch (IOException e) {
            return cpuInfo;
        } catch (Exception e2) {
            return cpuInfo;
        }
    }

    public static boolean isSupportCameraLedFlash(PackageManager pm) {
        if (pm == null) {
            return false;
        }
        FeatureInfo[] features = pm.getSystemAvailableFeatures();
        if (features == null) {
            return false;
        }
        for (FeatureInfo f : features) {
            if (f != null && "android.hardware.camera.flash".equals(f.name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSupportCameraHardware(Context context) {
        if (context == null || !context.getPackageManager().hasSystemFeature("android.hardware.camera")) {
            return false;
        }
        return true;
    }

    public static boolean isNotFastClick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime <= 1000) {
            return false;
        }
        lastClickTime = currentTime;
        return true;
    }

    public static void recycle(Map<View, int[]> mapViews) {
        synchronized (mapViews) {
            for (View view : mapViews.keySet()) {
                if (view == null) {
                    return;
                }
                int[] recycleIds = (int[]) mapViews.get(view);
                if (view instanceof AbsListView) {
                    recycleAbsList((AbsListView) view, recycleIds);
                } else if (view instanceof ImageView) {
                    recycleImageView(view);
                } else if (view instanceof ViewGroup) {
                    recycleViewGroup((ViewGroup) view, recycleIds);
                }
            }
            System.gc();
        }
    }

    public static void recycleAbsList(AbsListView absView, int[] recycleIds) {
        if (absView != null) {
            synchronized (absView) {
                for (int index = absView.getFirstVisiblePosition(); index <= absView.getLastVisiblePosition(); index++) {
                    ViewGroup views = (ViewGroup) ((ListAdapter) absView.getAdapter()).getView(index, null, absView);
                    for (int findViewById : recycleIds) {
                        recycleImageView(views.findViewById(findViewById));
                    }
                }
            }
        }
    }

    public static void recycleViewGroup(ViewGroup layout, int[] recycleIds) {
        if (layout != null) {
            synchronized (layout) {
                for (int i = 0; i < layout.getChildCount(); i++) {
                    View subView = layout.getChildAt(i);
                    if (subView instanceof ViewGroup) {
                        for (int findViewById : recycleIds) {
                            recycleImageView(subView.findViewById(findViewById));
                        }
                    } else if (subView instanceof ImageView) {
                        recycleImageView((ImageView) subView);
                    }
                }
            }
        }
    }

    public static void recycleImageView(View view) {
        if (view != null && (view instanceof ImageView)) {
            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable instanceof BitmapDrawable) {
                Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
                if (bmp != null && !bmp.isRecycled()) {
                    ((ImageView) view).setImageBitmap(null);
                    bmp.recycle();
                }
            }
        }
    }

    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        @SuppressLint("WrongConstant") List<RunningTaskInfo> list = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1);
        if (list == null || list.size() <= 0 || !className.equals(((RunningTaskInfo) list.get(0)).topActivity.getClassName())) {
            return false;
        }
        return true;
    }

    @SuppressLint("WrongConstant")
    public static boolean checkPermission(Context context, String permission) {
        if (VERSION.SDK_INT >= 23) {
            try {
                if (((Integer) Class.forName("android.content.Context").getMethod("checkSelfPermission", new Class[]{String.class}).invoke(context, new Object[]{permission})).intValue() == 0) {
                    return true;
                }
                return false;
            } catch (Exception e) {
                return false;
            }
        } else if (context.getPackageManager().checkPermission(permission, context.getPackageName()) == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static String getDeviceId(Context context) {
        String device_id = null;
        @SuppressLint("WrongConstant") TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        if (checkPermission(context, "android.permission.READ_PHONE_STATE")) {
            device_id = tm.getDeviceId();
        }
        if (TextUtils.isEmpty(device_id)) {
            return Secure.getString(context.getContentResolver(), "android_id");
        }
        return device_id;
    }

    public static String getDeviceInfo(Context context) {
        FileReader fstream;
        Throwable th;
        try {
            JSONObject json = new JSONObject();
            @SuppressLint("WrongConstant") TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
            String device_id = null;
            if (checkPermission(context, "android.permission.READ_PHONE_STATE")) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    BufferedReader in2 = new BufferedReader(fstream, 1024);
                    try {
                        mac = in2.readLine();
                        if (fstream != null) {
                            try {
                                fstream.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        if (in2 != null) {
                            try {
                                in2.close();
                                in = in2;
                            } catch (IOException e22) {
                                e22.printStackTrace();
                                in = in2;
                            }
                        }
                    } catch (IOException e3) {
                        in = in2;
                        if (fstream != null) {
                            try {
                                fstream.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                        }
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e2222) {
                                e2222.printStackTrace();
                            }
                        }
                        json.put("mac", mac);
                        if (TextUtils.isEmpty(device_id)) {
                            device_id = mac;
                        }
                        if (TextUtils.isEmpty(device_id)) {
                            device_id = Secure.getString(context.getContentResolver(), "android_id");
                        }
                        json.put("device_id", device_id);
                        return json.toString();
                    } catch (Throwable th2) {
                        th = th2;
                        in = in2;
                        if (fstream != null) {
                            try {
                                fstream.close();
                            } catch (IOException e22222) {
                                e22222.printStackTrace();
                            }
                        }
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e222222) {
                                e222222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e4) {
                    if (fstream != null) {
                        fstream.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                    json.put("mac", mac);
                    if (TextUtils.isEmpty(device_id)) {
                        device_id = mac;
                    }
                    if (TextUtils.isEmpty(device_id)) {
                        device_id = Secure.getString(context.getContentResolver(), "android_id");
                    }
                    json.put("device_id", device_id);
                    return json.toString();
                } catch (Throwable th3) {
                    th = th3;
                    if (fstream != null) {
                        fstream.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                    throw th;
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = Secure.getString(context.getContentResolver(), "android_id");
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Throwable e5) {
            e5.printStackTrace();
            return null;
        }
    }

    public static String getBrand() {
        return Build.BRAND;
    }
}
