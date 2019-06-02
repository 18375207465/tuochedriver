package com.tuochebang.service.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PictureUtil {
    private static final int COMPRESS_LEVEL = 40;
    private static final int PIC_LOAD_HEIGHT = 600;
    private static final int PIC_LOAD_WIDTH = 360;

    public static File getCompressPicFile(String filePath) throws IOException {
        Exception e;
        Throwable th;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, PIC_LOAD_WIDTH, PIC_LOAD_HEIGHT);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        if (bitmap == null) {
            return null;
        }
        bitmap = rotateBitmap(bitmap, readPictureDegree(filePath));
        FileOutputStream fos = null;
        File file = null;
        try {
            FileOutputStream fos2 = null;
            File compressImgFile = new File(Environment.getExternalStorageDirectory(), "small_" + new File(filePath).getName());
            fos2 = new FileOutputStream(compressImgFile);
            bitmap.compress(CompressFormat.JPEG, 40, fos2);
            if (fos2 != null) {
                try {
                    fos2.close();
                } catch (IOException e322) {
                    e322.printStackTrace();
                    fos = fos2;
                    return compressImgFile;
                }
            }
            fos = fos2;
            return compressImgFile;
        } catch (Exception e4) {
            return null;
        }

    }

    public static void compressFile(String filePath) throws IOException {
        Exception e;
        Throwable th;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, PIC_LOAD_WIDTH, PIC_LOAD_HEIGHT);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        if (bitmap != null) {
            bitmap = rotateBitmap(bitmap, readPictureDegree(filePath));
            FileOutputStream fos = null;
            try {
                FileOutputStream fos2 = new FileOutputStream(new File(filePath));
                try {
                    bitmap.compress(CompressFormat.JPEG, 40, fos2);
                    if (fos2 != null) {
                        try {
                            fos2.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                            fos = fos2;
                            return;
                        }
                    }
                    fos = fos2;
                } catch (Exception e3) {
                    e = e3;
                    fos = fos2;
                    try {
                        e.printStackTrace();
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fos = fos2;
                    if (fos != null) {
                        fos.close();
                    }
                }
            } catch (Exception e4) {
                e = e4;
                e.printStackTrace();
                if (fos != null) {
                    fos.close();
                }
            }
        }
    }

    public static void compressFile(Context context, Uri uri, String filePath) throws IOException {
        Exception e;
        Throwable th;
        InputStream inputStream = openInputStream(context, uri);
        if (inputStream != null) {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            options.inSampleSize = calculateInSampleSize(options, PIC_LOAD_WIDTH, PIC_LOAD_HEIGHT);
            closeInputStream(inputStream);
            options.inJustDecodeBounds = false;
            inputStream = openInputStream(context, uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            closeInputStream(inputStream);
            if (bitmap == null) {
                return;
            }
            FileOutputStream fos = null;
            try {
                File imgFile = new File(filePath);
                imgFile.deleteOnExit();
                imgFile.createNewFile();
                FileOutputStream fos2 = new FileOutputStream(imgFile);
                try {
                    bitmap.compress(CompressFormat.JPEG, 40, fos2);
                    if (fos2 != null) {
                        try {
                            fos2.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                            fos = fos2;
                            return;
                        }
                    }
                    fos = fos2;
                } catch (Exception e3) {
                    e = e3;
                    fos = fos2;
                    try {
                        e.printStackTrace();
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fos = fos2;
                    if (fos != null) {
                        fos.close();
                    }
                }
            } catch (Exception e4) {
                e = e4;
                e.printStackTrace();
                if (fos != null) {
                    fos.close();
                }
            }
        }
    }

    public static void deleteTempFile(String filePath) {
        File tempFile = new File(filePath);
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    public static int readPictureDegree(String path) {
        try {
            switch (new ExifInterface(path).getAttributeInt("Orientation", 1)) {
                case 3:
                    return 180;
                case 6:
                    return 90;
                case 8:
                    return 270;
                default:
                    return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate((float) rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    private static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        if (height <= reqHeight && width <= reqWidth) {
            return 1;
        }
        int heightRatio = Math.round(((float) height) / ((float) reqHeight));
        int widthRatio = Math.round(((float) width) / ((float) reqWidth));
        if (heightRatio < widthRatio) {
            return heightRatio;
        }
        return widthRatio;
    }

    private static int calculateInSampleSize(Options options, int reqWidth) {
        int width = options.outWidth;
        if (width > reqWidth) {
            return Math.round(((float) width) / ((float) reqWidth));
        }
        return 1;
    }

    public static Bitmap decodeSampledBitmapFromResource(String pathName, int reqWidth) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

    private static InputStream openInputStream(Context context, Uri uri) {
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    private static void closeInputStream(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
