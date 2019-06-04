package com.tuochebang.service.request.task;

import android.text.TextUtils;

import com.tuochebang.service.request.base.BaseRequest;
import com.tuochebang.service.request.entity.AdminInfo;
import com.tuochebang.service.request.entity.LoginInfo;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.json.JSONObject;

public class RegisterAdminRequest extends BaseRequest<LoginInfo> {
    public RegisterAdminRequest(String url, RequestMethod requestMethod, AdminInfo info) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        try {
            this.mParams.put("picture0", info.getPicture0());
            this.mParams.put("picture1", info.getPicture1());
            this.mParams.put("name", info.getName());
            this.mParams.put("businessLicense", info.getBusinessLicense());
            this.mParams.put("address", info.getAddress());
            this.mParams.put("password", info.getPassword());
            this.mParams.put("account", info.getAccount());
            this.mParams.put("corporate", info.getCorporate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefineRequestBodyForJson(this.mParams);
    }

    @Override
    public LoginInfo parseResponse(Headers responseHeaders, byte[] responseBody) {
        String result = StringRequest.parseResponseString(responseHeaders, responseBody);
        if (TextUtils.isEmpty(result)) {
            return new LoginInfo();
        }
        return super.parseResponse(responseHeaders, responseBody);
    }

    protected LoginInfo parseObject(String jsonData) {
        return new LoginInfo();
    }
}
