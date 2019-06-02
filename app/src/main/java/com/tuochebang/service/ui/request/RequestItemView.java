package com.tuochebang.service.ui.request;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

import com.framework.app.component.utils.ActivityUtil;
import com.framework.app.component.widget.DataLoadingView;
import com.framework.app.component.widget.XListEmptyView;
import com.framework.app.component.widget.XListView;
import com.tuochebang.service.R;
import com.tuochebang.service.adapter.UserTuocheRequestListAdapter;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.entity.TuocheRequestInfo;
import com.tuochebang.service.request.task.GetUserTuocheRequest;
import com.tuochebang.service.ui.returns.ReturnDetailActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.List;

public class RequestItemView extends RelativeLayout {
    public static String TUOCHE_REQUEST_COMPLETE_STATUS = "2";
    public static String TUOCHE_REQUEST_DOING_STATUS = "0";
    public static String TUOCHE_REQUEST_PAY_STATUS = "1";
    public static String TUOCHE_REQUEST_WAIT_STATUS = "3";
    private UserTuocheRequestListAdapter adapter;
    private int mCurrentPage = 1;
    private DataLoadingView mDataLoadingView;
    private XListView mListRequest;
    private String mStatus;
    private SwipeRefreshLayout mSwipRefreshList;

    /* renamed from: com.tuochebang.service.ui.request.RequestItemView$1 */
    class C07271 implements OnRefreshListener {
        C07271() {
        }

        public void onRefresh() {
            RequestItemView.this.mCurrentPage = 1;
            RequestItemView.this.httpGetReturnsCarRequest(XListView.XListRefreshType.ON_PULL_REFRESH, false);
        }
    }

    /* renamed from: com.tuochebang.service.ui.request.RequestItemView$5 */
    class C07325 extends Handler {
        C07325() {
        }
    }

    public RequestItemView(Context context) {
        super(context);
        initView(context);
    }

    public RequestItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RequestItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_request_item_view, this);
        this.mListRequest = (XListView) view.findViewById(R.id.request_list);
        this.mDataLoadingView = (DataLoadingView) view.findViewById(R.id.data_loadingview);
        XListEmptyView xListEmptyView = (XListEmptyView) view.findViewById(R.id.xlist_empty_view);
        xListEmptyView.setEmptyInfo(R.mipmap.ic_list_empty_view, "暂时没有返程车");
        this.mListRequest.setEmptyView(xListEmptyView);
        this.mSwipRefreshList = (SwipeRefreshLayout) view.findViewById(R.id.swip_refresh);
        this.mSwipRefreshList.setOnRefreshListener(new C07271());
        this.mListRequest.setPullLoadEnable(false);
        this.mListRequest.setAutoLoadMoreEnable(true);
        this.mListRequest.setPullRefreshEnable(false);
        this.adapter = new UserTuocheRequestListAdapter(context);
        this.mListRequest.setAdapter(this.adapter);
        this.mListRequest.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TuocheRequestInfo info = (TuocheRequestInfo) RequestItemView.this.adapter.getItem(position - 1);
                if (info != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(ReturnDetailActivity.EXTRAS_DETAIL_TYPE, 1);
                    bundle.putInt(ReturnDetailActivity.EXTRAS_ID, Integer.valueOf(info.getRequestId()).intValue());
                    ActivityUtil.next((Activity) context, ReturnDetailActivity.class, bundle);
                }
            }
        });
    }

    public void setData(String status) {
        this.mStatus = status;
        this.mCurrentPage = 1;
        httpGetReturnsCarRequest(XListView.XListRefreshType.ON_PULL_REFRESH, true);
    }

    private void httpGetReturnsCarRequest(final XListView.XListRefreshType xListRefreshType, final boolean showLoadingView) {
        final RequestQueue queue = NoHttp.newRequestQueue();
        GetUserTuocheRequest request = new GetUserTuocheRequest(ServerUrl.getInst().GET_TUOCHE_REQUEST(), RequestMethod.POST, this.mStatus, this.mCurrentPage);
        request.setmBeanClass(TuocheRequestInfo.class, -1);
        queue.add(0, request, new OnResponseListener<List<TuocheRequestInfo>>() {

            /* renamed from: com.tuochebang.service.ui.request.RequestItemView$3$1 */
            class C07291 implements OnClickListener {
                C07291() {
                }

                public void onClick(View v) {
                    RequestItemView.this.httpGetReturnsCarRequest(XListView.XListRefreshType.ON_PULL_REFRESH, true);
                }
            }

            public void onSucceed(int what, Response<List<TuocheRequestInfo>> response) {
                if (showLoadingView) {
                    RequestItemView.this.mDataLoadingView.showDataLoadSuccess();
                }
                List<TuocheRequestInfo> returnsRequestInfos = (List) response.get();
                if (returnsRequestInfos != null) {
                    Log.e("ReturnsItemView: ", returnsRequestInfos.toString());
                    if (XListView.XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                        RequestItemView.this.adapter.setList(returnsRequestInfos);
                    } else {
                        RequestItemView.this.adapter.addList(returnsRequestInfos);
                    }
                }
                if (returnsRequestInfos == null || returnsRequestInfos.size() < 10) {
                    RequestItemView.this.mListRequest.setPullLoadEnable(false);
                } else {
                    RequestItemView.this.mListRequest.setPullLoadEnable(true);
                }
            }

            public void onFailed(int what, Response<List<TuocheRequestInfo>> response) {
                Exception exception = response.getException();
                RequestItemView.this.mCurrentPage = RequestItemView.this.mCurrentPage - 1;
                RequestItemView.this.mDataLoadingView.showDataLoadFailed("网络开小差了...");
                RequestItemView.this.mDataLoadingView.setOnReloadClickListener(new C07291());
            }

            public void onStart(int what) {
            }

            public void onFinish(int what) {
                if (XListView.XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                    RequestItemView.this.mSwipRefreshList.setRefreshing(false);
                } else {
                    RequestItemView.this.mListRequest.onLoadMoreComplete();
                }
            }
        });
        if (showLoadingView) {
            this.mDataLoadingView.showDataLoading();
            new C07325().postDelayed(new Runnable() {
                public void run() {

                }
            }, 1000);
            return;
        }
    }
}
