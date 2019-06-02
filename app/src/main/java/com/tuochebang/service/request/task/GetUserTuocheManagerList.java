package com.tuochebang.service.request.task;

import com.alibaba.fastjson.JSON;
import com.tuochebang.service.request.base.BaseRequest;
import com.tuochebang.service.request.entity.Trailer;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.List;
import org.json.JSONObject;

public class GetUserTuocheManagerList extends BaseRequest<List<Trailer>> {
    public GetUserTuocheManagerList(String url, RequestMethod requestMethod, int pageNo) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        setHeader("pagesize", String.valueOf(10));
        setHeader("pageno", String.valueOf(pageNo));
        setDefineRequestBodyForJson(this.mParams);
    }

    protected List<Trailer> parseObject(String jsonData) {
        return JSON.parseArray(JSON.parseObject(jsonData).getJSONArray("driver").toJSONString(), Trailer.class);
    }
}
