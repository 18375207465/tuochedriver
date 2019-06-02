package com.tuochebang.service.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.framework.app.component.utils.ActivityUtil;
import com.tuochebang.service.R;
import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.base.BaseActivity;
import com.tuochebang.service.ui.returns.SelectLocationActivity;

public class LocationMapActivity extends BaseActivity implements AMapLocationListener, LocationSource, OnGeocodeSearchListener, OnCameraChangeListener {
    public static final String EXTRAS_ADDRESS = "extras_address";
    public static final String EXTRAS_LOC = "extras_loc";
    public static final String EXTRAS_TYPE = "extras_type";
    public static final int LOCATION_FROM = 0;
    public static final int LOCATION_TO = 1;
    private AMap aMap;
    private GeocodeSearch geocoderSearch;
    private boolean isReturn;
    private LatLng latlng;
    private String mAddress = "";
    private OnLocationChangedListener mListener;
    private double mLocationLat;
    private double mLocationLong;
    private AMapLocationClientOption mLocationOption;
    private Toolbar mToolBar;
    private TextView mTxtLocText;
    private TextView mTxtSure;
    private int mType = 0;
    private MapView mapView;
    private MarkerOptions markerOption;
    private AMapLocationClient mlocationClient;

    /* renamed from: com.tuochebang.service.ui.LocationMapActivity$1 */
    class C06621 implements OnClickListener {
        C06621() {
        }

        public void onClick(View v) {
            ActivityUtil.next(LocationMapActivity.this, SelectLocationActivity.class, 100);
        }
    }

    /* renamed from: com.tuochebang.service.ui.LocationMapActivity$2 */
    class C06632 implements OnClickListener {
        C06632() {
        }

        public void onClick(View v) {
            LocationMapActivity.this.onBtnBack();
        }
    }

    /* renamed from: com.tuochebang.service.ui.LocationMapActivity$3 */
    class C06643 implements OnClickListener {
        C06643() {
        }

        public void onClick(View v) {
            LocationMapActivity.this.onBtnBack();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getIntentExtras();
        initToolBar();
        initView(savedInstanceState);
    }

    private void getIntentExtras() {
        this.mType = getIntent().getIntExtra(EXTRAS_TYPE, 0);
    }

    private void initView(Bundle savedInstanceState) {
        this.mapView = (MapView) findViewById(R.id.map);
        this.mapView.onCreate(savedInstanceState);
        this.mTxtLocText = (TextView) findViewById(R.id.input_text);
        this.mTxtSure = (TextView) findViewById(R.id.tcb_sure_txt);
        if (this.aMap == null) {
            this.aMap = this.mapView.getMap();
        }
        this.aMap.setLocationSource(this);
        this.aMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.aMap.setMyLocationEnabled(true);
        this.aMap.setOnCameraChangeListener(this);
        this.mTxtLocText.setOnClickListener(new C06621());
        this.mTxtSure.setOnClickListener(new C06632());
        this.geocoderSearch = new GeocodeSearch(this);
        this.geocoderSearch.setOnGeocodeSearchListener(this);
        addMarkersToMap();
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C06643());
    }

    private void onBtnBack() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRAS_TYPE, this.mType);
        bundle.putString(EXTRAS_ADDRESS, this.mAddress);
        bundle.putParcelable("latlng", this.latlng);
        intent.putExtras(bundle);
        setResult(4, intent);
        finish();
    }

    protected void onResume() {
        super.onResume();
        this.mapView.onResume();
    }

    protected void onPause() {
        super.onPause();
        this.mapView.onPause();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mapView.onSaveInstanceState(outState);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mapView.onDestroy();
    }

    private void changeCamera(CameraUpdate update) {
        this.aMap.moveCamera(update);
    }

    public void onLocationChanged(AMapLocation aMapLocation) {
        if (this.mListener != null && aMapLocation != null) {
            if (aMapLocation == null || aMapLocation.getErrorCode() != 0) {
                Log.e("AmapErr", "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo());
                return;
            }
            this.mLocationLat = aMapLocation.getLatitude();
            this.mLocationLong = aMapLocation.getLongitude();
            MyApplication.getInstance().setLocLong(this.mLocationLong);
            MyApplication.getInstance().setLocLat(this.mLocationLat);
        }
    }

    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.mListener = onLocationChangedListener;
        if (this.mlocationClient == null) {
            this.mlocationClient = new AMapLocationClient(this);
            this.mLocationOption = new AMapLocationClientOption();
            this.mlocationClient.setLocationListener(this);
            this.mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
            this.mlocationClient.setLocationOption(this.mLocationOption);
            this.mlocationClient.startLocation();
        }
    }

    public void deactivate() {
        this.mListener = null;
        if (this.mlocationClient != null) {
            this.mlocationClient.stopLocation();
            this.mlocationClient.onDestroy();
        }
        this.mlocationClient = null;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            this.mAddress = data.getStringExtra(EXTRAS_ADDRESS);
            this.latlng = (LatLng) data.getParcelableExtra(EXTRAS_LOC);
            addMarkersToMap();
            this.mTxtLocText.setText(this.mAddress);
            this.isReturn = true;
            changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(this.latlng, 18.0f, 30.0f, 30.0f)));
        }
    }

    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode != 1000) {
        } else if (result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null) {
            if (this.isReturn) {
                this.isReturn = false;
                return;
            }
            this.mAddress = result.getRegeocodeAddress().getFormatAddress() + "附近";
            this.mAddress = this.mAddress.split(result.getRegeocodeAddress().getProvince() + result.getRegeocodeAddress().getCity() + result.getRegeocodeAddress().getDistrict())[1];
            this.mTxtLocText.setText(this.mAddress);
        }
    }

    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
    }

    private void getAddressName(final LatLonPoint latLonPoint) {
        new Thread(new Runnable() {
            public void run() {
                LocationMapActivity.this.geocoderSearch.getFromLocationAsyn(new RegeocodeQuery(latLonPoint, 20.0f, GeocodeSearch.AMAP));
            }
        }).start();
    }

    private void addMarkersToMap() {
        if (this.aMap != null) {
            this.aMap.clear();
        }
        this.markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.gps_point)).anchor(0.8f, 0.8f).position(new LatLng(MyApplication.getInstance().getmLocationLat(), MyApplication.getInstance().getmLocationLong()));
        this.aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(MyApplication.getInstance().getmLocationLat(), MyApplication.getInstance().getmLocationLong()), 15.0f));
    }

    public void onCameraChange(CameraPosition cameraPosition) {
        this.mTxtLocText.setText("正在获取地点...");
    }

    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        this.latlng = cameraPosition.target;
        getAddressName(new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude));
    }
}
