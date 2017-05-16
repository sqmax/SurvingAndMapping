package com.example.survingandmapping;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/15.
 */

public class Test {
    //aJiao,dJiao分别为起始边BA和结束边CD的方位角
    public static PingchaUtil.Jiao baFangwei=new PingchaUtil.Jiao(298,59,12);
    public static PingchaUtil.Jiao cdFangwei=new PingchaUtil.Jiao(182,10,45);
    //begin,end分别为起始点A和结束点D的坐标
    public static PingchaUtil.ZuoBiao begin=new PingchaUtil.ZuoBiao(3905040.607,426376.329);
    public static PingchaUtil.ZuoBiao end=new PingchaUtil.ZuoBiao(3904603.665,426460.041);
    //表示各站观测角
    public static ArrayList<PingchaUtil.Jiao> guancejiaoList=new ArrayList<>();
    //表示各站观测距离
    public static ArrayList<Double> disList=new ArrayList<>();
    //平差结果,即各点坐标
    public static ArrayList<PingchaUtil.ZuoBiao> resultList=new ArrayList<>();

    static {
        guancejiaoList.add(new PingchaUtil.Jiao(55,46,2));
        guancejiaoList.add(new PingchaUtil.Jiao(166,24,53));
        guancejiaoList.add(new PingchaUtil.Jiao(182,38,47));
        guancejiaoList.add(new PingchaUtil.Jiao(206,14,53));
        guancejiaoList.add(new PingchaUtil.Jiao(147,55,44));
        guancejiaoList.add(new PingchaUtil.Jiao(204,11,55));

        disList.add(189.4);
        disList.add(99.93);
        disList.add(83.11);
        disList.add(33.0);
        disList.add(44.70);

    }
    //该函数用于整个导线的平差计算
    public static void calPingcha(){
        int n=guancejiaoList.size();
        //改正数/mm
        int[] gaizengshu=new int[n];
        //坐标方位角
        ArrayList<PingchaUtil.Jiao> zuobiaoFangWeiJiao=new ArrayList<>();
        //增量
        ArrayList<PingchaUtil.ZuoBiao> zengliang=new ArrayList<>();
        //改后增量
        ArrayList<PingchaUtil.ZuoBiao> gaihouZL=new ArrayList<>();

        PingchaUtil.Jiao sum=baFangwei;
        for(PingchaUtil.Jiao jiao:guancejiaoList){
            sum=addJiao(sum,jiao);
        }
        Log.d("sqpolar","sum:"+sum.du+"度"+sum.fen+"分"+sum.miao+"秒");
        //此步相当于将观测角的和减去180*n
        while(Math.abs(sum.du- cdFangwei.du)>5){
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
    private static double cosJiao(PingchaUtil.Jiao a){
        double du=a.du+a.fen*1.0/60+a.miao*1.0/3600;
//        double rad=du/180*3.1415926;
        return Math.cos(Math.toRadians(du));
    }
    private static double sinJiao(PingchaUtil.Jiao a){
        double du=a.du+a.fen*1.0/60+a.miao*1.0/3600;
//        double rad=du/180*3.1415926;
        return Math.sin(Math.toRadians(du));
    }
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
    //化简角
    private static PingchaUtil.Jiao simpleJiao(PingchaUtil.Jiao jiao){
        while(jiao.du>=360){
            jiao.du-=360;
        }
        return jiao;
    }
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
