package com.weather.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class AppUtils {

    private AppUtils() {
    }

    public static String getDayFromDate(String timeStamp) {
        long stamp = Long.parseLong(timeStamp);

        Timestamp ts = new Timestamp(stamp);
        Date date = new Date(ts.getTime());

        return new SimpleDateFormat("EEEE").format(date);

    }
}
