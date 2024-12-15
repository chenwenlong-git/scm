package com.hete.supply.scm.server.scm.process.service.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * @author yanjiawei
 * Created on 2024/4/11.
 */
@Slf4j
public class PageExecutor<T> {

    /**
     * 默认当前页数
     */
    private static final int DEFAULT_CURRENT_PAGE = 1;

    /**
     * 默认分页大小
     */
    private static final int DEFAULT_PAGE_SIZE = 500;

    /**
     * 默认最大迭代次数
     */
    private static final int DEFAULT_MAX_ITERATIONS = 400;

    /**
     * 执行分页查询，并对每一页的结果进行处理。
     *
     * @param queryFunction   分页查询方法，用于执行查询并返回查询结果。
     * @param executeFunction 分页结果处理方法，对查询结果进行处理。
     */
    public void doForPage(PageQueryFunction<T> queryFunction, PageExecuteFunction<T> executeFunction) {
        doForPage(DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE, DEFAULT_MAX_ITERATIONS, queryFunction, executeFunction);
    }

    /**
     * 执行分页查询，并对每一页的结果进行处理。
     *
     * @param maxIterations   最大迭代次数，限制分页查询的最大次数。
     * @param queryFunction   分页查询方法，用于执行查询并返回查询结果。
     * @param executeFunction 分页结果处理方法，对查询结果进行处理。
     */
    public void doForPage(int maxIterations,
                          PageQueryFunction<T> queryFunction,
                          PageExecuteFunction<T> executeFunction) {
        doForPage(DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE, maxIterations, queryFunction, executeFunction);
    }

    /**
     * 执行分页查询，并对每一页的结果进行处理。
     *
     * @param currentPage     当前页数，从1开始。
     * @param pageSize        每页大小，限制每页查询的记录数。
     * @param maxIterations   最大迭代次数，限制分页查询的最大次数。
     * @param queryFunction   分页查询方法，用于执行查询并返回查询结果。
     * @param executeFunction 分页结果处理方法，对查询结果进行处理。
     */
    public void doForPage(int currentPage,
                          int pageSize,
                          int maxIterations,
                          PageQueryFunction<T> queryFunction, PageExecuteFunction<T> executeFunction) {
        while (currentPage <= maxIterations) {
            // 执行查询方法并获取结果
            IPage<T> pageData = queryFunction.doQuery(new Page<>(currentPage, pageSize));

            List<T> records = pageData.getRecords();
            if (CollectionUtils.isEmpty(records)) {
                log.info("分页执行器结束！无分页数据。");
                return;
            }

            // 处理查询结果
            executeFunction.doExecute(records);

            // 增加当前页数
            currentPage++;
        }
    }
}
