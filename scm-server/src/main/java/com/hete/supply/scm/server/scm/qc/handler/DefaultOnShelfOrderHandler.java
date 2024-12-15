package com.hete.supply.scm.server.scm.qc.handler;

import com.alibaba.fastjson.JSON;
import com.hete.supply.scm.server.scm.qc.service.base.QcOnShelvesOrderBaseService;
import com.hete.supply.scm.server.supplier.entity.dto.OnShelvesOrderCreateResultMqDto;
import lombok.extern.slf4j.Slf4j;

/**
 * DefaultOnShelfOrderHandler 是处理上架单操作的默认实现类，它继承自 AbstractOnShelfOrderHandler。
 * 该类实现了前置处理和后置处理方法，用于在处理上架单之前和之后执行特定逻辑。
 *
 * @author yanjiawei
 * @date 2023/10/25 09:33
 */
@Slf4j
public class DefaultOnShelfOrderHandler extends AbstractOnShelfOrderHandler<OnShelvesOrderCreateResultMqDto> {

    public DefaultOnShelfOrderHandler(QcOnShelvesOrderBaseService qcOnShelvesOrderBaseService) {
        super(qcOnShelvesOrderBaseService);
    }

    /**
     * 执行上架单处理之前的前置处理。
     * 这里实现了幂等保存上架单质检单相关信息，并打印日志。
     *
     * @param onShelfOrder 上架单数据，泛型类型 T 表示上架单的数据类型。
     */
    @Override
    protected void preProcessOnShelfOrder(OnShelvesOrderCreateResultMqDto onShelfOrder) {
        log.info("WMS非让步上架生成收货单 onShelfOrder:{}", JSON.toJSONString(onShelfOrder));
    }

    /**
     * 执行上架单处理之后的后置处理。
     * 如果需要在上架单处理完成后执行特定的逻辑，可以在这里实现。
     *
     * @param onShelfOrder 上架单数据，泛型类型 T 表示上架单的数据类型。
     */
    @Override
    protected void postProcessOnShelfOrder(OnShelvesOrderCreateResultMqDto onShelfOrder) {
        // TODO 可以在这里添加后置处理逻辑
    }
}
