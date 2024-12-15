package com.hete.supply.scm.server.scm.process.builder;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.hete.supply.scm.server.scm.entity.dto.ProduceDataAttrValueBySkuDto;
import com.hete.supply.scm.server.scm.entity.dto.ProduceDataAttrValueDto;
import com.hete.supply.scm.server.scm.process.entity.bo.MonthStartWeightAvgPriceResultBo;
import com.hete.supply.scm.server.scm.process.entity.bo.QueryMonthStartWeightPriceBo;
import com.hete.supply.scm.server.scm.process.entity.bo.SkuPriceResultBo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialBackItemPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialReceiptItemPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderMaterialPo;
import com.hete.supply.scm.server.scm.process.entity.po.RepairOrderResultPo;
import com.hete.supply.scm.server.scm.process.entity.vo.RepairMaterialCompositeVo;
import com.hete.supply.scm.server.scm.process.entity.vo.RepairMaterialReceiptVo;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/3/8.
 */
public class ProcOrderMaterialBuilder {

    /**
     * 构建 RepairMaterialCompositeVo 列表
     *
     * @param skuProductNameMap          SKU 到产品名称的映射
     * @param processOrderMaterialPoList 原料信息列表
     * @return 构建的 RepairMaterialCompositeVo 列表
     */
    public static List<RepairMaterialCompositeVo> buildRepairMaterialCompositeVos(Map<String, String> skuProductNameMap,
                                                                                  List<ProcessOrderMaterialPo> processOrderMaterialPoList) {
        if (CollectionUtils.isEmpty(processOrderMaterialPoList)) {
            return Collections.emptyList();
        }
        Map<String, List<ProcessOrderMaterialPo>> groupBySku = processOrderMaterialPoList.stream()
                .collect(Collectors.groupingBy(ProcessOrderMaterialPo::getSku));

        List<RepairMaterialCompositeVo> repairMaterialCompositeVos = Lists.newArrayList();
        groupBySku.forEach((sku, processOrderMaterialPos) -> {
            ProcessOrderMaterialPo oneOfMaterialPo = processOrderMaterialPos.stream()
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(oneOfMaterialPo)) {
                // 创建 RepairMaterialCompositeVo 对象并设置返修单号
                RepairMaterialCompositeVo repairMaterialCompositeVo = new RepairMaterialCompositeVo();
                repairMaterialCompositeVo.setRepairOrderNo(oneOfMaterialPo.getRepairOrderNo());

                // 创建 RepairMaterialVo 对象并设置原料信息
                repairMaterialCompositeVo.setMaterialId(oneOfMaterialPo.getProcessOrderMaterialId());

                repairMaterialCompositeVo.setSku(sku);
                repairMaterialCompositeVo.setSkuEncode(skuProductNameMap.getOrDefault(sku, ""));

                int requiredQuantity = processOrderMaterialPoList.stream()
                        .filter(po -> sku.equals(po.getSku()))
                        .mapToInt(ProcessOrderMaterialPo::getDeliveryNum)
                        .sum();
                repairMaterialCompositeVo.setRequiredQuantity(requiredQuantity);
                repairMaterialCompositeVos.add(repairMaterialCompositeVo);
            }
        });

        return repairMaterialCompositeVos;
    }


    /**
     * 填充 RepairMaterialCompositeVo 中的 RepairMaterialVo 的 RepairMaterialReceiptVos 列表
     *
     * @param repairMaterialCompositeVos       待填充的 RepairMaterialCompositeVo 列表
     * @param processMaterialReceiptItemPoList 原料收货明细列表
     * @param processMaterialBackItemPoList    原料归还明细列表
     * @param repairOrderResultPoList          返修结果列表
     */
    public static void fillRepairMaterialReceiptVos(List<RepairMaterialCompositeVo> repairMaterialCompositeVos,
                                                    List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPoList,
                                                    List<ProcessMaterialBackItemPo> processMaterialBackItemPoList,
                                                    List<RepairOrderResultPo> repairOrderResultPoList) {
        for (RepairMaterialCompositeVo repairMaterialCompositeVo : repairMaterialCompositeVos) {
            String sku = repairMaterialCompositeVo.getSku();
            List<ProcessMaterialReceiptItemPo> matchReceiptItems = processMaterialReceiptItemPoList.stream()
                    .filter(materialReceiptItem -> Objects.equals(sku, materialReceiptItem.getSku()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(matchReceiptItems)) {
                // 构建 RepairMaterialReceiptVos 列表
                List<RepairMaterialReceiptVo> repairMaterialReceiptVos = buildRepairMaterialReceiptVos(
                        matchReceiptItems, processMaterialBackItemPoList, repairOrderResultPoList);

                // 将 RepairMaterialReceiptVos 列表设置到 RepairMaterialVo 中
                repairMaterialCompositeVo.setRepairMaterialReceiptVos(repairMaterialReceiptVos);
            }
        }
    }


    private static List<RepairMaterialReceiptVo> buildRepairMaterialReceiptVos(List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPoList,
                                                                               List<ProcessMaterialBackItemPo> processMaterialBackItemPoList,
                                                                               List<RepairOrderResultPo> repairOrderResultPoList) {
        return processMaterialReceiptItemPoList.stream()
                .map(processMaterialReceiptItemPo -> {
                    String materialBatchCode = processMaterialReceiptItemPo.getSkuBatchCode();
                    Integer receiptQuantity = processMaterialReceiptItemPo.getReceiptNum();

                    RepairMaterialReceiptVo repairMaterialReceiptVo = new RepairMaterialReceiptVo();
                    repairMaterialReceiptVo.setBatchCode(materialBatchCode);
                    repairMaterialReceiptVo.setReceiptQuantity(receiptQuantity);
                    repairMaterialReceiptVo.setOutQuantity(processMaterialReceiptItemPo.getDeliveryNum());

                    // 计算加工绑定数量
                    int processedBindingQuantity = repairOrderResultPoList.stream()
                            .filter(repairOrderResultPo -> Objects.equals(materialBatchCode,
                                    repairOrderResultPo.getMaterialBatchCode()))
                            .mapToInt(RepairOrderResultPo::getMaterialUsageQuantity)
                            .sum();
                    repairMaterialReceiptVo.setProcessedBindingQuantity(processedBindingQuantity);

                    int returnedQuantity = processMaterialBackItemPoList.stream()
                            .filter(processMaterialBackItemPo -> Objects.equals(materialBatchCode,
                                    processMaterialBackItemPo.getSkuBatchCode()))
                            .mapToInt(ProcessMaterialBackItemPo::getDeliveryNum)
                            .sum();
                    repairMaterialReceiptVo.setReturnedQuantity(returnedQuantity);
                    repairMaterialReceiptVo.setReturnableQuantity(
                            Math.max(receiptQuantity - processedBindingQuantity - returnedQuantity, 0));

                    // 计算质检次品数量
                    int qualityInspectionDefectQuantity = repairOrderResultPoList.stream()
                            .filter(repairOrderResultPo -> Objects.equals(materialBatchCode,
                                    repairOrderResultPo.getMaterialBatchCode()))
                            .mapToInt(RepairOrderResultPo::getQcFailQuantity)
                            .sum();
                    repairMaterialReceiptVo.setQualityInspectionDefectQuantity(qualityInspectionDefectQuantity);

                    return repairMaterialReceiptVo;
                })
                .collect(Collectors.toList());
    }

    public static List<MonthStartWeightAvgPriceResultBo> buildMonthStartWeightAvgPriceResultBos(List<QueryMonthStartWeightPriceBo> queryMonthStartWeightPriceBos) {
        return queryMonthStartWeightPriceBos.stream()
                .map(queryMonthStartWeightPriceBo -> {
                    MonthStartWeightAvgPriceResultBo bo = new MonthStartWeightAvgPriceResultBo();
                    bo.setSku(queryMonthStartWeightPriceBo.getSku());
                    return bo;
                })
                .collect(Collectors.toList());
    }

    public static List<QueryMonthStartWeightPriceBo> buildQueryMonthStartWeightPriceBos(List<SkuPriceResultBo> filterZeroPriceBos) {
        if (CollectionUtils.isEmpty(filterZeroPriceBos)) {
            return Collections.emptyList();
        }

        return filterZeroPriceBos.stream()
                .map(filterZeroPriceBo -> {
                    QueryMonthStartWeightPriceBo queryBo = new QueryMonthStartWeightPriceBo();
                    queryBo.setSku(filterZeroPriceBo.getSku());
                    return queryBo;
                })
                .collect(Collectors.toList());
    }

    public static ProduceDataAttrValueBySkuDto buildProduceDataAttrValueBySkuDto(String sku,
                                                                                 List<Long> attributeNameIds) {
        ProduceDataAttrValueBySkuDto dto = new ProduceDataAttrValueBySkuDto();
        dto.setSku(sku);
        dto.setAttributeNameIdList(attributeNameIds);
        return dto;
    }

    public static ProduceDataAttrValueDto buildProduceDataAttrValueDto(Long laceAreaCategoryId,
                                                                       String laceArea) {
        ProduceDataAttrValueDto produceDataAttrValueDto = new ProduceDataAttrValueDto();
        produceDataAttrValueDto.setAttributeNameId(laceAreaCategoryId);
        produceDataAttrValueDto.setAttrValue(laceArea);
        return produceDataAttrValueDto;
    }
}
