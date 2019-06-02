package com.tuochebang.service.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.framework.app.component.adapter.CommonBaseAdapter;
import com.tuochebang.service.adapter.adapterview.TuocheManagerAdapterView;
import com.tuochebang.service.request.entity.Trailer;

public class TuocheManagerListAdapter extends CommonBaseAdapter<Trailer> {
    public TuocheManagerListAdapter(Context context) {
        super(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new TuocheManagerAdapterView(this.mContext);
        }
        ((TuocheManagerAdapterView) convertView).refreshView((Trailer) getItem(position));
        return convertView;
    }
}
