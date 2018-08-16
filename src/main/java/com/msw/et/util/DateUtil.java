package com.msw.et.util;

/**
 * @author  mashuangwei
 * 日期 2017/12/3
 */

import net.sf.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String formatDate(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static Date parse(String strDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(strDate);
    }

    public static JSONObject timeToDate(String seconds) {

        Date dt = new Date(Long.valueOf(seconds));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String timestamp = sdf.format(dt);
        String[] time = timestamp.split("-");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("year", Integer.parseInt(time[0]));
        jsonObject.put("month", Integer.parseInt(time[1]));
        jsonObject.put("day", Integer.parseInt(time[2]));
        jsonObject.put("hour", Integer.parseInt(time[3]));
        jsonObject.put("minutes", Integer.parseInt(time[4]));
        jsonObject.put("seconds", Integer.parseInt(time[5]));
        return jsonObject;
    }

    /**
     * 6      * 时间戳转换成日期格式字符串
     * 7      * @param seconds 精确到秒的字符串
     * 8      * @param formatStr
     * 9      * @return
     * 10
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }
}
