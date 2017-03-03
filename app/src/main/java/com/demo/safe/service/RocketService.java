package com.demo.safe.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.demo.safe.activity.BackGroundActivity;
import com.demo.safe.activity.R;

/**
 * Created by ChenXingLing on 2017/3/2.
 */

public class RocketService extends Service {

    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private WindowManager mWM;
    private int screenWidth;
    private int screenHeight;
    private View mViewRocket;
    private WindowManager.LayoutParams params;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            params.y = (int) msg.obj;
            //更新所在位置
            mWM.updateViewLayout(mViewRocket,params);
        }
    };

    @Override
    public void onCreate() {
        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);

        DisplayMetrics dm = new DisplayMetrics();
        mWM.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        showRocket();
        super.onCreate();
    }

    private void showRocket() {
        params = mParams;
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

        //定义吐司所在的布局，并将其转换成view对象，添加至窗体
        mViewRocket = View.inflate(this, R.layout.toast_view, null);

        ImageView iv_rocket = (ImageView) mViewRocket.findViewById(R.id.iv_rocket);
        AnimationDrawable animationDrawable = (AnimationDrawable) iv_rocket.getBackground();
        animationDrawable.start();

        mWM.addView(mViewRocket, params);

        mViewRocket.setOnTouchListener(new View.OnTouchListener() {
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
                        if (params.x > screenWidth - mViewRocket.getWidth()) {
                            params.x = screenWidth - mViewRocket.getWidth();
                        }
                        if (params.y > screenHeight - mViewRocket.getHeight() - 22) {
                            params.y = screenHeight - mViewRocket.getHeight() - 22;
                        }

                        //更新窗体中吐司位置
                        mWM.updateViewLayout(mViewRocket, params);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (params.x > 100 && params.x < 200 && params.y > 350) {
                            //发射火箭
                            sendRocket();
                            //尾气效果
                            Intent intent = new Intent(getApplicationContext(),BackGroundActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void sendRocket() {
        for (int i = 0; i < 11; i++) {
            int height = 350 - i * 35;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = Message.obtain();
            message.obj = height;
            handler.sendMessage(message);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (mWM != null && mViewRocket != null) {
            mWM.removeView(mViewRocket);
        }
        super.onDestroy();
    }
}
