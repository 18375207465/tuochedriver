package com.tuochebang.service.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import com.framework.app.component.utils.ToastUtil;
import com.tuochebang.service.R;
import com.tuochebang.service.app.MIME_TYPE;
import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.app.SchemeType;
import com.tuochebang.service.cache.FileUtil;
import com.yalantis.ucrop.util.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NAImageUtils {

    private static final long MAX_UPLOAD_FILE_SIZE = 10 * 1024 * 1024;

    private static final int OPTIONS_SCALE_UP = 0x1;

    private static final int OPTIONS_NONE = 0x0;

    private static final int MAX_PIC_WIDTH = 1600;

    private static final int MAX_PIC_HEIGHT = 1600;

    private static String CompressPath;

    /**
     * 返回图片旋转角度，PNG默认为0
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        if (path == null || path.endsWith(".png") || path.endsWith(".PNG")) {
            return degree;
        }
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (Exception e) {
            // P.e(e, "Read pic degree error");
        }
        return degree;
    }

    public static void recycle(Bitmap bitmap) {
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
                System.gc();
            }
            bitmap = null;
        }
    }

    private static Bitmap rotate(String srcPath, Bitmap bitmap) {
        int degree = readPictureDegree(srcPath);
        if (degree == 0) return bitmap;
        return rotateBitmap(degree, bitmap);
    }

    public static Bitmap rotateBitmap(int angle, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                true);
    }

    public static void compressBitmapToFile(Bitmap bitmap, boolean isPng, int quality,
                                            String outputFile) throws IOException {
        Bitmap.CompressFormat format = null;
        format = isPng ? (Bitmap.CompressFormat.PNG) : (Bitmap.CompressFormat.JPEG);
        FileOutputStream fos = new FileOutputStream(outputFile);
        if (fos != null && bitmap != null && !bitmap.isRecycled() && bitmap.compress(format,
                quality, fos)) {
            fos.flush();
            fos.close();
            recycle(bitmap);
        }
    }

    public static Bitmap extractThumbnail(Bitmap source, int width, int height, int options) {
        if (source == null) {
            return null;
        }
        int swidth = source.getWidth();
        int sheight = source.getHeight();
        float scale = 1.0f;
        if ((float) swidth / sheight < (float) width / height) {
            scale = width / (float) swidth;
        } else {
            scale = height / (float) sheight;
        }
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        return transform(matrix, source, width, height, OPTIONS_SCALE_UP | options);
    }

    private static Bitmap transform(Matrix scaler, Bitmap source, int targetWidth, int targetHeight,
                                    int options) {
        boolean scaleUp = (options != 0);
        boolean recycle = (options != 0);

        int deltaX = source.getWidth() - targetWidth;
        int deltaY = source.getHeight() - targetHeight;
        if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
            /*
             * In this case the bitmap is smaller, at least in one dimension,
             * than the target.  Transform it by placing as much of the image
             * as possible into the target and leaving the top/bottom or
             * left/right (or both) black.
             */
            Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.RGB_565);
            Canvas c = new Canvas(b2);
            int deltaXHalf = Math.max(0, deltaX / 2);
            int deltaYHalf = Math.max(0, deltaY / 2);
            Rect src = new Rect(deltaXHalf, deltaYHalf,
                    deltaXHalf + Math.min(targetWidth, source.getWidth()),
                    deltaYHalf + Math.min(targetHeight, source.getHeight()));
            int dstX = (targetWidth - src.width()) / 2;
            int dstY = (targetHeight - src.height()) / 2;
            Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight - dstY);
            c.drawBitmap(source, src, dst, null);
            if (recycle) {
                source.recycle();
            }
            return b2;
        }
        float bitmapWidthF = source.getWidth();
        float bitmapHeightF = source.getHeight();

        float bitmapAspect = bitmapWidthF / bitmapHeightF;
        float viewAspect = (float) targetWidth / targetHeight;

        if (bitmapAspect > viewAspect) {
            float scale = targetHeight / bitmapHeightF;
            if (scale < .9F || scale > 1F) {
                scaler.setScale(scale, scale);
            } else {
                scaler = null;
            }
        } else {
            float scale = targetWidth / bitmapWidthF;
            if (scale < .9F || scale > 1F) {
                scaler.setScale(scale, scale);
            } else {
                scaler = null;
            }
        }
        Bitmap b1;
        if (scaler != null) {
            // this is used for minithumb and crop, so we want to filter here.
            b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), scaler,
                    true);
        } else {
            b1 = source;
        }
        if (recycle && b1 != source) {
            source.recycle();
        }
        int dx1 = Math.max(0, b1.getWidth() - targetWidth);
        int dy1 = Math.max(0, b1.getHeight() - targetHeight);
        Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth, targetHeight);
        if (b2 != b1) {
            if (recycle || b1 != source) {
                b1.recycle();
            }
        }

        return b2;
    }

    public static Bitmap extractThumbnail(Bitmap bitmap, int maxWidth, int maxHeight) {
        Bitmap bmp = extractThumbnail(bitmap, maxWidth, maxHeight, OPTIONS_NONE);
        recycle(bitmap);
        return bmp;
    }

    public static String getImagePathFromUri(Uri uri) {
        String path = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        try {
            Cursor cursor = MyApplication.getInstance().getContentResolver()
                    .query(uri, proj, null, null, null);
            if (cursor != null && cursor.getCount() > 0 && cursor.getColumnCount() > 0) {
                int column_index = -1;
                try {
                    column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    cursor.close();
                }
                if (column_index != -1) {
                    cursor.moveToFirst();
                    path = cursor.getString(column_index);
                }
                cursor.close();
            }
        } catch (Exception e) {
            // P.e("Can not open cursor for image retrieving");
            path = null;
        }
        return path;
    }

    /**
     * get BitmapFactory.Options of the image without decoding it into RAM.
     *
     * @param srcPath absolute path of the image.
     * @return out Options by decoding. You can get outWidth, outHeight and MIMEType from this
     * Options by some methods like Options.getWidth.
     */
    public static BitmapFactory.Options getBitmapSizeInOpt(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, newOpts);
        return newOpts;
    }

    /**
     * Decode a bitmap into memory from a file on disk. This method may rotate the origin image if
     * it's necessary according to the exif information.
     *
     * @param srcPath absolute path of the image.
     * @return The decoded bitmap. May be null.
     */
    public static Bitmap decodeBitmapFromFile(String srcPath) {
        return decodeBitmapFromFile(srcPath, MAX_PIC_WIDTH, MAX_PIC_HEIGHT);
    }

    /**
     * Decode a bitmap into memory from a file on disk. This method may rotate the origin image if
     * it's necessary according to the exif information.
     *
     * @param srcPath   absolute path of the image.
     * @param reqWidth  required width pixels in int.
     * @param reqHeight required height pixels in int.
     * @return The decoded bitmap. May be null.
     */
    public static Bitmap decodeBitmapFromFile(String srcPath, int reqWidth, int reqHeight) {
        if (srcPath == null || reqWidth < 0 || reqHeight < 0) {
            return null;
        }

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inSampleSize = calculateInSampleSize(newOpts, reqWidth, reqHeight);//设置采样率
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收

        newOpts.inJustDecodeBounds = false;
        // CrashlyticsUtil.trackMemory();
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return rotate(srcPath, bitmap);
        //      return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        //其实是无效的,大家尽管尝试
    }

    //added by YlorD. Copy from Android official document.
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                            int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    || (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            inSampleSize *= 2;
        }

        return inSampleSize;
    }


    /**
     * Scan the new media file to insert it to MediaStore db. May used when a new media file is
     * created by download or any other way. Notice that the filePath need NOT to be a regular
     * file uri String which start with a scheme of "file://".
     *
     * @param context  the context to send a broadcast.
     * @param filePath absolute path of the file.
     */
    public static void scanNewMediaFile(Context context, String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.parse(SchemeType._FILE + filePath);
            intent.setData(uri);
            context.sendBroadcast(intent);
        }
    }

    public static String compressAndRotateImage(Context context, String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        } else {

            if (!checkCompressDir(context)) {
                return null;
            }

            if (filePath.startsWith(SchemeType._HTTP) || filePath.startsWith(SchemeType._HTTPS)) {
                File cachedFile =
                        new File(FileUtil.getCacheFile(MyApplication.getInstance()), "Glide");
                if (cachedFile != null) {
                    return cachedFile.getAbsolutePath();
                } else {
                    return null;
                }
            } else {
                File f = new File(filePath);
                if (f.exists()) {
                    if (f.length() > MAX_UPLOAD_FILE_SIZE) {
                        ToastUtil.showMessage(MyApplication.getInstance(), MyApplication.getInstance().getString(R.string.file_over_size));
                        return null;
                    }

                    BitmapFactory.Options opts = NAImageUtils.getBitmapSizeInOpt(filePath);

                    //5.1需求 图片长宽比大于2时不压缩图片直接上传
                    float rate = ((float) opts.outWidth) / ((float) opts.outHeight);
                    //5.5修正 图片长大于8192时无法decode 在编辑图片页面会无法显示
                    if (opts.outHeight < 8192 && rate > 2f) {
                        return filePath;
                    }


                    int degree = NAImageUtils.readPictureDegree(filePath);

                    //角度不为0，需要旋转处理
                    boolean isInvalidDegree = degree != 0;
                    //大小超过限制，需要缩放处理
                    boolean isInvalidSize =
                            (MIME_TYPE.PNG.equals(opts.outMimeType) || MIME_TYPE.JPEG.equals(
                                    opts.outMimeType)) && (opts.outWidth > MAX_PIC_WIDTH && rate <= 2f
                                    || opts.outHeight > MAX_PIC_HEIGHT && rate <= 2f);
                    //WebP图片，需要转换成JEPG
                    boolean isWebP = MIME_TYPE.WEBP.equals(opts.outMimeType);
                    if (isInvalidSize || isInvalidDegree || isWebP) {
                        float width = MAX_PIC_WIDTH;
                        float height = MAX_PIC_WIDTH;
                        Bitmap bitmap = null;
                        if (rate < 2f && (opts.outWidth > MAX_PIC_WIDTH || opts.outHeight > MAX_PIC_WIDTH)) {
                            if (opts.outWidth < MAX_PIC_WIDTH) {
                                return filePath;
                            }
                            height = ((float) opts.outHeight / (float) opts.outWidth) * MAX_PIC_WIDTH;
                            bitmap = NAImageUtils.decodeBitmapFromFile(filePath, MAX_PIC_WIDTH,
                                    (int) height);
                        }

                        if (rate > 2f && (opts.outWidth > MAX_PIC_WIDTH || opts.outHeight > MAX_PIC_WIDTH) &&
                                (opts.outWidth < MAX_PIC_WIDTH || opts.outHeight < MAX_PIC_WIDTH)) {
                            return filePath;
                        }

                        if ((rate > 2f || rate < 2f) && (opts.outWidth > MAX_PIC_WIDTH && opts.outHeight > MAX_PIC_WIDTH)) {
                            if (opts.outWidth > opts.outHeight) {
                                width = ((float) (opts.outWidth) / (float) (opts.outHeight)) * MAX_PIC_HEIGHT;
                                height = MAX_PIC_HEIGHT;
                            } else if (opts.outHeight > opts.outWidth) {
                                height = ((float) (opts.outHeight) / (float) opts.outWidth) * MAX_PIC_WIDTH;
                                width = MAX_PIC_WIDTH;
                            }
                            bitmap = NAImageUtils.decodeBitmapFromFile(filePath, (int) width,
                                    (int) height);
                        } else {
                            bitmap = NAImageUtils.decodeBitmapFromFile(filePath, MAX_PIC_WIDTH,
                                    MAX_PIC_HEIGHT);
                        }

                        if (bitmap == null) {
                            return null;
                        }
                        String fileName =
                                filePath.substring(filePath.lastIndexOf(File.separator) + 1);
                        String compress = getCompressPath(context) + fileName;

                        try {
                            FileOutputStream out = new FileOutputStream(compress);
                            Bitmap.CompressFormat format = MIME_TYPE.PNG.equals(opts.outMimeType)
                                    ? (Bitmap.CompressFormat.PNG) : (Bitmap.CompressFormat.JPEG);
                            bitmap.compress(format, 90, out);
                            if (MIME_TYPE.JPEG.equals(opts.outMimeType)) {
                                copyExifInfo(filePath, compress, bitmap.getWidth(),
                                        bitmap.getHeight(), ExifInterface.ORIENTATION_NORMAL);
                            }
                            return compress;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        return filePath;
                    }
                }
            }
        }
        return null;
    }

    private static String getCompressPath(Context context) {
        return StorageUtils.getCachePath(context) + "/pic/compress/";
    }

    private static boolean checkCompressDir(Context context) {
        File dir = new File(getCompressPath(context));
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 50) {
                FileUtil.deleteFile(dir);
                return dir.mkdirs();
            }
            return true;
        } else {
            return dir.mkdirs();
        }
    }

    public static int downloadPicFromUrl(String uri, String filePath) {
        InputStream response = null;
        OutputStream out = null;
        int size = 0;
        try {
            URL url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // must be GET
            urlConnection.setRequestMethod("GET");
            response = new BufferedInputStream(urlConnection.getInputStream());
            out = new BufferedOutputStream(new FileOutputStream(filePath));
            byte[] buffer = new byte[8192];
            int amount;
            out.flush();
            while ((amount = response.read(buffer)) >= 0) {
                out.write(buffer, 0, amount);
                size += amount;
            }
            return size;
        } catch (MalformedURLException e) {
            //P.e(e, "MalformedURLException");
            return -1;
        } catch (IOException e) {
            //P.e(e, "IOException");
            return -2;
        } catch (Exception e) {
            //P.e(e, "Unknown exception");
            return -3;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int targetWidth, int targetHeight) {
        // 获得图片的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 设置想要的大小
        int newWidth = targetWidth;
        int newHeight = targetHeight;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        Canvas canvas = new Canvas();
        Paint paint = new Paint();
        // 放在画布上
        canvas.drawBitmap(newBitmap, 0, 0, paint);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return newBitmap;
    }

    public static Bitmap cropArticleCover(String bitmapPath, int wantedWidth, int wantedHeight) {
        try {
            Bitmap bitmap;
            BitmapFactory.Options options = new BitmapFactory.Options();
            BitmapFactory.decodeFile(bitmapPath, options);
            boolean isCropWidth;
            boolean isSkipCrop = false;//是否跳过裁剪
            if ((options.outWidth < wantedWidth) && (options.outHeight < wantedHeight)) {
                isSkipCrop = true;
            }
            if (!isSkipCrop) {
                //加载之前,先设置缩放。避免大图OOM
                bitmap = decodeBitmapFromFile(bitmapPath, wantedWidth * 3, wantedHeight * 3);
                if (bitmap == null) {
                    return null;
                }
                int bitmapWidth = bitmap.getWidth();
                int bitmapHeight = bitmap.getHeight();
                float scaleWidth = ((float) wantedWidth) / bitmapWidth;
                float scaleHeight = ((float) wantedHeight) / bitmapHeight;
                Matrix matrix = new Matrix();
                if (scaleWidth < scaleHeight) {//高度比率较小,则原图高度比较大。以小边为基准截缩放
                    matrix.setScale(scaleHeight, scaleHeight);
                    isCropWidth = false;
                } else {
                    matrix.setScale(scaleWidth, scaleWidth);
                    isCropWidth = true;
                }
                //先缩放
                Bitmap scaledBitmap =
                        Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);
                Bitmap wantedBitmap;
                //再裁剪
                if (isCropWidth) {
                    int startY = (scaledBitmap.getHeight() / 2 - wantedHeight / 2) < 0 ? 0
                            : (scaledBitmap.getHeight() / 2 - wantedHeight / 2);
                    wantedBitmap =
                            Bitmap.createBitmap(scaledBitmap, 0, startY, wantedWidth, wantedHeight);
                } else {
                    int startX = (scaledBitmap.getWidth() / 2 - wantedWidth / 2) < 0 ? 0
                            : (scaledBitmap.getWidth() / 2 - wantedWidth / 2);
                    wantedBitmap =
                            Bitmap.createBitmap(scaledBitmap, startX, 0, wantedWidth, wantedHeight);
                }
                return wantedBitmap;
            } else {
                bitmap = NAImageUtils.decodeBitmapFromFile(bitmapPath, 900, 900);
                return bitmap;
            }
        } catch (Exception e) {
            //P.e(e, null);
            return null;
        }
    }

    public static int[] getWidthAndHeight(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int[] size = new int[2];
        size[0] = bitmap.getWidth();
        size[1] = bitmap.getHeight();
        return size;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap renderGaussianBlur(Context ctx, Bitmap image) throws Exception {
        final float BITMAP_SCALE = 0.08f;
        final float BLUR_RADIUS = 18.5f;

        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(ctx);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    public static Drawable overlayBitmap(Drawable d1, Drawable d2) {
        if (d1 != null && d2 != null) {
            Drawable[] drawables = new Drawable[2];
            drawables[0] = d1;
            drawables[1] = d2;
            return new LayerDrawable(drawables);
        } else if (d1 != null) {
            Drawable[] drawables = new Drawable[1];
            drawables[0] = d1;
            return new LayerDrawable(drawables);
        } else if (d2 != null) {
            Drawable[] drawables = new Drawable[1];
            drawables[0] = d2;
            return new LayerDrawable(drawables);
        } else {
            return null;
        }
    }

    public static boolean copyExifInfo(String fromPath, String toPath) {
        return copyExifInfo(fromPath, toPath, -1, -1, -1);
    }

    public static boolean copyExifInfo(String fromPath, String toPath, int w, int h,
                                       int orientation) {
        try {
            ExifInterface exifFrom = new ExifInterface(fromPath);
            ExifInterface exifTo = new ExifInterface(toPath);

            // From API 11
            if (Build.VERSION.SDK_INT >= 11) {
                if (exifFrom.getAttribute(ExifInterface.TAG_APERTURE) != null) {
                    exifTo.setAttribute(ExifInterface.TAG_APERTURE,
                            exifFrom.getAttribute(ExifInterface.TAG_APERTURE));
                }
                if (exifFrom.getAttribute(ExifInterface.TAG_EXPOSURE_TIME) != null) {
                    exifTo.setAttribute(ExifInterface.TAG_EXPOSURE_TIME,
                            exifFrom.getAttribute(ExifInterface.TAG_EXPOSURE_TIME));
                }
                if (exifFrom.getAttribute(ExifInterface.TAG_ISO) != null) {
                    exifTo.setAttribute(ExifInterface.TAG_ISO,
                            exifFrom.getAttribute(ExifInterface.TAG_ISO));
                }
            }
            if (exifFrom.getAttribute(ExifInterface.TAG_GPS_ALTITUDE) != null) {
                exifTo.setAttribute(ExifInterface.TAG_GPS_ALTITUDE,
                        exifFrom.getAttribute(ExifInterface.TAG_GPS_ALTITUDE));
            }
            if (exifFrom.getAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF) != null) {
                exifTo.setAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF,
                        exifFrom.getAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF));
            }
            if (exifFrom.getAttribute(ExifInterface.TAG_FOCAL_LENGTH) != null) {
                exifTo.setAttribute(ExifInterface.TAG_FOCAL_LENGTH,
                        exifFrom.getAttribute(ExifInterface.TAG_FOCAL_LENGTH));
            }
            if (exifFrom.getAttribute(ExifInterface.TAG_GPS_DATESTAMP) != null) {
                exifTo.setAttribute(ExifInterface.TAG_GPS_DATESTAMP,
                        exifFrom.getAttribute(ExifInterface.TAG_GPS_DATESTAMP));
            }
            if (exifFrom.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD) != null) {
                exifTo.setAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD,
                        exifFrom.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD));
            }
            if (exifFrom.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP) != null) {
                exifTo.setAttribute(ExifInterface.TAG_GPS_TIMESTAMP,
                        exifFrom.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP));
            }
            if (exifFrom.getAttribute(ExifInterface.TAG_DATETIME) != null) {
                exifTo.setAttribute(ExifInterface.TAG_DATETIME,
                        exifFrom.getAttribute(ExifInterface.TAG_DATETIME));
            }
            if (exifFrom.getAttribute(ExifInterface.TAG_FLASH) != null) {
                exifTo.setAttribute(ExifInterface.TAG_FLASH,
                        exifFrom.getAttribute(ExifInterface.TAG_FLASH));
            }
            if (exifFrom.getAttribute(ExifInterface.TAG_GPS_LATITUDE) != null) {
                exifTo.setAttribute(ExifInterface.TAG_GPS_LATITUDE,
                        exifFrom.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
            }
            if (exifFrom.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF) != null) {
                exifTo.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF,
                        exifFrom.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF));
            }
            if (exifFrom.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) != null) {
                exifTo.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,
                        exifFrom.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
            }
            if (exifFrom.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF) != null) {
                exifTo.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF,
                        exifFrom.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF));
            }
            if (exifFrom.getAttribute(ExifInterface.TAG_MAKE) != null) {
                exifTo.setAttribute(ExifInterface.TAG_MAKE,
                        exifFrom.getAttribute(ExifInterface.TAG_MAKE));
            }
            if (exifFrom.getAttribute(ExifInterface.TAG_MODEL) != null) {
                exifTo.setAttribute(ExifInterface.TAG_MODEL,
                        exifFrom.getAttribute(ExifInterface.TAG_MODEL));
            }
            if (exifFrom.getAttribute(ExifInterface.TAG_WHITE_BALANCE) != null) {
                exifTo.setAttribute(ExifInterface.TAG_WHITE_BALANCE,
                        exifFrom.getAttribute(ExifInterface.TAG_WHITE_BALANCE));
            }

            //Need to update it, with your new height width
            if (h == -1) {
                if (exifFrom.getAttribute(ExifInterface.TAG_IMAGE_LENGTH) != null) {
                    exifTo.setAttribute(ExifInterface.TAG_IMAGE_LENGTH,
                            exifFrom.getAttribute(ExifInterface.TAG_IMAGE_LENGTH));
                }
            } else {
                exifTo.setAttribute(ExifInterface.TAG_IMAGE_LENGTH, String.valueOf(h));
            }
            if (w == -1) {
                if (exifFrom.getAttribute(ExifInterface.TAG_IMAGE_WIDTH) != null) {
                    exifTo.setAttribute(ExifInterface.TAG_IMAGE_WIDTH,
                            exifFrom.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));
                }
            } else {
                exifTo.setAttribute(ExifInterface.TAG_IMAGE_WIDTH, String.valueOf(w));
            }
            if (orientation == -1) {
                if (exifFrom.getAttribute(ExifInterface.TAG_ORIENTATION) != null) {
                    exifTo.setAttribute(ExifInterface.TAG_ORIENTATION,
                            exifFrom.getAttribute(ExifInterface.TAG_ORIENTATION));
                }
            } else {
                exifTo.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(orientation));
            }

            exifTo.saveAttributes();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e1) {
            e1.printStackTrace();
            return false;
        }
    }

    public static boolean writeExifDuitang(String filePath) {
        try {
            ExifInterface exif = new ExifInterface(filePath);
            exif.setAttribute("Software", "DuiTang");
            exif.saveAttributes();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static byte[] bitmap2ByteArr(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 获取临时文件名
     */
    public static String getTempFileNamePrefix() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
        return format.format(new Date(System.currentTimeMillis()));
    }

    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }

    public static Bitmap drawableToBitmap(int size, Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && bitmap.getHeight() > 0) {
                Matrix matrix = new Matrix();
                float scaleHeight = size * 1.0f / bitmapDrawable.getIntrinsicHeight();
                matrix.postScale(scaleHeight, scaleHeight);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                return bitmap;
            }
        }
        bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * draw 9Path
     *
     * @param canvas Canvas
     * @param bmp    9path bitmap
     * @param rect   9path rect
     */
    public static void drawNinePath(Canvas canvas, Bitmap bmp, Rect rect) {
        NinePatch patch = new NinePatch(bmp, bmp.getNinePatchChunk(), null);
        patch.draw(canvas, rect);
    }

}
