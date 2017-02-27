package com.demo.safe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.demo.safe.util.ConstantValue;
import com.demo.safe.util.LogUtil;
import com.demo.safe.util.SpUtils;

/**
 * Created by ChenXingLing on 2017/2/27.
 */

public class BootReceiver extends BroadcastReceiver {

    private static final String tag = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.d(tag,"重启手机。。。");
        //1.手机开机后获取sim卡序列号
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simSerialNumber = manager.getSimSerialNumber();
        //2.sp中存储的序列号
        String simNum = SpUtils.getString(context, ConstantValue.SIM_NUM,"");
        //3.比对是否一致
        if (!simSerialNumber.equals(simNum)){
            //发送短信
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("5554",null,"sim change",null,null);
        }
    }
}
