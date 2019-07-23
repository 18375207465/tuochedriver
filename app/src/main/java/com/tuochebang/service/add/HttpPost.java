package com.tuochebang.service.add;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

public abstract class HttpPost {

    public void post(String url){
        //1.创建一个队列
        RequestQueue queue1 = NoHttp.newRequestQueue();
       //2.创建消息请求 参数1：String字符串，传网址 参数2：指定请求方式
        Request<String> request1 = NoHttp.createStringRequest(url, RequestMethod.POST);
        //3.利用队列去添加消息请求
        queue1.add(1, request1, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
                startHttp(what);
            }
            @Override
            public void onSucceed(int what, Response<String> response) {
                succeedHttp(what,response);
            }
            @Override
            public void onFailed(int what, Response<String> response) {
                failedHttp(what, response);
            }
            @Override
            public void onFinish(int what) {
                finishHttp(what);
            }
        });
    }
    public abstract void startHttp(int what);
    public abstract void succeedHttp(int what, Response<String> response);
    public abstract void failedHttp(int what, Response<String> response);
    public abstract void finishHttp(int what);
}
