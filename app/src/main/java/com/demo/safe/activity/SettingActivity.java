package com.demo.safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.demo.safe.service.AddressService;
import com.demo.safe.util.ConstantValue;
import com.demo.safe.util.MyApplication;
import com.demo.safe.util.ServiceUtil;
import com.demo.safe.util.SpUtils;
import com.demo.safe.view.SettingItemView;

/**
 * Created by ChenXingLing on 2017/2/22.
 */

public class SettingActivity extends BaseActivity {
    private static final String tag = "SettingActivity";

    private SettingItemView siv_update;
    private SettingItemView siv_phone_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUpdate();
        initAddress();
    }

    private void initAddress() {
        siv_phone_address = (SettingItemView) findViewById(R.id.siv_phone_address);
        boolean isRunning = ServiceUtil.isRunning(this, "com.demo.safe.service.AddressService");
//        boolean open_phone_address = SpUtils.getBoolean(this, ConstantValue.OPEN_PHONE_ADDRESS, false);
        siv_phone_address.setCheck(isRunning);
        siv_phone_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheckPhoneAddress = siv_phone_address.isCheck();
                siv_phone_address.setCheck(!isCheckPhoneAddress);
                if (!isCheckPhoneAddress) {
                    startService(new Intent(getApplicationContext(), AddressService.class));
                } else {
                    stopService(new Intent(getApplicationContext(), AddressService.class));
                }
                SpUtils.putBoolean(MyApplication.getContext(), ConstantValue.OPEN_PHONE_ADDRESS, !isCheckPhoneAddress);
            }
        });
    }

    private void initUpdate() {
        siv_update = (SettingItemView) findViewById(R.id.siv_update);
        boolean open_update = SpUtils.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
        siv_update.setCheck(open_update);
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = siv_update.isCheck();
                siv_update.setCheck(!isCheck);
                SpUtils.putBoolean(MyApplication.getContext(), ConstantValue.OPEN_UPDATE, !isCheck);
            }
        });
    }


}
