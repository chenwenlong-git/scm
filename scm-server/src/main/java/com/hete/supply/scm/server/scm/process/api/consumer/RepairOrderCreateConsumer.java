package com.hete.supply.scm.server.scm.process.api.consumer;

import com.hete.supply.scm.server.scm.process.entity.dto.CreateRepairOrderMqDto;
import com.hete.supply.scm.server.scm.process.service.biz.RepairOrderBizService;
import com.hete.support.rocketmq.listener.BaseMqListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

/**
 * @author yanjiawei
 * @Description 创建返修单
 * @Date 2023/12/28 09:34
 */
@Component
@RocketMQMessageListener(topic = "topic_plm", selectorExpression = "tag_create_repair_plan", consumerGroup = "scm_create_repair_order_consumer")
@RequiredArgsConstructor
public class RepairOrderCreateConsumer extends BaseMqListener<CreateRepairOrderMqDto> {
    private final RepairOrderBizService repairOrderBizService;

    @SneakyThrows
    @Override
    public void handleMessage(CreateRepairOrderMqDto createRepairOrderMqDto) {

        // 调用 CreateRepairOrderMqDto 对象的 validate 方法，确保数据的有效性
        createRepairOrderMqDto.validate();

        // 调用业务服务的方法，创建维修订单
        repairOrderBizService.createRepairOrder(createRepairOrderMqDto);
    }

}
