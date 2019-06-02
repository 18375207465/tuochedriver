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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.framework.app.component.utils.ActivityUtil;
import com.framework.app.component.utils.BroadCastUtil;
import com.framework.app.component.widget.DataLoadingView;
import com.framework.app.component.widget.XListEmptyView;
import com.framework.app.component.widget.XListView;
import com.tuochebang.service.R;
import com.tuochebang.service.adapter.MessageListAdapter;
import com.tuochebang.service.base.BaseActivity;
import com.tuochebang.service.constant.AppConstant.BroadCastAction;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.entity.MessageInfo;
import com.tuochebang.service.request.task.GetUserMessageRequest;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.List;

public class UserMessageActivity extends BaseActivity {
    private MessageListAdapter adapter;
    private int mCurrentPage = 1;
    private DataLoadingView mDataLoadingView;
    private XListView mListRequest;
    private BroadcastReceiver mMessageStatuChangeReceiver;
    private SwipeRefreshLayout mSwipRefreshList;
    private Toolbar mToolBar;

    /* renamed from: com.tuochebang.service.ui.user.UserMessageActivity$1 */
    class C08031 extends BroadcastReceiver {
        C08031() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (BroadCastAction.MESSAGE_CHANGE.equals(intent.getAction())) {
                    UserMessageActivity.this.httpGetMessageRequest(XListView.XListRefreshType.ON_PULL_REFRESH, true);
                }
            }
        }
    }

    /* renamed from: com.tuochebang.service.ui.user.UserMessageActivity$2 */
    class C08042 implements OnClickListener {
        C08042() {
        }

        public void onClick(View v) {
            UserMessageActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.service.ui.user.UserMessageActivity$3 */
    class C08053 implements OnRefreshListener {
        C08053() {
        }

        public void onRefresh() {
            UserMessageActivity.this.mCurrentPage = 1;
            UserMessageActivity.this.httpGetMessageRequest(XListView.XListRefreshType.ON_PULL_REFRESH, false);
        }
    }

    /* renamed from: com.tuochebang.service.ui.user.UserMessageActivity$4 */
    class C08064 implements OnItemClickListener {
        C08064() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            MessageInfo info = (MessageInfo) UserMessageActivity.this.adapter.getItem(position - 1);
            if (info != null) {
                BroadCastUtil.sendBroadCast(UserMessageActivity.this.mContext, BroadCastAction.MESSAGE_CHANGE);
                Bundle bundle = new Bundle();
                bundle.putSerializable(MessageDetailActivity.EXTRAS_MESSAGE_INFO, info);
                ActivityUtil.next(UserMessageActivity.this, MessageDetailActivity.class, bundle);
            }
        }
    }

    /* renamed from: com.tuochebang.service.ui.user.UserMessageActivity$7 */
    class C08107 extends Handler {
        C08107() {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initToolBar();
        initView();
        registMessageStatuChangeBroadCastReciver();
        setData();
    }

    private void registMessageStatuChangeBroadCastReciver() {
        this.mMessageStatuChangeReceiver = new C08031();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mMessageStatuChangeReceiver, new IntentFilter(BroadCastAction.MESSAGE_CHANGE));
    }

    private void setData() {
        httpGetMessageRequest(XListView.XListRefreshType.ON_PULL_REFRESH, true);
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C08042());
    }

    private void initView() {
        this.mListRequest = (XListView) findViewById(R.id.request_list);
        this.mDataLoadingView = (DataLoadingView) findViewById(R.id.data_loadingview);
        XListEmptyView xListEmptyView = (XListEmptyView) findViewById(R.id.xlist_empty_view);
        xListEmptyView.setEmptyInfo(R.mipmap.ic_list_empty_view, "暂时没有拖车");
        this.mListRequest.setEmptyView(xListEmptyView);
        this.mSwipRefreshList = (SwipeRefreshLayout) findViewById(R.id.swip_refresh);
        this.mSwipRefreshList.setOnRefreshListener(new C08053());
        this.mListRequest.setPullLoadEnable(false);
        this.mListRequest.setAutoLoadMoreEnable(true);
        this.mListRequest.setPullRefreshEnable(false);
        this.adapter = new MessageListAdapter(this.mContext);
        this.mListRequest.setAdapter(this.adapter);
        this.mListRequest.setOnItemClickListener(new C08064());
    }

    private void httpGetMessageRequest(final XListView.XListRefreshType xListRefreshType, final boolean showLoadingView) {
        final RequestQueue queue = NoHttp.newRequestQueue();
        GetUserMessageRequest request = new GetUserMessageRequest(ServerUrl.getInst().GET_TUOCHE_MESSAGE(), RequestMethod.POST, this.mCurrentPage);
        request.setmBeanClass(MessageInfo.class, -1);
        queue.add(0, request, new OnResponseListener<List<MessageInfo>>() {

            /* renamed from: com.tuochebang.service.ui.user.UserMessageActivity$5$1 */
            class C08071 implements OnClickListener {
                C08071() {
                }

                public void onClick(View v) {
                    UserMessageActivity.this.httpGetMessageRequest(XListView.XListRefreshType.ON_PULL_REFRESH, true);
                }
            }

            public void onSucceed(int what, Response<List<MessageInfo>> response) {
                if (showLoadingView) {
                    UserMessageActivity.this.mDataLoadingView.showDataLoadSuccess();
                }
                List<MessageInfo> messageRequestInfos = (List) response.get();
                if (messageRequestInfos != null) {
                    Log.e(UserMessageActivity.this.TAG, messageRequestInfos.toString());
                    if (XListView.XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                        UserMessageActivity.this.adapter.setList(messageRequestInfos);
                    } else {
                        UserMessageActivity.this.adapter.addList(messageRequestInfos);
                    }
                }
                if (messageRequestInfos == null || messageRequestInfos.size() < 10) {
                    UserMessageActivity.this.mListRequest.setPullLoadEnable(false);
                } else {
                    UserMessageActivity.this.mListRequest.setPullLoadEnable(true);
                }
            }

            public void onFailed(int what, Response<List<MessageInfo>> response) {
                Exception exception = response.getException();
                UserMessageActivity.this.mCurrentPage = UserMessageActivity.this.mCurrentPage - 1;
                UserMessageActivity.this.mDataLoadingView.showDataLoadFailed("网络开小差了...");
                UserMessageActivity.this.mDataLoadingView.setOnReloadClickListener(new C08071());
            }

            public void onStart(int what) {
            }

            public void onFinish(int what) {
                if (XListView.XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                    UserMessageActivity.this.mSwipRefreshList.setRefreshing(false);
                } else {
                    UserMessageActivity.this.mListRequest.onLoadMoreComplete();
                }
            }
        });
        if (showLoadingView) {
            this.mDataLoadingView.showDataLoading();
            new C08107().postDelayed(new Runnable() {
                public void run() {

                }
            }, 1000);
            return;
        }

    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mMessageStatuChangeReceiver != null) {
            LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this.mMessageStatuChangeReceiver);
        }
    }
}
