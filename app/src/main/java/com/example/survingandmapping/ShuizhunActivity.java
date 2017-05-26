package com.example.survingandmapping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

//水准测量
public class ShuizhunActivity extends AppCompatActivity {

    public EditText _one;
    public EditText _two;
    public EditText _three;
    public EditText _four;
    public EditText _five;
    public EditText _six;
    public EditText _seven;
    public EditText _eight;
    public EditText _K1;
    public EditText _K2;
    public Button save;
    public EditText station;
    public TextView LastStation;
    public TextView Ljsc;

    int one,two,three,four,five,six,seven,eight,nine,K1,K2
            ,ten,eleven1,eleven2,sixteen,seventeen,eighteen;
    float twelve,thirteen,fourtheen,fifteen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shui_zhun);

        _one=(EditText)findViewById(R.id.one);
        _two=(EditText)findViewById(R.id.two);
        _three=(EditText)findViewById(R.id.three);
        _four=(EditText)findViewById(R.id.four);
        _five=(EditText)findViewById(R.id.five);
        _six=(EditText)findViewById(R.id.six);
        _seven=(EditText)findViewById(R.id.seven);
        _eight=(EditText)findViewById(R.id.eight);
        _K1=(EditText)findViewById(R.id.K1);
        _K2=(EditText)findViewById(R.id.K2);
        station=(EditText)findViewById(R.id.station);
        save=(Button)findViewById(R.id.save);
        Ljsc=(TextView)findViewById(R.id.fifteen);
        LastStation=(TextView)findViewById(R.id.lastStation);

        final Intent i=getIntent();
        LastStation.setText("上一站:"+i.getStringExtra("测站"));
        Ljsc.setText("后前视距累积差:"+String.valueOf(i.getFloatExtra("累积视差",0)));
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean flag=true;
                flag=transfer();
                if(flag==false){
                    return;
                }
                //process对本站测量数据进行计算，并把上站得到的累计差传入，便于计算本站累计差
                process(i.getFloatExtra("累积视差",0.0f));
                Intent intent=new Intent(ShuizhunActivity.this,ShuizhunResult.class);
                intent.putExtra("前视距",twelve);
                intent.putExtra("后视距",thirteen);
                intent.putExtra("当前视差",fourtheen);
                intent.putExtra("累积视差",fifteen);
                intent.putExtra("前黑红读差",nine);
                intent.putExtra("后黑红读差",ten);
                intent.putExtra("高差之差",eleven1);
                intent.putExtra("黑面高差",sixteen);
                intent.putExtra("红面高差",seventeen);
                intent.putExtra("高差之差",eleven2);
                intent.putExtra("平均高差",eighteen);
                intent.putExtra("测站",station.getText().toString());
                //把本站测量数据传入结果显示的Activity，在结果显示界面，判断结果是否符合要求
                // 若本次测量结果满足要求，则在结果显示界面取出本站测量数据，并把数据存入数据库
                intent.putExtra("cezhan",station.getText().toString());
                intent.putExtra("K1",K1);
                intent.putExtra("K2",K2);
                intent.putExtra("one",one);
                intent.putExtra("two",two);
                intent.putExtra("three",three);
                intent.putExtra("five",five);
                intent.putExtra("six",six);
                intent.putExtra("four",four);
                intent.putExtra("eight",eight);
                intent.putExtra("seven",seven);

                startActivity(intent);
            }
        });
    }
    void process(float leijicha){
        //ten,nine分别为后视尺和前视尺的黑红面读书之差
        nine=four+K2-seven;
        ten=three+K1-eight;
        //sixteen为黑面所测的高差，seventheen为红面所测的高差。由于后视尺和前视线尺的常数不同，差值应为100
        sixteen=three-four;
        seventeen=eight-seven;

        //eleven1和eleven2高差之差，相等，可作为一次检核
        eleven1=ten-nine;
        if(Math.abs(seventeen)>Math.abs(sixteen)){
            if(seventeen>0) eleven2=sixteen-(seventeen-100);
            else eleven2=sixteen-(seventeen+100);
        }else{
            if(seventeen>0) eleven2=sixteen-(seventeen+100);
            else eleven2=sixteen-(seventeen-100);
        }
        //eighteen为该站所测得的高差
        if(Math.abs(seventeen)>Math.abs(sixteen)){
            if(seventeen>0) eighteen=(sixteen+(seventeen-100))/2;
            else eighteen=(sixteen+(seventeen+100))/2;
        }else{
            if(seventeen>0) eighteen=(sixteen+(seventeen+100))/2;
            else eighteen=(sixteen+(seventeen-100))/2;
        }
        //twelve为后视距，thirteen为前视距，fourteen为后视距与前视距的差值，fifteen为后前视距的累计差。
        twelve=slimFloat((one-two)*0.1f);
        thirteen=slimFloat((five-six)*0.1f);
        fourtheen=slimFloat(twelve-thirteen);
        fifteen=slimFloat(leijicha+fourtheen);
    }

    boolean check(String str){
        try {
            Double.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            Toast.makeText(ShuizhunActivity.this,"输入含有非数字",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    //将float数保留一位小数的函数
    float slimFloat(float x){
        int scale=2;//设置位数
        int roundingMode=4;//表示四舍五入，可以选择其他舍值方式，例如去尾等等
        BigDecimal bd=new BigDecimal((double)x);
        bd=bd.setScale(scale,roundingMode);
        return bd.floatValue();
    }
    boolean transfer(){
        if(_one.getText().toString().equals("")||_two.getText().toString().equals("")
                ||_three.getText().toString().equals("")||_four.getText().toString().equals("")
                ||_five.getText().toString().equals("")||_six.getText().toString().equals("")
                ||_seven.getText().toString().equals("")||_eight.getText().toString().equals("")
                ||_K1.getText().toString().equals("")||_K2.getText().toString().equals("")
                ||station.getText().toString().equals("")){
            Toast.makeText(ShuizhunActivity.this,"输入不完整，请重新输入",Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean flag=true;
        if(check(_one.getText().toString())==false) flag=false;
        if(check(_two.getText().toString())==false) flag=false;
        if(check(_three.getText().toString())==false) flag=false;
        if(check(_four.getText().toString())==false) flag=false;
        if(check(_five.getText().toString())==false) flag=false;
        if(check(_six.getText().toString())==false) flag=false;
        if(check(_seven.getText().toString())==false) flag=false;
        if(check(_eight.getText().toString())==false) flag=false;
        if(check(_K1.getText().toString())==false) flag=false;
        if(check(_K2.getText().toString())==false) flag=false;
        if(flag==false) return false;

        one=Integer.parseInt(_one.getText().toString());
        two=Integer.parseInt(_two.getText().toString());
        three=Integer.parseInt(_three.getText().toString());
        four=Integer.parseInt(_four.getText().toString());
        five=Integer.parseInt(_five.getText().toString());
        six=Integer.parseInt(_six.getText().toString());
        seven=Integer.parseInt(_seven.getText().toString());
        eight=Integer.parseInt(_eight.getText().toString());
        K1=Integer.parseInt(_K1.getText().toString());
        K2=Integer.parseInt(_K2.getText().toString());
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.backup:
                Toast.makeText(this,"上传",Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this,"删除",Toast.LENGTH_SHORT).show();
                break;
            case R.id.sz:
                Toast.makeText(this,"你正处于水准测量界面",Toast.LENGTH_SHORT).show();
                break;
            case R.id.dx:
                Intent intent=new Intent(ShuizhunActivity.this,DaoXianActivity.class);
                startActivity(intent);
                break;
            default:
        }
        return true;
    }
}
