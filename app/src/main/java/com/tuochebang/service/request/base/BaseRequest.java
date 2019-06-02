package com.tuochebang.service.request.base;

import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.framework.app.component.utils.BroadCastUtil;
import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.constant.AppConstant.BroadCastAction;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.RestRequest;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.json.JSONObject;

public abstract class BaseRequest<T> extends RestRequest<T> {
    private static final int CODE_SUCCEED = 0;
    private static final String KEY_CODE = "code";
    private static final String KEY_DATA = "data";
    private static final String KEY_MSG = "msg";
    public static final int TYPE_ARRAY = 1;
    public static final int TYPE_OBJ = 0;
    private Class<T> mBeanClass;
    protected JSONObject mParams;
    private int mParseType;

    public BaseRequest(String url) {
        this(url, RequestMethod.GET);
    }

    public BaseRequest(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
        this.mParseType = -1;
        setAccept(Headers.HEAD_VALUE_ACCEPT_ALL);
        addHeader("os", "Android");
        if (MyApplication.getInstance().isUserLogin()) {
            addHeader("token", MyApplication.getInstance().getToken());
        }
    }

    public void setmBeanClass(Class beanClass, int parseType) {
        this.mParseType = parseType;
        this.mBeanClass = beanClass;
    }

    public T parseResponse(Headers responseHeaders, byte[] responseBody) {
        String result = StringRequest.parseResponseString(responseHeaders, responseBody);
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);
        int code = jsonObject.getIntValue(KEY_CODE);
        String msg = jsonObject.getString("msg");
        Log.e("BaseRequest", code + "");
        Log.e("BaseRequest", msg);
        Log.e("BaseRequest", result);
        if (code == 0) {
            String jsonData = jsonObject.getString(KEY_DATA);
            if (this.mParseType == 0) {
                return JSON.parseObject(jsonData, this.mBeanClass);
            }
            if (this.mParseType == 1) {
                return (T) JSON.parseArray(jsonData, this.mBeanClass);
            }
            return parseObject(jsonData);
        } else if (code == 999) {
            BroadCastUtil.sendBroadCast(MyApplication.getInstance(), BroadCastAction.VALIDATA_TOKE);
            return null;
        } else if (code == -2) {
            BroadCastUtil.sendBroadCast(MyApplication.getInstance(), BroadCastAction.TOKE_EXPIRE);
            return null;
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("msg", msg);
            BroadCastUtil.sendBroadCast(MyApplication.getInstance(), BroadCastAction.REQUEST_ERROR, bundle);
            return null;
        }
    }

    protected T parseObject(String jsonData) {
        return null;
    }
}
