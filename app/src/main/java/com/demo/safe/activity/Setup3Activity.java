package com.demo.safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by ChenXingLing on 2017/2/23.
 */
public class Setup3Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
    }

    public void preBtn(View view) {
        Intent intent = new Intent(getApplicationContext(),Setup2Activity.class);
        startActivity(intent);
        finish();
    }

    public void nextPage(View view) {
        Intent intent = new Intent(getApplicationContext(),Setup4Activity.class);
        startActivity(intent);
        finish();
    }
}
