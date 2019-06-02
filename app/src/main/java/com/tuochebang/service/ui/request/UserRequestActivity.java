package com.tuochebang.service.ui.request;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;

import com.framework.app.component.adapter.CommonPagerAdapter;
import com.framework.app.component.view.CommonTabLayout;
import com.framework.app.component.view.listener.CustomTabEntity;
import com.framework.app.component.view.listener.OnTabSelectListener;
import com.tuochebang.service.R;
import com.tuochebang.service.base.BaseActivity;
import com.tuochebang.service.entity.TabEntity;
import java.util.ArrayList;
import java.util.List;

public class UserRequestActivity extends BaseActivity {
    private CommonPagerAdapter commonPagerAdapter;
    private RequestItemView completeItemView;
    private RequestItemView doingItemView;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList();
    private CommonTabLayout mTabLayout;
    private String[] mTitles = new String[]{"待支付", "进行中", "已完成"};
    private Toolbar mToolBar;
    private ViewPager mViewPager;
    private RequestItemView payItem;
    private RequestItemView watingItem;

    /* renamed from: com.tuochebang.service.ui.request.UserRequestActivity$1 */
    class C07351 implements OnTabSelectListener {
        C07351() {
        }

        public void onTabSelect(int position) {
            UserRequestActivity.this.mViewPager.setCurrentItem(position);
        }

        public void onTabReselect(int position) {
        }
    }

    /* renamed from: com.tuochebang.service.ui.request.UserRequestActivity$2 */
    class C07362 implements OnPageChangeListener {
        C07362() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
            UserRequestActivity.this.mTabLayout.setCurrentTab(position);
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    /* renamed from: com.tuochebang.service.ui.request.UserRequestActivity$3 */
    class C07373 implements OnClickListener {
        C07373() {
        }

        public void onClick(View v) {
            UserRequestActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request);
        initToolBar();
        initView();
    }

    private void initView() {
        for (String tabEntity : this.mTitles) {
            this.mTabEntities.add(new TabEntity(tabEntity));
        }
        this.mTabLayout = (CommonTabLayout) findViewById(R.id.tl_1);
        this.mViewPager = (ViewPager) findViewById(R.id.view_pager);
        this.mTabLayout.setTabData(this.mTabEntities);
        this.mTabLayout.setOnTabSelectListener(new C07351());
        List<View> listView = new ArrayList();
        this.payItem = new RequestItemView(this.mContext);
        this.payItem.setData(RequestItemView.TUOCHE_REQUEST_PAY_STATUS);
        this.doingItemView = new RequestItemView(this.mContext);
        this.doingItemView.setData(RequestItemView.TUOCHE_REQUEST_DOING_STATUS);
        this.completeItemView = new RequestItemView(this.mContext);
        this.completeItemView.setData(RequestItemView.TUOCHE_REQUEST_COMPLETE_STATUS);
        listView.add(this.payItem);
        listView.add(this.doingItemView);
        listView.add(this.completeItemView);
        this.commonPagerAdapter = new CommonPagerAdapter();
        this.commonPagerAdapter.setViewList(listView);
        this.mViewPager.setAdapter(this.commonPagerAdapter);
        this.mViewPager.setOnPageChangeListener(new C07362());
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C07373());
    }
}
