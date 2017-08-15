package com.example.dragon.sc.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Dragon on 2017/8/9.
 */


public class MyAdapter extends BaseAdapter {

    private Context context;
    private List<MyListItem> myList;

    public MyAdapter(Context context, List<MyListItem> myList) {
        this.context = context;
        this.myList = myList;
    }


    public int getCount() {
        return myList.size();
    }
    public Object getItem(int position) {
        return myList.get(position);
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        MyListItem myListItem = myList.get(position);
        return new MyAdapterView(this.context, myListItem );
    }

    class MyAdapterView extends LinearLayout {
        public static final String LOG_TAG = "MyAdapterView";

        public MyAdapterView(Context context, MyListItem myListItem ) {
            super(context);
            this.setOrientation(VERTICAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, LayoutParams.WRAP_CONTENT);
            TextView name = new TextView(context);
            name.setText(myListItem.getWhse());
            addView(name,params);

            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(50, LayoutParams.WRAP_CONTENT);
            TextView pcode = new TextView(context);
            pcode.setText(myListItem.getCo());
            addView(pcode,params2);


            LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(50, LayoutParams.WRAP_CONTENT);
            TextView div = new TextView(context);
            div.setText(myListItem.getDiv());
            addView(div,params3);
        }

    }


}