package com.tuochebang.service.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.TextView;

import com.framework.app.component.adapter.CommonBaseAdapter;
import com.framework.app.component.utils.DensityUtil;
import com.tuochebang.service.R;


public class SelectedDataAdapter extends CommonBaseAdapter<String> {
    public SelectedDataAdapter(Context context) {
        super(context);
    }

    public View getView(int position, View conrentView, ViewGroup parent) {
        TextView text = new TextView(this.mContext);
        LayoutParams params = new LayoutParams(-1, DensityUtil.dip2px(this.mContext, 48.0f));
        text.setTextSize(2, 20.0f);
        text.setTextColor(this.mContext.getResources().getColor(R.color.txt_title_color));
        text.setText((CharSequence) getList().get(position));
        text.setGravity(16);
        text.setLayoutParams(params);
        text.setPadding(DensityUtil.dip2px(this.mContext, 7.0f), 0, 0, 0);
        return text;
    }
}
