package com.example.survingandmapping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.survingandmapping.db.DaoXianCeLiang;

import java.util.ArrayList;
import java.util.List;

public class DaoXianBegin extends AppCompatActivity {
    private EditText du;
    private EditText fen;
    private EditText miao;
    private EditText dis;
    private EditText cezhan;
    private Button add;
    private Button pcBegin;
    private TextView disT;
    private StationAdapter adapter;

    private List<Station> stationList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dao_xian_begin);
        cezhan=(EditText)findViewById(R.id.cezhan);
        du=(EditText)findViewById(R.id.du);
        fen=(EditText)findViewById(R.id.fen);
        miao=(EditText)findViewById(R.id.miao);
        dis=(EditText)findViewById(R.id.dis);
        pcBegin=(Button)findViewById(R.id.pcBegin);
        add=(Button)findViewById(R.id.addToList);
        disT=(TextView)findViewById(R.id.disT);

        //因为第一站不用观测距离，将dis,disT两个控件隐藏
        //这里将dis的内容设为0，是为了避免改编辑框为空，会造成输入不完整的提示
        dis.setText("0");
        dis.setVisibility(View.INVISIBLE);
        disT.setVisibility(View.INVISIBLE);

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new StationAdapter(stationList);
        recyclerView.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addToList()==true){
                    dis.setVisibility(View.VISIBLE);
                    disT.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(DaoXianBegin.this,"该站数据已加如以下列表,下拉以查看",Toast.LENGTH_SHORT).show();
                }
            }
        });
        pcBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DaoXianBegin.this,PingchaResult.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        PingchaUtil.guancejiaoList.clear();
        PingchaUtil.disList.clear();
        stationList.clear();
        this.finish();
    }

    private boolean addToList(){
        if(cezhan.getText().toString().equals("")
                ||du.getText().toString().equals("")
                ||fen.getText().toString().equals("")
                ||miao.getText().toString().equals("")
                ||dis.getText().toString().equals("")){
            Toast.makeText(DaoXianBegin.this,"输入不完整，请重新输入",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(check(du.getText().toString())==false
                ||check(fen.getText().toString())==false
                ||check(miao.getText().toString())==false
                ||check(dis.getText().toString())==false){
            Toast.makeText(DaoXianBegin.this,"角度及距离观测值含非数，请重新输入",Toast.LENGTH_SHORT).show();
            return false;
        }
        //因为第一站不观测距离，所以第一站不用往disList中加观测值
        if(PingchaUtil.guancejiaoList.size()!=0){
            PingchaUtil.disList.add(Double.parseDouble(dis.getText().toString()));
        }
        //将各观测角放入PingchaUtil工具类中，便于计算平差
        PingchaUtil.guancejiaoList.add(new PingchaUtil.Jiao(Integer.parseInt(du.getText().toString())
                ,Integer.parseInt(fen.getText().toString()),Integer.parseInt(miao.getText().toString())));

        Station.GuanCeJ guanCeJ=new Station.GuanCeJ(Integer.parseInt(du.getText().toString())
                ,Integer.parseInt(fen.getText().toString()),Integer.parseInt(miao.getText().toString()));
        Station station=new Station(cezhan.getText().toString(),
                Double.parseDouble(dis.getText().toString()),guanCeJ);
        stationList.add(station);

        DaoXianCeLiang daoXianCeLiang=new DaoXianCeLiang();
        String cezh=cezhan.getText().toString();
        float guancejiao=Float.parseFloat(du.getText().toString())+Float.parseFloat(fen.getText().toString())/60
                +Float.parseFloat(miao.getText().toString())/3600;
        float guancejuli=Float.parseFloat(dis.getText().toString());
        daoXianCeLiang.setCezhan(cezh);
        daoXianCeLiang.setGuancejiao(guancejiao);
        daoXianCeLiang.setGuancejiuli(guancejuli);
        daoXianCeLiang.save();
        Toast.makeText(DaoXianBegin.this,"该站数据已保存如数据库",Toast.LENGTH_SHORT).show();
        Log.d("sqpolar","该站数据已保存如数据库");

        cezhan.setText("");
        du.setText("");
        fen.setText("");
        miao.setText("");
        dis.setText("");
        return true;
    }
    boolean check(String str){
        try {
            Double.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            Toast.makeText(DaoXianBegin.this,"输入含有非数字",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
