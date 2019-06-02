package com.tuochebang.service.widget.wxphotoselector;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;

import java.util.List;

public abstract class BasePopupWindowForListView<T> extends PopupWindow {
    protected Context context;
    protected View mContentView;
    protected List<T> mDatas;

    /* renamed from: com.tuochebang.user.widget.wxphotoselector.BasePopupWindowForListView$1 */
    class C10781 implements OnTouchListener {
        C10781() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() != 4) {
                return false;
            }
            BasePopupWindowForListView.this.dismiss();
            return true;
        }
    }

    protected abstract void beforeInitWeNeedSomeParams(Object... objArr);

    public abstract void init();

    public abstract void initEvents();

    public abstract void initViews();

    public BasePopupWindowForListView(View contentView, int width, int height, boolean focusable) {
        this(contentView, width, height, focusable, null);
    }

    public BasePopupWindowForListView(View contentView, int width, int height, boolean focusable, List<T> mDatas) {
        this(contentView, width, height, focusable, mDatas, new Object[0]);
    }

    public BasePopupWindowForListView(View contentView, int width, int height, boolean focusable, List<T> mDatas, Object... params) {
        super(contentView, width, height, focusable);
        this.mContentView = contentView;
        this.context = contentView.getContext();
        if (mDatas != null) {
            this.mDatas = mDatas;
        }
        if (params != null && params.length > 0) {
            beforeInitWeNeedSomeParams(params);
        }
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        setOutsideTouchable(true);
        setTouchInterceptor(new C10781());
        initViews();
        initEvents();
        init();
    }

    public View findViewById(int id) {
        return this.mContentView.findViewById(id);
    }

    protected static int dpToPx(Context context, int dp) {
        return (int) ((context.getResources().getDisplayMetrics().density * ((float) dp)) + 0.5f);
    }
}
