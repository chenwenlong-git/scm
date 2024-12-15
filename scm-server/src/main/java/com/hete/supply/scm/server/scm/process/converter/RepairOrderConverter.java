package com.hete.supply.scm.server.scm.process.converter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.process.builder.ProcOrderMaterialBuilder;
import com.hete.supply.scm.server.scm.process.entity.bo.RepairMaterialBo;
import com.hete.supply.scm.server.scm.process.entity.bo.RepairOrderBindingInfoBo;
import com.hete.supply.scm.server.scm.process.entity.bo.RepairOrderItemBo;
import com.hete.supply.scm.server.scm.process.entity.bo.SplitPlanOrderResultBo;
import com.hete.supply.scm.server.scm.process.entity.dto.CreateRepairOrderMqDto;
import com.hete.supply.scm.server.scm.process.entity.po.*;
import com.hete.supply.scm.server.scm.process.entity.vo.*;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.supply.wms.api.basic.entity.vo.WarehouseVo;
import com.hete.supply.wms.api.entry.entity.vo.ReceiveOrderForScmVo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2023/12/28.
 */
public class RepairOrderConverter {

    public static RepairOrderItemBo convertRepairOrderItem(Map<String, Integer> originalItem) {
        RepairOrderItemBo repairOrderItemBo = new RepairOrderItemBo();
        for (Map.Entry<String, Integer> entry : originalItem.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            repairOrderItemBo.setSku(key);
            repairOrderItemBo.setExpectProcessNum(value);
        }
        return repairOrderItemBo;
    }


    public static void converterToRepairItemOrderPos(List<SplitPlanOrderResultBo> splitResults) {

    }

    public static void converterToRepairMaterialsPos(List<SplitPlanOrderResultBo> splitResults) {

    }

    public static RepairOrderPo convert(RepairOrderPo source) {
        if (source == null) {
            return null;
        }

        RepairOrderPo target = new RepairOrderPo();
        target.setExpectProcessNum(source.getExpectProcessNum());
        target.setExpectCompleteProcessTime(source.getExpectCompleteProcessTime());
        target.setIsReceiveMaterial(source.getIsReceiveMaterial());
        target.setMissingInformation(source.getMissingInformation());
        target.setRepairOrderStatus(source.getRepairOrderStatus());
        target.setConfirmCompleteUser(source.getConfirmCompleteUser());
        target.setConfirmCompleteUsername(source.getConfirmCompleteUsername());
        target.setConfirmCompleteTime(source.getConfirmCompleteTime());
        target.setDeliveryNum(source.getDeliveryNum());
        target.setExpectWarehouseCode(source.getExpectWarehouseCode());
        target.setExpectWarehouseName(source.getExpectWarehouseName());
        target.setReceiveOrderNo(source.getReceiveOrderNo());
        target.setPlanNo(source.getPlanNo());
        target.setPlanType(source.getPlanType());
        target.setPlanTitle(source.getPlanTitle());
        target.setPlatform(source.getPlatform());
        target.setPlanCreateUser(source.getPlanCreateUser());
        target.setPlanCreateUsername(source.getPlanCreateUsername());
        target.setPlanCreateTime(source.getPlanCreateTime());
        target.setSalePrice(source.getSalePrice());
        target.setPlanRemark(source.getPlanRemark());
        // 其他属性...

        return target;
    }

    public static List<RepairMaterialBo> toCreateRepairMaterialBo(List<CreateRepairOrderMqDto.RepairMaterialItem> repairMaterialList,
                                                                  List<Map<String, Integer>> inputList) {
        List<RepairMaterialBo> outputList = new ArrayList<>();

        // 遍历内部 List<Map<String, Integer>>
        for (Map<String, Integer> map : inputList) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();

                CreateRepairOrderMqDto.RepairMaterialItem matchMaterialItem = repairMaterialList.stream()
                        .filter(repairMaterialItem -> Objects.equals(key, repairMaterialItem.getUniqueCode()))
                        .findFirst()
                        .orElse(null);
                if (Objects.nonNull(matchMaterialItem)) {
                    RepairMaterialBo repairMaterialBo = new RepairMaterialBo();
                    repairMaterialBo.setSku(matchMaterialItem.getSkuCode());
                    repairMaterialBo.setWarehouseCode(matchMaterialItem.getWarehouseCode());
                    repairMaterialBo.setShelfCode(matchMaterialItem.getWarehouseLocationCode());
                    repairMaterialBo.setProductQuality(matchMaterialItem.getProductQuality());
                    repairMaterialBo.setDeliveryNum(value);
                    outputList.add(repairMaterialBo);
                }
            }
        }

        return outputList;
    }


    public static List<RepairOrderSearchVo> poListConvertVoList(List<RepairOrderSearchVo> records,
                                                                Map<String, List<RepairOrderItemPo>> repairOrderItemPoMap,
                                                                Map<String, QcOrderPo> qcOrderPoMap,
                                                                Map<String, List<ProcessMaterialReceiptPo>> processMaterialReceiptPoMap,
                                                                List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPoList,
                                                                List<ReceiveOrderForScmVo> receiveOrderForScmVoList,
                                                                List<RepairOrderResultPo> repairOrderResultPoList,
                                                                Map<String, WarehouseVo> warehouseVoMap) {
        //获取数据
        for (RepairOrderSearchVo record : records) {
            List<RepairOrderItemPo> repairOrderItemPoList = repairOrderItemPoMap.get(record.getRepairOrderNo());
            if (CollectionUtils.isNotEmpty(repairOrderItemPoList)) {
                record.setRepairOrderItemDetailList(
                        RepairOrderItemConverter.poListConvertVoList(repairOrderItemPoList));
                // 完成数量
                Integer actProcessedCompleteCntTotal = repairOrderItemPoList.stream()
                        .mapToInt(RepairOrderItemPo::getActProcessedCompleteCnt)
                        .sum();
                record.setActProcessedCompleteCntTotal(actProcessedCompleteCntTotal);
            }

            // 返修人
            List<RepairOrderResultPo> repairOrderResultPos = repairOrderResultPoList.stream()
                    .filter(resultPo -> resultPo.getRepairOrderNo()
                            .equals(record.getRepairOrderNo()))
                    .collect(Collectors.toList());
            String completeRepairUserName = repairOrderResultPos.stream()
                    .map(RepairOrderResultPo::getRepairUsername)
                    .distinct()
                    .collect(Collectors.joining(","));
            record.setCompleteRepairUserName(completeRepairUserName);

            // 返修单结果表质检数
            int passAmount = Optional.of(repairOrderResultPos)
                    .orElse(new ArrayList<>())
                    .stream()
                    .mapToInt(RepairOrderResultPo::getQcPassQuantity)
                    .sum();
            record.setPassAmount(passAmount);

            int notPassAmount = Optional.of(repairOrderResultPos)
                    .orElse(new ArrayList<>())
                    .stream()
                    .mapToInt(RepairOrderResultPo::getQcFailQuantity)
                    .sum();
            record.setNotPassAmount(notPassAmount);


            // 质检信息
            QcOrderPo qcOrderPo = qcOrderPoMap.get(record.getRepairOrderNo());
            if (qcOrderPo != null) {
                record.setQcOrderNo(qcOrderPo.getQcOrderNo());
            }

            // 签收
            List<ProcessMaterialReceiptPo> processMaterialReceiptPoList
                    = processMaterialReceiptPoMap.get(record.getRepairOrderNo());
            if (CollectionUtils.isNotEmpty(processMaterialReceiptPoList)) {
                processMaterialReceiptPoList.stream()
                        .max(Comparator.comparing(ProcessMaterialReceiptPo::getCreateTime))
                        .ifPresent(processMaterialReceiptPo -> {
                            record.setMaterialReceiveOrderNo(processMaterialReceiptPo.getDeliveryNo());
                            List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPos
                                    = processMaterialReceiptItemPoList.stream()
                                    .filter(po -> po.getProcessMaterialReceiptId()
                                            .equals(processMaterialReceiptPo.getProcessMaterialReceiptId()))
                                    .collect(Collectors.toList());
                            int materialReceiptNumTotal = processMaterialReceiptItemPos.stream()
                                    .mapToInt(ProcessMaterialReceiptItemPo::getReceiptNum)
                                    .sum();
                            record.setMaterialReceiptNumTotal(materialReceiptNumTotal);
                        });
            }

            // 入库信息
            receiveOrderForScmVoList.stream()
                    .filter(vo -> vo.getReceiveOrderNo()
                            .equals(record.getReceiveOrderNo()))
                    .findFirst()
                    .ifPresent(receiveOrderForScmVo -> {
                        record.setReceiptNum(receiveOrderForScmVo.getReceiveAmount());
                    });

            // 仓库
            WarehouseVo warehouseVo = warehouseVoMap.get(record.getExpectWarehouseCode());
            if (null != warehouseVo) {
                record.setExpectWarehouseName(warehouseVo.getWarehouseName());
            }

        }

        return records;
    }

    public static RepairOrderDetailVo poConvertVo(RepairOrderPo repairOrderPo,
                                                  List<RepairOrderItemPo> repairOrderItemPoList,
                                                  List<ReceiveOrderForScmVo> receiveOrderForScmVoList,
                                                  List<ProcessMaterialReceiptPo> processMaterialReceiptPoList,
                                                  List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPoList,
                                                  List<ProcessMaterialBackItemPo> processMaterialBackItemPoList,
                                                  Map<String, String> skuEncodeMap,
                                                  QcOrderPo qcOrderPo,
                                                  List<RepairOrderResultPo> repairOrderResultPoList,
                                                  Map<Long, List<String>> fileCodeMap,
                                                  Map<String, WarehouseVo> warehouseVoMap,
                                                  List<ProcessOrderMaterialPo> processOrderMaterialPoList) {

        RepairOrderDetailVo repairOrderDetailVo = new RepairOrderDetailVo();
        repairOrderDetailVo.setRepairOrderId(repairOrderPo.getRepairOrderId());
        repairOrderDetailVo.setVersion(repairOrderPo.getVersion());
        repairOrderDetailVo.setRepairOrderNo(repairOrderPo.getRepairOrderNo());
        repairOrderDetailVo.setPlanNo(repairOrderPo.getPlanNo());
        repairOrderDetailVo.setPlanTitle(repairOrderPo.getPlanTitle());
        repairOrderDetailVo.setPlatform(repairOrderPo.getPlatform());
        repairOrderDetailVo.setExpectWarehouseCode(repairOrderPo.getExpectWarehouseCode());
        repairOrderDetailVo.setExpectCompleteProcessTime(repairOrderPo.getExpectCompleteProcessTime());
        repairOrderDetailVo.setRepairOrderStatus(repairOrderPo.getRepairOrderStatus());
        repairOrderDetailVo.setReceiveOrderNo(repairOrderPo.getReceiveOrderNo());
        repairOrderDetailVo.setDeliveryNum(repairOrderPo.getDeliveryNum());

        // 仓库
        WarehouseVo warehouseVo = warehouseVoMap.get(repairOrderPo.getExpectWarehouseCode());
        if (null != warehouseVo) {
            repairOrderDetailVo.setExpectWarehouseName(warehouseVo.getWarehouseName());
        }

        // 完成数量
        Integer actProcessedCompleteCntTotal = repairOrderItemPoList.stream()
                .mapToInt(RepairOrderItemPo::getActProcessedCompleteCnt)
                .sum();
        repairOrderDetailVo.setActProcessedCompleteCntTotal(actProcessedCompleteCntTotal);

        // 报废数量
        Integer actProcessScrapCntTotal = repairOrderItemPoList.stream()
                .mapToInt(RepairOrderItemPo::getActProcessScrapCnt)
                .sum();
        repairOrderDetailVo.setActProcessScrapCntTotal(actProcessScrapCntTotal);

        // 入库信息
        receiveOrderForScmVoList.stream()
                .filter(receiveOrderForScmVo -> receiveOrderForScmVo.getReceiveOrderNo()
                        .equals(repairOrderPo.getReceiveOrderNo()))
                .findFirst()
                .ifPresent(receiveOrderForScmVo -> {
                    repairOrderDetailVo.setReceiptNum(receiveOrderForScmVo.getReceiveAmount());
                });

        // 原料收货
        if (CollectionUtils.isNotEmpty(processMaterialReceiptPoList)) {
            processMaterialReceiptPoList.stream()
                    .max(Comparator.comparing(ProcessMaterialReceiptPo::getCreateTime))
                    .ifPresent(processMaterialReceiptPo -> {
                        List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPos
                                = processMaterialReceiptItemPoList.stream()
                                .filter(po -> po.getProcessMaterialReceiptId()
                                        .equals(processMaterialReceiptPo.getProcessMaterialReceiptId()))
                                .collect(Collectors.toList());
                        int materialReceiptNumTotal = processMaterialReceiptItemPos.stream()
                                .mapToInt(ProcessMaterialReceiptItemPo::getReceiptNum)
                                .sum();
                        repairOrderDetailVo.setMaterialReceiptNumTotal(materialReceiptNumTotal);

                        List<RepairOrderItemMaterialReceiptVo> repairOrderItemMaterialReceiptList = new ArrayList<>();
                        for (ProcessMaterialReceiptItemPo processMaterialReceiptItemPo :
                                processMaterialReceiptItemPos) {
                            Integer receiptNum = processMaterialReceiptItemPo.getReceiptNum();
                            if (receiptNum > 0) {
                                RepairOrderItemMaterialReceiptVo repairOrderItemMaterialReceiptVo
                                        = new RepairOrderItemMaterialReceiptVo();
                                repairOrderItemMaterialReceiptVo.setSku(processMaterialReceiptItemPo.getSku());
                                repairOrderItemMaterialReceiptVo.setBatchCode(
                                        processMaterialReceiptItemPo.getSkuBatchCode());
                                repairOrderItemMaterialReceiptVo.setSkuEncode(
                                        skuEncodeMap.get(processMaterialReceiptItemPo.getSku()));
                                repairOrderItemMaterialReceiptVo.setReceiptNum(
                                        processMaterialReceiptItemPo.getReceiptNum());
                                repairOrderItemMaterialReceiptList.add(repairOrderItemMaterialReceiptVo);
                            }
                        }
                        repairOrderDetailVo.setRepairOrderItemMaterialReceiptList(repairOrderItemMaterialReceiptList);
                    });

        }
        // 原料
        if (CollectionUtils.isNotEmpty(processOrderMaterialPoList)) {
            List<RepairOrderItemMaterialVo> repairOrderItemMaterialList = new ArrayList<>();
            for (ProcessOrderMaterialPo processOrderMaterialPo : processOrderMaterialPoList) {
                RepairOrderItemMaterialVo repairOrderItemMaterialVo
                        = new RepairOrderItemMaterialVo();
                repairOrderItemMaterialVo.setSku(processOrderMaterialPo.getSku());
                repairOrderItemMaterialVo.setSkuEncode(skuEncodeMap.get(processOrderMaterialPo.getSku()));
                repairOrderItemMaterialVo.setReceiptNum(processOrderMaterialPo.getDeliveryNum());
                repairOrderItemMaterialList.add(repairOrderItemMaterialVo);
            }
            // 原料收货单号取第一条的出库单编号
            repairOrderDetailVo.setMaterialReceiveOrderNo(processOrderMaterialPoList.get(0)
                    .getDeliveryNo());
            repairOrderDetailVo.setRepairOrderItemMaterialList(repairOrderItemMaterialList);
        }

        // 返修单结果表质检数
        int passAmount = Optional.of(repairOrderResultPoList)
                .orElse(new ArrayList<>())
                .stream()
                .mapToInt(RepairOrderResultPo::getQcPassQuantity)
                .sum();
        repairOrderDetailVo.setPassAmount(passAmount);

        int notPassAmount = Optional.of(repairOrderResultPoList)
                .orElse(new ArrayList<>())
                .stream()
                .mapToInt(RepairOrderResultPo::getQcFailQuantity)
                .sum();
        repairOrderDetailVo.setNotPassAmount(notPassAmount);


        // 质检
        if (qcOrderPo != null) {
            repairOrderDetailVo.setQcOrderNo(qcOrderPo.getQcOrderNo());
        }

        // 返修单明细
        repairOrderDetailVo.setRepairOrderItemDetailList(
                RepairOrderItemConverter.poListConvertVoList(repairOrderItemPoList));

        // 结果明细
        repairOrderDetailVo.setRepairOrderResultDetailList(
                RepairOrderResultConverter.poListConvertVoList(repairOrderItemPoList, repairOrderResultPoList,
                        fileCodeMap));

        // 返修单明细和结果列表
        repairOrderDetailVo.setRepairOrderItemAndResultList(
                RepairOrderItemConverter.itemPoAndResultPoListConvertVoList(repairOrderItemPoList,
                        repairOrderResultPoList,
                        fileCodeMap));
        // 原料信息列表（原料信息&原料收货信息&原料收货明细）
        List<RepairMaterialCompositeVo> repairMaterialCompositeVos
                = ProcOrderMaterialBuilder.buildRepairMaterialCompositeVos(skuEncodeMap, processOrderMaterialPoList);

        // 填充 repairMaterialCompositeVos 中的 RepairMaterialReceiptVo 列表
        ProcOrderMaterialBuilder.fillRepairMaterialReceiptVos(repairMaterialCompositeVos,
                processMaterialReceiptItemPoList,
                processMaterialBackItemPoList,
                repairOrderResultPoList);

        // 将填充后的 repairMaterialCompositeVos 设置到 repairOrderDetailVo 中
        repairOrderDetailVo.setRepairMaterialCompositeVos(repairMaterialCompositeVos);


        return repairOrderDetailVo;
    }

    public static RepairOrderReturnMaterialInfoVo toRepairOrderReturnMaterialInfoVo(String repairOrderNo,
                                                                                    String returnWarehouseCode,
                                                                                    String returnWarehouseName,
                                                                                    List<RepairOrderBindingInfoBo> repairOrderBindingInfo) {
        RepairOrderReturnMaterialInfoVo repairOrderReturnMaterialInfoVo = new RepairOrderReturnMaterialInfoVo();
        repairOrderReturnMaterialInfoVo.setRepairOrderNo(repairOrderNo);
        repairOrderReturnMaterialInfoVo.setReturnWarehouseCode(returnWarehouseCode);
        repairOrderReturnMaterialInfoVo.setReturnWarehouseName(returnWarehouseName);

        List<RepairOrderReturnMaterialInfoVo.MaterialReturnInfoVo> materialReturnInfoList
                = repairOrderBindingInfo.stream()
                .map(repairOrderBindingInfoBo -> {
                    RepairOrderReturnMaterialInfoVo.MaterialReturnInfoVo materialReturnInfoVo
                            = new RepairOrderReturnMaterialInfoVo.MaterialReturnInfoVo();

                    materialReturnInfoVo.setSku(repairOrderBindingInfoBo.getSku());
                    materialReturnInfoVo.setBatchCode(repairOrderBindingInfoBo.getBatchCode());
                    materialReturnInfoVo.setReturnableQuantity(
                            Math.max(repairOrderBindingInfoBo.getAvailableForReturn(), 0));
                    return materialReturnInfoVo;
                })
                .filter(materialReturnInfoVo -> materialReturnInfoVo.getReturnableQuantity() > 0)
                .collect(Collectors.toList());
        repairOrderReturnMaterialInfoVo.setMaterialReturnInfoList(materialReturnInfoList);

        return repairOrderReturnMaterialInfoVo;
    }
}










