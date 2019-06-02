package com.tuochebang.service.adapter.adapterview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tuochebang.service.R;
import com.tuochebang.service.request.entity.ModelType;

public class FilterTypeListAdapterView extends RelativeLayout {
    private static final int COLOR_CHECKED = Color.parseColor("#ff6666");
    private static final int COLOR_NORMAL = Color.parseColor("#333333");
    private static final String TAG = FilterTypeListAdapterView.class.getSimpleName();
    private ModelType mSelectFilterName = new ModelType();
    private TextView mTvFilterName;

    public FilterTypeListAdapterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }

    public FilterTypeListAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public FilterTypeListAdapterView(Context context) {
        super(context);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.adapter_view_filter_type_list, this);
        this.mTvFilterName = (TextView) findViewById(R.id.tv_filter_name);
    }

    public void refreshView(ModelType childType) {
        this.mTvFilterName.setText(childType.getTypeName());
        if (childType.getTypeName().equalsIgnoreCase(this.mSelectFilterName.getTypeName())) {
            this.mTvFilterName.setTextColor(COLOR_CHECKED);
        } else {
            this.mTvFilterName.setTextColor(COLOR_NORMAL);
        }
    }

    public void setSelectFilterName(ModelType selectFilterName) {
        this.mSelectFilterName = selectFilterName;
    }
}
