package com.example.survingandmapping;

/**
 * Created by Administrator on 2017/5/11.
 */

public class Station {
    private String ceZhan;
    private double dis;
    private GuanCeJ guanCeJ;

    public Station(String ceZhan,double dis,GuanCeJ guanCeJ){
        this.ceZhan=ceZhan;
        this.dis=dis;
        this.guanCeJ=guanCeJ;
    }
    public String getCeZhan() {return ceZhan;}

    public double getDis() {
        return dis;
    }

    public GuanCeJ getGuanCeJ() {
        return guanCeJ;
    }

    public static class GuanCeJ {
        int du, fen, miao;
        int getDu(){
            return du;
        }
        int getFen(){
            return fen;
        }
        int getMiao(){
            return miao;
        }
       public GuanCeJ(int du, int fen, int miao) {
            this.du = du;
            this.fen = fen;
            this.miao = miao;
        }
    }
}
