package com.training.query.Training.Query.util;

import java.util.Calendar;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class QuarterUtils {

    public static Map<String, Date> getCurrentQuarterDates() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH); // 0-based
        int year = cal.get(Calendar.YEAR);

        int startMonth;

        if (month <= 2) {        // Jan–Mar → Q1
            startMonth = Calendar.JANUARY;
        } else if (month <= 5) { // Apr–Jun → Q2
            startMonth = Calendar.APRIL;
        } else if (month <= 8) { // Jul–Sep → Q3
            startMonth = Calendar.JULY;
        } else {                 // Oct–Dec → Q4
            startMonth = Calendar.OCTOBER;
        }

        Calendar start = Calendar.getInstance();
        start.set(year, startMonth, 15, 0, 0, 0);
        start.set(Calendar.MILLISECOND, 0);

        Calendar end = Calendar.getInstance();
        end.set(year, startMonth, 30, 23, 59, 59);
        end.set(Calendar.MILLISECOND, 999);

        Map<String, Date> map = new HashMap<>();
        map.put("startDate", start.getTime());
        map.put("endDate", end.getTime());
        return map;
    }

    public static String getCurrentQuarterLabel() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        return switch (month / 3) {
            case 0 -> "Q1-" + year;
            case 1 -> "Q2-" + year;
            case 2 -> "Q3-" + year;
            default -> "Q4-" + year;
        };
    }
}