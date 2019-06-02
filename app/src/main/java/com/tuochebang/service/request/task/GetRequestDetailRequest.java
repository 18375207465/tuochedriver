package com.tuochebang.service.request.task;

import com.alibaba.fastjson.JSON;
import com.tuochebang.service.request.base.BaseRequest;
import com.tuochebang.service.request.entity.TuocheRequestInfo;
import com.yanzhenjie.nohttp.RequestMethod;

import org.json.JSONObject;

public class GetRequestDetailRequest extends BaseRequest<TuocheRequestInfo> {
    public GetRequestDetailRequest(String url, RequestMethod requestMethod, int requestId) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        try {
            this.mParams.put("requestId", String.valueOf(requestId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefineRequestBodyForJson(this.mParams);
    }

    protected TuocheRequestInfo parseObject(String jsonData) {
        return (TuocheRequestInfo) JSON.parseObject(JSON.parseObject(jsonData).getJSONObject("order").toJSONString(), TuocheRequestInfo.class);
    }
}
