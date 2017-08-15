package com.example.dragon.sc.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.dragon.sc.R;
import java.util.List;
import java.util.Map;

/**
 * Created by Dragon on 2017/8/9.
 */

public class JSONSpinnerAdapter extends BaseAdapter {

    private LayoutInflater flater = null;
    private List<Map<String,Object>> list;
    private Context context;

    public JSONSpinnerAdapter(){

    }

    public JSONSpinnerAdapter(Context context,List<Map<String,Object>> list){
        flater = LayoutInflater.from(context);
        this.context=context;
        this.list=list;
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
        ViewHolder holder = null;
//		if(convertView==null){
        holder = new ViewHolder();
        convertView = flater.inflate(R.layout.addmaterialspinner,parent,false);
        holder.matid = (TextView) convertView.findViewById(R.id.addmaterialspinnerid);
        holder.matdetail = (TextView) convertView.findViewById(R.id.addmaterialspinnertext);

        holder.matid.setText(list.get(position).get("matid").toString());
        holder.matdetail.setText(list.get(position).get("matdetail").toString());

//		}else{
//			holder = (ViewHolder) convertView.getTag();
//		}
        return convertView;
    }
    private class ViewHolder{
        TextView matid;
        TextView matdetail;

    }
}
