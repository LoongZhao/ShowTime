package com.example.dragon.sc.util;

/**
 * Created by Dragon on 2017/8/9.
 */



import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dragon.sc.R;


public class CartonInformationJSONAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private Context ctx;
    // 定义需要包装的JSONArray对象
    private JSONArray jsonArray;
    // 定义列表项显示JSONObject对象的哪个属性
    private String cartonpkt,cartonsku,cartonlabel,cartonno,cartondate,cartonpackqty;
    private boolean hasIcon;
    public static Map<Integer,Boolean> isSelected;

    public CartonInformationJSONAdapter(Context ctx , JSONArray jsonArray,String cartonpkt, String cartonsku,String cartonlabel,String cartonno,String packqty,String cartondate, boolean hasIcon)
    {
        mInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        this.jsonArray = jsonArray;
        this.cartonpkt = cartonpkt;
        this.cartonsku = cartonsku;
        this.cartonlabel = cartonlabel;
        this.cartonno = cartonno;
        this.cartondate = cartondate;
        this.cartonpackqty = packqty;
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
        //convertView为null的时候初始化convertView。
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.cartoninformationlistview, null);
//            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.cartonpkt = (TextView) convertView.findViewById(R.id.cartonpkt);
            holder.cartonsku = (TextView) convertView.findViewById(R.id.cartonsku);
            holder.cartonlabel = (TextView) convertView.findViewById(R.id.cartonlabel);
            holder.cartonno = (TextView) convertView.findViewById(R.id.cartonno);
            holder.cartondate = (TextView) convertView.findViewById(R.id.cartondate);
            holder.cartonpackqty = (TextView) convertView.findViewById(R.id.cartonpackqty);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            String cartonpkt1 = ((JSONObject)getItem(position)).getString(cartonpkt);
            String cartonsku1 = ((JSONObject)getItem(position)).getString(cartonsku);
            String cartonlabel1 = ((JSONObject)getItem(position)).getString(cartonlabel);
            String cartonno1 = ((JSONObject)getItem(position)).getString(cartonno);
            String cartondate1 = ((JSONObject)getItem(position)).getString(cartondate);
            String cartonpackqty1 = ((JSONObject)getItem(position)).getString(cartonpackqty);
            holder.cartonpkt.setText(cartonpkt1);
            holder.cartonsku.setText(cartonsku1);
            holder.cartonlabel.setText(cartonlabel1);
            holder.cartonno.setText(cartonno1);
            holder.cartondate.setText(cartondate1);
            holder.cartonpackqty.setText(cartonpackqty1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public final class ViewHolder {
        public TextView cartonpkt;
        public TextView cartonsku;
        public TextView cartonlabel;
        public TextView cartonno;
        public TextView cartondate;
        public TextView cartonpackqty;

    }

    public void init(){
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < jsonArray.length(); i++) {
            isSelected.put(i, false);
        }
    }

}

