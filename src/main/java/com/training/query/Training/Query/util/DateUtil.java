package com.training.query.Training.Query.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtil {
    public static boolean isDueInNext3Days(Date dueDate) {
        if (dueDate == null) return false;
        long diff = ChronoUnit.DAYS.between(
                LocalDate.now(),
                dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        );
        return diff <= 3;
    }
}
