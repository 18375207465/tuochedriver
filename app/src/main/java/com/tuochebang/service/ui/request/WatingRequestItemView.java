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
import com.luck.picture.lib.tools.ScreenUtils;
import com.tuochebang.service.R;
import com.tuochebang.service.adapter.TuocheRequestListAdapter;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.entity.TuocheRequestInfo;
import com.tuochebang.service.request.task.GetUserWatingRequest;
import com.tuochebang.service.util.AndroidUtil;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.List;

//拖车请求页面详情
public class WatingRequestItemView extends RelativeLayout {
    public static String TUOCHE_REQUEST_TIME_STATUS = "t";
    public static String TUOCHE_RETURN_DISTANCE_STATUS = "d";
    private TuocheRequestListAdapter adapter;
    private int mCurrentPage = 1;
    private DataLoadingView mDataLoadingView;
    private XListView mListRequest;
    private String mStatus;
    private SwipeRefreshLayout mSwipRefreshList;

    /* renamed from: com.tuochebang.service.ui.request.WatingRequestItemView$1 */
    class C07441 implements OnRefreshListener {
        C07441() {
        }

        public void onRefresh() {
            WatingRequestItemView.this.mCurrentPage = 1;
            WatingRequestItemView.this.httpGetRequestsCarRequest(XListView.XListRefreshType.ON_PULL_REFRESH, false);
        }
    }

    /* renamed from: com.tuochebang.service.ui.request.WatingRequestItemView$5 */
    class C07495 extends Handler {
        C07495() {
        }
    }

    public WatingRequestItemView(Context context) {
        super(context);
        initView(context);
    }

    public WatingRequestItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public WatingRequestItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_request_item_view, this);
        this.mListRequest = (XListView) view.findViewById(R.id.request_list);
        this.mDataLoadingView = (DataLoadingView) view.findViewById(R.id.data_loadingview);
        XListEmptyView xListEmptyView = (XListEmptyView) view.findViewById(R.id.xlist_empty_view);
        xListEmptyView.setEmptyInfo(R.mipmap.ic_list_empty_view, "暂时没有待抢订单");
        this.mListRequest.setEmptyView(xListEmptyView);
        this.mSwipRefreshList = (SwipeRefreshLayout) view.findViewById(R.id.swip_refresh);
        this.mSwipRefreshList.setOnRefreshListener(new C07441());
        this.mListRequest.setPullLoadEnable(false);
        this.mListRequest.setAutoLoadMoreEnable(true);
        this.mListRequest.setPullRefreshEnable(false);
        this.adapter = new TuocheRequestListAdapter(context);
        this.mListRequest.setAdapter(this.adapter);
        this.mListRequest.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TuocheRequestInfo info = (TuocheRequestInfo) WatingRequestItemView.this.adapter.getItem(position - 1);
                if (info != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("request", info);
                    ActivityUtil.next((Activity) context, WaitingRequestDetailActivity.class, bundle);
                }
            }
        });
    }

    public void setData(String status) {
        this.mStatus = status;
        this.mCurrentPage = 1;
        httpGetRequestsCarRequest(XListView.XListRefreshType.ON_PULL_REFRESH, true);
    }

    private void httpGetRequestsCarRequest(final XListView.XListRefreshType xListRefreshType, final boolean showLoadingView) {
        final RequestQueue queue = NoHttp.newRequestQueue();
        GetUserWatingRequest request = new GetUserWatingRequest(ServerUrl.getInst().GET_WATING_TUOCHE_REQUST(), RequestMethod.POST, this.mStatus, this.mCurrentPage);
        request.setmBeanClass(TuocheRequestInfo.class, -1);
        queue.add(0, request, new OnResponseListener<List<TuocheRequestInfo>>() {

            /* renamed from: com.tuochebang.service.ui.request.WatingRequestItemView$3$1 */
            class C07461 implements OnClickListener {
                C07461() {
                }

                public void onClick(View v) {
                    WatingRequestItemView.this.httpGetRequestsCarRequest(XListView.XListRefreshType.ON_PULL_REFRESH, true);
                }
            }

            public void onSucceed(int what, Response<List<TuocheRequestInfo>> response) {
                if (showLoadingView) {
                    WatingRequestItemView.this.mDataLoadingView.showDataLoadSuccess();
                }
                List<TuocheRequestInfo> returnsRequestInfos = (List) response.get();
                if (returnsRequestInfos != null) {
                    Log.e("WatingRequestItemView: ", returnsRequestInfos.toString());
                    if (XListView.XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                        WatingRequestItemView.this.adapter.setList(returnsRequestInfos);
                    } else {
                        WatingRequestItemView.this.adapter.addList(returnsRequestInfos);
                    }
                }
                if (returnsRequestInfos == null || returnsRequestInfos.size() < 10) {
                    WatingRequestItemView.this.mListRequest.setPullLoadEnable(false);
                } else {
                    WatingRequestItemView.this.mListRequest.setPullLoadEnable(true);
                }
            }

            public void onFailed(int what, Response<List<TuocheRequestInfo>> response) {
                Exception exception = response.getException();
                WatingRequestItemView.this.mCurrentPage = WatingRequestItemView.this.mCurrentPage - 1;
                WatingRequestItemView.this.mDataLoadingView.showDataLoadFailed("网络开小差了...");
                WatingRequestItemView.this.mDataLoadingView.setOnReloadClickListener(new C07461());
            }

            public void onStart(int what) {
            }

            public void onFinish(int what) {
                if (XListView.XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                    WatingRequestItemView.this.mSwipRefreshList.setRefreshing(false);
                } else {
                    WatingRequestItemView.this.mListRequest.onLoadMoreComplete();
                }
            }
        });
        if (showLoadingView) {
            this.mDataLoadingView.showDataLoading();
            new C07495().postDelayed(new Runnable() {
                public void run() {

                }
            }, 1000);
            return;
        }

    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int imageHeight = ScreenUtils.getScreenHeight(getContext());
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(imageHeight,
//                MeasureSpec.EXACTLY);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }
}
