package com.tuochebang.service;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.framework.app.component.utils.ToastUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Inspired by Grant, rebuild by KYANGC on 15-12-23
 */
public class PermissionHelper {

    private static final String TAG = "PermissionHelper";

    private static PermissionHelper mInstance = null;

    private final Set<String> mPendingRequests = new HashSet<>(1);

    private final Set<String> mPermissions = new HashSet<>(1);

    private final Map<String, Integer> mRequestCountMap = new HashMap<>();

    private final List<PermissionsResultAction> mPendingActions = new ArrayList<>(1);

    private PermissionHelper() {
        initializePermissionsMap();
    }

    public static PermissionHelper getInstance() {
        if (mInstance == null) {
            mInstance = new PermissionHelper();
        }
        return mInstance;
    }

    /**
     * 初始化，通过反射获取到设备上所有的权限。
     */
    private synchronized void initializePermissionsMap() {
        Field[] fields = Manifest.permission.class.getFields();
        for (Field field : fields) {
            String name = null;
            try {
                name = (String) field.get("");
            } catch (IllegalAccessException e) {
                //  P.e(e, "Could not access field");
            }
            mPermissions.add(name);
        }
    }

    /**
     * 获取到所有App声明的权限。
     */
    @NonNull
    private synchronized String[] getManifestPermissions(@NonNull final Activity activity) {
        PackageInfo packageInfo = null;
        List<String> list = new ArrayList<>(1);
        try {
            packageInfo = activity.getPackageManager()
                    .getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            //P.e("A problem occurred when retrieving permissions", e);
        }
        if (packageInfo != null) {
            String[] permissions = packageInfo.requestedPermissions;
            if (permissions != null) {
                for (String perm : permissions) {
                    // P.d("Manifest contained permission: " + perm);
                    list.add(perm);
                }
            }
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * 将permission和pendingAction绑定在一起。
     */
    private synchronized void addPendingAction(@NonNull String[] permissions,
                                               @Nullable PermissionsResultAction action) {
        if (action == null) {
            return;
        }
        action.registerPermissions(permissions);
        mPendingActions.add(action);
    }

    /**
     * 从pendingAction列表中移除某一Action。
     */
    private synchronized void removePendingAction(@Nullable PermissionsResultAction action) {
        for (Iterator<PermissionsResultAction> iterator = mPendingActions.iterator();
             iterator.hasNext(); ) {
            PermissionsResultAction weakRef = iterator.next();
            if (weakRef == action || weakRef == null) {
                iterator.remove();
            }
        }
    }

    /**
     * 在Android M之前请求权限。
     */
    private void doPermissionWorkBeforeAndroidM(@NonNull Activity activity,
                                                @NonNull String[] permissions,
                                                @Nullable PermissionsResultAction action) {
        for (String perm : permissions) {
            if (action != null) {
                if (!mPermissions.contains(perm)) {
                    action.onResult(perm, Permissions.NotFound);
                } else if (ActivityCompat.checkSelfPermission(activity, perm)
                        != PackageManager.PERMISSION_GRANTED) {
                    action.onResult(perm, Permissions.Denied);
                } else {
                    action.onResult(perm, Permissions.Granted);
                }
            }
        }
    }

    /**
     * 获取需要执行权限请求的权限列表。
     */
    @NonNull
    private List<String> getPermissionsListToRequest(@NonNull Activity activity,
                                                     @NonNull String[] permissions,
                                                     @Nullable PermissionsResultAction action) {
        List<String> permList = new ArrayList<>(permissions.length);
        for (String perm : permissions) {
            if (!mPermissions.contains(perm)) {
                if (action != null) {
                    action.onResult(perm, Permissions.NotFound);
                }
            } else if (ActivityCompat.checkSelfPermission(activity, perm)
                    != PackageManager.PERMISSION_GRANTED) {
                if (!mPendingRequests.contains(perm)) {
                    permList.add(perm);
                }
            } else {
                if (action != null) {
                    action.onResult(perm, Permissions.Granted);
                }
            }
        }
        return permList;
    }

    /**
     * Increase permission requiring count during a life cycle
     */
    private void increaseRequiringCount(String[] permissions) {
        for (String permission : permissions) {
            if (mRequestCountMap.containsKey(permission)) {
                mRequestCountMap.put(permission, mRequestCountMap.get(permission) + 1);
            } else {
                mRequestCountMap.put(permission, 1);
            }
        }
    }

    /**
     * Do requiring permission base on a permission request builder
     */
    private synchronized void requestPermissions(final PermissionRequestBuilder builder) {
        if (builder == null || builder.mContext == null || builder.mAction == null
                || builder.mPermissionList == null || builder.mPermissionList.size() < 1) {
            return;
        }

        builder.mAction.setDeniedAlertType(builder.mDeniedAlertType);
        final String[] permissions = builder.mPermissionList
                .toArray(new String[builder.mPermissionList.size()]);
        addPendingAction(permissions, builder.mAction);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            doPermissionWorkBeforeAndroidM(builder.mContext, permissions, builder.mAction);
            removePendingAction(builder.mAction);
        } else {
            List<String> permList = getPermissionsListToRequest(builder.mContext, permissions,
                    builder.mAction);
            if (permList.isEmpty()) {
                removePendingAction(builder.mAction);
            } else {
                final String[] permsToRequest = permList.toArray(new String[permList.size()]);
                mPendingRequests.addAll(permList);
                builder.mAction.bindActivity(builder.mContext);
                boolean hasRequiredBefore = false;
                int requiredCount = 0;
                for (String perm : permsToRequest) {
                    if (mRequestCountMap.containsKey(perm) && mRequestCountMap.get(perm) > 0) {
                        requiredCount++;
                    }
                }
                hasRequiredBefore = (requiredCount == permsToRequest.length);
                if (hasRequiredBefore && builder.mIsOnlyRequestOnce) {
                    //Required before and denied and only require once
                    builder.mAction.onResult(permsToRequest[0], Permissions.Denied);
                    builder.destroy();
                    mPendingActions.clear();
                    mPendingRequests.clear();
                } else {
                    //Not granted
                    if (builder.mIsReasonNeeded) {
                        String reason = null;
                        try {
                            reason = TextUtils.isEmpty(builder.mRequestReason) ? builder.mContext
                                    .getString(builder.mReasonRes) : builder.mRequestReason;
                        } catch (Exception e) {
                            // P.e(e, "Res error");
                        } finally {
                            if (TextUtils.isEmpty(reason)) {
                                //Directly require
                                ActivityCompat
                                        .requestPermissions(builder.mContext, permsToRequest, 1);
                                increaseRequiringCount(permsToRequest);
                                builder.destroy();
                            } else {
                                //Show reason dialog
                                new AlertDialog.Builder(builder.mContext)
                                        .setTitle("权限申请")
                                        .setMessage(reason)
                                        .setPositiveButton("确定",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                        if (builder.mContext != null) {
                                                            ActivityCompat.requestPermissions(
                                                                    builder.mContext,
                                                                    permsToRequest, 1);
                                                            increaseRequiringCount(permsToRequest);
                                                            builder.destroy();
                                                        } else {
                                                            builder.destroy();
                                                        }
                                                    }
                                                })
                                        .setCancelable(false)
                                        .create()
                                        .show();
                            }
                        }
                    } else {
                        //Directly require
                        ActivityCompat.requestPermissions(builder.mContext, permsToRequest, 1);
                        increaseRequiringCount(permsToRequest);
                        builder.destroy();
                    }
                }
            }
        }
    }

    /**
     * 检查App是否有该权限。
     */
    public synchronized boolean hasPermission(@Nullable Context context,
                                              @NonNull String permission) {
        return context != null && (ActivityCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED || !mPermissions.contains(permission));
    }

    /**
     * 检查App是否有给出的所有权限。
     */
    public synchronized boolean hasAllPermissions(@Nullable Context context,
                                                  @NonNull String[] permissions) {
        if (context == null) {
            return false;
        }
        boolean hasAllPermissions = true;
        for (String perm : permissions) {
            hasAllPermissions &= hasPermission(context, perm);
        }
        return hasAllPermissions;
    }

    /**
     * 在Activity的回调中捕获权限请求改变，执行权限请求。
     */
    public synchronized void notifyPermissionsChange(@NonNull String[] permissions,
                                                     @NonNull int[] results) {
        int size = permissions.length;
        if (results.length < size) {
            size = results.length;
        }
        Iterator<PermissionsResultAction> iterator = mPendingActions.iterator();
        while (iterator.hasNext()) {
            PermissionsResultAction action = iterator.next();
            for (int n = 0; n < size; n++) {
                if (action == null) {
                    iterator.remove();
                    break;
                } else {
                    if (action.onResult(permissions[n], results[n])) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
        for (int n = 0; n < size; n++) {
            mPendingRequests.remove(permissions[n]);
        }
    }

    /**
     * Start to build a permission request
     */
    public synchronized PermissionRequestBuilder buildRequest(Activity context) {
        return new PermissionRequestBuilder(context);
    }

    /**
     * 权限获得类型枚举。
     */
    public enum Permissions {
        Granted,
        Denied,
        NotFound
    }

    /**
     * 权限获取失败时提醒用户的方式。
     */
    public enum DeniedAlertType {
        Toast,
        Dialog,
        None
    }

    /**
     * 申请权限之后的动作类型。
     */
    public abstract static class PermissionsResultAction {

        private static final String TAG = "PermissionsResultAction";

        private final Set<String> mPermissions = new HashSet<>(1);

        private Looper mLooper = Looper.getMainLooper();

        private Activity mActivity = null;

        private DeniedAlertType mDeniedAlertType = DeniedAlertType.None;

        public PermissionsResultAction() {
        }

        @SuppressWarnings("unused")
        public PermissionsResultAction(@NonNull Looper looper) {
            mLooper = looper;
        }

        private void bindActivity(Activity activity) {
            mActivity = activity;
        }

        private void setDeniedAlertType(DeniedAlertType deniedAlertType) {
            mDeniedAlertType = deniedAlertType;
        }

        public void onAny() {

        }

        public void onGranted() {

        }

        public void onDenied(String permission) {
            if (mDeniedAlertType == DeniedAlertType.Toast) {
                ToastUtil.showMessage(mActivity, "获取权限失败");
            }
        }

        @SuppressWarnings({"WeakerAccess", "SameReturnValue"})
        public synchronized boolean shouldIgnorePermissionNotFound(String permission) {
            //P.d("Permission not found: " + permission);
            return true;
        }

        @SuppressWarnings("WeakerAccess")
        @CallSuper
        protected synchronized final boolean onResult(final @NonNull String permission,
                                                      int result) {
            if (result == PackageManager.PERMISSION_GRANTED) {
                return onResult(permission, Permissions.Granted);
            } else {
                return onResult(permission, Permissions.Denied);
            }
        }

        @SuppressWarnings("WeakerAccess")
        @CallSuper
        protected synchronized final boolean onResult(final @NonNull String permission,
                                                      Permissions result) {
            mPermissions.remove(permission);
            if (result == Permissions.Granted) {
                if (mPermissions.isEmpty()) {
                    new Handler(mLooper).post(new Runnable() {
                        @Override
                        public void run() {
                            onAny();
                            onGranted();
                        }
                    });
                    return true;
                }
            } else if (result == Permissions.Denied) {
                new Handler(mLooper).post(new Runnable() {
                    @Override
                    public void run() {
                        if (mDeniedAlertType == DeniedAlertType.Dialog) {
                            try {
                                showDeniedAlertDialog(mActivity, permission);
                            } catch (Exception e) {
                                //P.e(e, "Showing permission dialog error");
                            }
                        } else {
                            onAny();
                            onDenied(permission);
                        }
                    }
                });
                return true;
            } else if (result == Permissions.NotFound) {
                if (shouldIgnorePermissionNotFound(permission)) {
                    if (mPermissions.isEmpty()) {
                        new Handler(mLooper).post(new Runnable() {
                            @Override
                            public void run() {
                                onAny();
                                onGranted();
                            }
                        });
                        return true;
                    }
                } else {
                    new Handler(mLooper).post(new Runnable() {
                        @Override
                        public void run() {
                            if (mDeniedAlertType == DeniedAlertType.Dialog) {
                                try {
                                    showDeniedAlertDialog(mActivity, permission);
                                } catch (Exception e) {
                                    // P.e(e, "Showing permission dialog error");
                                }
                            } else {
                                onAny();
                                onDenied(permission);
                            }
                        }
                    });
                    return true;
                }
            }
            return false;
        }

        /**
         * 注册权限。
         */
        @SuppressWarnings("WeakerAccess")
        @CallSuper
        protected synchronized final void registerPermissions(@NonNull String[] perms) {
            Collections.addAll(mPermissions, perms);
        }

        private void showDeniedAlertDialog(final Activity context, final String deniedPermission)
                throws Exception {
            if (context == null) {
                return;
            }
            new AlertDialog.Builder(context)
                    .setTitle(R.string.apply_for_permission)
                    .setMessage(R.string.failed_in_requiring_permission)
                    .setPositiveButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    //denied
                                    dialog.dismiss();
                                    onAny();
                                    onDenied(deniedPermission);
                                }
                            })
                    .setNegativeButton(R.string.go_to_settings,
                            new DialogInterface.OnClickListener() {
                                //Jump to settings page and denied first
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent intent = new Intent(
                                            android.provider.Settings
                                                    .ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.parse("package:com.duitang.main"));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                    onAny();
                                    onDenied(deniedPermission);
                                    try {
                                        dialog.dismiss();
                                    } catch (Exception e) {
                                        // P.e(e, "Dismiss after onSaveInstance");
                                    }
                                }
                            })
                    .setCancelable(false)
                    .create()
                    .show();
        }
    }

    /**
     * Permission request builder
     */
    public static class PermissionRequestBuilder {

        DeniedAlertType mDeniedAlertType = DeniedAlertType.None;

        boolean mIsOnlyRequestOnce = false;

        boolean mIsReasonNeeded = false;

        String mRequestReason = null;

        int mReasonRes = 0;

        Activity mContext;

        PermissionsResultAction mAction;

        List<String> mPermissionList = new ArrayList<>();

        public PermissionRequestBuilder(Activity context) {
            mContext = context;
        }

        public PermissionRequestBuilder setDeniedAlertType(
                DeniedAlertType deniedAlertType) {
            mDeniedAlertType = deniedAlertType;
            return this;
        }

        public PermissionRequestBuilder setOnlyRequestOnce(Boolean onlyRequestOnce) {
            mIsOnlyRequestOnce = onlyRequestOnce;
            return this;
        }

        public PermissionRequestBuilder setRequestReason(boolean isReasonNeeded,
                                                         String requestReason) {
            mRequestReason = requestReason;
            mIsReasonNeeded = isReasonNeeded;
            return this;
        }

        public PermissionRequestBuilder setRequestReason(boolean isReasonNeeded,
                                                         int reasonRes) {
            mIsReasonNeeded = isReasonNeeded;
            mReasonRes = reasonRes;
            return this;
        }

        public PermissionRequestBuilder setRequestReason(int reasonRes) {
            mIsReasonNeeded = true;
            mReasonRes = reasonRes;
            return this;
        }

        public PermissionRequestBuilder setRequestReason(String requestReason) {
            mIsReasonNeeded = true;
            mRequestReason = requestReason;
            return this;
        }

        public PermissionRequestBuilder setAction(
                PermissionsResultAction action) {
            mAction = action;
            return this;
        }

        public PermissionRequestBuilder addRequestPermission(String permission) {
            if (mPermissionList == null) {
                mPermissionList = new ArrayList<>();
            }
            mPermissionList.add(permission);
            return this;
        }

        public void requst() {
            PermissionHelper.getInstance().requestPermissions(this);
        }

        public void destroy() {
            mContext = null;
            mAction = null;
            mPermissionList = null;
        }
    }
}
