package com.hete.supply.scm.server.scm.qc.handler;

import com.alibaba.fastjson.JSON;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.qc.entity.dto.QcOrderChangeDto;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.id.service.IdGenerateService;
import lombok.extern.slf4j.Slf4j;

/**
 * ReceiveOrderQcOrderStatusHandler 用于处理收货质检单创建状态变化的处理器，继承自 AbstractQcOrderStatusHandler。
 * 该类标记了用于记录日志的 Lombok Log 注解。
 *
 * @author yanjiawei
 * Created on 2023/10/18.
 */
@Slf4j
public class ReceiveOrderQcOrderCreateStatusHandler extends AbstractQcOrderStatusHandler<QcOrderChangeDto> {

    private final ConsistencySendMqService consistencySendMqService;
    private final IdGenerateService idGenerateService;

    public ReceiveOrderQcOrderCreateStatusHandler(ConsistencySendMqService consistencySendMqService,
                                                  IdGenerateService idGenerateService) {
        this.consistencySendMqService = consistencySendMqService;
        this.idGenerateService = idGenerateService;
    }

    /**
     * 处理收货质检单状态变化后的操作，包括推送相关数据到 WMS。
     *
     * @param qcOrderChangeDto 质检单状态变化的数据
     */
    @Override
    public void handlePostStatusChange(QcOrderChangeDto qcOrderChangeDto) {
        // 推送MQ到WMS
        qcOrderChangeDto.setKey(idGenerateService.getSnowflakeCode(ScmConstant.QC_UPDATE_PREFIX));
        log.info("创建质检单，推送消息到仓储服务! qcOrderChangeDto: [{}]", JSON.toJSONString(qcOrderChangeDto));
        consistencySendMqService.execSendMq(QcOrderUpdateSendMqHandler.class, qcOrderChangeDto);
    }
}

