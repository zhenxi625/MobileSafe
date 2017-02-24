package com.demo.safe.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by ChenXingLing on 2017/2/16.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
