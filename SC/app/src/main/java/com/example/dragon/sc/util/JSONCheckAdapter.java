package com.example.dragon.sc.util;

/**
 * Created by Dragon on 2017/8/9.
 */



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.dragon.sc.R;

import android.accounts.Account;
import android.accounts.OnAccountsUpdateListener;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

public class JSONCheckAdapter extends BaseAdapter{

    private LayoutInflater flater = null;
    private List<Map<String,Object>> list;
    public static Map<Integer,Object> isSelected = null;
    public static Map<Integer,Object> Selecteded = null;
    public static Map<String,Object> SelectedChId = null;
    public static Map<Integer,Boolean> isCheckBox = null;
    public static Map<String,Boolean> isCheckBoxSKU = null;
    private Context context;
    CheckViewHolder holder = null;

    public JSONCheckAdapter(){

    }

    public JSONCheckAdapter(Context context,List<Map<String,Object>> mlist){
        flater = LayoutInflater.from(context);
        this.context=context;
        this.list=mlist;
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
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = new CheckViewHolder();
        convertView = flater.inflate(R.layout.checklistview,parent,false);
        holder.checklistviewid = (CheckBox) convertView.findViewById(R.id.checklistid);
        holder.sku = (TextView) convertView.findViewById(R.id.sku_listview);
        holder.loc = (TextView) convertView.findViewById(R.id.loc_listview);
        holder.sysqty = (TextView) convertView.findViewById(R.id.sysqty_listview);
        holder.xjqty = (TextView) convertView.findViewById(R.id.xjqty_listview);
        holder.checkqty = (EditText) convertView.findViewById(R.id.checkqty_listview);

        convertView.setTag(holder);

        String ChState = list.get(position).get("ch_state").toString();
        holder.sku.setText(list.get(position).get("ch_sku").toString());
        holder.loc.setText(list.get(position).get("ch_loc").toString());
        holder.sysqty.setText(list.get(position).get("ch_sysqty").toString());
        holder.xjqty.setText(list.get(position).get("ch_xjqty").toString());
        holder.checkqty.setText(list.get(position).get("ch_qty").toString());
        if(ChState.equals("true")){
            holder.checklistviewid.setChecked(true);
            JSONCheckAdapter.isCheckBox.put(position, holder.checklistviewid.isChecked());
        }else{
            holder.checklistviewid.setChecked(isCheckBox.get(position));
        }
        holder.checkqty.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isSelected.put(position, s);
                Selecteded.put(Integer.parseInt(list.get(position).get("che_id").toString()), s);
                SelectedChId.put("ch_id", list.get(position).get("ch_id"));
            }
        });
        if(isSelected.get(position)!=null){
            holder.checkqty.setText(isSelected.get(position).toString());
        }
        holder.checklistviewid.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                JSONCheckAdapter.isCheckBox.put(position, isChecked);
                isCheckBoxSKU.put(list.get(position).get("che_id").toString(), isChecked);
            }
        });
        return convertView;
    }

    public void init(){
        isSelected = new HashMap<Integer, Object>();
        Selecteded = new HashMap<Integer, Object>();
        SelectedChId = new HashMap<String, Object>();
        isCheckBoxSKU = new HashMap<String, Boolean>();
        isCheckBox = new HashMap<Integer, Boolean>();
        for (int i = 0; i < list.size(); i++) {
            isCheckBox.put(i, false);
        }
    }



}

