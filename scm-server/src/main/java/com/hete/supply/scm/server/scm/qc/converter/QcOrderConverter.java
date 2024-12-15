package com.hete.supply.scm.server.scm.qc.converter;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.hete.supply.plm.api.goods.entity.vo.PlmSkuImage;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.entity.vo.QcExportVo;
import com.hete.supply.scm.api.scm.entity.vo.QcOrderDetailVo;
import com.hete.supply.scm.api.scm.entity.vo.QcOrderVo;
import com.hete.supply.scm.api.scm.entity.vo.QcVo;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.common.util.StringUtil;
import com.hete.supply.scm.server.scm.entity.dto.QcOrderCreateMqDto;
import com.hete.supply.scm.server.scm.entity.po.DefectHandlingPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProduceDataSpecBatchVo;
import com.hete.supply.scm.server.scm.qc.entity.bo.*;
import com.hete.supply.scm.server.scm.qc.entity.dto.*;
import com.hete.supply.scm.server.scm.qc.entity.po.*;
import com.hete.supply.scm.server.scm.qc.entity.vo.*;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.supplier.entity.bo.ReturnOrderBo;
import com.hete.supply.scm.server.supplier.entity.bo.ReturnOrderItemBo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderItemPo;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.entry.entity.vo.ReceiveOrderForScmVo;
import com.hete.support.api.exception.BizException;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.FormatStringUtil;
import com.hete.support.core.util.TimeZoneId;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
public class QcOrderConverter {
    private static final String RECEIVE_ORDER_NO_PREFIX = "SH";
    private static final Integer DB_DEFAULT_YEAR = 1970;

    public static ProcessOrderQcOrderFinishedBo toProcessOrderQcOrderBo(QcOrderPo qcOrderPo,
                                                                        List<QcDetailPo> qcDetailPos) {
        if (Objects.isNull(qcOrderPo)) {
            return null;
        }

        Map<String, List<QcDetailPo>> qcDetailPoMap = qcDetailPos.stream()
                .collect(Collectors.groupingBy(QcDetailPo::getBatchCode));
        List<ProcessOrderQcOrderDetailFinishedBo> processOrderQcOrderDetailFinishedBos = Lists.newArrayList();
        qcDetailPoMap.forEach((batchCode, qcDetailPosTmp) -> {
            int passAmount = qcDetailPosTmp.stream()
                    .mapToInt(QcDetailPo::getPassAmount)
                    .sum();
            int notPassAmount = qcDetailPosTmp.stream()
                    .mapToInt(QcDetailPo::getNotPassAmount)
                    .sum();

            ProcessOrderQcOrderDetailFinishedBo processOrderQcOrderDetailFinishedBo
                    = new ProcessOrderQcOrderDetailFinishedBo();
            processOrderQcOrderDetailFinishedBo.setBatchCode(batchCode);
            processOrderQcOrderDetailFinishedBo.setPassAmount(passAmount);
            processOrderQcOrderDetailFinishedBo.setNotPassAmount(notPassAmount);
            processOrderQcOrderDetailFinishedBos.add(processOrderQcOrderDetailFinishedBo);
        });

        final QcDetailPo qcDetailPo = qcDetailPos.get(0);
        ProcessOrderQcOrderFinishedBo resultBo = new ProcessOrderQcOrderFinishedBo();
        resultBo.setProcessOrderNo(qcOrderPo.getProcessOrderNo());
        resultBo.setPlatform(qcDetailPo.getPlatform());
        resultBo.setQcOrderNo(qcOrderPo.getQcOrderNo());
        resultBo.setBizTime(LocalDateTime.now());
        resultBo.setQcOrderDetails(processOrderQcOrderDetailFinishedBos);
        resultBo.setOperator(qcOrderPo.getOperator());
        resultBo.setOperatorName(qcOrderPo.getOperatorName());
        return resultBo;
    }


    public static QcOrderChangeDto toQcOrderChangeDto(QcOrderPo qcOrderPo,
                                                      List<QcDetailPo> qcDetailPos) {
        if (Objects.isNull(qcOrderPo)) {
            return null;
        }
        if (CollectionUtils.isEmpty(qcDetailPos)) {
            return null;
        }
        final QcDetailPo qcDetailPo = qcDetailPos.get(0);


        QcOrderChangeDto qcOrderChangeDto = new QcOrderChangeDto();
        qcOrderChangeDto.setQcOrderId(qcOrderPo.getQcOrderId());
        qcOrderChangeDto.setQcOrderNo(qcOrderPo.getQcOrderNo());
        qcOrderChangeDto.setPlatform(qcDetailPo.getPlatform());
        qcOrderChangeDto.setWarehouseCode(qcOrderPo.getWarehouseCode());
        qcOrderChangeDto.setReceiveOrderNo(qcOrderPo.getReceiveOrderNo());
        qcOrderChangeDto.setProcessOrderNo(qcOrderPo.getProcessOrderNo());
        qcOrderChangeDto.setQcType(qcOrderPo.getQcType());
        qcOrderChangeDto.setQcAmount(qcOrderPo.getQcAmount());
        qcOrderChangeDto.setQcState(qcOrderPo.getQcState());
        qcOrderChangeDto.setQcResult(qcOrderPo.getQcResult());
        qcOrderChangeDto.setHandOverTime(qcOrderPo.getHandOverTime());
        qcOrderChangeDto.setTaskFinishTime(qcOrderPo.getTaskFinishTime());
        qcOrderChangeDto.setAuditTime(qcOrderPo.getAuditTime());
        qcOrderChangeDto.setHandOverUser(qcOrderPo.getHandOverUser());
        qcOrderChangeDto.setOperator(qcOrderPo.getOperator());
        qcOrderChangeDto.setOperatorName(qcOrderPo.getOperatorName());
        qcOrderChangeDto.setAuditor(qcOrderPo.getAuditor());
        qcOrderChangeDto.setSkuDevType(qcOrderPo.getSkuDevType());
        qcOrderChangeDto.setCreateUser(qcOrderPo.getCreateUser());
        qcOrderChangeDto.setCreateTime(qcOrderPo.getCreateTime());
        qcOrderChangeDto.setCreateUsername(qcOrderPo.getCreateUsername());
        qcOrderChangeDto.setUpdateUser(qcOrderPo.getUpdateUser());
        qcOrderChangeDto.setUpdateTime(qcOrderPo.getUpdateTime());
        qcOrderChangeDto.setUpdateUsername(qcOrderPo.getUpdateUsername());

        if (CollectionUtils.isNotEmpty(qcDetailPos)) {
            List<QcOrderDetailChangeDto> qcOrderDetails = qcDetailPos.stream()
                    .map(detailPo -> {
                        QcOrderDetailChangeDto qcOrderDetailChangeDto = new QcOrderDetailChangeDto();
                        qcOrderDetailChangeDto.setQcDetailId(detailPo.getQcDetailId());
                        qcOrderDetailChangeDto.setQcOrderNo(detailPo.getQcOrderNo());
                        qcOrderDetailChangeDto.setContainerCode(detailPo.getContainerCode());
                        qcOrderDetailChangeDto.setBatchCode(detailPo.getBatchCode());
                        qcOrderDetailChangeDto.setSpu(detailPo.getSpu());
                        qcOrderDetailChangeDto.setSkuCode(detailPo.getSkuCode());
                        qcOrderDetailChangeDto.setAmount(detailPo.getAmount());
                        qcOrderDetailChangeDto.setWaitAmount(detailPo.getWaitAmount());
                        qcOrderDetailChangeDto.setPassAmount(detailPo.getPassAmount());
                        qcOrderDetailChangeDto.setNotPassAmount(detailPo.getNotPassAmount());
                        qcOrderDetailChangeDto.setQcResult(detailPo.getQcResult());
                        qcOrderDetailChangeDto.setQcNotPassedReason(detailPo.getQcNotPassedReason());
                        qcOrderDetailChangeDto.setRemark(detailPo.getRemark());
                        qcOrderDetailChangeDto.setPictureIds(detailPo.getPictureIds());
                        qcOrderDetailChangeDto.setWeight(detailPo.getWeight());
                        qcOrderDetailChangeDto.setRelationQcDetailId(detailPo.getRelationQcDetailId());
                        qcOrderDetailChangeDto.setCreateUsername(detailPo.getCreateUsername());
                        qcOrderDetailChangeDto.setUpdateUsername(detailPo.getUpdateUsername());
                        return qcOrderDetailChangeDto;
                    })
                    .collect(Collectors.toList());
            qcOrderChangeDto.setQcOrderDetails(qcOrderDetails);
        }
        return qcOrderChangeDto;
    }

    public static QcOrderChangeDto toQcOrderChangeDto(QcOrderPo qcOrderPo,
                                                      List<QcDetailPo> qcDetailPos,
                                                      WmsEnum.ReceiveType receiveType,
                                                      String supplierCode,
                                                      String deliveryOrderNo,
                                                      String goodsCategory,
                                                      String onShelvesOrderNo,
                                                      String deliverOrderNo) {
        QcOrderChangeDto qcOrderChangeDto = toQcOrderChangeDto(qcOrderPo, qcDetailPos);
        if (Objects.isNull(qcOrderChangeDto)) {
            return null;
        }
        final QcDetailPo qcDetailPo = qcDetailPos.get(0);
        qcOrderChangeDto.setPlatform(qcDetailPo.getPlatform());
        qcOrderChangeDto.setReceiveType(receiveType);
        qcOrderChangeDto.setSupplierCode(supplierCode);
        qcOrderChangeDto.setDeliveryOrderNo(deliveryOrderNo);
        qcOrderChangeDto.setGoodsCategory(goodsCategory);
        qcOrderChangeDto.setOnShelvesOrderNo(onShelvesOrderNo);
        qcOrderChangeDto.setDeliverOrderNo(deliverOrderNo);
        return qcOrderChangeDto;
    }


    public static List<QcSearchVo> qcOrderPoToSearchVo(List<QcSearchVo> qcOrderPoList,
                                                       List<QcDetailPo> qcDetailPoList,
                                                       List<QcReceiveOrderPo> qcReceiveOrderPoList,
                                                       List<QcOnShelvesOrderPo> qcOnShelvesOrderPoList,
                                                       Map<String, String> skuEncodeMap,
                                                       List<DefectHandlingPo> defectHandlingPoList,
                                                       Map<String, ReceiveOrderForScmVo> receiveOrderNoVoMap,
                                                       List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList,
                                                       List<QcDetailVo> qcDetailVoList,
                                                       List<QcOrigin> residentQcOrigins) {
        if (CollectionUtils.isEmpty(qcOrderPoList) || CollectionUtils.isEmpty(qcDetailPoList)) {
            return Collections.emptyList();
        }

        final Map<String, List<QcDetailPo>> qcOrderNoDetailPoMap = qcDetailPoList.stream()
                .collect(Collectors.groupingBy(QcDetailPo::getQcOrderNo));

        final Map<String, QcReceiveOrderPo> qcOrderNoReceivePoMap = qcReceiveOrderPoList.stream()
                .collect(Collectors.toMap(QcReceiveOrderPo::getQcOrderNo, Function.identity()));

        final Map<String, QcOnShelvesOrderPo> qcOrderNoOnShelvesPoMap = qcOnShelvesOrderPoList.stream()
                .filter(qcOnShelvesOrderPo -> !WmsEnum.OnShelvesOrderCreateType.CONCESSION.equals(
                        qcOnShelvesOrderPo.getType()))
                .collect(Collectors.toMap(QcOnShelvesOrderPo::getQcOrderNo, Function.identity()));

        final Map<String, List<String>> qcOrderNoOnShelvesNoListMap = qcOnShelvesOrderPoList.stream()
                .collect(Collectors.groupingBy(QcOnShelvesOrderPo::getQcOrderNo,
                        Collectors.mapping(QcOnShelvesOrderPo::getOnShelvesOrderNo, Collectors.toList())));

        final Map<String, List<DefectHandlingPo>> qcOrderNoDefectPoMap = defectHandlingPoList.stream()
                .collect(Collectors.groupingBy(DefectHandlingPo::getQcOrderNo));

        final Map<String, List<String>> qcOrderNoReturnOrderNoMap = qcOrderNoDefectPoMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> entry.getValue()
                                .stream()
                                .map(DefectHandlingPo::getReturnOrderNo)
                                .filter(StringUtils::isNotBlank)
                                .collect(Collectors.toList())));
        final Map<String, QcDetailVo> qcDetailVoMap = qcDetailVoList.stream()
                .collect(Collectors.toMap(QcDetailVo::getQcOrderNo, Function.identity()));

        final Map<String, List<String>> qcOrderNoReceiveOrderNoMap = qcOrderNoDefectPoMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> entry.getValue()
                                .stream()
                                .map(DefectHandlingPo::getRelatedOrderNo)
                                .filter(StringUtils::isNotBlank)
                                .filter(relatedOrderNo -> relatedOrderNo.startsWith(RECEIVE_ORDER_NO_PREFIX))
                                .distinct()
                                .collect(Collectors.toList())));

        final Map<String, Integer> returnSupplyNotPassCntMap = defectHandlingPoList.stream()
                .filter(defectHandlingPo -> DefectHandlingProgramme.RETURN_SUPPLY.equals(
                        defectHandlingPo.getDefectHandlingProgramme()))
                .collect(Collectors.groupingBy(DefectHandlingPo::getQcOrderNo,
                        Collectors.summingInt(DefectHandlingPo::getNotPassCnt)));

        final Map<String, Integer> compromiseNotPassCntMap = defectHandlingPoList.stream()
                .filter(defectHandlingPo -> DefectHandlingProgramme.COMPROMISE.equals(
                        defectHandlingPo.getDefectHandlingProgramme()))
                .filter(defectHandlingPo -> DefectHandlingStatus.CONFIRMED.equals(
                        defectHandlingPo.getDefectHandlingStatus()))
                .collect(Collectors.groupingBy(DefectHandlingPo::getQcOrderNo,
                        Collectors.summingInt(DefectHandlingPo::getNotPassCnt)));

        final Map<String, Integer> scrappedNotPassCntMap = defectHandlingPoList.stream()
                .filter(defectHandlingPo -> DefectHandlingProgramme.SCRAPPED.equals(
                        defectHandlingPo.getDefectHandlingProgramme()))
                .collect(Collectors.groupingBy(DefectHandlingPo::getQcOrderNo,
                        Collectors.summingInt(DefectHandlingPo::getNotPassCnt)));

        final Map<String, Integer> exchangeNotPassCntMap = defectHandlingPoList.stream()
                .filter(defectHandlingPo -> DefectHandlingProgramme.EXCHANGE_GOODS.equals(
                        defectHandlingPo.getDefectHandlingProgramme()))
                .collect(Collectors.groupingBy(DefectHandlingPo::getQcOrderNo,
                        Collectors.summingInt(DefectHandlingPo::getNotPassCnt)));

        final Map<String, List<PurchaseReturnOrderItemPo>> qcOrderNoReturnItemPoListMap
                = purchaseReturnOrderItemPoList.stream()
                .collect(Collectors.groupingBy(PurchaseReturnOrderItemPo::getReturnBizNo));


        return qcOrderPoList.stream()
                .map(qcOrderPo -> {
                    // 获取质检资料相关信息
                    final List<QcDetailPo> qcDetailPoList1 = qcOrderNoDetailPoMap.get(qcOrderPo.getQcOrderNo());
                    if (null == qcDetailPoList1) {
                        throw new BizException("质检单:{}缺失详情信息，数据错误，请联系系统管理员",
                                qcOrderPo.getQcOrderNo());
                    }
                    final QcDetailPo qcDetailPo = qcDetailPoList1.get(0);

                    final QcReceiveOrderPo qcReceiveOrderPo = qcOrderNoReceivePoMap.get(qcOrderPo.getQcOrderNo());
                    final QcOnShelvesOrderPo qcOnShelvesOrderPo = qcOrderNoOnShelvesPoMap.get(qcOrderPo.getQcOrderNo());

                    // 组装详情信息
                    final List<QcContainerVo> qcContainerList = qcDetailPoList1.stream()
                            .map(qcDetailPo1 -> {
                                final QcContainerVo qcContainerVo = new QcContainerVo();
                                qcContainerVo.setContainerCode(qcDetailPo1.getContainerCode());
                                qcContainerVo.setAmount(qcDetailPo1.getPassAmount() + qcDetailPo1.getNotPassAmount());
                                return qcContainerVo;
                            })
                            .collect(Collectors.groupingBy(QcContainerVo::getContainerCode,
                                    Collectors.summingInt(QcContainerVo::getAmount)))
                            .entrySet()
                            .stream()
                            .map(entry -> {
                                final QcContainerVo qcContainerVo = new QcContainerVo();
                                qcContainerVo.setContainerCode(entry.getKey());
                                qcContainerVo.setAmount(entry.getValue());
                                return qcContainerVo;
                            })
                            .collect(Collectors.toList());
                    final int waitAmount = qcDetailPoList1.stream()
                            .mapToInt(QcDetailPo::getWaitAmount)
                            .sum();
                    final int passAmount = qcDetailPoList1.stream()
                            .mapToInt(QcDetailPo::getPassAmount)
                            .sum();
                    final int notPassAmount = qcDetailPoList1.stream()
                            .mapToInt(QcDetailPo::getNotPassAmount)
                            .sum();


                    final QcSearchVo qcSearchVo = new QcSearchVo();
                    qcSearchVo.setQcOrderId(qcOrderPo.getQcOrderId());
                    qcSearchVo.setVersion(qcOrderPo.getVersion());
                    qcSearchVo.setQcOrderNo(qcOrderPo.getQcOrderNo());
                    qcSearchVo.setQcType(qcOrderPo.getQcType());
                    qcSearchVo.setQcState(qcOrderPo.getQcState());
                    qcSearchVo.setSku(qcDetailPo.getSkuCode());
                    qcSearchVo.setSkuEncode(skuEncodeMap.get(qcDetailPo.getSkuCode()));
                    qcSearchVo.setSkuBatchCode(qcDetailPo.getBatchCode());
                    qcSearchVo.setProcessOrderNo(qcOrderPo.getProcessOrderNo());
                    qcSearchVo.setRepairOrderNo(qcOrderPo.getRepairOrderNo());
                    qcSearchVo.setWarehouseCode(qcOrderPo.getWarehouseCode());
                    qcSearchVo.setCreateTime(qcOrderPo.getCreateTime());
                    qcSearchVo.setAmount(qcOrderPo.getQcAmount());
                    qcSearchVo.setHandOverTime(qcOrderPo.getHandOverTime());
                    qcSearchVo.setOperator(qcOrderPo.getOperator());
                    qcSearchVo.setOperatorName(qcOrderPo.getOperatorName());
                    qcSearchVo.setTaskFinishTime(qcOrderPo.getTaskFinishTime());
                    qcSearchVo.setQcContainerList(qcContainerList);
                    qcSearchVo.setWaitAmount(waitAmount);
                    qcSearchVo.setPassAmount(passAmount);
                    qcSearchVo.setNotPassAmount(notPassAmount);
                    qcSearchVo.setQcResult(qcOrderPo.getQcResult());
                    qcSearchVo.setHandOverUser(qcOrderPo.getHandOverUser());
                    qcSearchVo.setAuditor(qcOrderPo.getAuditor());
                    qcSearchVo.setAuditTime(qcOrderPo.getAuditTime());
                    qcSearchVo.setIsUrgentOrder(qcOrderPo.getIsUrgentOrder());
                    qcSearchVo.setQcOrigin(qcOrderPo.getQcOrigin());
                    qcSearchVo.setQcOriginProperty(qcOrderPo.getQcOriginProperty());
                    qcSearchVo.setQcSourceOrderNo(qcOrderPo.getQcSourceOrderNo());
                    qcSearchVo.setQcSourceOrderType(qcOrderPo.getQcSourceOrderType());

                    QcDetailVo qcDetailVo = qcDetailVoMap.get(qcOrderPo.getQcOrderNo());
                    qcSearchVo.setQcDetailHandItemList(Objects.nonNull(qcDetailVo) ?
                            qcDetailVo.getQcDetailHandItemList() : Collections.emptyList());
                    qcSearchVo.setQcUnPassDetailItemList(Objects.nonNull(qcDetailVo) ?
                            qcDetailVo.getQcUnPassDetailItemList() : Collections.emptyList());

                    BigDecimal qcPassRate = new BigDecimal(passAmount)
                            .divide(new BigDecimal(qcOrderPo.getQcAmount()),
                                    4, RoundingMode.DOWN);
                    qcSearchVo.setQcPassRate(qcPassRate.multiply(BigDecimal.TEN.multiply(BigDecimal.TEN))
                            .setScale(2, RoundingMode.DOWN));
                    if (null != qcOnShelvesOrderPo) {
                        qcSearchVo.setPlanAmount(qcOnShelvesOrderPo.getPlanAmount());
                    }
                    qcSearchVo.setOnShelvesOrderNoList(qcOrderNoOnShelvesNoListMap.get(qcOrderPo.getQcOrderNo()));
                    List<String> returnOrderList = new ArrayList<>();
                    if (null != qcReceiveOrderPo) {
                        qcSearchVo.setReceiveType(qcReceiveOrderPo.getReceiveType());
                        qcSearchVo.setGoodsCategory(qcReceiveOrderPo.getGoodsCategory());
                        qcSearchVo.setSupplierCode(qcReceiveOrderPo.getSupplierCode());
                        qcSearchVo.setReceiveOrderNo(qcReceiveOrderPo.getReceiveOrderNo());
                        qcSearchVo.setScmBizNo(qcReceiveOrderPo.getScmBizNo());
                    }
                    if (Objects.isNull(qcSearchVo.getSupplierCode())) {
                        qcSearchVo.setSupplierCode(qcOrderPo.getSupplierCode());
                    }
                    final List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList1
                            = qcOrderNoReturnItemPoListMap.get(qcOrderPo.getQcOrderNo());
                    if (CollectionUtils.isNotEmpty(purchaseReturnOrderItemPoList1)) {
                        final List<String> returnOrderNoList = purchaseReturnOrderItemPoList1.stream()
                                .map(PurchaseReturnOrderItemPo::getReturnOrderNo)
                                .collect(Collectors.toList());
                        returnOrderList.addAll(returnOrderNoList);
                        final int returnCntSum = purchaseReturnOrderItemPoList1.stream()
                                .mapToInt(PurchaseReturnOrderItemPo::getExpectedReturnCnt)
                                .sum();
                        qcSearchVo.setReturnCnt(returnCntSum);
                    }

                    qcSearchVo.setReturnCnt(Optional.ofNullable(qcSearchVo.getReturnCnt())
                            .orElse(0)
                            + returnSupplyNotPassCntMap.getOrDefault(qcOrderPo.getQcOrderNo(), 0));
                    qcSearchVo.setCompromiseCnt(compromiseNotPassCntMap.getOrDefault(qcOrderPo.getQcOrderNo(), 0));
                    qcSearchVo.setScrapCnt(scrappedNotPassCntMap.getOrDefault(qcOrderPo.getQcOrderNo(), 0));
                    qcSearchVo.setExchangeCnt(exchangeNotPassCntMap.getOrDefault(qcOrderPo.getQcOrderNo(), 0));
                    returnOrderList.addAll(qcOrderNoReturnOrderNoMap.getOrDefault(qcOrderPo.getQcOrderNo(), new ArrayList<>()));
                    qcSearchVo.setReturnOrderNoList(returnOrderList.stream()
                            .distinct()
                            .collect(Collectors.toList()));
                    qcSearchVo.setReceiveOrderNoList(qcOrderNoReceiveOrderNoMap.get(qcOrderPo.getQcOrderNo()));
                    qcSearchVo.setQcDetailSize((int) qcDetailPoList1.stream()
                            .map(QcDetailPo::getBatchCode)
                            .distinct()
                            .count());
                    qcSearchVo.setPurchaseChildOrderNo(qcOrderPo.getPurchaseChildOrderNo());

                    return qcSearchVo;
                })
                .collect(Collectors.toList());
    }

    public static ProcessOrderCreateQcBo toProcessOrderCreateQcBo(QcOrderCreateMqDto qcOrderCreateMqDto) {
        if (Objects.isNull(qcOrderCreateMqDto)) {
            return null;
        }
        final List<QcOrderCreateMqDto.QcOrderCreateDto> qcOrderCreateDtoList
                = qcOrderCreateMqDto.getQcOrderCreateDtoList();
        List<ProcessOrderCreateQcOrderBo> createQcOrderBos = qcOrderCreateDtoList.stream()
                .map(qcOrderCreateDto -> {
                    ProcessOrderCreateQcOrderBo qcOrderBo = new ProcessOrderCreateQcOrderBo();
                    // Set the warehouseCode
                    qcOrderBo.setWarehouseCode(qcOrderCreateDto.getWarehouseCode());
                    // Set the processOrderNo
                    qcOrderBo.setProcessOrderNo(qcOrderCreateDto.getProcessOrderNo());
                    qcOrderBo.setProcessOrderType(qcOrderCreateDto.getProcessOrderType());
                    // Set the qcType
                    qcOrderBo.setQcType(qcOrderCreateDto.getQcType());
                    // Set the operator
                    qcOrderBo.setOperator(qcOrderCreateDto.getOperator());
                    // Set the operatorName
                    qcOrderBo.setOperatorName(qcOrderCreateDto.getOperatorName());
                    qcOrderBo.setPlatform(qcOrderCreateDto.getPlatform());

                    // Set the goodDetailList
                    final List<QcOrderCreateMqDto.GoodDetail> goodDetailList = qcOrderCreateDto.getGoodDetailList();
                    if (CollectionUtils.isNotEmpty(goodDetailList)) {
                        List<ProcessOrderCreateQcOrderDetailBo> qcOrderDetails = goodDetailList.stream()
                                .map(goodDetail -> {
                                    ProcessOrderCreateQcOrderDetailBo qcOrderDetailBo
                                            = new ProcessOrderCreateQcOrderDetailBo();
                                    // Set the containerCode
                                    qcOrderDetailBo.setContainerCode(goodDetail.getContainerCode());
                                    // Set the batchCode
                                    qcOrderDetailBo.setBatchCode(goodDetail.getBatchCode());
                                    // Set the skuCode
                                    qcOrderDetailBo.setSkuCode(goodDetail.getSkuCode());
                                    // Set the amount
                                    qcOrderDetailBo.setAmount(goodDetail.getAmount());
                                    return qcOrderDetailBo;
                                })
                                .collect(Collectors.toList());
                        qcOrderBo.setQcOrderDetails(qcOrderDetails);
                    }
                    return qcOrderBo;
                })
                .collect(Collectors.toList());

        ProcessOrderCreateQcBo processOrderCreateQcBo = new ProcessOrderCreateQcBo();
        processOrderCreateQcBo.setCreateQcOrderBos(createQcOrderBos);

        return processOrderCreateQcBo;
    }

    public static QcDetailVo qcOrderPoToDetailVo(QcOrderPo qcOrderPo,
                                                 List<QcDetailPo> qcDetailPoList,
                                                 QcReceiveOrderPo qcReceiveOrderPo,
                                                 Map<String, String> skuEncodeMap,
                                                 List<PlmSkuImage> plmSkuImageList,
                                                 List<String> skuList,
                                                 Map<String, ProduceDataSpecBatchVo> produceDataSpecBatchVoMap,
                                                 String spuTips) {

        final int passAmount = qcDetailPoList.stream()
                .mapToInt(QcDetailPo::getPassAmount)
                .sum();

        final List<QcDetailPo> notPassedQcDetailList = qcDetailPoList.stream()
                .filter(qcDetailPo -> QcResult.NOT_PASSED.equals(qcDetailPo.getQcResult()))
                .collect(Collectors.toList());

        final QcDetailVo qcDetailVo = new QcDetailVo();
        qcDetailVo.setQcOrderId(qcOrderPo.getQcOrderId());
        qcDetailVo.setVersion(qcOrderPo.getVersion());
        qcDetailVo.setQcOrderNo(qcOrderPo.getQcOrderNo());
        qcDetailVo.setProcessOrderNo(qcOrderPo.getProcessOrderNo());
        qcDetailVo.setRepairOrderNo(qcOrderPo.getRepairOrderNo());
        qcDetailVo.setPurchaseChildOrderNo(qcOrderPo.getPurchaseChildOrderNo());
        qcDetailVo.setQcType(qcOrderPo.getQcType());
        qcDetailVo.setWarehouseCode(qcOrderPo.getWarehouseCode());
        qcDetailVo.setAmount(qcOrderPo.getQcAmount());
        qcDetailVo.setOperator(qcOrderPo.getOperator());
        qcDetailVo.setOperatorName(qcOrderPo.getOperatorName());
        qcDetailVo.setHandOverTime(qcOrderPo.getHandOverTime());
        qcDetailVo.setTaskFinishTime(qcOrderPo.getTaskFinishTime());
        qcDetailVo.setQcResult(qcOrderPo.getQcResult());
        qcDetailVo.setPassAmount(passAmount);
        qcDetailVo.setQcState(qcOrderPo.getQcState());
        qcDetailVo.setHandOverUser(qcOrderPo.getHandOverUser());
        qcDetailVo.setQcOrigin(qcOrderPo.getQcOrigin());
        qcDetailVo.setQcOriginProperty(qcOrderPo.getQcOriginProperty());
        qcDetailVo.setQcSourceOrderNo(qcOrderPo.getQcSourceOrderNo());
        qcDetailVo.setQcSourceOrderType(qcOrderPo.getQcSourceOrderType());
        if (null != qcReceiveOrderPo) {
            qcDetailVo.setReceiveType(qcReceiveOrderPo.getReceiveType());
            qcDetailVo.setScmBizNo(qcReceiveOrderPo.getScmBizNo());
            qcDetailVo.setReceiveOrderNo(qcReceiveOrderPo.getReceiveOrderNo());
            qcDetailVo.setSupplierCode(qcReceiveOrderPo.getSupplierCode());
        }
        if (Objects.isNull(qcDetailVo.getSupplierCode())) {
            qcDetailVo.setSupplierCode(qcOrderPo.getSupplierCode());
        }

        final List<QcDetailHandItemVo> qcDetailHandItemVoList = qcDetailPoList.stream()
                .filter(qcDetailPo -> QcResult.PASSED.equals(qcDetailPo.getQcResult()))
                .map(qcDetailPo -> {
                    final QcDetailHandItemVo qcDetailHandItemVo = new QcDetailHandItemVo();
                    qcDetailHandItemVo.setQcDetailId(qcDetailPo.getQcDetailId());
                    qcDetailHandItemVo.setContainerCode(qcDetailPo.getContainerCode());
                    qcDetailHandItemVo.setSkuBatchCode(qcDetailPo.getBatchCode());
                    qcDetailHandItemVo.setSku(qcDetailPo.getSkuCode());
                    qcDetailHandItemVo.setSkuEncode(skuEncodeMap.get(qcDetailPo.getSkuCode()));
                    qcDetailHandItemVo.setAmount(qcDetailPo.getAmount());
                    qcDetailHandItemVo.setPassAmount(qcDetailPo.getPassAmount());
                    qcDetailHandItemVo.setRemark(qcDetailPo.getRemark());
                    qcDetailHandItemVo.setPlatform(qcDetailPo.getPlatform());
                    if (null != qcReceiveOrderPo) {
                        qcDetailHandItemVo.setGoodsCategory(qcReceiveOrderPo.getGoodsCategory());
                    }
                    return qcDetailHandItemVo;
                })
                .collect(Collectors.toList());
        qcDetailVo.setQcDetailHandItemList(qcDetailHandItemVoList);

        if (CollectionUtils.isNotEmpty(notPassedQcDetailList)) {
            final List<QcUnPassDetailItemVo> qcUnPassDetailItemVoList = notPassedQcDetailList.stream()
                    .map(qcDetailPo -> {
                        final QcUnPassDetailItemVo qcUnPassDetailItemVo = new QcUnPassDetailItemVo();
                        qcUnPassDetailItemVo.setNotPassAmount(qcDetailPo.getNotPassAmount());
                        qcUnPassDetailItemVo.setQcNotPassedReasonList(FormatStringUtil.string2List(qcDetailPo.getQcNotPassedReason(), ","));
                        qcUnPassDetailItemVo.setContainerCode(qcDetailPo.getContainerCode());
                        qcUnPassDetailItemVo.setProblemFileCodeList(
                                FormatStringUtil.string2List(qcDetailPo.getPictureIds(), ","));
                        qcUnPassDetailItemVo.setRelationQcDetailId(qcDetailPo.getRelationQcDetailId());
                        qcUnPassDetailItemVo.setRemark(qcDetailPo.getRemark());
                        qcUnPassDetailItemVo.setSku(qcDetailPo.getSkuCode());
                        qcUnPassDetailItemVo.setSkuBatchCode(qcDetailPo.getBatchCode());
                        qcUnPassDetailItemVo.setSkuEncode(skuEncodeMap.get(qcDetailPo.getSkuCode()));
                        return qcUnPassDetailItemVo;
                    })
                    .collect(Collectors.toList());
            qcDetailVo.setQcUnPassDetailItemList(qcUnPassDetailItemVoList);
        }

        final List<QcDetailProduceDataVo> qcDetailProduceDataList = Optional.ofNullable(skuList)
                .orElse(Collections.emptyList())
                .stream()
                .map(sku -> {
                    final QcDetailProduceDataVo qcDetailProduceDataVo = new QcDetailProduceDataVo();
                    qcDetailProduceDataVo.setSku(sku);
                    if (CollectionUtils.isNotEmpty(plmSkuImageList)) {
                        plmSkuImageList.stream()
                                .filter(plmSkuImage -> plmSkuImage.getSkuCode()
                                        .equals(sku))
                                .findFirst()
                                .ifPresent(plmSkuImageSku -> qcDetailProduceDataVo.setSaleFileCodeList(plmSkuImageSku.getSaleFileCodeList()));
                    }
                    ProduceDataSpecBatchVo produceDataSpecBatchVo = produceDataSpecBatchVoMap.get(sku);
                    if (produceDataSpecBatchVo != null) {
                        qcDetailProduceDataVo.setSealImageFileCodeList(produceDataSpecBatchVo.getSealImageFileCodeList());
                        qcDetailProduceDataVo.setProduceDataSpecList(produceDataSpecBatchVo.getProduceDataSpecList());
                    }
                    return qcDetailProduceDataVo;
                })
                .collect(Collectors.toList());

        qcDetailVo.setQcDetailProduceDataList(qcDetailProduceDataList);
        qcDetailVo.setSpuTips(spuTips);
        return qcDetailVo;
    }


    public static List<QcDetailVo> qcOrderPoToDetailVos(List<QcDetailPo> allQcDetailPoList,
                                                        Map<String, String> skuEncodeMap) {
        Map<String, List<QcDetailPo>> groupedByQcOrderNo = allQcDetailPoList.stream()
                .collect(Collectors.groupingBy(QcDetailPo::getQcOrderNo));

        List<QcDetailVo> qcDetailVoList = Lists.newArrayList();
        for (Map.Entry<String, List<QcDetailPo>> entry : groupedByQcOrderNo.entrySet()) {
            final String qcOrderNo = entry.getKey();
            List<QcDetailPo> qcDetailList = entry.getValue();

            QcDetailVo qcDetailVo = new QcDetailVo();
            qcDetailVo.setQcOrderNo(qcOrderNo);

            final List<QcDetailHandItemVo> qcDetailHandItemVoList = qcDetailList.stream()
                    .filter(qcDetailPo -> QcResult.PASSED.equals(qcDetailPo.getQcResult()))
                    .map(qcDetailPo -> {
                        final QcDetailHandItemVo qcDetailHandItemVo = new QcDetailHandItemVo();
                        qcDetailHandItemVo.setQcDetailId(qcDetailPo.getQcDetailId());
                        qcDetailHandItemVo.setContainerCode(qcDetailPo.getContainerCode());
                        qcDetailHandItemVo.setSkuBatchCode(qcDetailPo.getBatchCode());
                        qcDetailHandItemVo.setSku(qcDetailPo.getSkuCode());
                        qcDetailHandItemVo.setSkuEncode(skuEncodeMap.get(qcDetailPo.getSkuCode()));
                        qcDetailHandItemVo.setAmount(qcDetailPo.getAmount());
                        qcDetailHandItemVo.setPassAmount(qcDetailPo.getPassAmount());
                        qcDetailHandItemVo.setRemark(qcDetailPo.getRemark());
                        qcDetailHandItemVo.setPlatform(qcDetailPo.getPlatform());
                        return qcDetailHandItemVo;
                    })
                    .collect(Collectors.toList());
            qcDetailVo.setQcDetailHandItemList(qcDetailHandItemVoList);

            final List<QcDetailPo> notPassedQcDetailList = qcDetailList.stream()
                    .filter(qcDetailPo -> QcResult.NOT_PASSED.equals(qcDetailPo.getQcResult()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(notPassedQcDetailList)) {
                final List<QcUnPassDetailItemVo> qcUnPassDetailItemVoList = notPassedQcDetailList.stream()
                        .map(qcDetailPo -> {
                            final QcUnPassDetailItemVo qcUnPassDetailItemVo = new QcUnPassDetailItemVo();
                            qcUnPassDetailItemVo.setNotPassAmount(qcDetailPo.getNotPassAmount());
                            qcUnPassDetailItemVo.setQcNotPassedReasonList(FormatStringUtil.string2List(qcDetailPo.getQcNotPassedReason(), ","));
                            qcUnPassDetailItemVo.setContainerCode(qcDetailPo.getContainerCode());
                            qcUnPassDetailItemVo.setProblemFileCodeList(
                                    FormatStringUtil.string2List(qcDetailPo.getPictureIds(), ","));
                            qcUnPassDetailItemVo.setRelationQcDetailId(qcDetailPo.getRelationQcDetailId());
                            qcUnPassDetailItemVo.setRemark(qcDetailPo.getRemark());
                            qcUnPassDetailItemVo.setSku(qcDetailPo.getSkuCode());
                            qcUnPassDetailItemVo.setSkuBatchCode(qcDetailPo.getBatchCode());
                            qcUnPassDetailItemVo.setSkuEncode(skuEncodeMap.get(qcDetailPo.getSkuCode()));
                            return qcUnPassDetailItemVo;
                        })
                        .collect(Collectors.toList());
                qcDetailVo.setQcUnPassDetailItemList(qcUnPassDetailItemVoList);
            }
            qcDetailVoList.add(qcDetailVo);
        }
        return qcDetailVoList;
    }

    public static ReceiveOrderQcOrderBo toReceiveOrderQcOrderBo(ReceiveOrderQcOrderDto receiveOrderCreateQcDto) {
        if (Objects.isNull(receiveOrderCreateQcDto)) {
            return null;
        }

        ReceiveOrderQcOrderBo receiveOrderQcOrderBo = new ReceiveOrderQcOrderBo();
        // 收货单号
        receiveOrderQcOrderBo.setReceiveOrderNo(receiveOrderCreateQcDto.getReceiveOrderNo());
        // 质检类型
        receiveOrderQcOrderBo.setQcType(receiveOrderCreateQcDto.getQcType());
        // 质检结果
        receiveOrderQcOrderBo.setQcResult(receiveOrderCreateQcDto.getQcResult());
        // 质检数量
        receiveOrderQcOrderBo.setQcAmount(receiveOrderCreateQcDto.getTotalReceiveAmount());
        // 收货类型
        receiveOrderQcOrderBo.setReceiveType(receiveOrderCreateQcDto.getReceiveType());
        // 出库单号
        receiveOrderQcOrderBo.setDeliveryOrderNo(receiveOrderCreateQcDto.getDeliveryOrderNo());
        // 供应商编码
        receiveOrderQcOrderBo.setSupplierCode(receiveOrderCreateQcDto.getSupplierCode());
        // 商品/辅料类目
        receiveOrderQcOrderBo.setGoodsCategory(receiveOrderCreateQcDto.getGoodsCategory());
        // 仓库编码
        receiveOrderQcOrderBo.setWarehouseCode(receiveOrderCreateQcDto.getWarehouseCode());
        // 发货单号
        receiveOrderQcOrderBo.setDeliverOrderNo(receiveOrderCreateQcDto.getScmBizNo());
        // 上架单号
        receiveOrderQcOrderBo.setOnShelvesOrderNo(receiveOrderCreateQcDto.getOnShelvesOrderNo());
        // 创建人
        receiveOrderQcOrderBo.setCreateUser(receiveOrderCreateQcDto.getCreateUser());
        // 创建名称
        receiveOrderQcOrderBo.setCreateUsername(receiveOrderCreateQcDto.getCreateUsername());
        // 供应链单据号
        receiveOrderQcOrderBo.setScmBizNo(receiveOrderCreateQcDto.getScmBizNo());

        final List<ReceiveOrderQcOrderDetailDto> receiveQcOrderDetails = receiveOrderCreateQcDto.getReceiveDetailList();
        if (CollectionUtils.isNotEmpty(receiveQcOrderDetails)) {
            List<ReceiveOrderQcOrderDetailBo> qcOrderDetails = receiveQcOrderDetails.stream()
                    .map(receiveQcOrderDetail -> {
                        ReceiveOrderQcOrderDetailBo receiveOrderQcOrderDetailBo = new ReceiveOrderQcOrderDetailBo();
                        // 批次码
                        receiveOrderQcOrderDetailBo.setBatchCode(receiveQcOrderDetail.getBatchCode());
                        // 容器编码
                        receiveOrderQcOrderDetailBo.setContainerCode(receiveQcOrderDetail.getContainerCode());
                        // 赫特SKU
                        receiveOrderQcOrderDetailBo.setSkuCode(receiveQcOrderDetail.getSkuCode());
                        // 待质检数量
                        receiveOrderQcOrderDetailBo.setWaitAmount(receiveQcOrderDetail.getReceiveAmount());
                        receiveOrderQcOrderDetailBo.setPlatform(receiveQcOrderDetail.getPlatform());

                        return receiveOrderQcOrderDetailBo;
                    })
                    .collect(Collectors.toList());
            // 质检明细
            receiveOrderQcOrderBo.setQcOrderDetails(qcOrderDetails);
        }
        return receiveOrderQcOrderBo;
    }

    public static List<QcDefectConfigVo> qcOrderDefectConfigPoToVo(List<QcOrderDefectConfigPo> qcOrderDefectConfigPoList) {
        if (CollectionUtils.isEmpty(qcOrderDefectConfigPoList)) {
            return Collections.emptyList();
        }

        return qcOrderDefectConfigPoList.stream()
                .map(po -> {
                    final QcDefectConfigVo qcDefectConfigVo = new QcDefectConfigVo();
                    qcDefectConfigVo.setQcOrderDefectConfigId(po.getQcOrderDefectConfigId());
                    qcDefectConfigVo.setVersion(po.getVersion());
                    qcDefectConfigVo.setDefectCategory(po.getDefectCategory());
                    qcDefectConfigVo.setDefectCode(po.getDefectCode());
                    qcDefectConfigVo.setDefectStatus(po.getDefectStatus());
                    qcDefectConfigVo.setParentDefectConfigId(po.getParentDefectConfigId());

                    return qcDefectConfigVo;
                })
                .collect(Collectors.toList());
    }

    public static QcOrderDefectConfigPo qcDefectConfigDtoToPo(QcDefectConfigCreateDto dto) {
        final QcOrderDefectConfigPo qcOrderDefectConfigPo = new QcOrderDefectConfigPo();
        qcOrderDefectConfigPo.setDefectCategory(dto.getDefectCategory());
        qcOrderDefectConfigPo.setDefectCode(dto.getDefectCode());
        qcOrderDefectConfigPo.setDefectStatus(DefectStatus.ENABLED);
        qcOrderDefectConfigPo.setParentDefectConfigId(dto.getParentDefectConfigId());
        return qcOrderDefectConfigPo;
    }

    public static List<QcDetailPo> convertUnPassedDtoToPo(String qcOrderNo,
                                                          List<QcUnPassDetailItemDto> qcUnPassDetailItemList,
                                                          Map<Long, QcDetailPo> qcDetailIdPoMap) {
        if (CollectionUtils.isEmpty(qcUnPassDetailItemList)) {
            return Collections.emptyList();
        }

        return qcUnPassDetailItemList.stream()
                .map(qcUnPassDetailItemDto -> {
                    final QcDetailPo passedQcDetailPo = qcDetailIdPoMap.get(qcUnPassDetailItemDto.getQcDetailId());
                    if (null == passedQcDetailPo) {
                        throw new BizException("质检详情id:{}找不到对应的数据，数据错误，请联系系统管理员!",
                                qcUnPassDetailItemDto.getQcDetailId());
                    }

                    final QcDetailPo qcDetailPo = new QcDetailPo();
                    qcDetailPo.setQcOrderNo(qcOrderNo);
                    qcDetailPo.setContainerCode(qcUnPassDetailItemDto.getContainerCode());
                    qcDetailPo.setBatchCode(qcUnPassDetailItemDto.getSkuBatchCode());
                    qcDetailPo.setSpu(passedQcDetailPo.getSpu());
                    qcDetailPo.setSkuCode(qcUnPassDetailItemDto.getSku());
                    qcDetailPo.setAmount(0);
                    qcDetailPo.setWaitAmount(0);
                    qcDetailPo.setPassAmount(0);
                    qcDetailPo.setNotPassAmount(qcUnPassDetailItemDto.getNotPassAmount());
                    qcDetailPo.setQcResult(QcResult.NOT_PASSED);
                    qcDetailPo.setQcNotPassedReason(String.join(",", qcUnPassDetailItemDto.getQcNotPassedReasonList()));
                    qcDetailPo.setRemark(qcUnPassDetailItemDto.getRemark());
                    qcDetailPo.setPictureIds(String.join(",", qcUnPassDetailItemDto.getProblemFileCodeList()));
                    qcDetailPo.setWeight(passedQcDetailPo.getWeight());
                    qcDetailPo.setRelationQcDetailId(qcUnPassDetailItemDto.getQcDetailId());
                    qcDetailPo.setPlatform(qcUnPassDetailItemDto.getPlatform());

                    return qcDetailPo;
                })
                .collect(Collectors.toList());

    }

    public static ReturnOrderBo qcDefectPoToBo(QcReceiveOrderPo qcReceiveOrderPo,
                                               ReturnType returnType,
                                               Map<String, BigDecimal> skuBatchCodePriceMap,
                                               PurchaseDeliverOrderPo purchaseDeliverOrderPo,
                                               List<QcDetailPo> qcDetailPoList,
                                               Integer totalNotPassAmount,
                                               SupplierPo supplierPo,
                                               String platform) {

        final List<ReturnOrderItemBo> returnOrderItemBoList = qcDetailPoList.stream()
                .map(qcOrderDetailPo -> {
                    final ReturnOrderItemBo returnOrderItemBo = new ReturnOrderItemBo();
                    returnOrderItemBo.setSku(qcOrderDetailPo.getSkuCode());
                    returnOrderItemBo.setSkuBatchCode(qcOrderDetailPo.getBatchCode());
                    returnOrderItemBo.setExpectedReturnCnt(qcOrderDetailPo.getNotPassAmount());
                    returnOrderItemBo.setBizDetailId(qcOrderDetailPo.getQcDetailId());
                    returnOrderItemBo.setQcOrderNo(qcReceiveOrderPo.getQcOrderNo());
                    BigDecimal settlePrice = Optional.ofNullable(skuBatchCodePriceMap.get(qcOrderDetailPo.getBatchCode())).orElse(BigDecimal.ZERO);
                    returnOrderItemBo.setSettlePrice(settlePrice);
                    returnOrderItemBo.setReturnBizNo(qcReceiveOrderPo.getQcOrderNo());
                    returnOrderItemBo.setDefectHandlingNo(qcOrderDetailPo.getQcDetailId() + "");

                    return returnOrderItemBo;
                })
                .collect(Collectors.toList());


        final ReturnOrderBo returnOrderBo = new ReturnOrderBo();
        returnOrderBo.setExpectedReturnCnt(totalNotPassAmount);
        returnOrderBo.setSupplierCode(qcReceiveOrderPo.getSupplierCode());
        returnOrderBo.setSupplierName(supplierPo.getSupplierName());
        returnOrderBo.setReturnType(returnType);
        returnOrderBo.setPurchaseReturnOrderItemBoList(returnOrderItemBoList);
        returnOrderBo.setOperator(GlobalContext.getUserKey());
        returnOrderBo.setOperatorUsername(GlobalContext.getUsername());
        returnOrderBo.setReceiveOrderNo(qcReceiveOrderPo.getReceiveOrderNo());
        returnOrderBo.setReturnBizNo(qcReceiveOrderPo.getScmBizNo());
        returnOrderBo.setPurchaseChildOrderNo(purchaseDeliverOrderPo.getPurchaseChildOrderNo());
        returnOrderBo.setWarehouseCode(purchaseDeliverOrderPo.getWarehouseCode());
        returnOrderBo.setPlatform(platform);

        return returnOrderBo;
    }

    public static QcVo toQcVo(List<QcOrderPo> mergedQcOrderList,
                              List<QcDetailPo> qcDetailPos) {
        QcVo qcVo = new QcVo();
        if (CollectionUtils.isEmpty(mergedQcOrderList)) {
            qcVo.setQcOrderVos(Collections.emptyList());
            return qcVo;
        }
        List<QcOrderVo> qcOrderVos = mergedQcOrderList.stream()
                .map(qcOrderPo -> {
                    final String qcOrderNo = qcOrderPo.getQcOrderNo();

                    QcOrderVo qcOrderVo = new QcOrderVo();
                    qcOrderVo.setQcOrderId(qcOrderPo.getQcOrderId());
                    qcOrderVo.setQcOrderNo(qcOrderNo);
                    qcOrderVo.setWarehouseCode(qcOrderPo.getWarehouseCode());
                    qcOrderVo.setReceiveOrderNo(qcOrderPo.getReceiveOrderNo());
                    qcOrderVo.setProcessOrderNo(qcOrderPo.getProcessOrderNo());
                    qcOrderVo.setQcType(qcOrderPo.getQcType());
                    qcOrderVo.setQcAmount(qcOrderPo.getQcAmount());
                    qcOrderVo.setQcState(qcOrderPo.getQcState());
                    qcOrderVo.setQcResult(qcOrderPo.getQcResult());
                    qcOrderVo.setHandOverTime(qcOrderPo.getHandOverTime());
                    qcOrderVo.setTaskFinishTime(qcOrderPo.getTaskFinishTime());
                    qcOrderVo.setAuditTime(qcOrderPo.getAuditTime());
                    qcOrderVo.setHandOverUser(qcOrderPo.getHandOverUser());
                    qcOrderVo.setOperator(qcOrderPo.getOperator());
                    qcOrderVo.setOperatorName(qcOrderPo.getOperatorName());
                    qcOrderVo.setAuditor(qcOrderPo.getAuditor());
                    qcOrderVo.setSkuDevType(qcOrderPo.getSkuDevType());
                    qcOrderVo.setCreateUser(qcOrderPo.getCreateUser());
                    qcOrderVo.setCreateTime(qcOrderPo.getCreateTime());
                    qcOrderVo.setCreateUsername(qcOrderPo.getCreateUsername());
                    qcOrderVo.setUpdateUser(qcOrderPo.getUpdateUser());
                    qcOrderVo.setUpdateTime(qcOrderPo.getUpdateTime());
                    qcOrderVo.setUpdateUsername(qcOrderPo.getUpdateUsername());

                    List<QcDetailPo> matchQcDetails
                            = qcDetailPos.stream()
                            .filter(qcDetailPo -> Objects.equals(qcOrderNo, qcDetailPo.getQcOrderNo()))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(matchQcDetails)) {
                        List<QcOrderDetailVo> qcOrderDetails = matchQcDetails.stream()
                                .map(qcDetailPo -> {
                                    QcOrderDetailVo qcOrderDetailVo = new QcOrderDetailVo();
                                    qcOrderDetailVo.setQcDetailId(qcDetailPo.getQcDetailId());
                                    qcOrderDetailVo.setQcOrderNo(qcDetailPo.getQcOrderNo());
                                    qcOrderDetailVo.setContainerCode(qcDetailPo.getContainerCode());
                                    qcOrderDetailVo.setBatchCode(qcDetailPo.getBatchCode());
                                    qcOrderDetailVo.setSpu(qcDetailPo.getSpu());
                                    qcOrderDetailVo.setSkuCode(qcDetailPo.getSkuCode());
                                    qcOrderDetailVo.setAmount(qcDetailPo.getAmount());
                                    qcOrderDetailVo.setWaitAmount(qcDetailPo.getWaitAmount());
                                    qcOrderDetailVo.setPassAmount(qcDetailPo.getPassAmount());
                                    qcOrderDetailVo.setNotPassAmount(qcDetailPo.getNotPassAmount());
                                    qcOrderDetailVo.setQcResult(qcDetailPo.getQcResult());
                                    qcOrderDetailVo.setQcNotPassedReason(qcDetailPo.getQcNotPassedReason());
                                    qcOrderDetailVo.setRemark(qcDetailPo.getRemark());
                                    qcOrderDetailVo.setPictureIds(qcDetailPo.getPictureIds());
                                    qcOrderDetailVo.setWeight(qcDetailPo.getWeight());
                                    qcOrderDetailVo.setRelationQcDetailId(qcDetailPo.getRelationQcDetailId());
                                    qcOrderDetailVo.setCreateUsername(qcDetailPo.getCreateUsername());
                                    qcOrderDetailVo.setUpdateUsername(qcDetailPo.getUpdateUsername());
                                    qcOrderDetailVo.setPlatform(qcDetailPo.getPlatform());
                                    return qcOrderDetailVo;
                                })
                                .collect(Collectors.toList());
                        qcOrderVo.setQcOrderDetails(qcOrderDetails);
                    }
                    return qcOrderVo;
                })
                .collect(Collectors.toList());
        qcVo.setQcOrderVos(qcOrderVos);
        return qcVo;
    }

    public static QcExportVo createQcDetailExportVo(QcDetailExportBo qcDetailExportBo,
                                                    DefectHandlingPo defectHandlingPo) {
        // 构建自定义日期格式化
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendValue(ChronoField.YEAR, 4)
                .appendLiteral('-')
                .appendValue(ChronoField.MONTH_OF_YEAR)
                .appendLiteral('-')
                .appendValue(ChronoField.DAY_OF_MONTH)
                .appendLiteral(' ')
                .appendValue(ChronoField.HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
                .appendLiteral(':')
                .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
                .toFormatter();

        QcExportVo vo = new QcExportVo();
        vo.setWarehouse(qcDetailExportBo.getWarehouseCode());
        vo.setQcOrderNo(qcDetailExportBo.getQcOrderNo());
        vo.setSku(qcDetailExportBo.getSkuCode());
        vo.setBatchCode(qcDetailExportBo.getBatchCode());
        vo.setDefectiveReason(StringUtil.extractContentFromMultipleStrings(qcDetailExportBo.getQcNotPassedReason()));
        vo.setQcType(Objects.nonNull(qcDetailExportBo.getQcType()) ? qcDetailExportBo.getQcType()
                .getRemark() : "");
        vo.setProcessOrderNo(qcDetailExportBo.getProcessOrderNo());
        vo.setRepairOrderNo(qcDetailExportBo.getRepairOrderNo());
        vo.setPurchaseChildOrderNo(qcDetailExportBo.getPurchaseChildOrderNo());
        vo.setQcSourceOrderNo(qcDetailExportBo.getQcSourceOrderNo());
        vo.setQcSourceOrderType(Objects.nonNull(qcDetailExportBo.getQcSourceOrderType()) ? qcDetailExportBo.getQcSourceOrderType().getRemark() : "");
        vo.setReceiveOrderNo(qcDetailExportBo.getReceiveOrderNo());
        // 供应链单据号
        vo.setScmBizNo(qcDetailExportBo.getScmBizNo());
        // 交接数量外层赋值
        // 正品数 次品数
        vo.setGoodQuantity(qcDetailExportBo.getPassAmount());
        vo.setDefectiveQuantity(qcDetailExportBo.getNotPassAmount());
        // 检验合格率外层赋值
        // 质检员
        vo.setQcInspector(qcDetailExportBo.getOperatorName());
        vo.setPlanAmount(qcDetailExportBo.getPassAmount());
        vo.setCompromiseCnt(Objects.nonNull(defectHandlingPo) ? defectHandlingPo.getNotPassCnt() : null);
        vo.setRemark(qcDetailExportBo.getRemark());
        vo.setTaskFinishTimeMonth(ScmTimeUtil.localDateTimeToStr(qcDetailExportBo.getTaskFinishTime(), TimeZoneId.CN, DatePattern.NORM_MONTH_PATTERN));
        vo.setTaskFinishTime(ScmTimeUtil.localDateTimeToStr(qcDetailExportBo.getTaskFinishTime(), TimeZoneId.CN, formatter));
        vo.setHandOverUser(qcDetailExportBo.getHandOverUser());
        vo.setHandOverTime(ScmTimeUtil.localDateTimeToStr(qcDetailExportBo.getHandOverTime(), TimeZoneId.CN, formatter));
        vo.setAuditor(qcDetailExportBo.getAuditor());
        vo.setAuditTime(ScmTimeUtil.localDateTimeToStr(qcDetailExportBo.getAuditTime(), TimeZoneId.CN, formatter));
        // 质检来源(质检类型)
        if (null != qcDetailExportBo.getQcOrigin()) {
            vo.setQcOrigin(qcDetailExportBo.getQcOrigin().getRemark());
        }
        // 质检来源属性(质检标识)
        if (null != qcDetailExportBo.getQcOriginProperty()) {
            vo.setQcOriginProperty(qcDetailExportBo.getQcOriginProperty().getRemark());
        }
        vo.setSupplier(qcDetailExportBo.getSupplierCode());
        vo.setWaitAmount(qcDetailExportBo.getWaitAmount());
        if (Objects.nonNull(qcDetailExportBo.getHandOverTime())
                && qcDetailExportBo.getHandOverTime().getYear() != DB_DEFAULT_YEAR
                && Objects.nonNull(qcDetailExportBo.getTaskFinishTime())
                && qcDetailExportBo.getTaskFinishTime().getYear() != DB_DEFAULT_YEAR) {
            long minutesDifference = Duration.between(qcDetailExportBo.getHandOverTime(), qcDetailExportBo.getTaskFinishTime()).toMinutes();
            BigDecimal daysDifference = BigDecimal.valueOf((double) minutesDifference / 60).setScale(2, RoundingMode.HALF_UP);
            vo.setQcUseHours(daysDifference);
        }
        return vo;
    }

    public static QcExportVo createQcExportVo(QcSearchVo qcInfo) {
        if (Objects.isNull(qcInfo)) {
            return null;
        }

        QcExportVo vo = new QcExportVo();
        vo.setWarehouse(qcInfo.getWarehouseCode());
        vo.setQcOrderNo(qcInfo.getQcOrderNo());
        vo.setProcessOrderNo(qcInfo.getProcessOrderNo());
        vo.setRepairOrderNo(qcInfo.getRepairOrderNo());
        vo.setReceiveOrderNo(qcInfo.getReceiveOrderNo());
        vo.setScmBizNo(qcInfo.getScmBizNo());
        vo.setReceivingType(Objects.nonNull(qcInfo.getReceiveType()) ? qcInfo.getReceiveType()
                .getRemark() : "");
        vo.setQcType(Objects.nonNull(qcInfo.getQcType()) ? qcInfo.getQcType()
                .getRemark() : "");
        vo.setQcStatus(Objects.nonNull(qcInfo.getQcState()) ? qcInfo.getQcState()
                .getRemark() : "");
        vo.setSku(qcInfo.getSku());
        vo.setProductName(qcInfo.getSkuEncode());
        vo.setSupplier(qcInfo.getSupplierCode());
        vo.setHandoverQuantity(qcInfo.getAmount());
        vo.setQcQuantity(qcInfo.getPassAmount() + qcInfo.getNotPassAmount());
        vo.setGoodQuantity(qcInfo.getPassAmount());
        vo.setDefectiveQuantity(qcInfo.getNotPassAmount());
        vo.setQcPassRate(StrUtil.format("{}%", qcInfo.getQcPassRate()));
        vo.setQcResult(Objects.nonNull(qcInfo.getQcResult()) ? qcInfo.getQcResult()
                .getRemark() : "");
        vo.setPlanAmount(qcInfo.getPlanAmount());
        vo.setReturnCnt(qcInfo.getReturnCnt());
        vo.setCompromiseCnt(qcInfo.getCompromiseCnt());
        vo.setScrapCnt(qcInfo.getScrapCnt());
        vo.setExchangeCnt(qcInfo.getExchangeCnt());
        vo.setOnShelvesOrderNos(Optional.ofNullable(qcInfo.getOnShelvesOrderNoList())
                .orElse(Collections.emptyList())
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",")));
        vo.setReturnOrderNos(Optional.ofNullable(qcInfo.getReturnOrderNoList())
                .orElse(Collections.emptyList())
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",")));
        vo.setReceiveOrderNos(Optional.ofNullable(qcInfo.getReceiveOrderNoList())
                .orElse(Collections.emptyList())
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",")));

        // 非必要字段
        vo.setQcInspector(qcInfo.getOperatorName());
        vo.setHandOverTime(ScmTimeUtil.localDateTimeToStr(qcInfo.getHandOverTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
        vo.setQcTime(ScmTimeUtil.localDateTimeToStr(qcInfo.getTaskFinishTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
        vo.setCreateTime(ScmTimeUtil.localDateTimeToStr(qcInfo.getCreateTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
        vo.setReceiveOrderNo(qcInfo.getReceiveOrderNo());
        if (null != qcInfo.getQcOrigin()) {
            vo.setQcOrigin(qcInfo.getQcOrigin().getRemark());
        }
        if (null != qcInfo.getQcOriginProperty()) {
            vo.setQcOriginProperty(qcInfo.getQcOriginProperty().getRemark());
        }
        vo.setPurchaseChildOrderNo(qcInfo.getPurchaseChildOrderNo());
        vo.setQcSourceOrderNo(qcInfo.getQcSourceOrderNo());
        vo.setQcSourceOrderType(Objects.nonNull(qcInfo.getQcSourceOrderType()) ? qcInfo.getQcSourceOrderType().getRemark() : "");
        return vo;
    }
}
