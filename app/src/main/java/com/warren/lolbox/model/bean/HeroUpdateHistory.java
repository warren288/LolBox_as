package com.warren.lolbox.model.bean;

import java.util.List;

/**
 * 英雄更新历史
 * @author:yangsheng
 * @date:2015/6/9
 */
public class HeroUpdateHistory {
    private List<HeroUpdate> changeLog;
    private String cName;
    private String coupon;
    private String eName;
    private String gold;
    private String location;

    public HeroUpdateHistory() {
    }

    public List<HeroUpdate> getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(List<HeroUpdate> changeLog) {
        this.changeLog = changeLog;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String getGold() {
        return gold;
    }

    public void setGold(String gold) {
        this.gold = gold;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "HeroUpdateHistory{" +
                "changeLog=" + changeLog +
                ", cName='" + cName + '\'' +
                ", coupon='" + coupon + '\'' +
                ", eName='" + eName + '\'' +
                ", gold='" + gold + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
