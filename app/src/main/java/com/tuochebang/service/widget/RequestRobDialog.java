package com.tuochebang.service.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuochebang.service.R;

public class RequestRobDialog extends Dialog {
    private LinearLayout mLLChoice;
    private TextView mTvTitle;

    public interface DialogButtonInterface {
        void onDialogButtonClick(DialogResult dialogResult);
    }

    /* renamed from: com.tuochebang.service.widget.RequestRobDialog$1 */
    class C08251 implements View.OnClickListener {
        C08251() {
        }

        public void onClick(View arg0) {
            RequestRobDialog.this.dismiss();
        }
    }

    /* renamed from: com.tuochebang.service.widget.RequestRobDialog$2 */
    class C08262 implements View.OnClickListener {
        C08262() {
        }

        public void onClick(View arg0) {
            RequestRobDialog.this.dismiss();
        }
    }

    public enum DialogResult {
        Yes,
        No
    }

    public RequestRobDialog(Context context, String title, String content, boolean isContentHtml) {
        super(context, R.style.DialogMainFullScreen);
        requestWindowFeature(1);
        setContentView(R.layout.dialog_push_request);
        this.mTvTitle = (TextView) findViewById(R.id.tv_title);
        findViewById(R.id.ll_dismiss).setOnClickListener(new C08251());
        this.mTvTitle.setText(title);
        setCanceledOnTouchOutside(true);
    }

    public RequestRobDialog(Context context, String title, String content) {
        super(context, R.style.DialogMainFullScreen);
        requestWindowFeature(1);
        setContentView(R.layout.dialog_push_request);
        this.mTvTitle = (TextView) findViewById(R.id.tv_title);
        findViewById(R.id.ll_dismiss).setOnClickListener(new C08262());
        this.mTvTitle.setText(title);
        setCanceledOnTouchOutside(true);
    }

    public RequestRobDialog(Context context, String title, String content, final DialogButtonInterface dialogButtonInterface) {
        super(context, R.style.DialogMainFullScreen);
        requestWindowFeature(1);
        setContentView(R.layout.dialog_push_request);
        this.mLLChoice = (LinearLayout) findViewById(R.id.ll_choice);
        this.mTvTitle = (TextView) findViewById(R.id.tv_title);
        this.mLLChoice.setVisibility(View.VISIBLE);
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                RequestRobDialog.this.dismiss();
                dialogButtonInterface.onDialogButtonClick(DialogResult.No);
            }
        });
        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                RequestRobDialog.this.dismiss();
                dialogButtonInterface.onDialogButtonClick(DialogResult.Yes);
            }
        });
        setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface arg0) {
                dialogButtonInterface.onDialogButtonClick(DialogResult.No);
            }
        });
        setCanceledOnTouchOutside(false);
    }
}
