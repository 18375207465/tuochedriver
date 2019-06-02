package com.tuochebang.service.request.task;

import com.alibaba.fastjson.JSON;
import com.tuochebang.service.request.base.BaseRequest;
import com.tuochebang.service.request.entity.ModelType;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.List;
import org.json.JSONObject;

public class GetPayAndSortMethodRequest extends BaseRequest<List<ModelType>> {
    public GetPayAndSortMethodRequest(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        setDefineRequestBodyForJson(this.mParams);
    }

    protected List<ModelType> parseObject(String jsonData) {
        return JSON.parseArray(JSON.parseObject(jsonData).getJSONArray("list").toJSONString(), ModelType.class);
    }
}
