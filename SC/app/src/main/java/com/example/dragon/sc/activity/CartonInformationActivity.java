package com.example.dragon.sc.activity;

/**
 * Created by Dragon on 2017/8/9.
 */



import java.io.File;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpConnection;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.dragon.sc.R;
import com.example.dragon.sc.util.CartonInformationJSONAdapter;
import com.example.dragon.sc.util.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CartonInformationActivity extends Activity{

    private TextView wcdbovename;
    private ImageView imageup;
    private String whse;
    private String co;
    private String div;
    private String ip;
    private Handler handler;
    private EditText cartonpktedit,cartonskuedit;
    private Button cartonbutton,scanbutton;
    private JSONObject json;
    private ListView cartonlistview;
    static ProgressDialog progressDialog = null;
    private Thread th;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.cartoninformation);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String uId = bundle.getString("uId");
        ip = bundle.getString("ip");
        whse = bundle.getString("whse");
        co = bundle.getString("co");
        div = bundle.getString("div");
        wcdbovename = (TextView) findViewById(R.id.wcdbovename);
        imageup = (ImageView) findViewById(R.id.imageup);
        cartonpktedit = (EditText) findViewById(R.id.cartonsoedit);
        cartonskuedit = (EditText) findViewById(R.id.cartonskuedit);
        cartonbutton = (Button) findViewById(R.id.cartonbutton);
        cartonlistview= (ListView) findViewById(R.id.cartonlistview);
        scanbutton = (Button) findViewById(R.id.scanbutton);
        wcdbovename.setText(uId);

        imageup.setOnClickListener(listener);
        cartonbutton.setOnClickListener(listener);
        scanbutton.setOnClickListener(listener);

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x11:
                        try {
                            JSONArray jsonary = json.getJSONArray("list");
                            CartonInformationJSONAdapter cartonAdapter = new CartonInformationJSONAdapter(CartonInformationActivity.this, jsonary, "pktCtrlNbr", "sku", "labelNbr", "cartonno","packqty", "scannedDate", true);
                            cartonlistview.setAdapter(cartonAdapter);
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case 0x12:
                        progressDialog.dismiss();
                        Toast.makeText(CartonInformationActivity.this, "请输入查询条件,查询内容过多。查询超时！", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }

        };
    }

    private OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imageup:
                    finish();
                    break;
                case R.id.scanbutton:
                    Intent scanIntent = new Intent(CartonInformationActivity.this,CaptureActivity.class);
                    startActivityForResult(scanIntent, 0);
                    break;
                case R.id.cartonbutton:
                    final String pktedit = cartonpktedit.getText().toString().trim();
                    final String skuedit = cartonskuedit.getText().toString().trim();
                    if(pktedit.equals("") && skuedit.equals("")){
                        AlertDialog loginout = new AlertDialog.Builder(CartonInformationActivity.this)
                                .setMessage("请输入查询条件！")
                                .setPositiveButton("确定",null).show();
                    }else{
                        progressDialog = android.app.ProgressDialog.show(CartonInformationActivity.this, "", "Loading......", true);
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    json = query(pktedit,skuedit,whse,co,div);
                                    Message msg = new Message();
                                    msg.what=0x11;
                                    handler.sendMessage(msg);
                                } catch (SocketTimeoutException e) {
                                    e.printStackTrace();
                                    Message m = new Message();
                                    m.what=0x12;
                                    handler.sendMessage(m);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String txtResult = bundle.getString("txtResult").toString().trim();
            cartonpktedit.setText(txtResult);
            final String pktedit = cartonpktedit.getText().toString().trim();
            final String skuedit = cartonskuedit.getText().toString().trim();
            progressDialog = android.app.ProgressDialog.show(CartonInformationActivity.this, "", "Loading......", true);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        json = query(pktedit,skuedit,whse,co,div);
                        Message msg = new Message();
                        msg.what=0x11;
                        handler.sendMessage(msg);
                    } catch (SocketTimeoutException e) {
                        e.printStackTrace();
                        Message m = new Message();
                        m.what=0x12;
                        handler.sendMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @SuppressWarnings("unused")
    private JSONObject query(String pktedit,String skuedit, String whse, String co,
                             String div) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("pktedit", pktedit);
        map.put("skuedit", skuedit);
        map.put("whse", whse);
        map.put("co", co);
        map.put("div", div);
        String url = HttpUtil.BASE_URL(ip) + "cartoninformation.jsp";
        return new JSONObject(HttpUtil.postRequest(url, map));
    }
}

