package com.demo.safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.demo.safe.util.ConstantValue;
import com.demo.safe.util.SpUtils;

/**
 * Created by ChenXingLing on 2017/2/23.
 */
public class SetupOverActivity extends BaseActivity {

    private static final String tag = "SetupOverActivity";
    private TextView tv_phone;
    private TextView tv_reset_setup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean setup_over = SpUtils.getBoolean(this, ConstantValue.SETUP_OVER, false);
        if (setup_over) {
            setContentView(R.layout.activity_setup_over);
            initUI();
        } else {
            Intent intent1 = new Intent(this, Setup1Activity.class);
            startActivity(intent1);
            finish();
        }
    }

    private void initUI() {
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        //设置联系人号码
        String phone = SpUtils.getString(this,ConstantValue.CONTACT_PHONE,"");
        tv_phone.setText(phone);
        //重新设置
        tv_reset_setup = (TextView) findViewById(R.id.tv_reset_setup);
        tv_reset_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),Setup1Activity.class);
                startActivity(intent1);

                finish();
            }
        });
    }
}
