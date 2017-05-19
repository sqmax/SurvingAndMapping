package com.example.survingandmapping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.survingandmapping.service.AutoUpdateService;
import com.example.survingandmapping.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView bingPicImg;
    private Button shui;
    private Button dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
         }
        initView();
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        bingPicImg=(ImageView)findViewById(R.id.bing_pic_img);
        shui=(Button)findViewById(R.id.shui);
        dao=(Button)findViewById(R.id.dao);
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String bingPic=prefs.getString("bing_pic",null);
        if(bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else{
            loadBingPic();
        }
        Intent intent=new Intent(this, AutoUpdateService.class);
        startService(intent);
        shui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ShuizhunActivity.class);
                startActivity(intent);
            }
        });
        dao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,DaoXianActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mHandler.sendEmptyMessage(0);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mHandler.sendEmptyMessage(0);
        }
    };
    private void loadBingPic(){
        String  requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(MainActivity.this).load(R.drawable.bc).into(bingPicImg);
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager
                        .getDefaultSharedPreferences(MainActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                if(bingPic!=null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(MainActivity.this).load(bingPic).into(bingPicImg);
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(MainActivity.this).load(R.drawable.bc).into(bingPicImg);
                        }
                    });
                }

            }
        });
    }
}
