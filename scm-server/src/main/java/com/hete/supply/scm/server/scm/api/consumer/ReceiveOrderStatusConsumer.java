package com.hete.supply.scm.server.scm.api.consumer;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.api.scm.entity.enums.ReceiveType;
import com.hete.supply.scm.api.scm.entity.enums.ReceiveTypeAttribute;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.defect.service.base.DefectBaseService;
import com.hete.supply.scm.server.scm.develop.service.biz.DevelopSampleOrderBizService;
import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderChangeMqDto;
import com.hete.supply.scm.server.scm.enums.wms.ScmBizReceiveOrderType;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderBizService;
import com.hete.supply.scm.server.scm.process.service.biz.RepairOrderBizService;
import com.hete.supply.scm.server.scm.sample.service.biz.SampleBizService;
import com.hete.supply.scm.server.supplier.settle.service.biz.SupplierDeliverBizService;
import com.hete.support.rocketmq.listener.BaseMqListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * wms 收货单状态变化消费
 *
 * @author RockyHuas
 * @date 2022/12/06 14:16
 */
@Component
@RocketMQMessageListener(topic = "topic_wms", selectorExpression = "tag_receive_state_change", consumerGroup = "scm_tag_receive_state_change_consumer")
@RequiredArgsConstructor
@Slf4j
public class ReceiveOrderStatusConsumer extends BaseMqListener<ReceiveOrderChangeMqDto> {
    private final ProcessOrderBizService processOrderBizService;
    private final SupplierDeliverBizService supplierDeliverBizService;
    private final SampleBizService sampleBizService;
    private final DefectBaseService defectBaseService;
    private final DevelopSampleOrderBizService developSampleOrderBizService;
    private final RepairOrderBizService repairOrderBizService;

    @SneakyThrows
    @Override
    public void handleMessage(ReceiveOrderChangeMqDto message) {
        log.info("WMS推送给SCM收货数据：{}", message);
        ReceiveType receiveType = message.getReceiveType();
        ReceiveTypeAttribute receiveTypeAttribute = message.getReceiveTypeAttribute();
        String type = null;
        String unionKey = message.getUnionKey();
        if (StringUtils.isNotBlank(unionKey)) {
            String[] keyArray = unionKey.split(ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK);
            if (keyArray.length > 1) {
                type = keyArray[1];
            }
        }
        if (ReceiveType.PROCESS_PRODUCT.equals(receiveType)) {
            if (Objects.equals(ReceiveTypeAttribute.PROCESS_PRODUCT_REPAIR, receiveTypeAttribute)) {
                repairOrderBizService.handleRepairReceiptStatusChange(message);
            } else {
                processOrderBizService.syncReceiptStatus(message);
            }
        } else if (ReceiveType.PROCESS_MATERIAL.equals(receiveType)) {
            //兼容旧数据
            if (StringUtils.isBlank(type)) {
                processOrderBizService.syncMaterialReceiptMsg(message);
            }
            //新数据时必须含有对应业务标记
            if (StringUtils.isNotBlank(type) && ScmBizReceiveOrderType.PROCESS_RAW.name().equals(type)) {
                processOrderBizService.syncMaterialReceiptMsg(message);
            }
        } else if (ReceiveType.DEFECTIVE_PROCESS_PRODUCT.equals(receiveType)) {
            final List<String> processScmBizTypes
                    = Arrays.asList(ScmBizReceiveOrderType.PROCESS_DEFECT_RECORD.name(),
                    ScmBizReceiveOrderType.INSIDE_CHECK.name(),
                    ScmBizReceiveOrderType.MATERIAL_DEFECT.name());
            List<String> repairOrderScmBizTypes
                    = List.of(ScmBizReceiveOrderType.REPAIR_RAW.name());

            if (processScmBizTypes.contains(type)) {
                defectBaseService.syncReceiptMsg(message);
            } else if (repairOrderScmBizTypes.contains(type)) {
                repairOrderBizService.syncMaterialReceiptMsg(message);
            } else {
                processOrderBizService.syncDefectiveReceiptMsg(message);
            }
        } else if (ReceiveType.BULK.equals(receiveType)) {
            supplierDeliverBizService.syncReceiptStatus(message);
        } else if (ReceiveType.SAMPLE.equals(receiveType)) {
            if (ScmBizReceiveOrderType.DEVELOP_SEAL_SAMPLE.name().equals(type)) {
                developSampleOrderBizService.syncReceiptMsg(message);
            } else {
                sampleBizService.syncReceiptMsg(message);
            }
        } else if (ReceiveType.DOWN_RANK.equals(receiveType)) {
            supplierDeliverBizService.syncModifyReceiptStatus(message);
        } else if (ReceiveType.CHANGE_GOODS.equals(receiveType)) {
            defectBaseService.syncReceiptMsg(message);
        } else if (ReceiveType.FAST_SALE.equals(receiveType)) {
            developSampleOrderBizService.syncReceiptMsg(message);
        } else {
            log.warn("收货单状态变化消费,同步业务类型异常,param:{}", message);
        }
    }
}
