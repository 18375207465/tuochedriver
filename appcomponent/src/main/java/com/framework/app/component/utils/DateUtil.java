package com.framework.app.component.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期时间格式化工具
 *
 * @author Xun.Zhang
 * @ClassName: DateUtil.java
 * @date 2015-1-14 上午9:26:20
 */
public class DateUtil {

    /**
     * 默认日期格式
     */
    public static final String DEFAULT_FORMAT = "yyyy.MM.dd HH:mm";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYYMMDD = "yy/MM/dd";
    public static final String MM_DD_HH_MM = "MM-dd HH:mm";

    public static String getDefaultFormatDate(long unixTimestamp) {
        Date date = new Date(unixTimestamp * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_FORMAT, Locale.getDefault());
        return formatter.format(date);
    }

    /**
     * 返回特定格式的日期字符串
     *
     * @param format
     * @param time
     * @return
     */
    public static String getStringDate(String format, long time) {
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    /**
     * 获取现在时间
     *
     * @return 返回<br>
     * 字符串格式</br> yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDateLong() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS, Locale.getDefault());
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String friendlyTime(long unixTimestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDD, Locale.US);
        Date time = new Date(unixTimestamp * 1000);
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = sdf.format(cal.getTime());
        String paramDate = sdf.format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0) {
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            } else {
                ftime = hour + "小时前";
            }
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0) {
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            } else {
                ftime = hour + "小时前";
            }
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = sdf.format(time);
        }
        return ftime;
    }

    /**
     * 以友好的方式显示时间
     *
     * @param sdate yyyy-MM-dd HH：mm:ss
     * @return
     */
    public static String friendlyTime(String sdate) {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD, Locale.US);
        Date time = strToDateLong(sdate);
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = sdf.format(cal.getTime());
        String paramDate = sdf.format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0) {
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            } else {
                ftime = hour + "小时前";
            }
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0) {
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            } else {
                ftime = hour + "小时前";
            }
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = sdf.format(time);
        }
        return ftime;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate 时间字符串 yyyyMMddHHmmss
     * @return Date
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD", Locale.getDefault());
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 月日
     */
    public static String get_Md_String(long time) {
        Date d = new Date(time);

        SimpleDateFormat times = new SimpleDateFormat("M月d日");
        return times.format(d);
    }

    /**
     * 功能描述：返回月
     *
     * @param date Date 日期
     * @return 返回月份
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 功能描述：返回日
     *
     * @param date Date 日期
     * @return 返回日份
     */
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }


    /**
     * 年月日
     */
    public static String get_YMd_String(long time) {
        Date d = new Date(time);
        SimpleDateFormat times = new SimpleDateFormat("Y年M月d日");
        return times.format(d);
    }

    /**
     * yyyy-MM-dd
     */
    public static String get_YYMMDD_W_String(long time) {
        SimpleDateFormat formatter_f = new SimpleDateFormat("yyyy-MM-dd");
        return formatter_f.format(new Date(time));
    }


    public static String get_YYMMDD_HH_MM_String(long time) {
        SimpleDateFormat formatter_f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter_f.format(new Date(time));
    }

    public static String get_yMdHms_String(long time) {
        Date d = new Date(time);
        SimpleDateFormat times = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return times.format(d);
    }


    /**
     * 日期格式字符串转换成时间戳
     *
     * @param
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            long temp = sdf.parse(date_str).getTime();
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getAge(Date birthDate) {

        if (birthDate == null)
            throw new
                    RuntimeException("出生日期不能为null");

        int age = 0;

        Date now = new Date();

        SimpleDateFormat format_y = new
                SimpleDateFormat("yyyy");
        SimpleDateFormat format_M = new
                SimpleDateFormat("MM");

        String birth_year =
                format_y.format(birthDate);
        String this_year =
                format_y.format(now);

        String birth_month =
                format_M.format(birthDate);
        String this_month =
                format_M.format(now);

        // 初步，估算
        age =
                Integer.parseInt(this_year) - Integer.parseInt(birth_year);

        // 如果未到出生月份，则age - 1
        if (this_month.compareTo(birth_month) < 0)
            age -= 1;
        if (age < 0)
            age = 0;
        return age;
    }

    /**
     * 当天的开始时间
     *
     * @return
     */
    public static long startOfTodDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        return date.getTime();
    }

    /**
     * 当天的结束时间
     *
     * @return
     */
    public static long endOfTodDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date date = calendar.getTime();
        return date.getTime();
    }

    /*
* 毫秒转化
*/
    public static String formatTime(long ms) {

        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

        return "第" + strDay + "天";
    }

    public static int getAgeForyyyy_mm_dd(String time) {
        long stamp = DateUtil.date2TimeStamp(time, "yyyy-MM-dd");
        java.sql.Date date = new java.sql.Date(stamp);
        int age = DateUtil.getAge(date);
        return age;
    }


}