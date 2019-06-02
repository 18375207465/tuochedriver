package com.tuochebang.service.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.framework.app.component.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tuochebang.service.R;

import java.util.List;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

public class ImageSimpleBrowsePagerAdapter extends PagerAdapter {
    private Activity mCurrentActivity;
    private List<String> mImageUrlList;
    private LayoutInflater mLayoutInflater;

    /* renamed from: com.tuochebang.service.adapter.ImageSimpleBrowsePagerAdapter$1 */
    class C06411 implements OnViewTapListener {
        C06411() {
        }

        public void onViewTap(View arg0, float arg1, float arg2) {
            if (ImageSimpleBrowsePagerAdapter.this.mCurrentActivity != null) {
                ImageSimpleBrowsePagerAdapter.this.mCurrentActivity.finish();
            }
        }
    }

    @SuppressLint("WrongConstant")
    public ImageSimpleBrowsePagerAdapter(Context context, List<String> imageUrlList) {
        this.mLayoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mImageUrlList = imageUrlList;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.mCurrentActivity = currentActivity;
    }

    public View instantiateItem(ViewGroup container, int position) {
        View view = this.mLayoutInflater.inflate(R.layout.adapter_image_simple_browse_item, container, false);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.photo_view);
        photoView.setOnViewTapListener(new C06411());
        ImageLoader.getInstance().displayImage((String) this.mImageUrlList.get(position), photoView, ImageLoaderUtil.getDisplayImageOptions(R.mipmap.logo));
        container.addView(view, -1, -1);
        return view;
    }

    public int getCount() {
        return this.mImageUrlList.size();
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
