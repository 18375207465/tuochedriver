package com.tuochebang.service.request.task;

import android.text.TextUtils;
import com.tuochebang.service.request.base.BaseRequest;
import com.tuochebang.service.request.entity.LoginInfo;
import com.yanzhenjie.nohttp.RequestMethod;

import org.json.JSONException;
import org.json.JSONObject;

public class EditUserRequest extends BaseRequest<LoginInfo> {
    public EditUserRequest(String url, RequestMethod requestMethod, String picture, String nickName) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        if (picture != null) {
            try {
                if (!TextUtils.isEmpty(picture)) {
                    this.mParams.put("picture", picture);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            this.mParams.put("nickName", nickName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setDefineRequestBodyForJson(this.mParams);
    }
}
