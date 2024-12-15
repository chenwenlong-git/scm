package com.hete.supply.scm.server.scm.process.api.consumer;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.ProcessMaterialReceiptCreateDto;
import com.hete.supply.scm.api.scm.entity.enums.DeliveryTypeAttribute;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessMaterialReceiptCreateMqDto;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessMaterialReceiptBizService;
import com.hete.support.api.exception.BizException;
import com.hete.support.rocketmq.listener.BaseMqListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 加工原料完成出库消费
 *
 * @author RockyHuas
 * @date 2022/12/06 17:16
 */
@Component
@RocketMQMessageListener(topic = "topic_wms", selectorExpression = "tag_process_material_delivered", consumerGroup = "scm_tag_process_material_delivered_consumer")
@RequiredArgsConstructor
public class ProcessMaterialDeliveredConsumer extends BaseMqListener<ProcessMaterialReceiptCreateMqDto> {
    private final ProcessMaterialReceiptBizService processMaterialReceiptBizService;

    @SneakyThrows
    @Override
    public void handleMessage(ProcessMaterialReceiptCreateMqDto message) {
        ProcessMaterialReceiptCreateDto processMaterialReceiptCreateDto = new ProcessMaterialReceiptCreateDto();
        final boolean isProcessOrder
                = Objects.isNull(message.getDeliveryTypeAttribute()) ||
                Objects.equals(DeliveryTypeAttribute.NORMAL_PROCESS, message.getDeliveryTypeAttribute());
        if (isProcessOrder) {
            processMaterialReceiptCreateDto.setProcessOrderNo(message.getProcessOrderNo());
        } else {
            processMaterialReceiptCreateDto.setRepairOrderNo(message.getProcessOrderNo());
        }
        processMaterialReceiptCreateDto.setDeliveryTypeAttribute(message.getDeliveryTypeAttribute());
        processMaterialReceiptCreateDto.setDeliveryNo(message.getDeliveryNo());
        processMaterialReceiptCreateDto.setDeliveryUser(message.getDeliveryUser());
        processMaterialReceiptCreateDto.setDeliveryUsername(message.getDeliveryUsername());
        processMaterialReceiptCreateDto.setDeliveryNum(message.getDeliveryNum());
        processMaterialReceiptCreateDto.setDeliveryTime(message.getDeliveryTime());
        processMaterialReceiptCreateDto.setDeliveryWarehouseCode(message.getDeliveryWarehouseCode());
        processMaterialReceiptCreateDto.setDeliveryWarehouseName(message.getDeliveryWarehouseName());

        List<ProcessMaterialReceiptCreateMqDto.MaterialReceiptItem> materialReceiptItems = message.getMaterialReceiptItems();
        if (CollectionUtils.isEmpty(materialReceiptItems)) {
            throw new BizException("加工原料收货详情不能为空");
        }
        Map<String, List<ProcessMaterialReceiptCreateMqDto.MaterialReceiptItem>> groupedProducts = materialReceiptItems.stream().collect(Collectors.groupingBy(ProcessMaterialReceiptCreateMqDto.MaterialReceiptItem::getSkuBatchCode));

        // 获取所有的批次码
        List<String> batchCodes = materialReceiptItems.stream().map(ProcessMaterialReceiptCreateMqDto.MaterialReceiptItem::getSkuBatchCode)
                .distinct().collect(Collectors.toList());
        List<ProcessMaterialReceiptCreateDto.MaterialReceiptItem> newMaterialReceiptItems = batchCodes.stream().map(item -> {
            List<ProcessMaterialReceiptCreateMqDto.MaterialReceiptItem> deliveryProducts = groupedProducts.get(item);
            Optional<ProcessMaterialReceiptCreateMqDto.MaterialReceiptItem> firstDeliveryProductOptional = deliveryProducts.stream().findFirst();
            int totalDeliveryNum = deliveryProducts.stream().mapToInt(ProcessMaterialReceiptCreateMqDto.MaterialReceiptItem::getDeliveryNum).sum();
            ProcessMaterialReceiptCreateDto.MaterialReceiptItem materialReceiptItem = new ProcessMaterialReceiptCreateDto.MaterialReceiptItem();
            firstDeliveryProductOptional.ifPresent(deliveryProduct -> materialReceiptItem.setSku(deliveryProduct.getSku()));
            materialReceiptItem.setSkuBatchCode(item);
            materialReceiptItem.setDeliveryNum(totalDeliveryNum);
            return materialReceiptItem;
        }).collect(Collectors.toList());
        processMaterialReceiptCreateDto.setMaterialReceiptItems(newMaterialReceiptItems);
        if (isProcessOrder) {
            processMaterialReceiptBizService.create(processMaterialReceiptCreateDto);
        } else {
            processMaterialReceiptBizService.createRepairMaterialReceipt(processMaterialReceiptCreateDto);
        }
    }
}
