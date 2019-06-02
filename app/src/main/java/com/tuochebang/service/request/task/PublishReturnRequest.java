package com.tuochebang.service.request.task;

import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.request.base.BaseRequest;
import com.yanzhenjie.nohttp.RequestMethod;

import org.json.JSONObject;

public class PublishReturnRequest extends BaseRequest<Object> {
    public PublishReturnRequest(String url, RequestMethod requestMethod, int typeId, String time, String begin, String end, double lat, double lng, double endlat, double endlng) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        try {
            this.mParams.put("name", MyApplication.getInstance().getUserInfo().getNickName());
            this.mParams.put("mobile", MyApplication.getInstance().getUserInfo().getMobile());
            this.mParams.put("typeId", typeId);
            this.mParams.put("time", time);
            this.mParams.put("begin", begin);
            this.mParams.put("end", end);
            this.mParams.put("b_longitude", 106.672461d);
            this.mParams.put("b_latitude", 26.593061d);
            this.mParams.put("e_longitude", 106.672461d);
            this.mParams.put("e_latitude", 26.593061d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefineRequestBodyForJson(this.mParams);
    }

    protected Object parseObject(String jsonData) {
        return "";
    }
}
