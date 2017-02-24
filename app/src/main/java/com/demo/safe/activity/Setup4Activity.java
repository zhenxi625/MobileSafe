package com.demo.safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.demo.safe.util.ConstantValue;
import com.demo.safe.util.SpUtils;

/**
 * Created by ChenXingLing on 2017/2/23.
 */
public class Setup4Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
    }

    public void preBtn(View view) {
        Intent intent = new Intent(getApplicationContext(),Setup3Activity.class);
        startActivity(intent);
        finish();
    }

    public void nextPage(View view) {
        Intent intent = new Intent(getApplicationContext(),SetupOverActivity.class);
        startActivity(intent);
        finish();
        SpUtils.putBoolean(this, ConstantValue.SETUP_OVER,true);
    }
}
