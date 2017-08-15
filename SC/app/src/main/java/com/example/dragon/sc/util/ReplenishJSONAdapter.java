package com.example.dragon.sc.util;

/**
 * Created by Dragon on 2017/8/9.
 */


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.dragon.sc.R;
import com.example.dragon.sc.util.CartonInformationJSONAdapter.ViewHolder;

public class ReplenishJSONAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private Context ctx;
    // 定义需要包装的JSONArray对象
    private JSONArray jsonArray;
    // 定义列表项显示JSONObject对象的哪个属性
    private String sku,sku_desc,fromloc,toloc,qty,remark;
    private boolean hasIcon;
    public static Map<Integer,Boolean> isSelected;

    public ReplenishJSONAdapter(Context ctx , JSONArray jsonArray,String sku, String sku_desc,String fromloc,String toloc,String qty,String remark, boolean hasIcon)
    {
        mInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        this.jsonArray = jsonArray;
        this.sku = sku;
        this.sku_desc = sku_desc;
        this.fromloc = fromloc;
        this.toloc = toloc;
        this.qty = qty;
        this.remark=remark;
        this.hasIcon = hasIcon;
        init();
    }

    @Override
    public int getCount()
    {
        return jsonArray.length();
    }

    @Override
    public Object getItem(int position)
    {
        return jsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        String replremark = null;
        //convertView为null的时候初始化convertView。
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.replenishlistview, null);
            holder.sku = (TextView) convertView.findViewById(R.id.replenish_sku);
            holder.skudesc = (TextView) convertView.findViewById(R.id.replenish_desc);
            holder.fromloc = (TextView) convertView.findViewById(R.id.replenish_from);
            holder.toloc = (TextView) convertView.findViewById(R.id.replenish_to);
            holder.qty = (TextView) convertView.findViewById(R.id.replenish_Quantity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            String replsku = ((JSONObject)getItem(position)).getString(sku);
            String replskudesc = ((JSONObject)getItem(position)).getString(sku_desc);
            String replfromloc = ((JSONObject)getItem(position)).getString(fromloc);
            String repltoloc = ((JSONObject)getItem(position)).getString(toloc);
            String replqty = ((JSONObject)getItem(position)).getString(qty);
            replremark = ((JSONObject)getItem(position)).getString(remark);
            holder.sku.setText(replsku);
            holder.skudesc.setText(replskudesc);
            holder.fromloc.setText(replfromloc);
            holder.toloc.setText(repltoloc);
            holder.qty.setText(replqty);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(replremark);

        if(replremark.equals("Y")){
            convertView.setBackgroundColor(Color.parseColor("#750000"));
        }
        return convertView;
    }

    public final class ViewHolder {
        public TextView sku;
        public TextView skudesc;
        public TextView fromloc;
        public TextView toloc;
        public TextView qty;

    }

    public void init(){
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < jsonArray.length(); i++) {
            isSelected.put(i, false);
        }
    }

}