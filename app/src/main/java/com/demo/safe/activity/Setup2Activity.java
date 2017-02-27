package com.demo.safe.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.demo.safe.util.ConstantValue;
import com.demo.safe.util.SpUtils;
import com.demo.safe.util.ToastUtil;
import com.demo.safe.view.SettingItemView;

/**
 * Created by ChenXingLing on 2017/2/23.
 */
public class Setup2Activity extends BaseSetupActivity implements View.OnClickListener {

    private SettingItemView siv_sim_bind;
    private static final int REQUEST_READ_PHONE_STATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        siv_sim_bind = (SettingItemView) findViewById(R.id.siv_sim_bind);
        siv_sim_bind.setOnClickListener(this);
        initData();
    }

    @Override
    public void showNextPage() {
        String simNum = SpUtils.getString(this, ConstantValue.SIM_NUM, "");
        if (!TextUtils.isEmpty(simNum)) {
            Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        } else {
            ToastUtil.show(getApplicationContext(), "请绑定sim卡");
        }
    }

    @Override
    public void showPrePage() {
        Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getSimNumber();
                } else {
                    ToastUtil.show(getApplicationContext(), "没有权限");
                }
                break;
            default:
                break;
        }
    }

    private void initData() {
        String sim_num = SpUtils.getString(getApplicationContext(), ConstantValue.SIM_NUM, "");
        if (TextUtils.isEmpty(sim_num)) {
            siv_sim_bind.setCheck(false);
        } else {
            siv_sim_bind.setCheck(true);
        }
    }

    private void getSimNumber() {
        Context context = getApplicationContext();
        boolean isCheck = siv_sim_bind.isCheck();
        siv_sim_bind.setCheck(!isCheck);
        if (!isCheck) {
            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String simSerialNumber = manager.getSimSerialNumber();//获取sim卡的序列卡号
            SpUtils.putString(context, ConstantValue.SIM_NUM, simSerialNumber);
        } else {
            SpUtils.remove(context, ConstantValue.SIM_NUM);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.siv_sim_bind:
                int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
                //检查并申请权限
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
                } else {
                    getSimNumber();
                }
                break;
            default:
                break;
        }
    }
}
