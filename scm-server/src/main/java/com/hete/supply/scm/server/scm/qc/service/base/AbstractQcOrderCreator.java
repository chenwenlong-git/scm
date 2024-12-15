package com.hete.supply.scm.server.scm.qc.service.base;

import com.hete.supply.scm.server.scm.qc.dao.QcDetailDao;
import com.hete.supply.scm.server.scm.qc.dao.QcOrderDao;
import com.hete.support.id.service.IdGenerateService;

/**
 * @param <T> 入参类型，表示质检单创建操作的输入参数类型。
 * @param <R> 质检单创建结果类型，表示创建质检单后的返回结果。
 * @author yanjiawei
 * @Description 抽象的质检单创建器类，提供了创建质检单的基本结构。
 * @Date 2023/10/25 09:35
 */
public abstract class AbstractQcOrderCreator<T, R> {

    protected final QcOrderDao qcOrderDao;
    protected final QcDetailDao qcDetailDao;
    protected final IdGenerateService idGenerateService;

    /**
     * 构造一个 AbstractQcOrderCreator 实例。
     *
     * @param qcOrderDao        质检单数据访问对象，用于操作质检单数据。
     * @param qcDetailDao       质检明细数据访问对象，用于操作质检明细数据。
     * @param idGenerateService ID生成服务，用于生成唯一标识符。
     */
    public AbstractQcOrderCreator(QcOrderDao qcOrderDao,
                                  QcDetailDao qcDetailDao,
                                  IdGenerateService idGenerateService) {
        this.qcOrderDao = qcOrderDao;
        this.qcDetailDao = qcDetailDao;
        this.idGenerateService = idGenerateService;
    }

    /**
     * 创建质检单
     *
     * @param t 入参类型
     * @return R 质检单创建结果
     */
    public final void createQcOrder(T t) {
        // 创建前置操作
        createPreOperations(t);

        R result = performCreateQcOrder(t);

        // 调用后置操作钩子
        doAfterCreation(result);
    }

    /**
     * 创建质检单前置操作的抽象方法，用于执行特定的前期逻辑。
     *
     * @param input 入参类型，表示质检单创建操作的输入参数。
     */
    protected abstract void createPreOperations(T input);

    /**
     * 执行质检单创建操作的抽象方法，用于实现具体的质检单创建逻辑。
     *
     * @param input 入参类型，表示质检单创建操作的输入参数。
     * @return R 质检单创建结果。
     */
    protected abstract R performCreateQcOrder(T input);

    /**
     * 后置操作钩子方法，用于执行特定的后期逻辑。
     *
     * @param result 质检单创建结果。
     */
    protected abstract void doAfterCreation(R result);

}

