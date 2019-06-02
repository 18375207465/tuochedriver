package com.tuochebang.service.request.task;

import com.alibaba.fastjson.JSON;
import com.tuochebang.service.request.base.BaseRequest;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.entity.MessageInfo;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.List;
import org.json.JSONObject;

public class GetUserMessageRequest extends BaseRequest<List<MessageInfo>> {
    public GetUserMessageRequest(String url, RequestMethod requestMethod, int pageNo) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        setHeader("pagesize", String.valueOf(10));
        setHeader("pageno", String.valueOf(pageNo));
        setDefineRequestBodyForJson(this.mParams);
    }

    protected List<MessageInfo> parseObject(String jsonData) {
        return JSON.parseArray(JSON.parseObject(jsonData).getJSONArray(ServerUrl.MESSAGE).toJSONString(), MessageInfo.class);
    }
}
