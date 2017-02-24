package com.demo.safe.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ChenXingLing on 2017/2/22.
 */

public class ToastUtil {

    public static void show(Context context,String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
