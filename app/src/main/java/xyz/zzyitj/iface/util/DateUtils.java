package xyz.zzyitj.iface.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * xyz.zzyitj.iface.util
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/12 10:19
 * @since 1.0
 */
public class DateUtils {
    /**
     * 检查当前时间是否大于中午（12：00）
     *
     * @return true 当前时间大于中午12:00
     */
    public static boolean checkNowIsNoon() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        return Integer.parseInt(simpleDateFormat.format(new Date())) >= 12;
    }
}
