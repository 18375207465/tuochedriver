package com.tuochebang.service.widget.wxphotoselector;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.framework.app.component.utils.ImageLoaderUtil;
import com.framework.app.component.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tuochebang.service.R;

import java.util.ArrayList;

public class PhotoGridAdapterView extends RelativeLayout {
    public static int mSelectLimitedCount = 9;
    public static ArrayList<String> mSelectedImage = new ArrayList();
    private ImageButton mBtnSelect;
    private String mDirPath;
    private ImageView mIvPhoto;
    private PhotoOnClickLister mPhotoOnClickLister;

    public interface PhotoOnClickLister {
        void onPhotoClick(int i);
    }

    public PhotoGridAdapterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }

    public PhotoGridAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public PhotoGridAdapterView(Context context) {
        super(context);
        initViews(context);
    }

    public void setDirPath(String dirPath) {
        this.mDirPath = dirPath;
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.grid_item, this);
        this.mIvPhoto = (ImageView) findViewById(R.id.id_item_image);
        this.mBtnSelect = (ImageButton) findViewById(R.id.id_item_select);
    }

    @TargetApi(16)
    public void refreshView(final String item) {
        ImageLoader.getInstance().displayImage("file://" + this.mDirPath + "/" + item, this.mIvPhoto, ImageLoaderUtil.getDisplayImageOptions(R.drawable.pic_default));
        if (mSelectedImage.contains(this.mDirPath + "/" + item)) {
            this.mBtnSelect.setImageResource(R.mipmap.icon_select_checked);
            this.mIvPhoto.setColorFilter(Color.parseColor("#77000000"));
        } else {
            this.mBtnSelect.setImageResource(R.mipmap.icon_select_unchecked);
            this.mIvPhoto.setColorFilter(null);
        }
        this.mIvPhoto.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PhotoGridAdapterView.mSelectedImage.contains(PhotoGridAdapterView.this.mDirPath + "/" + item)) {
                    PhotoGridAdapterView.mSelectedImage.remove(PhotoGridAdapterView.this.mDirPath + "/" + item);
                    PhotoGridAdapterView.this.mBtnSelect.setImageResource(R.mipmap.icon_select_unchecked);
                    PhotoGridAdapterView.this.mIvPhoto.setColorFilter(null);
                } else if (PhotoGridAdapterView.mSelectedImage.size() >= PhotoGridAdapterView.mSelectLimitedCount) {
                    ToastUtil.showNoticeMsg(PhotoGridAdapterView.this.getContext(), "最多选择" + PhotoGridAdapterView.mSelectLimitedCount + "张图片");
                    return;
                } else {
                    PhotoGridAdapterView.mSelectedImage.add(PhotoGridAdapterView.this.mDirPath + "/" + item);
                    PhotoGridAdapterView.this.mBtnSelect.setImageResource(R.mipmap.icon_select_checked);
                    PhotoGridAdapterView.this.mIvPhoto.setColorFilter(Color.parseColor("#77000000"));
                }
                if (PhotoGridAdapterView.this.mPhotoOnClickLister != null) {
                    PhotoGridAdapterView.this.mPhotoOnClickLister.onPhotoClick(PhotoGridAdapterView.mSelectedImage.size());
                }
            }
        });
    }

    public void setPhotoOnClickLister(PhotoOnClickLister photoOnClickLister) {
        this.mPhotoOnClickLister = photoOnClickLister;
    }
}
