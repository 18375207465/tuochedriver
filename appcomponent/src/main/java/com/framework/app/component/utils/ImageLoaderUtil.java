package com.framework.app.component.utils;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import java.io.File;

public class ImageLoaderUtil {
    private static final int IMAGE_CORNER_RADIUS_PIXELS = 150;
    private static final int MAX_IMAGE_DISK_CACHE_SIZE = 52428800;
    private static final int MAX_IMAGE_DISK_FILE_COUNT = 200;
    private static final int MAX_IMAGE_HEIGHT = 800;
    private static final int MAX_IMAGE_MEMORY_CACHE_SIZE = 2097152;
    private static final int MAX_IMAGE_WIDTH = 480;

    public static void initImageLoader(Context applicationContext) {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        ImageLoader.getInstance().init(new Builder(applicationContext).memoryCache(new LruMemoryCache(maxMemory == 0 ? 2097152 : maxMemory / 8)).memoryCacheExtraOptions(MAX_IMAGE_WIDTH, 800).threadPriority(3).denyCacheImageMultipleSizesInMemory().diskCacheSize(MAX_IMAGE_DISK_CACHE_SIZE).diskCacheFileCount(200).diskCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build());
    }

    public static void clearCache() {
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
    }

    public static DisplayImageOptions getDisplayImageOptions(int imageResId) {
        return getDisplayImageOptions(imageResId, false, true);
    }

    public static DisplayImageOptions getRoundDisplayImageOptions(int imageResId) {
        return getDisplayImageOptions(imageResId, true, true);
    }

    public static DisplayImageOptions getNotShowDisplayImageOptions() {
        return getDisplayImageOptions(0, false, true);
    }

    private static DisplayImageOptions getDisplayImageOptions(int imageResId, boolean cornerRadiusPixels, boolean resetViewBeforeLoading) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        if (imageResId != 0) {
            builder.showImageOnLoading(imageResId);
            builder.showImageForEmptyUri(imageResId);
            builder.showImageForEmptyUri(imageResId);
        }
        builder.cacheInMemory(true);
        builder.cacheOnDisk(true);
        builder.bitmapConfig(Config.RGB_565);
        builder.imageScaleType(ImageScaleType.EXACTLY);
        if (cornerRadiusPixels) {
            builder.displayer(new RoundedBitmapDisplayer(IMAGE_CORNER_RADIUS_PIXELS));
        }
        builder.resetViewBeforeLoading(resetViewBeforeLoading);
        return builder.build();
    }

    public static String getUriFromLocalFile(File file) {
        return Uri.decode(Uri.fromFile(file).toString());
    }
}
