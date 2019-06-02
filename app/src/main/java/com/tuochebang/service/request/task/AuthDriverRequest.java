package com.tuochebang.service.request.task;

import com.tuochebang.service.request.base.BaseRequest;
import com.yanzhenjie.nohttp.RequestMethod;

import org.json.JSONObject;

public class AuthDriverRequest extends BaseRequest<Object> {
    public AuthDriverRequest(String url, RequestMethod requestMethod, String authId, String status) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        try {
            this.mParams.put("authId", authId);
            this.mParams.put("status", status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefineRequestBodyForJson(this.mParams);
    }
}
