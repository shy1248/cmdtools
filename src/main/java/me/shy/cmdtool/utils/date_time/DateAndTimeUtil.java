package utils.date_time;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @fileName: com.ysb.commands.utils.date_time.DateAndTimeUtil.java
 * @version: 1.0
 * @since: 2016-4-11 下午2:32:19
 * @describe : 
 * 			        类说明。。。 
 *
 * ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2016.
 */

public class DateAndTimeUtil {
    private static final String DATE_PART_FORMAT = "yyyy-MM-dd";
    private static final String TIME_PART_FORMAT = "HH:mm:ss.SS";
    
    /**
     * 描述：获取当前时间
     * 
     * @return 返回当前时间e.g. 12:12:50
     */
    public static String getCurDateTime() {
        return getCurDateTime(TIME_PART_FORMAT);
    }
    
    /**
     * 描述：获取当前日期
     * 
     * @return 返回当前日期 e.g. 2006-06-06
     */
    public static String getCurDate() {
        return getCurDateTime(DATE_PART_FORMAT);
    }
    
    /**
     * 描述：根据给定的格式返回当前日期或时间
     * 
     * @param formatString
     *            e.g. "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String getCurDateTime(String formatString) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatString);
        String now = sdf.format(new Date());
        return now;
    }
    
    /**
     * 描述：在信息前加上当前时间
     * 
     * @param info
     *            源信息字符串
     * @return 格式化后的字符串
     */
    public static String formatInfo(String info) {
        return "[" + getCurDateTime() + "]： " + info + "\n";
    }
    
    /**
     * 描述：在控制台打印一行信息
     * 
     * @param message
     *            要打印的信息
     */
    public static void println(String message) {
        System.out.print(formatInfo(message));
    }
    
    /**
     * 
     *描述: 将秒数格式化为时分秒格式
     * @param second
     * @return
     * @author Yu Shuibo 2016-4-11 下午2:36:51
     */
    public static String changeSecToHMS(int second) {
        int min = second / 60;
        int hour = min / 60;
        int sec = second % 60;
        return hour + "hh" + min + "MM" + sec + "ss";
    }
    
    public static String chageSecToHMSFormat(int second, String hour, String min, String sec) {
        String timeHMS = changeSecToHMS(second);
        timeHMS.replaceAll("H", hour);
        timeHMS.replaceAll("M", min);
        timeHMS.replaceAll("S", sec);
        return timeHMS;
    }
    
}
