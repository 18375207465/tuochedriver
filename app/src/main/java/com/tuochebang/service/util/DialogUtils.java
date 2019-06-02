package com.tuochebang.service.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog.Builder;

import com.tuochebang.service.R;


public class DialogUtils {
    public static Dialog createProgressDialog(Context context) {
        return createProgressDialog(context, true);
    }

    public static Dialog createProgressDialog(Context context, boolean needCancle) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading");
        dialog.setCancelable(needCancle);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static Dialog showCommonDialog(Context context, String message, OnClickListener listener) {
        return new Builder(context).setMessage((CharSequence) message).setPositiveButton(context.getString(R.string.dialog_positive), listener).setNegativeButton(context.getString(R.string.dialog_negative), null).show();
    }

    public static Dialog showConfirmDialog(Context context, String message, OnClickListener listener) {
        return new Builder(context).setMessage((CharSequence) message).setPositiveButton(context.getString(R.string.dialog_positive), listener).show();
    }
}
