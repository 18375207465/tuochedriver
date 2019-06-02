package com.tuochebang.service.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.framework.app.component.adapter.CommonBaseAdapter;
import com.tuochebang.service.adapter.adapterview.AuthAdapterView;
import com.tuochebang.service.request.entity.Auth;

public class AuthListAdapter extends CommonBaseAdapter<Auth> {
    public AuthListAdapter(Context context) {
        super(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new AuthAdapterView(this.mContext);
        }
        ((AuthAdapterView) convertView).refreshView((Auth) getItem(position));
        return convertView;
    }
}
