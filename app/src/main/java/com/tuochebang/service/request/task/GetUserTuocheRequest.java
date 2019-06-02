package com.tuochebang.service.request.task;

import com.alibaba.fastjson.JSON;
import com.tuochebang.service.request.base.BaseRequest;
import com.tuochebang.service.request.entity.TuocheRequestInfo;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.List;
import org.json.JSONObject;

public class GetUserTuocheRequest extends BaseRequest<List<TuocheRequestInfo>> {
    public GetUserTuocheRequest(String url, RequestMethod requestMethod, String status, int pageNo) {
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

    protected List<TuocheRequestInfo> parseObject(String jsonData) {
        return JSON.parseArray(JSON.parseObject(jsonData).getJSONArray("order").toJSONString(), TuocheRequestInfo.class);
    }
}
