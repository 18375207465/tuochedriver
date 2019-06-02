package com.tuochebang.service.widget.wxphotoselector;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.framework.app.component.adapter.CommonBaseAdapter;

public class PhotoGridAdapter extends CommonBaseAdapter<String> {
    private String mDirPath;
    private PhotoGridAdapterView.PhotoOnClickLister mPhotoOnClickLister;

    public PhotoGridAdapter(Context context) {
        super(context);
    }

    public void setDirPath(String dirPath) {
        this.mDirPath = dirPath;
    }

    public void setPhotoOnClickLister(PhotoGridAdapterView.PhotoOnClickLister photoOnClickLister) {
        this.mPhotoOnClickLister = photoOnClickLister;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new PhotoGridAdapterView(this.mContext);
        }
        PhotoGridAdapterView photoGridAdapterView = (PhotoGridAdapterView) convertView;
        photoGridAdapterView.setDirPath(this.mDirPath);
        photoGridAdapterView.setPhotoOnClickLister(this.mPhotoOnClickLister);
        photoGridAdapterView.refreshView((String) getItem(position));
        return photoGridAdapterView;
    }
}
