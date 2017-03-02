package com.demo.safe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.safe.activity.R;

/**
 * Created by ChenXingLing on 2017/3/1.
 */

public class SettingClickView extends RelativeLayout {

    private static final String tag = "SettingItemView";
    TextView tv_des;
    TextView tv_title;

    public SettingClickView(Context context) {
        this(context, null);
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.setting_click_view, this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
    }

    public void setTitle(String title){
        tv_title.setText(title);
    }

    public void setDes(String des){
        tv_des.setText(des);
    }

}
