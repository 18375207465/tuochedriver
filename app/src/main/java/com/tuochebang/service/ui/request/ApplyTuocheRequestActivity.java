package com.tuochebang.service.ui.request;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.framework.app.component.utils.ActivityUtil;
import com.framework.app.component.utils.BroadCastUtil;
import com.framework.app.component.utils.ToastUtil;
import com.framework.app.component.widget.DataLoadingView;
import com.framework.app.component.widget.XListEmptyView;
import com.framework.app.component.widget.XListView;
import com.tuochebang.service.R;
import com.tuochebang.service.adapter.SelectDriverListAdapter;
import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.base.BaseActivity;
import com.tuochebang.service.constant.AppConstant.BroadCastAction;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.entity.Trailer;
import com.tuochebang.service.request.task.GetUserTuocheManagerList;
import com.tuochebang.service.request.task.RobOrderRequest;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.List;

public class ApplyTuocheRequestActivity extends BaseActivity {
    public static String EXTRAS_REQUST_ID = "extras_request_id";
    private SelectDriverListAdapter adapter;
    private int mCurrentPage = 1;
    private DataLoadingView mDataLoadingView;
    private XListView mListRequest;
    private String mRequestId;
    private Trailer mSelectName;
    private SwipeRefreshLayout mSwipRefreshList;
    private Toolbar mToolBar;

    /* renamed from: com.tuochebang.service.ui.request.ApplyTuocheRequestActivity$1 */
    class C07181 implements OnClickListener {
        C07181() {
        }

        public void onClick(View v) {
            ApplyTuocheRequestActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.service.ui.request.ApplyTuocheRequestActivity$2 */
    class C07192 implements OnClickListener {
        C07192() {
        }

        public void onClick(View v) {
            if (ApplyTuocheRequestActivity.this.mSelectName == null) {
                ToastUtil.showMessage(MyApplication.getInstance(), "请选择拖车¬");
            } else if (ApplyTuocheRequestActivity.this.mSelectName.getUnfinish() > 0) {
                ToastUtil.showMessage(MyApplication.getInstance(), "还有未完成的订单，暂不能接单");
            } else {
                ApplyTuocheRequestActivity.this.onBtnApply();
            }
        }
    }

    /* renamed from: com.tuochebang.service.ui.request.ApplyTuocheRequestActivity$3 */
    class C07203 implements OnResponseListener<Object> {
        C07203() {
        }

        public void onStart(int what) {
            ApplyTuocheRequestActivity.this.showCommonProgreessDialog("请稍后..");
        }

        public void onSucceed(int what, Response<Object> response) {
            if (response.get() != null) {
                ActivityUtil.next(ApplyTuocheRequestActivity.this, RobOrderSuccessActivity.class);
                BroadCastUtil.sendBroadCast(ApplyTuocheRequestActivity.this.mContext, BroadCastAction.ROB_ORDER_SUCCESS);
                ApplyTuocheRequestActivity.this.finish();
            }
        }

        public void onFailed(int what, Response<Object> response) {
        }

        public void onFinish(int what) {
            ApplyTuocheRequestActivity.this.dismissCommonProgressDialog();
        }
    }

    /* renamed from: com.tuochebang.service.ui.request.ApplyTuocheRequestActivity$4 */
    class C07214 implements OnRefreshListener {
        C07214() {
        }

        public void onRefresh() {
            ApplyTuocheRequestActivity.this.mCurrentPage = 1;
            ApplyTuocheRequestActivity.this.httpGetTuocheManagerRequest(XListView.XListRefreshType.ON_PULL_REFRESH, false);
        }
    }

    /* renamed from: com.tuochebang.service.ui.request.ApplyTuocheRequestActivity$5 */
    class C07225 implements OnItemClickListener {
        C07225() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Trailer item = (Trailer) ApplyTuocheRequestActivity.this.adapter.getItem(position - 1);
            ApplyTuocheRequestActivity.this.mSelectName = item;
            if (item != null) {
                ApplyTuocheRequestActivity.this.adapter.setSelectFilterName(item);
            }
        }
    }

    /* renamed from: com.tuochebang.service.ui.request.ApplyTuocheRequestActivity$8 */
    class C07268 extends Handler {
        C07268() {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuoche_manager);
        getIntentExtras();
        initToolBar();
        initView();
    }

    private void getIntentExtras() {
        this.mRequestId = getIntent().getStringExtra(EXTRAS_REQUST_ID);
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C07181());
        this.mToolBar.setTitle((CharSequence) "选择拖车");
        TextView mTxtSure = (TextView) findViewById(R.id.tcb_sure_txt);
        mTxtSure.setVisibility(View.VISIBLE);
        mTxtSure.setOnClickListener(new C07192());
    }

    private void onBtnApply() {
        RequestQueue queue = NoHttp.newRequestQueue();
        queue.add(0, new RobOrderRequest(ServerUrl.getInst().ROB_ORDER_REQUEST(), RequestMethod.POST, this.mRequestId, String.valueOf(this.mSelectName.getUserId())), new C07203());

    }

    private void initView() {
        this.mListRequest = (XListView) findViewById(R.id.request_list);
        this.mDataLoadingView = (DataLoadingView) findViewById(R.id.data_loadingview);
        XListEmptyView xListEmptyView = (XListEmptyView) findViewById(R.id.xlist_empty_view);
        xListEmptyView.setEmptyInfo(R.mipmap.ic_list_empty_view, "暂时没有拖车");
        this.mListRequest.setEmptyView(xListEmptyView);
        this.mSwipRefreshList = (SwipeRefreshLayout) findViewById(R.id.swip_refresh);
        this.mSwipRefreshList.setOnRefreshListener(new C07214());
        this.mListRequest.setPullLoadEnable(false);
        this.mListRequest.setAutoLoadMoreEnable(true);
        this.mListRequest.setPullRefreshEnable(false);
        this.adapter = new SelectDriverListAdapter(this.mContext);
        this.mListRequest.setAdapter(this.adapter);
        this.mListRequest.setOnItemClickListener(new C07225());
        httpGetTuocheManagerRequest(XListView.XListRefreshType.ON_PULL_REFRESH, true);
    }

    private void httpGetTuocheManagerRequest(final XListView.XListRefreshType xListRefreshType, final boolean showLoadingView) {
        final RequestQueue queue = NoHttp.newRequestQueue();
        GetUserTuocheManagerList request = new GetUserTuocheManagerList(ServerUrl.getInst().GET_MANAGER_TUOCHE_REQUST(), RequestMethod.POST, this.mCurrentPage);
        request.setmBeanClass(Trailer.class, -1);
        queue.add(0, request, new OnResponseListener<List<Trailer>>() {

            /* renamed from: com.tuochebang.service.ui.request.ApplyTuocheRequestActivity$6$1 */
            class C07231 implements OnClickListener {
                C07231() {
                }

                public void onClick(View v) {
                    ApplyTuocheRequestActivity.this.httpGetTuocheManagerRequest(XListView.XListRefreshType.ON_PULL_REFRESH, true);
                }
            }

            public void onSucceed(int what, Response<List<Trailer>> response) {
                if (showLoadingView) {
                    ApplyTuocheRequestActivity.this.mDataLoadingView.showDataLoadSuccess();
                }
                List<Trailer> returnsRequestInfos = (List) response.get();
                if (returnsRequestInfos != null) {
                    Log.e("TuocheManagerData: ", returnsRequestInfos.toString());
                    if (XListView.XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                        ApplyTuocheRequestActivity.this.adapter.setList(returnsRequestInfos);
                    } else {
                        ApplyTuocheRequestActivity.this.adapter.addList(returnsRequestInfos);
                    }
                }
                if (returnsRequestInfos == null || returnsRequestInfos.size() < 10) {
                    ApplyTuocheRequestActivity.this.mListRequest.setPullLoadEnable(false);
                } else {
                    ApplyTuocheRequestActivity.this.mListRequest.setPullLoadEnable(true);
                }
            }

            public void onFailed(int what, Response<List<Trailer>> response) {
                Exception exception = response.getException();
                ApplyTuocheRequestActivity.this.mCurrentPage = ApplyTuocheRequestActivity.this.mCurrentPage - 1;
                ApplyTuocheRequestActivity.this.mDataLoadingView.showDataLoadFailed("网络开小差了...");
                ApplyTuocheRequestActivity.this.mDataLoadingView.setOnReloadClickListener(new C07231());
            }

            public void onStart(int what) {
            }

            public void onFinish(int what) {
                if (XListView.XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                    ApplyTuocheRequestActivity.this.mSwipRefreshList.setRefreshing(false);
                } else {
                    ApplyTuocheRequestActivity.this.mListRequest.onLoadMoreComplete();
                }
            }
        });
        if (showLoadingView) {
            this.mDataLoadingView.showDataLoading();
            new C07268().postDelayed(new Runnable() {
                public void run() {

                }
            }, 1000);
            return;
        }

    }
}
