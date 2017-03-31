package com.kk;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;

public class DateUtils {
	
	  /**
     * 根据给定的格式，将日期格式化为字符串
     *
     * @param date   日期对象
     * @param format 希望被转换成的格式
     * @return
     */
    public static String format(Date date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    /**
     * 把原本有格式的日期字符串格式化成另外一个格式
     *
     * @param date      待有格式的时间字符串
     * @param format    时间字符创原有的格式
     * @param formatStr 希望被转换成的格式
     * @return
     */
    public static String format(String date, String format, String formatStr) {
        if (date == null || "".equals(date)) {
            return null;
        }
        String formatDate = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date d = sdf.parse(date);
            SimpleDateFormat sdf2 = new SimpleDateFormat(formatStr);
            formatDate = sdf2.format(d);
        } catch (Exception e) {
        }
        return formatDate;
    }

    /**
     * 将指定格式的时间字符串转化为时间对象
     *
     * @param date
     * @param format
     * @return
     */
    public static Date strToDate(String date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date d = null;
        try {
            d = df.parse(date);
        } catch (Exception e) {
        }
        return d;
    }

    /**
     * 返回本周星期的每一天的对应的日期
     *
     * @param field
     * @return
     */
    public static Date getDayOfWeek(int field) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, field);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 返回本周星期的每一天的对应的日期
     *
     * @param field
     * @return
     */
    public static String getDayOfWeek(int field, String format) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, field);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return format(calendar.getTime(), format);
    }

    /**
     * 获取当前时间是本周几
     *
     * @param date
     * @return
     */
    public static int getWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取参数时间所在的周是第几周
     *
     * @param date
     * @return
     */
    public static int getWeekIndex(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取本周第一天的日期对象
     *
     * @return
     */
    public static Date getFirstDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取参数日期所在周 第一天的日期对象
     *
     * @return
     */
    public static Date getFirstDayOfWeek(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取本周第一天的日期字符串对象
     *
     * @param format
     * @return
     */
    public static String getFirstDayOfWeek(String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return format(calendar.getTime(), format);
    }

    /**
     * 获取本周最后一天的日期
     * 返回格式 yyyy-MM-dd
     *
     * @return Date
     */
    public static Date getLastDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 获取本周最后一天的日期
     * 返回格式 yyyy-MM-dd
     *
     * @return
     */
    public static Date getLastDayOfWeek(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 获取本周最后一天的日期
     * 返回格式 yyyy-MM-dd
     *
     * @return
     */
    public static String getLastDayOfWeek(String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return format(calendar.getTime(), format);
    }

    /**
     * 计算某个日期往后或往前推N天后的日期
     *
     * @param date 日期对象
     * @param num  天数  正数表示往后推 负数表示往前推
     * @return
     */
    public static Date addDay(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, num);
        return calendar.getTime();
    }

    /**
     * 计算某个日期往后或往前推N天后的日期
     *
     * @param date 日期对象
     * @param num  天数  正数表示往后推 负数表示往前推
     * @return
     */
    public static Date addMonth(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, num);
        return calendar.getTime();
    }

    /**
     * 计算某个日期往后或往前推N天后的日期
     *
     * @param date 日期对象
     * @param num  天数  正数表示往后推 负数表示往前推
     * @return
     */
    public static String addDay(Date date, int num, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, num);
        return format(calendar.getTime(), format);
    }

    /**
     * 计算某个日期往后或往前推N天后的日期  排除周末
     *
     * @param date 日期对象
     * @param num  天数  正数表示往后推 负数表示往前推
     * @return
     */
    public static Date addDayWithoutWeekend(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for (int i = 0; i < num; i++) {
            calendar.add(Calendar.DATE, 1);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                i--;
            }
        }
        return calendar.getTime();
    }

    /**
     * 获取两个日期间隔的天数
     *
     * @return
     */
    public static int getDateInterval(Date start, Date end) {
        long daysOfMilliseconds = end.getTime() - start.getTime();
        long days = (daysOfMilliseconds / (1000 * 60 * 60 * 24));
        return Long.valueOf(days).intValue();
    }


    /**
     * 排除周六日以后的天数
     */
    public static int getDateIntervalWithoutWeek(Date start, Date end) {
        int count = getDateInterval(start, end);
        //定义变量，获得周六周日的总天数
        int jrcount = 0;
        Date d = null;
        Calendar c = null;
        int day;
        for (int i = 1; i <= count; i++) {
            d = addDay(start, i);
            c = Calendar.getInstance();
            c.setTime(d);
            day = c.get(Calendar.DAY_OF_WEEK);
            if (day == 1 || day == 7) {
                jrcount++;
            }
        }
        return count + 1 - jrcount;
    }

    /**
     * 根据周号获取 对应周的开始时间(星期天)
     *
     * @return
     */
    public static Date getStartDateByWeekIndex(Integer year, Integer weekIndex) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.WEEK_OF_YEAR, weekIndex);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 根据周号获取 对应周的结束时间(星期六)
     *
     * @return
     */
    public static Date getEndDateByWeekIndex(Integer year, Integer weekIndex) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.WEEK_OF_YEAR, weekIndex);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    public static int computeWeeks(Date startDate, Date endDate) {
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(startDate);
        beginCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        beginCalendar.set(Calendar.DAY_OF_WEEK, beginCalendar.getFirstDayOfWeek());
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        endCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        endCalendar.set(Calendar.DAY_OF_WEEK, endCalendar.getFirstDayOfWeek());
        int days = 1;
        while (beginCalendar.before(endCalendar)) {
            days++;
            beginCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return days / 7;

    }


    /**
     * 校验日期格式是否正确(正确则格式化成功返回”true“错误就报错返回”false“)
     *
     * @param date
     * @param format
     * @return
     */
    public static boolean isValidDate(String date, String format) {
        try {
            SimpleDateFormat dateFormat = null;
            dateFormat = new SimpleDateFormat(format);
            dateFormat.setLenient(false);
            dateFormat.parse(date);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            //如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }


    /**
     * 获取本月最后一天的日期
     * 返回格式 yyyy-MM-dd
     *
     * @return
     */
    public static String getLastDayOfMonth(Date d, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return format(calendar.getTime(), format);
    }
    
    /**
	 * 整点时间毫秒数 2014-3-18 17:0:0:000
	 */
	public static long oclock(Date time) {
		return new DateTime(time).withMinuteOfHour(0).withSecondOfMinute(0)
				.withMillisOfSecond(0).getMillis();
	}
	
	/**
	 * 整日时间毫秒数 2014-3-18 0:0:0:000
	 */
	public static long dclock(Date time) {
		return new DateTime(time).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
				.withMillisOfSecond(0).getMillis();
	}

	/**
	 * 将Long型时间格式化成yyyy-MM-dd :HH:mm:ss
	 * @param time
	 * @return
	 */
	public static String longToDate(long time){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd :HH:mm:ss");
		return df.format(time);
	}
   
	/**
	 * 将“秒”转换成“天时分秒”
	 * @param seconds
	 * @return
	 */
	private static String SecondsToTime(final long seconds) {  
	    if(seconds < 0){  
	        return "秒数必须大于0";  
	    }  
	    long one_day = 60 * 60 * 24;  
	    long one_hour  = 60 * 60;  
	    long one_minute = 60;  
	    long day,hour,minute,second = 0L;;  
	      
	    day = seconds / one_day;  
	    hour = seconds % one_day / one_hour ;  
	    minute = seconds % one_day % one_hour /  one_minute;  
	    second = seconds % one_day % one_hour %  one_minute;  
	      
	    if(seconds < one_minute){  
	        return seconds + "秒";  
	    }else if(seconds >= one_minute && seconds < one_hour){  
	        return minute + "分" + second + "秒";  
	    }else if (seconds >= one_hour && seconds < one_day){  
	        return hour + "时" + minute + "分" +  second + "秒";  
	    }else{  
	        return day + "天" + hour + "时" + minute + "分" +  second + "秒";  
	    }  
	}  
	
	public static void main(String []agrs){
		
		System.out.println(SecondsToTime(3600));
		longToDate(1400575988604L);
		
		
	}
	
	
}
