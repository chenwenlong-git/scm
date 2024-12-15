package com.hete.supply.scm.server.scm.builder;

import cn.hutool.core.util.StrUtil;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.process.entity.dto.UpdateBatchCodePriceDto;
import com.hete.supply.scm.server.scm.process.entity.po.BatchCodeCostPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderItemPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderPo;
import com.hete.supply.scm.server.scm.process.entity.po.RepairOrderItemPo;
import com.hete.supply.scm.server.scm.process.enums.CostRelateOrderIdType;
import com.hete.supply.scm.server.scm.process.enums.CostRelatedOrderType;
import com.hete.supply.scm.server.scm.process.enums.CostType;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/1/25.
 */
public class BatchCodeCostBuilder {

    public static BatchCodeCostPo buildBatchCodeCostPo(ProcessOrderPo processOrderPo,
                                                       ProcessOrderItemPo processOrderItemPo,
                                                       CostType costType,
                                                       BigDecimal totalAmount) {
        // 这里是您构建 BatchCodeCostPo 对象的逻辑
        BatchCodeCostPo batchCodeCostPo = new BatchCodeCostPo();

        // 假设 BatchCodeCostPo 有一个 setProcessOrderNo 方法用于设置 processOrderNo
        batchCodeCostPo.setRelateOrderNo(processOrderPo.getProcessOrderNo());
        batchCodeCostPo.setRelateOrderNoType(CostRelatedOrderType.PROCESS_ORDER);
        batchCodeCostPo.setRelateOrderId(processOrderItemPo.getProcessOrderItemId());
        batchCodeCostPo.setRelateOrderIdType(CostRelateOrderIdType.PROCESS_ORDER_ITEM);
        batchCodeCostPo.setSku(processOrderItemPo.getSku());
        batchCodeCostPo.setBatchCode(processOrderItemPo.getSkuBatchCode());
        batchCodeCostPo.setCostType(costType);
        batchCodeCostPo.setTotalAmount(totalAmount);

        // 其他设置属性的逻辑...

        return batchCodeCostPo;
    }


    public static BatchCodeCostPo buildBatchCodeCostPo(String repairOrderNo,
                                                       RepairOrderItemPo repairOrderItemPo,
                                                       CostType costType,
                                                       BigDecimal totalAmount) {
        // 这里是您构建 BatchCodeCostPo 对象的逻辑
        BatchCodeCostPo batchCodeCostPo = new BatchCodeCostPo();

        // 假设 BatchCodeCostPo 有一个 setProcessOrderNo 方法用于设置 processOrderNo
        batchCodeCostPo.setRelateOrderNo(repairOrderNo);
        batchCodeCostPo.setRelateOrderNoType(CostRelatedOrderType.REPAIR_ORDER);
        batchCodeCostPo.setRelateOrderId(repairOrderItemPo.getRepairOrderItemId());
        batchCodeCostPo.setRelateOrderIdType(CostRelateOrderIdType.REPAIR_ORDER_ITEM);
        batchCodeCostPo.setSku(repairOrderItemPo.getSku());
        batchCodeCostPo.setBatchCode(repairOrderItemPo.getBatchCode());
        batchCodeCostPo.setCostType(costType);
        batchCodeCostPo.setTotalAmount(totalAmount);

        // 其他设置属性的逻辑...

        return batchCodeCostPo;
    }

    public static UpdateBatchCodePriceDto buildUpdateBatchCodePriceDto(String batchCode,
                                                                       BigDecimal batchCodeTotalCost) {
        UpdateBatchCodePriceDto dto = new UpdateBatchCodePriceDto();
        UpdateBatchCodePriceDto.UpdateBatchCodeCostPrice updateBatchCodeCostPrice
                = new UpdateBatchCodePriceDto.UpdateBatchCodeCostPrice();
        updateBatchCodeCostPrice.setPrice(batchCodeTotalCost);
        updateBatchCodeCostPrice.setBatchCode(batchCode);
        dto.setBatchCodePriceList(List.of(updateBatchCodeCostPrice));
        dto.setKey(StrUtil.format("{}{}", ScmConstant.UPDATE_BATCH_CODE_COST_PREFIX, batchCode));

        // 可以添加其他设置属性的逻辑...

        return dto;
    }
}
