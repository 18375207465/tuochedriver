package com.framework.app.component.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;

import com.framework.app.component.crouton.Crouton;
import com.framework.app.component.crouton.Style;
import com.framework.app.component.dialog.CommonNoticeDialog;
import com.framework.app.component.dialog.CommonNoticeDialog.DIALOG_TYPE;
import com.framework.app.component.dialog.CommonNoticeDialog.DialogButtonInterface;
import com.framework.app.component.utils.ToastUtil;

/**
 * Fragment基类
 *
 * @author Xun.Zhang
 * @ClassName: BaseFragment.java
 * @date 2014-12-8 上午11:15:12
 */
public abstract class BaseFragment extends Fragment {

    /**
     * TAG为每一个Activity的类名
     */
    protected String TAG = BaseFragment.class.getSimpleName();

    private ProgressDialog mProgressDialog;

    protected boolean mHasInitialLoaded;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = this.getClass().getSimpleName();
    }

    public void onResume() {
        super.onResume();

//		MobclickAgent.onPageStart(TAG); // 统计页面
    }

    public void onPause() {
        super.onPause();

        //MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        if (this.getView() != null) {
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 显示ProgressDialog
     *
     * @param hint 要显示的文字
     */
    public void showProgreessDialog(String hint) {
        if (null == mProgressDialog) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        try {
                            dismissProgressDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return false;
                }
            });
        }

        if (!mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(hint);
            mProgressDialog.show();
        }

    }

    /**
     * 关闭ProgressDialog
     */
    public void dismissProgressDialog() {
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 显示系统Toast
     *
     * @param msg
     */
    public void showToastMsg(String msg) {
        ToastUtil.showMessageLong(getActivity(), msg);
    }

    /**
     * 显示成功或确认提示框
     *
     * @param msg
     */
    public void showSureMsg(String title, String msg) {
        new CommonNoticeDialog(getActivity(), DIALOG_TYPE.SURE, title, msg).show();
    }

    /**
     * 显示警告信息
     *
     * @param msg
     */
    public void showWarnMsg(String msg) {
        new CommonNoticeDialog(getActivity(), DIALOG_TYPE.WARN, "失败", msg).show();
    }

    /**
     * 显示警告信息
     *
     * @param msg
     */
    public void showWarnMsg(String title, String msg) {
        new CommonNoticeDialog(getActivity(), DIALOG_TYPE.WARN, title, msg).show();
    }

    /**
     * 显示提示信息
     *
     * @param msg
     */
    public void showNoticeMsg(String msg) {
        new CommonNoticeDialog(getActivity(), DIALOG_TYPE.WARN, "提示", msg).show();
    }

    /**
     * 显示带取消和确定按钮的提示框
     */
    public void showChoiceDialog(String title, String content, DialogButtonInterface dialogButtonInterface) {
        new CommonNoticeDialog(getActivity(), title, content, dialogButtonInterface).show();
    }

    /**
     * 显示Crouton提示
     *
     * @param msg
     */
    public void showCroutonMsg(String msg) {
        Crouton.makeText(getActivity(), msg, Style.ALERT).show();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()
                && !isHasInitialLoaded()
                && isPageLazyLoad()) {
            doOnInitialLoad();
        }
    }

    protected boolean isPageLazyLoad() {
        return false;
    }

    protected abstract void doOnInitialLoad();

    private boolean isHasInitialLoaded() {
        return mHasInitialLoaded;
    }


}