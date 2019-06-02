package com.tuochebang.service.ui.user;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.framework.app.component.widget.DataLoadingView;
import com.tuochebang.service.R;
import com.tuochebang.service.base.BaseActivity;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.entity.MessageInfo;
import com.tuochebang.service.request.task.GetMessageDetailRequest;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

public class MessageDetailActivity extends BaseActivity {
    public static String EXTRAS_MESSAGE_INFO = "extras_message_info";
    private DataLoadingView mDataLoadingView;
    private MessageInfo mMessageInfo;
    private Toolbar mToolBar;
    private TextView mTxtContent;
    private TextView mTxtTime;
    private TextView mTxtTitle;

    /* renamed from: com.tuochebang.service.ui.user.MessageDetailActivity$1 */
    class C07821 implements OnClickListener {
        C07821() {
        }

        public void onClick(View v) {
            MessageDetailActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.service.ui.user.MessageDetailActivity$2 */
    class C07842 implements OnResponseListener<MessageInfo> {

        /* renamed from: com.tuochebang.service.ui.user.MessageDetailActivity$2$1 */
        class C07831 implements OnClickListener {
            C07831() {
            }

            public void onClick(View v) {
                MessageDetailActivity.this.httpGetMessageDetailRequest();
            }
        }

        C07842() {
        }

        public void onSucceed(int what, Response<MessageInfo> response) {
            if (((MessageInfo) response.get()) != null) {
                MessageDetailActivity.this.mDataLoadingView.showDataLoadSuccess();
            }
        }

        public void onFailed(int what, Response<MessageInfo> response) {
            Exception exception = response.getException();
            MessageDetailActivity.this.mDataLoadingView.showDataLoadFailed("网络开小差了...");
            MessageDetailActivity.this.mDataLoadingView.setOnReloadClickListener(new C07831());
        }

        public void onStart(int what) {
            MessageDetailActivity.this.mDataLoadingView.showDataLoading();
        }

        public void onFinish(int what) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        getIntentExtras();
        initToolBar();
        initView();
        setData();
    }

    private void setData() {
        httpGetMessageDetailRequest();
        this.mTxtTitle.setText(this.mMessageInfo.getTitle());
        this.mTxtTime.setText(this.mMessageInfo.getTimestamp());
        this.mTxtContent.setText(this.mMessageInfo.getContent());
    }

    private void getIntentExtras() {
        this.mMessageInfo = (MessageInfo) getIntent().getSerializableExtra(EXTRAS_MESSAGE_INFO);
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C07821());
    }

    private void initView() {
        this.mDataLoadingView = (DataLoadingView) findViewById(R.id.data_loadingview);
        this.mTxtTitle = (TextView) findViewById(R.id.txt_title);
        this.mTxtContent = (TextView) findViewById(R.id.txt_content);
        this.mTxtTime = (TextView) findViewById(R.id.txt_time);
    }

    private void httpGetMessageDetailRequest() {
        RequestQueue queue = NoHttp.newRequestQueue();
        GetMessageDetailRequest request = new GetMessageDetailRequest(ServerUrl.getInst().GET_TUOCHE_DETAIL_MESSAGE(), RequestMethod.POST, this.mMessageInfo.getMessageId());
        request.setmBeanClass(MessageInfo.class, -1);
        queue.add(0, request, new C07842());
    }
}
