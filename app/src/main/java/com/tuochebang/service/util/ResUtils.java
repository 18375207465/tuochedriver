package com.tuochebang.service.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;

import com.bumptech.glide.load.Key;
import com.tuochebang.service.constant.AppConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ResUtils {
    private ResUtils() {
    }

    public static int getStatusBarHeight(@NonNull Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", AppConfig.DEVICE_TYPE);
        return resourceId > 0 ? context.getResources().getDimensionPixelSize(resourceId) : 0;
    }

    @ColorInt
    public static int getThemeAttrColor(@NonNull Context context, @AttrRes int attr) {
        TypedArray a = context.obtainStyledAttributes(null, new int[]{attr});
        try {
            int color = a.getColor(0, 0);
            return color;
        } finally {
            a.recycle();
        }
    }

    public static Drawable getThemeAttrDrawable(@NonNull Context context, @AttrRes int attr) {
        TypedArray a = context.obtainStyledAttributes(null, new int[]{attr});
        try {
            Drawable drawable = a.getDrawable(0);
            return drawable;
        } finally {
            a.recycle();
        }
    }

    public static String getRawString(@NonNull Context context, @RawRes int rawId) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(rawId), Key.STRING_CHARSET_NAME));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                sb.append(line).append("\n");
            } else {
                sb.deleteCharAt(sb.length() - 1);
                reader.close();
                return sb.toString();
            }
        }
    }
}
