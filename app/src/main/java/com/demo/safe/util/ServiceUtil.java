package com.demo.safe.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by ChenXingLing on 2017/3/1.
 */

public class ServiceUtil {

    public static boolean isRunning(Context context,String serviceName){
        ActivityManager mAm = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfoList = mAm.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo serviceInfo : serviceInfoList){
            if (serviceName.equals(serviceInfo.service.getClassName())){
                return true;
            }
        }
        return false;
    }

}
