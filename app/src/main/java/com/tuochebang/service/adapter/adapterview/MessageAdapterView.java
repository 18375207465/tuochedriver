package com.tuochebang.service.adapter.adapterview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tuochebang.service.R;
import com.tuochebang.service.request.entity.MessageInfo;

public class MessageAdapterView extends RelativeLayout {
    private Context mContext;
    private TextView mTxtContent;
    private TextView mTxtTime;

    public MessageAdapterView(Context context) {
        super(context);
        initView(context);
    }

    public MessageAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MessageAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_view_message, this);
        this.mTxtTime = (TextView) view.findViewById(R.id.txt_time);
        this.mTxtContent = (TextView) view.findViewById(R.id.txt_content);
    }

    public void refreshView(MessageInfo info) {
        this.mTxtContent.setText(info.getTitle());
        this.mTxtTime.setText(info.getTimestamp());
        if (info.getIsRead() == 1) {
            this.mTxtContent.setTextColor(getResources().getColor(R.color.txt_info_color));
        }
    }
}
