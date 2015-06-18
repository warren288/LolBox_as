package com.warren.lolbox.model.bean;

import java.util.List;
import java.util.Map;

/**
 * 英雄每周数据，实体
 * @author:yangsheng
 * @date:2015/6/15
 */
public class HeroWeekDataIn {
    private int average_a_num;
    private int average_d_num;
    private int average_k_num;
    private int average_win_ratio;
    private int champion_id;
    private int rank;
    private List<HeroWeekRate> charts;

    public HeroWeekDataIn() {
    }

    public int getAverage_a_num() {
        return average_a_num;
    }

    public void setAverage_a_num(int average_a_num) {
        this.average_a_num = average_a_num;
    }

    public int getAverage_d_num() {
        return average_d_num;
    }

    public void setAverage_d_num(int average_d_num) {
        this.average_d_num = average_d_num;
    }

    public int getAverage_k_num() {
        return average_k_num;
    }

    public void setAverage_k_num(int average_k_num) {
        this.average_k_num = average_k_num;
    }


    public int getAverage_win_ratio() {
        return average_win_ratio;
    }

    public void setAverage_win_ratio(int average_win_ratio) {
        this.average_win_ratio = average_win_ratio;
    }

    public int getChampion_id() {
        return champion_id;
    }

    public void setChampion_id(int champion_id) {
        this.champion_id = champion_id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public List<HeroWeekRate> getCharts() {
        return charts;
    }

    public void setCharts(List<HeroWeekRate> charts) {
        this.charts = charts;
    }

    @Override
    public String toString() {
        return "HeroWeekDataIn{" +
                "average_a_num=" + average_a_num +
                ", average_d_num=" + average_d_num +
                ", average_win_ratio=" + average_win_ratio +
                ", champion_id=" + champion_id +
                ", rank=" + rank +
                ", charts=" + charts +
                '}';
    }
}
