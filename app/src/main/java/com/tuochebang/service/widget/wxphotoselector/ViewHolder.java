package com.tuochebang.service.widget.wxphotoselector;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class ViewHolder {
    private View mConvertView;
    private int mPosition;
    private final SparseArray<View> mViews = new SparseArray();

    private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mPosition = position;
        this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        this.mConvertView.setTag(this);
    }

    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.mPosition = position;
        return holder;
    }

    public View getConvertView() {
        return this.mConvertView;
    }

    public <T extends View> T getView(int viewId) {
        View view = (View) this.mViews.get(viewId);
        if (view != null) {
            return (T) view;
        }
        view = this.mConvertView.findViewById(viewId);
        this.mViews.put(viewId, view);
        return (T) view;
    }

    public ViewHolder setText(int viewId, String text) {
        ((TextView) getView(viewId)).setText(text);
        return this;
    }

    public ViewHolder setImageResource(int viewId, int drawableId) {
        ((ImageView) getView(viewId)).setImageResource(drawableId);
        return this;
    }

    public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ((ImageView) getView(viewId)).setImageBitmap(bm);
        return this;
    }

    public ViewHolder setImageByUrl(int viewId, String url) {
        ImageLoader.getInstance().displayImage("file://" + url, (ImageView) getView(viewId));
        return this;
    }

    public int getPosition() {
        return this.mPosition;
    }
}
