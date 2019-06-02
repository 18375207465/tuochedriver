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
import com.tuochebang.service.adapter.TuocheManagerListAdapter;
import com.tuochebang.service.base.BaseActivity;
import com.tuochebang.service.constant.AppConstant.BroadCastAction;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.entity.Trailer;
import com.tuochebang.service.request.task.GetUserTuocheManagerList;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import java.util.List;

public class UserTuocheManagerActivity extends BaseActivity {
    private TuocheManagerListAdapter adapter;
    private int mCurrentPage = 1;
    private DataLoadingView mDataLoadingView;
    private BroadcastReceiver mDriverStatuChangeReceiver;
    private XListView mListRequest;
    private SwipeRefreshLayout mSwipRefreshList;
    private Toolbar mToolBar;

    /* renamed from: com.tuochebang.service.ui.user.UserTuocheManagerActivity$1 */
    class C08111 extends BroadcastReceiver {
        C08111() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (BroadCastAction.DRIVER_CHANGE.equals(intent.getAction())) {
                    UserTuocheManagerActivity.this.httpGetTuocheManagerRequest(XListView.XListRefreshType.ON_PULL_REFRESH, true);
                }
            }
        }
    }

    /* renamed from: com.tuochebang.service.ui.user.UserTuocheManagerActivity$2 */
    class C08122 implements OnClickListener {
        C08122() {
        }

        public void onClick(View v) {
            UserTuocheManagerActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.service.ui.user.UserTuocheManagerActivity$3 */
    class C08133 implements OnRefreshListener {
        C08133() {
        }

        public void onRefresh() {
            UserTuocheManagerActivity.this.mCurrentPage = 1;
            UserTuocheManagerActivity.this.httpGetTuocheManagerRequest(XListView.XListRefreshType.ON_PULL_REFRESH, false);
        }
    }

    /* renamed from: com.tuochebang.service.ui.user.UserTuocheManagerActivity$6 */
    class C08176 extends Handler {
        C08176() {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuoche_manager);
        initToolBar();
        initView();
        registDriverStatuChangeBroadCastReciver();
    }

    private void registDriverStatuChangeBroadCastReciver() {
        this.mDriverStatuChangeReceiver = new C08111();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mDriverStatuChangeReceiver, new IntentFilter(BroadCastAction.DRIVER_CHANGE));
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C08122());
    }

    private void initView() {
        this.mListRequest = (XListView) findViewById(R.id.request_list);
        this.mDataLoadingView = (DataLoadingView) findViewById(R.id.data_loadingview);
        XListEmptyView xListEmptyView = (XListEmptyView) findViewById(R.id.xlist_empty_view);
        xListEmptyView.setEmptyInfo(R.mipmap.ic_list_empty_view, "暂时没有拖车");
        this.mListRequest.setEmptyView(xListEmptyView);
        this.mSwipRefreshList = (SwipeRefreshLayout) findViewById(R.id.swip_refresh);
        this.mSwipRefreshList.setOnRefreshListener(new C08133());
        this.mListRequest.setPullLoadEnable(false);
        this.mListRequest.setAutoLoadMoreEnable(true);
        this.mListRequest.setPullRefreshEnable(false);
        this.adapter = new TuocheManagerListAdapter(this.mContext);
        this.mListRequest.setAdapter(this.adapter);
        httpGetTuocheManagerRequest(XListView.XListRefreshType.ON_PULL_REFRESH, true);
    }

    private void httpGetTuocheManagerRequest(final XListView.XListRefreshType xListRefreshType, final boolean showLoadingView) {
        final RequestQueue queue = NoHttp.newRequestQueue();
        GetUserTuocheManagerList request = new GetUserTuocheManagerList(ServerUrl.getInst().GET_MANAGER_TUOCHE_REQUST(), RequestMethod.POST, this.mCurrentPage);
        request.setmBeanClass(Trailer.class, -1);
        queue.add(0, request, new OnResponseListener<List<Trailer>>() {

            /* renamed from: com.tuochebang.service.ui.user.UserTuocheManagerActivity$4$1 */
            class C08141 implements OnClickListener {
                C08141() {
                }

                public void onClick(View v) {
                    UserTuocheManagerActivity.this.httpGetTuocheManagerRequest(XListView.XListRefreshType.ON_PULL_REFRESH, true);
                }
            }

            public void onSucceed(int what, Response<List<Trailer>> response) {
                if (showLoadingView) {
                    UserTuocheManagerActivity.this.mDataLoadingView.showDataLoadSuccess();
                }
                List<Trailer> returnsRequestInfos = (List) response.get();
                if (returnsRequestInfos != null) {
                    Log.e("TuocheManagerData: ", returnsRequestInfos.toString());
                    if (XListView.XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                        UserTuocheManagerActivity.this.adapter.setList(returnsRequestInfos);
                    } else {
                        UserTuocheManagerActivity.this.adapter.addList(returnsRequestInfos);
                    }
                }
                if (returnsRequestInfos == null || returnsRequestInfos.size() < 10) {
                    UserTuocheManagerActivity.this.mListRequest.setPullLoadEnable(false);
                } else {
                    UserTuocheManagerActivity.this.mListRequest.setPullLoadEnable(true);
                }
            }

            public void onFailed(int what, Response<List<Trailer>> response) {
                Exception exception = response.getException();
                UserTuocheManagerActivity.this.mCurrentPage = UserTuocheManagerActivity.this.mCurrentPage - 1;
                UserTuocheManagerActivity.this.mDataLoadingView.showDataLoadFailed("网络开小差了...");
                UserTuocheManagerActivity.this.mDataLoadingView.setOnReloadClickListener(new C08141());
            }

            public void onStart(int what) {
            }

            public void onFinish(int what) {
                if (XListView.XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                    UserTuocheManagerActivity.this.mSwipRefreshList.setRefreshing(false);
                } else {
                    UserTuocheManagerActivity.this.mListRequest.onLoadMoreComplete();
                }
            }
        });
        if (showLoadingView) {
            this.mDataLoadingView.showDataLoading();
            new C08176().postDelayed(new Runnable() {
                public void run() {

                }
            }, 1000);
            return;
        }

    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mDriverStatuChangeReceiver != null) {
            LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this.mDriverStatuChangeReceiver);
        }
    }
}
