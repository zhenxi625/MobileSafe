package com.demo.safe.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.demo.safe.receiver.DeviceAdmin;

/**
 * Created by ChenXingLing on 2017/2/28.
 */

public class CommunicateActivity extends BaseActivity implements View.OnClickListener {

    private ComponentName mDeviceAdminSimple;
    private DevicePolicyManager policyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate);

        Button bt_start = (Button) findViewById(R.id.bt_start);
        bt_start.setOnClickListener(this);
        Button bt_lock = (Button) findViewById(R.id.bt_lock);
        bt_lock.setOnClickListener(this);
        Button bt_wipedata = (Button) findViewById(R.id.bt_wipedata);
        bt_wipedata.setOnClickListener(this);
        Button bt_uninstall = (Button) findViewById(R.id.bt_uninstall);
        bt_uninstall.setOnClickListener(this);

        mDeviceAdminSimple = new ComponentName(this, DeviceAdmin.class);
        policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);//设备策略管理器
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_start:
                start();
                break;
            case R.id.bt_lock:
                if (policyManager.isAdminActive(mDeviceAdminSimple)){
                    policyManager.lockNow();
                } else {
                    start();
                }
                break;
            case R.id.bt_wipedata:
                wipeData();
                break;
            case R.id.bt_uninstall:
                break;
            default:
                uninstall();
                break;
        }
    }

    public void start(){
        Intent intent1 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent1.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSimple);
        intent1.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"设备管理器");
        startActivity(intent1);
    }

    public void wipeData(){
        if (policyManager.isAdminActive(mDeviceAdminSimple)){
            policyManager.wipeData(0);//清除手机数据
            policyManager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);//清除sd卡
        } else {
            start();
        }
    }

    public void uninstall(){
        if (policyManager.isAdminActive(mDeviceAdminSimple)){
            Intent intent1 = new Intent("android.intent.action.DELETE");
            intent1.addCategory("android.intent.category.default");
            intent1.setData(Uri.parse("package:"+getPackageName()));
            startActivity(intent1);
        } else {
            start();
        }
    }
}
