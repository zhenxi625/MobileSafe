package com.demo.safe.activity;

import android.content.Intent;
import android.os.Bundle;

import com.demo.safe.util.ConstantValue;
import com.demo.safe.util.SpUtils;

/**
 * Created by ChenXingLing on 2017/2/23.
 */
public class SetupOverActivity extends BaseActivity{

    private static final String tag = "SetupOverActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean setup_over = SpUtils.getBoolean(this, ConstantValue.SETUP_OVER,false);
        if (setup_over){
            setContentView(R.layout.activity_setup_over);
        } else {
            Intent intent1 = new Intent(this,Setup1Activity.class);
            startActivity(intent1);
            finish();
        }
    }
}
