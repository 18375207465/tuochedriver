package com.tuochebang.service.ui.returns;

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

public class UserReturnsActivity extends BaseActivity {
    private CommonPagerAdapter commonPagerAdapter;
    private ReturnsItemView completeItemView;
    private ReturnsItemView doingItemView;
    private BroadcastReceiver mReturnStatuChangeReceiver;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList();
    private CommonTabLayout mTabLayout;
    private String[] mTitles = new String[]{"待抢单", "进行中", "已完成"};
    private Toolbar mToolBar;
    private ViewPager mViewPager;
    private ReturnsItemView watingItem;

    /* renamed from: com.tuochebang.service.ui.returns.UserReturnsActivity$1 */
    class C07731 extends BroadcastReceiver {
        C07731() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (BroadCastAction.RETURN_CHANGE.equals(intent.getAction())) {
                    UserReturnsActivity.this.watingItem.setData(ReturnsItemView.TUOCHE_RETURN_WAIT_STATUS);
                }
            }
        }
    }

    /* renamed from: com.tuochebang.service.ui.returns.UserReturnsActivity$2 */
    class C07742 implements OnTabSelectListener {
        C07742() {
        }

        public void onTabSelect(int position) {
            UserReturnsActivity.this.mViewPager.setCurrentItem(position);
        }

        public void onTabReselect(int position) {
        }
    }

    /* renamed from: com.tuochebang.service.ui.returns.UserReturnsActivity$3 */
    class C07753 implements OnPageChangeListener {
        C07753() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
            UserReturnsActivity.this.mTabLayout.setCurrentTab(position);
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    /* renamed from: com.tuochebang.service.ui.returns.UserReturnsActivity$4 */
    class C07764 implements OnClickListener {
        C07764() {
        }

        public void onClick(View v) {
            UserReturnsActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_returns);
        initToolBar();
        initView();
        registReturnStatuChangeBroadCastReciver();
    }

    private void registReturnStatuChangeBroadCastReciver() {
        this.mReturnStatuChangeReceiver = new C07731();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mReturnStatuChangeReceiver, new IntentFilter(BroadCastAction.RETURN_CHANGE));
    }

    private void initView() {
        for (String tabEntity : this.mTitles) {
            this.mTabEntities.add(new TabEntity(tabEntity));
        }
        this.mTabLayout = (CommonTabLayout) findViewById(R.id.tl_1);
        this.mViewPager = (ViewPager) findViewById(R.id.view_pager);
        this.mTabLayout.setTabData(this.mTabEntities);
        this.mTabLayout.setOnTabSelectListener(new C07742());
        List<View> listView = new ArrayList();
        this.watingItem = new ReturnsItemView(this.mContext);
        this.watingItem.setData(ReturnsItemView.TUOCHE_RETURN_WAIT_STATUS);
        this.doingItemView = new ReturnsItemView(this.mContext);
        this.doingItemView.setData(ReturnsItemView.TUOCHE_RETURN_DOING_STATUS);
        this.completeItemView = new ReturnsItemView(this.mContext);
        this.completeItemView.setData(ReturnsItemView.TUOCHE_RETURN_COMPLETE_STATUS);
        listView.add(this.watingItem);
        listView.add(this.doingItemView);
        listView.add(this.completeItemView);
        this.commonPagerAdapter = new CommonPagerAdapter();
        this.commonPagerAdapter.setViewList(listView);
        this.mViewPager.setAdapter(this.commonPagerAdapter);
        this.mViewPager.setOnPageChangeListener(new C07753());
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C07764());
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mReturnStatuChangeReceiver != null) {
            LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this.mReturnStatuChangeReceiver);
        }
    }
}
