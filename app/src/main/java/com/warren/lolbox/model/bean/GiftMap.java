package com.warren.lolbox.model.bean;

import java.util.List;

/**
 * 天赋图
 * @author:yangsheng
 * @date:2015/8/23
 */
public class GiftMap {
    private List<GiftDot> a;
    private List<GiftDot> d;
    private List<GiftDot> g;

    public GiftMap() {
    }

    public List<GiftDot> getA() {
        return a;
    }

    public void setA(List<GiftDot> a) {
        this.a = a;
    }

    public List<GiftDot> getD() {
        return d;
    }

    public void setD(List<GiftDot> d) {
        this.d = d;
    }

    public List<GiftDot> getG() {
        return g;
    }

    public void setG(List<GiftDot> g) {
        this.g = g;
    }

    @Override
    public String toString() {
        return "GiftMap{" +
                "a=" + a +
                ", d=" + d +
                ", g=" + g +
                '}';
    }
}
