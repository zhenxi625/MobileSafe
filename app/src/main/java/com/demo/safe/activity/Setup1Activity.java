package com.demo.safe.activity;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by ChenXingLing on 2017/2/23.
 */
public class Setup1Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    @Override
    public void showNextPage() {
        Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }

    @Override
    public void showPrePage() {

    }
}
