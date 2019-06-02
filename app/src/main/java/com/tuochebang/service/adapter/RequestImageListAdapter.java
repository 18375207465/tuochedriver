package com.tuochebang.service.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.framework.app.component.adapter.CommonBaseAdapter;
import com.tuochebang.service.adapter.adapterview.RequestImageAdapterView;

public class RequestImageListAdapter extends CommonBaseAdapter<String> {
    public RequestImageListAdapter(Context context) {
        super(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new RequestImageAdapterView(this.mContext);
        }
        ((RequestImageAdapterView) convertView).refreshView((String) getItem(position));
        return convertView;
    }
}
