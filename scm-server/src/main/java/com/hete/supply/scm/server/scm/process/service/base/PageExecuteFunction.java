package com.hete.supply.scm.server.scm.process.service.base;

import java.util.List;

/**
 * 定义一个函数式接口，用于处理分页查询结果。
 *
 * @author yanjiawei
 * Created on 2024/4/11.
 */
@FunctionalInterface
public interface PageExecuteFunction<T> {

    /**
     * 处理分页查询结果。
     *
     * @param records 查询结果列表。
     */
    void doExecute(List<T> records);
}
