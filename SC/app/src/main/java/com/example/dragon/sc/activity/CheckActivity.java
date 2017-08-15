package com.example.dragon.sc.activity;

/**
 * Created by Dragon on 2017/8/9.
 */



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dragon.sc.R;
import com.example.dragon.sc.util.CheckViewHolder;
import com.example.dragon.sc.util.HttpUtil;
import com.example.dragon.sc.util.JSONCheckAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CheckActivity extends Activity{

    private ImageView menuup;
    private TextView wcdbovename,totaltext,startpage,endpage,checkmenu;
    private String uId,ip,whse,co,div;
    private LinearLayout prex,next;
    List<Map<String,Object>> list = null;
    private ListView checklistview;
    Handler handler = null;
    private int page=0;
    String total = null;
    private RelativeLayout checkload,checksave,checkcomplete;
    CheckViewHolder checkviewhodler;
    private String ceng;
    JSONObject jn = null;
    JSONObject jso = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.check);

        menuup = (ImageView) findViewById(R.id.menuup);
        wcdbovename = (TextView) findViewById(R.id.wcdbovename);
        prex = (LinearLayout) findViewById(R.id.prex);
        next = (LinearLayout) findViewById(R.id.next);
        checklistview = (ListView) findViewById(R.id.checklistview);
        totaltext = (TextView) findViewById(R.id.total);
        startpage = (TextView) findViewById(R.id.pagenow);
        endpage = (TextView) findViewById(R.id.pagesize);
        checkload = (RelativeLayout) findViewById(R.id.checkload);
        checkmenu = (TextView) findViewById(R.id.checkmenu);
        checksave = (RelativeLayout) findViewById(R.id.checksave);
        checkcomplete = (RelativeLayout) findViewById(R.id.checkcomplete);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        uId = bundle.getString("uId");
        ip = bundle.getString("ip");
        whse = bundle.getString("whse");
        co = bundle.getString("co");
        div = bundle.getString("div");

        checkmenu.setText(co+"盘点");
        wcdbovename.setText(uId);
        menuup.setOnClickListener(listener);
        prex.setOnClickListener(listener);
        next.setOnClickListener(listener);
        checkload.setOnClickListener(listener);
        checksave.setOnClickListener(listener);
        checkcomplete.setOnClickListener(listener);

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x10:
                        totaltext.setText(total);
                        startpage.setText(String.valueOf(page*30+1));
                        endpage.setText(String.valueOf((page+1)*30));
                        JSONCheckAdapter adapter = new JSONCheckAdapter(CheckActivity.this,list);
                        adapter.notifyDataSetChanged();
                        checklistview.setAdapter(adapter);
                        checklistview.findViewById(R.id.checkqty_listview);

                        break;
                    case 0x11:
                        try {
                            String a = jn.getString("a");
                            if(Integer.parseInt(a)>0){
                                AlertDialog dialog = new AlertDialog.Builder(CheckActivity.this)
                                        .setMessage("保存成功!")
                                        .setPositiveButton("确定", null).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case 0x12:
                        try {
                            String a = jso.getString("a");
                            if(Integer.parseInt(a)>0){
                                AlertDialog dialog = new AlertDialog.Builder(CheckActivity.this)
                                        .setTitle("提示")
                                        .setMessage("此次"+co+"盘点结束!")
                                        .setPositiveButton("确定", null).show();
                            }
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
                case R.id.menuup:
                    finish();
                    break;
                case R.id.checksave:
                    Set<String> b = JSONCheckAdapter.isCheckBoxSKU.keySet();
                    Set<Integer> a = JSONCheckAdapter.Selecteded.keySet();
                    List<Integer> aa = new ArrayList<Integer>();
                    List<String> bb = new ArrayList<String>();
                    aa.addAll(a);
                    bb.addAll(b);
                    StringBuffer buf = new StringBuffer();
                    StringBuffer str = new StringBuffer();
                    StringBuffer sbuf = new StringBuffer();
                    StringBuffer sstr = new StringBuffer();
                    final Map<String,String> map = new HashMap<String, String>();
                    for(int j=bb.size()-1;j>=0;j--){
                        Object bbb = bb.get(j);
                        if(j>0){
                            sbuf.append(bbb);
                            sbuf.append(",");
                            sstr.append(JSONCheckAdapter.isCheckBoxSKU.get(bbb).toString());
                            sstr.append(",");
                        }else{
                            sbuf.append(bbb);
                            sstr.append(JSONCheckAdapter.isCheckBoxSKU.get(bbb).toString());
                        }
                    }
                    map.put("ch_sku", sbuf.toString());
                    map.put("checkbox", sstr.toString());
                    for(int i=aa.size()-1;i>=0;i--){
                        Object aaa = aa.get(i);
                        if(i>0){
                            buf.append(aaa);
                            buf.append(",");
                            str.append(JSONCheckAdapter.Selecteded.get(aaa).toString());
                            str.append(",");
                        }else{
                            buf.append(aaa);
                            str.append(JSONCheckAdapter.Selecteded.get(aaa).toString());
                        }
                    }
                    map.put("che_id", buf.toString());
                    map.put("checked", str.toString());
                    map.put("checkman", uId);
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                jn = new JSONObject(HttpUtil.postRequest("http://"+ip+":8888/sclimat/check_updateCheck", map));
                                Message m1 = new Message();
                                m1.what=0x11;
                                handler.sendMessage(m1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                    break;
                case R.id.checkload:
                    LayoutInflater inflater = LayoutInflater.from(CheckActivity.this);
                    final View view = inflater.inflate(R.layout.ceng, null);
                    AlertDialog dialog = new AlertDialog.Builder(CheckActivity.this)
                            .setTitle("你想盘点")
                            .setView(view)
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            EditText cengedit = (EditText) view.findViewById(R.id.EditIp);
                                            ceng = cengedit.getText().toString().trim();
                                            page=0;
                                            new Thread(new Runnable() {

                                                @Override
                                                public void run() {
                                                    try {
                                                        JSONObject json = query(co,ceng,"30","0");
                                                        JSONArray jsonary = json.getJSONArray("list");
                                                        total = json.getString("total");
                                                        list = new ArrayList<Map<String,Object>>();
                                                        for(int i=0;i<jsonary.length();i++){
                                                            JSONObject job = jsonary.getJSONObject(i);
                                                            Map<String,Object> map = new HashMap<String, Object>();
                                                            map.put("ch_id",job.getInt("ch_id"));
                                                            map.put("che_id", job.getInt("che_id"));
                                                            map.put("ch_sku", job.getString("ch_sku"));
                                                            map.put("ch_loc", job.getString("ch_loc"));
                                                            map.put("ch_sysqty", job.getString("ch_sysqty"));
                                                            map.put("ch_xjqty", job.getString("ch_xjqty"));
                                                            if(job.isNull("ch_state")){
                                                                map.put("ch_state", "");
                                                            }else{
                                                                map.put("ch_state", job.getString("ch_state"));
                                                            }
                                                            if(job.isNull("ch_qty")){
                                                                map.put("ch_qty", "");
                                                            }else{
                                                                map.put("ch_qty", job.getString("ch_qty"));
                                                            }
                                                            list.add(map);
                                                        }
                                                        Message msg = new Message();
                                                        msg.what=0x10;
                                                        handler.sendMessage(msg);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }).start();
                                        }
                                    }).setNegativeButton("取消", null).show();


                    break;
                case R.id.prex:
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                JSONObject json = query(co,ceng,"30",String.valueOf(page));
                                JSONArray jsonary = json.getJSONArray("list");
                                list = new ArrayList<Map<String,Object>>();
                                for(int i=0;i<jsonary.length();i++){
                                    JSONObject job = jsonary.getJSONObject(i);
                                    Map<String,Object> map = new HashMap<String, Object>();
                                    map.put("ch_id",job.getInt("ch_id"));
                                    map.put("che_id", job.getInt("che_id"));
                                    map.put("ch_sku", job.getString("ch_sku"));
                                    map.put("ch_loc", job.getString("ch_loc"));
                                    map.put("ch_sysqty", job.getString("ch_sysqty"));
                                    map.put("ch_xjqty", job.getString("ch_xjqty"));
                                    if(job.isNull("ch_state")){
                                        map.put("ch_state", "");
                                    }else{
                                        map.put("ch_state", job.getString("ch_state"));
                                    }
                                    if(job.isNull("ch_qty")){
                                        map.put("ch_qty", "");
                                    }else{
                                        map.put("ch_qty", job.getString("ch_qty"));
                                    }
                                    list.add(map);
                                }
                                Message msg = new Message();
                                msg.what=0x10;
                                handler.sendMessage(msg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    if(page>0){
                        page--;
                    }else{
                        page=0;
                        Toast.makeText(CheckActivity.this, "已经是第一页了", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.next:
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                JSONObject json = query(co,ceng,"30",String.valueOf(page));
                                JSONArray jsonary = json.getJSONArray("list");
                                list = new ArrayList<Map<String,Object>>();
                                for(int i=0;i<jsonary.length();i++){
                                    JSONObject job = jsonary.getJSONObject(i);
                                    Map<String,Object> map = new HashMap<String, Object>();
                                    map.put("ch_id",job.getInt("ch_id"));
                                    map.put("che_id", job.getInt("che_id"));
                                    map.put("ch_sku", job.getString("ch_sku"));
                                    map.put("ch_loc", job.getString("ch_loc"));
                                    map.put("ch_sysqty", job.getString("ch_sysqty"));
                                    map.put("ch_xjqty", job.getString("ch_xjqty"));
                                    if(job.isNull("ch_state")){
                                        map.put("ch_state", "");
                                    }else{
                                        map.put("ch_state", job.getString("ch_state"));
                                    }
                                    if(job.isNull("ch_qty")){
                                        map.put("ch_qty", "");
                                    }else{
                                        map.put("ch_qty", job.getString("ch_qty"));
                                    }
                                    list.add(map);
                                }
                                Message msg = new Message();
                                msg.what=0x10;
                                handler.sendMessage(msg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    int p = 0;
                    if(Integer.parseInt(total)%30==0){
                        p = Integer.parseInt(total)/30;
                    }else{
                        p = Integer.parseInt(total)/30+1;
                    }
                    if(page<p-1){
                        page++;
                    }else{
                        page=p-1;
                        Toast.makeText(CheckActivity.this, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.checkcomplete:
                    AlertDialog dial = new AlertDialog.Builder(CheckActivity.this)
                            .setTitle("提示")
                            .setMessage("亲,你的小伙伴是否都已经盘点完成了!")
                            .setPositiveButton("完成",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,int which) {
                                            final Map<String,String> ma = new HashMap<String,String>();
                                            String ch_id = JSONCheckAdapter.SelectedChId.get("ch_id").toString();
                                            ma.put("ch_id", ch_id);
                                            new Thread(new Runnable() {

                                                @Override
                                                public void run() {
                                                    try {
                                                        jso = new JSONObject(HttpUtil.postRequest("http://"+ip+":8888/sclimat/check_UpdateComplete", ma));
                                                        Message mse = new Message();
                                                        mse.what=0x12;
                                                        handler.sendMessage(mse);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }).start();
                                        }
                                    }).setNegativeButton("取消", null).show();
                    break;
                default:
                    break;
            }
        }
    };

    private JSONObject query(String co,String ceng,String pageSize,String pageNow) throws Exception
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("ceng", ceng);
        map.put("co", co);
        map.put("pageSize", pageSize);
        map.put("pageNow", pageNow);
        String url = "http://"+ip+":8888/sclimat/check_searchCheck";
        return new JSONObject(HttpUtil.postRequest(url, map));
    }

}

