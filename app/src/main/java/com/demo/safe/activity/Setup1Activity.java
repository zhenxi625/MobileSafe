package com.demo.safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by ChenXingLing on 2017/2/23.
 */
public class Setup1Activity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    public void nextPage(View view) {
        Intent intent = new Intent(getApplicationContext(),Setup2Activity.class);
        startActivity(intent);
        finish();
    }

}
