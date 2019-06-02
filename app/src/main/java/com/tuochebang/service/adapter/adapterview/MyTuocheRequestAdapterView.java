package com.tuochebang.service.adapter.adapterview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.framework.app.component.utils.ActivityUtil;
import com.tuochebang.service.R;
import com.tuochebang.service.request.entity.TuocheRequestInfo;
import com.tuochebang.service.ui.request.ApplyTuocheRequestActivity;

public class MyTuocheRequestAdapterView extends RelativeLayout {
    private Context mContext;
    private TextView mTxtApply;
    private TextView mTxtDestination;
    private TextView mTxtEdit;
    private TextView mTxtGo;
    private TextView mTxtPrice;
    private TextView mTxtTime;

    public MyTuocheRequestAdapterView(Context context) {
        super(context);
        initView(context);
    }

    public MyTuocheRequestAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyTuocheRequestAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_view_wating_request, this);
        this.mTxtTime = (TextView) view.findViewById(R.id.txt_go_time);
        this.mTxtGo = (TextView) view.findViewById(R.id.txt_go_location);
        this.mTxtDestination = (TextView) view.findViewById(R.id.txt_destination_location);
        this.mTxtPrice = (TextView) view.findViewById(R.id.txt_price);
        this.mTxtApply = (TextView) view.findViewById(R.id.txt_btn_apply);
    }

    public void refreshView(final TuocheRequestInfo info) {
        this.mTxtGo.setText(info.getBegin());
        this.mTxtDestination.setText(info.getEnd());
        this.mTxtTime.setText(info.getTime());
        this.mTxtPrice.setText(info.getMoney() + "");
        this.mTxtApply.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MyTuocheRequestAdapterView.this.onBtnApply(info);
            }
        });
    }

    private void onBtnApply(TuocheRequestInfo info) {
        Bundle bundle = new Bundle();
        bundle.putString(ApplyTuocheRequestActivity.EXTRAS_REQUST_ID, info.getRequestId());
        ActivityUtil.next((Activity) this.mContext, ApplyTuocheRequestActivity.class, bundle);
    }
}
