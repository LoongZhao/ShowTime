package com.example.dragon.sc.util;

/**
 * Created by Dragon on 2017/8/9.
 */




import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import com.example.dragon.sc.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 */
public class JSONArrayAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private Context ctx;
    // 定义需要包装的JSONArray对象
    private JSONArray jsonArray;
    // 定义列表项显示JSONObject对象的哪个属性
    private String pktCtrlNbr,cartonCode,mergeSeq,total;
    private boolean hasIcon;
    public static Map<Integer,Boolean> isSelected;
    private ArrayList<Map<String,Object>> datalist;

    public JSONArrayAdapter(Context ctx , ArrayList<Map<String,Object>> datalist)
    {
        mInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        this.datalist = datalist;
        init();
    }

    @Override
    public int getCount()
    {
        return datalist.size();
    }

    @Override
    public Object getItem(int position)
    {
        return datalist.get(position);
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
            convertView = mInflater.inflate(R.layout.huoxianglist, null);
            holder.cartonCode = (TextView) convertView.findViewById(R.id.cartonCode);
            holder.pktCtrlNbr = (TextView) convertView.findViewById(R.id.pktCtrlNbr);
            holder.total = (TextView) convertView.findViewById(R.id.total);
            holder.mergeSeq = (TextView) convertView.findViewById(R.id.mergeSeq);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            holder.mergeSeq.setText(datalist.get(position).get("mergeSeq").toString());
            holder.total.setText(datalist.get(position).get("total").toString());
            holder.cartonCode.setText(datalist.get(position).get("cartonCode").toString());
            holder.pktCtrlNbr.setText(datalist.get(position).get("pktCtrlNbr").toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public final class ViewHolder {
        public TextView pktCtrlNbr;
        public TextView total;
        public TextView mergeSeq;
        public TextView cartonCode;

    }

    public void init(){
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i <datalist.size(); i++) {
            isSelected.put(i, false);
        }
    }
}
