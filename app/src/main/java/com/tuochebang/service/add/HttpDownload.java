package com.tuochebang.service.add;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.tuochebang.service.MainActivity;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadRequest;

import java.util.concurrent.locks.Condition;

public class HttpDownload {
    private Context mContext;
    private ProgressDialog progressDialog;
    private MyDownloadListener myDownloadListener =new MyDownloadListener();
    private String apkName="siji.apk";
    private String pathFile = Environment.getExternalStorageDirectory().getAbsolutePath()+"/tuochebang";//文件夹路径

    public HttpDownload(Context context){
        mContext=context;
    }
    public void download(String url){
        initProgressDialog();
        // url 下载地址
        // fileFolder 保存的文件夹
        // fileName 文件名
        // isRange 是否断点续传下载
        // isDeleteOld 如果发现文件已经存在是否删除后重新下载
        //path = Environment.getExternalStorageDirectory().getAbsolutePath();
        DownloadRequest downloadRequest = NoHttp.createDownloadRequest(url, pathFile, apkName, false, true);
        NoHttp.getDownloadQueueInstance().add(0, downloadRequest, myDownloadListener);
    }
    private class MyDownloadListener implements DownloadListener {
        @Override
        public void onDownloadError(int what, Exception exception) {
            progressDialog.dismiss();
        }

        @Override
        public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {

        }

        @Override
        public void onProgress(int what, int progress, long fileCount, long speed) {
            progressDialog.setMax(100);
            progressDialog.setProgress(progress);
           // Log.i("Flog","progress-"+progress+"  fileCount-"+fileCount+"   speed-"+speed);
        }

        @Override
        public void onFinish(int what, String filePath) {
            progressDialog.dismiss();
            AnZhuangAPK.installNormal(mContext,pathFile+"/"+apkName);
        }

        @Override
        public void onCancel(int what) {

        }
    }

    //设置进度条提示
    private void initProgressDialog() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //progressDialog.setMax(100);
        progressDialog.setTitle("更新");
        //progressDialog.setMessage("正在更新软件，软件更新完成后将重启，请不要执行任何操作");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}
