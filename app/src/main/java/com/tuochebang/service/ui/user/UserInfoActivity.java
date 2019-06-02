package com.tuochebang.service.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.framework.app.component.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tuochebang.service.R;
import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.base.BaseActivity;
import com.tuochebang.service.cache.FileUtil;
import com.tuochebang.service.constant.AppConstant;
import com.tuochebang.service.constant.AppConstant.BroadCastAction;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.entity.LoginInfo;
import com.tuochebang.service.request.entity.UserInfo;
import com.tuochebang.service.request.task.EditUserRequest;
import com.tuochebang.service.ui.CommonEditActivity;
import com.tuochebang.service.ui.SelectPhotoActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import de.hdodenhof.circleimageview.CircleImageView;

import java.io.File;
import java.util.List;

public class UserInfoActivity extends BaseActivity {
    private List<String> mAccountList;
    private String mHeaderUrl;
    private CircleImageView mImgHeader;
    private RelativeLayout mRlAccount;
    private RelativeLayout mRlCompanyName;
    private RelativeLayout mRlHeader;
    private RelativeLayout mRlNickName;
    private RelativeLayout mRlPhone;
    private Toolbar mToolBar;
    private TextView mTxtAccount;
    private TextView mTxtCompanyFlag;
    private TextView mTxtCompanyName;
    private TextView mTxtNickName;
    private TextView mTxtPhone;
    private TextView mTxtSave;
    private UserInfo mUserInfo;

    /* renamed from: com.tuochebang.service.ui.user.UserInfoActivity$1 */
    class C07961 implements OnClickListener {
        C07961() {
        }

        public void onClick(View v) {
            UserInfoActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.service.ui.user.UserInfoActivity$2 */
    class C07972 implements OnClickListener {
        C07972() {
        }

        public void onClick(View v) {
            ActivityUtil.next(UserInfoActivity.this, SelectPhotoActivity.class, 100);
        }
    }

    /* renamed from: com.tuochebang.service.ui.user.UserInfoActivity$3 */
    class C07983 implements OnClickListener {
        C07983() {
        }

        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(CommonEditActivity.EXTRAS_INPUT_TYPE, "text");
            bundle.putString(CommonEditActivity.EXTRAS_TITLE, "昵称");
            bundle.putString(CommonEditActivity.EXTRAS_TYPE, "nickName");
            Intent intent = new Intent();
            intent.setClass(UserInfoActivity.this, CommonEditActivity.class);
            intent.putExtras(bundle);
            UserInfoActivity.this.startActivityForResult(intent, 100);
        }
    }

    /* renamed from: com.tuochebang.service.ui.user.UserInfoActivity$4 */
    class C07994 implements OnClickListener {
        C07994() {
        }

        public void onClick(View v) {
            UserInfoActivity.this.httpUpdateUserInfo();
        }
    }

    /* renamed from: com.tuochebang.service.ui.user.UserInfoActivity$5 */
    class C08005 implements OSSProgressCallback<PutObjectRequest> {
        C08005() {
        }

        public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
        }
    }

    /* renamed from: com.tuochebang.service.ui.user.UserInfoActivity$7 */
    class C08027 implements OnResponseListener<LoginInfo> {
        C08027() {
        }

        public void onSucceed(int what, Response<LoginInfo> response) {
            LoginInfo loginInfo = (LoginInfo) response.get();
            if (loginInfo != null) {
                Log.e(UserInfoActivity.this.TAG, loginInfo.toString());
            }
            MyApplication.getInstance().setUserInfo(loginInfo.getUser());
            BroadCastUtil.sendBroadCast(UserInfoActivity.this.mContext, BroadCastAction.USER_INFO_CHANGE);
            UserInfoActivity.this.finish();
        }

        public void onFailed(int what, Response<LoginInfo> response) {
            Exception exception = response.getException();
        }

        public void onStart(int what) {
            UserInfoActivity.this.showCommonProgreessDialog("保存中..");
        }

        public void onFinish(int what) {
            UserInfoActivity.this.dismissCommonProgressDialog();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initToolBar();
        initView();
        initListener();
        initData();
    }

    private void initData() {
        this.mUserInfo = (UserInfo) FileUtil.readFile(this.mContext, AppConstant.FLAG_USER_INFO);
        if (this.mUserInfo != null) {
            if (!TextUtils.isEmpty(this.mUserInfo.getNickName())) {
                this.mTxtNickName.setText(this.mUserInfo.getNickName());
            }
            if (TextUtils.isEmpty(this.mUserInfo.getCorporate())) {
                this.mTxtCompanyFlag.setVisibility(View.VISIBLE);
            } else {
                this.mTxtCompanyName.setText(this.mUserInfo.getCorporate());
            }
            if (!TextUtils.isEmpty(this.mUserInfo.getPicture())) {
                ImageLoader.getInstance().displayImage(this.mUserInfo.getPicture(), this.mImgHeader);
            }
        }
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C07961());
    }

    private void initListener() {
        this.mRlHeader.setOnClickListener(new C07972());
        this.mRlNickName.setOnClickListener(new C07983());
        this.mTxtSave.setOnClickListener(new C07994());
    }

    private void initView() {
        this.mImgHeader = (CircleImageView) findViewById(R.id.tcb_user_header_img);
        this.mRlHeader = (RelativeLayout) findViewById(R.id.tcb_user_avater_rl);
        this.mRlNickName = (RelativeLayout) findViewById(R.id.tcb_user_nickname_rl);
        this.mRlCompanyName = (RelativeLayout) findViewById(R.id.tcb_user_commpany_rl);
        this.mTxtNickName = (TextView) findViewById(R.id.tcb_user_nickname_txt);
        this.mTxtPhone = (TextView) findViewById(R.id.tcb_user_phone_ed);
        this.mTxtCompanyName = (TextView) findViewById(R.id.tcb_user_commpany_name_txt);
        this.mTxtCompanyFlag = (TextView) findViewById(R.id.tcb_user_company_flag_txt);
        this.mRlAccount = (RelativeLayout) findViewById(R.id.tcb_user_address_rl);
        this.mRlPhone = (RelativeLayout) findViewById(R.id.tcb_user_phone_rl);
        this.mTxtAccount = (TextView) findViewById(R.id.tcb_user_address_txt);
        this.mTxtSave = (TextView) findViewById(R.id.tcb_save_txt);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            String path = data.getStringExtra(SelectPhotoActivity.FLAG_IMAGE_PATH);
            ImageLoader.getInstance().displayImage(ImageLoaderUtil.getUriFromLocalFile(new File(path)), this.mImgHeader);
            uploadImage(path);
        } else if (resultCode != 2 && resultCode == 3 && requestCode == 100) {
            String value = data.getStringExtra(CommonEditActivity.EXTRAS_VALUE);
            String type = data.getStringExtra(CommonEditActivity.EXTRAS_TYPE);
            if (type.equals("nickName")) {
                this.mTxtNickName.setText(value);
            } else if (type.equals("companyName")) {
                this.mTxtCompanyName.setText(value);
                this.mTxtCompanyFlag.setVisibility(View.GONE);
            } else if (type.equals("phone")) {
                this.mTxtPhone.setText(value);
            }
        }
    }

    private void uploadImage(final String filePath) {
        PutObjectRequest put = new PutObjectRequest(AppConstant.ALIYUN_OSS_BUCKET, AppConstant.ALIYUN_OSS_KEY + filePath, filePath);
        put.setProgressCallback(new C08005());
        OSSAsyncTask task = MyApplication.getInstance().getOssClient().asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                UserInfoActivity.this.mHeaderUrl = ServerUrl.URL_UPLOAD + "/" + AppConstant.ALIYUN_OSS_KEY + filePath;
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

    private void httpUpdateUserInfo() {
        RequestQueue queue = NoHttp.newRequestQueue();
        EditUserRequest request = new EditUserRequest(ServerUrl.getInst().UPDATE_USER_INFO_URL(), RequestMethod.POST, this.mHeaderUrl, this.mTxtNickName.getText().toString());
        request.setmBeanClass(LoginInfo.class, 0);
        queue.add(0, request, new C08027());
    }
}
