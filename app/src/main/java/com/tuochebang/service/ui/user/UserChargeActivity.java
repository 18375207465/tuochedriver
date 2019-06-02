package com.tuochebang.service.ui.user;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.framework.app.component.utils.ToastUtil;
import com.tuochebang.service.R;
import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.base.BaseActivity;
import com.tuochebang.service.util.AndroidUtil;

public class UserChargeActivity extends BaseActivity {
    private static final String CHANNEL_ALIPAY = "alipay";
    private static final String CHANNEL_WECHAT = "wx";
    private Button mBtnCharge;
    private Button mBtnClear;
    private EditText mEdCharge;
    private String mPayChannel;
    private RadioButton mRdAliPay;
    private RadioButton mRdWechat;
    private Toolbar mToolBar;

    /* renamed from: com.tuochebang.service.ui.user.UserChargeActivity$1 */
    class C07921 implements TextWatcher {
        C07921() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                UserChargeActivity.this.mBtnClear.setVisibility(View.VISIBLE);
                UserChargeActivity.this.judgePrice();
            } else {
                UserChargeActivity.this.mBtnCharge.setEnabled(false);
                UserChargeActivity.this.mBtnClear.setVisibility(View.INVISIBLE);
            }
            UserChargeActivity.this.mBtnClear.setVisibility(View.VISIBLE);
        }

        public void afterTextChanged(Editable s) {
        }
    }

    /* renamed from: com.tuochebang.service.ui.user.UserChargeActivity$2 */
    class C07932 implements OnClickListener {
        C07932() {
        }

        public void onClick(View v) {
            UserChargeActivity.this.onBtnPayClick();
        }
    }

    /* renamed from: com.tuochebang.service.ui.user.UserChargeActivity$3 */
    class C07943 implements OnClickListener {
        C07943() {
        }

        public void onClick(View v) {
            UserChargeActivity.this.mEdCharge.getText().clear();
            UserChargeActivity.this.mBtnClear.setVisibility(View.INVISIBLE);
        }
    }

    /* renamed from: com.tuochebang.service.ui.user.UserChargeActivity$4 */
    class C07954 implements OnClickListener {
        C07954() {
        }

        public void onClick(View v) {
            UserChargeActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        initToolBar();
        initView();
        initListener();
    }

    private void initListener() {
        this.mEdCharge.addTextChangedListener(new C07921());
        this.mBtnCharge.setOnClickListener(new C07932());
        this.mBtnClear.setOnClickListener(new C07943());
    }

    private void judgePrice() {
        if (Integer.valueOf(this.mEdCharge.getText().toString()).intValue() > 0) {
            this.mBtnCharge.setEnabled(true);
        } else {
            ToastUtil.showMessage(MyApplication.getInstance(), "请输入大于0的金额");
        }
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C07954());
    }

    private void initView() {
        this.mRdWechat = (RadioButton) findViewById(R.id.rb_wechat);
        this.mRdAliPay = (RadioButton) findViewById(R.id.rb_alipay);
        this.mBtnCharge = (Button) findViewById(R.id.charge);
        this.mBtnClear = (Button) findViewById(R.id.bt_charge_clear);
        this.mEdCharge = (EditText) findViewById(R.id.et_charge);
    }

    public void onBtnPayClick() {
        if (this.mRdAliPay.isChecked()) {
            this.mPayChannel = CHANNEL_WECHAT;
        } else if (!this.mRdWechat.isChecked()) {
            ToastUtil.showMessage(MyApplication.getInstance(), "请选择一种支付方式");
            return;
        } else if (AndroidUtil.isAppInstalled(this, "com.tencent.mm")) {
            this.mPayChannel = CHANNEL_WECHAT;
        } else {
            showNoticeMsg("您还没有安装微信哟");
        }
        this.mBtnCharge.setEnabled(false);
        this.mBtnCharge.setText("正在支付...");
    }
}
