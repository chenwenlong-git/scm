package com.hete.supply.scm.server.scm.ibfs.service.base;

import com.hete.supply.scm.server.scm.ibfs.dao.FinanceSettleOrderDao;
import com.hete.supply.scm.server.scm.ibfs.dao.FinanceSettleOrderItemDao;
import com.hete.support.id.service.IdGenerateService;

/**
 * @author yanjiawei
 * Created on 2024/5/23.
 */
public abstract class AbstractFinanceSettleOrderCreator<T, R> {
    protected final FinanceSettleOrderDao financeSettleOrderDao;
    protected final FinanceSettleOrderItemDao financeSettleOrderItemDao;
    protected final IdGenerateService idGenerateService;

    public AbstractFinanceSettleOrderCreator(FinanceSettleOrderDao financeSettleOrderDao,
                                             FinanceSettleOrderItemDao financeSettleOrderItemDao,
                                             IdGenerateService idGenerateService) {
        this.financeSettleOrderDao = financeSettleOrderDao;
        this.financeSettleOrderItemDao = financeSettleOrderItemDao;
        this.idGenerateService = idGenerateService;
    }

    public final void createFinanceSettleOrder(T t) {
        // 创建前置操作
        createPreOperations(t);

        R result = performCreateQcOrder(t);

        // 调用后置操作钩子
        doAfterCreation(result);
    }

    protected abstract void createPreOperations(T input);

    protected abstract R performCreateQcOrder(T input);

    protected abstract void doAfterCreation(R result);
}
