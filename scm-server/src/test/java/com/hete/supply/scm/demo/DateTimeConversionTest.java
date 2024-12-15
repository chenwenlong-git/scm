package com.hete.supply.scm.demo;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * @author yanjiawei
 * Created on 2024/3/11.
 */
public class DateTimeConversionTest {
    public static void main(String[] args) {
        // 创建 LocalDateTime 对象
        LocalDateTime now = LocalDateTime.now();

        // 创建 DateTimeFormatterBuilder，并设置自定义格式
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendValue(ChronoField.YEAR)
                .appendLiteral('-')
                .appendValue(ChronoField.MONTH_OF_YEAR) // 月份前不补 0
                .appendLiteral('-')
                .appendValue(ChronoField.DAY_OF_MONTH)
                .appendLiteral(' ')
                .appendValue(ChronoField.HOUR_OF_DAY)
                .appendLiteral(':')
                .appendValue(ChronoField.MINUTE_OF_HOUR)
                .appendLiteral(':')
                .appendValue(ChronoField.SECOND_OF_MINUTE)
                .toFormatter();

        // 格式化 LocalDateTime 对象
        String formattedDateTime = now.format(formatter);

        // 输出格式化后的日期时间
        System.out.println(formattedDateTime);
    }
}
