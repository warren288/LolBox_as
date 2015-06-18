package com.warren.lolbox.model.bean;

import java.util.Map;

/**
 * 英雄每周数据
 * @author:yangsheng
 * @date:2015/6/15
 */
public class HeroWeekData {
    private int code;
    private String msg;
    private HeroWeekDataIn data;

    public HeroWeekData() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public HeroWeekDataIn getData() {
        return data;
    }

    public void setData(HeroWeekDataIn data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HeroWeekData{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
