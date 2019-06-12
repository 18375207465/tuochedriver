package com.tuochebang.service.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.framework.app.component.utils.ActivityUtil;
import com.tuochebang.service.PermissionHelper;
import com.tuochebang.service.R;
import com.tuochebang.service.adapter.SelectedDataAdapter;
import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.base.BaseActivity;
import com.tuochebang.service.cache.FileUtil;
import com.tuochebang.service.util.ImageUtil;
import com.tuochebang.service.util.PictureUtil;
import com.tuochebang.service.util.StorageUtils;
import com.tuochebang.service.widget.wxphotoselector.WxPhotoSelectorActivity;
import com.yalantis.ucrop.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectPhotoActivity extends BaseActivity {
    private static final int DEFALUT_ASPECT_X = 1;
    private static final int DEFALUT_ASPECT_Y = 1;
    private static final int DEFALUT_CROP_HEIGHT = 270;
    private static final int DEFALUT_CROP_WIDTH = 270;
    private static final int DEFALUT_PHOTO_NUMS = 4;
    private static final int DEFALUT_SELECT_TYPE = 0;
    public static final String EXTRA_DIALOG_TITLE = "dialog_title";
    public static final String EXTRA_PHOTO_ASPECT_X = "photo_aspect_x";
    public static final String EXTRA_PHOTO_ASPECT_Y = "photo_aspect_y";
    public static final String EXTRA_PHOTO_CROP_HEIGHT = "photo_crop_height";
    public static final String EXTRA_PHOTO_CROP_WIDTH = "photo_crop_width";
    public static final String EXTRA_PHOTO_NUMS = "photo_nums";
    public static final String EXTRA_PHOTO_SELECT_TYPE = "photo_select_type";
    public static final String FLAG_IMAGE_PATH = "flag_image_path";
    private static final String IMAGE_DEFAULT_NAME = "default.png";
    private static final int REQ_RESULT_PHOTO_CAPTURE = 500;
    private static final int REQ_RESULT_PHOTO_CROP = 700;
    private static final int REQ_RESULT_WXPHOTO = 100;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 20;
    private String IMAGE_DEFAULE_PATH = "/base/image/";
    private ArrayList<String> imageList;
    private Uri mCameraImageUri;
    private String mDialogTitle;
    private ListView mOptionsListView;
    private int mPhotoAspectX;
    private int mPhotoAspectY;
    private int mPhotoNums;
    private int mPhotoOutputX;
    private int mPhotoOutputY;
    private int mPhotoSelectType;
    private int mPosition;
    private TextView mTvTitle;

    private String mSaveDir;

    private String mPhotoSaveDir;

    private String mEditSaveDir;

    private final String DEFAULT_SAVE_DIR =
            StorageUtils.isSDCardExist() ? Environment.getExternalStorageDirectory()
                    + File.separator
                    + Environment.DIRECTORY_DCIM
                    + File.separator
                    + Directory.TUOCHEBANG_DIR_NAME
                    + File.separator : Environment.getDownloadCacheDirectory().getAbsolutePath();

    /* renamed from: com.tuochebang.service.ui.SelectPhotoActivity$1 */
    class C06701 implements OnItemClickListener {
        C06701() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, final int position, long arg3) {
            PermissionHelper.getInstance().buildRequest(SelectPhotoActivity.this)
                    .addRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .addRequestPermission(Manifest.permission.CAMERA)
                    .addRequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .setDeniedAlertType(PermissionHelper.DeniedAlertType.Toast)
                    .setAction(new PermissionHelper.PermissionsResultAction() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onGranted() {
                            SelectPhotoActivity.this.mPosition = position;
                            if (mPosition == 0) {
                                openCamera();
                            } else if (mPosition == 1) {
                                SelectPhotoActivity.this.openMediaStore();
                            } else {
                                SelectPhotoActivity.this.openWxPhotoSelect();
                            }
                        }

                        @Override
                        public void onDenied(String permission) {
                            super.onDenied(permission);
                        }
                    })
                    .requst();


        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);
        getExtras();
        initViews();
    }

    private void getExtras() {
        this.mDialogTitle = getIntent().getStringExtra(EXTRA_DIALOG_TITLE);
        this.mPhotoOutputX = getIntent().getIntExtra(EXTRA_PHOTO_CROP_WIDTH, 270);
        this.mPhotoOutputY = getIntent().getIntExtra(EXTRA_PHOTO_CROP_HEIGHT, 270);
        this.mPhotoAspectX = getIntent().getIntExtra(EXTRA_PHOTO_ASPECT_X, 1);
        this.mPhotoAspectY = getIntent().getIntExtra(EXTRA_PHOTO_ASPECT_Y, 1);
        this.mPhotoSelectType = getIntent().getIntExtra(EXTRA_PHOTO_SELECT_TYPE, 0);
        this.mPhotoNums = getIntent().getIntExtra(EXTRA_PHOTO_NUMS, 4);
    }

    private void initViews() {
        this.imageList = new ArrayList();
        this.mTvTitle = (TextView) findViewById(R.id.tv_message);
        if (this.mDialogTitle != null) {
            this.mTvTitle.setText(this.mDialogTitle);
        }
        this.mOptionsListView = (ListView) findViewById(R.id.lv_options);
        this.mOptionsListView.setOnItemClickListener(new C06701());
        SelectedDataAdapter selectedDataAdapter = new SelectedDataAdapter(SelectPhotoActivity.this);
        List<String> stringList = new ArrayList();
        stringList.add(getString(R.string.txt_take_info));
        stringList.add(getString(R.string.txt_select_from_album));
        selectedDataAdapter.setList(stringList);
        mOptionsListView.setAdapter(selectedDataAdapter);
    }

    public void onBgClick(View v) {
        finish();
    }

    private void openCamera() {
        try {
            if (Environment.getExternalStorageState().equals("mounted")) {
                try {

                    mCameraImageUri = FileProvider.getUriForFile(
                            this,
                            getPackageName() + ".provider",
                            FileUtil.createCacheFile(this.IMAGE_DEFAULE_PATH, System.currentTimeMillis() + IMAGE_DEFAULT_NAME));
                    //mCameraImageUri = Uri.fromFile(FileUtil.createCacheFile(this.IMAGE_DEFAULE_PATH, System.currentTimeMillis() + IMAGE_DEFAULT_NAME));
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraImageUri);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, REQ_RESULT_PHOTO_CAPTURE);
                } catch (IOException e) {
                    showNoticeMsg("不能创建临时图片文件");
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            showNoticeMsg("没有合适的相机应用程序");
        }
    }

    public String getPhotoSaveDir() {
        if (TextUtils.isEmpty(mPhotoSaveDir)) {
            mPhotoSaveDir = getSaveDir() + Directory.PHOTO_DIR_NAME + File.separator;
        }
        return mPhotoSaveDir;
    }

    public String getSaveDir() {
        if (TextUtils.isEmpty(mSaveDir)) {
            mSaveDir = DEFAULT_SAVE_DIR;
        }
        return mSaveDir;
    }

    private interface Directory {

        //保存用拍摄图片的目录名
        String PHOTO_DIR_NAME = "Tuochebang_driver";

        //保存被编辑图片的目录名
        String EDIT_DIR_NAME = "Photo_Edit";

        //用保存的所有图片的根目录名
        String TUOCHEBANG_DIR_NAME = "Tuochebang";
    }

    private void openMediaStore() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            try {
                this.mCameraImageUri = Uri.fromFile(FileUtil.createSDFile(this.IMAGE_DEFAULE_PATH, System.currentTimeMillis() + IMAGE_DEFAULT_NAME));
                Intent intent = new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI);
                intent.putExtra("noFaceDetection", false);
                setupCropIntent(intent);
                startActivityForResult(intent, REQ_RESULT_PHOTO_CROP);
            } catch (IOException e) {
                showNoticeMsg("不能创建临时图片文件");
                e.printStackTrace();
            }
        }
    }

    private void openWxPhotoSelect() {
        Bundle bundle = new Bundle();
        bundle.putInt(WxPhotoSelectorActivity.EXTRA_SELECT_LIMITED_COUNT, this.mPhotoNums);
        ActivityUtil.next(this, WxPhotoSelectorActivity.class, bundle, false, 100);
    }

    private void setupCropIntent(Intent intent) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        if (android.os.Build.MANUFACTURER.contains("HUAWEI")) {
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999);
        } else {
            intent.putExtra("aspectX", this.mPhotoAspectX);
            intent.putExtra("aspectY", this.mPhotoAspectY);
        }
        intent.putExtra("crop", "false");
        intent.putExtra("outputX", this.mPhotoOutputX);
        intent.putExtra("outputY", this.mPhotoOutputY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra("output", this.mCameraImageUri);
        intent.putExtra("outputFormat", CompressFormat.JPEG.toString());
    }

    private void cropImageUriByTakePhoto() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(mCameraImageUri, "image/*");
        setupCropIntent(intent);
        startActivityForResult(intent, REQ_RESULT_PHOTO_CROP);
    }

    public static Bitmap toturn(Bitmap img) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90.0f);
        return Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case 100:
                    this.imageList = data.getStringArrayListExtra(WxPhotoSelectorActivity.EXTRA_RETURN_IMAGES);
                    Intent it = new Intent();
                    it.putStringArrayListExtra(WxPhotoSelectorActivity.EXTRA_RETURN_IMAGES, this.imageList);
                    setResult(-1, it);
                    finish();
                    return;
                case REQ_RESULT_PHOTO_CAPTURE /*500*/:
                    cropImageUriByTakePhoto();
                    return;
                case REQ_RESULT_PHOTO_CROP /*700*/:
                    if (this.mCameraImageUri != null) {
                        String path = mCameraImageUri.getPath();
                        path = FileUtil.getFileFromUri(mCameraImageUri, MyApplication.getInstance());
                        //String path = this.mCameraImageUri.getPath();
                        Intent intent = new Intent();
                        if (this.mPhotoSelectType != 0) {
                            this.imageList.add(path);
                            intent.putStringArrayListExtra(WxPhotoSelectorActivity.EXTRA_RETURN_IMAGES, this.imageList);
                        } else {
                            checkPhoto();
                            intent.putExtra(FLAG_IMAGE_PATH, this.mCameraImageUri.getPath());
                        }
                        setResult(-1, intent);
                    } else {
                        showNoticeMsg("不能获取到图片");
                    }
                    finish();
                    return;
                default:
                    return;
            }
        }
        finish();
    }

    private void checkPhoto() {
        int degree = PictureUtil.readPictureDegree(this.mCameraImageUri.getPath());
        if (degree != 0) {
            try {
                ImageUtil.saveBitmap(this.mCameraImageUri.getPath(), PictureUtil.rotateBitmap(BitmapFactory.decodeFile(this.mCameraImageUri.getPath()), degree));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private interface Key {

        String UUID = "UUID";

        String SAVE_DIR = "KEY_SAVE_DIR";

        String USER_QUESTION = "USER_QUESTION";

        String IS_FIRST_UPLOAD = "is_first_upload";

        String UPDATE = "update";
    }

}
