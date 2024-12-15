package com.hete.supply.scm.server.scm.process.service.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 定义一个函数式接口，用于执行分页查询并返回查询结果。
 *
 * @author yanjiawei
 * Created on 2024/4/11.
 */
@FunctionalInterface
public interface PageQueryFunction<T> {

    /**
     * 执行分页查询并返回查询结果。
     *
     * @param page 分页对象，包含当前页数和每页大小信息。
     * @return 查询结果的分页对象。
     */
    IPage<T> doQuery(Page<T> page);
}
