package com.tuochebang.service.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tuochebang.service.R;
import com.tuochebang.service.adapter.FilterTypeListAdapter;
import com.tuochebang.service.request.entity.ModelType;
import java.util.ArrayList;

public class SelectItemActivity extends Activity {
    public static String ACCOUNT_TYPE = "账户类型";
    public static String BIANSU_TYPE = "变速箱";
    public static String CAR_TYPE = "车型";
    public static String EXTRAS_CHILDTYPE = LocationMapActivity.EXTRAS_TYPE;
    public static String EXTRAS_LIST_DATA = "extras_list_data";
    public static String EXTRAS_SELECT_NAME = "extras_select_name";
    public static String PAY_TYPE = "支付";
    public static String QUDONG_TYPE = "驱动";
    public static String REQUEST_TYPE = "请求类型";
    public static int SELECT_ITEM_CODE = 1;
    private FilterTypeListAdapter mAdapter;
    private ArrayList<ModelType> mChilds;
    private ListView mListView;
    private ModelType mSelectName;
    private TextView mTxtSelectTitle;
    private String mType;

    /* renamed from: com.tuochebang.service.ui.SelectItemActivity$1 */
    class C06681 implements OnItemClickListener {
        C06681() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            ModelType item = (ModelType) SelectItemActivity.this.mAdapter.getItem(position);
            SelectItemActivity.this.mSelectName = item;
            SelectItemActivity.this.mAdapter.setSelectFilterName(item);
            SelectItemActivity.this.onBtnSureClick();
        }
    }

    /* renamed from: com.tuochebang.service.ui.SelectItemActivity$2 */
    class C06692 implements OnClickListener {
        C06692() {
        }

        public void onClick(View v) {
            SelectItemActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiivty_select_item);
        getIntentExtras();
        initView();
    }

    private void getIntentExtras() {
        this.mChilds = (ArrayList) getIntent().getSerializableExtra(EXTRAS_LIST_DATA);
        this.mType = getIntent().getStringExtra(EXTRAS_CHILDTYPE);
    }

    private void initView() {
        this.mListView = (ListView) findViewById(R.id.lv_list);
        this.mAdapter = new FilterTypeListAdapter(this);
        this.mListView.setAdapter(this.mAdapter);
        if (this.mChilds != null) {
            this.mAdapter.setList(this.mChilds);
        }
        this.mListView.setOnItemClickListener(new C06681());
        this.mTxtSelectTitle = (TextView) findViewById(R.id.tcb_selct_title);
        this.mTxtSelectTitle.setText(this.mType);
        findViewById(R.id.ll_container).setOnClickListener(new C06692());
    }

    private void onBtnSureClick() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRAS_SELECT_NAME, this.mSelectName);
        bundle.putString(EXTRAS_CHILDTYPE, this.mType);
        intent.putExtras(bundle);
        setResult(2, intent);
        finish();
    }
}
