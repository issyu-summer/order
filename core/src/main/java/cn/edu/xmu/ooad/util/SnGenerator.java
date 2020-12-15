package cn.edu.xmu.ooad.util;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 编号生成器
 * @author issyu 30320182200070
 * @date 2020/12/15 0:27
 */
public class SnGenerator {

    public static String nextSn(){
        UUID uuid = UUID.randomUUID();
        String sn = uuid.toString().replace("-", "");
        return sn;
    }
}
