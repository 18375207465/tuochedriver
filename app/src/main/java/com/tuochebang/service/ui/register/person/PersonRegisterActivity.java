package com.tuochebang.service.ui.register.person;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.jiguang.net.HttpUtils;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.framework.app.component.utils.ActivityUtil;
import com.framework.app.component.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tuochebang.service.R;
import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.base.BaseActivity;
import com.tuochebang.service.cache.FileUtil;
import com.tuochebang.service.constant.AppConstant;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.task.PersonRegisterRequest;
import com.tuochebang.service.ui.SelectPhotoActivity;
import com.tuochebang.service.ui.WebActivity;
import com.tuochebang.service.util.NAImageUtils;
import com.tuochebang.service.widget.wxphotoselector.WxPhotoSelectorActivity;
import com.yalantis.ucrop.util.FileUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PersonRegisterActivity extends BaseActivity {
    private final String CORRECT_SIDE_IMG = "correct_side_img";
    private final String CROSS_SIDE_IMG = "cross_side_img";
    private String CURRENT_IMG = "";
    private final String OPPOSITE_SIDE_IMG = "opposite_side_img";
    Handler handler = new C15171();
    private TextView mBtnNext;
    private EditText mEdAccount;
    private EditText mEdName;
    private EditText mEdPwd;
    private ImageView mImgCorrectSide;
    private ImageView mImgCross;
    private ImageView mImgOppositeSide;
    private Toolbar mToolBar;
    private Map<String, String> uploadUrls = new HashMap();

    /* renamed from: com.tuochebang.service.ui.register.person.PersonRegisterActivity$1 */
    class C15171 extends Handler {
        C15171() {
        }

        public void handleMessage(Message msg) {
            String url = msg.getData().getString(WebActivity.FLAG_URL);
            Log.e("handleMessage", url);
            if (CURRENT_IMG.equals(CORRECT_SIDE_IMG)) {
                ImageLoader.getInstance().displayImage(url, mImgCorrectSide);
            } else if (CURRENT_IMG.equals(OPPOSITE_SIDE_IMG)) {
                ImageLoader.getInstance().displayImage(url, mImgOppositeSide);
            } else if (CURRENT_IMG.equalsIgnoreCase(CROSS_SIDE_IMG)) {
                ImageLoader.getInstance().displayImage(url, mImgCross);
            }
            PersonRegisterActivity.this.dismissCommonProgressDialog();
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.person.PersonRegisterActivity$2 */
    class C15182 implements OnClickListener {
        C15182() {
        }

        public void onClick(View v) {
            PersonRegisterActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.person.PersonRegisterActivity$3 */
    class C15193 implements OnClickListener {
        C15193() {
        }

        public void onClick(View view) {
            PersonRegisterActivity.this.CURRENT_IMG = "correct_side_img";
            PersonRegisterActivity.this.selectPhoto();
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.person.PersonRegisterActivity$4 */
    class C15204 implements OnClickListener {
        C15204() {
        }

        public void onClick(View view) {
            PersonRegisterActivity.this.CURRENT_IMG = "opposite_side_img";
            PersonRegisterActivity.this.selectPhoto();
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.person.PersonRegisterActivity$5 */
    class C15215 implements OnClickListener {
        C15215() {
        }

        public void onClick(View v) {
            CURRENT_IMG = CROSS_SIDE_IMG;
            selectPhoto();
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.person.PersonRegisterActivity$6 */
    class C15226 implements OnClickListener {
        C15226() {
        }

        public void onClick(View view) {
            if (TextUtils.isEmpty(PersonRegisterActivity.this.mEdAccount.getText().toString()) || TextUtils.isEmpty(PersonRegisterActivity.this.mEdName.getText().toString()) || TextUtils.isEmpty(PersonRegisterActivity.this.mEdPwd.getText().toString())) {
                PersonRegisterActivity.this.showToast("请将信息填写完整后再提交");
            } else if (PersonRegisterActivity.this.uploadUrls.size() >= 2) {
                PersonRegisterActivity.this.httpRegisterRequest(PersonRegisterActivity.this.mEdAccount.getText().toString(), PersonRegisterActivity.this.mEdPwd.getText().toString(), PersonRegisterActivity.this.mEdName.getText().toString(), (String) PersonRegisterActivity.this.uploadUrls.get("correct_side_img"), (String) PersonRegisterActivity.this.uploadUrls.get("opposite_side_img"), (String) PersonRegisterActivity.this.uploadUrls.get("cross_side_img"));
            } else {
                PersonRegisterActivity.this.showToast("请将信息填写完整后再提交");
            }
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.person.PersonRegisterActivity$7 */
    class C15237 implements OnResponseListener<Object> {
        C15237() {
        }

        public void onSucceed(int what, Response<Object> response) {
            if (response.get() == null) return;
            ToastUtil.showMessage(MyApplication.getInstance(), "注册成功");
            PersonRegisterActivity.this.finish();
        }

        public void onFailed(int what, Response<Object> response) {
            Exception exception = response.getException();
        }

        public void onStart(int what) {
            PersonRegisterActivity.this.showCommonProgreessDialog("請稍後...");
        }

        public void onFinish(int what) {
            PersonRegisterActivity.this.dismissCommonProgressDialog();
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.person.PersonRegisterActivity$8 */
    class C15248 implements Observer<Boolean> {
        C15248() {
        }

        public void onSubscribe(Disposable d) {
        }

        public void onNext(Boolean aBoolean) {
            if (aBoolean.booleanValue()) {
                Bundle bundle = new Bundle();
                bundle.putString(SelectPhotoActivity.EXTRA_DIALOG_TITLE, "上传照片");
                bundle.putInt(SelectPhotoActivity.EXTRA_PHOTO_SELECT_TYPE, 5);
                bundle.putInt(SelectPhotoActivity.EXTRA_PHOTO_NUMS, 1);
                ActivityUtil.next((Activity) PersonRegisterActivity.this.mContext, SelectPhotoActivity.class, bundle, false, 100);
                return;
            }
            ToastUtil.showMessage(MyApplication.getInstance(), PersonRegisterActivity.this.getString(R.string.picture_jurisdiction));
        }

        public void onError(Throwable e) {
        }

        public void onComplete() {
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.person.PersonRegisterActivity$9 */
    class C15259 implements OSSProgressCallback<PutObjectRequest> {
        C15259() {
        }

        public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_register);
        getIntentExtras();
        initToolBar();
        initView();
        initListener();
    }

    private void getIntentExtras() {
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C15182());
    }

    private void initView() {
        this.mImgCorrectSide = (ImageView) findViewById(R.id.img_car_photo);
        this.mImgOppositeSide = (ImageView) findViewById(R.id.img_car_insurance);
        this.mImgCross = (ImageView) findViewById(R.id.img_car_cross);
        this.mBtnNext = (TextView) findViewById(R.id.tcb_save_txt);
        this.mEdPwd = (EditText) findViewById(R.id.tcb_pwd_ed);
        this.mEdAccount = (EditText) findViewById(R.id.tcb_account_txt);
        this.mEdName = (EditText) findViewById(R.id.tcb_name_ed);
    }

    private void initListener() {
        this.mImgCorrectSide.setOnClickListener(new C15193());
        this.mImgOppositeSide.setOnClickListener(new C15204());
        this.mImgCross.setOnClickListener(new C15215());
        this.mBtnNext.setOnClickListener(new C15226());
    }

    private void httpRegisterRequest(String account, String pwd, String name, String carPhoto, String carInsuince, String crossUrl) {
        RequestQueue queue = NoHttp.newRequestQueue();
        queue.add(0, new PersonRegisterRequest(ServerUrl.getInst().PERSON_REGISTER_URL(), RequestMethod.POST, account, pwd, name, carPhoto, carInsuince, crossUrl), new C15237());
    }

    private void selectPhoto() {
        Bundle bundle = new Bundle();
        bundle.putString(SelectPhotoActivity.EXTRA_DIALOG_TITLE, "上传照片");
        bundle.putInt(SelectPhotoActivity.EXTRA_PHOTO_SELECT_TYPE, 5);
        bundle.putInt(SelectPhotoActivity.EXTRA_PHOTO_NUMS, 1);
        ActivityUtil.next((Activity) this.mContext, SelectPhotoActivity.class, bundle, false, 100);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (data == null) return;
            ArrayList<String> images = data.getStringArrayListExtra(WxPhotoSelectorActivity.EXTRA_RETURN_IMAGES);
            if (images != null && images.size() > 0) {
                showCommonProgreessDialog("请稍后...");
                Iterator it = images.iterator();
                while (it.hasNext()) {
                    uploadImage((String) it.next());
                    Log.i("Flog","选择照片返回路径="+images);
                }
            }
        }
    }

    private void uploadImage(String filePath) {
        final String path = NAImageUtils.compressAndRotateImage(MyApplication.getInstance(), filePath);
        PutObjectRequest put = new PutObjectRequest(AppConstant.ALIYUN_OSS_BUCKET, AppConstant.ALIYUN_OSS_KEY + System.currentTimeMillis(), path);
        put.setProgressCallback(new C15259());
        OSSAsyncTask task = MyApplication.getInstance().getOssClient().asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                String url =
                        MyApplication.getInstance().getOssClient()
                                .presignPublicObjectURL(AppConstant.ALIYUN_OSS_BUCKET, request.getObjectKey());
                uploadUrls.put(CURRENT_IMG, url);
                Message msg = PersonRegisterActivity.this.handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                msg.setData(bundle);
                PersonRegisterActivity.this.handler.sendMessage(msg);
            }

            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                if (clientExcepion != null) {
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                }
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    
}
