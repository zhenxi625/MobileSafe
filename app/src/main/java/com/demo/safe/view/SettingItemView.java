package com.demo.safe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.safe.activity.R;
import com.demo.safe.util.LogUtil;

/**
 * Created by ChenXingLing on 2017/2/22.
 */

public class SettingItemView extends RelativeLayout {

    private static final String tag = "SettingItemView";
    public static final String NAME_SPACE = "http://schemas.android.com/apk/lib/com.demo.safe.activity";

    private TextView tv_des;
    private CheckBox cb_box;
    private String desTitle;
    private String desOff;
    private String desOn;


    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.setting_item_view, this);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
        cb_box = (CheckBox) findViewById(R.id.cb_box);
        initAttrs(attrs);
        tv_title.setText(desTitle);
    }

    //获取自定义属性的属性值
    private void initAttrs(AttributeSet attrs) {
        desTitle = attrs.getAttributeValue(NAME_SPACE,"destitle");
        desOff = attrs.getAttributeValue(NAME_SPACE,"desoff");
        desOn = attrs.getAttributeValue(NAME_SPACE,"deson");
    }

    /**
     * @return true开启 false关闭
     */
    public boolean isCheck() {
        return cb_box.isChecked();
    }

    public void setCheck(boolean isCheck) {
        cb_box.setChecked(isCheck);
        if (isCheck) {
            tv_des.setText(desOn);
        } else {
            tv_des.setText(desOff);
        }
    }
}
