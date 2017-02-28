package com.demo.safe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.LocationProvider;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.demo.safe.activity.R;
import com.demo.safe.util.ConstantValue;
import com.demo.safe.util.SpUtils;

/**
 * Created by ChenXingLing on 2017/2/27.
 */

public class SmsReceiver extends BroadcastReceiver {

    private DevicePolicyManager policyManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        policyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);//设备策略管理器
        //是否开启防盗保护
        boolean open_security = SpUtils.getBoolean(context, ConstantValue.OPEN_SECURITY,false);
        //获取短信内容
        if (open_security){
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objects){
                //获取短信对象
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
//                String originatingAddress = smsMessage.getOriginatingAddress();//发送短点的电话号码
                String messageBody = smsMessage.getDisplayMessageBody();//短信内容

                if (messageBody.contains("#*alarm*#")){
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
                    mediaPlayer.setLooping(true);//无限循环
                    mediaPlayer.start();
                }
                if (messageBody.contains("#*location*#")){
                    context.startService(new Intent(context, LocationReceiver.class));
                }
                if (messageBody.contains("#*lockscreen*#")){
                    policyManager.lockNow();
                }
                if (messageBody.contains("#*wipedata*#")){
                    policyManager.wipeData(0);//清除手机数据
                    policyManager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);//清除sd卡
                }
            }
        }
    }
}
