package com.tuochebang.service.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.framework.app.component.utils.DateUtil;
import com.framework.app.component.utils.ImageLoaderUtil;
import com.framework.app.component.utils.LoggerUtil;
import com.tuochebang.service.cache.FileUtil;
import com.tuochebang.service.constant.AppConstant;
import com.tuochebang.service.request.entity.UserInfo;
import com.yanzhenjie.nohttp.NoHttp;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication mInstance;
    private String OSSaccessKeyId = "9jxXe7WbOWVT4AP8";
    private String OSSaccessKeySecret = "75KfTcsnJx8hLNMySCt48NJImXcNAO";
    private String mLocationCity = "成都";
    private AMapLocationClient mLocationClient;
    private double mLocationLat = 0.0d;
    public AMapLocationListener mLocationListener = new C06571();
    private double mLocationLong = 0.0d;
    private AMapLocationClientOption mLocationOption;
    private String mToken;
    private UserInfo mUserInfo;
    private OSS oss;

    /* renamed from: com.tuochebang.service.app.MyApplication$1 */
    class C06571 implements AMapLocationListener {
        C06571() {
        }

        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation == null) {
                return;
            }
            if (aMapLocation.getErrorCode() == 0) {
                aMapLocation.getLocationType();
                MyApplication.this.mLocationLat = aMapLocation.getLatitude();
                MyApplication.this.mLocationLong = aMapLocation.getLongitude();
                aMapLocation.getAccuracy();
                new SimpleDateFormat(DateUtil.YYYY_MM_DD_HH_MM_SS).format(new Date(aMapLocation.getTime()));
                aMapLocation.getAddress();
                aMapLocation.getCountry();
                aMapLocation.getProvince();
                MyApplication.this.mLocationCity = aMapLocation.getCity();
                aMapLocation.getDistrict();
                aMapLocation.getStreet();
                aMapLocation.getStreetNum();
                aMapLocation.getCityCode();
                aMapLocation.getAdCode();
                aMapLocation.getAoiName();
                LoggerUtil.e("amap info: ", aMapLocation.toString());
                return;
            }
            Log.e("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
        }
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    public void onCreate() {
        super.onCreate();
        mInstance = this;
        init();
    }

    private void init() {
        initHttp();
        initImageLoader();
        initLocalData();
        initAliyunOSS();
        initAmap();
    }

    private void initAmap() {
        this.mLocationClient = new AMapLocationClient(getApplicationContext());
        this.mLocationClient.setLocationListener(this.mLocationListener);
        this.mLocationOption = new AMapLocationClientOption();
        this.mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        this.mLocationOption.setNeedAddress(true);
        this.mLocationOption.setOnceLocation(false);
        this.mLocationOption.setWifiActiveScan(true);
        this.mLocationOption.setMockEnable(false);
        this.mLocationOption.setInterval(60000);
        this.mLocationClient.setLocationOption(this.mLocationOption);
        this.mLocationClient.startLocation();
    }

    private void initAliyunOSS() {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(this.OSSaccessKeyId, this.OSSaccessKeySecret);
        this.oss = new OSSClient(getApplicationContext(), "oss-cn-qingdao.aliyuncs.com", credentialProvider);
    }

    private void initLocalData() {
        this.mUserInfo = (UserInfo) FileUtil.readFile(mInstance, AppConstant.FLAG_USER_INFO);
        this.mToken = (String) FileUtil.readFile(mInstance, AppConstant.FALG_LOGIN_INFO);
    }

    private void initImageLoader() {
        ImageLoaderUtil.initImageLoader(mInstance);
    }

    private void initHttp() {
        NoHttp.initialize(this);
    }

    public void setUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
        FileUtil.saveFile(this, AppConstant.FLAG_USER_INFO, userInfo);
    }

    public void setLoginInfo(String token) {
        this.mToken = token;
        FileUtil.saveFile(this, AppConstant.FALG_LOGIN_INFO, token);
    }

    public String getToken() {
        return this.mToken;
    }

    public UserInfo getUserInfo() {
        return this.mUserInfo;
    }

    public boolean isUserLogin() {
        return this.mToken != null;
    }

    public OSS getOssClient() {
        return this.oss;
    }

    public void setLocLong(double mLocationLong) {
        this.mLocationLong = mLocationLong;
    }

    public void setLocLat(double mLocationLat) {
        this.mLocationLat = mLocationLat;
    }

    public double getmLocationLong() {
        return this.mLocationLong;
    }

    public double getmLocationLat() {
        return this.mLocationLat;
    }

    public void onTerminate() {
        super.onTerminate();
        this.mLocationClient.onDestroy();
        this.mLocationClient.stopLocation();
    }
}
