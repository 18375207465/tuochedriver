package com.framework.app.component.dialog;

import com.framework.app.component.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 通用提示对话框
 *
 * @author bevis.zeng version 2.0.2 2015-4-16
 */
public class CommonNoticeDialog extends Dialog {
    private ImageView mIcon;
    private TextView mTvTitle;
    private TextView mTvContent;
    private LinearLayout mLLChoice;

    public enum DIALOG_TYPE {
        WARN, SURE, CHOICE
    }

    //内容为html的提示框
    public CommonNoticeDialog(Context context, DIALOG_TYPE iconType,
                              String title, String content, boolean isContentHtml) {
        super(context, R.style.DialogMainFullScreen);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 灭掉对话框标题，要放在setContentView前面否则会报错
        setContentView(R.layout.dialog_common_notice);
        mIcon = (ImageView) findViewById(R.id.iv_icon);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvContent = (TextView) findViewById(R.id.tv_content);
        findViewById(R.id.ll_dismiss).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        dismiss();
                    }
                });
        mIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

            }
        });
        switch (iconType) {
            case WARN:
                mIcon.setImageResource(R.drawable.popup_tips_fail);
                break;
            case SURE:
                mIcon.setImageResource(R.drawable.popup_tips_success);
                break;
            default:
                mIcon.setImageResource(R.drawable.popup_tips_success);
                break;
        }
        mTvTitle.setText(title);
        if (isContentHtml) {
            mTvContent.setText(Html.fromHtml(content));
        } else {
            mTvContent.setText(content);
        }
        setCanceledOnTouchOutside(true);
    }

    //内容为String的提示框
    public CommonNoticeDialog(Context context, DIALOG_TYPE iconType,
                              String title, String content) {
        super(context, R.style.DialogMainFullScreen);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 灭掉对话框标题，要放在setContentView前面否则会报错
        setContentView(R.layout.dialog_common_notice);
        mIcon = (ImageView) findViewById(R.id.iv_icon);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvContent = (TextView) findViewById(R.id.tv_content);
        findViewById(R.id.ll_dismiss).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        dismiss();
                    }
                });
        mIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

            }
        });
        switch (iconType) {
            case WARN:
                mIcon.setImageResource(R.drawable.popup_tips_fail);
                break;
            case SURE:
                mIcon.setImageResource(R.drawable.popup_tips_success);
                break;
            default:
                mIcon.setImageResource(R.drawable.popup_tips_success);
                break;
        }
        mTvTitle.setText(title);
        mTvContent.setText(content);
        setCanceledOnTouchOutside(true);
    }

    public CommonNoticeDialog(Context context, DIALOG_TYPE iconType,
                              String title, String content, final DialogButtonInterface dialogButtonInterface) {
        super(context, R.style.DialogMainFullScreen);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 灭掉对话框标题，要放在setContentView前面否则会报错
        setContentView(R.layout.dialog_common_notice);
        mIcon = (ImageView) findViewById(R.id.iv_icon);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvContent = (TextView) findViewById(R.id.tv_content);
        findViewById(R.id.ll_dismiss).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        dialogButtonInterface.onDialogButtonClick(DialogResult.Yes);
                        dismiss();
                    }
                });
        mIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

            }
        });
        switch (iconType) {
            case WARN:
                mIcon.setImageResource(R.drawable.popup_tips_fail);
                break;
            case SURE:
                mIcon.setImageResource(R.drawable.popup_tips_success);
                break;
            default:
                mIcon.setImageResource(R.drawable.popup_tips_success);
                break;
        }
        mTvTitle.setText(title);
        mTvContent.setText(content);
        setCanceledOnTouchOutside(true);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialogButtonInterface.onDialogButtonClick(DialogResult.Yes);
            }
        });
    }

    //带取消和确定按钮的对话框
    public CommonNoticeDialog(Context context, String title, String content,
                              final DialogButtonInterface dialogButtonInterface) {
        super(context, R.style.DialogMainFullScreen);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 灭掉对话框标题，要放在setContentView前面否则会报错
        setContentView(R.layout.dialog_common_notice);
        mLLChoice = (LinearLayout) findViewById(R.id.ll_choice);
        mIcon = (ImageView) findViewById(R.id.iv_icon);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvContent = (TextView) findViewById(R.id.tv_content);
        mLLChoice.setVisibility(View.VISIBLE);
        mIcon.setImageResource(R.drawable.popup_tips_inquire);
        mTvTitle.setText(title);
        mTvContent.setText(content);
        findViewById(R.id.btn_cancel).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        dismiss();
                        dialogButtonInterface
                                .onDialogButtonClick(DialogResult.No);
                    }
                });
        findViewById(R.id.btn_ok).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        dismiss();
                        dialogButtonInterface
                                .onDialogButtonClick(DialogResult.Yes);
                    }
                });
        setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                dialogButtonInterface.onDialogButtonClick(DialogResult.No);
            }
        });
        setCanceledOnTouchOutside(false);
    }

    public enum DialogResult {
        Yes, No
    }

    public interface DialogButtonInterface {
        void onDialogButtonClick(DialogResult dialogResult);
    }

}
