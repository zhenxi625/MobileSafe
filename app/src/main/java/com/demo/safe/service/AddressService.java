package com.demo.safe.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.demo.safe.util.LogUtil;

/**
 * Created by ChenXingLing on 2017/2/28.
 */

public class AddressService extends Service {

    private static final String tag = "AddressService";

    private TelephonyManager mTM;
    private MyPhoneStateListener mPhoneStateListener;

    @Override
    public void onCreate() {
        //1.电话管理者对象
        mTM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //2.监听电话状态
        mPhoneStateListener = new MyPhoneStateListener();
        mTM.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (mTM != null && mPhoneStateListener != null) {
            //取消监听电话状态
            mTM.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        super.onDestroy();
    }

    class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    //电话发生改变
                    LogUtil.i(tag, "电话发生改变");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //空闲
                    LogUtil.i(tag, "空闲");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //响铃
                    LogUtil.i(tag, "响铃");
                    break;
                default:
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }
}
