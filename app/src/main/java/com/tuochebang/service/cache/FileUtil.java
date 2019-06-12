package com.tuochebang.service.cache;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.load.Key;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.tuochebang.service.app.SchemeType;
import com.tuochebang.service.constant.AppConfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class FileUtil {
    public static String BITMAP_CACHE_SDPATH = (getSDPath() + "formats/");

    public static String getSDPath() {
        return Environment.getExternalStorageDirectory() + File.separator;
    }

    public static String getUserHeaderCacheFolder() {
        return AppConfig.CACHE_IMAGE_FOLDER + File.separator;
    }

    public static void saveFile(Context context, String name, Object obj) {
        if (context != null) {
            try {
                FileOutputStream fos = new FileOutputStream(new File(context.getFilesDir(), name));
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(obj);
                fos.close();
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Object readFile(Context context, String name) {
        File file = new File(context.getFilesDir(), name);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            fis.close();
            ois.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object readFileFromAsset(Context context, String assetName) {
        try {
            InputStream is = context.getAssets().open(assetName);
            ObjectInputStream ois = new ObjectInputStream(is);
            Object obj = ois.readObject();
            is.close();
            ois.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static double getDirSize(File file) {
        if (!file.exists()) {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0d;
        } else if (!file.isDirectory()) {
            return (((double) file.length()) / 1024.0d) / 1024.0d;
        } else {
            double size = 0.0d;
            for (File f : file.listFiles()) {
                size += getDirSize(f);
            }
            return size;
        }
    }

    public static void deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            file.delete();
        }
    }

    public static File createSDFile(String fileName) throws IOException {
        File dirFile = new File(getSDPath() + fileName);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return dirFile;
    }

    public static File createSDFile(String dir, String fileName) throws IOException {
        String path = getSDPath() + dir;
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(path, fileName);
        file.deleteOnExit();
        file.createNewFile();
        return file;
    }

    public static File createCacheFile(String dir, String fileName) throws IOException {
        String path = getUserHeaderCacheFolder() + dir;
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(path, fileName);
        file.deleteOnExit();
        file.createNewFile();
        return file;
    }

    public static String getFromAssets(Context context, String fileName) throws IOException {
        Exception e;
        Throwable th;
        StringBuffer result = new StringBuffer();
        InputStreamReader inputReader = null;
        BufferedReader bufReader = null;
        try {
            InputStreamReader inputReader2 = new InputStreamReader(context.getResources().getAssets().open(fileName), Key.STRING_CHARSET_NAME);
            try {
                BufferedReader bufReader2 = new BufferedReader(inputReader2);
                while (true) {
                    try {
                        String lineTxt = bufReader2.readLine();
                        if (lineTxt == null) {
                            break;
                        }
                        result.append(lineTxt);
                    } catch (Exception e2) {
                        e = e2;
                        bufReader = bufReader2;
                        inputReader = inputReader2;
                    } catch (Throwable th2) {
                        th = th2;
                        bufReader = bufReader2;
                        inputReader = inputReader2;
                    }
                }
                if (inputReader2 != null) {
                    try {
                        inputReader2.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                        bufReader = bufReader2;
                        inputReader = inputReader2;
                    }
                }
                if (bufReader2 != null) {
                    bufReader2.close();
                }
                bufReader = bufReader2;
                inputReader = inputReader2;
            } catch (Exception e4) {
                e = e4;
                inputReader = inputReader2;
                try {
                    e.printStackTrace();
                    if (inputReader != null) {
                        try {
                            inputReader.close();
                        } catch (IOException e32) {
                            e32.printStackTrace();
                        }
                    }
                    if (bufReader != null) {
                        bufReader.close();
                    }
                    return result.toString().trim();
                } catch (Throwable th3) {
                    th = th3;
                    if (inputReader != null) {
                        try {
                            inputReader.close();
                        } catch (IOException e322) {
                            e322.printStackTrace();
                        }
                    }
                    if (bufReader != null) {
                        bufReader.close();
                    }
                }
            } catch (Throwable th4) {
                th = th4;
                inputReader = inputReader2;
                if (inputReader != null) {
                    inputReader.close();
                }
                if (bufReader != null) {
                    bufReader.close();
                }
            }
        } catch (Exception e5) {
            e = e5;
            e.printStackTrace();
            if (inputReader != null) {
                inputReader.close();
            }
            if (bufReader != null) {
                bufReader.close();
            }
            return result.toString().trim();
        }
        return result.toString().trim();
    }

    public static void writeStreamToFile(InputStream stream, File file) throws IOException {
        OutputStream output = null;
        try {
            output = new FileOutputStream(file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (Throwable th) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            byte[] buffer = new byte[1024];
            while (true) {
                int read = stream.read(buffer);
                if (read == -1) {
                    break;
                }
                output.write(buffer, 0, read);
            }
            output.flush();
            output.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        } catch (Throwable th2) {
            output.close();
        }
        try {
            stream.close();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    public static void saveBitmap(Bitmap bm, String picName) {
        Log.e("", "保存图片");
        try {
            if (!isFileExist("")) {
                createSDDir("");
            }
            File f = new File(BITMAP_CACHE_SDPATH, picName + PictureFileUtils.POSTFIX);
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Log.e("", "已经保存");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static File createSDDir(String dirName) throws IOException {
        File dir = new File(BITMAP_CACHE_SDPATH + dirName);
        if (Environment.getExternalStorageState().equals("mounted")) {
            System.out.println("createSDDir:" + dir.getAbsolutePath());
            System.out.println("createSDDir:" + dir.mkdir());
        }
        return dir;
    }

    public static boolean isFileExist(String fileName) {
        File file = new File(BITMAP_CACHE_SDPATH + fileName);
        file.isFile();
        return file.exists();
    }

    public static void delFile(String fileName) {
        File file = new File(BITMAP_CACHE_SDPATH + fileName);
        if (file.isFile()) {
            file.delete();
        }
        file.exists();
    }

    public static void deleteDir() {
        File dir = new File(BITMAP_CACHE_SDPATH);
        if (dir != null && dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    file.delete();
                } else if (file.isDirectory()) {
                    deleteDir();
                }
            }
            dir.delete();
        }
    }

    /**
     * 返回缓存文件夹
     */
    public static File getCacheFile(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = null;
            file = context.getExternalCacheDir();//获取系统管理的sd卡缓存文件
            if (file == null) {//如果获取的文件为空,就使用自己定义的缓存文件夹做缓存路径
                file = new File(getCacheFilePath(context));
                makeDirs(file);
            }
            return file;
        } else {
            return context.getCacheDir();
        }
    }

    /**
     * 获取自定义缓存文件地址
     *
     * @param context
     * @return
     */
    public static String getCacheFilePath(Context context) {
        String packageName = context.getPackageName();
        return "/mnt/sdcard/" + packageName;
    }

    /**
     * 创建未存在的文件夹
     *
     * @param file
     * @return
     */
    public static File makeDirs(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }


    public static String getFileFromUri(Uri uri, Context context) {
        if (uri == null) {
            return null;
        }
        switch (uri.getScheme()) {
            case SchemeType.CONTENT:
                return getFileFromContentUri(uri, context);
            case "file":
                return uri.getPath();
            default:
                return null;
        }
    }

    /**
     * Gets the corresponding path to a file from the given content:// URI
     *
     * @param contentUri The content:// URI to find the file path from
     * @param context    Context
     * @return the file path as a string
     */

    private static String getFileFromContentUri(Uri contentUri, Context context) {
        File rootDataDir = context.getFilesDir();
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(rootDataDir + File.separator + fileName);
            copyFile(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }


    public static void copyFile(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            copyStream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int copyStream(InputStream input, OutputStream output) throws Exception, IOException {
        final int BUFFER_SIZE = 1024 * 2;
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
            }
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        return count;
    }


}
