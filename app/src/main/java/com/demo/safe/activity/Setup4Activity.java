package com.demo.safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.demo.safe.util.ConstantValue;
import com.demo.safe.util.SpUtils;
import com.demo.safe.util.ToastUtil;

/**
 * Created by ChenXingLing on 2017/2/23.
 */
public class Setup4Activity extends BaseActivity {

    private CheckBox cb_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        initUI();
    }

    private void initUI() {
        //是否选中状态 回显
        cb_box = (CheckBox) findViewById(R.id.cb_box);
        boolean open_security = SpUtils.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
        //根据状态修改checkbox中的文字
        cb_box.setChecked(open_security);
        if (open_security) {
            cb_box.setText("安全设置已开启");
        } else {
            cb_box.setText("安全设置已关闭");
        }
        //点击过程中
        cb_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //checkbox点击后的状态存储
                SpUtils.putBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, isChecked);
                //根据状态改变显示文字
                if (isChecked) {
                    cb_box.setText("安全设置已开启");
                } else {
                    cb_box.setText("安全设置已关闭");
                }
            }
        });

    }

    public void preBtn(View view) {
        Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    public void nextPage(View view) {
        boolean isOpen = SpUtils.getBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, false);
        if (isOpen) {
            Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
            startActivity(intent);
            finish();
            SpUtils.putBoolean(this, ConstantValue.SETUP_OVER, true);
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        } else {
            ToastUtil.show(getApplicationContext(), "未开启防盗设置");
        }

    }
}
