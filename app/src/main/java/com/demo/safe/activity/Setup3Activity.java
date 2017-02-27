package com.demo.safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.demo.safe.util.ConstantValue;
import com.demo.safe.util.SpUtils;
import com.demo.safe.util.ToastUtil;

/**
 * Created by ChenXingLing on 2017/2/23.
 */
public class Setup3Activity extends BaseSetupActivity {

    private EditText et_phone_number;
    private Button bt_select_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        initUI();
    }

    @Override
    public void showNextPage() {
        String editPhone = et_phone_number.getText().toString();
        if (TextUtils.isEmpty(editPhone)) {
            ToastUtil.show(getApplicationContext(), "请填写联系人");
        } else {
            SpUtils.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE, editPhone);
            Intent intent = new Intent(getApplicationContext(), Setup4Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        }
    }

    @Override
    public void showPrePage() {
        Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    private void initUI() {
        String phone = SpUtils.getString(this, ConstantValue.CONTACT_PHONE, "");
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        et_phone_number.setText(phone);//回显到页面
        bt_select_number = (Button) findViewById(R.id.bt_select_number);
        bt_select_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), ContactListActivity.class);
                startActivityForResult(intent1, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //返回到当前界面，接收结果
        if (null != data) {
            String phone = data.getStringExtra("phone");
            phone = phone.replace("-", "").replace(" ", "").trim();
            et_phone_number.setText(phone);
            //存储联系人
            SpUtils.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE, phone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
