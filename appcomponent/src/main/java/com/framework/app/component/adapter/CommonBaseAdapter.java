package com.framework.app.component.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象类 - 程序中通用泛型Adapter，继承Android本身的BaseAdapter
 *
 * @author Xun.Zhang
 * @ClassName: CommonBaseAdapter
 * @date 2014-10-30 上午11:20:00
 */
public abstract class CommonBaseAdapter<T> extends BaseAdapter implements CommonAdapterOptible<T>, View.OnClickListener {
    protected int mPosition;

    protected OnListViewItemClickListener mOnItemClickListener;
    /**
     * LayoutInflater对象（父类可以直接用）
     */
    protected final LayoutInflater mLayoutInflater;

    /**
     * Context对象引用（父类可以直接用）
     */
    protected final Context mContext;

    /**
     * Adapter的列表对象，注意是用final修饰的，被初始化之后，不能重新赋值，便于保证List<T>的唯一性
     */
    private final List<T> mList;

    public CommonBaseAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        mList = new ArrayList<T>();
    }

    @Override
    public int getCount() {
        return (null == mList) ? 0 : mList.size();
    }

    @Override
    public T getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public void setList(List<T> list) {
        mList.clear();
        if (null != list) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public void addList(List<T> list) {
        if (null != list) {
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public void removeItem(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 批量删除item
     */
    public void removeItems(List<Integer> items) {
        for (int i = items.size() - 1; i >= 0; i--) { // 倒序
            if (i <= items.size()) {
                try {
                    mList.remove(items.get(i).intValue());
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
    }


    @Override
    public List<T> getList() {
        return mList;
    }

    public interface OnListViewItemClickListener {
        void onItemClick(View view, Object data, int position);
    }

    public void setOnItemClickListener(OnListViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, getItem(mPosition), mPosition);
        }
    }


}
