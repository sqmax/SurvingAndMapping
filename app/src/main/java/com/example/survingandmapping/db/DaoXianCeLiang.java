package com.example.survingandmapping.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/5/21.
 */

public class DaoXianCeLiang extends DataSupport {
    private int id;
    private String cezhan;

    public String getCezhan() {
        return cezhan;
    }

    public void setCezhan(String cezhan) {
        this.cezhan = cezhan;
    }

    private float guancejiao;
    private float guancejiuli;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getGuancejiao() {
        return guancejiao;
    }

    public void setGuancejiao(float guancejiao) {
        this.guancejiao = guancejiao;
    }

    public float getGuancejiuli() {
        return guancejiuli;
    }

    public void setGuancejiuli(float guancejiuli) {
        this.guancejiuli = guancejiuli;
    }
}
