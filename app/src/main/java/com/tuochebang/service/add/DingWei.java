package com.tuochebang.service.add;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import java.util.List;

public abstract class DingWei {
    public DingWei(Context context, Bundle bundle){
        this.context=context;
        this.bundle=bundle;
    }
    private MapView mMapView;
    private AMap aMap;
    private Context context;
    private Bundle bundle;
    private String addressName="";//返回的地址

    public void getAddressName(){
        mMapView=new MapView(context);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(bundle);
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        // myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
       //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                double w=location.getLatitude();//获取纬度
                double j=location.getLongitude();//获取经度
                final LatLng mLatlng = new LatLng(w, j);
              //  Log.i("Flog","精度-"+j+"    维度-"+w);
                //location.getAccuracy();//获取精度信息
                //逆地理编码（坐标转地址）
                GeocodeSearch geocoderSearch = new GeocodeSearch(context);
                geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                    @Override
                    public void onRegeocodeSearched(RegeocodeResult result, int i) {
                        if (i == 1000) {
                            if (result != null && result.getRegeocodeAddress() != null
                                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                               // addressName = result.getRegeocodeAddress().getFormatAddress();//获取地址名称
                                String cs=result.getRegeocodeAddress().getCity();//城市
                                String qu=result.getRegeocodeAddress().getDistrict();//区
                                List<PoiItem> list = result.getRegeocodeAddress().getPois();
                                addressName=cs+qu+list.get(0)+"附近";
                            }
                        }
                        mMapView.onDestroy();
                        addressName(addressName,mLatlng);
                        //Log.i("Flog","地理逆转-"+i);
                    }
                    @Override
                    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                        mMapView.onDestroy();//关闭
                        addressName(addressName,mLatlng);
                        //Log.i("Flog","地理逆转请求失败-"+i);
                    }
                });

                // LatLonPoint latLonPoint = new LatLonPoint(39.90865, 116.39751);
                LatLonPoint latLonPoint = new LatLonPoint(w, j);
                // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
                geocoderSearch.getFromLocationAsyn(query);//请求
            }
        }); ;
    }

    public abstract void addressName(String name,LatLng latLng);


}
