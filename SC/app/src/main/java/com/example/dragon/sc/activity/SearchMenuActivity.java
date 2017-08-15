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
import com.example.dragon.sc.R;

import com.example.dragon.sc.util.HttpUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SearchMenuActivity extends Activity{

    private ImageView menuup;
    private TextView wcdbovename;
    private RelativeLayout wcd;
    private String jsonmap;
    private String uId,ip,whse,co,div;
    private String checkjson;
    List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
    List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
    ExAdapter adapter;
    ExpandableListView exList;
    Handler handler = null;
    ProgressDialog progressdialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stockcheck);
        menuup = (ImageView) findViewById(R.id.menuup);
        wcdbovename = (TextView) findViewById(R.id.wcdbovename);
        wcd = (RelativeLayout) findViewById(R.id.wcd);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        jsonmap = bundle.getString("jsonmap");
        uId = bundle.getString("uId");
        ip = bundle.getString("ip");
        whse = bundle.getString("whse");
        co = bundle.getString("co");
        div = bundle.getString("div");
        checkjson = bundle.getString("json");

        wcdbovename.setText(uId);
        menuup.setOnClickListener(listener);
        try {
            Map<String, String> curGroupMap = new HashMap<String, String>();
            System.out.println(checkjson+"KKKKKKKKKKKKKKKKK");
            JSONObject sobjjson = new JSONObject(checkjson);
            String text = sobjjson.getString("text");
            JSONObject attributes = sobjjson.getJSONObject("attributes");
            String url = attributes.getString("url");
            curGroupMap.put("text", text);
            curGroupMap.put("url", url);
            groupData.add(curGroupMap);
            JSONArray sobjjsonary = sobjjson.getJSONArray("children");
            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
            for(int l=0;l<sobjjsonary.length();l++){
                Map<String, String> curChildMap = new HashMap<String, String>();
                JSONObject arytobj = sobjjsonary.getJSONObject(l);
                String childtext = arytobj.getString("text");
                JSONObject obj = arytobj.getJSONObject("attributes");
                String ur = obj.getString("url");
                curChildMap.put("childtext", childtext);
                curChildMap.put("ur", ur);
                children.add(curChildMap);
            }
            childData.add(children);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter=new ExAdapter(SearchMenuActivity.this);
        exList = (ExpandableListView) findViewById(R.id.treelist);
        exList.setAdapter(adapter);
        exList.setGroupIndicator(null);
        exList.setDivider(null);
    }

    private OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.menuup:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    class ExAdapter extends BaseExpandableListAdapter {
        SearchMenuActivity exlistview;

        public ExAdapter(SearchMenuActivity elv) {
            super();
            exlistview = elv;
        }
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {

            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.tree, null);
            }

            TextView title = (TextView) view.findViewById(R.id.lineartext);
            TextView explain = (TextView) view.findViewById(R.id.lineartextexplain);
            title.setText(groupData.get(groupPosition).get("text").toString());
            explain.setText(groupData.get(groupPosition).get("url").toString());

            ImageView image=(ImageView) view.findViewById(R.id.oajia);
            if(isExpanded){
                image.setBackgroundResource(R.drawable.jian);
            }else{
                image.setBackgroundResource(R.drawable.jia);
            }

            return view;
        }


        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public Object getGroup(int groupPosition) {
            return groupData.get(groupPosition).get("text").toString();
        }

        public int getGroupCount() {
            return groupData.size();

        }

        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.treebuttonlist, null);
            }
            final Button title = (Button) view.findViewById(R.id.newtask);
            final String buttonname = childData.get(groupPosition).get(childPosition).get("ur").toString();
            title.setText(childData.get(groupPosition).get(childPosition).get("childtext").toString());
            title.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(buttonname.equals("realtimesohlookup.app")){
                        Intent intent1 = new Intent(SearchMenuActivity.this,SousuoActivity.class);
                        intent1.putExtra("uId", uId);
                        intent1.putExtra("ip", ip);
                        intent1.putExtra("whse", whse);
                        intent1.putExtra("co", co);
                        intent1.putExtra("div", div);
                        startActivity(intent1);
                    }
                    if(buttonname.equals("cartoninformation.app")){
                        Intent intent1 = new Intent(SearchMenuActivity.this,CartonInformationActivity.class);
                        intent1.putExtra("uId", uId);
                        intent1.putExtra("ip", ip);
                        intent1.putExtra("whse", whse);
                        intent1.putExtra("co", co);
                        intent1.putExtra("div", div);
                        startActivity(intent1);
                    }
                    if(buttonname.equals("replenishment.app")){
                        Intent replenishIntent = new Intent(SearchMenuActivity.this,ReplenishActivity.class);
                        replenishIntent.putExtra("uId", uId);
                        replenishIntent.putExtra("ip", ip);
                        replenishIntent.putExtra("whse", whse);
                        replenishIntent.putExtra("co", co);
                        replenishIntent.putExtra("div", div);
                        startActivity(replenishIntent);
                    }
                }
            });
            return view;
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public Object getChild(int groupPosition, int childPosition) {
            return childData.get(groupPosition).get(childPosition).get("childtext").toString();
        }

        public int getChildrenCount(int groupPosition) {
            return childData.get(groupPosition).size();
        }

        public boolean hasStableIds() {
            return true;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

    private JSONObject query(String co,String action,String pageSize,String pageNow) throws Exception
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("co", co);
        map.put("pageSize", pageSize);
        map.put("pageNow", pageNow);
        String url = HttpUtil.BASE_URL(ip) + action;
        return new JSONObject(HttpUtil.postRequest(url, map));
    }

}

