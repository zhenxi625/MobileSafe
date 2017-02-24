package com.demo.safe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ChenXingLing on 2017/2/22.
 */

public class FocusTextView extends TextView {

    //通过java代码创建控件
    public FocusTextView(Context context) {
        super(context);
    }

    //由系统调用
    public FocusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FocusTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean isFocused(){
        return true;
    }
}
