package com.example.dragon.sc.util;

/**
 * Created by Dragon on 2017/8/9.
 */




import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.View;
import android.app.Activity;
/**
 * Description:
 * <br/><a href="http://www.saichenglogistics.com">赛诚国际物流有限公司</a>
 * <br/>© 2012 Sai Cheng Logistics International Co.,Ltd
 * <br/>Program Name:scliappClient
 * <br/>Date:2012-12-01
 * @version  1.0
 */
public class DialogUtil{
    // 定义一个显示消息的对话框
    public static void showDialog(final Context ctx
            , String msg , boolean closeSelf)
    {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
                .setMessage(msg).setCancelable(false);
        if(closeSelf)
        {
            builder.setPositiveButton("确定", new OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    // 结束当前Activity
                    ((Activity)ctx).finish();
                }
            }).setNegativeButton("取消", null);
        }
        else
        {
            builder.setPositiveButton("确定", null);
        }
        builder.create().show();
    }
    // 定义一个显示指定组件的对话框
    public static boolean showDialog(Context ctx , View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
                .setView(view).setCancelable(false)
                .setPositiveButton("确定", null);
        builder.create()
                .show();
        return false;
    }
}

