package com.warren.lolbox.url;

import java.util.Date;

/**
 * @author:yangsheng
 * @date:2015/6/9
 */
public class DuowanUtil {

    /**
     * 将字符串形式的时间数字转换为实际时间
     * @param strTimeSrc  format: "1405601553"
     * @return format: "2014年8月7日"
     */
    public static String translateTime(String strTimeSrc){
        Date time = new Date(1000 * Long.parseLong(strTimeSrc));
        String strTime ="" + (time.getYear() + 1900) + "年" + (time.getMonth() + 1) + "月" + time.getDate() + "日 "
                + time.getHours() + ":" + time.getMinutes();
        return strTime;
    }
}
