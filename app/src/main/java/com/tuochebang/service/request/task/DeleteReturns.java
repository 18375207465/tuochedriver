package com.tuochebang.service.request.task;

import com.tuochebang.service.request.base.BaseRequest;
import com.yanzhenjie.nohttp.RequestMethod;

import org.json.JSONObject;

public class DeleteReturns extends BaseRequest<Object> {
    public DeleteReturns(String url, RequestMethod requestMethod, String returnId) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        try {
            this.mParams.put("returnId", returnId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setHeader("pagesize", String.valueOf(10));
        setHeader("pageno", String.valueOf(10));
        setDefineRequestBodyForJson(this.mParams);
    }

    protected Object parseObject(String jsonData) {
        return "";
    }
}
