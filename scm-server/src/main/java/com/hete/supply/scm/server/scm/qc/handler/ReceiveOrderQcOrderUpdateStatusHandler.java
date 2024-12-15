package com.hete.supply.scm.server.scm.qc.handler;

import com.alibaba.fastjson.JSON;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.qc.entity.dto.QcOrderChangeDto;
import com.hete.supply.scm.server.scm.qc.enums.QcBizOperate;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.id.service.IdGenerateService;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * ReceiveOrderQcOrderUpdateStatusHandler 用于处理收货质检单更新后状态变化的处理器，继承自 AbstractQcOrderStatusHandler。
 * 该类标记了用于记录日志的 Lombok Log 注解。
 *
 * @author yanjiawei
 * Created on 2023/10/18.
 */
@Slf4j
public class ReceiveOrderQcOrderUpdateStatusHandler extends AbstractQcOrderStatusHandler<QcOrderChangeDto> {

    private final ConsistencySendMqService consistencySendMqService;
    private final IdGenerateService idGenerateService;
    private final QcBizOperate qcBizOperate;

    public ReceiveOrderQcOrderUpdateStatusHandler(ConsistencySendMqService consistencySendMqService,
                                                  IdGenerateService idGenerateService,
                                                  QcBizOperate qcBizOperate) {
        this.consistencySendMqService = consistencySendMqService;
        this.idGenerateService = idGenerateService;
        this.qcBizOperate = qcBizOperate;
    }

    /**
     * 处理收货质检单状态变化后的操作，包括推送相关数据到 WMS。
     *
     * @param qcOrderChangeDto 质检单状态变化的数据
     */
    @Override
    public void handlePostStatusChange(QcOrderChangeDto qcOrderChangeDto) {
        List<QcBizOperate> isResetQcBizOperates = Arrays.asList(QcBizOperate.RESET, QcBizOperate.APPROVE_UN_PASSED);
        qcOrderChangeDto.setIsReset(isResetQcBizOperates.contains(qcBizOperate) ? BooleanType.TRUE : BooleanType.FALSE);

        // 推送MQ到WMS
        qcOrderChangeDto.setKey(idGenerateService.getSnowflakeCode(ScmConstant.QC_UPDATE_PREFIX));
        log.info("质检单状态发生业务逻辑变更，推送消息到仓储服务! qcOrderChangeDto: [{}]", JSON.toJSONString(qcOrderChangeDto));
        consistencySendMqService.execSendMq(QcOrderUpdateSendMqHandler.class, qcOrderChangeDto);
    }
}