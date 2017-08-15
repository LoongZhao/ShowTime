package com.example.dragon.sc.util;

/**
 * Created by Dragon on 2017/8/9.
 */



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.dragon.sc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class JSONAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    public static Map<Integer,Boolean> isSelected;
    private ArrayList<Map<String,Object>> datalist;
    private Context context;
    ViewHolder holder = null;

    public JSONAdapter(Context context,ArrayList<Map<String,Object>> datalist){
        super();
        this.datalist = datalist;
        mInflater = LayoutInflater.from(context);
        this.context = context;
        init();
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        //convertView为null的时候初始化convertView。
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listview,parent,false);
            holder.id = (TextView) convertView.findViewById(R.id.Tid);
            holder.sku = (TextView) convertView.findViewById(R.id.Tsku);
            holder.loc = (TextView) convertView.findViewById(R.id.Tloc);
            holder.num = (TextView) convertView.findViewById(R.id.Tnum);
            holder.skuDesc = (TextView) convertView.findViewById(R.id.TskuDesc);
            holder.zhouzhuan = (TextView) convertView.findViewById(R.id.Tzz);
            holder.cBox = (CheckBox) convertView.findViewById(R.id.cb);
            holder.pktId = (TextView) convertView.findViewById(R.id.TpktId);
            holder.pktCtrlNbr = (TextView) convertView.findViewById(R.id.TpktCtrlNbr);
            holder.posalzone = (TextView) convertView.findViewById(R.id.zone);
            holder.ordseq = (TextView) convertView.findViewById(R.id.ord_seq);
            holder.refnum1 = (TextView) convertView.findViewById(R.id.ref_num1);
            holder.tonby = (TextView) convertView.findViewById(R.id.to_nby);
            holder.mergeNbr = (TextView) convertView.findViewById(R.id.mNbr);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            String pickFlag = datalist.get(position).get("pickFlag").toString();
            String ornbr = datalist.get(position).get("pktCtrlNbr").toString();
            String pid = datalist.get(position).get("pktId").toString();
            holder.id.setText(datalist.get(position).get("id").toString());
            holder.ordseq.setText(datalist.get(position).get("ord_seq").toString());
            holder.refnum1.setText(datalist.get(position).get("ref_num1").toString());
            holder.tonby.setText(datalist.get(position).get("to_nby").toString());
            holder.sku.setText(datalist.get(position).get("sku").toString());
            holder.loc.setText(datalist.get(position).get("loc").toString().substring(3));
            holder.num.setText(datalist.get(position).get("num").toString());
            holder.skuDesc.setText(datalist.get(position).get("skuDesc").toString());
            holder.zhouzhuan.setText(datalist.get(position).get("zhouzhuan").toString());
            holder.pktId.setText(pid);
            holder.posalzone.setText(datalist.get(position).get("posalzone").toString());
            holder.pktCtrlNbr.setText(ornbr);
            holder.mergeNbr.setText(datalist.get(position).get("mergeNbr").toString());
            holder.cBox.setChecked(isSelected.get(position));
            if(pickFlag.equals("P")){
                holder.cBox.setChecked(true);
                JSONAdapter.isSelected.put(position, holder.cBox.isChecked());
            }else{
                holder.cBox.setChecked(isSelected.get(position));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
    public void init(){
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < datalist.size(); i++) {
            isSelected.put(i, false);
        }
    }
}
