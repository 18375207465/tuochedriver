package com.tuochebang.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.framework.app.component.utils.ActivityUtil;
import com.framework.app.component.utils.BroadCastUtil;
import com.framework.app.component.utils.ImageLoaderUtil;
import com.framework.app.component.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.base.BaseActivity;
import com.tuochebang.service.constant.AppConstant.BroadCastAction;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.entity.LoginInfo;
import com.tuochebang.service.request.entity.UserInfo;
import com.tuochebang.service.request.task.UserInfoRequest;
import com.tuochebang.service.ui.login.LoginActivity;
import com.tuochebang.service.ui.request.UserRequestActivity;
import com.tuochebang.service.ui.request.WatingRequestActivity;
import com.tuochebang.service.ui.returns.PublishReturnActivity;
import com.tuochebang.service.ui.returns.UserReturnsActivity;
import com.tuochebang.service.ui.setting.SettingActivity;
import com.tuochebang.service.ui.user.UserAuthActivity;
import com.tuochebang.service.ui.user.UserChargeActivity;
import com.tuochebang.service.ui.user.UserInfoActivity;
import com.tuochebang.service.ui.user.UserMessageActivity;
import com.tuochebang.service.ui.user.UserTuocheManagerActivity;
import com.tuochebang.service.util.ImageUtil;
import com.tuochebang.service.view.PullToZoomScrollView;
import com.tuochebang.service.widget.RequestRobDialog;
import com.tuochebang.service.widget.RequestRobDialog.DialogButtonInterface;
import com.tuochebang.service.widget.RequestRobDialog.DialogResult;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import de.hdodenhof.circleimageview.CircleImageView;
import java.util.Random;

public class MainActivity extends BaseActivity {
    private View mAdminView;
    private View mDriverView;
    private CircleImageView mImgHeader;
    private ImageView mImgNews;
    private ImageView mImgSetting;
    private PullToZoomScrollView mPullView;
    private RelativeLayout mRlHeaderView;
    private RelativeLayout mRlRecharge;
    private RelativeLayout mRlTuocheManage;
    private RelativeLayout mRlUserAuth;
    private RelativeLayout mRlUserFindReturn;
    private RelativeLayout mRlUserMessage;
    private RelativeLayout mRlUserPullReturn;
    private RelativeLayout mRlUserRequest;
    private RelativeLayout mRlUserReturns;
    private RelativeLayout mRlUserSetting;
    private TextView mTxtAddress;
    private TextView mTxtCorporate;
    private TextView mTxtNickName;
    private TextView mTxtUserType;
    private BroadcastReceiver mUserStatuChangeReceiver;

    /* renamed from: com.tuochebang.service.MainActivity$1 */
    class C06311 implements DialogButtonInterface {
        C06311() {
        }

        public void onDialogButtonClick(DialogResult dialogResult) {
            if (dialogResult == DialogResult.Yes) {
                ToastUtil.showMessage(MyApplication.getInstance(), "抢单");
            }
        }
    }

    /* renamed from: com.tuochebang.service.MainActivity$2 */
    class C06322 implements OnClickListener {
        C06322() {
        }

        public void onClick(View v) {
        }
    }

    /* renamed from: com.tuochebang.service.MainActivity$3 */
    class C06333 implements OnClickListener {
        C06333() {
        }

        public void onClick(View v) {
            if (MyApplication.getInstance().isUserLogin()) {
                ActivityUtil.next(MainActivity.this, UserInfoActivity.class);
            } else {
                ActivityUtil.next(MainActivity.this, LoginActivity.class);
            }
        }
    }

    /* renamed from: com.tuochebang.service.MainActivity$4 */
    class C06344 implements OnClickListener {
        C06344() {
        }

        public void onClick(View v) {
            if (MyApplication.getInstance().isUserLogin()) {
                ActivityUtil.next(MainActivity.this, UserReturnsActivity.class);
            } else {
                ActivityUtil.next(MainActivity.this, LoginActivity.class);
            }
        }
    }

    /* renamed from: com.tuochebang.service.MainActivity$5 */
    class C06355 implements OnClickListener {
        C06355() {
        }

        public void onClick(View v) {
            ActivityUtil.next(MainActivity.this, SettingActivity.class);
        }
    }

    /* renamed from: com.tuochebang.service.MainActivity$6 */
    class C06366 implements OnClickListener {
        C06366() {
        }

        public void onClick(View v) {
            if (MyApplication.getInstance().isUserLogin()) {
                ActivityUtil.next(MainActivity.this, PublishReturnActivity.class);
            } else {
                ActivityUtil.next(MainActivity.this, LoginActivity.class);
            }
        }
    }

    /* renamed from: com.tuochebang.service.MainActivity$7 */
    class C06377 implements OnClickListener {
        C06377() {
        }

        public void onClick(View v) {
            if (MyApplication.getInstance().isUserLogin()) {
                ActivityUtil.next(MainActivity.this, WatingRequestActivity.class);
            } else {
                ActivityUtil.next(MainActivity.this, LoginActivity.class);
            }
        }
    }

    /* renamed from: com.tuochebang.service.MainActivity$8 */
    class C06388 implements OnClickListener {
        C06388() {
        }

        public void onClick(View v) {
            if (MyApplication.getInstance().isUserLogin()) {
                ActivityUtil.next(MainActivity.this, UserTuocheManagerActivity.class);
            } else {
                ActivityUtil.next(MainActivity.this, LoginActivity.class);
            }
        }
    }

    /* renamed from: com.tuochebang.service.MainActivity$9 */
    class C06399 implements OnClickListener {
        C06399() {
        }

        public void onClick(View v) {
            if (MyApplication.getInstance().isUserLogin()) {
                ActivityUtil.next(MainActivity.this, UserAuthActivity.class);
            } else {
                ActivityUtil.next(MainActivity.this, LoginActivity.class);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!MyApplication.getInstance().isUserLogin()) {
            ActivityUtil.next(this, LoginActivity.class);
        }
        textPushDialog();
        setCouldDoubleBackExit(true);
        initView();
        initListerner();
        registUserStatuChangeBroadCastReciver();
        initData();
    }

    private void textPushDialog() {
        if (MyApplication.getInstance().isUserLogin() && new Random().nextInt() % 2 == 0) {
            new RequestRobDialog(this.mContext, "", "", new C06311()).show();
        }
    }

    private void initListerner() {
        this.mRlUserReturns.setOnClickListener(new C06322());
        findViewById(R.id.ll_user_info).setOnClickListener(new C06333());
        this.mRlUserReturns.setOnClickListener(new C06344());
        this.mImgSetting.setOnClickListener(new C06355());
        this.mRlUserPullReturn.setOnClickListener(new C06366());
        this.mRlUserFindReturn.setOnClickListener(new C06377());
        this.mRlTuocheManage.setOnClickListener(new C06388());
        this.mRlUserAuth.setOnClickListener(new C06399());
        this.mRlUserSetting.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ActivityUtil.next(MainActivity.this, SettingActivity.class);
            }
        });
        this.mRlUserRequest.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MyApplication.getInstance().isUserLogin()) {
                    ActivityUtil.next(MainActivity.this, UserRequestActivity.class);
                } else {
                    ActivityUtil.next(MainActivity.this, LoginActivity.class);
                }
            }
        });
        this.mRlRecharge.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MyApplication.getInstance().isUserLogin()) {
                    ActivityUtil.next(MainActivity.this, UserChargeActivity.class);
                } else {
                    ActivityUtil.next(MainActivity.this, LoginActivity.class);
                }
            }
        });
        this.mRlUserMessage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MyApplication.getInstance().isUserLogin()) {
                    ActivityUtil.next(MainActivity.this, UserMessageActivity.class);
                } else {
                    ActivityUtil.next(MainActivity.this, LoginActivity.class);
                }
            }
        });
        this.mImgNews.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MyApplication.getInstance().isUserLogin()) {
                    ActivityUtil.next(MainActivity.this, UserMessageActivity.class);
                } else {
                    ActivityUtil.next(MainActivity.this, LoginActivity.class);
                }
            }
        });
    }

    private void initView() {
        this.mImgHeader = (CircleImageView) findViewById(R.id.tcb_profile_img);
        this.mTxtNickName = (TextView) findViewById(R.id.tcb_user_name_txt);
        this.mTxtCorporate = (TextView) findViewById(R.id.tcb_user_company_txt);
        this.mTxtUserType = (TextView) findViewById(R.id.tcb_user_auth_txt);
        this.mTxtAddress = (TextView) findViewById(R.id.tcb_user_address_txt);
        this.mPullView = (PullToZoomScrollView) findViewById(R.id.tcb_main_root_scroll);
        this.mRlHeaderView = (RelativeLayout) findViewById(R.id.rl_header_view);
        this.mPullView.setZoomView(this.mRlHeaderView);
        this.mRlUserPullReturn = (RelativeLayout) findViewById(R.id.tcb_pull_return_rl);
        this.mRlUserFindReturn = (RelativeLayout) findViewById(R.id.tcb_wating_pull_request_rl);
        this.mRlUserRequest = (RelativeLayout) findViewById(R.id.tcb_user_request_rl);
        this.mRlUserReturns = (RelativeLayout) findViewById(R.id.tcb_returns_car_rl);
        this.mAdminView = findViewById(R.id.ll_admin_view);
        this.mRlUserMessage = (RelativeLayout) findViewById(R.id.tcb_message_rl);
        this.mRlUserSetting = (RelativeLayout) findViewById(R.id.tcb_setting_rl);
        this.mDriverView = findViewById(R.id.ll_driver_view);
        this.mRlUserAuth = (RelativeLayout) findViewById(R.id.tcb_authorize_rl);
        this.mRlTuocheManage = (RelativeLayout) findViewById(R.id.tcb_manage_rl);
        this.mRlRecharge = (RelativeLayout) findViewById(R.id.tcb_recharge_rl);
        this.mImgSetting = (ImageView) findViewById(R.id.img_setting);
        this.mImgNews = (ImageView) findViewById(R.id.img_news);
    }

    private void registUserStatuChangeBroadCastReciver() {
        this.mUserStatuChangeReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String action = intent.getAction();
                    if (BroadCastAction.USER_LOGIN_SUCCESS.equals(action)) {
                        MainActivity.this.refreshView(MyApplication.getInstance().getUserInfo());
                    } else if (BroadCastAction.USER_LOGOUT_SUCCESS.equals(action)) {
                        ActivityUtil.next(MainActivity.this, LoginActivity.class);
                        MainActivity.this.refreshView(null);
                    } else if (BroadCastAction.USER_INFO_CHANGE.equals(action)) {
                        MainActivity.this.httpGetUserInfo();
                    } else if (BroadCastAction.VALIDATA_TOKE.equals(action)) {
                        ToastUtil.showMessage(MyApplication.getInstance(), "该账号已在其它设备请重新登录");
                        MyApplication.getInstance().setUserInfo(null);
                        MyApplication.getInstance().setLoginInfo(null);
                        BroadCastUtil.sendBroadCast(MyApplication.getInstance(), BroadCastAction.USER_LOGOUT_SUCCESS);
                    } else if (BroadCastAction.TOKE_EXPIRE.equals(action)) {
                        ToastUtil.showMessage(MyApplication.getInstance(), "登录信息已过期请重新登录");
                        MyApplication.getInstance().setUserInfo(null);
                        MyApplication.getInstance().setLoginInfo(null);
                        BroadCastUtil.sendBroadCast(MyApplication.getInstance(), BroadCastAction.USER_LOGOUT_SUCCESS);
                    } else if (BroadCastAction.REQUEST_ERROR.equals(action)) {
                        ToastUtil.showMessage(MyApplication.getInstance(), intent.getStringExtra("msg"));
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter(BroadCastAction.USER_LOGIN_SUCCESS);
        filter.addAction(BroadCastAction.USER_LOGOUT_SUCCESS);
        filter.addAction(BroadCastAction.USER_INFO_CHANGE);
        filter.addAction(BroadCastAction.VALIDATA_TOKE);
        filter.addAction(BroadCastAction.REQUEST_ERROR);
        filter.addAction(BroadCastAction.TOKE_EXPIRE);
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mUserStatuChangeReceiver, filter);
    }

    private void refreshView(UserInfo mUserInfo) {
        if (mUserInfo == null) {
            this.mTxtCorporate.setText("---");
            this.mTxtNickName.setText("未登录");
            this.mTxtAddress.setText("----");
            this.mRlHeaderView.setBackground(getResources().getDrawable(R.mipmap.background_one));
            this.mImgHeader.setImageResource(R.mipmap.icon_bigportrait);
            return;
        }
        if (TextUtils.isEmpty(mUserInfo.getPicture())) {
            this.mImgHeader.setImageResource(R.mipmap.icon_bigportrait);
        } else {
            ImageLoader.getInstance().displayImage(mUserInfo.getPicture(), this.mImgHeader, ImageLoaderUtil.getNotShowDisplayImageOptions(), new ImageLoadingListener() {
                public void onLoadingStarted(String arg0, View arg1) {
                }

                public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                    MainActivity.this.mImgHeader.setImageResource(R.mipmap.icon_bigportrait);
                }

                public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
                    Bitmap userHeader = bitmap;
                    if (userHeader != null) {
                        MainActivity.this.mRlHeaderView.setBackgroundDrawable(ImageUtil.BoxBlurFilter(userHeader));
                    }
                }

                public void onLoadingCancelled(String arg0, View arg1) {
                }
            });
        }
        this.mTxtCorporate.setText(mUserInfo.getCorporate());
        this.mTxtNickName.setText(mUserInfo.getNickName());
        this.mTxtAddress.setText(mUserInfo.getAddress());
        if (mUserInfo.getUserType() == 0) {
            this.mAdminView.setVisibility(View.VISIBLE);
            this.mDriverView.setVisibility(View.GONE);
            this.mImgNews.setVisibility(View.VISIBLE);
            this.mImgSetting.setVisibility(View.VISIBLE);
        } else if (mUserInfo.getUserType() == 1) {
            this.mAdminView.setVisibility(View.GONE);
            this.mDriverView.setVisibility(View.VISIBLE);
            this.mImgNews.setVisibility(View.GONE);
            this.mImgSetting.setVisibility(View.GONE);
        }
    }

    private void initData() {
        if (MyApplication.getInstance().isUserLogin()) {
            refreshView(MyApplication.getInstance().getUserInfo());
            httpGetUserInfo();
        }
    }

    private void httpGetUserInfo() {
        RequestQueue queue = NoHttp.newRequestQueue();
        UserInfoRequest request = new UserInfoRequest(ServerUrl.getInst().USER_INFO_URL(), RequestMethod.POST, String.valueOf(MyApplication.getInstance().getUserInfo().getUserId()));
        request.setmBeanClass(LoginInfo.class, 0);
        queue.add(0, request, new OnResponseListener<LoginInfo>() {
            public void onSucceed(int what, Response<LoginInfo> response) {
                LoginInfo loginInfo = (LoginInfo) response.get();
                if (loginInfo != null) {
                    Log.e(MainActivity.this.TAG, loginInfo.toString());
                    MyApplication.getInstance().setUserInfo(loginInfo.getUser());
                    MainActivity.this.refreshView(loginInfo.getUser());
                }
            }

            public void onFailed(int what, Response<LoginInfo> response) {
                Exception exception = response.getException();
            }

            public void onStart(int what) {
            }

            public void onFinish(int what) {
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mUserStatuChangeReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mUserStatuChangeReceiver);
        }
    }
}
