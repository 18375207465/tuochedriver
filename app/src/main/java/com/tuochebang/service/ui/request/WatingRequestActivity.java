package com.tuochebang.service.ui.request;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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
import com.tuochebang.service.constant.AppConstant.BroadCastAction;
import com.tuochebang.service.entity.TabEntity;
import java.util.ArrayList;
import java.util.List;

/**
 * 待抢订单
 */
public class WatingRequestActivity extends BaseActivity {
    private CommonPagerAdapter commonPagerAdapter;
    private WatingRequestItemView mDistance;
    private BroadcastReceiver mOrderStatuChangeReceiver;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList();
    private CommonTabLayout mTabLayout;
    private WatingRequestItemView mTime;
    private String[] mTitles = new String[]{"时间", "距离"};
    private Toolbar mToolBar;
    private ViewPager mViewPager;

    /* renamed from: com.tuochebang.service.ui.request.WatingRequestActivity$1 */
    class C07401 extends BroadcastReceiver {
        C07401() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (BroadCastAction.ROB_ORDER_SUCCESS.equals(intent.getAction())) {
                    WatingRequestActivity.this.finish();
                }
            }
        }
    }

    /* renamed from: com.tuochebang.service.ui.request.WatingRequestActivity$2 */
    class C07412 implements OnClickListener {
        C07412() {
        }

        public void onClick(View v) {
            WatingRequestActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.service.ui.request.WatingRequestActivity$3 */
    class C07423 implements OnTabSelectListener {
        C07423() {
        }

        public void onTabSelect(int position) {
            WatingRequestActivity.this.mViewPager.setCurrentItem(position);
        }

        public void onTabReselect(int position) {
        }
    }

    /* renamed from: com.tuochebang.service.ui.request.WatingRequestActivity$4 */
    class C07434 implements OnPageChangeListener {
        C07434() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
            WatingRequestActivity.this.mTabLayout.setCurrentTab(position);
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initToolBar();
        initView();
        registOrderStatuChangeBroadCastReciver();
    }

    private void registOrderStatuChangeBroadCastReciver() {
        this.mOrderStatuChangeReceiver = new C07401();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mOrderStatuChangeReceiver, new IntentFilter(BroadCastAction.ROB_ORDER_SUCCESS));
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C07412());
        this.mToolBar.setTitle((CharSequence) "待抢拖车请求");
    }

    private void initView() {
        for (String tabEntity : this.mTitles) {
            this.mTabEntities.add(new TabEntity(tabEntity));
        }
        this.mTabLayout = (CommonTabLayout) findViewById(R.id.tl_1);
        this.mViewPager = (ViewPager) findViewById(R.id.view_pager);
        this.mTabLayout.setTabData(this.mTabEntities);
        this.mTabLayout.setOnTabSelectListener(new C07423());
        List<View> listView = new ArrayList();
        this.mTime = new WatingRequestItemView(this.mContext);
        this.mTime.setData(WatingRequestItemView.TUOCHE_REQUEST_TIME_STATUS);
        this.mDistance = new WatingRequestItemView(this.mContext);
        this.mDistance.setData(WatingRequestItemView.TUOCHE_RETURN_DISTANCE_STATUS);
        listView.add(this.mTime);
        listView.add(this.mDistance);
        this.commonPagerAdapter = new CommonPagerAdapter();
        this.commonPagerAdapter.setViewList(listView);
        this.mViewPager.setAdapter(this.commonPagerAdapter);
        this.mViewPager.setOnPageChangeListener(new C07434());
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mOrderStatuChangeReceiver != null) {
            LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this.mOrderStatuChangeReceiver);
        }
    }
}
