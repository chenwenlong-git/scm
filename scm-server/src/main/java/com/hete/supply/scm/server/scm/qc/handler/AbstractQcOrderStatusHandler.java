package com.hete.supply.scm.server.scm.qc.handler;

/**
 * 抽象的质检单状态处理器类，提供了质检单状态变更后的后置处理操作。
 *
 * @param <T> 处理实体的类型，表示状态变更操作的处理实体类型。
 * @author yanjiawei
 * @date 2023/10/25 09:33
 */
public abstract class AbstractQcOrderStatusHandler<T> {

    /**
     * 后置处理方法，用于处理状态变更后的操作。
     *
     * @param t 处理实体，表示状态变更操作的处理实体。
     */
    public abstract void handlePostStatusChange(T t);
}
