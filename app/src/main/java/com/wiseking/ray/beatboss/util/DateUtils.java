package com.wiseking.ray.beatboss.util;

/**
 * Created by guo on 2018/2/4.
 */

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    @SuppressLint("SimpleDateFormat")
    public static String dateToString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        return df.format(date);
    }
}
