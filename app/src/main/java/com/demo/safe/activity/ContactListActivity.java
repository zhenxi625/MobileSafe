package com.demo.safe.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.safe.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ChenXingLing on 2017/2/24.
 */
public class ContactListActivity extends BaseActivity{

    private static final String tag = "ContactListActivity";
    private static final int REQUEST_READ_CONTACTS = 1;
    private List<HashMap<String,String>> contactList = new ArrayList<>();
    private ListView lv_contact;
    private MyAdapter mAdapter;

    Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            mAdapter = new MyAdapter();
            lv_contact.setAdapter(mAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        initUI();
        getPermission();
    }

    public void getPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        //检查并申请权限
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
        } else {
            initData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    initData();
                } else {
                    ToastUtil.show(getApplicationContext(), "没有权限");
                }
                break;
            default:
                break;
        }
    }

    private void initData() {
        //因为读取联系人可能是一个耗时操作，所以需要防到子线程中
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(
                        Uri.parse("content://com.android.contacts/raw_contacts"),
                        new String[]{"contact_id"},null,null,null
                );
                contactList.clear();
                while (cursor.moveToNext()){
                    String id = cursor.getString(0);
                    //根据用户id，查询data表和mimetype表生成视图，获取data和mimetype字段
                    Cursor indexCursor = contentResolver.query(
                            Uri.parse("content://com.android.contacts/data"),
                            new String[]{"data1","mimetype"},"raw_contact_id =?",
                            new String[]{id},null
                    );
                    //循环获取每一个联系人的号码、姓名、数据类型
                    HashMap<String,String> hashMap = new HashMap<>();
                    while (indexCursor.moveToNext()){
                        String data = indexCursor.getString(0);
                        String type = indexCursor.getString(1);
                        //区分类型，将对应的数据放入map中
                        if (type.equals("vnd.android.cursor.item/phone_v2")){
                            if (!TextUtils.isEmpty(data)){
                                hashMap.put("phone",data);
                            }
                        } else if (type.equals("vnd.android.cursor.item/name")){
                            if (!TextUtils.isEmpty(data)){
                                hashMap.put("name",data);
                            }
                        }
                    }
                    indexCursor.close();
                    contactList.add(hashMap);
                }
                cursor.close();
                //发送空消息，告知主线程可以使用子线程已经填充好的数据
                mHandler.sendEmptyMessage(0);
            }
        }).start();

    }

    private void initUI() {
        lv_contact = (ListView) findViewById(R.id.lv_contact);
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1 获取点中条目的对象
                if (null != mAdapter){
                    HashMap<String,String> hashMap = mAdapter.getItem(position);
                    //2.获取选择条目的电话号码
                    String phone = hashMap.get("phone");
                    //3.将此号码传递到下一个页面
                    Intent intent1 = new Intent();
                    intent1.putExtra("phone",phone);
                    setResult(0,intent1);
                    finish();
                }
            }
        });
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public HashMap<String, String> getItem(int position) {
            return contactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(),R.layout.listview_contact_item,null);
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            tv_name.setText(getItem(position).get("name"));
            tv_phone.setText(getItem(position).get("phone"));
            return view;
        }
    }
}
