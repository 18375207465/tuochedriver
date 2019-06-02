package com.tuochebang.service.request.task;

import com.tuochebang.service.request.base.BaseRequest;
import com.yanzhenjie.nohttp.RequestMethod;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckAccountRequest extends BaseRequest<String> {
    public CheckAccountRequest(String url, RequestMethod requestMethod, String account) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        try {
            this.mParams.put("account", account);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefineRequestBodyForJson(this.mParams);
    }

    protected String parseObject(String jsonData) {
        try {
            return new JSONObject(jsonData).getString("checkResult");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
