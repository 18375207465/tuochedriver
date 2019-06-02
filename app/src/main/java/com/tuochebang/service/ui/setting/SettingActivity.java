package com.tuochebang.service.ui.setting;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.framework.app.component.utils.BroadCastUtil;
import com.framework.app.component.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tuochebang.service.R;
import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.base.BaseActivity;
import com.tuochebang.service.cache.FileUtil;
import com.tuochebang.service.constant.AppConstant.BroadCastAction;
import com.tuochebang.service.util.AndroidUtil;
import com.tuochebang.service.widget.SwitchView.OnStateChangedListener;
import java.text.DecimalFormat;

public class SettingActivity extends BaseActivity {
    private Button mBtnLogout;
    private Switch mOrderSwitchView;
    private RelativeLayout mRlAbout;
    private RelativeLayout mRlClearCache;
    private RelativeLayout mRlFeedBack;
    private Toolbar mToolBar;
    private TextView mTxtClearCache;

    /* renamed from: com.tuochebang.service.ui.setting.SettingActivity$1 */
    class C07771 implements OnClickListener {
        C07771() {
        }

        public void onClick(View v) {
            new ClearCacheAsyncTask().execute(new Void[0]);
        }
    }

    /* renamed from: com.tuochebang.service.ui.setting.SettingActivity$2 */
    class C07782 implements OnClickListener {
        C07782() {
        }

        public void onClick(View v) {
            SettingActivity.this.onBtnLogout();
        }
    }

    /* renamed from: com.tuochebang.service.ui.setting.SettingActivity$3 */
    class C07793 implements OnClickListener {
        C07793() {
        }

        public void onClick(View v) {
        }
    }

    /* renamed from: com.tuochebang.service.ui.setting.SettingActivity$4 */
    class C07804 implements OnClickListener {
        C07804() {
        }

        public void onClick(View v) {
            SettingActivity.this.finish();
        }
    }

    private class ClearCacheAsyncTask extends AsyncTask<Void, Void, Void> {
        private ClearCacheAsyncTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            SettingActivity.this.showCommonProgreessDialog("正在清除中");
            SettingActivity.this.mTxtClearCache.setEnabled(false);
        }

        protected Void doInBackground(Void... params) {
            ImageLoader.getInstance().clearDiskCache();
            AndroidUtil.deleteCacheImages();
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            SettingActivity.this.dismissCommonProgressDialog();
            SettingActivity.this.showToast("清除缓存成功");
            SettingActivity.this.mTxtClearCache.setText("0.0 M");
            SettingActivity.this.mTxtClearCache.setEnabled(true);
        }
    }

    private class DiskCacheSizeAsyncTask extends AsyncTask<Void, Void, String> {
        private DiskCacheSizeAsyncTask() {
        }

        protected String doInBackground(Void... params) {
            String size = "0";
            if (!ImageLoader.getInstance().getDiskCache().getDirectory().isDirectory()) {
                return size;
            }
            size = new DecimalFormat(".##").format(FileUtil.getDirSize(ImageLoader.getInstance().getDiskCache().getDirectory()));
            if (size.startsWith(".")) {
                return "0" + size;
            }
            return size;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            SettingActivity.this.mTxtClearCache.setText(result + "M");
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initToolBar();
        initView();
        initListener();
        initData();
    }

    private void initData() {
        getDiskCacheSizeByAsyncTask();
    }

    private void initListener() {
        this.mRlClearCache.setOnClickListener(new C07771());
        this.mBtnLogout.setOnClickListener(new C07782());
        this.mRlFeedBack.setOnClickListener(new C07793());
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C07804());
    }

    private void initView() {
        this.mRlAbout = (RelativeLayout) findViewById(R.id.tcb_about_us_rl);
        this.mRlClearCache = (RelativeLayout) findViewById(R.id.tcb_clear_cache_rl);
        this.mRlFeedBack = (RelativeLayout) findViewById(R.id.tcb_feed_phone_rl);
        this.mTxtClearCache = (TextView) findViewById(R.id.tcb_clear_cache_txt);
        this.mBtnLogout = (Button) findViewById(R.id.bt_log_out);
        this.mOrderSwitchView = (Switch) findViewById(R.id.switch_order_view);
        this.mOrderSwitchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    private void onBtnLogout() {
        ToastUtil.showMessage(MyApplication.getInstance(), "退出成功");
        MyApplication.getInstance().setUserInfo(null);
        MyApplication.getInstance().setLoginInfo(null);
        BroadCastUtil.sendBroadCast(this.mContext, BroadCastAction.USER_LOGOUT_SUCCESS);
        finish();
    }

    private void getDiskCacheSizeByAsyncTask() {
        new DiskCacheSizeAsyncTask().execute(new Void[0]);
    }
}
