package com.hete.supply.scm.server.scm.qc.handler;

import com.alibaba.fastjson.JSON;
import com.hete.supply.scm.server.scm.defect.service.biz.DefectBizService;
import com.hete.supply.scm.server.scm.qc.service.base.QcOnShelvesOrderBaseService;
import com.hete.supply.scm.server.supplier.entity.dto.OnShelvesOrderCreateResultMqDto;
import lombok.extern.slf4j.Slf4j;

/**
 * DefectHandlingOnShelfOrderHandler 是处理让步上架单操作的实现类，它继承自 AbstractOnShelfOrderHandler。
 * 该类实现了前置处理和后置处理方法，用于在处理让步上架单之前和之后执行特定逻辑。
 * 为了适应让步上架单的需求，它提供了额外的处理逻辑，包括更新缺陷信息。
 *
 * @author yanjiawei
 * @date 2023/10/25 09:33
 */
@Slf4j
public class DefectHandlingOnShelfOrderHandler extends AbstractOnShelfOrderHandler<OnShelvesOrderCreateResultMqDto> {

    private final DefectBizService defectBizService;

    /**
     * 构造函数用于注入依赖的服务。
     *
     * @param qcOnShelvesOrderBaseService 上架单服务
     * @param defectBizService            缺陷信息服务
     */
    public DefectHandlingOnShelfOrderHandler(QcOnShelvesOrderBaseService qcOnShelvesOrderBaseService,
                                             DefectBizService defectBizService) {
        super(qcOnShelvesOrderBaseService);
        this.defectBizService = defectBizService;
    }

    /**
     * 执行让步上架单处理之前的前置处理。
     * 这里实现了幂等保存上架单质检单相关信息，并打印日志。
     *
     * @param onShelfOrder 上架单数据，泛型类型 T 表示上架单的数据类型。
     */
    @Override
    protected void preProcessOnShelfOrder(OnShelvesOrderCreateResultMqDto onShelfOrder) {
        log.info("WMS让步上架生成收货单 onShelfOrder:{}", JSON.toJSONString(onShelfOrder));
    }

    /**
     * 执行让步上架单处理之后的后置处理。
     * 该方法在处理让步上架单完成后会更新缺陷信息。
     *
     * @param onShelfOrder 上架单数据，泛型类型 T 表示上架单的数据类型。
     */
    @Override
    protected void postProcessOnShelfOrder(OnShelvesOrderCreateResultMqDto onShelfOrder) {
        defectBizService.updateDefectCompromise(onShelfOrder);
    }
}
