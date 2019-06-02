package com.tuochebang.service.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.framework.app.component.adapter.CommonBaseAdapter;
import com.tuochebang.service.adapter.adapterview.SelectDriverAdapterView;
import com.tuochebang.service.adapter.adapterview.SelectDriverAdapterView.onCheckListener;
import com.tuochebang.service.request.entity.Trailer;

public class SelectDriverListAdapter extends CommonBaseAdapter<Trailer> {
    private Trailer mSelectFilterName = new Trailer();

    /* renamed from: com.tuochebang.service.adapter.SelectDriverListAdapter$1 */
    class C06421 implements onCheckListener {
        C06421() {
        }

        public void checkListener() {
            SelectDriverListAdapter.this.notifyDataSetChanged();
        }
    }

    public SelectDriverListAdapter(Context context) {
        super(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new SelectDriverAdapterView(this.mContext);
        }
        ((SelectDriverAdapterView) convertView).setSelectFilterName(this.mSelectFilterName);
        ((SelectDriverAdapterView) convertView).refreshView((Trailer) getItem(position), position);
        ((SelectDriverAdapterView) convertView).setOnCheckListener(new C06421());
        return convertView;
    }

    public void setSelectFilterName(Trailer selectFilterName) {
        this.mSelectFilterName = selectFilterName;
        notifyDataSetChanged();
    }
}
