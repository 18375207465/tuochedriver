package com.tuochebang.service.request.task;

import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.request.base.BaseRequest;
import com.yanzhenjie.nohttp.RequestMethod;

import org.json.JSONObject;

public class EditReturnRequest extends BaseRequest<Object> {
    public EditReturnRequest(String url, RequestMethod requestMethod, int typeId, String time, String begin, String end, double lat, double lng, double endlat, double endlng, String returnId) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        try {
            this.mParams.put("name", MyApplication.getInstance().getUserInfo().getNickName());
            this.mParams.put("mobile", MyApplication.getInstance().getUserInfo().getMobile());
            this.mParams.put("typeId", typeId);
            this.mParams.put("time", time);
            this.mParams.put("begin", begin);
            this.mParams.put("end", end);
            this.mParams.put("b_longitude", lng);
            this.mParams.put("b_latitude", lat);
            this.mParams.put("e_longitude", endlng);
            this.mParams.put("e_latitude", endlat);
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
