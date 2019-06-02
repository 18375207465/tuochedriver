package com.tuochebang.service.adapter.adapterview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.framework.app.component.dialog.CommonNoticeDialog;
import com.framework.app.component.utils.BroadCastUtil;
import com.framework.app.component.utils.ToastUtil;
import com.tuochebang.service.R;
import com.tuochebang.service.constant.AppConstant.BroadCastAction;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.entity.Auth;
import com.tuochebang.service.request.task.AuthDriverRequest;
import com.tuochebang.service.ui.user.UserAuthActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

public class AuthAdapterView extends RelativeLayout {
    private Context mContext;
    private TextView mTxtAuth;
    private TextView mTxtDriverName;
    private TextView mTxtDriverPhone;
    private TextView mTxtTime;

    /* renamed from: com.tuochebang.service.adapter.adapterview.AuthAdapterView$2 */
    class C06452 implements OnResponseListener<Object> {
        C06452() {
        }

        public void onSucceed(int what, Response<Object> response) {
            ToastUtil.showMessage(AuthAdapterView.this.mContext.getApplicationContext(), "操作成功");
            BroadCastUtil.sendBroadCast((UserAuthActivity) AuthAdapterView.this.mContext, BroadCastAction.AUTH_CHANGE);
        }

        public void onFailed(int what, Response<Object> response) {
            Exception exception = response.getException();
        }

        public void onStart(int what) {
            ((UserAuthActivity) AuthAdapterView.this.mContext).showCommonProgreessDialog("請稍後...");
        }

        public void onFinish(int what) {
            ((UserAuthActivity) AuthAdapterView.this.mContext).dismissCommonProgressDialog();
        }
    }

    public AuthAdapterView(Context context) {
        super(context);
        initView(context);
    }

    public AuthAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AuthAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_view_auth_item, this);
        this.mTxtTime = (TextView) view.findViewById(R.id.txt_apply_time);
        this.mTxtDriverName = (TextView) view.findViewById(R.id.txt_driver_name);
        this.mTxtDriverPhone = (TextView) view.findViewById(R.id.txt_driver_phone);
        this.mTxtAuth = (TextView) view.findViewById(R.id.txt_btn_auth);
    }

    public void refreshView(final Auth info) {
        this.mTxtDriverName.setText(info.getName());
        this.mTxtTime.setText(info.getTimestamp());
        this.mTxtDriverPhone.setText(info.getMobile());
        this.mTxtAuth.setOnClickListener(new OnClickListener() {

            /* renamed from: com.tuochebang.service.adapter.adapterview.AuthAdapterView$1$1 */
            class C06431 implements CommonNoticeDialog.DialogButtonInterface {
                C06431() {
                }

                public void onDialogButtonClick(CommonNoticeDialog.DialogResult dialogResult) {
                    if (dialogResult == CommonNoticeDialog.DialogResult.Yes) {
                        AuthAdapterView.this.httpAuthDriver(String.valueOf(info.getAuthId()), "1");
                    } else if (dialogResult == CommonNoticeDialog.DialogResult.No) {
                        AuthAdapterView.this.httpAuthDriver(String.valueOf(info.getAuthId()), "2");
                    }
                }
            }

            public void onClick(View v) {
                ((UserAuthActivity) AuthAdapterView.this.mContext).showChoiceDialog("提示", "请选择是否授权该用户", new C06431());
            }
        });
    }

    private void httpAuthDriver(String id, String auth) {
        RequestQueue queue = NoHttp.newRequestQueue();
        AuthDriverRequest request = new AuthDriverRequest(ServerUrl.getInst().GET_AUTH_DRIVER_REQUST(), RequestMethod.POST, id, auth);
        request.setmBeanClass(Object.class, 0);
        queue.add(0, request, new C06452());
    }
}
