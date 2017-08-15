package com.example.dragon.sc.activity;

/**
 * Created by Dragon on 2017/8/9.
 */



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dragon.sc.R;
import com.example.dragon.sc.util.HttpUtil;
import com.example.dragon.sc.util.JSONLoadAdapter;
import com.example.dragon.sc.util.JSONSpinnerAdapter;
import com.example.dragon.sc.util.UseViewHoldder;

public class UserMaterialActivity extends Activity{

    private RelativeLayout addmatRela,finishRela;
    private LinearLayout search,Add,Remove,confirm;
    private ImageView newup;
    private TextView wcdbovename;
    private ListView usematerialListView;
    private Handler handler = null;
    private JSONLoadAdapter loadAdapter = null;
    List<Map<String,Object>> list = null;
    List<Map<String,Object>> spinnerlist = null;
    private String ip,uid,co;
    Map<String,String> matidmap = null;
    JSONArray joj = null;
    private EditText matcodeedt,matnameedt,matstandardedt,unitsedt,qtyedt;
    List<String> savelist = new ArrayList<String>();
    List<Map<String,String>> removelist = new ArrayList<Map<String,String>>();
    private String removemsg = null;
    private String confirmsg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        uid = bundle.getString("uId");
        ip = bundle.getString("ip");
        co = bundle.getString("co");

        newup = (ImageView) findViewById(R.id.newup);
        wcdbovename = (TextView) findViewById(R.id.wcdbovename);
        wcdbovename.setText(uid);
        search = (LinearLayout) findViewById(R.id.search);
        usematerialListView = (ListView) findViewById(R.id.usematerialListView);
        Add = (LinearLayout) findViewById(R.id.Add);
        Remove = (LinearLayout) findViewById(R.id.Remove);
        confirm = (LinearLayout) findViewById(R.id.confirm);


        newup.setOnClickListener(onClickListener);
        search.setOnClickListener(onClickListener);
        Add.setOnClickListener(onClickListener);
        Remove.setOnClickListener(onClickListener);
        confirm.setOnClickListener(onClickListener);

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x11:
                        loadAdapter = new JSONLoadAdapter(UserMaterialActivity.this, list);
                        usematerialListView.setAdapter(loadAdapter);
                        usematerialListView.setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                UseViewHoldder viewholder = (UseViewHoldder) view.getTag();
                                viewholder.cb.toggle();
                                JSONLoadAdapter.isSelected.put(position, viewholder.cb.isChecked());
                                String matid = viewholder.matid.getText().toString();
                                String matcode = viewholder.matcode.getText().toString();
                                String qty = viewholder.qty.getText().toString();
                                final Map<String,String> map = new HashMap<String, String>();
                                map.put("uid", matid);
                                map.put("matcode", matcode);
                                map.put("qty", qty);
                                removelist.add(map);
                            }
                        });
                        break;
                    case 0x12:
                        LayoutInflater flater = LayoutInflater.from(UserMaterialActivity.this);
                        final View view = flater.inflate(R.layout.loadaddmaterial,null);
                        Spinner addmaterialspinner = (Spinner) view.findViewById(R.id.loadaddmaterialspinner);
                        JSONSpinnerAdapter spinnerAdapter = new JSONSpinnerAdapter(UserMaterialActivity.this, spinnerlist);
                        addmaterialspinner.setAdapter(spinnerAdapter);
                        addmaterialspinner.setSelection(0, true);
                        matcodeedt = (EditText) view.findViewById(R.id.matcodeedt);
                        matnameedt = (EditText) view.findViewById(R.id.matnameedt);
                        matstandardedt = (EditText) view.findViewById(R.id.matstandardedt);
                        unitsedt = (EditText) view.findViewById(R.id.unitsedt);
                        qtyedt = (EditText) view.findViewById(R.id.qtyedt);
                        addmaterialspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> arg0,
                                                       View arg1, int arg2, long arg3) {

                                matidmap  = new HashMap<String, String>();
                                TextView spinnerId = (TextView) arg0.findViewById(R.id.addmaterialspinnerid);
                                String matid = spinnerId.getText().toString();
                                matidmap.put("mat_id", matid);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            joj = new JSONArray(HttpUtil.postRequest("http://"+ip+":8888/sclimat/grid_queryMatId", matidmap));
                                            Message ms = new Message();
                                            ms.what=0x13;
                                            handler.sendMessage(ms);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {

                            }
                        });
                        AlertDialog dialog = new AlertDialog.Builder(UserMaterialActivity.this)
                                .setTitle("Add Material")
                                .setView(view)
                                .setPositiveButton("SAVE",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog,int which) {
                                                String matcode = matcodeedt.getText().toString();
                                                String matname = matnameedt.getText().toString();
                                                String matstandard = matstandardedt.getText().toString();
                                                String units = unitsedt.getText().toString();
                                                String qty = qtyedt.getText().toString();
                                                final Map<String,String> map = new HashMap<String, String>();
                                                map.put("mat_code", matcode);
                                                map.put("mat_name", matname);
                                                map.put("mat_standard", matstandard);
                                                map.put("units", units);
                                                map.put("num", qty);
                                                map.put("projectname", co);
                                                map.put("username", uid);
                                                new Thread(new Runnable() {

                                                    @Override
                                                    public void run() {
                                                        try {
                                                            JSONObject savejson = new JSONObject(HttpUtil.postRequest("http://"+ip+":8888/sclimat/use_save", map));
                                                            String msg = savejson.getString("msg");
                                                            savelist.add(msg);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        Message me = new Message();
                                                        me.what=0x14;
                                                        handler.sendMessage(me);
                                                    }
                                                }).start();
                                            }
                                        }).setNegativeButton("CANCEL", null).show();

                        break;

                    case 0x13:
                        try {
                            JSONObject material = joj.getJSONObject(0);
                            matcodeedt.setText(material.get("mat_code").toString());
                            matnameedt.setText(material.get("mat_name").toString());
                            if(material.isNull("mat_standard")){
                                matstandardedt.setText("");
                            }else{
                                matstandardedt.setText(material.get("mat_standard").toString());
                            }
                            if(material.isNull("units")){
                                unitsedt.setText("");
                            }else{
                                unitsedt.setText(material.get("units").toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case 0x14:
                        String c = savelist.get(0);
                        if(c.equals("1")){
                            search.performClick();
                        }

                        break;
                    case 0x15:
                        if(removemsg.equals("1")){
                            search.performClick();
                        }
                        break;
                    case 0x16:
                        AlertDialog loginout = new AlertDialog.Builder(UserMaterialActivity.this)
                                .setTitle("提示")
                                .setMessage(confirmsg)
                                .setPositiveButton("确定",new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,int which) {
                                        search.performClick();
                                    }

                                }).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.newup:
                    finish();
                    break;
                case R.id.search:
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                Map<String, String> map = new HashMap<String, String>();
                                list = new ArrayList<Map<String,Object>>();
                                map.put("limit", String.valueOf(100));
                                map.put("start", String.valueOf(0));
                                JSONObject json = new JSONObject(HttpUtil.postRequest("http://"+ip+":8888/sclimat/use_Search", map));
                                JSONArray root = json.getJSONArray("root");
                                for(int i=0;i<root.length();i++){
                                    JSONObject job = root.getJSONObject(i);
                                    Map<String,Object> jsonmap = new HashMap<String, Object>();
                                    jsonmap.put("matid", job.get("u_id"));
                                    jsonmap.put("matcode", job.get("mat_code"));
                                    jsonmap.put("matname", job.get("mat_name"));
                                    if(job.isNull("mat_standard")){
                                        jsonmap.put("matstandard", "");
                                    }else{
                                        jsonmap.put("matstandard", job.get("mat_standard"));
                                    }
                                    if(job.isNull("units")){
                                        jsonmap.put("units", "");
                                    }else{
                                        jsonmap.put("units", job.get("units"));
                                    }
                                    jsonmap.put("time", job.get("create_date"));
                                    if(job.isNull("qty")){
                                        jsonmap.put("qty", "");
                                    }else{
                                        jsonmap.put("qty", job.get("qty"));
                                    }

                                    list.add(jsonmap);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Message m = new Message();
                            m.what=0x11;
                            handler.sendMessage(m);
                        }
                    }).start();
                    break;
                case R.id.Add:
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                Map<String, String> map = new HashMap<String, String>();

                                spinnerlist = new ArrayList<Map<String,Object>>();
                                map.put("limit", String.valueOf(100));
                                map.put("start", String.valueOf(0));
                                JSONArray json = new JSONArray(HttpUtil.postRequest("http://"+ip+":8888/sclimat/grid_queryMaterial",map));
                                for(int j=0;j<json.length();j++){
                                    Map<String, Object> jsonmap = new HashMap<String, Object>();
                                    JSONObject obj = json.getJSONObject(j);
                                    jsonmap.put("matid",obj.get("mat_id"));
                                    jsonmap.put("matdetail",obj.get("mat_detail"));
                                    spinnerlist.add(jsonmap);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Message msg = new Message();
                            msg.what=0x12;
                            handler.sendMessage(msg);
                        }
                    }).start();

                    break;
                case R.id.Remove:

                    if(removelist.size()!=0){
                        AlertDialog loginout = new AlertDialog.Builder(UserMaterialActivity.this)
                                .setTitle("提示")
                                .setMessage("你确定删除！")
                                .setPositiveButton("确定",new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,int which) {
                                        StringBuffer buf = new StringBuffer();
                                        for(int k=0;k<removelist.size();k++){
                                            final Map<String,String> matid = removelist.get(k);
                                            buf.append(matid.get("uid"));
                                            buf.append(",");
                                        }
                                        final Map<String,String> map = new HashMap<String, String>();
                                        map.put("uid",buf.toString());
                                        new Thread(new Runnable() {
                                            public void run() {
                                                try {
                                                    JSONObject json = new JSONObject(HttpUtil.postRequest("http://"+ip+":8888/sclimat/use_remove",map));
                                                    System.out.println("到了吗");
                                                    removemsg = json.getString("msg");
                                                    Message mg = new Message();
                                                    mg.what=0x15;
                                                    handler.sendMessage(mg);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();
                                    }
                                }).setNegativeButton("取消", null).show();
                    }
                    break;
                case R.id.confirm:
                    if(removelist.size()!=0){
                        AlertDialog loginout = new AlertDialog.Builder(UserMaterialActivity.this)
                                .setTitle("提示")
                                .setMessage("你确定提交！")
                                .setPositiveButton("确定",new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,int which) {
                                        StringBuffer buf = new StringBuffer();
                                        StringBuffer str = new StringBuffer();
                                        for(int k=0;k<removelist.size();k++){
                                            final Map<String,String> matid = removelist.get(k);
                                            buf.append(matid.get("matcode"));
                                            str.append(matid.get("qty"));
                                            buf.append(",");
                                            str.append(",");
                                        }
                                        final Map<String,String> map = new HashMap<String, String>();
                                        map.put("mat_code",buf.toString());
                                        map.put("num", str.toString());
                                        new Thread(new Runnable() {
                                            public void run() {
                                                try {
                                                    JSONObject json = new JSONObject(HttpUtil.postRequest("http://"+ip+":8888/sclimat/use_confirm",map));
                                                    confirmsg = json.getString("msg");
                                                    Message mg = new Message();
                                                    mg.what=0x16;
                                                    handler.sendMessage(mg);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();
                                    }
                                }).setNegativeButton("取消", null).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

}

