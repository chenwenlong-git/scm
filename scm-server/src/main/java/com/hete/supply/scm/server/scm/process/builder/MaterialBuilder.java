package com.hete.supply.scm.server.scm.process.builder;

import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.server.scm.process.entity.bo.SkuPriceQueryBo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialReceiptItemPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialReceiptPo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/3/1.
 */
public class MaterialBuilder {

    public static List<SkuPriceQueryBo> buildSkuPriceQueryBos(List<ProcessMaterialReceiptPo> processMaterialReceiptPos,
                                                              List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPos) {
        return processMaterialReceiptItemPos.stream()
                .map(processMaterialReceiptItemPo -> {
                    SkuPriceQueryBo skuPriceQueryBo = new SkuPriceQueryBo();
                    skuPriceQueryBo.setSku(processMaterialReceiptItemPo.getSku());
                    skuPriceQueryBo.setBatchCode(processMaterialReceiptItemPo.getSkuBatchCode());

                    Long processMaterialReceiptId = processMaterialReceiptItemPo.getProcessMaterialReceiptId();
                    String materialDeliveryWarehouseCode = ParamValidUtils.requireNotNull(
                            processMaterialReceiptPos.stream()
                                    .filter(processMaterialReceiptPo -> Objects.equals(
                                            processMaterialReceiptId,
                                            processMaterialReceiptPo.getProcessMaterialReceiptId()))
                                    .map(ProcessMaterialReceiptPo::getDeliveryWarehouseCode)
                                    .findFirst()
                                    .orElse(null), "获取原料成本信息异常！原料发货仓库编码不存在。");
                    skuPriceQueryBo.setMaterialDeliveryWarehouseCode(materialDeliveryWarehouseCode);
                    return skuPriceQueryBo;
                })
                .collect(Collectors.toList());
    }
}
