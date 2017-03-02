package com.demo.safe.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.demo.safe.service.AddressService;
import com.demo.safe.util.ConstantValue;
import com.demo.safe.util.MyApplication;
import com.demo.safe.util.ServiceUtil;
import com.demo.safe.util.SpUtils;
import com.demo.safe.view.SettingClickView;
import com.demo.safe.view.SettingItemView;

/**
 * Created by ChenXingLing on 2017/2/22.
 */

public class SettingActivity extends BaseActivity {
    private static final String tag = "SettingActivity";

    private SettingItemView siv_update;
    private SettingItemView siv_phone_address;
    private String[] mToastStyleDes;
    private int mToastStyle;
    private SettingClickView scv_toast_style;
    private SettingClickView scv_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUpdate();
        initAddress();
        initToastStyle();
        initLocation();
    }

    private void initLocation() {
        scv_location = (SettingClickView) findViewById(R.id.scv_location);
        scv_location.setTitle("归属地提示框显示位置");
        scv_location.setDes("设置归属地提示框显示位置");
        scv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ToastLocationActivity.class));
            }
        });
    }

    private void initToastStyle() {
        scv_toast_style = (SettingClickView) findViewById(R.id.scv_toast_style);
        scv_toast_style.setTitle("设置归属地显示风格");
        //1。创建风格数组
        mToastStyleDes = new String[]{"透明","橙色","蓝色","灰色","绿色"};
        //2.获取吐司显示样式的索引值
        mToastStyle = SpUtils.getInt(getApplicationContext(),ConstantValue.TOAST_STYLE,0);
        //3.通过索引获取数组中的文字
        scv_toast_style.setDes(mToastStyleDes[mToastStyle]);
        //4.监听事件，弹出对话框
        scv_toast_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToastStlyeDialog();
            }
        });
    }

    private void showToastStlyeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("请选择样式");
        /**
         * 选择单个条目的监听事件
         * 参数说明：
         * 1.样式数组
         * 2.弹出对话框中选择条目的索引值
         * 3.点击条目后触发的点击事件（1.记录选中的索引值；2.关闭对话框；3.显示选中条目的文字）
         */
        int chooseItem = SpUtils.getInt(getApplicationContext(),ConstantValue.TOAST_STYLE,0);
        builder.setSingleChoiceItems(mToastStyleDes, chooseItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SpUtils.putInt(getApplicationContext(),ConstantValue.TOAST_STYLE,which);
                dialog.dismiss();
                scv_toast_style.setDes(mToastStyleDes[which]);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
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
