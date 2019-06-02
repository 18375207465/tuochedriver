package com.tuochebang.service.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.tuochebang.service.R;
import com.tuochebang.service.base.BaseActivity;

public class CommonEditActivity extends BaseActivity {
    public static String EXTRAS_INPUT_TYPE = "extras_input_type";
    public static String EXTRAS_TITLE = "extras_title";
    public static String EXTRAS_TYPE = LocationMapActivity.EXTRAS_TYPE;
    public static String EXTRAS_VALUE = "extras_value";
    private EditText mEdInfo;
    private String mInputType;
    private String mTitle;
    private Toolbar mToolBar;
    private TextView mTxtSave;
    private String mType;

    /* renamed from: com.tuochebang.service.ui.CommonEditActivity$1 */
    class C06591 implements OnClickListener {
        C06591() {
        }

        public void onClick(View v) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(CommonEditActivity.EXTRAS_VALUE, CommonEditActivity.this.mEdInfo.getText().toString());
            bundle.putString(CommonEditActivity.EXTRAS_TYPE, CommonEditActivity.this.mType);
            intent.putExtras(bundle);
            CommonEditActivity.this.hideSoftInput(CommonEditActivity.this.mEdInfo);
            CommonEditActivity.this.setResult(3, intent);
            CommonEditActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.service.ui.CommonEditActivity$2 */
    class C06602 implements OnClickListener {
        C06602() {
        }

        public void onClick(View v) {
            CommonEditActivity.this.hideSoftInput(CommonEditActivity.this.mEdInfo);
            CommonEditActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_edit);
        getIntentExtras();
        initToolBar();
        initView();
        initListener();
    }

    private void initListener() {
        this.mTxtSave.setOnClickListener(new C06591());
    }

    private void initView() {
        this.mEdInfo = (EditText) findViewById(R.id.tcb_edit_info_ed);
        this.mTxtSave = (TextView) findViewById(R.id.tcb_sure_txt);
        if (this.mInputType.equals("number")) {
            this.mEdInfo.setInputType(2);
        } else if (this.mInputType.equals("text")) {
            this.mEdInfo.setInputType(1);
        }
    }

    private void getIntentExtras() {
        this.mInputType = getIntent().getStringExtra(EXTRAS_INPUT_TYPE);
        this.mTitle = getIntent().getStringExtra(EXTRAS_TITLE);
        this.mType = getIntent().getStringExtra(EXTRAS_TYPE);
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C06602());
        this.mToolBar.setTitle(this.mTitle);
    }
}
