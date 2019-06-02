package com.tuochebang.service.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.framework.app.component.adapter.CommonBaseAdapter;
import com.tuochebang.service.adapter.adapterview.UserTuocheRequestAdapterView;
import com.tuochebang.service.request.entity.TuocheRequestInfo;

public class UserTuocheRequestListAdapter extends CommonBaseAdapter<TuocheRequestInfo> {
    public UserTuocheRequestListAdapter(Context context) {
        super(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new UserTuocheRequestAdapterView(this.mContext);
        }
        ((UserTuocheRequestAdapterView) convertView).refreshView((TuocheRequestInfo) getItem(position));
        return convertView;
    }
}
