package com.tuochebang.service.add;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

public abstract class HttpGet {
    public void get(String url){
        //使用Nohttp进行网络访问的网址
        //   String url = "http://www.baidu.com";
        //1.创建一个队列
        RequestQueue queue = NoHttp.newRequestQueue();
        //2.创建消息请求，参数1 String字符串,传网址，参数2 指定请求的方式
        //提示：请求的数据是什么类型，就调用对应的方法，主要是中间的单词不一样
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        //3利用队列去添加消息请求  参数1：请求的标识  参数2：消息请求对象  参数3：请求的回调监听
        //请求可以并发，统一处理响应结果
        queue.add(0, request, new OnResponseListener<String>() {
            //请求开始时，回调的方法，一般做进度条对话框的加载
            @Override
            public void onStart(int what) {
                startHttp(what);
            }
            //请求成功，回调的方法，代码直接运行到主线程
            @Override
            public void onSucceed(int what, Response<String> response) {
                succeedHttp(what,response);
            }
            //网络请求失败的回调,代码直接运行到主线程
            @Override
            public void onFailed(int what, Response<String> response) {
                failedHttp(what,response);
            }
            //网络请求成功，一般隐藏进度条对话框
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
