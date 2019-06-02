package com.tuochebang.service.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.framework.app.component.dialog.CommonNoticeDialog;
import com.framework.app.component.utils.ExitAppUtil;
import com.framework.app.component.utils.ToastUtil;
import com.tuochebang.service.R;
import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.util.DialogUtils;
import com.tuochebang.service.util.ResUtils;

public class BaseActivity extends AppCompatActivity {
    protected String TAG;
    private boolean couldDoubleBackExit;
    private boolean doubleBackExitPressedOnce;
    private long firstTime = 0;
    private ProgressDialog mCommonProgressDialog;
    protected Context mContext;
    private InputMethodManager mInputMethodManager;
    private Dialog progressDialog;

    /* renamed from: com.tuochebang.service.base.BaseActivity$1 */
    class C06581 implements OnKeyListener {
        C06581() {
        }

        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == 4) {
                try {
                    BaseActivity.this.dismissCommonProgressDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }

    @SuppressLint("WrongConstant")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        if (VERSION.SDK_INT >= 19 && VERSION.SDK_INT < 21) {
            getWindow().addFlags(67108864);
        }
        setRequestedOrientation(1);
        this.mInputMethodManager = (InputMethodManager) getSystemService("input_method");
        ExitAppUtil.getInstance().addActivity(this);
        init();
    }

    public void setContentView(int layoutResId) {
        if (VERSION.SDK_INT < 19 || VERSION.SDK_INT >= 21) {
            super.setContentView(layoutResId);
            return;
        }
        ViewGroup rootView = new FrameLayout(this);
        rootView.setLayoutParams(new LayoutParams(-1, -1));
        View contentView = LayoutInflater.from(this).inflate(layoutResId, rootView, false);
        contentView.setFitsSystemWindows(true);
        rootView.addView(contentView);
        View statusBarView = new View(this);
        statusBarView.setLayoutParams(new LayoutParams(-1, ResUtils.getStatusBarHeight(this)));
        statusBarView.setBackgroundColor(ResUtils.getThemeAttrColor(this, R.attr.colorPrimaryDark));
        rootView.addView(statusBarView);
        super.setContentView(rootView);
    }

    private void init() {
        this.TAG = getClass().getSimpleName();
        this.progressDialog = DialogUtils.createProgressDialog(this);
    }

    public void setCouldDoubleBackExit(boolean couldDoubleBackExit) {
        this.couldDoubleBackExit = couldDoubleBackExit;
    }

    public void onBackPressed() {
        if (this.couldDoubleBackExit) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - this.firstTime > 2000) {
                ToastUtil.showMessage(MyApplication.getInstance(), "再按一次返回键关闭程序");
                this.firstTime = secondTime;
                return;
            }
            exit();
            return;
        }
        super.onBackPressed();
    }

    protected boolean hideSoftInput(View v) {
        return this.mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 2);
    }

    public void showToast(String msg) {
        ToastUtil.showMessage(MyApplication.getInstance(), msg);
    }

    protected void exit() {
        ExitAppUtil.getInstance().exit();
    }

    public void showNoticeMsg(String msg) {
    }

    public void showCommonProgreessDialog(String hint) {
        if (this.mCommonProgressDialog == null) {
            this.mCommonProgressDialog = new ProgressDialog(this.mContext);
            this.mCommonProgressDialog.setOnKeyListener(new C06581());
        }
        if (!this.mCommonProgressDialog.isShowing()) {
            this.mCommonProgressDialog.setMessage(hint);
            this.mCommonProgressDialog.show();
        }
    }

    public void dismissCommonProgressDialog() {
        if (this.mCommonProgressDialog != null && this.mCommonProgressDialog.isShowing()) {
            this.mCommonProgressDialog.dismiss();
        }
    }

    public void showChoiceDialog(String title, String content, CommonNoticeDialog.DialogButtonInterface dialogButtonInterface) {
        new CommonNoticeDialog((Context) this, title, content, dialogButtonInterface).show();
    }
}
