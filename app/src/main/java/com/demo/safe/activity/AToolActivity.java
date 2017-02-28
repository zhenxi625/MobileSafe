package com.demo.safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ChenXingLing on 2017/2/28.
 */
public class AToolActivity extends BaseActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_tool);

        initPhontAddress();

        textView = (TextView) findViewById(R.id.tv_query_phone_address);
    }

    private void initPhontAddress() {
        textView = (TextView) findViewById(R.id.tv_query_phone_address);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),QueryAddressActivity.class);
                startActivity(intent1);
            }
        });
    }
}
