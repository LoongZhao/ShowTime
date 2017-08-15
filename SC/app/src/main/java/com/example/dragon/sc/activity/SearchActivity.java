package com.example.dragon.sc.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dragon.sc.R;
import com.example.dragon.sc.util.HttpUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends Activity {

    private EditText editMsg;
    private Button btStock,btReplenishment;
    private String sku;
//    private TextView wrongTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        editMsg = (EditText)findViewById(R.id.msgEditText);
        btStock = (Button) findViewById(R.id.btStock);
        btReplenishment = (Button) findViewById(R.id.btReplenishment);
        btReplenishment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(SearchActivity.this)
                        .setTitle("请选择")
                        .setSingleChoiceItems(new String[]{"需要补货信息", "补货操作日志"}, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("++++++++++++++++++",dialog.toString());
                                Log.d("++++++++++++++++++",String.valueOf(which));
                                sku=editMsg.getText().toString().trim();
                                if (which == 0){

                                }
                                if (which == 1) {

                                }
                                Intent intent = new Intent(SearchActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });
        btStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sku=editMsg.getText().toString().trim();

            }
        });
    }
    private static JSONObject query(String username, String password) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("user", username);
        map.put("pass", password);
//        System.out.println(username="MMMMMMMMMMMMMMMMMMMMMMM"+password);
        String url = HttpUtil.BASE_URL("") + "user/login.do";
        Log.d("_________", username+password+url);
        return new JSONObject(HttpUtil.postRequest(url, map));

//        return new JSONObject();
    }
}
