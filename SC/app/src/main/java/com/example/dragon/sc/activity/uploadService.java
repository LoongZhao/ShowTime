package com.example.dragon.sc.activity;

/**
 * Created by Dragon on 2017/8/9.
 */



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.dragon.sc.util.HttpUtil;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class uploadService extends Service {

    private String ip;
    private List list;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Bundle bundle =intent.getExtras();
        ip = bundle.getString("ip");
        list = bundle.getStringArrayList("serlist");
        for(int i=0;i<list.size();i++){

            Map<String,String> map1 = (Map<String, String>) list.get(i);
            String pktId = map1.get("pktId");
            String flg = map1.get("flg");
            Map<String,String> map = new HashMap<String,String>();
            map.put("pckId", pktId);
            map.put("flg", flg);
            try {
                HttpUtil.postRequest(HttpUtil.BASE_URL(ip) + "setpkt.jsp", map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}
