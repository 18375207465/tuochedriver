package com.tuochebang.service.ui.returns;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.tuochebang.service.R;
import com.tuochebang.service.base.BaseActivity;
import com.tuochebang.service.constant.AppConstant;
import com.tuochebang.service.ui.LocationMapActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectLocationActivity extends BaseActivity implements TextWatcher, InputtipsListener {
    private SimpleAdapter aAdapter;
    private LatLng latlng;
    private String mAddress;
    private AutoCompleteTextView mInputText;
    private ListView mLvInput;
    private LatLonPoint mPoint;
    private Toolbar mToolBar;

    /* renamed from: com.tuochebang.service.ui.returns.SelectLocationActivity$1 */
    class C07711 implements OnItemClickListener {
        C07711() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Map<String, Object> mMap = (Map) SelectLocationActivity.this.aAdapter.getItem(position);
            SelectLocationActivity.this.mPoint = (LatLonPoint) mMap.get("point");
            SelectLocationActivity.this.mAddress = ((String) mMap.get("address")) + ((String) mMap.get("name"));
            SelectLocationActivity.this.latlng = new LatLng(SelectLocationActivity.this.mPoint.getLatitude(), SelectLocationActivity.this.mPoint.getLongitude());
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(LocationMapActivity.EXTRAS_ADDRESS, SelectLocationActivity.this.mAddress);
            bundle.putParcelable(LocationMapActivity.EXTRAS_LOC, SelectLocationActivity.this.latlng);
            intent.putExtras(bundle);
            SelectLocationActivity.this.setResult(-1, intent);
            SelectLocationActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.service.ui.returns.SelectLocationActivity$2 */
    class C07722 implements OnClickListener {
        C07722() {
        }

        public void onClick(View v) {
            SelectLocationActivity.this.onBtnBack();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        initToolBar();
        initView();
    }

    private void initView() {
        this.mLvInput = (ListView) findViewById(R.id.inputlist);
        this.mInputText = (AutoCompleteTextView) findViewById(R.id.input_edittext);
        this.mInputText.addTextChangedListener(this);
        this.mLvInput.setOnItemClickListener(new C07711());
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C07722());
    }

    private void onBtnBack() {
    }

    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == 1000) {
            List<HashMap<String, Object>> listString = new ArrayList();
            for (int i = 0; i < tipList.size(); i++) {
                HashMap<String, Object> map = new HashMap();
                map.put("name", ((Tip) tipList.get(i)).getName());
                map.put("address", ((Tip) tipList.get(i)).getDistrict());
                map.put("point", ((Tip) tipList.get(i)).getPoint());
                listString.add(map);
            }
            this.aAdapter = new SimpleAdapter(getApplicationContext(), listString, R.layout.adapter_view_location_tip, new String[]{"name", "address"}, new int[]{R.id.poi_field_id, R.id.poi_value_id});
            this.mLvInput.setAdapter(this.aAdapter);
            this.aAdapter.notifyDataSetChanged();
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        InputtipsQuery inputquery = new InputtipsQuery(s.toString().trim(), AppConstant.DEFAULT_CITY);
        inputquery.setCityLimit(true);
        Inputtips inputTips = new Inputtips((Context) this, inputquery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
    }

    public void afterTextChanged(Editable s) {
    }
}
