package com.example.dragon.sc.activity;

/**
 * Created by Dragon on 2017/7/18.
 */



import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import com.example.dragon.sc.util.HttpUtil;
import com.example.dragon.sc.R;
import com.example.dragon.sc.util.ParseXml;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity {

    private static EditText Edituser;
    private static EditText Editpwd;
    private EditText EditIp;
    private Button bnlogin, bnconfigration;
    private TextView wrongTextView;
    private static String uId;
    private static JSONObject jsonObjmap;
    private static JSONObject privmap;
    private static String jsonmap;
    private static String ip;
    private static final String userinfo = "UserInfo";
    private CheckBox logincheck;
    private static String jsonwhse;
    private static String jsonco;
    private static String jsondiv;
    private static String jsonpass;
    private static String jsonprivmap;
    static Handler handler = null;
    private String parserversion;
    private HashMap<String, String> hashMap;
    private ProgressBar progressBar;
    private int progress;
    private boolean cancelUpdate = false;
    private Dialog downLoadDialog;
    private String filesavepath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Edituser = (EditText) findViewById(R.id.userEditText);
        Editpwd = (EditText) findViewById(R.id.pwdEditText);
        bnlogin = (Button) findViewById(R.id.bnLogin);
        wrongTextView = (TextView) findViewById(R.id.wrongTextView);
        bnconfigration = (Button) findViewById(R.id.bnconfigration);
        logincheck = (CheckBox) findViewById(R.id.logincheck);

        // 网络配置信息
        bnconfigration.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
                final View view = inflater.inflate(R.layout.configration, null);
                EditIp = (EditText) view.findViewById(R.id.EditIp);
                EditIp.setText(ip);
                AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("个人信息配置")
                        .setView(view)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        EditIp = (EditText) view.findViewById(R.id.EditIp);
                                        ip = EditIp.getText().toString().trim();
                                        Log.i("配置的ip", ip);
                                    }
                                }).setNegativeButton("取消", null).show();
            }
        });
        // 加载checkbox信息和登录信息
        LoadUserData();
        bnlogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            if (loginPro()) {
                                SaveUserData();
                                Intent intent = new Intent(LoginActivity.this,PreAppActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("uId", uId);
                                bundle.putString("jsonpass", jsonpass);
//                                bundle.putString("jsonmap", jsonmap);
                                bundle.putString("ip", ip);
                                bundle.putString("jsonwhse", jsonwhse);
                                bundle.putString("jsonco", jsonco);
                                bundle.putString("jsondiv", jsondiv);
//                                bundle.putString("jsonprivmap", jsonprivmap);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            } else {
                                Message msg = new Message();
                                Log.d("****", "进这里了。。。");
                                msg.what=0x45;
                                handler.sendMessage(msg);
                            }
                        } catch (Exception e) {
                            Message msg = new Message();
                            msg.what=0x56;
                            handler.sendMessage(msg);
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x45:
                        wrongTextView.setText("用户名称或者密码错误，请重新输入！");
                        break;
                    case 0x56:
//                        DialogUtil.showDialog(LoginActivity.this,"服务器或网络异常，请稍后再试！", false);
                        break;
                    case 0x67:
                        showupdatedialog();
                        break;
                    case 0x78:
                        progressBar.setProgress(progress);
                        break;
                    case 0x89:
                        downLoadDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "文件下载完成,正在安装更新", Toast.LENGTH_SHORT).show();
                        installAPK();
                        finish();
                        break;
                    default:
                        break;
                }
            }

        };

        try {
            updateversion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JSONObject query(String username, String password) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("user", username);
        map.put("pass", password);
        String url = HttpUtil.BASE_URL(ip) + "user/login.do";
//        String url = HttpUtil.BASE_URL(ip) + "jsonlogin_login.action";
        Log.d("_________", username+password+url);
        return new JSONObject(HttpUtil.postRequest(url, map));
    }

    private static boolean loginPro() throws Exception {
        String username = Edituser.getText().toString().trim ();
        String pwd = Editpwd.getText().toString().trim();
        JSONObject jsonObj = query(username, pwd);
        Log.d("****", jsonObj.toString());
        if (jsonObj.getBoolean("status")) {
            Log.d("*****", jsonObj.toString());
            jsonObjmap = jsonObj.getJSONObject("data");
//            jsonmap = jsonObjmap.toString();
            uId = jsonObjmap.getString("id");
            jsonwhse =jsonObjmap.getString("whse");
            jsonco = jsonObjmap.getString("co");
            jsondiv = jsonObjmap.getString("div");
            jsonpass = jsonObjmap.getString("pass");
//            privmap = jsonObjmap.getJSONObject("privmap");
//            jsonprivmap = privmap.toString();
            Log.d("****", jsonObjmap.toString());
            Log.d("****", uId);
            Log.d("****", jsonwhse);
            Log.d("****", jsonco);
            Log.d("****", jsondiv);
            return true;
        }
        return false;
    }


    public void SaveUserData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(
                userinfo, 0);
        Editor spedit = sharedPreferences.edit();
        if (logincheck.isChecked()) {

            Log.i("LoginActivity", "check已经点击");

            spedit.putBoolean("isSave", true);
            spedit.putString("Edituser", Edituser.getText().toString().trim());
            spedit.putString("Editpwd", Editpwd.getText().toString().trim());
            if (ip == null && ip.equals("")) {
                spedit.putString("ip", "");
            } else {
                spedit.putString("ip", ip);
            }
            spedit.commit();
        } else {
            spedit.putBoolean("isSave", false);
            spedit.putString("Edituser", "");
            spedit.putString("Editpwd", "");
            spedit.putString("ip", ip);
            spedit.commit();
        }
    }

    public void LoadUserData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(userinfo, 0);
        if (sharedPreferences.getBoolean("isSave", true)) {
            String name = sharedPreferences.getString("Edituser", "");
            String pwd = sharedPreferences.getString("Editpwd", "");
            ip = sharedPreferences.getString("ip", "");
            if (!name.equals("") && !pwd.equals("")) {
                Edituser.setText(name);
                Editpwd.setText(pwd);
                logincheck.setChecked(true);
            }
        }else{
            ip = sharedPreferences.getString("ip", "");
        }
    }

    public  void updateversion() throws Exception{
        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    PackageManager pm = getPackageManager();
                    PackageInfo pinfo = pm.getPackageInfo(getPackageName(), 0);
                    String currentversion = pinfo.versionName;

                    XmlPullParser pull = Xml.newPullParser();
                    URL url = new URL("http://"+ip+":8888/android/version.xml");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(5 * 1000);
                    conn.setRequestMethod("GET");
                    InputStream inputStream = conn.getInputStream();
                    ParseXml parsexml = new ParseXml();
                    hashMap = parsexml.parseXml(inputStream);
                    if(hashMap != null){
                        parserversion = hashMap.get("versionCode");
                    }
                    if(currentversion.equals(parserversion)){

                    }else{
                        Message message = new Message();
                        message.what=0x67;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();

    }

    protected void showupdatedialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("版本升级");
        builder.setMessage("检测到最新版本，请更新!");
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                showProgressDialog();
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                            String sdpath = Environment.getExternalStorageDirectory()+"/";
                            filesavepath = sdpath+"scliappClient";
                            try {
                                URL ul = new URL("http://"+ip+":8888/android/scliappClient.apk");
                                HttpURLConnection httpconn = (HttpURLConnection) ul.openConnection();
                                httpconn.setReadTimeout(5*1000);
                                httpconn.setRequestMethod("GET");
                                httpconn.setRequestProperty("Charser", "GBK,UTF-8");
                                int fileleghth = httpconn.getContentLength();
                                InputStream inputstream = httpconn.getInputStream();
                                File file = new File(filesavepath);
                                if(!file.exists()){
                                    file.mkdir();
                                }
                                File apkfile = new File(filesavepath, hashMap.get("fileName")+".apk");
                                FileOutputStream os = new FileOutputStream(apkfile);
                                int count = 0;
                                byte buf[] = new byte[1024];
                                do {
                                    int numread = inputstream.read(buf);
                                    System.out.println("numread:"+numread);
                                    count += numread;
                                    System.out.println("count:"+count);
                                    progress = (int) (((float) count / fileleghth) * 100);
                                    System.out.println("progress:"+progress);
                                    Message message = new Message();
                                    message.what = 0x78;
                                    handler.sendMessage(message);
                                    if (numread <= 0) {
                                        downLoadDialog.dismiss();
                                        Message message2 = new Message();
                                        message2.what = 0x89;
                                        handler.sendMessage(message2);
                                        break;
                                    }
                                    os.write(buf, 0, numread);
                                } while (!cancelUpdate);
                                os.close();
                                inputstream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();
            }
        });

        builder.create().show();
    }

    public void showProgressDialog(){
        AlertDialog.Builder al = new AlertDialog.Builder(LoginActivity.this);
        al.setTitle("正在更新");
        final LayoutInflater in = LayoutInflater.from(LoginActivity.this);
        View v = in.inflate(R.layout.downloaddialog, null);
        progressBar = (ProgressBar) v.findViewById(R.id.updateProgress);
        al.setView(v);
        al.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                cancelUpdate = true;
            }
        });
        downLoadDialog = al.create();
        downLoadDialog = al.show();
    }
    public void installAPK(){
        File apkfile = new File(filesavepath,hashMap.get("fileName")+".apk");
        if(!apkfile.exists()){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
