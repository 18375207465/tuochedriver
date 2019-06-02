package com.tuochebang.service.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.framework.app.component.adapter.CommonBaseAdapter;
import com.tuochebang.service.adapter.adapterview.MyTuocheRequestAdapterView;
import com.tuochebang.service.request.entity.TuocheRequestInfo;

public class TuocheRequestListAdapter extends CommonBaseAdapter<TuocheRequestInfo> {
    public TuocheRequestListAdapter(Context context) {
        super(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new MyTuocheRequestAdapterView(this.mContext);
        }
        ((MyTuocheRequestAdapterView) convertView).refreshView((TuocheRequestInfo) getItem(position));
        return convertView;
    }
}
