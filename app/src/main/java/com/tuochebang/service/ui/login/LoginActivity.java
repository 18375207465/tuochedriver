package com.tuochebang.service.ui.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.framework.app.component.utils.ActivityUtil;
import com.framework.app.component.utils.BroadCastUtil;
import com.framework.app.component.utils.ToastUtil;
import com.tuochebang.service.PermissionHelper;
import com.tuochebang.service.R;
import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.base.BaseActivity;
import com.tuochebang.service.constant.AppConstant.BroadCastAction;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.entity.LoginInfo;
import com.tuochebang.service.request.task.LoginRequest;
import com.tuochebang.service.ui.register.RegisterActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

public class LoginActivity extends BaseActivity {
    private static final int PHONE_NUMBER_SIZE = 1;
    private static final int PWD_SIZE = 1;
    private Button mBtnClear;
    private TextView mBtnLogin;
    private Button mBtnPwdClear;
    private TextView mBtnRegister;
    private EditText mEdPhone;
    private EditText mEdPwd;
    private ScrollView mScrollView;

    /* renamed from: com.tuochebang.service.ui.login.LoginActivity$1 */
    class C06711 implements TextWatcher {
        C06711() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            LoginActivity.this.mBtnClear.setVisibility(View.VISIBLE);
        }

        public void afterTextChanged(Editable s) {
        }
    }

    /* renamed from: com.tuochebang.service.ui.login.LoginActivity$2 */
    class C06722 implements OnTouchListener {
        C06722() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            LoginActivity.this.changeScrollView();
            return false;
        }
    }

    /* renamed from: com.tuochebang.service.ui.login.LoginActivity$3 */
    class C06733 implements TextWatcher {
        C06733() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                LoginActivity.this.mBtnPwdClear.setVisibility(View.VISIBLE);
            } else {
                LoginActivity.this.mBtnPwdClear.setVisibility(View.INVISIBLE);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    }

    /* renamed from: com.tuochebang.service.ui.login.LoginActivity$4 */
    class C06744 implements OnClickListener {
        C06744() {
        }

        public void onClick(View v) {
            LoginActivity.this.httpLoginRequest();
        }
    }

    /* renamed from: com.tuochebang.service.ui.login.LoginActivity$5 */
    class C06755 implements OnClickListener {
        C06755() {
        }

        public void onClick(View v) {
            LoginActivity.this.mEdPhone.getText().clear();
            LoginActivity.this.mBtnClear.setVisibility(View.INVISIBLE);
        }
    }

    /* renamed from: com.tuochebang.service.ui.login.LoginActivity$6 */
    class C06766 implements OnClickListener {
        C06766() {
        }

        public void onClick(View v) {
            LoginActivity.this.mEdPwd.getText().clear();
            LoginActivity.this.mBtnPwdClear.setVisibility(View.INVISIBLE);
        }
    }

    /* renamed from: com.tuochebang.service.ui.login.LoginActivity$7 */
    class C06777 implements OnClickListener {
        C06777() {
        }

        public void onClick(View v) {
            ActivityUtil.next(LoginActivity.this, RegisterActivity.class);
        }
    }

    /* renamed from: com.tuochebang.service.ui.login.LoginActivity$8 */
    class C06788 implements OnResponseListener<LoginInfo> {
        C06788() {
        }

        public void onSucceed(int what, Response<LoginInfo> response) {
            LoginInfo loginInfo = (LoginInfo) response.get();
            if (loginInfo != null) {
                Log.e("LoginPresenter", loginInfo.toString());
                MyApplication.getInstance().setLoginInfo(loginInfo.getToken());
                MyApplication.getInstance().setUserInfo(loginInfo.getUser());
                ToastUtil.showMessage(MyApplication.getInstance(), "登录成功");
                BroadCastUtil.sendBroadCast(LoginActivity.this.mContext, BroadCastAction.USER_LOGIN_SUCCESS);
                LoginActivity.this.finish();
            }
        }

        public void onFailed(int what, Response<LoginInfo> response) {
            Exception exception = response.getException();
        }

        public void onStart(int what) {
            LoginActivity.this.showCommonProgreessDialog("請稍後...");
        }

        public void onFinish(int what) {
            LoginActivity.this.dismissCommonProgressDialog();
        }
    }

    /* renamed from: com.tuochebang.service.ui.login.LoginActivity$9 */
    class C06799 implements Runnable {
        C06799() {
        }

        public void run() {
            LoginActivity.this.mScrollView.scrollTo(0, 500);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();
        changeScrollView();
        PermissionHelper.getInstance().buildRequest(this)
                .addRequestPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .addRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .addRequestPermission(Manifest.permission.LOCATION_HARDWARE)
                .setDeniedAlertType(PermissionHelper.DeniedAlertType.Toast)
                .setAction(new PermissionHelper.PermissionsResultAction() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onGranted() {

                    }

                    @Override
                    public void onDenied(String permission) {
                        super.onDenied(permission);
                    }
                })
                .requst();

    }

    private void initView() {
        this.mEdPhone = (EditText) findViewById(R.id.username);
        this.mEdPwd = (EditText) findViewById(R.id.password);
        this.mBtnLogin = (TextView) findViewById(R.id.txt_btn_login);
        this.mBtnClear = (Button) findViewById(R.id.bt_username_clear);
        this.mBtnPwdClear = (Button) findViewById(R.id.bt_pwd_clear);
        this.mBtnRegister = (TextView) findViewById(R.id.txt_user_register);
        this.mScrollView = (ScrollView) findViewById(R.id.tcb_scroll);
    }

    private void initListener() {
        this.mEdPhone.addTextChangedListener(new C06711());
        this.mEdPhone.setOnTouchListener(new C06722());
        this.mEdPwd.addTextChangedListener(new C06733());
        this.mBtnLogin.setOnClickListener(new C06744());
        this.mBtnClear.setOnClickListener(new C06755());
        this.mBtnPwdClear.setOnClickListener(new C06766());
        this.mBtnRegister.setOnClickListener(new C06777());
    }

    private void httpLoginRequest() {
        RequestQueue queue = NoHttp.newRequestQueue();
        LoginRequest request = new LoginRequest(ServerUrl.getInst().USER_LOGIN_URL(), RequestMethod.POST, this.mEdPhone.getText().toString(), this.mEdPwd.getText().toString());
        request.setmBeanClass(LoginInfo.class, 0);
        queue.add(0, request, new C06788());
    }

    private void changeScrollView() {
        new Handler().postDelayed(new C06799(), 800);
    }
}
