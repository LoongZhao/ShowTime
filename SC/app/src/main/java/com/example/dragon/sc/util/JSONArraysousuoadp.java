package com.example.dragon.sc.util;

/**
 * Created by Dragon on 2017/8/9.
 */



import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dragon.sc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class JSONArraysousuoadp extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context ctx;
    // 定义需要包装的JSONArray对象
    private JSONArray jsonArray;
    // 定义列表项显示JSONObject对象的哪个属性
    private String ssku,sloc,sskudesc,sunitqty,barcode;
    private boolean hasIcon;
    public static Map<Integer,Boolean> isSelected;


    public JSONArraysousuoadp(Context ctx , JSONArray jsonArray,String sku, String skudesc,String loc,String unitqty,String barcode, boolean hasIcon)
    {
        mInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        this.jsonArray = jsonArray;
        this.ssku = sku;
        this.sloc = loc;
        this.sskudesc=skudesc;
        this.sunitqty=unitqty;
        this.barcode = barcode;
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
        try
        {
            // 返回物品的ID
            return ((JSONObject)getItem(position)).getInt("pckId");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        //convertView为null的时候初始化convertView。
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.sousuolistview, null);
            holder.barcode =  (TextView) convertView.findViewById(R.id.Tbarcode);
            holder.sku = (TextView) convertView.findViewById(R.id.Tsku);
            holder.loc = (TextView) convertView.findViewById(R.id.Tloc);
            holder.skuDesc = (TextView) convertView.findViewById(R.id.Tskudesc);
            holder.unitqty = (TextView) convertView.findViewById(R.id.Tunitqty);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            String barcode1 = ((JSONObject)getItem(position)).getString(barcode);
            String sku1 = ((JSONObject)getItem(position)).getString(ssku);
            String loc1 = ((JSONObject)getItem(position)).getString(sloc);
            String unitqty1 = ((JSONObject)getItem(position)).getString(sunitqty);
            String skudesc1 = ((JSONObject)getItem(position)).getString(sskudesc);
            holder.barcode.setText(barcode1);
            holder.sku.setText(sku1);
            holder.loc.setText(loc1);
            holder.skuDesc.setText(skudesc1);
            holder.unitqty.setText(unitqty1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public final class ViewHolder {
        public TextView sku;
        public TextView barcode;
        public TextView skuDesc;
        public TextView loc;
        public TextView unitqty;
        public TextView scannedqty;

    }

    public void init(){
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < jsonArray.length(); i++) {
            isSelected.put(i, false);
        }
    }

}

