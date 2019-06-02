package com.tuochebang.service.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuochebang.service.R;

public class RequestChoiceDialog extends Dialog {
    private ImageView mIcon;
    private LinearLayout mLLChoice;
    private TextView mTvContent;
    private TextView mTvTitle;

    public interface DialogButtonInterface {
        void onDialogButtonClick(DialogResult dialogResult);
    }

    /* renamed from: com.tuochebang.service.widget.RequestChoiceDialog$1 */
    class C08181 implements View.OnClickListener {
        C08181() {
        }

        public void onClick(View arg0) {
            RequestChoiceDialog.this.dismiss();
        }
    }

    /* renamed from: com.tuochebang.service.widget.RequestChoiceDialog$2 */
    class C08192 implements View.OnClickListener {
        C08192() {
        }

        public void onClick(View arg0) {
        }
    }

    /* renamed from: com.tuochebang.service.widget.RequestChoiceDialog$3 */
    class C08203 implements View.OnClickListener {
        C08203() {
        }

        public void onClick(View arg0) {
            RequestChoiceDialog.this.dismiss();
        }
    }

    /* renamed from: com.tuochebang.service.widget.RequestChoiceDialog$4 */
    class C08214 implements View.OnClickListener {
        C08214() {
        }

        public void onClick(View arg0) {
        }
    }

    public enum DialogResult {
        Yes,
        No
    }

    public RequestChoiceDialog(Context context, String title, String content, boolean isContentHtml) {
        super(context, R.style.DialogMainFullScreen);
        requestWindowFeature(1);
        setContentView(R.layout.dialog_choice_request);
        this.mIcon = (ImageView) findViewById(R.id.iv_icon);
        this.mTvTitle = (TextView) findViewById(R.id.tv_title);
        this.mTvContent = (TextView) findViewById(R.id.tv_content);
        findViewById(R.id.ll_dismiss).setOnClickListener(new C08181());
        this.mIcon.setOnClickListener(new C08192());
        this.mTvTitle.setText(title);
        if (isContentHtml) {
            this.mTvContent.setText(Html.fromHtml(content));
        } else {
            this.mTvContent.setText(content);
        }
        setCanceledOnTouchOutside(true);
    }

    public RequestChoiceDialog(Context context, String title, String content) {
        super(context, R.style.DialogMainFullScreen);
        requestWindowFeature(1);
        setContentView(R.layout.dialog_choice_request);
        this.mIcon = (ImageView) findViewById(R.id.iv_icon);
        this.mTvTitle = (TextView) findViewById(R.id.tv_title);
        this.mTvContent = (TextView) findViewById(R.id.tv_content);
        findViewById(R.id.ll_dismiss).setOnClickListener(new C08203());
        this.mIcon.setOnClickListener(new C08214());
        this.mTvTitle.setText(title);
        this.mTvContent.setText(content);
        setCanceledOnTouchOutside(true);
    }

    public RequestChoiceDialog(Context context, String title, String content, final DialogButtonInterface dialogButtonInterface) {
        super(context, R.style.DialogMainFullScreen);
        requestWindowFeature(1);
        setContentView(R.layout.dialog_choice_request);
        this.mLLChoice = (LinearLayout) findViewById(R.id.ll_choice);
        this.mIcon = (ImageView) findViewById(R.id.iv_icon);
        this.mTvTitle = (TextView) findViewById(R.id.tv_title);
        this.mTvContent = (TextView) findViewById(R.id.tv_content);
        this.mLLChoice.setVisibility(View.VISIBLE);
        this.mTvTitle.setText(title);
        this.mTvContent.setText(content);
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                RequestChoiceDialog.this.dismiss();
                dialogButtonInterface.onDialogButtonClick(DialogResult.No);
            }
        });
        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                RequestChoiceDialog.this.dismiss();
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
