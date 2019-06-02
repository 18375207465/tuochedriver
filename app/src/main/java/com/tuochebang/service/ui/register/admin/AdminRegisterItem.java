package com.tuochebang.service.ui.register.admin;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.framework.app.component.utils.ActivityUtil;
import com.framework.app.component.utils.ToastUtil;
import com.tuochebang.service.R;
import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.entity.AdminInfo;
import com.tuochebang.service.request.task.CheckAccountRequest;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

public class AdminRegisterItem extends RelativeLayout {
    private static final int PHONE_NUMBER_SIZE = 1;
    private static final int PWD_SIZE = 1;
    private Button mBtnClear;
    private Button mBtnPwdClear;
    private Button mBtnRegister;
    private CheckBox mCbXieyi;
    private Context mContext;
    private EditText mEdPhone;
    private EditText mEdPwd;

    /* renamed from: com.tuochebang.service.ui.register.admin.AdminRegisterItem$1 */
    class C06861 implements TextWatcher {
        C06861() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            AdminRegisterItem.this.mBtnClear.setVisibility(VISIBLE);
            if (s.length() == 11) {
                AdminRegisterItem.this.httpCheckAccountRequest();
            }
        }

        public void afterTextChanged(Editable s) {
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.admin.AdminRegisterItem$2 */
    class C06872 implements TextWatcher {
        C06872() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                AdminRegisterItem.this.mBtnPwdClear.setVisibility(VISIBLE);
            } else {
                AdminRegisterItem.this.mBtnPwdClear.setVisibility(INVISIBLE);
            }
            if (s.length() < 1) {
                AdminRegisterItem.this.mBtnRegister.setEnabled(false);
            } else if (AdminRegisterItem.this.mEdPhone.getText().toString().length() >= 1) {
                AdminRegisterItem.this.mBtnRegister.setEnabled(true);
            } else {
                AdminRegisterItem.this.mBtnRegister.setEnabled(false);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.admin.AdminRegisterItem$3 */
    class C06883 implements OnClickListener {
        C06883() {
        }

        public void onClick(View view) {
            if (AdminRegisterItem.this.mCbXieyi.isChecked()) {
                Bundle bundle = new Bundle();
                AdminInfo info = new AdminInfo();
                info.setAccount(AdminRegisterItem.this.mEdPhone.getText().toString());
                info.setPassword(AdminRegisterItem.this.mEdPwd.getText().toString());
                bundle.putSerializable("adminInfo", info);
                ActivityUtil.next((Activity) AdminRegisterItem.this.mContext, IdCardActivity.class, bundle);
                return;
            }
            ToastUtil.showMessage(MyApplication.getInstance(), "请同意服务协议");
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.admin.AdminRegisterItem$4 */
    class C06894 implements OnClickListener {
        C06894() {
        }

        public void onClick(View v) {
            AdminRegisterItem.this.mEdPhone.getText().clear();
            AdminRegisterItem.this.mBtnClear.setVisibility(INVISIBLE);
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.admin.AdminRegisterItem$5 */
    class C06905 implements OnClickListener {
        C06905() {
        }

        public void onClick(View v) {
            AdminRegisterItem.this.mEdPwd.getText().clear();
            AdminRegisterItem.this.mBtnPwdClear.setVisibility(INVISIBLE);
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.admin.AdminRegisterItem$6 */
    class C06916 implements OnResponseListener<String> {
        C06916() {
        }

        public void onSucceed(int what, Response<String> response) {
            if (((String) response.get()).equals("true")) {
                ToastUtil.showMessage(MyApplication.getInstance(), "该手机号已经注册");
                AdminRegisterItem.this.mEdPhone.getText().clear();
                AdminRegisterItem.this.mBtnClear.setVisibility(INVISIBLE);
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

    public AdminRegisterItem(Context context) {
        super(context);
        initView(context);
    }

    public AdminRegisterItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AdminRegisterItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.component_view_admin_register, this);
        this.mEdPhone = (EditText) view.findViewById(R.id.username);
        this.mEdPwd = (EditText) view.findViewById(R.id.password);
        this.mBtnClear = (Button) view.findViewById(R.id.bt_username_clear);
        this.mBtnPwdClear = (Button) view.findViewById(R.id.bt_pwd_clear);
        this.mBtnRegister = (Button) view.findViewById(R.id.register);
        this.mCbXieyi = (CheckBox) view.findViewById(R.id.cb_xieyi);
        this.mEdPhone.addTextChangedListener(new C06861());
        this.mEdPwd.addTextChangedListener(new C06872());
        this.mBtnRegister.setOnClickListener(new C06883());
        this.mBtnClear.setOnClickListener(new C06894());
        this.mBtnPwdClear.setOnClickListener(new C06905());
    }

    private void httpCheckAccountRequest() {
        RequestQueue queue = NoHttp.newRequestQueue();
        queue.add(0, new CheckAccountRequest(ServerUrl.getInst().CHECK_ACCOUNT_EXIST(), RequestMethod.POST, this.mEdPhone.getText().toString()), new C06916());
    }
}
