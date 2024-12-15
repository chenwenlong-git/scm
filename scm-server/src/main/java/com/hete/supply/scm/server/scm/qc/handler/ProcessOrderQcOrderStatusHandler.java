package com.hete.supply.scm.server.scm.qc.handler;

import com.hete.supply.scm.api.scm.entity.enums.QcState;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderBizService;
import com.hete.supply.scm.server.scm.qc.entity.bo.ProcessOrderQcOrderFinishedBo;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * ProcessOrderQcOrderStatusHandler 是质检单状态变更的处理类，用于处理质检单状态变更后的相关操作。
 * 这个类继承自 AbstractQcOrderStatusHandler 并实现了其中的抽象方法来处理质检单状态的变更。
 * 它负责处理加工单的质检状态完成后的操作，例如标记质检完成等。
 *
 * @author yanjiawei
 * @date 2023/10/25 09:33
 */
@Slf4j
public class ProcessOrderQcOrderStatusHandler extends AbstractQcOrderStatusHandler<ProcessOrderQcOrderFinishedBo> {
    private final ProcessOrderBizService processOrderBizService;
    private final QcState qcState;

    /**
     * 构造一个 ProcessOrderQcOrderStatusHandler 的实例。
     *
     * @param processOrderBizService 处理加工单业务逻辑的服务
     */
    public ProcessOrderQcOrderStatusHandler(ProcessOrderBizService processOrderBizService,
                                            QcState qcState) {
        this.processOrderBizService = processOrderBizService;
        this.qcState = qcState;
    }

    /**
     * 处理质检单状态变更后的操作，标记质检完成。
     *
     * @param qcOrderChangeBo 质检单状态变更信息
     */
    @Override
    public void handlePostStatusChange(ProcessOrderQcOrderFinishedBo qcOrderChangeBo) {
        if (!Objects.equals(QcState.FINISHED, qcState)) {
            return;
        }
        // 加工质检后置操作
        processOrderBizService.completeQc(qcOrderChangeBo);
    }
}

