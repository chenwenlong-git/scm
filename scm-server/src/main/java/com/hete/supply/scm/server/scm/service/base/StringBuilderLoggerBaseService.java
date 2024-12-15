package com.hete.supply.scm.server.scm.service.base;/**
 * 日志输出类
 *
 * @author yanjiawei
 * Created on 2023/9/3.
 */

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yanjiawei
 * @date 2023年09月03日 23:54
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class StringBuilderLoggerBaseService {

    @Autowired
    private Environment environment;

    /**
     * @Description 根据字符限制输出stringBuilder内容到log
     * @author yanjiawei
     * @Date 2023/9/4 00:33
     */
    public void logStringBuilderContent(StringBuilder stringBuilder, String bizTrace) {
        Integer limit = environment.getProperty("stringBuilderLogLimit", Integer.class);
        if (Objects.isNull(limit)) {
            limit = 500;
        }

        if (stringBuilder == null || limit <= 0) {
            // 如果传入的参数不合法，不执行任何操作
            return;
        }

        int length = stringBuilder.length();
        if (length == 0) {
            // 如果StringBuilder内容为空，不执行任何操作
            return;
        }

        int startIndex = 0;
        int endIndex = Math.min(limit, length);
        AtomicInteger partNo = new AtomicInteger(1);

        while (startIndex < length) {
            String content = stringBuilder.substring(startIndex, endIndex);
            log.info(StrUtil.format("BizTrace:{} partNo:{}\n{}", bizTrace, partNo.getAndIncrement(), content));
            startIndex = endIndex;
            endIndex = Math.min(startIndex + limit, length);
        }
    }
}
