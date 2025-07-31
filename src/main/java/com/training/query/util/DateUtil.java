package com.training.query.util;



import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class DateUtil {
    public static long daysUntil(Date due) {
        long diffMillis = due.getTime() - System.currentTimeMillis();
        return TimeUnit.MILLISECONDS.toDays(diffMillis);
    }
    private DateUtil() {}
}
