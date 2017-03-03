package com.demo.safe.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.demo.safe.util.ConstantValue;
import com.demo.safe.util.SpUtils;

/**
 * Created by ChenXingLing on 2017/3/2.
 */
public class ToastLocationActivity extends BaseActivity {

    private ImageView mIvDrag;
    private Button mBtTop;
    private Button mBtBottom;
    private int startX;
    private int startY;
    private WindowManager mWm;
    private int screenWidth;
    private int screenHeight;
    private long[] mHits = new long[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast_location);

        initUI();
    }

    private void initUI() {
        mIvDrag = (ImageView) findViewById(R.id.iv_drag);
        mBtTop = (Button) findViewById(R.id.bt_top);
        mBtBottom = (Button) findViewById(R.id.bt_bottom);

        WindowManager mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        mWM.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        int locationX = SpUtils.getInt(getApplicationContext(), ConstantValue.LOCATION_X, 0);
        int locationY = SpUtils.getInt(getApplicationContext(), ConstantValue.LOCATION_Y, 0);

        //左上角坐标哦作用在drag上
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = locationX;
        layoutParams.topMargin = locationY;
        mIvDrag.setLayoutParams(layoutParams);

        if (locationY > screenHeight/2){
            mBtBottom.setVisibility(View.INVISIBLE);
            mBtTop.setVisibility(View.VISIBLE);
        } else {
            mBtBottom.setVisibility(View.VISIBLE);
            mBtTop.setVisibility(View.INVISIBLE);
        }

        mIvDrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.arraycopy(mHits,1,mHits,0,mHits.length-1);
                mHits[mHits.length-1] = SystemClock.uptimeMillis();
                if (mHits[mHits.length-1]-mHits[0] < 500){
                    int left = screenWidth/2-mIvDrag.getWidth()/2;
                    int top = screenHeight/2-mIvDrag.getHeight()/2;
                    int right = screenWidth/2 + mIvDrag.getWidth()/2;
                    int bottom = screenHeight/2 + mIvDrag.getHeight()/2;
                    mIvDrag.layout(left, top, right, bottom);
                    SpUtils.putInt(getApplicationContext(), ConstantValue.LOCATION_X, mIvDrag.getLeft());
                    SpUtils.putInt(getApplicationContext(), ConstantValue.LOCATION_Y, mIvDrag.getTop());
                }
            }
        });

        //监听拖拽过程(按下，拖动，抬起)
        mIvDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();

                        int disX = moveX - startX;
                        int disY = moveY - startY;

                        //当前控件所在屏幕的位置
                        int left = mIvDrag.getLeft() + disX;
                        int top = mIvDrag.getTop() + disY;
                        int right = mIvDrag.getRight() + disX;
                        int bottom = mIvDrag.getBottom() + disY;

                        //不能拖拽出手机屏幕
                        //上下左右边缘不能超出屏幕可显示区域
                        //(屏幕高度-22=屏幕最大显示区域)
                        if (left < 0 || right > screenWidth || top < 0 || bottom > screenHeight - 22) {
                            return true;
                        }
                        if (top > screenHeight /2){
                            mBtBottom.setVisibility(View.INVISIBLE);
                            mBtTop.setVisibility(View.VISIBLE);
                        } else {
                            mBtBottom.setVisibility(View.VISIBLE);
                            mBtTop.setVisibility(View.INVISIBLE);
                        }

                        mIvDrag.layout(left, top, right, bottom);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        SpUtils.putInt(getApplicationContext(), ConstantValue.LOCATION_X, mIvDrag.getLeft());
                        SpUtils.putInt(getApplicationContext(), ConstantValue.LOCATION_Y, mIvDrag.getTop());
                        break;
                    default:
                        break;
                }
                //既要响应点击事件又要响应拖拽事件，此结果改为false
                return false;//返回false不响应事件
            }
        });
    }
}
