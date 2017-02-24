package com.demo.safe.util;

/**
 * Created by ChenXingLing on 2017/2/9.
 */

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
