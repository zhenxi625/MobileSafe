package com.demo.safe.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.demo.safe.dao.BlackNumberDao;

/**
 * Created by ChenXingLing on 2017/3/3.
 */

public class BlackNumberActivity extends BaseActivity {

    private Button bt_add;
    private ListView lv_black_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_number);

        initUI();
        initData();
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BlackNumberDao dao = BlackNumberDao.getInstance(getApplicationContext());
            }
        }).start();
    }

    private void initUI() {
        bt_add = (Button) findViewById(R.id.bt_add);
        lv_black_number = (ListView) findViewById(R.id.lv_black_number);
    }
}
