package com.example.survingandmapping.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/5/21.
 */

public class DaoXianQiShi extends DataSupport {
    private int id;
    private float qishibianFWJ;
    private float jieshubianFWJ;
    private float qishidianZBX;
    private float qishidianZBY;
    private float jieshudianZBX;
    private float jieshudianZBY;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getQishibianFWJ() {
        return qishibianFWJ;
    }

    public void setQishibianFWJ(float qishibianFWJ) {
        this.qishibianFWJ = qishibianFWJ;
    }

    public float getJieshubianFWJ() {
        return jieshubianFWJ;
    }

    public void setJieshubianFWJ(float jieshubianFWJ) {
        this.jieshubianFWJ = jieshubianFWJ;
    }

    public float getQishidianZBX() {
        return qishidianZBX;
    }

    public void setQishidianZBX(float qishidianZBX) {
        this.qishidianZBX = qishidianZBX;
    }

    public float getQishidianZBY() {
        return qishidianZBY;
    }

    public void setQishidianZBY(float qishidianZBY) {
        this.qishidianZBY = qishidianZBY;
    }

    public float getJieshudianZBX() {
        return jieshudianZBX;
    }

    public void setJieshudianZBX(float jieshudianZBX) {
        this.jieshudianZBX = jieshudianZBX;
    }

    public float getJieshudianZBY() {
        return jieshudianZBY;
    }

    public void setJieshudianZBY(float jieshudianZBY) {
        this.jieshudianZBY = jieshudianZBY;
    }
}
