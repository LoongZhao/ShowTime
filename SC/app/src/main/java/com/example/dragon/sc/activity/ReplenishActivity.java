package com.example.dragon.sc.activity;

/**
 * Created by Dragon on 2017/8/9.
 */



import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dragon.sc.util.HttpUtil;
import com.example.dragon.sc.util.ReplenishJSONAdapter;
import com.example.dragon.sc.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

public class ReplenishActivity extends Activity{

    private ImageView menuup;
    private TextView wcdbovename;
    private String uId,ip,whse,co,div;
    private Button scanbutton,sousuobutton;
    private EditText sousuoEdit,LOCEdit;
    static ProgressDialog progressDialog = null;
    private JSONObject jsonobj;
    private Handler handler;
    private ListView replenishlistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.replenish);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        uId = bundle.getString("uId");
        ip = bundle.getString("ip");
        whse = bundle.getString("whse");
        co = bundle.getString("co");
        div = bundle.getString("div");

        menuup = (ImageView) findViewById(R.id.menuup);
        wcdbovename = (TextView) findViewById(R.id.wcdbovename);
        scanbutton = (Button) findViewById(R.id.scanbutton);
        sousuoEdit = (EditText) findViewById(R.id.sousuoEdit);
        sousuobutton = (Button) findViewById(R.id.sousuobutton);
        LOCEdit = (EditText) findViewById(R.id.LOCEdit);
        replenishlistview = (ListView) findViewById(R.id.replenishlistview);
        wcdbovename.setText(uId);

        menuup.setOnClickListener(listener);
        scanbutton.setOnClickListener(listener);
        sousuobutton.setOnClickListener(listener);
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x11:
                        try {
                            JSONArray jsonary = jsonobj.getJSONArray("list");
                            ReplenishJSONAdapter replAdapter = new ReplenishJSONAdapter(ReplenishActivity.this, jsonary, "sku", "skudesc", "fromloc", "tolocation", "qty","remark", true);
                            replenishlistview.setAdapter(replAdapter);
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 0x12:
                        progressDialog.dismiss();
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
                case R.id.menuup:
                    finish();
                    break;
                case R.id.scanbutton:
                    Intent intent = new Intent(ReplenishActivity.this,CaptureActivity.class);
                    startActivityForResult(intent, 0);
                    break;
                case R.id.sousuobutton:
                    progressDialog = android.app.ProgressDialog.show(ReplenishActivity.this, "", "Loading......", true);
                    final String sku = sousuoEdit.getText().toString().trim();
                    final String loc = LOCEdit.getText().toString().trim();
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                jsonobj = query(sku,loc, whse, co, div);
                                Message message = new Message();
                                message.what=0x11;
                                handler.sendMessage(message);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Message msg = new Message();
                                msg.what=0x12;
                                handler.sendMessage(msg);
                            }
                        }
                    }).start();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String txtResult = bundle.getString("txtResult").toString().trim();
            sousuoEdit.setText(txtResult);
        }
    }

    @SuppressWarnings("unused")
    private JSONObject query(String sku,String loc, String whse, String co,
                             String div) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("sku", sku);
        map.put("loc", loc);
        map.put("whse", whse);
        map.put("co", co);
        map.put("div", div);
        String url = HttpUtil.BASE_URL(ip) + "replenish.jsp";
        return new JSONObject(HttpUtil.postRequest(url, map));
    }


}

