package com.tuochebang.service.request.task;

import com.alibaba.fastjson.JSON;
import com.tuochebang.service.request.base.BaseRequest;
import com.tuochebang.service.request.entity.ReturnCarInfo;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.List;
import org.json.JSONObject;

public class GetUserReturnsCarRequest extends BaseRequest<List<ReturnCarInfo>> {
    public GetUserReturnsCarRequest(String url, RequestMethod requestMethod, int status, int pageNo) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        try {
            this.mParams.put("status", status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setHeader("pagesize", String.valueOf(10));
        setHeader("pageno", String.valueOf(pageNo));
        setDefineRequestBodyForJson(this.mParams);
    }

    protected List<ReturnCarInfo> parseObject(String jsonData) {
        return JSON.parseArray(JSON.parseObject(jsonData).getJSONArray("returns").toJSONString(), ReturnCarInfo.class);
    }
}
