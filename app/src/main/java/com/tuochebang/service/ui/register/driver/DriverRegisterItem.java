package com.tuochebang.service.ui.register.driver;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.framework.app.component.utils.ActivityUtil;
import com.framework.app.component.utils.ToastUtil;
import com.tuochebang.service.R;
import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.task.CheckAccountRequest;
import com.tuochebang.service.request.task.RegisterRequest;
import com.tuochebang.service.ui.register.RegisterActivity;
import com.tuochebang.service.ui.register.RegisterSuccessActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

public class DriverRegisterItem extends RelativeLayout {
    private static final int PHONE_NUMBER_SIZE = 1;
    private static final int PWD_SIZE = 1;
    private Button mBtnAdminClear;
    private Button mBtnClear;
    private Button mBtnPwdClear;
    private Button mBtnRegister;
    private Context mContext;
    private EditText mEdAdminName;
    private EditText mEdPhone;
    private EditText mEdPwd;

    /* renamed from: com.tuochebang.service.ui.register.driver.DriverRegisterItem$1 */
    class C07091 implements TextWatcher {
        C07091() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                DriverRegisterItem.this.mBtnClear.setVisibility(VISIBLE);
            } else {
                DriverRegisterItem.this.mBtnClear.setVisibility(GONE);
            }
            if (s.length() == 11) {
                DriverRegisterItem.this.httpCheckAccountRequest();
            }
        }

        public void afterTextChanged(Editable s) {
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.driver.DriverRegisterItem$2 */
    class C07102 implements TextWatcher {
        C07102() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                DriverRegisterItem.this.mBtnAdminClear.setVisibility(VISIBLE);
            } else {
                DriverRegisterItem.this.mBtnAdminClear.setVisibility(GONE);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.driver.DriverRegisterItem$3 */
    class C07113 implements TextWatcher {
        C07113() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                DriverRegisterItem.this.mBtnPwdClear.setVisibility(VISIBLE);
            } else {
                DriverRegisterItem.this.mBtnPwdClear.setVisibility(INVISIBLE);
            }
            if (s.length() < 1) {
                DriverRegisterItem.this.mBtnRegister.setEnabled(false);
            } else if (DriverRegisterItem.this.mEdPhone.getText().toString().length() >= 1) {
                DriverRegisterItem.this.mBtnRegister.setEnabled(true);
            } else {
                DriverRegisterItem.this.mBtnRegister.setEnabled(false);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.driver.DriverRegisterItem$4 */
    class C07124 implements OnClickListener {
        C07124() {
        }

        public void onClick(View v) {
            DriverRegisterItem.this.mEdPhone.getText().clear();
            DriverRegisterItem.this.mBtnClear.setVisibility(INVISIBLE);
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.driver.DriverRegisterItem$5 */
    class C07135 implements OnClickListener {
        C07135() {
        }

        public void onClick(View v) {
            DriverRegisterItem.this.mEdPwd.getText().clear();
            DriverRegisterItem.this.mBtnPwdClear.setVisibility(INVISIBLE);
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.driver.DriverRegisterItem$6 */
    class C07146 implements OnClickListener {
        C07146() {
        }

        public void onClick(View v) {
            DriverRegisterItem.this.mEdAdminName.getText().clear();
            DriverRegisterItem.this.mBtnAdminClear.setVisibility(INVISIBLE);
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.driver.DriverRegisterItem$7 */
    class C07157 implements OnClickListener {
        C07157() {
        }

        public void onClick(View view) {
            DriverRegisterItem.this.httpRegisterRequest();
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.driver.DriverRegisterItem$8 */
    class C07168 implements OnResponseListener<Object> {
        C07168() {
        }

        public void onSucceed(int what, Response<Object> response) {
            if (response.get() != null) {
                ToastUtil.showMessage(DriverRegisterItem.this.mContext.getApplicationContext(), "注册成功");
                ActivityUtil.next((RegisterActivity) DriverRegisterItem.this.mContext, RegisterSuccessActivity.class);
                ((RegisterActivity) DriverRegisterItem.this.mContext).finish();
            }
        }

        public void onFailed(int what, Response<Object> response) {
            Exception exception = response.getException();
        }

        public void onStart(int what) {
            ((RegisterActivity) DriverRegisterItem.this.mContext).showCommonProgreessDialog("請稍後...");
        }

        public void onFinish(int what) {
            ((RegisterActivity) DriverRegisterItem.this.mContext).dismissCommonProgressDialog();
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.driver.DriverRegisterItem$9 */
    class C07179 implements OnResponseListener<String> {
        C07179() {
        }

        public void onSucceed(int what, Response<String> response) {
            if (((String) response.get()).equals("true")) {
                ToastUtil.showMessage(MyApplication.getInstance(), "该手机号已经注册");
                DriverRegisterItem.this.mEdPhone.getText().clear();
                DriverRegisterItem.this.mBtnClear.setVisibility(INVISIBLE);
            }
        }

        public void onFailed(int what, Response<String> response) {
            Exception exception = response.getException();
        }

        public void onStart(int what) {
        }

        public void onFinish(int what) {
        }
    }

    public DriverRegisterItem(Context context) {
        super(context);
        initView(context);
    }

    public DriverRegisterItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DriverRegisterItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.component_view_driver_register, this);
        this.mEdPhone = (EditText) view.findViewById(R.id.phone);
        this.mEdAdminName = (EditText) view.findViewById(R.id.admin_name);
        this.mEdPwd = (EditText) view.findViewById(R.id.password);
        this.mBtnClear = (Button) view.findViewById(R.id.bt_username_clear);
        this.mBtnPwdClear = (Button) view.findViewById(R.id.bt_pwd_clear);
        this.mBtnAdminClear = (Button) view.findViewById(R.id.bt_admin_clear);
        this.mBtnRegister = (Button) view.findViewById(R.id.register);
        this.mEdPhone.addTextChangedListener(new C07091());
        this.mEdAdminName.addTextChangedListener(new C07102());
        this.mEdPwd.addTextChangedListener(new C07113());
        this.mBtnClear.setOnClickListener(new C07124());
        this.mBtnPwdClear.setOnClickListener(new C07135());
        this.mBtnAdminClear.setOnClickListener(new C07146());
        this.mBtnRegister.setOnClickListener(new C07157());
    }

    private void httpRegisterRequest() {
        RequestQueue queue = NoHttp.newRequestQueue();
        RegisterRequest request = new RegisterRequest(ServerUrl.getInst().USER_REGISTER_URL(), RequestMethod.POST, this.mEdAdminName.getText().toString(), this.mEdPwd.getText().toString(), this.mEdPhone.getText().toString());
        request.setmBeanClass(Object.class, 0);
        queue.add(0, request, new C07168());
    }

    private void httpCheckAccountRequest() {
        RequestQueue queue = NoHttp.newRequestQueue();
        queue.add(0, new CheckAccountRequest(ServerUrl.getInst().CHECK_ACCOUNT_EXIST(), RequestMethod.POST, this.mEdPhone.getText().toString()), new C07179());
    }
}
