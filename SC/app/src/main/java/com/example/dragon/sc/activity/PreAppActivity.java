package com.example.dragon.sc.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.dragon.sc.R;
import com.example.dragon.sc.database.MySQLiteOpenHelper;
import com.example.dragon.sc.util.HttpUtil;
import com.example.dragon.sc.util.MyAdapter;
import com.example.dragon.sc.util.MyListItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class PreAppActivity extends Activity {
	private int divposition,coposition,whseposition;
	private View view1;
	private String whse,co,div;
	private Spinner w,c,d;
	private ImageView imageclose,oajia,wmjia;
	private String uId,ip;
	private TextView wcdbovename;
	private MySQLiteOpenHelper mySQLiteOpenHelper;
	private SQLiteDatabase sDatabase = null;
	private String jsonwhse,jsonco,jsondiv,jsonpass;
	private static final String AppInfo = "AppInfo";
	private static String DATA_URL = "/data/data/";
	private ListView treelist;
	private JSONObject resultJson,jsonObjma;
//	private JSONTreeAdapter tree;
	List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
	List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
	ExAdapter adapter;
	ExpandableListView exList;
	JSONObject jsony,checkjson;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preindex);


		imageclose = (ImageView) findViewById(R.id.imageclose);
		wcdbovename = (TextView) findViewById(R.id.wcdbovename);
		oajia = (ImageView) findViewById(R.id.oajia);
		treelist = (ListView) findViewById(R.id.treelist);
		mySQLiteOpenHelper = new MySQLiteOpenHelper(getApplicationContext(), "scliappClient.db", null, 1);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		uId = bundle.getString("uId");
		ip = bundle.getString("ip");
		jsonwhse = bundle.getString("jsonwhse");
		jsonco = bundle.getString("jsonco");
		jsondiv = bundle.getString("jsondiv");
		jsonpass = bundle.getString("jsonpass");
		wcdbovename.setText(uId);
		final Map<String, String> map = new HashMap<String, String>();
		map.put("user", uId);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String sendurl = HttpUtil.BASE_URL(ip) + "user/menu.do";
					resultJson =new JSONObject(HttpUtil.postRequest(sendurl,map));
					String sendurl2 = HttpUtil.BASE_URL(ip) + "user/WCD.do";
					jsonObjma =new JSONObject(HttpUtil.postRequest(sendurl2,map));
				}catch (Exception e){
					e.printStackTrace();
				}
			}

		}).start();



		imageclose.setOnClickListener(listener);

		try{
			JSONArray treejsonobj = resultJson.getJSONArray("tree");
			Log.d("***", "treejsonobj");
			for(int l=0;l<treejsonobj.length();l++){
				JSONObject jsontary = treejsonobj.getJSONObject(l);
				String id = jsontary.getString("id");
				JSONArray childjsonary = jsontary.getJSONArray("children");
				for(int k=0;k<childjsonary.length();k++){
					Map<String, String> curGroupMap = new HashMap<String, String>();
					JSONObject childjsontary = childjsonary.getJSONObject(k);
					String text = childjsontary.getString("text");
					JSONObject attributes = childjsontary.getJSONObject("attributes");
					String url = attributes.getString("url");
					curGroupMap.put("text", text);
					curGroupMap.put("url", url);
					groupData.add(curGroupMap);
					JSONArray aryjson = childjsontary.getJSONArray("children");
					List<Map<String, String>> children = new ArrayList<Map<String, String>>();
					for(int u=0;u<aryjson.length();u++){

						Map<String, String> curChildMap = new HashMap<String, String>();
						JSONObject objjson = aryjson.getJSONObject(u);
						String childtext = objjson.getString("text");
						JSONObject attr = objjson.getJSONObject("attributes");
						String ur = attr.getString("url");
						if(ur.trim().equals("order_picking.app")){
							JSONArray arraychild = objjson.getJSONArray("children");
							for(int y=0;y<arraychild.length();y++){
								jsony = arraychild.getJSONObject(y);
							}
						}
						if(ur.trim().equals("checkstatistics.app")){
							JSONArray arraychild = objjson.getJSONArray("children");
							for(int y=0;y<arraychild.length();y++){
								checkjson = arraychild.getJSONObject(y);
							}
						}
						curChildMap.put("childtext", childtext);
						curChildMap.put("ur", ur);
						children.add(curChildMap);
					}
					childData.add(children);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Iterator iteratorjson = jsonObjma.keys();
			int p = 0;
			while (iteratorjson.hasNext()) {
				String jsonwhse = (String) iteratorjson.next();
				Log.d("***",jsonwhse);
				JSONObject jsonObjQHW = jsonObjma.getJSONObject(jsonwhse);
				Log.d("***",jsonObjQHW.toString());
				JSONArray jsonAryQHW = jsonObjQHW.getJSONArray("children");
				for (int i = 0; i < jsonAryQHW.length(); i++) {
					JSONObject jsonObject = jsonAryQHW.getJSONObject(i);
					String co = jsonObject.getString("key");
					JSONArray jsonAry = jsonObject.getJSONArray("children");
					for (int j = 0; j < jsonAry.length(); j++) {
						JSONObject jsonObjAry = jsonAry.getJSONObject(j);
						String div = jsonObjAry.getString("key");
						sDatabase = mySQLiteOpenHelper.getReadableDatabase();
						sDatabase.execSQL("insert into t_d values('"+j+"','"+div+"','"+i+"')");
					}
					sDatabase.execSQL("insert into t_c values('"+i+"','"+co+"','"+p+"')");
				}
				sDatabase.execSQL("insert into t_w values('"+p+"','"+jsonwhse+"')");
				p++;
			}
			sDatabase.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		adapter=new ExAdapter(PreAppActivity.this);
		exList = (ExpandableListView) findViewById(R.id.treelist);
		exList.setAdapter(adapter);
		exList.setGroupIndicator(null);
		exList.setDivider(null);

		SharedPreferences sharedPreferences = this.getSharedPreferences(AppInfo, 0);
		whse = sharedPreferences.getString("whse", "");
		co = sharedPreferences.getString("co", "");
		div = sharedPreferences.getString("div", "");
	}
	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.imageclose:
					AlertDialog loginout = new AlertDialog.Builder(PreAppActivity.this)
							.setMessage("你确定退出！")
							.setPositiveButton("确定",new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,int which) {
									sDatabase = mySQLiteOpenHelper.getReadableDatabase();
									sDatabase.execSQL("delete from t_w");
									sDatabase.execSQL("delete from t_c");
									sDatabase.execSQL("delete from t_d");
									sDatabase.close();
									File file = new File(DATA_URL+getPackageName().toString()+ "/shared_prefs",AppInfo);
									file.delete();
									SharedPreferences sharedPreferences = getSharedPreferences(AppInfo, 0);
									Editor spedit = sharedPreferences.edit();
									spedit.remove("whse");
									spedit.remove("co");
									spedit.remove("div");
									spedit.remove("whsep");
									spedit.remove("cop");
									spedit.remove("divp");
									spedit.commit();
									((Activity)PreAppActivity.this).finish();
								}
							}).setNegativeButton("取消", null).show();

					Log.i("关闭", "关闭MenuActivity");
					break;
				default:
					break;
			}
		}
	};

	//按键退出
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			AlertDialog dialog = new AlertDialog.Builder(PreAppActivity.this)
					.setMessage("你确定退出！")
					.setPositiveButton("确定",new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,int which) {
							sDatabase = mySQLiteOpenHelper.getReadableDatabase();
							sDatabase.execSQL("delete from t_w");
							sDatabase.execSQL("delete from t_c");
							sDatabase.execSQL("delete from t_d");
							sDatabase.close();
							File file = new File(DATA_URL+getPackageName().toString()+ "/shared_prefs",AppInfo);
							file.delete();
							SharedPreferences sharedPreferences = getSharedPreferences(AppInfo, 0);
							Editor spedit = sharedPreferences.edit();
							spedit.remove("whse");
							spedit.remove("co");
							spedit.remove("div");
							spedit.remove("whsep");
							spedit.remove("cop");
							spedit.remove("divp");
							spedit.commit();
							((Activity)PreAppActivity.this).finish();
						}
					}).setNegativeButton("取消", null).show();

		}
		return false;
	}
	public void wspinner(){

		List<MyListItem> wlist = new ArrayList<MyListItem>();
		String whse[];
		sDatabase = mySQLiteOpenHelper.getReadableDatabase();
		String wsql = "select distinct(w) from t_w group by wid";
		Cursor wcursor = sDatabase.rawQuery(wsql, null);
		wcursor.moveToFirst();
		int wcount = wcursor.getCount();
		whse = new String[wcount];
		int a = 0;
		do{
			try{
				whse[a] = wcursor.getString(wcursor.getColumnIndex("w"));
				MyListItem mylistItem = new MyListItem();
				mylistItem.setWhse(whse[a]);
				wlist.add(mylistItem);
				a++;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}while(wcursor.moveToNext());
		sDatabase.close();
		MyAdapter wadapter = new MyAdapter(PreAppActivity.this, wlist);
		w.setAdapter(wadapter);
		w.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				String whse = ((MyListItem)parent.getItemAtPosition(position)).getWhse();
				whseposition = position;
				cspinner(String.valueOf(whseposition));
				dspinner(String.valueOf(whseposition));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
	}
	public void cspinner(String wid){
		ArrayList<MyListItem> clist = new ArrayList<MyListItem>();
		String co[];
		int t=0;
		sDatabase = mySQLiteOpenHelper.getReadableDatabase();
		String csql = "select distinct(c) from t_c where wid=?";
		Cursor ccursor = sDatabase.rawQuery(csql, new String[]{wid});
		ccursor.moveToFirst();
		int ccount = ccursor.getCount();
		co = new String[ccount];
		do{
			try{
				co[t] = ccursor.getString(ccursor.getColumnIndex("c"));
				MyListItem myListItem = new MyListItem();
				myListItem.setCo(co[t]);
				clist.add(myListItem);
				t++;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}while(ccursor.moveToNext());
		sDatabase.close();
		MyAdapter cadapter = new MyAdapter(PreAppActivity.this, clist);
		c.setAdapter(cadapter);
		c.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				String co = ((MyListItem)parent.getItemAtPosition(position)).getCo();
				coposition = position;
				dspinner(String.valueOf(coposition));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
	}
	public void dspinner(String cid){
		ArrayList<MyListItem> dlist = new ArrayList<MyListItem>();
		String div[];
		int y=0;
		sDatabase = mySQLiteOpenHelper.getReadableDatabase();
		String dsql = "select distinct(d) from t_d where cid=?";
		Cursor dcursor = sDatabase.rawQuery(dsql, new String[]{cid});
		dcursor.moveToFirst();
		int dcount = dcursor.getCount();
		div = new String[dcount];
		do{
			try{
				div[y] = dcursor.getString(dcursor.getColumnIndex("d"));
				MyListItem myListItem = new MyListItem();
				myListItem.setDiv(div[y]);
				dlist.add(myListItem);
				y++;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}while(dcursor.moveToNext());
		MyAdapter dadapter = new MyAdapter(PreAppActivity.this, dlist);
		d.setAdapter(dadapter);
		d.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				String div = ((MyListItem)parent.getItemAtPosition(position)).getDiv();
				if(position>=0){
					divposition = position;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		sDatabase.close();
	}

	public void saveApp(){
		SharedPreferences sharedPreferences = this.getSharedPreferences(AppInfo, 0);
		Editor spedit = sharedPreferences.edit();
		spedit.putInt("whsep", whseposition);
		spedit.putInt("cop", coposition);
		spedit.putInt("divp", divposition);
		spedit.putString("whse", whse);
		spedit.putString("co", co);
		spedit.putString("div", div);
		spedit.commit();
	}
	public void LoadApp() {
		int whsep,cop,divp;
		sDatabase = mySQLiteOpenHelper.getReadableDatabase();
		String Lwsql = "select wid from t_w where w=?";
		String Lcsql = "select cid from t_c where c=?";
		String Ldsql = "select did from t_d where d=?";
		Cursor Lwcursor = sDatabase.rawQuery(Lwsql, new String[]{jsonwhse});
		Cursor Lccursor = sDatabase.rawQuery(Lcsql, new String[]{jsonco});
		Cursor Ldcursor = sDatabase.rawQuery(Ldsql, new String[]{jsondiv});
		Lwcursor.moveToFirst();
		Lccursor.moveToFirst();
		Ldcursor.moveToFirst();
		String swhsep = Lwcursor.getString(Lwcursor.getColumnIndex("wid"));
		String scop = Lccursor.getString(Lccursor.getColumnIndex("cid"));
		String sdivp = Ldcursor.getString(Ldcursor.getColumnIndex("did"));
		whsep = Integer.parseInt(swhsep);
		cop = Integer.parseInt(scop);
		divp = Integer.parseInt(sdivp);
		w.setSelection(whsep, true);
		c.setSelection(cop, true);
		d.setSelection(divp, true);

	}
	public void LoadApp2() {
		SharedPreferences sharedPreferences = this.getSharedPreferences(AppInfo, 0);
		String whse = sharedPreferences.getString("whse", "");
		String co = sharedPreferences.getString("co", "");
		String div = sharedPreferences.getString("div", "");
		int whsep = sharedPreferences.getInt("whsep", 0);
		int cop = sharedPreferences.getInt("cop", 0);
		int divp = sharedPreferences.getInt("divp", 0);
		if(whse!=null && co!=null && div!=null){
			w.setSelection(whsep, true);
			c.setSelection(cop, true);
			d.setSelection(divp, true);
		}else{

		}
	}

	class ExAdapter extends BaseExpandableListAdapter {
		PreAppActivity exlistview;

		public ExAdapter(PreAppActivity elv) {
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
		//**************************************
		public View getChildView(int groupPosition, int childPosition,
								 boolean isLastChild, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.treebuttonlist, null);
			}
			final Button title = (Button) view.findViewById(R.id.newtask);
			final String ur = childData.get(groupPosition).get(childPosition).get("ur").toString();
			title.setText(childData.get(groupPosition).get(childPosition).get("childtext").toString());
			title.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(ur.equals("order_picking.app")){
						Intent intent = new Intent(PreAppActivity.this,MenuActivity.class);
						Bundle bundle = new Bundle();
						if(whse.equals("") && co.equals("") && div.equals("")){
							bundle.putString("whse", jsonwhse);
							bundle.putString("co", jsonco);
							bundle.putString("div", jsondiv);
						}else{
							bundle.putString("whse", whse);
							bundle.putString("co", co);
							bundle.putString("div", div);
						}
						bundle.putString("jsony", jsony.toString());
						bundle.putString("uId", uId);
						bundle.putString("ip", ip);
						intent.putExtras(bundle);
						startActivity(intent);
					}
					if(ur.equals("Personal.app")){
						LayoutInflater inflater = LayoutInflater.from(PreAppActivity.this);
						view1 = inflater.inflate(R.layout.wcd, null);
						w = (Spinner) view1.findViewById(R.id.w);
						c = (Spinner) view1.findViewById(R.id.c);
						d = (Spinner) view1.findViewById(R.id.d);

						wspinner();

						if(whse.equals("") && co.equals("") && div.equals("")){
							LoadApp();
						}else{
							LoadApp2();
						}
						AlertDialog dialog = new AlertDialog.Builder(PreAppActivity.this)
								.setTitle("个人配置")
								.setView(view1)
								.setPositiveButton("确定",new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,int which) {

										MyListItem Itemwhse = (MyListItem) w.getSelectedItem();
										MyListItem Itemco = (MyListItem) c.getSelectedItem();
										MyListItem Itemdiv = (MyListItem) d.getSelectedItem();
										whse = Itemwhse.getWhse();
										co = Itemco.getCo();
										div = Itemdiv.getDiv();
										saveApp();
										try {
											Map<String, String> map = new HashMap<String, String>();
											map.put("uId", uId);
											map.put("whse", whse);
											map.put("co", co);
											map.put("div", div);
											HttpUtil.postRequest(HttpUtil.BASE_URL(ip) + "savewcd.jsp", map);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}).setNegativeButton("取消", null).show();
					}
					if(ur.equals("use.app")){
						Intent useIntent = new Intent(PreAppActivity.this,UserMaterialActivity.class);
						Bundle bundle = new Bundle();
						if(whse.equals("") && co.equals("") && div.equals("")){
							bundle.putString("whse", jsonwhse);
							bundle.putString("co", jsonco);
							bundle.putString("div", jsondiv);
						}else{
							bundle.putString("whse", whse);
							bundle.putString("co", co);
							bundle.putString("div", div);
						}
						bundle.putString("uId", uId);
						bundle.putString("ip", ip);
						useIntent.putExtras(bundle);
						startActivity(useIntent);
					}
					if(ur.equals("checkstatistics.app")){
						Intent checkIntent = new Intent(PreAppActivity.this,StockCheckActivity.class);
						Bundle bundle = new Bundle();
						if(whse.equals("") && co.equals("") && div.equals("")){
							bundle.putString("whse", jsonwhse);
							bundle.putString("co", jsonco);
							bundle.putString("div", jsondiv);
						}else{
							bundle.putString("whse", whse);
							bundle.putString("co", co);
							bundle.putString("div", div);
						}
						bundle.putString("checkjson", checkjson.toString());
						bundle.putString("uId", uId);
						bundle.putString("ip", ip);
						checkIntent.putExtras(bundle);
						startActivity(checkIntent);
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
		//**************************************
		public boolean hasStableIds() {
			return true;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}
}
