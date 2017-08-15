package com.example.dragon.sc.activity;

/**
 * Created by Dragon on 2017/7/18.
 */


import java.util.Date;

import com.example.dragon.sc.R;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.widget.ImageView;
import android.widget.TextView;

public class LoadingScreen1Activity extends Activity {
    /** Called when the activity is first created. */
    private int count = 5;
    private int[] imgIDs = {R.id.widget0,R.id.widget1,R.id.widget2,R.id.widget3,R.id.widget4};
    private long time = 4*1000;
    boolean isStop = false;

    private static final int TYPE_SELECTED = 1;
    private static final int TYPE_NO_SELECTED = 2;
    private static final int TYPE_STOP = 3;
    private TextView version;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        version = (TextView) findViewById(R.id.version);
        PackageManager pm = getPackageManager();
        PackageInfo pinfo;
        try {
            pinfo = pm.getPackageInfo(getPackageName(), 0);
            String currentversion = pinfo.versionName;
            version.setText("Phoenix System Pad "+currentversion);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        for(int id : imgIDs)
            ((ImageView)findViewById(id)).setBackgroundResource(R.drawable.progress_bg_small);

        final IndexThread showThread = new IndexThread();
        final InitialThread workingThread = new InitialThread();
        showThread.start();
        workingThread.start();
    }

    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what)
            {
                case TYPE_STOP:
                    Intent intent = new Intent(LoadingScreen1Activity.this, LoginActivity.class);
                    startActivity(intent);
                    isStop = true;
                    finish();
                    break;
                case TYPE_SELECTED:
                    ((ImageView)findViewById(msg.arg1)).setBackgroundResource(R.drawable.progress_go_small);
                    break;
                case TYPE_NO_SELECTED:
                    ((ImageView)findViewById(msg.arg1)).setBackgroundResource(R.drawable.progress_bg_small);
                    break;
            }
        }
    };

    class InitialThread extends Thread{


        @Override
        public void run()
        {

            try {
                Thread.sleep(loadtime()+3000);//替换为初始化代码....

                Message msg;
                msg = new Message();
                msg.what = TYPE_STOP;
                myHandler.sendMessage(msg);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Message msg;
                msg = new Message();
                msg.what = TYPE_STOP;
                myHandler.sendMessage(msg);
            }


        }
    }

    class IndexThread extends Thread
    {

        @Override
        public void run()
        {
            Message msg;
            while(!isStop)
            {
                for(int i= 0 ; i < count ; i++)
                {
                    msg = new Message();
                    msg.what = TYPE_SELECTED;
                    msg.arg1 = imgIDs[i];
                    myHandler.sendMessage(msg);
                    msg = new Message();
                    if(i==0)
                    {
                        msg.what = TYPE_NO_SELECTED;
                        msg.arg1 = imgIDs[count-1];
                        myHandler.sendMessage(msg);
                    }
                    else
                    {
                        msg.what = TYPE_NO_SELECTED;
                        msg.arg1 = imgIDs[i-1];
                        myHandler.sendMessage(msg);
                    }
                    SystemClock.sleep(500);
                }
            }
        }
    }



    //结束加载
    public Long loadtime()
    {
        long between=0;
        long begin = new Date().getSeconds();
        return between;
    }


}
