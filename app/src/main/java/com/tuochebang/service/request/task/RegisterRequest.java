package com.tuochebang.service.request.task;

import com.tuochebang.service.request.base.BaseRequest;
import com.yanzhenjie.nohttp.RequestMethod;

import org.json.JSONObject;

public class RegisterRequest extends BaseRequest<Object> {
    public RegisterRequest(String url, RequestMethod requestMethod, String masterAccount, String password, String mobile) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        try {
            this.mParams.put("masterAccount", masterAccount);
            this.mParams.put("password", password);
            this.mParams.put("mobile", mobile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefineRequestBodyForJson(this.mParams);
    }
}
