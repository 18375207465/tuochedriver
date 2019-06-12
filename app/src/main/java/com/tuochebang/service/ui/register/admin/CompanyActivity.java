package com.tuochebang.service.ui.register.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.framework.app.component.utils.ActivityUtil;
import com.framework.app.component.utils.BroadCastUtil;
import com.framework.app.component.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tuochebang.service.R;
import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.base.BaseActivity;
import com.tuochebang.service.constant.AppConstant;
import com.tuochebang.service.constant.AppConstant.BroadCastAction;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.entity.AdminInfo;
import com.tuochebang.service.request.entity.LoginInfo;
import com.tuochebang.service.request.task.RegisterAdminRequest;
import com.tuochebang.service.ui.CommonEditActivity;
import com.tuochebang.service.ui.LocationMapActivity;
import com.tuochebang.service.ui.SelectPhotoActivity;
import com.tuochebang.service.ui.register.RegisterSuccessActivity;
import com.tuochebang.service.util.NAImageUtils;
import com.tuochebang.service.widget.wxphotoselector.WxPhotoSelectorActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.Iterator;

public class CompanyActivity extends BaseActivity {
    Handler handler = new C06921();
    private AdminInfo mAdminInfo;
    private ImageView mImgZhizhao;
    private RelativeLayout mRlAddress;
    private RelativeLayout mRlAdminName;
    private RelativeLayout mRlCompanyName;
    private Toolbar mToolBar;
    private TextView mTxtAddress;
    private TextView mTxtAdminName;
    private TextView mTxtCompanyName;

    /* renamed from: com.tuochebang.service.ui.register.admin.CompanyActivity$1 */
    class C06921 extends Handler {
        C06921() {
        }

        public void handleMessage(Message msg) {
            ImageLoader.getInstance().displayImage(msg.getData().getString("url"), CompanyActivity.this.mImgZhizhao);
            dismissCommonProgressDialog();
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.admin.CompanyActivity$2 */
    class C06932 implements OnClickListener {
        C06932() {
        }

        public void onClick(View v) {
            CompanyActivity.this.onBtnCompanyName();
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.admin.CompanyActivity$3 */
    class C06943 implements OnClickListener {
        C06943() {
        }

        public void onClick(View v) {
            CompanyActivity.this.onBtnAdminName();
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.admin.CompanyActivity$4 */
    class C06954 implements OnClickListener {
        C06954() {
        }

        public void onClick(View v) {
            CompanyActivity.this.onBtnSelectLocation();
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.admin.CompanyActivity$5 */
    class C06965 implements OnClickListener {
        C06965() {
        }

        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(SelectPhotoActivity.EXTRA_DIALOG_TITLE, "上传照片");
            bundle.putInt(SelectPhotoActivity.EXTRA_PHOTO_SELECT_TYPE, 5);
            bundle.putInt(SelectPhotoActivity.EXTRA_PHOTO_NUMS, 1);
            ActivityUtil.next((Activity) CompanyActivity.this.mContext, SelectPhotoActivity.class, bundle, false, 100);
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.admin.CompanyActivity$6 */
    class C06976 implements OnClickListener {
        C06976() {
        }

        public void onClick(View v) {
            CompanyActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.admin.CompanyActivity$7 */
    class C06987 implements OnClickListener {
        C06987() {
        }

        public void onClick(View v) {
            CompanyActivity.this.onBtnSubmit();
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.admin.CompanyActivity$8 */
    class C06998 implements OnResponseListener<LoginInfo> {
        C06998() {
        }

        public void onSucceed(int what, Response<LoginInfo> response) {
            if (response.get() == null) return;
            ToastUtil.showMessage(MyApplication.getInstance(), "注册成功");
            BroadCastUtil.sendBroadCast(CompanyActivity.this.mContext, BroadCastAction.USER_REGISTER_SUCCESS);
            ActivityUtil.next(CompanyActivity.this, RegisterSuccessActivity.class);
            CompanyActivity.this.finish();
        }

        public void onFailed(int what, Response<LoginInfo> response) {
            Exception exception = response.getException();
        }

        public void onStart(int what) {
            showCommonProgreessDialog("請稍後...");
        }

        public void onFinish(int what) {
            dismissCommonProgressDialog();
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.admin.CompanyActivity$9 */
    class C07009 implements OSSProgressCallback<PutObjectRequest> {
        C07009() {
        }

        public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);
        getIntentExtras();
        initToolBar();
        initView();
        initListener();
    }

    private void getIntentExtras() {
        this.mAdminInfo = (AdminInfo) getIntent().getSerializableExtra("adminInfo");
    }

    private void initListener() {
        this.mRlCompanyName.setOnClickListener(new C06932());
        this.mRlAdminName.setOnClickListener(new C06943());
        this.mRlAddress.setOnClickListener(new C06954());
        this.mImgZhizhao.setOnClickListener(new C06965());
    }

    private void onBtnAdminName() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonEditActivity.EXTRAS_INPUT_TYPE, "text");
        bundle.putString(CommonEditActivity.EXTRAS_TITLE, "法人");
        bundle.putString(CommonEditActivity.EXTRAS_TYPE, "adminName");
        Intent intent = new Intent();
        intent.setClass(this, CommonEditActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 100);
    }

    private void onBtnCompanyName() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonEditActivity.EXTRAS_INPUT_TYPE, "text");
        bundle.putString(CommonEditActivity.EXTRAS_TITLE, "公司名称");
        bundle.putString(CommonEditActivity.EXTRAS_TYPE, "companyName");
        Intent intent = new Intent();
        intent.setClass(this, CommonEditActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 100);
    }

    private void onBtnSelectLocation() {
        Bundle bundle = new Bundle();
        Intent intent = new Intent();
        intent.setClass(this, LocationMapActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 100);
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C06976());
    }

    private void initView() {
        this.mImgZhizhao = (ImageView) findViewById(R.id.img_zhizhao);
        this.mRlCompanyName = (RelativeLayout) findViewById(R.id.rl_company_name);
        this.mRlAddress = (RelativeLayout) findViewById(R.id.txt_position_rl);
        this.mTxtCompanyName = (TextView) findViewById(R.id.txt_company_name);
        this.mTxtAddress = (TextView) findViewById(R.id.tcb_position_txt);
        this.mTxtAdminName = (TextView) findViewById(R.id.txt_admin_name);
        this.mRlAdminName = (RelativeLayout) findViewById(R.id.rl_admin_name);
        findViewById(R.id.submit).setOnClickListener(new C06987());
    }

    private void onBtnSubmit() {
        if (TextUtils.isEmpty(this.mAdminInfo.getCorporate()) || TextUtils.isEmpty(this.mAdminInfo.getBusinessLicense()) || TextUtils.isEmpty(this.mAdminInfo.getName()) || TextUtils.isEmpty(this.mAdminInfo.getAddress())) {
            ToastUtil.showMessage(MyApplication.getInstance(), "请填写完整后提交");
        } else {
            httpSubmit();
        }
    }

    private void httpSubmit() {
        RequestQueue queue = NoHttp.newRequestQueue();
        queue.add(0, new RegisterAdminRequest(ServerUrl.getInst().ADMIN_REGISTER_URL(), RequestMethod.POST, this.mAdminInfo), new C06998());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String value;
        if (resultCode == 4) {
            value = data.getStringExtra(LocationMapActivity.EXTRAS_ADDRESS);
            this.mTxtAddress.setText(value);
            this.mAdminInfo.setAddress(value);
        } else if (resultCode == -1) {
            if (requestCode == 100) {
                ArrayList<String> images = data.getStringArrayListExtra(WxPhotoSelectorActivity.EXTRA_RETURN_IMAGES);
                if (images != null && images.size() > 0) {
                    showCommonProgreessDialog("请稍后...");
                    Iterator it = images.iterator();
                    while (it.hasNext()) {
                        uploadImage((String) it.next());
                    }
                }
            }
        } else if (resultCode == 3 && requestCode == 100) {
            value = data.getStringExtra(CommonEditActivity.EXTRAS_VALUE);
            String type = data.getStringExtra(CommonEditActivity.EXTRAS_TYPE);
            if (type.equals("companyName")) {
                this.mTxtCompanyName.setText(value);
                this.mAdminInfo.setCorporate(value);
            } else if (type.equals("adminName")) {
                this.mTxtAdminName.setText(value);
                this.mAdminInfo.setName(value);
            }
        }
    }

    private void uploadImage(final String filePath) {
        final String path = NAImageUtils.compressAndRotateImage(MyApplication.getInstance(), filePath);
        PutObjectRequest put = new PutObjectRequest(AppConstant.ALIYUN_OSS_BUCKET, AppConstant.ALIYUN_OSS_KEY + path, path);
        put.setProgressCallback(new C07009());
        OSSAsyncTask task = MyApplication.getInstance().getOssClient().asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                String url =
                        MyApplication.getInstance().getOssClient()
                                .presignPublicObjectURL(AppConstant.ALIYUN_OSS_BUCKET, request.getObjectKey());
                CompanyActivity.this.mAdminInfo.setBusinessLicense(url);
                Message msg = CompanyActivity.this.handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                msg.setData(bundle);
                handler.sendMessage(msg);
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
}
