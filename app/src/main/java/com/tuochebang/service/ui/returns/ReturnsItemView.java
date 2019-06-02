package com.tuochebang.service.ui.returns;

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
import com.tuochebang.service.adapter.ReturnCarListAdapter;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.entity.ReturnCarInfo;
import com.tuochebang.service.request.task.GetUserReturnsCarRequest;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.List;

public class ReturnsItemView extends RelativeLayout {
    public static int TUOCHE_RETURN_COMPLETE_STATUS = 2;
    public static int TUOCHE_RETURN_DOING_STATUS = 1;
    public static int TUOCHE_RETURN_WAIT_STATUS = 0;
    private ReturnCarListAdapter adapter;
    private int mCurrentPage = 1;
    private DataLoadingView mDataLoadingView;
    private XListView mListRequest;
    private int mStatus;
    private SwipeRefreshLayout mSwipRefreshList;

    /* renamed from: com.tuochebang.service.ui.returns.ReturnsItemView$1 */
    class C07651 implements OnRefreshListener {
        C07651() {
        }

        public void onRefresh() {
            ReturnsItemView.this.mCurrentPage = 1;
            ReturnsItemView.this.httpGetReturnsCarRequest(XListView.XListRefreshType.ON_PULL_REFRESH, false);
        }
    }

    /* renamed from: com.tuochebang.service.ui.returns.ReturnsItemView$5 */
    class C07705 extends Handler {
        C07705() {
        }
    }

    public ReturnsItemView(Context context) {
        super(context);
        initView(context);
    }

    public ReturnsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ReturnsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        this.mSwipRefreshList.setOnRefreshListener(new C07651());
        this.mListRequest.setPullLoadEnable(false);
        this.mListRequest.setAutoLoadMoreEnable(true);
        this.mListRequest.setPullRefreshEnable(false);
        this.adapter = new ReturnCarListAdapter(context);
        this.mListRequest.setAdapter(this.adapter);
        this.mListRequest.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ReturnCarInfo info = (ReturnCarInfo) ReturnsItemView.this.adapter.getItem(position - 1);
                if (info != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(ReturnDetailActivity.EXTRAS_DETAIL_TYPE, 0);
                    bundle.putInt(ReturnDetailActivity.EXTRAS_ID, info.getReturnId());
                    ActivityUtil.next((Activity) context, ReturnDetailActivity.class, bundle);
                }
            }
        });
    }

    public void setData(int status) {
        this.mStatus = status;
        this.mCurrentPage = 1;
        httpGetReturnsCarRequest(XListView.XListRefreshType.ON_PULL_REFRESH, true);
    }

    private void httpGetReturnsCarRequest(final XListView.XListRefreshType xListRefreshType, final boolean showLoadingView) {
        final RequestQueue queue = NoHttp.newRequestQueue();
        GetUserReturnsCarRequest request = new GetUserReturnsCarRequest(ServerUrl.getInst().GET_TUOCHE_RETURNS(), RequestMethod.POST, this.mStatus, this.mCurrentPage);
        request.setmBeanClass(ReturnCarInfo.class, -1);
        queue.add(0, request, new OnResponseListener<List<ReturnCarInfo>>() {

            /* renamed from: com.tuochebang.service.ui.returns.ReturnsItemView$3$1 */
            class C07671 implements OnClickListener {
                C07671() {
                }

                public void onClick(View v) {
                    ReturnsItemView.this.httpGetReturnsCarRequest(XListView.XListRefreshType.ON_PULL_REFRESH, true);
                }
            }

            public void onSucceed(int what, Response<List<ReturnCarInfo>> response) {
                if (showLoadingView) {
                    ReturnsItemView.this.mDataLoadingView.showDataLoadSuccess();
                }
                List<ReturnCarInfo> returnsRequestInfos = (List) response.get();
                if (returnsRequestInfos != null) {
                    Log.e("ReturnsItemView: ", returnsRequestInfos.toString());
                    if (XListView.XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                        ReturnsItemView.this.adapter.setList(returnsRequestInfos);
                    } else {
                        ReturnsItemView.this.adapter.addList(returnsRequestInfos);
                    }
                }
                if (returnsRequestInfos == null || returnsRequestInfos.size() < 10) {
                    ReturnsItemView.this.mListRequest.setPullLoadEnable(false);
                } else {
                    ReturnsItemView.this.mListRequest.setPullLoadEnable(true);
                }
            }

            public void onFailed(int what, Response<List<ReturnCarInfo>> response) {
                Exception exception = response.getException();
                ReturnsItemView.this.mCurrentPage = ReturnsItemView.this.mCurrentPage - 1;
                ReturnsItemView.this.mDataLoadingView.showDataLoadFailed("网络开小差了...");
                ReturnsItemView.this.mDataLoadingView.setOnReloadClickListener(new C07671());
            }

            public void onStart(int what) {
            }

            public void onFinish(int what) {
                if (XListView.XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                    ReturnsItemView.this.mSwipRefreshList.setRefreshing(false);
                } else {
                    ReturnsItemView.this.mListRequest.onLoadMoreComplete();
                }
            }
        });
        if (showLoadingView) {
            this.mDataLoadingView.showDataLoading();
            new C07705().postDelayed(new Runnable() {
                public void run() {

                }
            }, 1000);
            return;
        }
    }
}
