package com.tuochebang.service.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.framework.app.component.widget.HackyViewPager;
import com.tuochebang.service.R;
import com.tuochebang.service.adapter.ImageSimpleBrowsePagerAdapter;
import com.tuochebang.service.base.BaseActivity;
import java.util.List;
public class ImageSimpleBrowseActivity extends BaseActivity {
    public static final String EXTAR_CAN_DEL = "can_del";
    public static final String EXTAR_DEL_URL = "del_url";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    public static final String EXTRA_POSITION = "position";
    private int mCurPostion = 0;
    private ImageSimpleBrowsePagerAdapter mImageSimpleBrowsePagerAdapter;
    private List<String> mImageUrlList;
    private LinearLayout mIndicatorViewGroup;
    private boolean mIsImageCanDel;
    private int mLastIndicator = 0;
    private ViewPager mViewPager;

    /* renamed from: com.tuochebang.service.ui.ImageSimpleBrowseActivity$1 */
    class C06611 implements OnPageChangeListener {
        C06611() {
        }

        public void onPageSelected(int position) {
            ImageSimpleBrowseActivity.this.mIndicatorViewGroup.getChildAt(position).setEnabled(true);
            ImageSimpleBrowseActivity.this.mIndicatorViewGroup.getChildAt(ImageSimpleBrowseActivity.this.mLastIndicator).setEnabled(false);
            ImageSimpleBrowseActivity.this.mLastIndicator = position;
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_simple_browse);
        getIntentExtras();
        initActionBar();
        initViews();
    }

    private void initActionBar() {
    }

    private void getIntentExtras() {
        this.mImageUrlList = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
        if (this.mImageUrlList != null && this.mImageUrlList.size() == 0) {
            this.mImageUrlList.add("This is a default image");
        }
        this.mCurPostion = getIntent().getExtras().getInt("position");
        this.mIsImageCanDel = getIntent().getBooleanExtra(EXTAR_CAN_DEL, false);
    }

    private void initViews() {
        this.mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        this.mImageSimpleBrowsePagerAdapter = new ImageSimpleBrowsePagerAdapter(this, this.mImageUrlList);
        this.mImageSimpleBrowsePagerAdapter.setCurrentActivity(this);
        this.mViewPager.setAdapter(this.mImageSimpleBrowsePagerAdapter);
        this.mViewPager.setOnPageChangeListener(new C06611());
        initIndicator(this.mImageUrlList.size());
        this.mViewPager.setCurrentItem(this.mCurPostion);
    }

    private void initIndicator(int pageTotalCount) {
        this.mIndicatorViewGroup = (LinearLayout) findViewById(R.id.layout_indicator);
        if (pageTotalCount > 0) {
            for (int i = 0; i < pageTotalCount; i++) {
                ImageView imageView = new ImageView(this);
                imageView.setPadding(5, 5, 5, 5);
                imageView.setLayoutParams(new LayoutParams(-2, -2));
                imageView.setImageResource(R.drawable.btn_viewpager_dot);
                imageView.setEnabled(false);
                this.mIndicatorViewGroup.addView(imageView);
            }
            this.mIndicatorViewGroup.getChildAt(this.mCurPostion).setEnabled(true);
        }
        if (this.mIndicatorViewGroup.getChildCount() > 0) {
            this.mIndicatorViewGroup.setVisibility(View.VISIBLE);
        }
    }
}
