package com.demo.safe.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.safe.util.ConstantValue;
import com.demo.safe.util.LogUtil;
import com.demo.safe.util.MyApplication;
import com.demo.safe.util.SpUtils;
import com.demo.safe.util.StreamUtil;
import com.demo.safe.util.ToastUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class SplashActivity extends BaseActivity {

    protected static final String tag = "SplashActivity";
    private static final int REQUEST_SYSTEM_ALERT_WINDOW = 5;
    private String mVersionName;
    private String mVersionDes;
    private String mVersionCode;
    private String mDownloadUrl;

    public static final int UPDATE_VERSION = 100;
    public static final int ENTER_HOME = 101;
    public static final int URL_ERROR = 102;
    private static final int IO_ERROR = 103;
    private static final int JSON_ERROR = 104;
    private TextView tv_version_name;
    private RelativeLayout rl_root;
    private int mLocalVersionCode;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_VERSION:
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    enterHome();
                    break;
                case URL_ERROR:
                    ToastUtil.show(MyApplication.getContext(), "url错误");
                    enterHome();
                    break;
                case IO_ERROR:
                    ToastUtil.show(MyApplication.getContext(), "读取错误");
                    enterHome();
                    break;
                case JSON_ERROR:
                    ToastUtil.show(MyApplication.getContext(), "json解析错误");
                    enterHome();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉titleBar
        setContentView(R.layout.activity_splash);

        initUI();
        initDate();
        //初始化动画
        initAnimation();

        initDB();
    }

    private void initDB() {
        initAddressDB("address.db");
    }

    /**
     * copy数据库至files文件夹下
     *
     * @param dbName
     */
    private void initAddressDB(String dbName) {
        //1.在files文件夹下创建同名dbName数据库文件过程
        File files = getFilesDir();
        File file = new File(files, dbName);
        if (file.exists()) {
            return;
        }
        InputStream stream = null;
        FileOutputStream fos = null;
        //2.输入流读入第三方资产目录下的文件
        try {
            stream = getAssets().open(dbName);
            //3.将读入的内容输入到指定文件夹下的文件中去
            fos = new FileOutputStream(file);
            //4.每次读取的内容大小
            byte[] bs = new byte[1024];
            int temp = -1;
            while ((temp = stream.read(bs)) != -1) {
                fos.write(bs, 0, temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null && fos != null) {
                try {
                    stream.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);//时间
        rl_root.startAnimation(alphaAnimation);
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
    }

    /**
     * 获取数据
     */
    private void initDate() {

        //1.应用版本名称
        tv_version_name.setText("版本名称：" + getVersionName());
        //2.检测，如果有更新提示用户更新
        mLocalVersionCode = getVersionCode();
        LogUtil.d(tag, "版本号:" + mLocalVersionCode);
        //3.获取服务器版本号
        if (SpUtils.getBoolean(this, ConstantValue.OPEN_UPDATE, false)) {
            checkVersion();
        } else {
//            enterHome();
            mHandler.sendEmptyMessageDelayed(ENTER_HOME, 4000);
        }
    }

    /**
     * 检测版本号
     */
    private void checkVersion() {
        /*String mAddress = "http://183.230.182.143:8686/security/app/getInfoColl.html?starttime=&endtime=&token=11C7A42A06D56C22B7EEC2742D62730296C5FEEE";
        HttpUtil.sendHttpRequest(mAddress, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //将服务器返回结果存到Message中
                LogUtil.d(tag,response);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(MyApplication.getContext(), "请求失败", Toast.LENGTH_SHORT).show();
            }
        });*/

        new Thread() {
            public void run() {
                Message message = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    //10.0.2.2 谷歌预留ip 仅限模拟器访问本地服务器
//                    String mAddress = "http://10.0.2.2:8080/version.json";
                    String address = "http://192.168.201.12:8080/version.json";
                    URL url = new URL(address);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(2000);
                    connection.setReadTimeout(2000);
                    if (connection.getResponseCode() == 200) {
                        InputStream inputStream = connection.getInputStream();
                        String json = StreamUtil.streamToString(inputStream);
                        Log.d(tag, json);
                        //7.json解析
                        JSONObject jsonObject = new JSONObject(json);
                        mVersionName = jsonObject.getString("versionName");
                        mVersionDes = jsonObject.getString("versionDes");
                        mVersionCode = jsonObject.getString("versionCode");
                        mDownloadUrl = jsonObject.getString("downloadUrl");
                        //8.对比版本号
                        if (mLocalVersionCode < Integer.parseInt(mVersionCode)) {
                            message.what = UPDATE_VERSION;
                        } else {
                            message.what = ENTER_HOME;
                        }
                    }
                } catch (IOException e) {
                    message.what = IO_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    message.what = JSON_ERROR;
                    e.printStackTrace();
                } finally {
                    long endTime = System.currentTimeMillis();
                    if (endTime - startTime < 4000) {
                        try {
                            Thread.sleep(4000 - (endTime - startTime));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(message);
                }
            }
        }.start();
    }

    /**
     * 获取版本号
     *
     * @return 非0则表示获取成功
     */
    private int getVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本名称
     *
     * @return 应用返回名称
     */
    public String getVersionName() {
        //PackageManager包管理者对象
        PackageManager pm = getPackageManager();
        try {
            //从包管理者对象中，获取指定报名的基本信息（版本名称，版本号），传0代表基本信息
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.smile);
        builder.setTitle("版本更新");
        builder.setMessage(mVersionDes);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadApk();
            }
        });
        builder.setNegativeButton("下次更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void downloadApk() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "mobileSafe74.apk";
            HttpUtils httpUtils = new HttpUtils();
            LogUtil.d(tag, "下载地址：" + mDownloadUrl);
            httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
                //下载成功
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    LogUtil.d(tag, "下载成功");
                    File file = responseInfo.result;
                    installApk(file);
                }

                //下载失败
                @Override
                public void onFailure(HttpException e, String s) {
                    LogUtil.d(tag, "下载失败");
                    enterHome();
                }

                //开始下载
                @Override
                public void onStart() {
                    LogUtil.d(tag, "开始下载");
                    super.onStart();
                }

                //下载过程中
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    LogUtil.d(tag, "下载中....");
                    LogUtil.d(tag, "total:" + total);
                    LogUtil.d(tag, "current:" + current);
                    LogUtil.d(tag, "isUploading:" + isUploading);
                    super.onLoading(total, current, isUploading);
                }
            });

        }
    }

    private void installApk(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
//        intent.setData(Uri.fromFile(file));
//        intent.setType("application/vnd.android.package-archive");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

}
