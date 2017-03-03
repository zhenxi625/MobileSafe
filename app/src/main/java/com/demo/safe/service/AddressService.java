package com.demo.safe.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.demo.safe.activity.R;
import com.demo.safe.engine.AddressDao;
import com.demo.safe.util.ConstantValue;
import com.demo.safe.util.LogUtil;
import com.demo.safe.util.SpUtils;

/**
 * Created by ChenXingLing on 2017/2/28.
 */

public class AddressService extends Service {

    private static final String tag = "AddressService";
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();

    private TelephonyManager mTM;
    private MyPhoneStateListener mPhoneStateListener;
    private View mViewToast;
    private WindowManager mWM;
    private String mAddress;
    private TextView tv_toast;
    private int screenWidth;
    private int screenHeight;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            tv_toast.setText(mAddress);
        }
    };
    private int[] mImagesId;

    @Override
    public void onCreate() {
        //1.电话管理者对象
        mTM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //2.监听电话状态
        mPhoneStateListener = new MyPhoneStateListener();
        mTM.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);

        DisplayMetrics dm = new DisplayMetrics();
        mWM.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

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
                    //挂断电话，移除吐司
                    if (mWM != null && mViewToast != null) {
                        mWM.removeView(mViewToast);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //空闲
                    LogUtil.i(tag, "空闲");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //响铃
                    LogUtil.i(tag, "响铃");
                    showToast(incomingNumber);
                    break;
                default:
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    private void showToast(String incomingNumber) {
        final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        //在响铃的时候显示吐司，和电话类型保持一致
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //指定吐司所在位置
        params.gravity = Gravity.LEFT + Gravity.TOP;
        //吐司的显示效果(布局文件)
        mViewToast = View.inflate(this, R.layout.toast_view, null);
        tv_toast = (TextView) mViewToast.findViewById(R.id.tv_toast);

        mViewToast.setOnTouchListener(new View.OnTouchListener() {
            private int startX;
            private int startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();

                        int disX = moveX - startX;
                        int disY = moveY - startY;

                        params.x = params.x + disX;
                        params.y = params.y + disY;

                        if (params.x < 0) {
                            params.x = 0;
                        }
                        if (params.y < 0) {
                            params.y = 0;
                        }
                        if (params.x > screenWidth - mViewToast.getWidth()) {
                            params.x = screenWidth - mViewToast.getWidth();
                        }
                        if (params.y > screenHeight - mViewToast.getHeight() - 22) {
                            params.y = screenHeight - mViewToast.getHeight() - 22;
                        }

                        //更新窗体中吐司位置
                        mWM.updateViewLayout(mViewToast, params);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        SpUtils.putInt(getApplicationContext(), ConstantValue.LOCATION_X, params.x);
                        SpUtils.putInt(getApplicationContext(), ConstantValue.LOCATION_Y, params.y);
                        break;
                    default:
                        break;
                }
                //既要响应点击事件又要响应拖拽事件，此结果改为false
                return true;
            }
        });

        //吐司左上角x坐标
        params.x = SpUtils.getInt(getApplicationContext(), ConstantValue.LOCATION_X, 0);
        //吐司左上角y坐标
        params.y = SpUtils.getInt(getApplicationContext(), ConstantValue.LOCATION_Y, 0);

        //从SpUtils中获取颜色索引值对应的图片
        mImagesId = new int[]{R.drawable.toastbg, R.drawable.toastbgcheng,
                R.drawable.toastbglan, R.drawable.toastbghui, R.drawable.toastbglv};
        int toastStyleIndex = SpUtils.getInt(getApplicationContext(), ConstantValue.TOAST_STYLE, 0);
        tv_toast.setBackgroundResource(mImagesId[toastStyleIndex]);

        mWM.addView(mViewToast, mParams);

        query(incomingNumber);
    }

    private void query(final String incomingNumber) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAddress = AddressDao.getAddress(incomingNumber);
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }
}
