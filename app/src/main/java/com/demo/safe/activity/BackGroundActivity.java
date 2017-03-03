package com.demo.safe.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

/**
 * Created by ChenXingLing on 2017/3/3.
 */
public class BackGroundActivity extends BaseActivity {

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backgroud);
        ImageView iv_bottom = (ImageView) findViewById(R.id.iv_bottom);
        ImageView iv_top = (ImageView) findViewById(R.id.iv_top);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(500);
        iv_top.startAnimation(alphaAnimation);
        iv_bottom.startAnimation(alphaAnimation);

        handler.sendEmptyMessageDelayed(0,1000);


    }
}
