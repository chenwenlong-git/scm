package com.hete.supply.scm.server.scm.supplier.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;

/**
 * @author yanjiawei
 * Created on 2024/8/24.
 */
public class CustomLocalDateSerializer extends JsonSerializer<LocalDate> {
    // 自定义日期格式化逻辑
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        // 获取年份的后两位
        String year = String.valueOf(value.getYear()).substring(2);
        // 获取月份（不补0）
        int month = value.getMonthValue();
        // 获取日期（不补0）
        int day = value.getDayOfMonth();

        // 格式化为 yy-m-d
        String formattedDate = String.format("%s/%d/%d", year, month, day);
        gen.writeString(formattedDate);
    }
}
