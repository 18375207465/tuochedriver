package com.tuochebang.service.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.framework.app.component.adapter.CommonBaseAdapter;
import com.tuochebang.service.adapter.adapterview.FilterTypeListAdapterView;
import com.tuochebang.service.request.entity.ModelType;

public class FilterTypeListAdapter extends CommonBaseAdapter<ModelType> {
    private ModelType mSelectFilterName = new ModelType();

    public FilterTypeListAdapter(Context context) {
        super(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new FilterTypeListAdapterView(this.mContext);
        }
        ((FilterTypeListAdapterView) convertView).setSelectFilterName(this.mSelectFilterName);
        ((FilterTypeListAdapterView) convertView).refreshView((ModelType) getItem(position));
        return convertView;
    }

    public void setSelectFilterName(ModelType selectFilterName) {
        this.mSelectFilterName = selectFilterName;
        notifyDataSetChanged();
    }
}
