package com.tuochebang.service.ui.returns;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amap.api.maps2d.model.LatLng;
import com.framework.app.component.utils.ActivityUtil;
import com.framework.app.component.utils.BroadCastUtil;
import com.framework.app.component.utils.ToastUtil;
import com.tuochebang.service.R;
import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.base.BaseActivity;
import com.tuochebang.service.constant.AppConstant.BroadCastAction;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.entity.ModelType;
import com.tuochebang.service.request.entity.ReturnCarInfo;
import com.tuochebang.service.request.task.EditReturnRequest;
import com.tuochebang.service.request.task.GetPayAndSortMethodRequest;
import com.tuochebang.service.request.task.PublishReturnRequest;
import com.tuochebang.service.ui.LocationMapActivity;
import com.tuochebang.service.ui.SelectItemActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PublishReturnActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    public static final String EXTRAS_INFO = "extras_info";
    public static String EXTRAS_TYPE = LocationMapActivity.EXTRAS_TYPE;
    public static int TYPE_EDIT = 1;
    public static int TYPE_PUBLISH = 0;
    private String date;
    private Button mBtnSure;
    private LatLng mEndLatLng;
    private LatLng mLatlng;
    private ReturnCarInfo mReturnInfo;
    private RelativeLayout mRlBegin;
    private RelativeLayout mRlEnd;
    private RelativeLayout mRlTime;
    private RelativeLayout mRlType;
    private Toolbar mToolBar;
    private TextView mTxtBeginLoc;
    private TextView mTxtEndLoc;
    private TextView mTxtTime;
    private TextView mTxtType;
    private int mType;
    private int mTypeId;
    private int mTypeLoc;
    private List<ModelType> modelTypes = new ArrayList();

    /* renamed from: com.tuochebang.service.ui.returns.PublishReturnActivity$1 */
    class C07501 implements OnClickListener {
        C07501() {
        }

        public void onClick(View v) {
            PublishReturnActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.service.ui.returns.PublishReturnActivity$2 */
    class C07512 implements OnClickListener {
        C07512() {
        }

        public void onClick(View v) {
            PublishReturnActivity.this.mTypeLoc = 0;
            PublishReturnActivity.this.onBtnSelectLocation();
        }
    }

    /* renamed from: com.tuochebang.service.ui.returns.PublishReturnActivity$3 */
    class C07523 implements OnClickListener {
        C07523() {
        }

        public void onClick(View v) {
            PublishReturnActivity.this.mTypeLoc = 1;
            PublishReturnActivity.this.onBtnSelectLocation();
        }
    }

    /* renamed from: com.tuochebang.service.ui.returns.PublishReturnActivity$4 */
    class C07534 implements OnClickListener {
        C07534() {
        }

        public void onClick(View v) {
            PublishReturnActivity.this.showDatePickDialog();
        }
    }

    /* renamed from: com.tuochebang.service.ui.returns.PublishReturnActivity$5 */
    class C07545 implements OnClickListener {
        C07545() {
        }

        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(SelectItemActivity.EXTRAS_CHILDTYPE, SelectItemActivity.CAR_TYPE);
            bundle.putSerializable(SelectItemActivity.EXTRAS_LIST_DATA, (Serializable) PublishReturnActivity.this.modelTypes);
            Intent intent = new Intent();
            intent.setClass(PublishReturnActivity.this, SelectItemActivity.class);
            intent.putExtras(bundle);
            PublishReturnActivity.this.startActivityForResult(intent, SelectItemActivity.SELECT_ITEM_CODE);
        }
    }

    /* renamed from: com.tuochebang.service.ui.returns.PublishReturnActivity$6 */
    class C07556 implements OnClickListener {
        C07556() {
        }

        public void onClick(View v) {
            if (PublishReturnActivity.this.mTxtBeginLoc.getText().toString().equals("输入起点") || PublishReturnActivity.this.mTxtEndLoc.getText().toString().equals("输入终点") || TextUtils.isEmpty(PublishReturnActivity.this.mTxtTime.getText().toString())) {
                ToastUtil.showMessage(MyApplication.getInstance(), "请填写完整后提交");
            } else if (PublishReturnActivity.this.mType == PublishReturnActivity.TYPE_PUBLISH) {
                PublishReturnActivity.this.httpPublishReturnRequest();
            } else if (PublishReturnActivity.this.mType == PublishReturnActivity.TYPE_EDIT) {
                PublishReturnActivity.this.httpEditReturnRequest();
            }
        }
    }

    /* renamed from: com.tuochebang.service.ui.returns.PublishReturnActivity$7 */
    class C07567 implements OnResponseListener<Object> {
        C07567() {
        }

        public void onSucceed(int what, Response<Object> response) {
            if (response.get() != null) {
                ToastUtil.showMessage(MyApplication.getInstance(), "修改成功");
                BroadCastUtil.sendBroadCast(PublishReturnActivity.this.mContext, BroadCastAction.RETURN_CHANGE);
                PublishReturnActivity.this.finish();
            }
        }

        public void onFailed(int what, Response<Object> response) {
            Exception exception = response.getException();
        }

        public void onStart(int what) {
            PublishReturnActivity.this.showCommonProgreessDialog("请稍后..");
        }

        public void onFinish(int what) {
            PublishReturnActivity.this.dismissCommonProgressDialog();
        }
    }

    /* renamed from: com.tuochebang.service.ui.returns.PublishReturnActivity$8 */
    class C07578 implements OnResponseListener<List<ModelType>> {
        C07578() {
        }

        public void onSucceed(int what, Response<List<ModelType>> response) {
            if (response.get() != null) {
                PublishReturnActivity.this.modelTypes = (List) response.get();
                if (PublishReturnActivity.this.mType == PublishReturnActivity.TYPE_PUBLISH) {
                    PublishReturnActivity.this.mTxtType.setText(((ModelType) PublishReturnActivity.this.modelTypes.get(0)).getTypeName());
                    PublishReturnActivity.this.mTypeId = ((ModelType) PublishReturnActivity.this.modelTypes.get(0)).getTypeId();
                }
            }
        }

        public void onFailed(int what, Response<List<ModelType>> response) {
            Exception exception = response.getException();
        }

        public void onStart(int what) {
        }

        public void onFinish(int what) {
        }
    }

    /* renamed from: com.tuochebang.service.ui.returns.PublishReturnActivity$9 */
    class C07589 implements OnResponseListener<Object> {
        C07589() {
        }

        public void onSucceed(int what, Response<Object> response) {
            if (response.get() != null) {
                ToastUtil.showMessage(MyApplication.getInstance(), "发布成功");
                ActivityUtil.next(PublishReturnActivity.this, UserReturnsActivity.class);
                PublishReturnActivity.this.finish();
            }
        }

        public void onFailed(int what, Response<Object> response) {
            Exception exception = response.getException();
        }

        public void onStart(int what) {
            PublishReturnActivity.this.showCommonProgreessDialog("请稍后..");
        }

        public void onFinish(int what) {
            PublishReturnActivity.this.dismissCommonProgressDialog();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_return);
        getIntentExtras();
        initToolBar();
        initView();
        initListener();
        initData();
    }

    private void getIntentExtras() {
        this.mType = getIntent().getIntExtra(EXTRAS_TYPE, TYPE_PUBLISH);
        this.mReturnInfo = (ReturnCarInfo) getIntent().getSerializableExtra(EXTRAS_INFO);
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        if (this.mType == TYPE_EDIT) {
            this.mToolBar.setTitle((CharSequence) "编辑返程车");
        } else {
            this.mToolBar.setTitle((CharSequence) "发布返程车");
        }
        this.mToolBar.setNavigationOnClickListener(new C07501());
    }

    private void initData() {
        httpGetReturnType();
        if (this.mType == TYPE_EDIT) {
            refreshView();
            return;
        }
        this.mTxtTime.setText(new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss").format(new Date(System.currentTimeMillis())));
    }

    private void refreshView() {
        this.mTxtType.setText(this.mReturnInfo.getType());
        this.mTxtTime.setText(this.mReturnInfo.getTime());
        this.mTxtBeginLoc.setText(this.mReturnInfo.getBegin());
        this.mTxtEndLoc.setText(this.mReturnInfo.getEnd());
        this.mLatlng = new LatLng(this.mReturnInfo.getB_latitude(), this.mReturnInfo.getB_longitude());
        this.mEndLatLng = new LatLng(this.mReturnInfo.getE_latitude(), this.mReturnInfo.getE_longitude());
    }

    private void initListener() {
        this.mRlBegin.setOnClickListener(new C07512());
        this.mRlEnd.setOnClickListener(new C07523());
        this.mRlTime.setOnClickListener(new C07534());
        this.mRlType.setOnClickListener(new C07545());
        this.mBtnSure.setOnClickListener(new C07556());
    }

    private void httpEditReturnRequest() {
        RequestQueue queue = NoHttp.newRequestQueue();
        queue.add(0, new EditReturnRequest(ServerUrl.getInst().EDIT_RETURN_REQUEST(), RequestMethod.POST, this.mTypeId, this.mTxtTime.getText().toString(), this.mTxtBeginLoc.getText().toString(), this.mTxtEndLoc.getText().toString(), this.mLatlng.latitude, this.mLatlng.longitude, this.mEndLatLng.latitude, this.mEndLatLng.longitude, String.valueOf(this.mReturnInfo.getReturnId())), new C07567());
    }

    private void initView() {
        this.mRlBegin = (RelativeLayout) findViewById(R.id.tcb_return_begin_loc_rl);
        this.mRlEnd = (RelativeLayout) findViewById(R.id.tcb_return_end_loc_rl);
        this.mTxtBeginLoc = (TextView) findViewById(R.id.tcb_return_begin_loc_txt);
        this.mTxtEndLoc = (TextView) findViewById(R.id.tcb_return_end_loc_txt);
        this.mRlTime = (RelativeLayout) findViewById(R.id.tcb_return_time_rl);
        this.mTxtTime = (TextView) findViewById(R.id.tcb_return_time_txt);
        this.mTxtType = (TextView) findViewById(R.id.tcb_return_type_txt);
        this.mRlType = (RelativeLayout) findViewById(R.id.tcb_return_type_rl);
        this.mBtnSure = (Button) findViewById(R.id.sure);
    }

    private void onBtnSelectLocation() {
        Bundle bundle = new Bundle();
        Intent intent = new Intent();
        intent.setClass(this, LocationMapActivity.class);
        bundle.putInt(LocationMapActivity.EXTRAS_TYPE, this.mTypeLoc);
        intent.putExtras(bundle);
        startActivityForResult(intent, 100);
    }

    private void httpGetReturnType() {
        RequestQueue queue = NoHttp.newRequestQueue();
        queue.add(0, new GetPayAndSortMethodRequest(ServerUrl.getInst().GET_REQUEST_TYPE(), RequestMethod.POST), new C07578());

    }

    private void httpPublishReturnRequest() {
        RequestQueue queue = NoHttp.newRequestQueue();
        queue.add(0, new PublishReturnRequest(ServerUrl.getInst().PUBLISH_RETURN_REQUEST(), RequestMethod.POST, this.mTypeId, this.mTxtTime.getText().toString(), this.mTxtBeginLoc.getText().toString(), this.mTxtEndLoc.getText().toString(), this.mLatlng.latitude, this.mLatlng.longitude, this.mEndLatLng.latitude, this.mEndLatLng.longitude), new C07589());

    }

    private void showTimePickDialog() {
        Calendar now = Calendar.getInstance();
        @SuppressLint("WrongConstant") TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, now.get(11), now.get(12), now.get(13), true);
        timePickerDialog.setThemeDark(true);
        timePickerDialog.vibrate(false);
        timePickerDialog.dismissOnPause(true);
        timePickerDialog.setAccentColor(Color.parseColor("#323334"));
        timePickerDialog.show(getFragmentManager(), "Datepickerdialog");
    }

    private void showDatePickDialog() {
        Calendar now = Calendar.getInstance();
        @SuppressLint("WrongConstant") DatePickerDialog dpd = DatePickerDialog.newInstance(this, now.get(1), now.get(2), now.get(5));
        dpd.setThemeDark(true);
        dpd.vibrate(false);
        dpd.dismissOnPause(true);
        dpd.showYearPickerFirst(true);
        dpd.setAccentColor(Color.parseColor("#323334"));
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 4) {
            String value = data.getStringExtra(LocationMapActivity.EXTRAS_ADDRESS);
            int type = data.getIntExtra(LocationMapActivity.EXTRAS_TYPE, 0);
            LatLng latLng = (LatLng) data.getParcelableExtra("latlng");
            if (type == 0) {
                this.mTxtBeginLoc.setText(value);
                this.mLatlng = latLng;
            } else if (type == 1) {
                this.mTxtEndLoc.setText(value);
                this.mEndLatLng = latLng;
            }
        } else if (resultCode == 2 && requestCode == SelectItemActivity.SELECT_ITEM_CODE) {
            String childType = data.getStringExtra(SelectItemActivity.EXTRAS_CHILDTYPE);
            ModelType selectModel = (ModelType) data.getSerializableExtra(SelectItemActivity.EXTRAS_SELECT_NAME);
            if (childType.equals(SelectItemActivity.CAR_TYPE)) {
                this.mTxtType.setText(selectModel.getTypeName());
                this.mTypeId = selectModel.getTypeId();
            }
        }
    }

    @SuppressLint("WrongConstant")
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        this.date = year + "-" + monthOfYear + "-" + dayOfMonth;
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, year);
        calendar.set(2, monthOfYear);
        calendar.set(5, dayOfMonth);
        showTimePickDialog();
    }

    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        this.mTxtTime.setText(this.date + " " + hourOfDay + ":" + minute + ":" + second);
    }
}
