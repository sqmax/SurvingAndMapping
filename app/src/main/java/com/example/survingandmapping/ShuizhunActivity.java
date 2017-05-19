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
        final Intent intent=getIntent();

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

        if(intent==null){
            Ljsc.setText("后前视距累积差:0");
            LastStation.setText("上一站：0");
        }else{
            LastStation.setText("上一站:"+intent.getStringExtra("测站"));
            Ljsc.setText("后前视距累积差:"+String.valueOf(intent.getFloatExtra("累积视差",0)));
        }
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean flag=true;
                flag=transfer();
                if(flag==false){
                    return;
                }
                process(intent);
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
                startActivity(intent);
            }
        });
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
    boolean check(String str){
        try {
            Double.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            Toast.makeText(ShuizhunActivity.this,"输入含有非数字",Toast.LENGTH_SHORT).show();
            return false;
        }
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

    void process(Intent intent){
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
        twelve=(one-two)*0.1f;
        thirteen=(five-six)*0.1f;
        fourtheen=((int)((twelve-thirteen)*10))*0.1f;
        if(intent==null){
            fifteen=fourtheen;
        }else{
            fifteen=intent.getFloatExtra("累积视差",0)+fourtheen;
        }
    }
}
