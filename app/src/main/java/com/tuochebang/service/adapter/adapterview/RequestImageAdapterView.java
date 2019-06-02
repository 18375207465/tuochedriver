package com.tuochebang.service.adapter.adapterview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.framework.app.component.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tuochebang.service.R;


public class RequestImageAdapterView extends RelativeLayout {
    private Context mContext;
    private ImageView mImg;

    public RequestImageAdapterView(Context context) {
        super(context);
        initView(context);
    }

    public RequestImageAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RequestImageAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        this.mImg = (ImageView) LayoutInflater.from(context).inflate(R.layout.adapter_item_select_pic, this).findViewById(R.id.item_grida_image);
    }

    public void refreshView(String info) {
        ImageLoader.getInstance().displayImage(info, this.mImg, ImageLoaderUtil.getDisplayImageOptions(R.mipmap.logo));
    }
}
