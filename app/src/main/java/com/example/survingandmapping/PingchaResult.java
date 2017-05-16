package com.example.survingandmapping;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import static com.example.survingandmapping.PingchaUtil.resultList;

public class PingchaResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pingcha_result);

        PingchaUtil.debugFuc();
        PingchaUtil.calPingcha();

        ZuobiaoAdapter adapter=new ZuobiaoAdapter(PingchaResult.this,R.layout.result_item, resultList);
        ListView listView=(ListView)findViewById(R.id.result_list);
        listView.setAdapter(adapter);
    }


    class ZuobiaoAdapter extends ArrayAdapter<PingchaUtil.ZuoBiao>{

        private int resourceId;

        public ZuobiaoAdapter(Context context, int viewResourceId, List<PingchaUtil.ZuoBiao> objects){
            super(context,viewResourceId,objects);
            resourceId=viewResourceId;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            PingchaUtil.ZuoBiao zb=getItem(position);
            View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            TextView xZuobiao=(TextView)view.findViewById(R.id.xzuoB);
            TextView yZuobiao=(TextView)view.findViewById(R.id.yzuoB);
            xZuobiao.setText("X: "+zb.getX());
            yZuobiao.setText("Y: "+zb.getY());
            return view;
        }
    }
}
