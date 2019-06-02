package com.tuochebang.service.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.framework.app.component.adapter.CommonBaseAdapter;
import com.tuochebang.service.adapter.adapterview.MyReturnCarAdapterView;
import com.tuochebang.service.request.entity.ReturnCarInfo;

public class ReturnCarListAdapter extends CommonBaseAdapter<ReturnCarInfo> {
    public ReturnCarListAdapter(Context context) {
        super(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new MyReturnCarAdapterView(this.mContext);
        }
        ((MyReturnCarAdapterView) convertView).refreshView((ReturnCarInfo) getItem(position));
        return convertView;
    }
}
