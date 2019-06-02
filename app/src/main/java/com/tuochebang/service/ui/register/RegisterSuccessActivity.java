package com.tuochebang.service.ui.register;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tuochebang.service.R;
import com.tuochebang.service.base.BaseActivity;

public class RegisterSuccessActivity extends BaseActivity {
    private Button mBtnSure;
    private Toolbar mToolBar;

    /* renamed from: com.tuochebang.service.ui.register.RegisterSuccessActivity$1 */
    class C06841 implements OnClickListener {
        C06841() {
        }

        public void onClick(View v) {
            RegisterSuccessActivity.this.onBtnClick();
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.RegisterSuccessActivity$2 */
    class C06852 implements OnClickListener {
        C06852() {
        }

        public void onClick(View v) {
            RegisterSuccessActivity.this.onBtnClick();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);
        initToolBar();
        initView();
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C06841());
    }

    private void initView() {
        this.mBtnSure = (Button) findViewById(R.id.sure);
        this.mBtnSure.setOnClickListener(new C06852());
    }

    private void onBtnClick() {
        finish();
    }
}
