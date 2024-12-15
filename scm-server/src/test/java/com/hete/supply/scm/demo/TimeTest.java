package com.hete.supply.scm.demo;/**
 * test
 *
 * @author yanjiawei
 * Created on 2023/8/28.
 */

import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.support.api.exception.ParamIllegalException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * @author yanjiawei
 * @date 2023年08月28日 09:28
 */
public class TimeTest {
    public static void main(String[] args) {
        // 构建自定义的 DateTimeFormatter
        DateTimeFormatter customFormatter = new DateTimeFormatterBuilder()
                .appendValue(ChronoField.YEAR, 4) // 年
                .appendLiteral('-')
                .appendValue(ChronoField.MONTH_OF_YEAR) // 月，不补零
                .appendLiteral('-')
                .appendValue(ChronoField.DAY_OF_MONTH) // 日，不补零
                .appendLiteral(' ')
                .appendValue(ChronoField.HOUR_OF_DAY, 2) // 时，补零
                .appendLiteral(':')
                .appendValue(ChronoField.MINUTE_OF_HOUR, 2) // 分，补零
                .appendLiteral(':')
                .appendValue(ChronoField.SECOND_OF_MINUTE, 2) // 秒，补零
                .toFormatter();

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 使用自定义的格式化器格式化当前时间
        String formattedDate = now.format(customFormatter);
        System.out.println(formattedDate);
    }

}
