package com.demo.safe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.safe.util.ConstantValue;
import com.demo.safe.util.Md5Util;
import com.demo.safe.util.MyApplication;
import com.demo.safe.util.SpUtils;
import com.demo.safe.util.ToastUtil;

/**
 * Created by ChenXingLing on 2017/2/21.
 */
public class HomeActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final int REQUEST_SYSTEM_ALERT_WINDOW = 1;
    private GridView gv_home;
    private String[] mTitleStr;
    private int[] mDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_home);

        initUI();
        initData();
    }

    private void initData() {
        mTitleStr = new String[]{
                "手机防盗", "通信卫士", "软件管理",
                "进程管理", "流量统计", "手机杀毒",
                "缓存清理", "高级工具", "设置中心"};
        mDrawable = new int[]{
                R.drawable.fdgj, R.drawable.txws, R.drawable.rjgl,
                R.drawable.jcgl, R.drawable.lltj, R.drawable.sjsd,
                R.drawable.hcql, R.drawable.gjgj, R.drawable.szzx,
        };
        gv_home.setAdapter(new MyAdapter());
        gv_home.setOnItemClickListener(this);
    }

    private void initUI() {
        gv_home = (GridView) findViewById(R.id.gv_home);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_SYSTEM_ALERT_WINDOW:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //有权限的操作
                    Intent intent1 = new Intent(MyApplication.getContext(), SettingActivity.class);
                    startActivity(intent1);
                } else {
                    ToastUtil.show(getApplicationContext(), "没有权限");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(!Settings.canDrawOverlays(getApplicationContext())) {
                    Toast.makeText(this,"not granted",Toast.LENGTH_SHORT);
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                showDialog();
                break;
            case 1:
                //通信卫士
                Intent bnActivity = new Intent(getApplicationContext(), BlackNumberActivity.class);
                startActivity(bnActivity);

                //一键锁屏
//                Intent com = new Intent(getApplicationContext(), CommunicateActivity.class);
//                startActivity(com);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                //高级工具
                Intent aToolActivity = new Intent(getApplicationContext(),AToolActivity.class);
                startActivity(aToolActivity);
                break;
            case 8:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(!Settings.canDrawOverlays(getApplicationContext())) {
                        //启动Activity让用户授权
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        startActivity(intent);
                        return;
                    } else {
                        Intent intent1 = new Intent(MyApplication.getContext(), SettingActivity.class);
                        startActivity(intent1);
                    }
                } else {
                    Intent intent1 = new Intent(MyApplication.getContext(), SettingActivity.class);
                    startActivity(intent1);
                }
                break;
            default:
                break;
        }
    }

    private void showDialog() {
        String psd = SpUtils.getString(this, ConstantValue.MOBILE_SAFE_PSD, "");
        if (TextUtils.isEmpty(psd)) {
            showSetPsdDialog();
        } else {
            showConfirmPsdDialog();
        }
    }

    private void showConfirmPsdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_confirm_psd, null);
        //让对话框显示自定义界面
        dialog.setView(view, 0, 0, 0, 0);//为了兼容低版本，让其没有内边距（android默认提供的边距）
        dialog.show();

        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button bt_commit = (Button) view.findViewById(R.id.bt_commit);
        bt_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ed_confirm_psd = (EditText) view.findViewById(R.id.ed_confirm_psd);
                String confirmPsd = ed_confirm_psd.getText().toString();
                Context context = MyApplication.getContext();
                if (TextUtils.isEmpty(confirmPsd)) {
                    ToastUtil.show(context, "密码错误");
                } else {
                    String oldPsd = SpUtils.getString(context, ConstantValue.MOBILE_SAFE_PSD, "");
                    if (!oldPsd.equals(Md5Util.encoder(confirmPsd))) {
                        ToastUtil.show(context, "密码错误");
                    } else {
                        Intent intent = new Intent(context, SetupOverActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }
            }
        });
    }

    private void showSetPsdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_set_psd, null);
        //让对话框显示自定义界面
        dialog.setView(view, 0, 0, 0, 0);//为了兼容低版本，让其没有内边距（android默认提供的边距）
        dialog.show();

        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button bt_commit = (Button) view.findViewById(R.id.bt_commit);
        bt_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ed_set_psd = (EditText) view.findViewById(R.id.ed_set_psd);
                EditText ed_confirm_psd = (EditText) view.findViewById(R.id.ed_confirm_psd);
                String sedPsd = ed_set_psd.getText().toString();
                String confirmPsd = ed_confirm_psd.getText().toString();
                Context context = MyApplication.getContext();
                if (TextUtils.isEmpty(sedPsd) || TextUtils.isEmpty(confirmPsd)) {
                    ToastUtil.show(context, "密码错误");
                } else {
                    if (!sedPsd.equals(confirmPsd)) {
                        ToastUtil.show(context, "两次密码不一致");
                    } else {
                        Intent intent = new Intent(context, SetupOverActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                        SpUtils.putString(context, ConstantValue.MOBILE_SAFE_PSD, Md5Util.encoder(sedPsd));
                    }
                }
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTitleStr.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitleStr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(MyApplication.getContext(), R.layout.gridview_item, null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_title.setText(mTitleStr[position]);
            iv_icon.setBackgroundResource(mDrawable[position]);
            return view;
        }
    }
}
