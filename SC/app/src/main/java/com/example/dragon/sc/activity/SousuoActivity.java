package com.example.dragon.sc.activity;

/**
 * Created by Dragon on 2017/8/9.
 */



import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.dragon.sc.R;
import com.example.dragon.sc.util.HttpUtil;
import com.example.dragon.sc.util.JSONArraysousuoadp;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SousuoActivity extends Activity {

    private TextView wcdbovename,wrongTextView;
    private EditText sousuoEdit,locEdit;
    private Button sousuoimg,scanbutton;
    private ImageView imageup;
    private Button saomiao;
    private String regex = "[0-9]+";
    private String whse;
    private String co;
    private String div;
    private String ip;
    private Button sousuobutton;
    private JSONObject jsonary;
    private ListView sousuolistview;
    private Handler handler;
    static ProgressDialog progressDialog = null;
    private String prodStat = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.sousuo);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String uId = bundle.getString("uId");
        ip = bundle.getString("ip");
        whse = bundle.getString("whse");
        co = bundle.getString("co");
        div = bundle.getString("div");
        wcdbovename = (TextView) findViewById(R.id.wcdbovename);
        imageup = (ImageView) findViewById(R.id.imageup);
        wrongTextView = (TextView) findViewById(R.id.wrongTextView);
        sousuobutton = (Button) findViewById(R.id.sousuobutton);
        sousuoEdit = (EditText) findViewById(R.id.sousuoEdit);
        locEdit = (EditText) findViewById(R.id.LOCEdit);
        sousuolistview = (ListView) findViewById(R.id.sousuolistview);
        scanbutton = (Button) findViewById(R.id.scanbutton);

        wcdbovename.setText(uId);


        imageup.setOnClickListener(listener);
        sousuobutton.setOnClickListener(listener);
        scanbutton.setOnClickListener(listener);

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x11:
                        JSONObject jsonobjmap;
                        try {
                            jsonobjmap = jsonary.getJSONObject("map");
                            JSONArray jsonobjtree = jsonobjmap.getJSONArray("tree");
                            System.out.println(jsonobjtree+":::::::::::::::::::::::::::::::::::::::::::");
                            JSONArraysousuoadp jsonadp = new JSONArraysousuoadp(SousuoActivity.this, jsonobjtree,"sku", "skuDesc", "fromLoc", "unitqty","barcode", true);
                            sousuolistview.setAdapter(jsonadp);
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                case R.id.scanbutton:
                    Intent intent = new Intent(SousuoActivity.this,CaptureActivity.class);     //调用Barcode Scanner
                    startActivityForResult(intent, 0);
                    break;
                case R.id.imageup:
                    finish();
                    break;
                case R.id.sousuobutton:
                    if(co.equals("RCA")){
                        LayoutInflater inflater = LayoutInflater.from(SousuoActivity.this);
                        final View view = inflater.inflate(R.layout.prodstat, null);
                        AlertDialog dialog = new AlertDialog.Builder(SousuoActivity.this)
                                .setTitle("请选择")
                                .setView(view)
                                .setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {

                                                RadioButton IE = (RadioButton) view.findViewById(R.id.IE);
                                                RadioButton LC = (RadioButton) view.findViewById(R.id.LC);
                                                if(IE.isChecked()==true){
                                                    prodStat = "APR";
                                                }
                                                if(LC.isChecked()==true){
                                                    prodStat = "LC";
                                                }
                                                progressDialog = android.app.ProgressDialog.show(SousuoActivity.this, "", "Loading......", true);
                                                final String sku = sousuoEdit.getText().toString().trim();
                                                final String loc = locEdit.getText().toString().trim();
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            jsonary = query(sku,loc, whse, co, div,prodStat);
                                                            Message message = new Message();
                                                            message.what=0x11;
                                                            handler.sendMessage(message);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }).start();

                                            }
                                        }).setNegativeButton("取消", null).show();
                    }else{
                        progressDialog = android.app.ProgressDialog.show(SousuoActivity.this, "", "Loading......", true);
                        final String sku = sousuoEdit.getText().toString().trim();
                        final String loc = locEdit.getText().toString().trim();
                        System.out.println(sku+"OOPPPPOOPPPPOPPPPO"+loc);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    jsonary = query(sku,loc, whse, co, div,prodStat);
                                    Message message = new Message();
                                    message.what=0x11;
                                    handler.sendMessage(message);
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
            sousuoEdit.setText(txtResult);
            if(co.equals("RCA")){
                LayoutInflater inflater = LayoutInflater.from(SousuoActivity.this);
                final View view = inflater.inflate(R.layout.prodstat, null);
                AlertDialog dialog = new AlertDialog.Builder(SousuoActivity.this)
                        .setTitle("请选择")
                        .setView(view)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        RadioButton IE = (RadioButton) view.findViewById(R.id.IE);
                                        RadioButton LC = (RadioButton) view.findViewById(R.id.LC);
                                        if(IE.isChecked()==true){
                                            prodStat = "APR";
                                        }
                                        if(LC.isChecked()==true){
                                            prodStat = "LC";
                                        }
                                        progressDialog = android.app.ProgressDialog.show(SousuoActivity.this, "", "Loading......", true);
                                        final String sku = sousuoEdit.getText().toString().trim();
                                        final String loc = locEdit.getText().toString().trim();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    jsonary = query(sku,loc, whse, co, div,prodStat);
                                                    Message message = new Message();
                                                    message.what=0x11;
                                                    handler.sendMessage(message);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();

                                    }
                                }).setNegativeButton("取消", null).show();
            }else{
                progressDialog = android.app.ProgressDialog.show(SousuoActivity.this, "", "Loading......", true);
                final String sku = sousuoEdit.getText().toString().trim();
                final String loc = locEdit.getText().toString().trim();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            jsonary = query(sku,loc, whse, co, div,prodStat);
                            Message message = new Message();
                            message.what=0x11;
                            handler.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }


    @SuppressWarnings("unused")
    private JSONObject query(String sku,String loc, String whse, String co,
                             String div,String prodStat) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("sku", sku);
        map.put("loc", loc);
        map.put("whse", whse);
        map.put("co", co);
        map.put("div", div);
        map.put("prodStat", prodStat);
        String url = HttpUtil.BASE_URL(ip) + "action.jsp";
        return new JSONObject(HttpUtil.postRequest(url, map));
    }

}
