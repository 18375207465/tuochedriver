package com.tuochebang.service.ui.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.framework.app.component.widget.DataLoadingView;
import com.framework.app.component.widget.XListEmptyView;
import com.framework.app.component.widget.XListView;
import com.tuochebang.service.R;
import com.tuochebang.service.adapter.AuthListAdapter;
import com.tuochebang.service.base.BaseActivity;
import com.tuochebang.service.constant.AppConstant.BroadCastAction;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.entity.Auth;
import com.tuochebang.service.request.task.GetUserAuthList;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import java.util.List;

public class UserAuthActivity extends BaseActivity {
    private AuthListAdapter adapter;
    private BroadcastReceiver mAuthStatuChangeReceiver;
    private int mCurrentPage = 1;
    private DataLoadingView mDataLoadingView;
    private XListView mListRequest;
    private SwipeRefreshLayout mSwipRefreshList;
    private Toolbar mToolBar;

    /* renamed from: com.tuochebang.service.ui.user.UserAuthActivity$1 */
    class C07851 extends BroadcastReceiver {
        C07851() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (BroadCastAction.AUTH_CHANGE.equals(intent.getAction())) {
                    UserAuthActivity.this.httpGetAuthListRequest(XListView.XListRefreshType.ON_PULL_REFRESH, true);
                }
            }
        }
    }

    /* renamed from: com.tuochebang.service.ui.user.UserAuthActivity$2 */
    class C07862 implements OnRefreshListener {
        C07862() {
        }

        public void onRefresh() {
            UserAuthActivity.this.mCurrentPage = 1;
            UserAuthActivity.this.httpGetAuthListRequest(XListView.XListRefreshType.ON_PULL_REFRESH, false);
        }
    }

    /* renamed from: com.tuochebang.service.ui.user.UserAuthActivity$5 */
    class C07905 extends Handler {
        C07905() {
        }
    }

    /* renamed from: com.tuochebang.service.ui.user.UserAuthActivity$6 */
    class C07916 implements OnClickListener {
        C07916() {
        }

        public void onClick(View v) {
            UserAuthActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_list);
        initToolBar();
        initView();
        registAuthStatuChangeBroadCastReciver();
    }

    private void registAuthStatuChangeBroadCastReciver() {
        this.mAuthStatuChangeReceiver = new C07851();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mAuthStatuChangeReceiver, new IntentFilter(BroadCastAction.AUTH_CHANGE));
    }

    private void initView() {
        this.mListRequest = (XListView) findViewById(R.id.request_list);
        this.mDataLoadingView = (DataLoadingView) findViewById(R.id.data_loadingview);
        XListEmptyView xListEmptyView = (XListEmptyView) findViewById(R.id.xlist_empty_view);
        xListEmptyView.setEmptyInfo(R.mipmap.ic_list_empty_view, "暂时没有待授权司机");
        this.mListRequest.setEmptyView(xListEmptyView);
        this.mSwipRefreshList = (SwipeRefreshLayout) findViewById(R.id.swip_refresh);
        this.mSwipRefreshList.setOnRefreshListener(new C07862());
        this.mListRequest.setPullLoadEnable(false);
        this.mListRequest.setAutoLoadMoreEnable(true);
        this.mListRequest.setPullRefreshEnable(false);
        this.adapter = new AuthListAdapter(this.mContext);
        this.mListRequest.setAdapter(this.adapter);
        httpGetAuthListRequest(XListView.XListRefreshType.ON_PULL_REFRESH, true);
    }

    private void httpGetAuthListRequest(final XListView.XListRefreshType xListRefreshType, final boolean showLoadingView) {
        final RequestQueue queue = NoHttp.newRequestQueue();
        GetUserAuthList request = new GetUserAuthList(ServerUrl.getInst().GET_USER_AUTH_REQUST(), RequestMethod.POST, this.mCurrentPage);
        request.setmBeanClass(Auth.class, -1);
        queue.add(0, request, new OnResponseListener<List<Auth>>() {

            /* renamed from: com.tuochebang.service.ui.user.UserAuthActivity$3$1 */
            class C07871 implements OnClickListener {
                C07871() {
                }

                public void onClick(View v) {
                    UserAuthActivity.this.httpGetAuthListRequest(XListView.XListRefreshType.ON_PULL_REFRESH, true);
                }
            }

            public void onSucceed(int what, Response<List<Auth>> response) {
                if (showLoadingView) {
                    UserAuthActivity.this.mDataLoadingView.showDataLoadSuccess();
                }
                List<Auth> returnsRequestInfos = (List) response.get();
                if (returnsRequestInfos != null) {
                    Log.e("TuocheManagerData: ", returnsRequestInfos.toString());
                    if (XListView.XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                        UserAuthActivity.this.adapter.setList(returnsRequestInfos);
                    } else {
                        UserAuthActivity.this.adapter.addList(returnsRequestInfos);
                    }
                }
                if (returnsRequestInfos == null || returnsRequestInfos.size() < 10) {
                    UserAuthActivity.this.mListRequest.setPullLoadEnable(false);
                } else {
                    UserAuthActivity.this.mListRequest.setPullLoadEnable(true);
                }
            }

            public void onFailed(int what, Response<List<Auth>> response) {
                Exception exception = response.getException();
                UserAuthActivity.this.mCurrentPage = UserAuthActivity.this.mCurrentPage - 1;
                UserAuthActivity.this.mDataLoadingView.showDataLoadFailed("网络开小差了...");
                UserAuthActivity.this.mDataLoadingView.setOnReloadClickListener(new C07871());
            }

            public void onStart(int what) {
            }

            public void onFinish(int what) {
                if (XListView.XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                    UserAuthActivity.this.mSwipRefreshList.setRefreshing(false);
                } else {
                    UserAuthActivity.this.mListRequest.onLoadMoreComplete();
                }
            }
        });
        if (showLoadingView) {
            this.mDataLoadingView.showDataLoading();
            new C07905().postDelayed(new Runnable() {
                public void run() {

                }
            }, 1000);
            return;
        }

    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C07916());
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mAuthStatuChangeReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mAuthStatuChangeReceiver);
        }
    }
}
