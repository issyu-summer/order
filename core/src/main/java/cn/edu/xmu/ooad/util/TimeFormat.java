package cn.edu.xmu.ooad.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author issyu 30320182200070
 * @date 2020/12/5 0:45
 * String 与 LocalDateTime的转换
 */
public class TimeFormat {

    public static LocalDateTime stringToDateTime(String s){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse(s,df);
        return  ldt;
    }
}
