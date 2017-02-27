package com.demo.safe.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ChenXingLing on 2017/1/6.
 */

public abstract class BaseSetupActivity extends BaseActivity {

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("BaseSetupActivity", getClass().getSimpleName());
        ActivityController.addActivity(this);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() - e2.getX() > 0) {
                    showNextPage();
                }
                if (e1.getX() - e2.getX() < 0) {
                    //由左向右，上一页
                    showPrePage();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }

    //1.监听屏幕响应事件 按下、移动、抬起
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        //3.通过手势处理类，接收多种类型的事件，用作处理
        return super.onTouchEvent(event);
    }

    //下一页的方法，由子类实现
    public abstract void showNextPage();

    //上一页的方法，由子类实现
    public abstract void showPrePage();

    //点击下一页的时候，根据子类的showNextPage方法做相应跳转
    public void nextPage(View view) {
        showNextPage();
    }

    //点击上一页的时候，根据子类的showNextPage方法做相应跳转
    public void prePage(View view) {
        showPrePage();
    }

}
