package com.demo.safe.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.demo.safe.engine.AddressDao;

/**
 * Created by ChenXingLing on 2017/2/28.
 */
public class QueryAddressActivity extends BaseActivity {

    private EditText et_phone;
    private Button bt_query;
    private TextView tv_result;
    private String address;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            tv_result.setText(address);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_phone_address);

        initUI();
    }

    private void initUI() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        tv_result = (TextView) findViewById(R.id.tv_result);
        bt_query = (Button) findViewById(R.id.bt_query);
        //点击查询
        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone.getText().toString();
                if (TextUtils.isEmpty(phone)){
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
                    et_phone.startAnimation(shake);

                    //自定义插补器
                    /*shake.setInterpolator(new Interpolator() {
                        @Override
                        public float getInterpolation(float input) {
                            CycleInterpolator
                            return 0;
                        }
                    });*/

                    //手机震动
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vibrator.vibrate(1000);//毫秒
                    //规律震动（规律:震动300毫秒停300毫秒震动300毫秒停300毫秒,-1表示只执行一次不重复）
                    vibrator.vibrate(new long[]{300,300,300,300},-1);

                } else {
                query(phone);//查询
                }
            }
        });
        //在内容发生改变的时候查询（实时查询）
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = et_phone.getText().toString();
                query(phone);//查询
            }
        });
    }

    private void query(final String phone) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                address = AddressDao.getAddress(phone);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }
}
