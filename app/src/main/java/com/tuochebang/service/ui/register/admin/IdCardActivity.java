package com.tuochebang.service.ui.register.admin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.framework.app.component.utils.ActivityUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tuochebang.service.R;
import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.base.BaseActivity;
import com.tuochebang.service.constant.AppConstant;
import com.tuochebang.service.constant.AppConstant.BroadCastAction;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.entity.AdminInfo;
import com.tuochebang.service.ui.SelectPhotoActivity;
import com.tuochebang.service.widget.wxphotoselector.WxPhotoSelectorActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class IdCardActivity extends BaseActivity {
    private final String CORRECT_SIDE_IMG = "correct_side_img";
    private String CURRENT_IMG = "";
    private final String OPPOSITE_SIDE_IMG = "opposite_side_img";
    Handler handler = new C07011();
    private AdminInfo mAdminInfo;
    private Button mBtnNext;
    private ImageView mImgCorrectSide;
    private ImageView mImgOppositeSide;
    private BroadcastReceiver mRegisterStatuChangeReceiver;
    private Toolbar mToolBar;
    private Map<String, String> uploadUrls = new HashMap();

    /* renamed from: com.tuochebang.service.ui.register.admin.IdCardActivity$1 */
    class C07011 extends Handler {
        C07011() {
        }

        public void handleMessage(Message msg) {
            String url = msg.getData().getString("url");
            if (IdCardActivity.this.CURRENT_IMG.equals("correct_side_img")) {
                ImageLoader.getInstance().displayImage(url, IdCardActivity.this.mImgCorrectSide);
            } else if (IdCardActivity.this.CURRENT_IMG.equals("opposite_side_img")) {
                ImageLoader.getInstance().displayImage(url, IdCardActivity.this.mImgOppositeSide);
            }
            IdCardActivity.this.dismissCommonProgressDialog();
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.admin.IdCardActivity$2 */
    class C07022 extends BroadcastReceiver {
        C07022() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (BroadCastAction.USER_REGISTER_SUCCESS.equals(intent.getAction())) {
                    IdCardActivity.this.finish();
                }
            }
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.admin.IdCardActivity$3 */
    class C07033 implements OnClickListener {
        C07033() {
        }

        public void onClick(View v) {
            IdCardActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.admin.IdCardActivity$4 */
    class C07044 implements OnClickListener {
        C07044() {
        }

        public void onClick(View view) {
            IdCardActivity.this.CURRENT_IMG = "correct_side_img";
            IdCardActivity.this.selectPhoto();
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.admin.IdCardActivity$5 */
    class C07055 implements OnClickListener {
        C07055() {
        }

        public void onClick(View view) {
            IdCardActivity.this.CURRENT_IMG = "opposite_side_img";
            IdCardActivity.this.selectPhoto();
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.admin.IdCardActivity$6 */
    class C07066 implements OnClickListener {
        C07066() {
        }

        public void onClick(View view) {
            if (IdCardActivity.this.uploadUrls.size() >= 2) {
                Bundle bundle = new Bundle();
                IdCardActivity.this.mAdminInfo.setPicture0((String) IdCardActivity.this.uploadUrls.get("correct_side_img"));
                IdCardActivity.this.mAdminInfo.setPicture1((String) IdCardActivity.this.uploadUrls.get("opposite_side_img"));
                bundle.putSerializable("adminInfo", IdCardActivity.this.mAdminInfo);
                ActivityUtil.next(IdCardActivity.this, CompanyActivity.class, bundle);
                return;
            }
            IdCardActivity.this.showToast("请上传您的证件照");
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.admin.IdCardActivity$7 */
    class C07077 implements OSSProgressCallback<PutObjectRequest> {
        C07077() {
        }

        public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_card);
        getIntentExtras();
        initToolBar();
        initView();
        registUserStatuChangeBroadCastReciver();
        initListener();
    }

    private void registUserStatuChangeBroadCastReciver() {
        this.mRegisterStatuChangeReceiver = new C07022();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mRegisterStatuChangeReceiver, new IntentFilter(BroadCastAction.USER_REGISTER_SUCCESS));
    }

    private void getIntentExtras() {
        this.mAdminInfo = (AdminInfo) getIntent().getSerializableExtra("adminInfo");
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C07033());
    }

    private void initView() {
        this.mImgCorrectSide = (ImageView) findViewById(R.id.img_correct_side);
        this.mImgOppositeSide = (ImageView) findViewById(R.id.img_opposite_side);
        this.mBtnNext = (Button) findViewById(R.id.next);
    }

    private void initListener() {
        this.mImgCorrectSide.setOnClickListener(new C07044());
        this.mImgOppositeSide.setOnClickListener(new C07055());
        this.mBtnNext.setOnClickListener(new C07066());
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
            ArrayList<String> images = data.getStringArrayListExtra(WxPhotoSelectorActivity.EXTRA_RETURN_IMAGES);
            if (images != null && images.size() > 0) {
                showCommonProgreessDialog("请稍后...");
                Iterator it = images.iterator();
                while (it.hasNext()) {
                    uploadImage((String) it.next());
                }
            }
        }
    }

    private void uploadImage(final String filePath) {
        PutObjectRequest put = new PutObjectRequest(AppConstant.ALIYUN_OSS_BUCKET, AppConstant.ALIYUN_OSS_KEY + filePath, filePath);
        put.setProgressCallback(new C07077());
        OSSAsyncTask task = MyApplication.getInstance().getOssClient().asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                IdCardActivity.this.uploadUrls.put(IdCardActivity.this.CURRENT_IMG, ServerUrl.URL_UPLOAD + "/" + AppConstant.ALIYUN_OSS_KEY + filePath);
                Message msg = IdCardActivity.this.handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("url", ServerUrl.URL_UPLOAD + "/" + AppConstant.ALIYUN_OSS_KEY + filePath);
                msg.setData(bundle);
                IdCardActivity.this.handler.sendMessage(msg);
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
        if (this.mRegisterStatuChangeReceiver != null) {
            LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this.mRegisterStatuChangeReceiver);
        }
    }
}
