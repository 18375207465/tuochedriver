package com.tuochebang.service.request.task;

import android.text.TextUtils;

import com.tuochebang.service.request.base.BaseRequest;
import com.tuochebang.service.request.entity.LoginInfo;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.json.JSONObject;

public class PersonRegisterRequest extends BaseRequest<Object> {
    public PersonRegisterRequest(String url, RequestMethod requestMethod, String account, String password, String name, String businessLicense, String picture0, String picture1) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        try {
            this.mParams.put("account", account);
            this.mParams.put("password", password);
            this.mParams.put("name", name);
            this.mParams.put("businessLicense", businessLicense);
            this.mParams.put("picture0", picture0);
            this.mParams.put("picture1", picture1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefineRequestBodyForJson(mParams);
    }

    @Override
    public Object parseResponse(Headers responseHeaders, byte[] responseBody) {
        String result = StringRequest.parseResponseString(responseHeaders, responseBody);
        if (TextUtils.isEmpty(result)) {
            return new Object();
        }
        return super.parseResponse(responseHeaders, responseBody);
    }

    protected Object parseObject(String jsonData) {
        return "";
    }
}
