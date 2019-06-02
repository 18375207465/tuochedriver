package com.tuochebang.service.adapter.adapterview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.framework.app.component.utils.ActivityUtil;
import com.tuochebang.service.R;
import com.tuochebang.service.constant.AppConstant;
import com.tuochebang.service.request.entity.TuocheRequestInfo;
import com.tuochebang.service.ui.PayActivity;
import com.tuochebang.service.ui.request.UserRequestActivity;
import com.tuochebang.service.util.Tools;
import com.tuochebang.service.widget.RequestChoiceDialog;
import com.tuochebang.service.widget.RequestChoiceDialog.DialogButtonInterface;
import com.tuochebang.service.widget.RequestChoiceDialog.DialogResult;

public class UserTuocheRequestAdapterView extends RelativeLayout {
    private Context mContext;
    private TextView mTxtApply;
    private TextView mTxtCompanyName;
    private TextView mTxtDestination;
    private TextView mTxtEdit;
    private TextView mTxtGo;
    private TextView mTxtPhone;
    private TextView mTxtPrice;
    private TextView mTxtTime;

    /* renamed from: com.tuochebang.service.adapter.adapterview.UserTuocheRequestAdapterView$2 */
    class C06562 implements DialogButtonInterface {
        C06562() {
        }

        public void onDialogButtonClick(DialogResult dialogResult) {
            if (dialogResult == DialogResult.Yes) {
                Tools.callPhone((UserRequestActivity) UserTuocheRequestAdapterView.this.mContext, AppConstant.APP_PHONE);
            }
        }
    }

    public UserTuocheRequestAdapterView(Context context) {
        super(context);
        initView(context);
    }

    public UserTuocheRequestAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public UserTuocheRequestAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_view_mine_request, this);
        this.mTxtTime = (TextView) view.findViewById(R.id.txt_go_time);
        this.mTxtGo = (TextView) view.findViewById(R.id.txt_go_location);
        this.mTxtDestination = (TextView) view.findViewById(R.id.txt_destination_location);
        this.mTxtApply = (TextView) view.findViewById(R.id.txt_btn_operation);
        this.mTxtCompanyName = (TextView) view.findViewById(R.id.txt_company_name);
        this.mTxtPhone = (TextView) view.findViewById(R.id.txt_phone);
        this.mTxtPrice = (TextView) view.findViewById(R.id.txt_price);
    }

    public void refreshView(final TuocheRequestInfo info) {
        this.mTxtGo.setText(info.getBegin());
        this.mTxtDestination.setText(info.getEnd());
        this.mTxtTime.setText(info.getTime());
        this.mTxtPrice.setText(info.getMoney() + "");
        this.mTxtCompanyName.setText(info.getCorporate());
        this.mTxtPhone.setText(info.getMobile());
        if (Integer.valueOf(info.getStatus()).intValue() == 0) {
            this.mTxtApply.setVisibility(VISIBLE);
            this.mTxtApply.setText("取消");
        } else if (Integer.valueOf(info.getStatus()).intValue() == 1) {
            this.mTxtApply.setVisibility(VISIBLE);
            this.mTxtApply.setText("去支付");
        } else if (Integer.valueOf(info.getStatus()).intValue() == 2) {
            this.mTxtApply.setVisibility(GONE);
        } else if (Integer.valueOf(info.getStatus()).intValue() == 3) {
            this.mTxtApply.setVisibility(VISIBLE);
            this.mTxtApply.setText("取消");
        }
        this.mTxtApply.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UserTuocheRequestAdapterView.this.oBtnApply(info.getStatus());
            }
        });
    }

    private void oBtnApply(String status) {
        if (Integer.valueOf(status).intValue() == 1) {
            ActivityUtil.next((UserRequestActivity) this.mContext, PayActivity.class);
        } else if (Integer.valueOf(status).intValue() == 0) {
            new RequestChoiceDialog((UserRequestActivity) this.mContext, "取消订单请拨打客服电话", AppConstant.APP_PHONE, new C06562()).show();
        }
    }
}
