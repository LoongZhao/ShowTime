package com.example.dragon.sc.util;

/**
 * Created by Dragon on 2017/8/9.
 */



import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.example.dragon.sc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class JSONLoadAdapter extends BaseAdapter{
    private LayoutInflater flater = null;
    private List<Map<String,Object>> list;
    public static Map<Integer,Boolean> isSelected;
    private Context context;

    public JSONLoadAdapter(){

    }

    public JSONLoadAdapter(Context context,List<Map<String,Object>> list){
        super();
        flater = LayoutInflater.from(context);
        this.context=context;
        this.list=list;
        init();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UseViewHoldder holder = null;
//		if(convertView==null){
        holder = new UseViewHoldder();
        convertView = flater.inflate(R.layout.loadlistview,parent,false);
        holder.matid = (TextView) convertView.findViewById(R.id.matid);
        holder.matcode = (TextView) convertView.findViewById(R.id.matcode);
        holder.matname = (TextView) convertView.findViewById(R.id.matname);
        holder.matstandard = (TextView) convertView.findViewById(R.id.matstandard);
        holder.units = (TextView) convertView.findViewById(R.id.units);
        holder.time = (TextView) convertView.findViewById(R.id.time);
        holder.qty = (TextView) convertView.findViewById(R.id.qty);
        holder.cb = (CheckBox) convertView.findViewById(R.id.matchecked);

        convertView.setTag(holder);

        holder.matid.setText(list.get(position).get("matid").toString());
        holder.matcode.setText(list.get(position).get("matcode").toString());
        holder.matname.setText(list.get(position).get("matname").toString());
        holder.matstandard.setText(list.get(position).get("matstandard").toString());
        holder.units.setText(list.get(position).get("units").toString());
        holder.time.setText(list.get(position).get("time").toString());
        holder.qty.setText(list.get(position).get("qty").toString());
        holder.cb.setChecked(isSelected.get(position));
//		}else{
//			holder = (ViewHolder) convertView.getTag();
//		}
        return convertView;
    }

    public void init(){
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < list.size(); i++) {
            isSelected.put(i, false);
        }
    }

}

