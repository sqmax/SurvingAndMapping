package com.example.survingandmapping;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/13.
 */

public class PingchaUtil {

    //aJiao,dJiao分别为起始边BA和结束边CD的方位角
    public static PingchaUtil.Jiao baFangwei;
    public static PingchaUtil.Jiao cdFangwei;
    //begin,end分别为起始点A和结束点D的坐标
    public static PingchaUtil.ZuoBiao begin;
    public static PingchaUtil.ZuoBiao end;
    //表示各站观测角
    public static ArrayList<Jiao> guancejiaoList=new ArrayList<>();
    //表示各站观测距离
    public static ArrayList<Double> disList=new ArrayList<>();
    //平差结果,即各点坐标
    public static ArrayList<ZuoBiao> resultList=new ArrayList<>();

    //该函数用于整个导线的平差计算
    public static void calPingcha(){
        int n=guancejiaoList.size();
        //改正数/mm
        int[] gaizengshu=new int[n];
        //坐标方位角
        ArrayList<Jiao> zuobiaoFangWeiJiao=new ArrayList<>();
        //增量
        ArrayList<ZuoBiao> zengliang=new ArrayList<>();
        //改后增量
        ArrayList<ZuoBiao> gaihouZL=new ArrayList<>();

        PingchaUtil.Jiao sum=baFangwei;
        for(PingchaUtil.Jiao jiao:guancejiaoList){
            sum=addJiao(sum,jiao);
        }
        Log.d("sqpolar","sum:"+sum.du+"度"+sum.fen+"分"+sum.miao+"秒");
        //此步相当于将观测角的和减去180*n
        while(sum.du>=360){
            sum=minusJiao(sum,new PingchaUtil.Jiao(180,0,0));
        }
        //f为改正数
        int f=-((sum.du*3600+sum.fen*60+sum.miao)-(cdFangwei.du*3600+ cdFangwei.fen*60+ cdFangwei.miao)),feach,left;
        Log.d("sqpolar","f:"+f);
        feach=f/n;
        left=f%n;
        for(int i=0;i<n;i++){
            gaizengshu[i]=feach;
        }
        //分配改正数
        for(int i=1;i<n;i++){
            if(left<0){
                if(left++!=0){
                    gaizengshu[i]-=1;
                }else{
                    break;
                }
            }else if(left>0){
                if(left--!=0){
                    gaizengshu[i]+=1;
                }else{
                    break;
                }
            }
        }
        for(int i=0;i<n;i++){
            Log.d("sqpolar",gaizengshu[i]+"");
        }
        //计算方位角
        PingchaUtil.Jiao tmp=baFangwei;
        for(int i=0;i<n-1;i++){
            tmp=addJiao(tmp,guancejiaoList.get(i));
            tmp=addJiao(tmp,new PingchaUtil.Jiao(0,0,gaizengshu[i]));
            tmp.du-=180;
            zuobiaoFangWeiJiao.add(tmp);
        }
        for(int i=0;i<n-1;i++){
            Log.d("sqpolar",zuobiaoFangWeiJiao.get(i).du+"度"+zuobiaoFangWeiJiao.get(i).fen+"分"+zuobiaoFangWeiJiao.get(i).miao+"秒");
        }
        //增量x,y的改正数
        int[] tmpGzsX=new int[n-1];
        int[] tmpGzsY=new int[n-1];
        double sumX=0,sumY=0,sumDis=0;
        for(int i=0;i<n-1;i++){
            //计算增量x,y
            zengliang.add(new PingchaUtil.ZuoBiao(cosJiao(zuobiaoFangWeiJiao.get(i))*disList.get(i),
                    sinJiao(zuobiaoFangWeiJiao.get(i))*disList.get(i)));
            sumX+=zengliang.get(i).x;
            sumY+=zengliang.get(i).y;
            sumDis+=disList.get(i);
        }
        for(int i=0;i<n-1;i++){
            Log.d("sqpolar",zengliang.get(i).x+" "+zengliang.get(i).y);
        }
        double fx=-(sumX-(end.x-begin.x));
        double fy=-(sumY-(end.y-begin.y));
        Log.d("sqpolar",fx+" "+fy);
        for(int i=0;i<n-1;i++){
            double tmpX,tmpY;
            tmpX=disList.get(i)/sumDis*fx;
            tmpY=disList.get(i)/sumDis*fy;
            gaihouZL.add(new PingchaUtil.ZuoBiao(zengliang.get(i).x+tmpX,zengliang.get(i).y+tmpY));
        }
        Log.d("sqpolar","改后增量");
        for(int i=0;i<n-1;i++){
            Log.d("sqpolar",gaihouZL.get(i).x+" "+gaihouZL.get(i).y);
        }
        //计算最终平差的坐标
        PingchaUtil.ZuoBiao tmpZB=begin;
        resultList.clear();
        resultList.add(new PingchaUtil.ZuoBiao(begin.x,begin.y));
        for(int i=0;i<n-2;i++){
            tmpZB.x=tmpZB.x+gaihouZL.get(i).x;
            tmpZB.y=tmpZB.y+gaihouZL.get(i).y;
            PingchaUtil.ZuoBiao item=new PingchaUtil.ZuoBiao(tmpZB.x,tmpZB.y);
            resultList.add(item);
        }
        resultList.add(new PingchaUtil.ZuoBiao(end.x,end.y));
        for(int i=0;i<n;i++){
            Log.d("sqpolar",resultList.get(i).x+" "+resultList.get(i).y);
        }
    }

    //用来求自定义的角的cos值
    private static double cosJiao(PingchaUtil.Jiao a){
        double du=a.du+a.fen*1.0/60+a.miao*1.0/3600;
//        double rad=du/180*3.1415926;
        return Math.cos(Math.toRadians(du));
    }
    //用来求自定义角的sin值
    private static double sinJiao(PingchaUtil.Jiao a){
        double du=a.du+a.fen*1.0/60+a.miao*1.0/3600;
//        double rad=du/180*3.1415926;
        return Math.sin(Math.toRadians(du));
    }
    //用来求两个自定义角的和
    private static PingchaUtil.Jiao addJiao(PingchaUtil.Jiao a, PingchaUtil.Jiao b){
        PingchaUtil.Jiao rnt=new PingchaUtil.Jiao();
        int f1=a.du*3600+a.fen*60+a.miao;
        int f2=b.du*3600+b.fen*60+b.miao;
        int f=f1+f2;
        rnt.du=f/3600;
        rnt.fen=f%3600/60;
        rnt.miao=f%60;
        return rnt;
    }
    //用来求两个自定义角的差
    private static PingchaUtil.Jiao minusJiao(PingchaUtil.Jiao a, PingchaUtil.Jiao b){
        PingchaUtil.Jiao rnt=new PingchaUtil.Jiao();
        int f1=a.du*3600+a.fen*60+a.miao;
        int f2=b.du*3600+b.fen*60+b.miao;
        int f=f1-f2;
        rnt.du=f/3600;
        rnt.fen=f%3600/60;
        rnt.miao=f%60;
        return rnt;
    }
    //化简角
    private static PingchaUtil.Jiao simpleJiao(PingchaUtil.Jiao jiao){
        while(jiao.du>=360){
            jiao.du-=360;
        }
        return jiao;
    }
    //该函数由于测试数据
    public static void debugFuc(){
        Log.d("sqpolar",baFangwei.du+"度"+baFangwei.fen+"分"+baFangwei.miao+"秒");
        Log.d("sqpolar", cdFangwei.du+"度"+ cdFangwei.fen+"分"+ cdFangwei.miao+"秒");
        Log.d("sqpolar","X:"+begin.x+" Y:"+begin.y);
        Log.d("sqpolar","Y:"+end.x+" Y:"+end.y);
        for(PingchaUtil.Jiao jiao:guancejiaoList){
            Log.d("sqpolar",jiao.du+"度"+jiao.fen+"分"+jiao.miao+"秒");
        }
        for (Double d:disList){
            Log.d("sqpolar",d+"");
        }
    }
    public static class Jiao{
        int du;
        int fen;
        int miao;
        public Jiao(){
            du=fen=miao=0;
        }
        public Jiao(int du,int fen,int miao){
            this.du=du;
            this.fen=fen;
            this.miao=miao;
        }
    }
    public static class ZuoBiao{
        double x;
        double y;

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public ZuoBiao(double x, double y){
            this.x=x;
            this.y=y;
        }
    }
}
