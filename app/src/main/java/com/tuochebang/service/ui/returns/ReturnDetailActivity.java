package com.tuochebang.service.ui.returns;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.app.component.dialog.CommonNoticeDialog;
import com.framework.app.component.widget.DataLoadingView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tuochebang.service.R;
import com.tuochebang.service.base.BaseActivity;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.entity.ReturnCarInfo;
import com.tuochebang.service.request.entity.TuocheRequestInfo;
import com.tuochebang.service.request.task.GetRequestDetailRequest;
import com.tuochebang.service.request.task.GetReturnDetailRequest;
import com.tuochebang.service.util.Tools;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReturnDetailActivity extends BaseActivity {
    public static final String EXTRAS_DETAIL_TYPE = "extras_detail_type";
    public static final String EXTRAS_ID = "extras_id";
    public static final int TYPE_REQUEST = 1;
    public static final int TYPE_RETURN = 0;
    private DataLoadingView mDataLoadingView;
    private int mDetailType;
    private int mId;
    private ImageView mImgCall;
    private ImageView mImgStatus;
    private CircleImageView mImgUser;
    private LinearLayout mLLOrderInfo;
    private TuocheRequestInfo mRequestInfo;
    private ReturnCarInfo mReturnInfo;
    private Toolbar mToolBar;
    private TextView mTxtCarBegin;
    private TextView mTxtCarEnd;
    private TextView mTxtCarInfo;
    private TextView mTxtCompanyName;
    private TextView mTxtDriverName;
    private TextView mTxtDriverPhone;
    private TextView mTxtDriverType;
    private TextView mTxtGoPay;
    private TextView mTxtOrderNum;
    private TextView mTxtPay;
    private TextView mTxtPrice;
    private TextView mTxtTime;
    private TextView mTxtUserPhone;

    /* renamed from: com.tuochebang.service.ui.returns.ReturnDetailActivity$1 */
    class C07591 implements OnClickListener {
        C07591() {
        }

        public void onClick(View v) {
            ReturnDetailActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.service.ui.returns.ReturnDetailActivity$2 */
    class C07602 implements OnClickListener {
        C07602() {
        }

        public void onClick(View v) {
            ReturnDetailActivity.this.onBtnGoPay();
        }
    }

    /* renamed from: com.tuochebang.service.ui.returns.ReturnDetailActivity$3 */
    class C07623 implements OnClickListener {

        /* renamed from: com.tuochebang.service.ui.returns.ReturnDetailActivity$3$1 */
        class C07611 implements CommonNoticeDialog.DialogButtonInterface {
            C07611() {
            }

            public void onDialogButtonClick(CommonNoticeDialog.DialogResult dialogResult) {
                if (dialogResult == CommonNoticeDialog.DialogResult.Yes) {
                    Tools.callPhone(ReturnDetailActivity.this, ReturnDetailActivity.this.mDetailType == 0 ? ReturnDetailActivity.this.mReturnInfo.getCustomer().getPhone() : ReturnDetailActivity.this.mRequestInfo.getCustomer().getPhone());
                }
            }
        }

        C07623() {
        }

        public void onClick(View v) {
            ReturnDetailActivity.this.showChoiceDialog("提示", "是否拨打该号码?", new C07611());
        }
    }

    /* renamed from: com.tuochebang.service.ui.returns.ReturnDetailActivity$4 */
    class C07634 implements OnResponseListener<ReturnCarInfo> {
        C07634() {
        }

        public void onStart(int what) {
            ReturnDetailActivity.this.mDataLoadingView.showDataLoading();
        }

        public void onSucceed(int what, Response<ReturnCarInfo> response) {
            if (response != null) {
                ReturnDetailActivity.this.mReturnInfo = (ReturnCarInfo) response.get();
                ReturnDetailActivity.this.mDataLoadingView.showDataLoadSuccess();
                ReturnDetailActivity.this.refreshReturnView();
            }
        }

        public void onFailed(int what, Response<ReturnCarInfo> response) {
        }

        public void onFinish(int what) {
        }
    }

    /* renamed from: com.tuochebang.service.ui.returns.ReturnDetailActivity$5 */
    class C07645 implements OnResponseListener<TuocheRequestInfo> {
        C07645() {
        }

        public void onStart(int what) {
            ReturnDetailActivity.this.mDataLoadingView.showDataLoading();
        }

        public void onSucceed(int what, Response<TuocheRequestInfo> response) {
            ReturnDetailActivity.this.mRequestInfo = (TuocheRequestInfo) response.get();
            ReturnDetailActivity.this.mDataLoadingView.showDataLoadSuccess();
            ReturnDetailActivity.this.refreshRequestView();
        }

        public void onFailed(int what, Response<TuocheRequestInfo> response) {
        }

        public void onFinish(int what) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_detail);
        getIntentExtras();
        initView();
        initToolBar();
        initListener();
        initData();
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C07591());
        this.mTxtGoPay = (TextView) findViewById(R.id.tcb_pay_txt);
        if (this.mDetailType == 1) {
            this.mToolBar.setTitle((CharSequence) "拖车请求详情");
        } else {
            this.mToolBar.setTitle((CharSequence) "返程车请求详情");
        }
    }

    private void getIntentExtras() {
        this.mDetailType = getIntent().getIntExtra(EXTRAS_DETAIL_TYPE, 0);
        this.mId = getIntent().getIntExtra(EXTRAS_ID, 0);
    }

    private void initData() {
        if (this.mDetailType == 1) {
            httpGetRequestDetail();
        } else if (this.mDetailType == 0) {
            httpGetReturnDetail();
        }
    }

    private void initListener() {
        this.mTxtGoPay.setOnClickListener(new C07602());
        this.mImgCall.setOnClickListener(new C07623());
    }

    private void onBtnGoPay() {
    }

    private void initView() {
        this.mDataLoadingView = (DataLoadingView) findViewById(R.id.data_loadingview);
        this.mImgUser = (CircleImageView) findViewById(R.id.img_user);
        this.mTxtTime = (TextView) findViewById(R.id.txt_time);
        this.mTxtOrderNum = (TextView) findViewById(R.id.txt_order_num);
        this.mTxtUserPhone = (TextView) findViewById(R.id.txt_user_phone);
        this.mTxtCompanyName = (TextView) findViewById(R.id.txt_company_name);
        this.mTxtCarBegin = (TextView) findViewById(R.id.tcb_return_begin_txt);
        this.mTxtCarEnd = (TextView) findViewById(R.id.tcb_return_end_txt);
        this.mTxtPrice = (TextView) findViewById(R.id.txt_price);
        this.mTxtDriverName = (TextView) findViewById(R.id.tcb_driver_username_txt);
        this.mTxtDriverType = (TextView) findViewById(R.id.tcb_driver_type_txt);
        this.mTxtDriverPhone = (TextView) findViewById(R.id.tcb_driver_phone_txt);
        this.mImgStatus = (ImageView) findViewById(R.id.img_status);
        this.mImgCall = (ImageView) findViewById(R.id.img_call);
        this.mLLOrderInfo = (LinearLayout) findViewById(R.id.ll_order_info);
    }

    private void httpGetReturnDetail() {
        RequestQueue queue = NoHttp.newRequestQueue();
        GetReturnDetailRequest returnDetailRequest = new GetReturnDetailRequest(ServerUrl.getInst().GET_TUOCHE_RETURNS_DETAIL(), RequestMethod.POST, this.mId);
        returnDetailRequest.setmBeanClass(ReturnCarInfo.class, -1);
        queue.add(0, returnDetailRequest, new C07634());
    }

    private void httpGetRequestDetail() {
        RequestQueue queue = NoHttp.newRequestQueue();
        GetRequestDetailRequest returnDetailRequest = new GetRequestDetailRequest(ServerUrl.getInst().GET_TUOCHE_REQUEST_DETAIL(), RequestMethod.POST, this.mId);
        returnDetailRequest.setmBeanClass(TuocheRequestInfo.class, -1);
        queue.add(0, returnDetailRequest, new C07645());
    }

    private void refreshRequestView() {
        if (this.mRequestInfo != null) {
            if (Integer.valueOf(this.mRequestInfo.getStatus()).intValue() == 0) {
                this.mLLOrderInfo.setVisibility(View.VISIBLE);
                this.mImgStatus.setImageResource(R.mipmap.icon_waiting_list);
                this.mTxtUserPhone.setText(this.mRequestInfo.getCustomer().getPhone());
                this.mTxtCompanyName.setText(this.mRequestInfo.getCustomer().getCorporate());
                ImageLoader.getInstance().displayImage(this.mRequestInfo.getCustomer().getPicture(), this.mImgUser);
            } else if (Integer.valueOf(this.mRequestInfo.getStatus()).intValue() == 1) {
                this.mLLOrderInfo.setVisibility(View.VISIBLE);
                this.mImgStatus.setImageResource(R.mipmap.icon_pay);
                this.mTxtUserPhone.setText(this.mRequestInfo.getCustomer().getPhone());
                this.mTxtCompanyName.setText(this.mRequestInfo.getCustomer().getCorporate());
                ImageLoader.getInstance().displayImage(this.mRequestInfo.getCustomer().getPicture(), this.mImgUser);
            } else if (Integer.valueOf(this.mRequestInfo.getStatus()).intValue() == 2) {
                this.mLLOrderInfo.setVisibility(View.VISIBLE);
                this.mImgStatus.setImageResource(R.mipmap.icon_already_completed);
                this.mTxtUserPhone.setText(this.mRequestInfo.getCustomer().getPhone());
                this.mTxtCompanyName.setText(this.mRequestInfo.getCustomer().getCorporate());
                ImageLoader.getInstance().displayImage(this.mRequestInfo.getCustomer().getPicture(), this.mImgUser);
            } else if (Integer.valueOf(this.mRequestInfo.getStatus()).intValue() == 3) {
                this.mLLOrderInfo.setVisibility(View.GONE);
                this.mImgStatus.setImageResource(R.mipmap.icon_confirm);
            }
            this.mTxtCarBegin.setText(this.mRequestInfo.getBegin());
            this.mTxtCarEnd.setText(this.mRequestInfo.getEnd());
            this.mTxtPrice.setText(Tools.getShowMoneyString(Double.valueOf(this.mRequestInfo.getMoney()).doubleValue()));
            this.mTxtTime.setText(this.mRequestInfo.getTime());
            this.mTxtDriverType.setText(this.mRequestInfo.getType());
            this.mTxtDriverName.setText(this.mRequestInfo.getName());
            this.mTxtDriverPhone.setText(this.mRequestInfo.getMobile());
            this.mTxtOrderNum.setText(this.mRequestInfo.getOrderNo());
        }
    }

    private void refreshReturnView() {
        if (this.mReturnInfo != null) {
            if (this.mReturnInfo.getStatus() == 0) {
                this.mLLOrderInfo.setVisibility(View.GONE);
                this.mImgStatus.setImageResource(R.mipmap.icon_waiting_list);
            } else if (this.mReturnInfo.getStatus() == 1) {
                this.mLLOrderInfo.setVisibility(View.VISIBLE);
                this.mImgStatus.setImageResource(R.mipmap.icon_inhand);
                this.mTxtUserPhone.setText(this.mReturnInfo.getCustomer().getPhone());
                this.mTxtCompanyName.setText(this.mReturnInfo.getCustomer().getCorporate());
                ImageLoader.getInstance().displayImage(this.mReturnInfo.getCustomer().getPicture(), this.mImgUser);
            } else if (this.mReturnInfo.getStatus() == 2) {
                this.mLLOrderInfo.setVisibility(View.VISIBLE);
                this.mImgStatus.setImageResource(R.mipmap.icon_already_completed);
                this.mTxtUserPhone.setText(this.mReturnInfo.getCustomer().getPhone());
                this.mTxtCompanyName.setText(this.mReturnInfo.getCustomer().getCorporate());
                ImageLoader.getInstance().displayImage(this.mReturnInfo.getCustomer().getPicture(), this.mImgUser);
            }
            this.mTxtCarBegin.setText(this.mReturnInfo.getBegin());
            this.mTxtCarEnd.setText(this.mReturnInfo.getEnd());
            this.mTxtPrice.setText(Tools.getShowMoneyString(this.mReturnInfo.getMoney()));
            this.mTxtTime.setText(this.mReturnInfo.getTime());
            this.mTxtDriverType.setText(this.mReturnInfo.getType());
            this.mTxtDriverName.setText(this.mReturnInfo.getName());
            this.mTxtDriverPhone.setText(this.mReturnInfo.getMobile());
            this.mTxtOrderNum.setText(this.mReturnInfo.getOrderNo());
        }
    }
}
