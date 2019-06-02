package com.tuochebang.service.ui.request;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.framework.app.component.optimize.GridViewForScrollView;
import com.framework.app.component.utils.ActivityUtil;
import com.tuochebang.service.R;
import com.tuochebang.service.adapter.RequestImageListAdapter;
import com.tuochebang.service.base.BaseActivity;
import com.tuochebang.service.request.entity.TuocheRequestInfo;
import com.tuochebang.service.util.Tools;

public class WaitingRequestDetailActivity extends BaseActivity {
    private RequestImageListAdapter adapter;
    private Button mBtnRob;
    private GridViewForScrollView mGridView;
    TuocheRequestInfo mRequestInfo;
    private Toolbar mToolBar;
    private TextView mTxtBegin;
    private TextView mTxtCarInfo;
    private TextView mTxtEnd;
    private TextView mTxtPay;
    private TextView mTxtPrice;
    private TextView mTxtTime;

    /* renamed from: com.tuochebang.service.ui.request.WaitingRequestDetailActivity$1 */
    class C07381 implements OnClickListener {
        C07381() {
        }

        public void onClick(View v) {
            WaitingRequestDetailActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.service.ui.request.WaitingRequestDetailActivity$2 */
    class C07392 implements OnClickListener {
        C07392() {
        }

        public void onClick(View v) {
            ActivityUtil.next(WaitingRequestDetailActivity.this, ApplyTuocheRequestActivity.class);
            WaitingRequestDetailActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitrequest_detail);
        getIntentExtras();
        initToolBar();
        initView();
        initListener();
        initData();
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C07381());
    }

    private void initData() {
        if (this.mRequestInfo != null) {
            this.mTxtBegin.setText(this.mRequestInfo.getBegin());
            this.mTxtEnd.setText(this.mRequestInfo.getEnd());
            this.mTxtTime.setText(this.mRequestInfo.getTime());
            this.mTxtCarInfo.setText(this.mRequestInfo.getCar().getInfo() + " " + this.mRequestInfo.getCar().getModelName() + " " + this.mRequestInfo.getCar().getDriveName() + " " + this.mRequestInfo.getCar().getGearName());
            this.mTxtPay.setText(this.mRequestInfo.getPayName());
            this.mTxtPrice.setText(Tools.getShowMoneyString(Double.valueOf(this.mRequestInfo.getMoney()).doubleValue()));
            this.adapter.setList(this.mRequestInfo.getPicture());
        }
    }

    private void getIntentExtras() {
        this.mRequestInfo = (TuocheRequestInfo) getIntent().getSerializableExtra("request");
    }

    private void initListener() {
        this.mBtnRob.setOnClickListener(new C07392());
    }

    private void initView() {
        this.mTxtCarInfo = (TextView) findViewById(R.id.txt_car_info);
        this.mTxtTime = (TextView) findViewById(R.id.tcb_time_txt);
        this.mTxtBegin = (TextView) findViewById(R.id.tcb_begin_txt);
        this.mTxtEnd = (TextView) findViewById(R.id.tcb_end_txt);
        this.mTxtPay = (TextView) findViewById(R.id.tcb_pay_txt);
        this.mTxtPrice = (TextView) findViewById(R.id.tcb_request_price_txt);
        this.mGridView = (GridViewForScrollView) findViewById(R.id.noScrollgridview);
        this.mBtnRob = (Button) findViewById(R.id.rob);
        this.mGridView.setSelector(new ColorDrawable(0));
        this.adapter = new RequestImageListAdapter(this.mContext);
        this.mGridView.setAdapter(this.adapter);
    }
}
