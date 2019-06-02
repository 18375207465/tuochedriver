package com.tuochebang.service.widget.wxphotoselector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;
    protected final int mItemLayoutId;
    protected LayoutInflater mInflater;

    public abstract void convert(ViewHolder viewHolder, T t);

    public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
    }

    public int getCount() {
        return this.mDatas.size();
    }

    public T getItem(int position) {
        return this.mDatas.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = getViewHolder(position, convertView, parent);
        convert(viewHolder, getItem(position));
        return viewHolder.getConvertView();
    }

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(this.mContext, convertView, parent, this.mItemLayoutId, position);
    }
}
