package com.tuochebang.service.widget.wxphotoselector;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Media;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.framework.app.component.utils.ActivityUtil;
import com.tuochebang.service.R;
import com.tuochebang.service.base.BaseActivity;
import com.tuochebang.service.ui.ImageSimpleBrowseActivity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class WxPhotoSelectorActivity extends BaseActivity implements ListImageDirPopupWindow.OnImageDirSelected {
    public static final String EXTRA_RETURN_IMAGES = "return_images";
    public static final String EXTRA_SELECT_LIMITED_COUNT = "select_limited_count";
    private List<String> mAllPhotoList;
    private TextView mChooseDir;
    private HashSet<String> mDirPaths = new HashSet();
    private GridView mGirdView;
    private Handler mHandler = new C10821();
    private List<ImageFloder> mImageFloders = new ArrayList();
    private ListImageDirPopupWindow mListImageDirPopupWindow;
    private File mMostPicFile;
    private PhotoGridAdapter mPhotoGridAdapter;
    private int mPicsSize;
    private TextView mPreViewCount;
    private ProgressDialog mProgressDialog;
    private int mScreenHeight;
    Toolbar mToolBar;
    Button mTxtRight;
    int totalCount = 0;

    /* renamed from: com.tuochebang.user.widget.wxphotoselector.WxPhotoSelectorActivity$1 */
    class C10821 extends Handler {
        C10821() {
        }

        public void handleMessage(Message msg) {
            WxPhotoSelectorActivity.this.mProgressDialog.dismiss();
            WxPhotoSelectorActivity.this.freshViews();
            WxPhotoSelectorActivity.this.initListDirPopupWindw();
        }
    }

    /* renamed from: com.tuochebang.user.widget.wxphotoselector.WxPhotoSelectorActivity$2 */
    class C10832 implements PhotoGridAdapterView.PhotoOnClickLister {
        C10832() {
        }

        public void onPhotoClick(int photoTotalCount) {
            if (photoTotalCount == 0) {
                WxPhotoSelectorActivity.this.mTxtRight.setEnabled(false);
                WxPhotoSelectorActivity.this.mTxtRight.setText("完成");
                WxPhotoSelectorActivity.this.mTxtRight.setVisibility(View.GONE);
                WxPhotoSelectorActivity.this.mPreViewCount.setEnabled(false);
                WxPhotoSelectorActivity.this.mPreViewCount.setText("预览");
                return;
            }
            WxPhotoSelectorActivity.this.mTxtRight.setVisibility(View.VISIBLE);
            WxPhotoSelectorActivity.this.mTxtRight.setEnabled(true);
            WxPhotoSelectorActivity.this.mTxtRight.setText("完成(" + PhotoGridAdapterView.mSelectedImage.size() + "/" + PhotoGridAdapterView.mSelectLimitedCount + ")");
            WxPhotoSelectorActivity.this.mPreViewCount.setEnabled(true);
            WxPhotoSelectorActivity.this.mPreViewCount.setText("预览(" + photoTotalCount + ")");
        }
    }

    /* renamed from: com.tuochebang.user.widget.wxphotoselector.WxPhotoSelectorActivity$3 */
    class C10843 implements OnDismissListener {
        C10843() {
        }

        public void onDismiss() {
            LayoutParams lp = WxPhotoSelectorActivity.this.getWindow().getAttributes();
            lp.alpha = 1.0f;
            WxPhotoSelectorActivity.this.getWindow().setAttributes(lp);
        }
    }

    /* renamed from: com.tuochebang.user.widget.wxphotoselector.WxPhotoSelectorActivity$4 */
    class C10854 implements OnClickListener {
        C10854() {
        }

        public void onClick(View v) {
            WxPhotoSelectorActivity.this.btnFinishOnClick();
        }
    }

    /* renamed from: com.tuochebang.user.widget.wxphotoselector.WxPhotoSelectorActivity$5 */
    class C10865 implements OnClickListener {
        C10865() {
        }

        public void onClick(View v) {
            WxPhotoSelectorActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.user.widget.wxphotoselector.WxPhotoSelectorActivity$6 */
    class C10886 implements Runnable {

        /* renamed from: com.tuochebang.user.widget.wxphotoselector.WxPhotoSelectorActivity$6$1 */
        class C10871 implements FilenameFilter {
            C10871() {
            }

            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg")) {
                    return true;
                }
                return false;
            }
        }

        C10886() {
        }

        public void run() {
            String firstImage = null;
            Cursor mCursor = WxPhotoSelectorActivity.this.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, null, "mime_type=? or mime_type=?", new String[]{"image/jpeg", "image/png"}, "date_modified");
            Log.e("TAG", mCursor.getCount() + "");
            while (mCursor.moveToNext()) {
                String path = mCursor.getString(mCursor.getColumnIndex("_data"));
                Log.e("TAG", path);
                if (firstImage == null) {
                    firstImage = path;
                }
                File parentFile = new File(path).getParentFile();
                if (parentFile != null) {
                    String dirPath = parentFile.getAbsolutePath();
                    if (!WxPhotoSelectorActivity.this.mDirPaths.contains(dirPath)) {
                        WxPhotoSelectorActivity.this.mDirPaths.add(dirPath);
                        ImageFloder imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                        int picSize = parentFile.list(new C10871()).length;
                        WxPhotoSelectorActivity wxPhotoSelectorActivity = WxPhotoSelectorActivity.this;
                        wxPhotoSelectorActivity.totalCount += picSize;
                        imageFloder.setCount(picSize);
                        WxPhotoSelectorActivity.this.mImageFloders.add(imageFloder);
                        if (picSize > WxPhotoSelectorActivity.this.mPicsSize) {
                            WxPhotoSelectorActivity.this.mPicsSize = picSize;
                            WxPhotoSelectorActivity.this.mMostPicFile = parentFile;
                        }
                    }
                }
            }
            mCursor.close();
            WxPhotoSelectorActivity.this.mDirPaths = null;
            WxPhotoSelectorActivity.this.mHandler.sendEmptyMessage(272);
        }
    }

    /* renamed from: com.tuochebang.user.widget.wxphotoselector.WxPhotoSelectorActivity$7 */
    class C10897 implements OnClickListener {
        C10897() {
        }

        public void onClick(View v) {
            WxPhotoSelectorActivity.this.mListImageDirPopupWindow.setAnimationStyle(R.style.anim_popup_dir);
            WxPhotoSelectorActivity.this.mListImageDirPopupWindow.showAsDropDown(WxPhotoSelectorActivity.this.mChooseDir, 0, 0);
            LayoutParams lp = WxPhotoSelectorActivity.this.getWindow().getAttributes();
            lp.alpha = 0.3f;
            WxPhotoSelectorActivity.this.getWindow().setAttributes(lp);
        }
    }

    /* renamed from: com.tuochebang.user.widget.wxphotoselector.WxPhotoSelectorActivity$8 */
    class C10908 implements OnClickListener {
        C10908() {
        }

        public void onClick(View v) {
            Bundle bundle = new Bundle();
            ArrayList<String> imageLoaderList = new ArrayList();
            Iterator it = PhotoGridAdapterView.mSelectedImage.iterator();
            while (it.hasNext()) {
                imageLoaderList.add("file://" + ((String) it.next()));
            }
            bundle.putSerializable(ImageSimpleBrowseActivity.EXTRA_IMAGE_URLS, imageLoaderList);
            ActivityUtil.next(WxPhotoSelectorActivity.this, ImageSimpleBrowseActivity.class, bundle);
        }
    }

    /* renamed from: com.tuochebang.user.widget.wxphotoselector.WxPhotoSelectorActivity$9 */
    class C10919 implements FilenameFilter {
        C10919() {
        }

        public boolean accept(File dir, String filename) {
            if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg")) {
                return true;
            }
            return false;
        }
    }

    private void freshViews() {
        if (this.mMostPicFile == null) {
            showNoticeMsg("亲，系统里没有一张图片");
            return;
        }
        this.mAllPhotoList = Arrays.asList(this.mMostPicFile.list());
        this.mPhotoGridAdapter = new PhotoGridAdapter(this);
        this.mPhotoGridAdapter.setDirPath(this.mMostPicFile.getAbsolutePath());
        this.mPhotoGridAdapter.setList(this.mAllPhotoList);
        this.mPhotoGridAdapter.setPhotoOnClickLister(new C10832());
        this.mGirdView.setAdapter(this.mPhotoGridAdapter);
        this.mPreViewCount.setText("预览");
        this.mPreViewCount.setEnabled(false);
    }

    private void initListDirPopupWindw() {
        this.mListImageDirPopupWindow = new ListImageDirPopupWindow(-1, (int) (((double) this.mScreenHeight) * 0.7d), this.mImageFloders, LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_dir, null));
        this.mListImageDirPopupWindow.setOnDismissListener(new C10843());
        this.mListImageDirPopupWindow.setOnImageDirSelected(this);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_photo_selector);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        this.mScreenHeight = outMetrics.heightPixels;
        initExtras();
        initActionBar();
        initView();
        getImages();
        initEvent();
    }

    private void initExtras() {
        PhotoGridAdapterView.mSelectLimitedCount = getIntent().getIntExtra(EXTRA_SELECT_LIMITED_COUNT, 4);
    }

    private void initActionBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mTxtRight = (Button) findViewById(R.id.tcb_right_txt);
        this.mTxtRight.setVisibility(View.GONE);
        this.mTxtRight.setOnClickListener(new C10854());
        this.mToolBar.setNavigationOnClickListener(new C10865());
    }

    protected void btnFinishOnClick() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_RETURN_IMAGES, PhotoGridAdapterView.mSelectedImage);
        setResult(-1, intent);
        finish();
    }

    private void getImages() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            this.mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
            new Thread(new C10886()).start();
            return;
        }
        showNoticeMsg("暂无外部存储");
    }

    private void initView() {
        this.mGirdView = (GridView) findViewById(R.id.id_gridView);
        this.mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
        this.mPreViewCount = (TextView) findViewById(R.id.id_total_count);
    }

    private void initEvent() {
        this.mChooseDir.setOnClickListener(new C10897());
        this.mPreViewCount.setOnClickListener(new C10908());
    }

    public void selected(ImageFloder floder) {
        this.mMostPicFile = new File(floder.getDir());
        this.mAllPhotoList = Arrays.asList(this.mMostPicFile.list(new C10919()));
        this.mPhotoGridAdapter.setDirPath(this.mMostPicFile.getAbsolutePath());
        this.mPhotoGridAdapter.setList(this.mAllPhotoList);
        this.mChooseDir.setText(floder.getName());
        this.mListImageDirPopupWindow.dismiss();
    }

    protected void onDestroy() {
        super.onDestroy();
        PhotoGridAdapterView.mSelectedImage.clear();
    }
}
