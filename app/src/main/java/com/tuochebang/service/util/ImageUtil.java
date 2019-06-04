package com.tuochebang.service.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;

import com.tuochebang.service.app.MyApplication;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import uk.co.senab.photoview.IPhotoView;

public class ImageUtil {
    private static final int COMPRESS_LEVEL = 40;
    private static float[] carray = new float[20];

    public static Bitmap readBitMap(Context context, int resId) {
        Options opt = new Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        return BitmapFactory.decodeStream(context.getResources().openRawResource(resId), null, opt);
    }

    public static Bitmap getSDCardImg(String imagePath) {
        Options opt = new Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        return BitmapFactory.decodeFile(imagePath, opt);
    }

    public static Bitmap resetImage(Bitmap sourceBitmap, int resetWidth, int resetHeight) {
        float maxTmpScale;
        int width = sourceBitmap.getWidth();
        int height = sourceBitmap.getHeight();
        float scaleWidth = ((float) resetWidth) / ((float) width);
        float scaleHeight = ((float) resetHeight) / ((float) height);
        if (scaleWidth >= scaleHeight) {
            maxTmpScale = scaleWidth;
        } else {
            maxTmpScale = scaleHeight;
        }
        int tmpWidth = (int) (((float) width) * maxTmpScale);
        int tmpHeight = (int) (((float) height) * maxTmpScale);
        Matrix m = new Matrix();
        m.setScale(maxTmpScale, maxTmpScale, (float) tmpWidth, (float) tmpHeight);
        return Bitmap.createBitmap(Bitmap.createBitmap(sourceBitmap, 0, 0, width, height, m, false), (tmpWidth - resetWidth) / 2, (tmpHeight - resetHeight) / 2, resetWidth, resetHeight);
    }

    public static Bitmap getNativeImage(String imagePath) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap myBitmap = BitmapFactory.decodeFile(imagePath, options);
        int be = (int) (((float) options.outHeight) / 200.0f);
        if (((double) (((float) (options.outHeight % IPhotoView.DEFAULT_ZOOM_DURATION)) / 200.0f)) >= 0.5d) {
            be++;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, options);
    }

    public static Drawable CreateStateDrawable(Context context, int bulr, int focus) {
        Drawable bulrDrawable = context.getResources().getDrawable(bulr);
        Drawable focusDrawable = context.getResources().getDrawable(focus);
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{16842919}, focusDrawable);
        drawable.addState(new int[0], bulrDrawable);
        return drawable;
    }

    public static Bitmap getImage(String srcPath) {
        Options newOpts = new Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        int be = 1;
        if (w > h && ((float) w) > 480.0f) {
            be = (int) (((float) newOpts.outWidth) / 480.0f);
        } else if (w < h && ((float) h) > 800.0f) {
            be = (int) (((float) newOpts.outHeight) / 800.0f);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;
        return compressForQuality(BitmapFactory.decodeFile(srcPath, newOpts));
    }

    public static Bitmap compressForQuality(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 40, baos);
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            image.compress(CompressFormat.JPEG, options, baos);
            options -= 10;
        }
        return BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()), null, null);
    }

    public static Bitmap compressForScale(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 40, baos);
        if (baos.toByteArray().length / 1024 > 1024) {
            baos.reset();
            image.compress(CompressFormat.JPEG, 50, baos);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Options newOpts = new Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        int be = 1;
        if (w > h && ((float) w) > 480.0f) {
            be = (int) (((float) newOpts.outWidth) / 480.0f);
        } else if (w < h && ((float) h) > 800.0f) {
            be = (int) (((float) newOpts.outHeight) / 800.0f);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;
        return compressForQuality(BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()), null, newOpts));
    }

    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        Bitmap bmpGrayscale = Bitmap.createBitmap(bmpOriginal.getWidth(), bmpOriginal.getHeight(), Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        paint.setColorFilter(null);
        c.drawBitmap(bmpGrayscale, 0.0f, 0.0f, paint);
        ColorMatrix cm = new ColorMatrix();
        getValueBlackAndWhite();
        cm.set(carray);
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        c.drawBitmap(bmpOriginal, 0.0f, 0.0f, paint);
        return bmpGrayscale;
    }

    public static void getValueSaturation() {
        carray[0] = 5.0f;
        carray[1] = 0.0f;
        carray[2] = 0.0f;
        carray[3] = 0.0f;
        carray[4] = -254.0f;
        carray[5] = 0.0f;
        carray[6] = 5.0f;
        carray[7] = 0.0f;
        carray[8] = 0.0f;
        carray[9] = -254.0f;
        carray[10] = 0.0f;
        carray[11] = 0.0f;
        carray[12] = 5.0f;
        carray[13] = 0.0f;
        carray[14] = -254.0f;
        carray[15] = 0.0f;
        carray[16] = 0.0f;
        carray[17] = 0.0f;
        carray[18] = 5.0f;
        carray[19] = -254.0f;
    }

    private static void getValueBlackAndWhite() {
        carray[0] = 0.308f;
        carray[1] = 0.609f;
        carray[2] = 0.082f;
        carray[3] = 0.0f;
        carray[4] = 0.0f;
        carray[5] = 0.308f;
        carray[6] = 0.609f;
        carray[7] = 0.082f;
        carray[8] = 0.0f;
        carray[9] = 0.0f;
        carray[10] = 0.308f;
        carray[11] = 0.609f;
        carray[12] = 0.082f;
        carray[13] = 0.0f;
        carray[14] = 0.0f;
        carray[15] = 0.0f;
        carray[16] = 0.0f;
        carray[17] = 0.0f;
        carray[18] = 1.0f;
        carray[19] = 0.0f;
    }

    public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
        return toRoundCorner(toGrayscale(bmpOriginal), pixels);
    }

    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        float roundPx = (float) pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable, int pixels) {
        return new BitmapDrawable(toRoundCorner(bitmapDrawable.getBitmap(), pixels));
    }

    public static Bitmap createReflectedImage(Bitmap originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height / 2, width, height / 2, matrix, false);
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height / 2) + height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(originalImage, 0.0f, 0.0f, null);
        canvas.drawRect(0.0f, (float) height, (float) width, (float) (height + 4), new Paint());
        canvas.drawBitmap(reflectionImage, 0.0f, (float) (height + 4), null);
        Paint paint = new Paint();
        paint.setShader(new LinearGradient(0.0f, (float) originalImage.getHeight(), 0.0f, (float) (bitmapWithReflection.getHeight() + 4), 1895825407, ViewCompat.MEASURED_SIZE_MASK, TileMode.MIRROR));
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawRect(0.0f, (float) height, (float) width, (float) (bitmapWithReflection.getHeight() + 4), paint);
        return bitmapWithReflection;
    }

    public static void saveBitmap(String url, Bitmap mBitmap) throws IOException {
        File f = new File(url);
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(CompressFormat.JPEG, 40, fOut);
        try {
            fOut.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static Bitmap getBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            Options bitmapOptions = new Options();
            bitmapOptions.inSampleSize = 2;
            bitmapOptions.inDither = true;
            bitmapOptions.inPreferredConfig = Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, bitmapOptions);
        } catch (FileNotFoundException e) {
        }
        return bitmap;
    }

    public static void deleteImage(String path) {
        new File(path).deleteOnExit();
    }

    public static void distoryBitmap(Bitmap bitmap) {
        if (bitmap != null && bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    @SuppressLint({"NewApi"})
    public static Bitmap blurBitmap(Bitmap bitmap) {
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        RenderScript rs = RenderScript.create(MyApplication.getInstance());
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
        blurScript.setRadius(25.0f);
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);
        allOut.copyTo(outBitmap);
        bitmap.recycle();
        rs.destroy();
        return outBitmap;
    }

    public static Drawable BoxBlurFilter(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] inPixels = new int[(width * height)];
        int[] outPixels = new int[(width * height)];
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_4444);
        bmp.getPixels(inPixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < 7; i++) {
            blur(inPixels, outPixels, width, height, 7.0f);
            blur(outPixels, inPixels, height, width, 7.0f);
        }
        blurFractional(inPixels, outPixels, width, height, 7.0f);
        blurFractional(outPixels, inPixels, height, width, 7.0f);
        bitmap.setPixels(inPixels, 0, width, 0, 0, width, height);
        return new BitmapDrawable(bitmap);
    }

    public static void blur(int[] in, int[] out, int width, int height, float radius) {
        int i;
        int widthMinus1 = width - 1;
        int r = (int) radius;
        int tableSize = (r * 2) + 1;
        int[] divide = new int[(tableSize * 256)];
        for (i = 0; i < tableSize * 256; i++) {
            divide[i] = i / tableSize;
        }
        int inIndex = 0;
        for (int y = 0; y < height; y++) {
            int outIndex = y;
            int ta = 0;
            int tr = 0;
            int tg = 0;
            int tb = 0;
            for (i = -r; i <= r; i++) {
                int rgb = in[clamp(i, 0, width - 1) + inIndex];
                ta += (rgb >> 24) & 255;
                tr += (rgb >> 16) & 255;
                tg += (rgb >> 8) & 255;
                tb += rgb & 255;
            }
            for (int x = 0; x < width; x++) {
                out[outIndex] = (((divide[ta] << 24) | (divide[tr] << 16)) | (divide[tg] << 8)) | divide[tb];
                int i1 = (x + r) + 1;
                if (i1 > widthMinus1) {
                    i1 = widthMinus1;
                }
                int i2 = x - r;
                if (i2 < 0) {
                    i2 = 0;
                }
                int rgb1 = in[inIndex + i1];
                int rgb2 = in[inIndex + i2];
                ta += ((rgb1 >> 24) & 255) - ((rgb2 >> 24) & 255);
                tr += ((16711680 & rgb1) - (16711680 & rgb2)) >> 16;
                tg += ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & rgb1) - (MotionEventCompat.ACTION_POINTER_INDEX_MASK & rgb2)) >> 8;
                tb += (rgb1 & 255) - (rgb2 & 255);
                outIndex += height;
            }
            inIndex += width;
        }
    }

    public static void blurFractional(int[] in, int[] out, int width, int height, float radius) {
        radius -= (float) ((int) radius);
        float f = 1.0f / (1.0f + (2.0f * radius));
        int inIndex = 0;
        for (int y = 0; y < height; y++) {
            int outIndex = y;
            out[outIndex] = in[0];
            outIndex += height;
            for (int x = 1; x < width - 1; x++) {
                int i = inIndex + x;
                int rgb1 = in[i - 1];
                int rgb2 = in[i];
                int rgb3 = in[i + 1];
                int a2 = (rgb2 >> 24) & 255;
                int r2 = (rgb2 >> 16) & 255;
                int g2 = (rgb2 >> 8) & 255;
                int b2 = rgb2 & 255;
                int i2 = ((((int) (((float) (a2 + ((int) (((float) (((rgb1 >> 24) & 255) + ((rgb3 >> 24) & 255))) * radius)))) * f)) << 24) | (((int) (((float) (r2 + ((int) (((float) (((rgb1 >> 16) & 255) + ((rgb3 >> 16) & 255))) * radius)))) * f)) << 16)) | (((int) (((float) (g2 + ((int) (((float) (((rgb1 >> 8) & 255) + ((rgb3 >> 8) & 255))) * radius)))) * f)) << 8);
                out[outIndex] = i2 | ((int) (((float) (b2 + ((int) (((float) ((rgb1 & 255) + (rgb3 & 255))) * radius)))) * f));
                outIndex += height;
            }
            out[outIndex] = in[width - 1];
            inIndex += width;
        }
    }

    public static int clamp(int x, int a, int b) {
        if (x < a) {
            return a;
        }
        return x > b ? b : x;
    }

    public static byte[] Bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

}
