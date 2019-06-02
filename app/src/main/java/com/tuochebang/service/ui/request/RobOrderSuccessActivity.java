package com.tuochebang.service.ui.request;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.framework.app.component.utils.ActivityUtil;
import com.tuochebang.service.R;
import com.tuochebang.service.base.BaseActivity;

public class RobOrderSuccessActivity extends BaseActivity {
    private Button mBtnSure;
    private Toolbar mToolBar;

    /* renamed from: com.tuochebang.service.ui.request.RobOrderSuccessActivity$1 */
    class C07331 implements OnClickListener {
        C07331() {
        }

        public void onClick(View v) {
            RobOrderSuccessActivity.this.onBtnClick();
        }
    }

    /* renamed from: com.tuochebang.service.ui.request.RobOrderSuccessActivity$2 */
    class C07342 implements OnClickListener {
        C07342() {
        }

        public void onClick(View v) {
            RobOrderSuccessActivity.this.onBtnClick();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rob_success);
        initToolBar();
        initView();
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C07331());
    }

    private void initView() {
        this.mBtnSure = (Button) findViewById(R.id.sure);
        this.mBtnSure.setOnClickListener(new C07342());
    }

    private void onBtnClick() {
        ActivityUtil.next(this, UserRequestActivity.class);
        finish();
    }
}
