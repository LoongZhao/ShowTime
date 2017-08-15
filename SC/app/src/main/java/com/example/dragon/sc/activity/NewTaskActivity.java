package com.example.dragon.sc.activity;

/**
 * Created by Dragon on 2017/8/9.
 */



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.example.dragon.sc.R;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONArray;
import org.json.JSONObject;
import com.example.dragon.sc.util.DialogUtil;
import com.example.dragon.sc.util.HttpUtil;
import com.example.dragon.sc.util.JSONAdapter;
import com.example.dragon.sc.util.JSONArrayAdapter;
import com.example.dragon.sc.util.ViewHolder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup;

public class NewTaskActivity extends Activity {

    private static ImageView newup;
    private static TextView wcdbovename,uptex,renwutex,downtex,info;
    private static LinearLayout uplinear,renwuLinear,downlinear;
    private static RelativeLayout huoxiangrela,loadrela,saverela,completerela;
    private static String uId,whse,co,div,ip;
    private static String flag = "W";
    private static TableRow textab;
    private static ListView skulist,huoxianglist;
    private static String mergeNbr;
    static ViewHolder vHollder;
    private static ArrayList<Map<String,Object>> arrlist;
    private static ArrayList<Map<String,Object>> datalist;
    private static List serlist = new ArrayList();
    static int page = 0;
    private static JSONArray jsonobjtree,jsonobjhuoxiangtree;
    private static TextView pkt2,desc2,num2,ordseq2,refnum12,sku2,tonbr2,zone2,pck2;
    static JSONArrayAdapter jsonarryadapter = null;
    private static String flg=null;
    static Handler h = null;
    static ProgressDialog progressDialog = null;
    private static List<Map<String,Object>> strlist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action);

        newup = (ImageView) findViewById(R.id.newup);
        wcdbovename = (TextView) findViewById(R.id.wcdbovename);
        uplinear = (LinearLayout) findViewById(R.id.uplinear);
        renwuLinear = (LinearLayout) findViewById(R.id.renwuLinear);
        downlinear = (LinearLayout) findViewById(R.id.downlinear);
        huoxiangrela = (RelativeLayout) findViewById(R.id.huoxiangrela);
        loadrela = (RelativeLayout) findViewById(R.id.loadrela);
        uptex = (TextView) findViewById(R.id.uptex);
        renwutex = (TextView) findViewById(R.id.renwutex);
        downtex = (TextView) findViewById(R.id.downtex);
        info = (TextView) findViewById(R.id.info);
        textab = (TableRow) findViewById(R.id.textab);
        skulist = (ListView) findViewById(R.id.skulist);
        saverela = (RelativeLayout) findViewById(R.id.saverela);
        completerela = (RelativeLayout) findViewById(R.id.completerela);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        uId = (String) bundle.get("uId");
        ip = (String) bundle.get("ip");
        whse = (String) bundle.get("whse");
        co = (String) bundle.get("co");
        div = (String) bundle.get("div");

        wcdbovename.setText(uId);
        newup.setOnClickListener(listener);
        wcdbovename.setOnClickListener(listener);
        uplinear.setOnClickListener(listener);
        renwuLinear.setOnClickListener(listener);
        downlinear.setOnClickListener(listener);
        huoxiangrela.setOnClickListener(listener);
        loadrela.setOnClickListener(listener);
        saverela.setOnClickListener(listener);
        completerela.setOnClickListener(listener);

        h = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x23:
                        progressDialog.dismiss();
                        LayoutInflater inflater = LayoutInflater.from(NewTaskActivity.this);
                        final View view = inflater.inflate(R.layout.huoxianginfo, null);
                        huoxianglist = (ListView) view.findViewById(R.id.huoxianglist);
                        TextView huoxianginfo = (TextView) view.findViewById(R.id.huoxianginfo);
                        if(jsonobjhuoxiangtree != null){
                            if(jsonobjhuoxiangtree.length()!=0){
                                huoxianginfo.setVisibility(View.GONE);
                                jsonarryadapter = new JSONArrayAdapter(NewTaskActivity.this, datalist);
                                huoxianglist.setAdapter(jsonarryadapter);
                            }else{
                                huoxianginfo.setVisibility(View.VISIBLE);
                                huoxianginfo.setText("无加载数据,请重新刷新或者登录下");
                            }
                        }
                        AlertDialog dialog = new AlertDialog.Builder(NewTaskActivity.this)
                                .setTitle("货箱信息")
                                .setView(view)
                                .setPositiveButton("确定",null).show();
                        break;
                    case 0x34:
                        progressDialog.dismiss();
                        if(jsonobjtree!=null){
                            if(jsonobjtree.length()==0){
                                skulist.setVisibility(View.GONE);
                                info.setVisibility(View.VISIBLE);
                                saverela.setVisibility(View.GONE);
                                completerela.setVisibility(View.GONE);
                                textab.setVisibility(View.GONE);
                                renwutex.setText("新派发的任务");
                                info.setText("无加载数据,请重新刷新或者登录");
                            }else{
                                skulist.setVisibility(View.VISIBLE);
                                info.setVisibility(View.GONE);
                                saverela.setVisibility(View.VISIBLE);
                                completerela.setVisibility(View.VISIBLE);
                                textab.setVisibility(View.VISIBLE);
                                renwutex.setVisibility(View.VISIBLE);
                                JSONAdapter jsonadapter = new JSONAdapter(NewTaskActivity.this, arrlist);
                                skulist.setAdapter(jsonadapter);
                                renwutex.setText(mergeNbr);
                                skulist.setOnItemClickListener(new OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> parent,View view, int position, long id) {
                                        vHollder = (ViewHolder) view.getTag();
                                        vHollder.cBox.toggle();
                                        JSONAdapter.isSelected.put(position, vHollder.cBox.isChecked());
                                        Map<String,String> map = new HashMap<String, String>();
                                        try {
                                            String ordnbr = vHollder.pktCtrlNbr.getText().toString().trim();
                                            String pktId = vHollder.pktId.getText().toString().trim();
                                            String qty = vHollder.num.getText().toString().trim();
                                            map.put("pktId", pktId);
                                            map.put("nbr", qty);
                                            map.put("ordnbr", ordnbr);
                                            if(vHollder.cBox.isChecked()){
                                                flg = "P";
                                            }else{
                                                flg = "N";
                                            }
                                            map.put("flg", flg);
                                            serlist.add(map);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                skulist.setOnItemLongClickListener(new OnItemLongClickListener() {

                                    @Override
                                    public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) {
                                        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                        vibrator.vibrate(40);

                                        vHollder = (ViewHolder) view.getTag();
                                        LayoutInflater inflater = LayoutInflater.from(NewTaskActivity.this);
                                        final View itemview = inflater.inflate(R.layout.action_item, null);
                                        String pckid1 = vHollder.pktId.getText().toString().trim();
                                        String pktnbr1 = vHollder.pktCtrlNbr.getText().toString().trim();
                                        String ordseq1 = vHollder.ordseq.getText().toString().trim();
                                        String zone1 = vHollder.posalzone.getText().toString().trim();
                                        String sku1 = vHollder.sku.getText().toString().trim();
                                        String desc1 = vHollder.skuDesc.getText().toString().trim();
                                        String num1 = vHollder.num.getText().toString().trim();
                                        String ref1 = vHollder.refnum1.getText().toString().trim();
                                        String tonby1 = vHollder.tonby.getText().toString().trim();
                                        pck2  = (TextView) itemview.findViewById(R.id.item_pckId);
                                        pkt2  = (TextView) itemview.findViewById(R.id.item_pkt);
                                        desc2  = (TextView) itemview.findViewById(R.id.item_desc);
                                        num2  = (TextView) itemview.findViewById(R.id.item_num);
                                        ordseq2  = (TextView) itemview.findViewById(R.id.item_ordseq);
                                        refnum12  = (TextView) itemview.findViewById(R.id.item_refnum1);
                                        sku2  = (TextView) itemview.findViewById(R.id.item_sku);
                                        tonbr2  = (TextView) itemview.findViewById(R.id.item_tonbr);
                                        zone2  = (TextView) itemview.findViewById(R.id.item_zone);

                                        pck2.setText(pckid1);
                                        pkt2.setText(pktnbr1);
                                        desc2.setText(desc1);
                                        num2.setText(num1);
                                        ordseq2.setText(ordseq1);
                                        tonbr2.setText(tonby1);
                                        refnum12.setText(ref1);
                                        sku2.setText(sku1);
                                        zone2.setText(zone1);
                                        AlertDialog dialog = new AlertDialog.Builder(NewTaskActivity.this)
                                                .setTitle("订单明细")
                                                .setView(itemview)
                                                .setPositiveButton("保存",
                                                        new DialogInterface.OnClickListener() {

                                                            @Override
                                                            public void onClick(DialogInterface dialog,int which) {
                                                                String ref = refnum12.getText().toString();
                                                                String tonbr = tonbr2.getText().toString();
                                                                String pckId = pck2.getText().toString();
                                                                Map<String,String> map = new HashMap<String, String>();
                                                                map.put("refnum1", ref);
                                                                map.put("tonby", tonbr);
                                                                map.put("pckId", pckId);
                                                                try {
                                                                    HttpUtil.postRequest(HttpUtil.BASE_URL(ip) + "setref.jsp", map);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                                loadrela.performClick();
                                                            }
                                                        }).setNegativeButton("关闭", null).show();
                                        return true;
                                    }
                                });
                            }
                        }
                        break;
                    case 0x90:
                        if(page==jsonobjtree.length()-1){
                            page--;
                            loadrela.performClick();
                        }else{
                            loadrela.performClick();
                        }
                        break;
                    case 1:
                        progressDialog.dismiss();
                        Toast.makeText(NewTaskActivity.this, "connection timeout......", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        progressDialog.dismiss();
                        Toast.makeText(NewTaskActivity.this, "There is no network....", Toast.LENGTH_LONG).show();
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
                case R.id.newup:
                    finish();
                    break;
                case R.id.uplinear:
                    uplinear.setBackgroundColor(Color.parseColor("#00928D"));
                    renwuLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                    downlinear.setBackgroundColor(Color.parseColor("#ffffff"));
                    uptex.setTextColor(Color.parseColor("#ffffff"));
                    renwutex.setTextColor(Color.parseColor("#00928D"));
                    downtex.setTextColor(Color.parseColor("#00928D"));
                    page--;
                    if(page>=0){
                        loadrela.performClick();
                    }else{
                        page=0;
                    }
                    break;
                case R.id.renwuLinear:
                    renwuLinear.setBackgroundColor(Color.parseColor("#00928D"));
                    uplinear.setBackgroundColor(Color.parseColor("#ffffff"));
                    downlinear.setBackgroundColor(Color.parseColor("#ffffff"));
                    uptex.setTextColor(Color.parseColor("#00928D"));
                    renwutex.setTextColor(Color.parseColor("#ffffff"));
                    downtex.setTextColor(Color.parseColor("#00928D"));
                    break;
                case R.id.downlinear:
                    renwuLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                    uplinear.setBackgroundColor(Color.parseColor("#ffffff"));
                    downlinear.setBackgroundColor(Color.parseColor("#00928D"));
                    uptex.setTextColor(Color.parseColor("#00928D"));
                    renwutex.setTextColor(Color.parseColor("#00928D"));
                    downtex.setTextColor(Color.parseColor("#ffffff"));
                    if(jsonobjtree==null){

                    }else{
                        page++;
                        if(page<jsonobjtree.length()){
                            loadrela.performClick();
                        }else{
                            page=jsonobjtree.length()-1;
                        }
                    }
                    break;
                case R.id.huoxiangrela:
                    progressDialog = android.app.ProgressDialog.show(NewTaskActivity.this, "", "Loading......", true);
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            String action1 = "main.jsp";
                            try {
                                JSONObject jsonhuoxiang = query(whse, co, div, flag, uId,action1);
                                JSONObject jsonobj = jsonhuoxiang.getJSONObject("map");
                                jsonobjhuoxiangtree = jsonobj.getJSONArray("tree");
                                datalist = new ArrayList<Map<String,Object>>();
                                for(int a=0;a<jsonobjhuoxiangtree.length();a++){
                                    JSONObject jsonobjary = (JSONObject) jsonobjhuoxiangtree.getJSONObject(a);
                                    JSONArray jsonary = jsonobjary.getJSONArray("children");
                                    for(int c=0;c<jsonary.length();c++){
                                        HashMap<String, Object> map =  new HashMap<String, Object>();
                                        JSONObject jsonobjhuoxiang = jsonary.getJSONObject(c);
                                        String mergeSeq = jsonobjhuoxiang.getString("mergeSeq");
                                        map.put("mergeSeq", mergeSeq);
                                        String total = jsonobjhuoxiang.getString("total");
                                        map.put("total", total);
                                        String cartonCode = jsonobjhuoxiang.getString("cartonCode");
                                        map.put("cartonCode", cartonCode);
                                        String pktCtrlNbr = jsonobjhuoxiang.getString("pktCtrlNbr");
                                        map.put("pktCtrlNbr", pktCtrlNbr);
                                        datalist.add(map);
                                    }
                                }

                            }catch (ConnectTimeoutException e) {
                                Message m = new Message();
                                m.what=1;
                                h.sendMessage(m);
                            }catch (HttpHostConnectException e) {
                                Message m = new Message();
                                m.what=2;
                                h.sendMessage(m);
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                            Message m = new Message();
                            m.what=0x23;
                            h.sendMessage(m);
                        }
                    }).start();
                    break;

                case R.id.loadrela:

                    Configuration cf= getResources().getConfiguration(); //获取设置的配置信息
                    Display dis = getWindowManager().getDefaultDisplay();
//				int screenWidth = dis.getWidth();// 获取屏幕宽度
//				int screenHeight = dis.getHeight();// 获取屏幕高度

                    int ori = cf.orientation ; //获取屏幕方向

                    if(ori == cf.ORIENTATION_LANDSCAPE){

                        //横屏
                        ViewGroup.LayoutParams params = skulist.getLayoutParams();
                        params.height=370;

                        skulist.setLayoutParams(params);
                    }else if(ori == cf.ORIENTATION_PORTRAIT){

                        // 竖屏
                        ViewGroup.LayoutParams params = skulist.getLayoutParams();
                        params.height=800;
                        skulist.setLayoutParams(params);
                    }
                    progressDialog = android.app.ProgressDialog.show(NewTaskActivity.this, "", "Loading......", true);
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            String action2 = "load.jsp";
                            try {
                                JSONObject jsonmap = query(whse, co, div, flag, uId,action2);
                                JSONObject jsonobjmap = jsonmap.getJSONObject("map");
                                jsonobjtree = jsonobjmap.getJSONArray("tree");
                                arrlist = new ArrayList<Map<String,Object>>();
                                strlist = new ArrayList<Map<String,Object>>();
                                for(int i=0;i<jsonobjtree.length();i++){
                                    JSONObject jsonobji = jsonobjtree.getJSONObject(i);
                                    JSONArray jsonarychild = jsonobji.getJSONArray("children");
                                    if(i==page){
                                        int jsonlength = jsonarychild.length();
                                        for(int q=0;q<jsonlength;q++){
                                            Map<String, Object> map =  new HashMap<String, Object>();
                                            Map<String, Object> ma =  new HashMap<String, Object>();
                                            JSONObject jsonobjq = jsonarychild.getJSONObject(q);
                                            String pickFlag = jsonobjq.getString("pickFlag");
                                            map.put("pickFlag", pickFlag);
                                            String shipToAddr3 = jsonobjq.getString("shipToAddr3");
                                            map.put("posalzone", shipToAddr3);
                                            String mergeSeq = jsonobjq.getString("mergeSeq");
                                            map.put("zhouzhuan", mergeSeq);
                                            String skuDesc = jsonobjq.getString("skuDesc");
                                            map.put("skuDesc", skuDesc);
                                            String fromLoc = jsonobjq.getString("fromLoc");
                                            map.put("loc", fromLoc);
                                            String pckId = jsonobjq.getString("pckId");
                                            map.put("pktId", pckId);
                                            String pktCtrlNbr = jsonobjq.getString("pktCtrlNbr");
                                            map.put("pktCtrlNbr", pktCtrlNbr);
                                            ma.put("pktCtrlNbr", pktCtrlNbr);
                                            strlist.add(ma);
                                            String unitPqt = jsonobjq.getString("unitPqt");
                                            map.put("num", unitPqt);
                                            String sku = jsonobjq.getString("sku");
                                            map.put("sku", sku);
                                            String ordseq = jsonobjq.getString("ordseq");
                                            map.put("ord_seq", q+1);
                                            map.put("id", q+1);
                                            String refnum1 = jsonobjq.getString("refnum1");
                                            map.put("ref_num1", refnum1);
                                            String tonby = jsonobjq.getString("tonby");
                                            map.put("to_nby", tonby);
                                            mergeNbr = jsonobjq.getString("mergeNbr");
                                            map.put("mergeNbr", mergeNbr);
                                            arrlist.add(map);
                                        }
                                    }
                                }
                            }catch (ConnectTimeoutException e) {
                                Message ms = new Message();
                                ms.what=1;
                                h.sendMessage(ms);
                            }catch (HttpHostConnectException e) {
                                Message ms = new Message();
                                ms.what=2;
                                h.sendMessage(ms);
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                            Message ms = new Message();
                            ms.what=0x34;
                            h.sendMessage(ms);
                        }
                    }).start();
                    break;
                case R.id.saverela:
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            Map<String,String> checkpkt1 = new HashMap<String, String>();
                            checkpkt1.put("co", co);
                            Intent dataIntent1 = new Intent(NewTaskActivity.this,uploadService.class);
                            Bundle b1 = new Bundle();
                            b1.putString("ip", ip);
                            b1.putStringArrayList("serlist", (ArrayList<String>) serlist);
                            dataIntent1.putExtras(b1);
                            startService(dataIntent1);
                        }
                    }).start();

                    break;
                case R.id.completerela:
                    List<ViewHolder> li = new ArrayList<ViewHolder>();
                    int count = skulist.getCount();
                    for(int i=0;i<count;i++){
                        if(JSONAdapter.isSelected.get(i)){
                            ViewHolder vHollder  = (ViewHolder) skulist.getTag();
                            li.add(vHollder);
                        }
                    }
                    if((skulist.getCount()-li.size())==0){

                        AlertDialog loginout = new AlertDialog.Builder(NewTaskActivity.this)
                                .setMessage("你确定提交！")
                                .setPositiveButton("确定",new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,int which) {
                                        new Thread(new Runnable() {

                                            @Override
                                            public void run() {
                                                Set set = new HashSet(strlist);
                                                strlist.clear();
                                                strlist.addAll(set);
                                                for(int s=0;s<strlist.size();s++){
                                                    String pktCtrlNbr = strlist.get(s).get("pktCtrlNbr").toString();
                                                    Map<String,String> checkpkt = new HashMap<String, String>();
                                                    checkpkt.put("pktCtrlNbr", pktCtrlNbr);
                                                    checkpkt.put("co", co);
                                                    try {
                                                        JSONObject a = new JSONObject(HttpUtil.postRequest(HttpUtil.BASE_URL(ip) + "checkpkt.jsp", checkpkt));
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                Message me=new Message();
                                                me.what=0x90;
                                                h.sendMessage(me);
                                                Intent dataIntent = new Intent(NewTaskActivity.this,uploadService.class);
                                                Bundle b = new Bundle();
                                                b.putString("ip", ip);
                                                b.putStringArrayList("serlist", (ArrayList<String>) serlist);
                                                dataIntent.putExtras(b);
                                                startService(dataIntent);
                                            }
                                        }).start();
                                    }
                                }).setNegativeButton("取消", null).show();
                    }else{
                        DialogUtil.showDialog(NewTaskActivity.this, "你还有 "+(skulist.getCount()-li.size())+" 件货没捡!", false);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @SuppressWarnings("unused")
    private JSONObject query(String whse,String co,String div,String flag,String uId,String action) throws Exception
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("uId", uId);
        map.put("whse", whse);
        map.put("co", co);
        map.put("div", div);
        map.put("flag", flag);
        String url = HttpUtil.BASE_URL(ip) + action;
        return new JSONObject(HttpUtil.postRequest(url, map));
    }

    @SuppressWarnings("unused")
    private JSONObject search(String co,String pktCtrlNbr) throws Exception
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("co", co);
        map.put("pktCtrlNbr", pktCtrlNbr);
        String url = HttpUtil.BASE_URL(ip) + "checkpkt.jsp";
        return new JSONObject(HttpUtil.postRequest(url, map));
    }

}
