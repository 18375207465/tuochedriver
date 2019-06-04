package com.tuochebang.service.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class StorageUtils {

    /**
     * 防止第三方rom的影响，加入重试和try-catch防止异常
     *
     * 修复fabric上StorageUtil的NaAllipcation初始化异常
     * 2017.4.6
     */
    public static boolean isSDCardExist() {
        int retryTime = 2;
        while (retryTime > 0) {
            try {
                return Environment.getExternalStorageState() != null
                        && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
            } catch (Exception ignore) {
                retryTime--;
            }
        }
        return false;
    }

    public static String getCachePath(Context context) {
        File file = getCacheFile(context);
        return file != null ? file.getPath() : null;
    }

    public static File getCacheFile(Context context) {
        File file;
        try {
            if (isSDCardExist()) {
                file = getExternalCache(context);
            } else {
                file = getInnerCache(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
            file = null;
        }
        return file;
    }

    public static File getExternalCache(Context context) {
        File file;
        try {
            file = context.getApplicationContext().getExternalCacheDir();
        } catch (Exception e) {
            file = null;
        }
        return file;
    }

    public static File getInnerCache(Context context) {
        File file;
        try {
            file = context.getCacheDir();
        } catch (Exception e) {
            file = null;
        }
        return file;
    }

    public static long getAvailableExternalStorageSize() {
        long size = 0;
        if (android.os.Build.VERSION.SDK_INT >= 18) {
            try {
                if (Environment.getExternalStorageDirectory() != null) {
                    StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
                    size = stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
                }
            } catch (Exception e) {
                try {
                    StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
                    size = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
                } catch (Exception ignored) {
                }
            }
        } else if (Environment.getExternalStorageDirectory() != null) {
            try {
                StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
                size = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
            } catch (Exception ignored) {
            }
        }
        return size;
    }

    public static String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader =
                    new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null) {
                Result += line;
            }
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
