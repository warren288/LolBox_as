package com.warren.lolbox.model.bean;

/**
 * 英雄胜率榜条目
 * @author:yangsheng
 * @date:2015/9/16
 */
public class HeroWinRateItem{
    private String titleCN;
    private String nameUS;
    private String nameCN;
    private float winRate;
    private float presentRate;
    private long totalPresent;

    public HeroWinRateItem() {
    }

    public String getTitleCN() {
        return titleCN;
    }

    public void setTitleCN(String titleCN) {
        this.titleCN = titleCN;
    }

    public String getNameUS() {
        return nameUS;
    }

    public void setNameUS(String nameUS) {
        this.nameUS = nameUS;
    }

    public String getNameCN() {
        return nameCN;
    }

    public void setNameCN(String nameCN) {
        this.nameCN = nameCN;
    }

    public float getWinRate() {
        return winRate;
    }

    public void setWinRate(float winRate) {
        this.winRate = winRate;
    }

    public float getPresentRate() {
        return presentRate;
    }

    public void setPresentRate(float presentRate) {
        this.presentRate = presentRate;
    }

    public long getTotalPresent() {
        return totalPresent;
    }

    public void setTotalPresent(long totalPresent) {
        this.totalPresent = totalPresent;
    }

    @Override
    public String toString() {
        return "HeroWinRateItem{" +
                "titleCN='" + titleCN + '\'' +
                ", nameUS='" + nameUS + '\'' +
                ", nameCN='" + nameCN + '\'' +
                ", winRate=" + winRate +
                ", presentRate=" + presentRate +
                ", totalPresent=" + totalPresent +
                '}';
    }


}
