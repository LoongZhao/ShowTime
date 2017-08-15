package com.example.dragon.sc.activity;

/**
 * Created by Dragon on 2017/8/9.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.dragon.sc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MenuActivity extends Activity {

    private ImageView menuup;
    private TextView wcdbovename;
    private RelativeLayout wcd;
    private String jsonmap;
    private String uId,ip,whse,co,div;
    private String jsony,searchdetail;
    List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
    List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
//    ExAdapter adapter;
    ExpandableListView exList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        menuup = (ImageView) findViewById(R.id.menuup);
        wcdbovename = (TextView) findViewById(R.id.wcdbovename);
        wcd = (RelativeLayout) findViewById(R.id.wcd);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
//        jsonmap = bundle.getString("jsonmap");
        uId = bundle.getString("uId");
        ip = bundle.getString("ip");
        whse = bundle.getString("whse");
        co = bundle.getString("co");
        div = bundle.getString("div");
//        jsony = bundle.getString("jsony");

        wcdbovename.setText(uId);
        menuup.setOnClickListener(listener);

//        try {
//            Map<String, String> curGroupMap = new HashMap<String, String>();
//            JSONObject sobjjson = new JSONObject(jsony);
//            String text = sobjjson.getString("text");
//            JSONObject attributes = sobjjson.getJSONObject("attributes");
//            String url = attributes.getString("url");
//            curGroupMap.put("text", text);
//            curGroupMap.put("url", url);
//            groupData.add(curGroupMap);
//            JSONArray sobjjsonary = sobjjson.getJSONArray("children");
//            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
//            for(int l=0;l<sobjjsonary.length();l++){
//                Map<String, String> curChildMap = new HashMap<String, String>();
//                JSONObject arytobj = sobjjsonary.getJSONObject(l);
//                String childtext = arytobj.getString("text");
//                JSONObject obj = arytobj.getJSONObject("attributes");
//                String ur = obj.getString("url");
//                if(ur.equals("search.app")){
//                    JSONArray searchjson = arytobj.getJSONArray("children");
//                    for(int m=0;m<searchjson.length();m++){
//                        searchdetail = searchjson.getString(m).toString();
//                    }
//                }
//                curChildMap.put("childtext", childtext);
//                curChildMap.put("ur", ur);
//                children.add(curChildMap);
//            }
//            childData.add(children);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        adapter=new ExAdapter(MenuActivity.this);
//        exList = (ExpandableListView) findViewById(R.id.treelist);
//        registerForContextMenu(exList);
//        exList.setAdapter(adapter);
//        exList.setGroupIndicator(null);
//        exList.setDivider(null);

    }

    private View.OnClickListener listener = new View.OnClickListener() {

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
        MenuActivity exlistview;

        public ExAdapter(MenuActivity elv) {
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
            title.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(buttonname.equals("new_task.app")){
                        Intent intent1 = new Intent(MenuActivity.this,NewTaskActivity.class);
                        intent1.putExtra("uId", uId);
                        intent1.putExtra("ip", ip);
                        intent1.putExtra("whse", whse);
                        intent1.putExtra("co", co);
                        intent1.putExtra("div", div);
                        startActivity(intent1);
                    }
                    if(buttonname.equals("search.app")){
                        Intent intent3 = new Intent(MenuActivity.this,SearchMenuActivity.class);
                        System.out.println(searchdetail+"AAAAAAAAAAAAAA");
                        intent3.putExtra("json", searchdetail);
                        intent3.putExtra("uId", uId);
                        intent3.putExtra("ip", ip);
                        intent3.putExtra("whse", whse);
                        intent3.putExtra("co", co);
                        intent3.putExtra("div", div);
                        startActivity(intent3);
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
}

