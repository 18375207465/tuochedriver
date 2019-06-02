package com.tuochebang.service.request.task;

import com.alibaba.fastjson.JSON;
import com.tuochebang.service.request.base.BaseRequest;
import com.tuochebang.service.request.entity.ReturnCarInfo;
import com.yanzhenjie.nohttp.RequestMethod;

import org.json.JSONObject;

public class GetReturnDetailRequest extends BaseRequest<ReturnCarInfo> {
    public GetReturnDetailRequest(String url, RequestMethod requestMethod, int returnId) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        try {
            this.mParams.put("returnId", String.valueOf(returnId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefineRequestBodyForJson(this.mParams);
    }

    protected ReturnCarInfo parseObject(String jsonData) {
        return (ReturnCarInfo) JSON.parseObject(JSON.parseObject(jsonData).getJSONObject("returns").toJSONString(), ReturnCarInfo.class);
    }
}
