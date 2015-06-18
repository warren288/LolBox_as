package com.warren.lolbox.model.bean;

import java.util.HashMap;
import java.util.List;

/**
 * 英雄每周数据比例实体
 * @author:yangsheng
 * @date:2015/6/15
 */
public class HeroWeekRate {
    private String color;
    private String title;
    private String y_axis_title;
    private List<HashMap<String, String>> ratio_data;

    public HeroWeekRate() {
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getY_axis_title() {
        return y_axis_title;
    }

    public void setY_axis_title(String y_axis_title) {
        this.y_axis_title = y_axis_title;
    }

    public List<HashMap<String, String>> getRatio_data() {
        return ratio_data;
    }

    public void setRatio_data(List<HashMap<String, String>> ratio_data) {
        this.ratio_data = ratio_data;
    }

    @Override
    public String toString() {
        return "HeroWeekRate{" +
                "color='" + color + '\'' +
                ", title='" + title + '\'' +
                ", y_axis_title='" + y_axis_title + '\'' +
                ", ratio_data=" + ratio_data +
                '}';
    }
}
