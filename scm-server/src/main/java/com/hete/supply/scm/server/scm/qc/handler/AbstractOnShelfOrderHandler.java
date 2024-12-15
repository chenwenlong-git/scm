package com.hete.supply.scm.server.scm.qc.handler;

import com.hete.supply.scm.server.scm.qc.service.base.QcOnShelvesOrderBaseService;
import com.hete.supply.scm.server.supplier.entity.dto.OnShelvesOrderCreateResultMqDto;

/**
 * @author yanjiawei
 * @Description 抽象的上架单处理器类，提供了上架单处理的基本结构，具体的上架单处理逻辑可以在子类中实现。
 * @Date 2023/10/25 09:33
 */
public abstract class AbstractOnShelfOrderHandler<T> {

    protected final QcOnShelvesOrderBaseService qcOnShelvesOrderBaseService;

    /**
     * 构造一个 AbstractOnShelfOrderHandler 实例。
     *
     * @param qcOnShelvesOrderBaseService 上架单服务，用于执行上架单的具体操作。
     */
    public AbstractOnShelfOrderHandler(QcOnShelvesOrderBaseService qcOnShelvesOrderBaseService) {
        this.qcOnShelvesOrderBaseService = qcOnShelvesOrderBaseService;
    }

    /**
     * 执行上架单处理之前的前置处理，允许子类实现特定的前期逻辑。
     *
     * @param onShelfOrder 上架单数据，泛型类型 T 表示上架单的数据类型。
     */
    protected abstract void preProcessOnShelfOrder(T onShelfOrder);

    /**
     * 执行上架单处理之后的后置处理，允许子类实现特定的后期逻辑。
     *
     * @param onShelfOrder 上架单数据，泛型类型 T 表示上架单的数据类型。
     */
    protected abstract void postProcessOnShelfOrder(T onShelfOrder);

    /**
     * 处理上架单的方法，包括前置处理、具体处理逻辑和后置处理。
     *
     * @param onShelfOrder 上架单数据，泛型类型 T 表示上架单的数据类型。
     */
    public void processShelfOrder(T onShelfOrder) {
        // 前置处理
        preProcessOnShelfOrder(onShelfOrder);

        // 上架单处理逻辑
        qcOnShelvesOrderBaseService.saveOnShelvesOrderIdempotent((OnShelvesOrderCreateResultMqDto) onShelfOrder);

        // 后置处理
        postProcessOnShelfOrder(onShelfOrder);
    }
}
