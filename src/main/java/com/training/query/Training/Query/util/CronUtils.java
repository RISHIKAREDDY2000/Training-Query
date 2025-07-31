package com.training.query.Training.Query.util;

import org.springframework.scheduling.support.CronExpression;

import java.time.ZonedDateTime;
import java.util.Date;

public class CronUtils {


    public static Date getNextRunTime(String cronExpression) {
        try {
            CronExpression expression = CronExpression.parse(cronExpression);
            ZonedDateTime now = ZonedDateTime.now();
            ZonedDateTime next = expression.next(now);
            return next != null ? Date.from(next.toInstant()) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}