package com.hete.supply.scm.server.scm.process.builder;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.hete.supply.plm.api.goods.entity.vo.PlmSkuVo;
import com.hete.supply.plm.api.repair.entity.enums.ProcessType;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderChangeMqDto;
import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderCreateMqDto;
import com.hete.supply.scm.server.scm.entity.po.ScmImagePo;
import com.hete.supply.scm.server.scm.enums.BackStatus;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.enums.wms.ScmBizReceiveOrderType;
import com.hete.supply.scm.server.scm.enums.wms.SkuDevType;
import com.hete.supply.scm.server.scm.process.entity.bo.*;
import com.hete.supply.scm.server.scm.process.entity.dto.*;
import com.hete.supply.scm.server.scm.process.entity.po.*;
import com.hete.supply.scm.server.scm.process.entity.vo.RepairOrderPrintMaterialReturnVo;
import com.hete.supply.scm.server.scm.process.entity.vo.RepairOrderPrintProductReceiptVo;
import com.hete.supply.scm.server.scm.process.entity.vo.RepairOrderPrintResultVo;
import com.hete.supply.scm.server.scm.qc.entity.bo.RepairOrderCreateQcBo;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.basic.entity.dto.SkuBatchCreateDto;
import com.hete.supply.wms.api.basic.entity.vo.WarehouseVo;
import com.hete.supply.wms.api.entry.entity.dto.ReceiveOrderGetDto;
import com.hete.supply.wms.api.leave.entity.vo.ProcessDeliveryOrderVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.core.holder.GlobalContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/1/5.
 */
public class RepairOrderBuilder {

    /**
     * 构建返修结果的 PO 对象
     *
     * @param requestDto        返修结果的 DTO 对象
     * @param repairOrderItemPo 返修单明细的 PO 对象
     * @return 构建的返修结果的 PO 对象
     */
    public static RepairOrderResultPo buildRepairOrderResultPo(RepairDetailResultRequestDto requestDto,
                                                               RepairOrderItemPo repairOrderItemPo) {
        RepairOrderResultPo repairOrderResultPo = new RepairOrderResultPo();

        // 根据 DTO 和 PO 设置相应的属性
        repairOrderResultPo.setRepairOrderNo(repairOrderItemPo.getRepairOrderNo());
        repairOrderResultPo.setRepairOrderItemId(repairOrderItemPo.getRepairOrderItemId());
        repairOrderResultPo.setSpu(repairOrderItemPo.getSpu());
        repairOrderResultPo.setSku(repairOrderItemPo.getSku());
        repairOrderResultPo.setBatchCode(repairOrderItemPo.getBatchCode());
        repairOrderResultPo.setMaterialBatchCode(requestDto.getMaterialBatchCode());

        Integer successfulRepairQuantity = requestDto.getSuccessfulRepairQuantity();
        repairOrderResultPo.setMaterialUsageQuantity(successfulRepairQuantity);
        repairOrderResultPo.setCompletedQuantity(successfulRepairQuantity);

        repairOrderResultPo.setRepairUser(
                Objects.nonNull(requestDto.getRepairUser()) ? requestDto.getRepairUser() : GlobalContext.getUserKey());
        repairOrderResultPo.setRepairUsername(Objects.nonNull(
                requestDto.getRepairUsername()) ? requestDto.getRepairUsername() : GlobalContext.getUsername());
        repairOrderResultPo.setRepairTime(
                Objects.nonNull(requestDto.getRepairTime()) ? requestDto.getRepairTime() : LocalDateTime.now());

        return repairOrderResultPo;
    }

    /**
     * 构建 ScmImagePo 列表
     *
     * @param request             返修结果的 DTO 对象
     * @param repairOrderResultPo 返修结果的 PO 对象
     * @return 构建的 ScmImagePo 列表
     */
    public static List<ScmImagePo> buildScmImagePoList(RepairDetailResultRequestDto request,
                                                       RepairOrderResultPo repairOrderResultPo) {
        List<String> imageCodes = request.getImageCodes();
        return CollectionUtils.isEmpty(imageCodes) ? Collections.emptyList() : imageCodes.stream()
                .map(imageCode -> {
                    ScmImagePo scmImagePo = new ScmImagePo();
                    scmImagePo.setFileCode(imageCode);
                    scmImagePo.setImageBizId(repairOrderResultPo.getRepairOrderResultId());
                    scmImagePo.setImageBizType(ImageBizType.REPAIR_RESULT);
                    return scmImagePo;
                })
                .collect(Collectors.toList());

    }

    /**
     * 构建 ScmImagePo 列表
     *
     * @param matchCompletionResult 返修结果的 DTO 对象
     * @param repairOrderResultPo   返修结果的 PO 对象
     * @return 构建的 ScmImagePo 列表
     */
    public static List<ScmImagePo> buildScmImagePoList(CompletionRequestDto.CompletionDetailRequestDto matchCompletionResult,
                                                       RepairOrderResultPo repairOrderResultPo) {
        List<String> imageCodes = matchCompletionResult.getImageCodes();
        return imageCodes.stream()
                .filter(StrUtil::isNotBlank)
                .map(imageCode -> {
                    ScmImagePo scmImagePo = new ScmImagePo();
                    scmImagePo.setFileCode(imageCode);
                    scmImagePo.setImageBizId(repairOrderResultPo.getRepairOrderResultId());
                    scmImagePo.setImageBizType(ImageBizType.REPAIR_RESULT);
                    return scmImagePo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 构建 RepairOrderPo 对象
     *
     * @param createDto 创建维修订单的 DTO 对象
     * @return 构建的 RepairOrderPo 对象
     */
    public static RepairOrderPo buildRepairOrderPo(String repairOrderNo,
                                                   Integer expectProcessNum,
                                                   CreateRepairOrderMqDto createDto) {
        ParamValidUtils.requireNotNull(createDto, "创建返修单失败，计划单信息为空！");

        String planNo = createDto.getPlanNo();
        String title = createDto.getTitle();
        String generateUserCode = createDto.getGenerateUserCode();
        String generateUsername = createDto.getGenerateUsername();
        LocalDateTime generateTime = createDto.getGenerateTime();
        String receiveWarehouseCode = createDto.getReceiveWarehouseCode();
        String receiveWarehouseName = createDto.getReceiveWarehouseName();
        LocalDateTime expectCompleteTime = createDto.getExpectCompleteTime();
        String platCode = createDto.getPlatCode();
        BigDecimal salePrice = createDto.getSalePrice();
        String remark = createDto.getRemark();
        ProcessType planType = createDto.getProcessType();

        RepairOrderPo repairOrderPo = new RepairOrderPo();
        repairOrderPo.setRepairOrderNo(repairOrderNo);
        repairOrderPo.setExpectProcessNum(expectProcessNum);
        repairOrderPo.setExpectCompleteProcessTime(expectCompleteTime);
        repairOrderPo.setIsReceiveMaterial(IsReceiveMaterial.FALSE);
        repairOrderPo.setRepairOrderStatus(RepairOrderStatus.WAITING_FOR_READY);
        repairOrderPo.setExpectWarehouseCode(receiveWarehouseCode);
        repairOrderPo.setExpectWarehouseName(receiveWarehouseName);

        repairOrderPo.setPlanNo(planNo);
        repairOrderPo.setPlanType(planType);
        repairOrderPo.setPlanTitle(title);
        repairOrderPo.setPlatform(platCode);

        repairOrderPo.setPlanCreateUser(generateUserCode);
        repairOrderPo.setPlanCreateUsername(generateUsername);
        repairOrderPo.setPlanCreateTime(generateTime);
        repairOrderPo.setSalePrice(salePrice);
        repairOrderPo.setPlanRemark(remark);
        return repairOrderPo;
    }

    public static List<RepairOrderItemPo> buildRepairOrderItemPos(String repairOrderNo,
                                                                  List<RepairOrderItemBo> repairOrderItems) {
        ParamValidUtils.requireNotEmpty(repairOrderItems, "创建返修单失败，返修单商品信息为空！");
        return repairOrderItems.stream()
                .map(repairOrderItemBo -> {

                    RepairOrderItemPo repairOrderItemPo = new RepairOrderItemPo();
                    repairOrderItemPo.setRepairOrderNo(repairOrderNo);
                    repairOrderItemPo.setSpu(repairOrderItemBo.getSpu());
                    repairOrderItemPo.setSku(repairOrderItemBo.getSku());
                    repairOrderItemPo.setExpectProcessNum(repairOrderItemBo.getExpectProcessNum());
                    return repairOrderItemPo;
                })
                .collect(Collectors.toList());
    }

    public static List<ProcessOrderMaterialPo> buildRepairMaterialPos(String repairOrderNo,
                                                                      List<RepairMaterialBo> repairMaterials) {
        ParamValidUtils.requireNotEmpty(repairMaterials, "创建返修单失败，返修单原料信息为空！");

        return repairMaterials.stream()
                .map(repairMaterialBo -> {

                    ProcessOrderMaterialPo repairMaterialPo = new ProcessOrderMaterialPo();
                    repairMaterialPo.setRepairOrderNo(repairOrderNo);
                    repairMaterialPo.setSku(repairMaterialBo.getSku());
                    repairMaterialPo.setWarehouseCode(repairMaterialBo.getWarehouseCode());
                    repairMaterialPo.setShelfCode(repairMaterialBo.getShelfCode());
                    repairMaterialPo.setProductQuality(repairMaterialPo.getProductQuality());
                    repairMaterialPo.setDeliveryNum(repairMaterialBo.getDeliveryNum());

                    return repairMaterialPo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 构建拆分计划订单结果列表的创建返修单结果消息DTO列表。
     *
     * @param splitResults 拆分计划订单结果列表
     * @return 创建返修单结果消息DTO列表
     */
    public static List<CreateRepairOrderResultMqDto> buildCreateResultDtoList(List<SplitPlanOrderResultBo> splitResults) {
        return splitResults.stream()
                .map(splitPlanOrderResultBo -> {
                    // 获取返修单信息，确保不为空
                    RepairOrderPo repairOrderPo = splitPlanOrderResultBo.getRepairOrderPo();

                    // 获取返修单商品信息列表，确保不为空
                    List<RepairOrderItemPo> repairOrderItemPos = splitPlanOrderResultBo.getRepairOrderItemPos();

                    // 获取返修单计划号和返修单号
                    String planNo = repairOrderPo.getPlanNo();
                    String repairOrderNo = repairOrderPo.getRepairOrderNo();

                    // 构建创建返修单结果消息DTO
                    CreateRepairOrderResultMqDto resultMqDto = new CreateRepairOrderResultMqDto();
                    resultMqDto.setPlanNo(planNo);
                    resultMqDto.setRepairNo(repairOrderNo);

                    // 获取商品信息的 SKU 列表并设置到DTO中
                    List<String> skuCodeList = repairOrderItemPos.stream()
                            .map(RepairOrderItemPo::getSku)
                            .collect(Collectors.toList());
                    resultMqDto.setSkuCodeList(skuCodeList);

                    return resultMqDto;
                })
                .collect(Collectors.toList());
    }

    public static SkuBatchCreateDto buildSkuBatchCreateDto(String repairOrderNo, String supplierCode, String supplierName, List<String> skuList) {
        // 构建 SKU 批次创建 DTO
        SkuBatchCreateDto skuBatchCreateDto = new SkuBatchCreateDto();
        skuBatchCreateDto.setPurchaseChildOrderNo(repairOrderNo);
        skuBatchCreateDto.setSkuCodeList(skuList);

        if (StrUtil.isNotBlank(supplierCode)) {
            skuBatchCreateDto.setSupplierCode(supplierCode);
        }
        return skuBatchCreateDto;
    }

    /**
     * 构建 CreateRepairOrderSyncBo 对象。
     *
     * @param repairOrderNo 返修单号
     * @param planNo        计划单号
     * @return 构建的 CreateRepairOrderSyncBo 对象
     */
    public static CreateRepairOrderSyncBo buildCreateRepairOrderSyncBo(String planNo,
                                                                       String repairOrderNo) {
        CreateRepairOrderSyncBo repairOrderSyncBo = new CreateRepairOrderSyncBo();
        repairOrderSyncBo.setRepairOrderNo(repairOrderNo);
        repairOrderSyncBo.setPlanNo(planNo);
        // 可以根据需要设置其他属性

        return repairOrderSyncBo;
    }


    /**
     * 构建返修质检详情列表
     *
     * @param repairOrderResultPos 返修结果列表
     * @return 返修质检详情列表
     */
    public static List<RepairQcDetailCreateBo> buildRepairDetailCreateBo(List<RepairOrderResultPo> repairOrderResultPos) {
        List<RepairQcDetailCreateBo> repairQcDetailCreateBos = Lists.newArrayList();

        // 按sku和批次码分组
        Map<String, List<RepairOrderResultPo>> groupedBySkuAndBatch = repairOrderResultPos.stream()
                .collect(Collectors.groupingBy(result -> result.getSku() + result.getBatchCode()));

        groupedBySkuAndBatch.forEach((key, resultList) -> {
            RepairQcDetailCreateBo repairQcDetailCreateBo = new RepairQcDetailCreateBo();
            RepairOrderResultPo repairOrderResultPo = resultList.stream()
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(repairOrderResultPo)) {
                repairQcDetailCreateBo.setSpu(repairQcDetailCreateBo.getSpu());
                repairQcDetailCreateBo.setSku(repairOrderResultPo.getSku());
                repairQcDetailCreateBo.setBatchCode(repairOrderResultPo.getBatchCode());
                repairQcDetailCreateBo.setRepairOrderItemId(repairOrderResultPo.getRepairOrderItemId());
            }

            int totalCompletedQuantity = resultList.stream()
                    .mapToInt(RepairOrderResultPo::getCompletedQuantity)
                    .sum();
            repairQcDetailCreateBo.setPendingQcQuantity(totalCompletedQuantity);

            int totalQcPassQuantity = resultList.stream()
                    .mapToInt(RepairOrderResultPo::getQcPassQuantity)
                    .sum();
            repairQcDetailCreateBo.setQcPassQuantity(totalQcPassQuantity);

            int totalQcFailQuantity = resultList.stream()
                    .mapToInt(RepairOrderResultPo::getQcFailQuantity)
                    .sum();
            repairQcDetailCreateBo.setQcFailQuantity(totalQcFailQuantity);
            repairQcDetailCreateBos.add(repairQcDetailCreateBo);
        });
        return repairQcDetailCreateBos;
    }

    public static List<RepairQcDetailCreateBo> buildRepairQcDetailCreateBos(List<RepairOrderResultPo> repairOrderResultPos) {
        List<RepairQcDetailCreateBo> repairQcDetailCreateBos = buildRepairDetailCreateBo(repairOrderResultPos);
        return buildMergedList(repairQcDetailCreateBos);
    }

    public static List<RepairQcDetailCreateBo> buildMergedList(List<RepairQcDetailCreateBo> repairQcDetailCreateBos) {
        List<RepairQcDetailCreateBo> mergedList = Lists.newArrayList();

        List<RepairQcDetailCreateBo> existQcPassList = repairQcDetailCreateBos.stream()
                .filter(repairQcDetailCreateBo -> repairQcDetailCreateBo.getQcPassQuantity() > 0)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(existQcPassList)) {
            List<RepairQcDetailCreateBo> qcPassList = deepCopyList(existQcPassList);
            qcPassList.forEach(qcPass -> {
                qcPass.setPendingQcQuantity(qcPass.getPendingQcQuantity());
                qcPass.setQcFailQuantity(0);
            });
            mergedList.addAll(qcPassList);
        }

        List<RepairQcDetailCreateBo> existQcFailList = repairQcDetailCreateBos.stream()
                .filter(repairQcDetailCreateBo -> repairQcDetailCreateBo.getQcFailQuantity() > 0)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(existQcFailList)) {
            List<RepairQcDetailCreateBo> qcFailList = deepCopyList(existQcFailList);
            qcFailList.forEach(qcPass -> {
                qcPass.setPendingQcQuantity(qcPass.getPendingQcQuantity());
                qcPass.setQcPassQuantity(0);
            });
            mergedList.addAll(qcFailList);
        }
        return mergedList;
    }

    public static ReceiveOrderCreateMqDto buildReceiveOrderCreateMqDto(RepairOrderPo repairOrderPo,
                                                                       String platform,
                                                                       List<RepairQcDetailCreateBo> repairQcDetailCreateBos,
                                                                       List<RepairOrderItemPo> repairOrderItemPos) {
        ReceiveOrderCreateMqDto receiveOrderCreateMqDto = new ReceiveOrderCreateMqDto();

        ReceiveOrderCreateMqDto.ReceiveOrderCreateItem receiveOrderCreateItem
                = new ReceiveOrderCreateMqDto.ReceiveOrderCreateItem();
        receiveOrderCreateItem.setReceiveType(ReceiveType.PROCESS_PRODUCT);
        receiveOrderCreateItem.setReceiveTypeAttribute(ReceiveTypeAttribute.PROCESS_PRODUCT_REPAIR);
        receiveOrderCreateItem.setWarehouseCode(repairOrderPo.getExpectWarehouseCode());
        receiveOrderCreateItem.setSkuDevType(SkuDevType.NORMAL);
        receiveOrderCreateItem.setQcType(WmsEnum.QcType.NOT_CHECK);
        receiveOrderCreateItem.setScmBizNo(repairOrderPo.getRepairOrderNo());
        receiveOrderCreateItem.setPurchaseOrderType(PurchaseOrderType.NORMAL);
        receiveOrderCreateItem.setIsUrgentOrder(BooleanType.FALSE);
        receiveOrderCreateItem.setIsDirectSend(BooleanType.FALSE);
        receiveOrderCreateItem.setIsNormalOrder(BooleanType.FALSE);
        receiveOrderCreateItem.setPlaceOrderTime(new DateTime().toLocalDateTime());
        receiveOrderCreateItem.setSendTime(new DateTime().toLocalDateTime());
        receiveOrderCreateItem.setOperator(GlobalContext.getUserKey());
        receiveOrderCreateItem.setOperatorName(GlobalContext.getUsername());

        List<ReceiveOrderCreateMqDto.ReceiveOrderDetail> receiveOrderDetails = repairQcDetailCreateBos.stream()
                .map(repairQcDetailCreateBo -> {
                    ReceiveOrderCreateMqDto.ReceiveOrderDetail receiveOrderDetail = new ReceiveOrderCreateMqDto.ReceiveOrderDetail();
                    receiveOrderDetail.setPlatCode(platform);
                    receiveOrderDetail.setSpu(repairQcDetailCreateBo.getSpu());
                    receiveOrderDetail.setSkuCode(repairQcDetailCreateBo.getSku());
                    receiveOrderDetail.setBatchCode(repairQcDetailCreateBo.getBatchCode());

                    Long repairOrderItemId = repairQcDetailCreateBo.getRepairOrderItemId();
                    RepairOrderItemPo matchRepairOrderItemPo = ParamValidUtils.requireNotNull(
                            repairOrderItemPos.stream()
                                    .filter(repairOrderItemPo -> Objects.equals(repairOrderItemId,
                                            repairOrderItemPo.getRepairOrderItemId()))
                                    .findFirst()
                                    .orElse(null), "返修明细信息不存在，请刷新页面重试");
                    receiveOrderDetail.setPurchaseAmount(matchRepairOrderItemPo.getExpectProcessNum());
                    receiveOrderDetail.setDeliveryAmount(repairQcDetailCreateBo.getQcPassQuantity());
                    return receiveOrderDetail;
                })
                .collect(Collectors.toList());
        receiveOrderCreateItem.setDetailList(receiveOrderDetails);

        receiveOrderCreateMqDto.setReceiveOrderCreateItemList(List.of(receiveOrderCreateItem));
        return receiveOrderCreateMqDto;
    }

    public static List<RepairDetailResultRequestDto> buildRepairDetailResultRequestDtoList(List<CompletionRequestDto.CompletionDetailRequestDto> newRepairResultList,
                                                                                           List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPos) {
        return newRepairResultList.stream()
                .map(detail -> {
                    Integer subCompletedQuantity = detail.getCompletedQuantity();
                    String subMaterialBatchCode = detail.getMaterialBatchCode();

                    RepairDetailResultRequestDto repairDetailResult = new RepairDetailResultRequestDto();
                    repairDetailResult.setRepairUser(detail.getRepairUser());
                    repairDetailResult.setRepairUsername(detail.getRepairUsername());
                    repairDetailResult.setRepairTime(detail.getRepairTime());
                    repairDetailResult.setBatchCode(detail.getBatchCode());
                    repairDetailResult.setImageCodes(detail.getImageCodes());
                    repairDetailResult.setSuccessfulRepairQuantity(subCompletedQuantity);
                    repairDetailResult.setMaterialBatchCode(
                            subCompletedQuantity > 0 ? subMaterialBatchCode : processMaterialReceiptItemPos.stream()
                                    .findFirst()
                                    .map(ProcessMaterialReceiptItemPo::getSkuBatchCode)
                                    .orElse(""));
                    return repairDetailResult;
                })
                .collect(Collectors.toList());
    }

    public static RepairOrderInStorageDto buildRepairOrderInStorageDto(String repairOrderNo,
                                                                       List<ReceiveOrderChangeMqDto.BatchCodeDetail> batchCodeDetailList) {
        RepairOrderInStorageDto repairOrderInStorageDto = new RepairOrderInStorageDto();
        repairOrderInStorageDto.setRepairNo(repairOrderNo);

        if (CollectionUtils.isEmpty(batchCodeDetailList)) {
            throw new BizException("返修收货单入库推送PLM失败，原因:{}", "收货单上架无商品信息");
        }
        List<RepairOrderInStorageDto.SkuInStorageInfo> inStorageSkus = batchCodeDetailList.stream()
                .map(purchaseReceiptSyncItemDto -> {
                    RepairOrderInStorageDto.SkuInStorageInfo skuInStorageInfo
                            = new RepairOrderInStorageDto.SkuInStorageInfo();
                    skuInStorageInfo.setSkuCode(purchaseReceiptSyncItemDto.getSkuCode());
                    skuInStorageInfo.setReceiveAmount(purchaseReceiptSyncItemDto.getPassAmount());
                    return skuInStorageInfo;
                })
                .collect(Collectors.toList());
        repairOrderInStorageDto.setInStorageSkus(inStorageSkus);
        repairOrderInStorageDto.setKey(
                StrUtil.format("{}:{}", ScmConstant.REPAIR_ORDER_QUALITY_INSPECTION_COMPLETED, repairOrderNo));

        return repairOrderInStorageDto;
    }

    /**
     * 构建一个 {@link RepairOrderChangeDto} 对象，用于表示返修单的变更信息。
     *
     * @param repairOrderNo 返修单号。
     * @param msgKey        变更消息的键值。
     * @return 构建好的 {@link RepairOrderChangeDto} 对象。
     */
    public static RepairOrderChangeDto buildRepairOrderChangeDto(String repairOrderNo,
                                                                 String msgKey) {
        RepairOrderChangeDto repairOrderChangeDto = new RepairOrderChangeDto();
        repairOrderChangeDto.setRepairNo(repairOrderNo);
        repairOrderChangeDto.setKey(msgKey);
        // 可以设置其他属性，根据需要

        return repairOrderChangeDto;
    }


    public static RepairOrderPo buildCompletedQcRepairOrderPo(RepairOrderPo repairOrderPo,
                                                              List<RepairOrderResultPo> repairOrderResultPos) {
        repairOrderPo.setConfirmCompleteUser(GlobalContext.getUserKey());
        repairOrderPo.setConfirmCompleteUsername(GlobalContext.getUsername());
        repairOrderPo.setConfirmCompleteTime(LocalDateTime.now());
        repairOrderPo.setRepairOrderStatus(repairOrderResultPos.stream()
                .anyMatch(
                        result -> result.getCompletedQuantity() > 0) ?
                RepairOrderStatus.WAITING_FOR_QUALITY_INSPECTION :
                RepairOrderStatus.COMPLETED);
        return repairOrderPo;
    }

    public static RepairOrderCreateQcBo buildRepairOrderCreateQcBo(String repairOrderNo,
                                                                   String expectWarehouseCode,
                                                                   List<RepairOrderResultPo> repairOrderResultPos,
                                                                   String platform) {
        RepairOrderCreateQcBo repairOrderCreateQcBo = new RepairOrderCreateQcBo();
        repairOrderCreateQcBo.setRepairOrderNo(repairOrderNo);
        repairOrderCreateQcBo.setWarehouseCode(expectWarehouseCode);
        repairOrderCreateQcBo.setPlatform(platform);

        List<RepairQcDetailCreateBo> repairQcDetailCreateBos = buildRepairQcDetailCreateBos(repairOrderResultPos);
        repairOrderCreateQcBo.setRepairQcDetailCreateBos(repairQcDetailCreateBos);
        return repairOrderCreateQcBo;
    }

    public static List<RepairQcDetailCreateBo> deepCopyList(List<RepairQcDetailCreateBo> originalList) {
        List<RepairQcDetailCreateBo> copiedList = new ArrayList<>();

        // 遍历原始列表，进行深拷贝
        for (RepairQcDetailCreateBo originalDetail : originalList) {
            RepairQcDetailCreateBo copiedDetail = new RepairQcDetailCreateBo();

            // 进行属性的赋值，确保是深拷贝
            copiedDetail.setSku(originalDetail.getSku());
            copiedDetail.setSpu(originalDetail.getSpu());
            copiedDetail.setBatchCode(originalDetail.getBatchCode());
            copiedDetail.setPendingQcQuantity(originalDetail.getPendingQcQuantity());
            copiedDetail.setQcPassQuantity(originalDetail.getQcPassQuantity());
            copiedDetail.setQcFailQuantity(originalDetail.getQcFailQuantity());
            copiedDetail.setRepairOrderItemId(originalDetail.getRepairOrderItemId());
            copiedList.add(copiedDetail);
        }

        return copiedList;
    }

    public static RepairOrderPrintResultVo buildRepairOrderPrintResultVo(String deliveryNo,
                                                                         RepairOrderPo matchRepairOrder,
                                                                         ProcessOrderMaterialPo matchRepairMaterial,
                                                                         ProcessDeliveryOrderVo matchRepairDelivery,
                                                                         List<WarehouseVo> warehouseVos) {
        RepairOrderPrintResultVo repairOrderPrintResultVo = new RepairOrderPrintResultVo();
        repairOrderPrintResultVo.setOutboundOrderNo(deliveryNo);

        if (Objects.nonNull(matchRepairOrder)) {
            repairOrderPrintResultVo.setRepairOrderId(matchRepairOrder.getRepairOrderId());
            repairOrderPrintResultVo.setRepairOrderNo(matchRepairOrder.getRepairOrderNo());
            repairOrderPrintResultVo.setReceivingWarehouseCode(matchRepairOrder.getExpectWarehouseCode());

            WarehouseVo matchReceivingWare = warehouseVos.stream()
                    .filter(warehouseVo -> Objects.equals(matchRepairOrder.getExpectWarehouseCode(),
                            warehouseVo.getWarehouseCode()))
                    .findFirst()
                    .orElse(null);
            repairOrderPrintResultVo.setReceivingWarehouseName(
                    Objects.nonNull(matchReceivingWare) ? matchReceivingWare.getWarehouseName() : "");
            repairOrderPrintResultVo.setScheduledTime(matchRepairOrder.getExpectCompleteProcessTime());
            repairOrderPrintResultVo.setRepairOrderStatus(matchRepairOrder.getRepairOrderStatus());
            repairOrderPrintResultVo.setOrderUsername(matchRepairOrder.getPlanCreateUsername());
            repairOrderPrintResultVo.setOrderTime(matchRepairOrder.getPlanCreateTime());
            repairOrderPrintResultVo.setPlanRemark(matchRepairOrder.getPlanRemark());
        }

        if (Objects.nonNull(matchRepairMaterial)) {
            repairOrderPrintResultVo.setMaterialShippingWarehouseCode(matchRepairMaterial.getWarehouseCode());
            WarehouseVo matchMaterialShippingWarehouse = warehouseVos.stream()
                    .filter(warehouseVo -> Objects.equals(matchRepairMaterial.getWarehouseCode(),
                            warehouseVo.getWarehouseCode()))
                    .findFirst()
                    .orElse(null);
            repairOrderPrintResultVo.setMaterialShippingWarehouseName(Objects.nonNull(
                    matchMaterialShippingWarehouse) ? matchMaterialShippingWarehouse.getWarehouseName() : "");
        }

        if (Objects.nonNull(matchRepairDelivery)) {
            repairOrderPrintResultVo.setPickingOrderNo(matchRepairDelivery.getPickOrderNo());
            repairOrderPrintResultVo.setOutboundWarehouseCode(matchRepairDelivery.getWarehouseCode());
            repairOrderPrintResultVo.setOutboundWarehouseName(matchRepairDelivery.getWarehouseName());
            repairOrderPrintResultVo.setPalletInformation(matchRepairDelivery.getPickingCartStackCodeList());
        }
        return repairOrderPrintResultVo;
    }

    public static List<RepairOrderPrintResultVo.RepairProductInfoVo> buildRepairProductInfoVo(List<RepairOrderItemPo> matchRepairOrderItemPos,
                                                                                              List<PlmSkuVo> skuEncodes) {
        if (CollectionUtils.isEmpty(matchRepairOrderItemPos)) {
            return Collections.emptyList();
        }

        return matchRepairOrderItemPos.stream()
                .map(matchRepairOrderItemPo -> {
                    String sku = matchRepairOrderItemPo.getSku();

                    RepairOrderPrintResultVo.RepairProductInfoVo vo
                            = new RepairOrderPrintResultVo.RepairProductInfoVo();
                    vo.setSku(sku);

                    PlmSkuVo plmSkuVo = skuEncodes.stream()
                            .filter(skuEncode -> Objects.equals(sku, skuEncode.getSkuCode()))
                            .findFirst()
                            .orElse(null);
                    vo.setSkuEncode(Objects.nonNull(plmSkuVo) ? plmSkuVo.getSkuEncode() : "");
                    vo.setBatchCode(matchRepairOrderItemPo.getBatchCode());
                    vo.setProcessingQuantity(matchRepairOrderItemPo.getExpectProcessNum());
                    vo.setGoodQuantity(matchRepairOrderItemPo.getActProcessedCompleteCnt());
                    vo.setDefectiveQuantity(matchRepairOrderItemPo.getActProcessScrapCnt());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    public static List<RepairOrderPrintResultVo.UnitMaterialRatioVo> buildUnitMaterialRatioVos(List<ProcessOrderMaterialPo> matchRepairMaterials,
                                                                                               List<PlmSkuVo> skuEncodes) {
        if (CollectionUtils.isEmpty(matchRepairMaterials)) {
            return Collections.emptyList();
        }

        return matchRepairMaterials.stream()
                .map(matchRepairMaterial -> {
                    String sku = matchRepairMaterial.getSku();

                    RepairOrderPrintResultVo.UnitMaterialRatioVo vo
                            = new RepairOrderPrintResultVo.UnitMaterialRatioVo();
                    vo.setSku(sku);

                    vo.setRate(1.0);
                    PlmSkuVo plmSkuVo = skuEncodes.stream()
                            .filter(skuEncode -> Objects.equals(sku, skuEncode.getSkuCode()))
                            .findFirst()
                            .orElse(null);
                    vo.setSkuEncode(Objects.nonNull(plmSkuVo) ? plmSkuVo.getSkuEncode() : "");
                    vo.setDeliveryNum(matchRepairMaterial.getDeliveryNum());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    public static List<RepairOrderPrintResultVo.OutboundDetailInfoVo> buildOutboundDetailInfoVos(List<ProcessOrderMaterialPo> matchRepairMaterial,
                                                                                                 List<PlmSkuVo> skuEncodes) {
        Map<String, List<ProcessOrderMaterialPo>> materialSkuMap = matchRepairMaterial.stream()
                .collect(Collectors.groupingBy(ProcessOrderMaterialPo::getSku));

        List<RepairOrderPrintResultVo.OutboundDetailInfoVo> resultList = Lists.newArrayList();
        materialSkuMap.forEach((sku, processOrderMaterialPos) -> {
            RepairOrderPrintResultVo.OutboundDetailInfoVo vo = new RepairOrderPrintResultVo.OutboundDetailInfoVo();
            vo.setSku(sku);

            PlmSkuVo plmSkuVo = skuEncodes.stream()
                    .filter(skuEncode -> Objects.equals(sku, skuEncode.getSkuCode()))
                    .findFirst()
                    .orElse(null);
            vo.setSkuEncode(Objects.nonNull(plmSkuVo) ? plmSkuVo.getSkuEncode() : "");

            int deliverCnt = processOrderMaterialPos.stream()
                    .mapToInt(ProcessOrderMaterialPo::getDeliveryNum)
                    .sum();
            vo.setDeliverCnt(deliverCnt);

            resultList.add(vo);
        });

        return resultList;
    }

    public static ReceiveOrderCreateMqDto.ReceiveOrderCreateItem buildReceiveOrderCreateItem(String repairOrderNo,
                                                                                             String key,
                                                                                             String warehouseCode) {
        ReceiveOrderCreateMqDto.ReceiveOrderCreateItem receiveOrderCreateItem
                = new ReceiveOrderCreateMqDto.ReceiveOrderCreateItem();

        receiveOrderCreateItem.setReceiveType(ReceiveType.DEFECTIVE_PROCESS_PRODUCT);
        receiveOrderCreateItem.setUnionKey(
                key + ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK + ScmBizReceiveOrderType.REPAIR_RAW.name());
        receiveOrderCreateItem.setQcType(WmsEnum.QcType.NOT_CHECK);

        receiveOrderCreateItem.setWarehouseCode(warehouseCode);
        receiveOrderCreateItem.setSkuDevType(SkuDevType.NORMAL);
        receiveOrderCreateItem.setScmBizNo(repairOrderNo);
        receiveOrderCreateItem.setPurchaseOrderType(PurchaseOrderType.NORMAL);
        receiveOrderCreateItem.setIsUrgentOrder(BooleanType.FALSE);
        receiveOrderCreateItem.setIsDirectSend(BooleanType.FALSE);
        receiveOrderCreateItem.setIsNormalOrder(BooleanType.FALSE);
        receiveOrderCreateItem.setPlaceOrderTime(LocalDateTime.now());
        receiveOrderCreateItem.setOperator(GlobalContext.getUserKey());
        receiveOrderCreateItem.setOperatorName(GlobalContext.getUsername());
        receiveOrderCreateItem.setSendTime(new DateTime().toLocalDateTime());
        return receiveOrderCreateItem;
    }

    public static List<ReceiveOrderCreateMqDto.ReceiveOrderDetail> buildReceiveOrderDetail(Map<String, String> spuMap,
                                                                                           String platform,
                                                                                           List<SubmitReturnMaterialRequestDto.MaterialReturnInfoVo> materialReturnInfoList) {
        return materialReturnInfoList.stream()
                .map(materialReturnInfoVo -> {
                    ReceiveOrderCreateMqDto.ReceiveOrderDetail receiveOrderDetail = new ReceiveOrderCreateMqDto.ReceiveOrderDetail();
                    receiveOrderDetail.setPlatCode(platform);
                    receiveOrderDetail.setBatchCode(materialReturnInfoVo.getBatchCode());
                    receiveOrderDetail.setSkuCode(materialReturnInfoVo.getSku());
                    receiveOrderDetail.setSpu(spuMap.get(receiveOrderDetail.getSkuCode()));
                    receiveOrderDetail.setPurchaseAmount(materialReturnInfoVo.getReturnQuantity());
                    receiveOrderDetail.setDeliveryAmount(materialReturnInfoVo.getReturnQuantity());
                    return receiveOrderDetail;
                })
                .collect(Collectors.toList());
    }

    public static ReceiveOrderCreateMqDto buildReceiveOrderCreateMqDto(String msgKey,
                                                                       ReceiveOrderCreateMqDto.ReceiveOrderCreateItem receiveOrderCreateItem) {
        ReceiveOrderCreateMqDto receiveOrderCreateMqDto = new ReceiveOrderCreateMqDto();
        receiveOrderCreateMqDto.setKey(msgKey);

        receiveOrderCreateMqDto.setReceiveOrderCreateItemList(List.of(receiveOrderCreateItem));
        return receiveOrderCreateMqDto;
    }

    public static ProcessMaterialBackPo buildProcessMaterialBackPo(String repairOrderNo,
                                                                   String msgKey) {
        ProcessMaterialBackPo processMaterialBackPo = new ProcessMaterialBackPo();
        processMaterialBackPo.setRepairOrderNo(repairOrderNo);
        processMaterialBackPo.setMessageKey(msgKey);
        processMaterialBackPo.setBackStatus(BackStatus.WAIT_BACK);
        return processMaterialBackPo;
    }

    public static List<ProcessMaterialBackItemPo> buildProcessMaterialBackItemPos(Long processMaterialBackId,
                                                                                  List<SubmitReturnMaterialRequestDto.MaterialReturnInfoVo> materialReturnInfoList) {
        return materialReturnInfoList.stream()
                .map(materialReturnInfo -> {
                    ProcessMaterialBackItemPo processMaterialBackItemPo = new ProcessMaterialBackItemPo();
                    processMaterialBackItemPo.setSkuBatchCode(materialReturnInfo.getBatchCode());
                    processMaterialBackItemPo.setDeliveryNum(materialReturnInfo.getReturnQuantity());
                    processMaterialBackItemPo.setProcessMaterialBackId(processMaterialBackId);
                    return processMaterialBackItemPo;
                })
                .collect(Collectors.toList());
    }

    public static List<RepairOrderPrintProductReceiptVo> buildRepairOrderPrintProductReceiptVos(List<RepairOrderPo> printRepairOrderPos) {
        return CollectionUtils.isEmpty(printRepairOrderPos) ? Collections.emptyList() : printRepairOrderPos.stream()
                .map(printRepairOrderPo -> {
                    RepairOrderPrintProductReceiptVo printVo = new RepairOrderPrintProductReceiptVo();
                    printVo.setRepairOrderNo(printRepairOrderPo.getRepairOrderNo());

                    RepairOrderPrintProductReceiptVo.ProductReceiptInfoVo receiptInfoVo
                            = new RepairOrderPrintProductReceiptVo.ProductReceiptInfoVo();
                    receiptInfoVo.setReceiptOrderNo(printRepairOrderPo.getReceiveOrderNo());
                    receiptInfoVo.setWarehouseCode(printRepairOrderPo.getExpectWarehouseCode());
                    printVo.setProductReceiptInfoList(List.of(receiptInfoVo));
                    return printVo;
                })
                .collect(Collectors.toList());
    }

    public static ReceiveOrderGetDto buildReceiveOrderGetDto(List<String> queryReceiveOrderNos) {
        if (CollectionUtils.isEmpty(queryReceiveOrderNos)) {
            return null;
        }

        ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
        receiveOrderGetDto.setReceiveOrderNoList(queryReceiveOrderNos);
        return receiveOrderGetDto;
    }

    public static List<RepairOrderPrintMaterialReturnVo> buildRepairOrderPrintMaterialReturnVos(Map<String, List<String>> repairOrderBackReceiptNosMap,
                                                                                                Map<String, String> receiveOrderWarehouseCodeMap) {
        if (CollectionUtils.isEmpty(receiveOrderWarehouseCodeMap)) {
            return Collections.emptyList();
        }

        List<RepairOrderPrintMaterialReturnVo> resultVos = Lists.newArrayList();
        repairOrderBackReceiptNosMap.forEach((repairOrderNo, receiptNos) -> {
            RepairOrderPrintMaterialReturnVo repairOrderPrintMaterialReturnVo = new RepairOrderPrintMaterialReturnVo();
            repairOrderPrintMaterialReturnVo.setRepairOrderNo(repairOrderNo);

            List<RepairOrderPrintMaterialReturnVo.PrintMaterialReturnInfoVo> materialReturnInfoList = receiptNos.stream()
                    .map(receiptNo -> {
                        RepairOrderPrintMaterialReturnVo.PrintMaterialReturnInfoVo printMaterialReturnInfoVo
                                = new RepairOrderPrintMaterialReturnVo.PrintMaterialReturnInfoVo();
                        printMaterialReturnInfoVo.setReceiptOrderNo(receiptNo);
                        printMaterialReturnInfoVo.setWarehouseCode(receiveOrderWarehouseCodeMap.getOrDefault(receiptNo, ""));
                        return printMaterialReturnInfoVo;
                    })
                    .collect(Collectors.toList());
            repairOrderPrintMaterialReturnVo.setPrintMaterialReturnInfoList(materialReturnInfoList);

            resultVos.add(repairOrderPrintMaterialReturnVo);
        });

        return resultVos;
    }
}
