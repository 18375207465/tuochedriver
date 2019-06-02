package com.tuochebang.service.ui.register;

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
import com.tuochebang.service.ui.register.admin.AdminRegisterItem;
import com.tuochebang.service.ui.register.driver.DriverRegisterItem;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends BaseActivity {
    private AdminRegisterItem admin;
    private CommonPagerAdapter commonPagerAdapter;
    DriverRegisterItem driver;
    private BroadcastReceiver mRegisterStatuChangeReceiver;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList();
    private CommonTabLayout mTabLayout;
    private String[] mTitles = new String[]{"管理员", "司机"};
    private Toolbar mToolBar;
    private ViewPager mViewPager;

    /* renamed from: com.tuochebang.service.ui.register.RegisterActivity$1 */
    class C06801 extends BroadcastReceiver {
        C06801() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (BroadCastAction.USER_REGISTER_SUCCESS.equals(intent.getAction())) {
                    RegisterActivity.this.finish();
                }
            }
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.RegisterActivity$2 */
    class C06812 implements OnClickListener {
        C06812() {
        }

        public void onClick(View v) {
            RegisterActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.RegisterActivity$3 */
    class C06823 implements OnTabSelectListener {
        C06823() {
        }

        public void onTabSelect(int position) {
            RegisterActivity.this.mViewPager.setCurrentItem(position);
        }

        public void onTabReselect(int position) {
        }
    }

    /* renamed from: com.tuochebang.service.ui.register.RegisterActivity$4 */
    class C06834 implements OnPageChangeListener {
        C06834() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
            RegisterActivity.this.mTabLayout.setCurrentTab(position);
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initToolBar();
        initView();
        registUserStatuChangeBroadCastReciver();
    }

    private void registUserStatuChangeBroadCastReciver() {
        this.mRegisterStatuChangeReceiver = new C06801();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mRegisterStatuChangeReceiver, new IntentFilter(BroadCastAction.USER_REGISTER_SUCCESS));
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C06812());
    }

    private void initView() {
        for (String tabEntity : this.mTitles) {
            this.mTabEntities.add(new TabEntity(tabEntity));
        }
        this.mTabLayout = (CommonTabLayout) findViewById(R.id.tl_1);
        this.mViewPager = (ViewPager) findViewById(R.id.view_pager);
        this.mTabLayout.setTabData(this.mTabEntities);
        this.mTabLayout.setOnTabSelectListener(new C06823());
        List<View> listView = new ArrayList();
        this.admin = new AdminRegisterItem(this.mContext);
        this.driver = new DriverRegisterItem(this.mContext);
        listView.add(this.admin);
        listView.add(this.driver);
        this.commonPagerAdapter = new CommonPagerAdapter();
        this.commonPagerAdapter.setViewList(listView);
        this.mViewPager.setAdapter(this.commonPagerAdapter);
        this.mViewPager.setOnPageChangeListener(new C06834());
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mRegisterStatuChangeReceiver != null) {
            LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this.mRegisterStatuChangeReceiver);
        }
    }
}
