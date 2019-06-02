package com.tuochebang.service.adapter.adapterview;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.framework.app.component.dialog.CommonNoticeDialog;
import com.framework.app.component.utils.ActivityUtil;
import com.framework.app.component.utils.BroadCastUtil;
import com.framework.app.component.utils.ToastUtil;
import com.tuochebang.service.R;
import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.constant.AppConstant.BroadCastAction;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.entity.ReturnCarInfo;
import com.tuochebang.service.request.task.DeleteReturns;
import com.tuochebang.service.ui.returns.PublishReturnActivity;
import com.tuochebang.service.ui.returns.UserReturnsActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

public class MyReturnCarAdapterView extends RelativeLayout {
    private Context mContext;
    private RelativeLayout mRlDelete;
    private TextView mTxtCompanyName;
    private TextView mTxtDelete;
    private TextView mTxtDestination;
    private TextView mTxtEdit;
    private TextView mTxtGo;
    private TextView mTxtPhoneNum;
    private TextView mTxtPrice;
    private TextView mTxtTime;
    private TextView mTxtType;

    /* renamed from: com.tuochebang.service.adapter.adapterview.MyReturnCarAdapterView$3 */
    class C06493 implements OnResponseListener<Object> {
        C06493() {
        }

        public void onStart(int what) {
            ((UserReturnsActivity) MyReturnCarAdapterView.this.mContext).showCommonProgreessDialog("请稍后...");
        }

        public void onSucceed(int what, Response<Object> response) {
            if (response.get() != null) {
                ToastUtil.showMessage(MyApplication.getInstance(), "删除成功");
                BroadCastUtil.sendBroadCast(MyReturnCarAdapterView.this.mContext, BroadCastAction.RETURN_CHANGE);
            }
        }

        public void onFailed(int what, Response<Object> response) {
        }

        public void onFinish(int what) {
            ((UserReturnsActivity) MyReturnCarAdapterView.this.mContext).dismissCommonProgressDialog();
        }
    }

    public MyReturnCarAdapterView(Context context) {
        super(context);
        initView(context);
    }

    public MyReturnCarAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyReturnCarAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_view_mine_return, this);
        this.mTxtTime = (TextView) view.findViewById(R.id.txt_go_time);
        this.mTxtGo = (TextView) view.findViewById(R.id.txt_from_loc);
        this.mTxtDestination = (TextView) view.findViewById(R.id.txt_destination_loc);
        this.mTxtType = (TextView) view.findViewById(R.id.txt_type);
        this.mRlDelete = (RelativeLayout) view.findViewById(R.id.rl_delete);
        this.mTxtDelete = (TextView) view.findViewById(R.id.txt_btn_delete);
        this.mTxtEdit = (TextView) view.findViewById(R.id.txt_btn_edit);
    }

    public void refreshView(final ReturnCarInfo info) {
        this.mTxtGo.setText(info.getBegin());
        this.mTxtDestination.setText(info.getEnd());
        this.mTxtTime.setText(info.getTime());
        this.mTxtType.setText(info.getType());
        if (info.getStatus() == 0) {
            this.mRlDelete.setVisibility(VISIBLE);
        } else {
            this.mRlDelete.setVisibility(GONE);
        }
        this.mTxtDelete.setOnClickListener(new OnClickListener() {

            /* renamed from: com.tuochebang.service.adapter.adapterview.MyReturnCarAdapterView$1$1 */
            class C06461 implements CommonNoticeDialog.DialogButtonInterface {
                C06461() {
                }

                public void onDialogButtonClick(CommonNoticeDialog.DialogResult dialogResult) {
                    if (dialogResult == CommonNoticeDialog.DialogResult.Yes) {
                        MyReturnCarAdapterView.this.httpDeleteReturn(info.getReturnId());
                    }
                }
            }

            public void onClick(View v) {
                ((UserReturnsActivity) MyReturnCarAdapterView.this.mContext).showChoiceDialog("提示", "是否删除该返程车?", new C06461());
            }
        });
        this.mTxtEdit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(PublishReturnActivity.EXTRAS_TYPE, PublishReturnActivity.TYPE_EDIT);
                bundle.putSerializable(PublishReturnActivity.EXTRAS_INFO, info);
                ActivityUtil.next((UserReturnsActivity) MyReturnCarAdapterView.this.mContext, PublishReturnActivity.class, bundle);
            }
        });
    }

    private void httpDeleteReturn(int returnId) {
        RequestQueue queue = NoHttp.newRequestQueue();
        queue.add(0, new DeleteReturns(ServerUrl.getInst().DELETE_TUOCHE_RETURNS(), RequestMethod.POST, String.valueOf(returnId)), new C06493());
    }
}
