package com.tuochebang.service.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class PayActivity extends BaseActivity {
    private static final String CHANNEL_ALIPAY = "alipay";
    private static final String CHANNEL_WECHAT = "wx";
    private Button mBtnCharge;
    private Button mBtnClear;
    private EditText mEdCharge;
    private String mPayChannel;
    private RadioButton mRdAliPay;
    private RadioButton mRdWechat;
    private Toolbar mToolBar;

    /* renamed from: com.tuochebang.service.ui.PayActivity$1 */
    class C06661 implements OnClickListener {
        C06661() {
        }

        public void onClick(View v) {
            PayActivity.this.onBtnPayClick();
        }
    }

    /* renamed from: com.tuochebang.service.ui.PayActivity$2 */
    class C06672 implements OnClickListener {
        C06672() {
        }

        public void onClick(View v) {
            PayActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        initToolBar();
        initView();
        initListener();
    }

    private void initListener() {
        this.mBtnCharge.setOnClickListener(new C06661());
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C06672());
    }

    private void initView() {
        this.mRdWechat = (RadioButton) findViewById(R.id.rb_wechat);
        this.mRdAliPay = (RadioButton) findViewById(R.id.rb_alipay);
        this.mBtnCharge = (Button) findViewById(R.id.charge);
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
