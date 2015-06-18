package com.warren.lolbox.model.bean;

/**
 * 英雄更新数据
 * @author:yangsheng
 * @date:2015/6/9
 */
public class HeroUpdate {

    private String info;
    private String time;
    private String version;

    public HeroUpdate(){

    }

    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "HeroUpdate{" +
                "info='" + info + '\'' +
                ", time='" + time + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

}
