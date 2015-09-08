package com.warren.lolbox.model.bean;

import java.util.List;

/**
 * 天赋点
 * @author:yangsheng
 * @date:15-8-23
 */
public class GiftDot{


    private List<String> level;
    private String need;
    private String name;
    private String id;

    public GiftDot(){

    }

    public void setLevel(List<String> level) {
        this.level = level;
    }

    public void setNeed(String need) {
        this.need = need;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getLevel() {
        return level;
    }

    public String getNeed() {
        return need;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }


    @Override
    public String toString() {
        return "GiftDot{" +
                "level=" + level +
                ", need='" + need + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
