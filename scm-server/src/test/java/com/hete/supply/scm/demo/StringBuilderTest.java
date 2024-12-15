package com.hete.supply.scm.demo;/**
 * 1
 *
 * @author yanjiawei
 * Created on 2023/9/3.
 */

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yanjiawei
 * @date 2023年09月03日 23:36
 */
@Slf4j
public class StringBuilderTest {

    public static void logStringBuilderContent(StringBuilder stringBuilder, String bizTrace, int limit) {
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
            System.out.println(StrUtil.format("BizTrace:{} partNo:{}\n{}", bizTrace, partNo.getAndIncrement(), content));
            startIndex = endIndex;
            endIndex = Math.min(startIndex + limit, length);
        }
    }

    public static void main(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("【筛选加工单并排序】产能池编号：capacity-pool-001，是否超额：TRUE，订单类型：[NORMAL, OVERSEAS_REPAIR, LIMITED] 按产能指数从高到低，相同产能指数创建时间最早排序：");
        stringBuilder.append("序号：1.加工单号：JG2307041202 产能指数:150 创建时间:2023-07-04 09:07:32\n");
        stringBuilder.append("序号：2.加工单号：JG2308152226 产能指数:100 创建时间:2023-08-15 09:57:03\n");
        stringBuilder.append("序号：3.加工单号：JG2308260690 产能指数:100 创建时间:2023-08-26 03:49:52\n");
        stringBuilder.append("序号：4.加工单号：JG2308263762 产能指数:100 创建时间:2023-08-26 06:01:50\n");
        stringBuilder.append("序号：5.加工单号：JG2308267858 产能指数:100 创建时间:2023-08-26 06:03:03\n");
        stringBuilder.append("序号：6.加工单号：JG2308261714 产能指数:100 创建时间:2023-08-26 06:05:26\n");
        stringBuilder.append("序号：7.加工单号：JG2308265810 产能指数:100 创建时间:2023-08-26 06:06:29\n");
        stringBuilder.append("序号：8.加工单号：JG2308120690 产能指数:80 创建时间:2023-08-12 02:55:29\n");
        stringBuilder.append("序号：9.加工单号：JG2308148114 产能指数:70 创建时间:2023-08-14 06:43:09\n");
        stringBuilder.append("序号：10.加工单号：JG2308264786 产能指数:70 创建时间:2023-08-26 06:00:24\n");
        logStringBuilderContent(stringBuilder, "abc", 200);
    }
}
