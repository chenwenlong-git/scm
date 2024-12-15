package com.hete.supply.scm.server.supplier.purchase.handler;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.entity.bo.ProduceDataUpdatePurchasePriceBo;
import com.hete.supply.scm.server.scm.entity.dto.ProduceDataPurchasePriceDto;
import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderChangeMqDto;
import com.hete.supply.scm.server.scm.handler.ProduceDataPurchasePriceHandler;
import com.hete.supply.scm.server.scm.purchase.dao.*;
import com.hete.supply.scm.server.scm.purchase.entity.po.*;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseBaseService;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.scm.server.supplier.purchase.service.base.PurchaseDeliverBaseService;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.consistency.core.service.ConsistencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/12/25 10:56
 */
@Service
@RequiredArgsConstructor
public class PurchaseDeliverOnShelvedStrategy implements PurchaseDeliverStrategy {
    private final PurchaseDeliverOrderDao purchaseDeliverOrderDao;
    private final PurchaseDeliverBaseService purchaseDeliverBaseService;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final LogBaseService logBaseService;
    private final PurchaseChildOrderChangeDao purchaseChildOrderChangeDao;
    private final PurchaseParentOrderChangeDao purchaseParentOrderChangeDao;
    private final PurchaseDeliverOrderItemDao purchaseDeliverOrderItemDao;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;
    private final PurchaseParentOrderItemDao purchaseParentOrderItemDao;
    private final PurchaseChildOrderRawDao purchaseChildOrderRawDao;
    private final PurchaseBaseService purchaseBaseService;
    private final PurchaseParentOrderDao purchaseParentOrderDao;
    private final ConsistencyService consistencyService;

    @Override
    public void syncWmsChangeState(PurchaseChildOrderPo purchaseChildOrderPo,
                                   WmsEnum.ReceiveOrderState receiveOrderState,
                                   PurchaseDeliverOrderPo purchaseDeliverOrderPo,
                                   List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList,
                                   ReceiveOrderChangeMqDto dto,
                                   List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList,
                                   PurchaseParentOrderPo purchaseParentOrderPo,
                                   PurchaseParentOrderChangePo purchaseParentOrderChangePo,
                                   PurchaseChildOrderChangePo purchaseChildOrderChangePo,
                                   List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList) {
        final LocalDateTime now = LocalDateTime.now();
        final List<ReceiveOrderChangeMqDto.BatchCodeDetail> batchCodeDetailList = dto.getBatchCodeDetailList();
        final List<ReceiveOrderChangeMqDto.PurchaseReceiptSyncItemDto> purchaseReceiptSyncItemList = dto.getPurchaseReceiptSyncItemList();


        // 状态为已上架并且质检结果列表不为空时，更新质检结果的正品次品数到发货单
        if (CollectionUtils.isNotEmpty(batchCodeDetailList)) {
            this.updateQualityAndDefectiveCnt(batchCodeDetailList, purchaseDeliverOrderItemPoList,
                    purchaseChildOrderItemPoList, purchaseParentOrderItemPoList);
        }
        // 状态为已上架并且收货列表不为空时，更新收货数。兼容wms一键上架流程
        if (CollectionUtils.isNotEmpty(purchaseReceiptSyncItemList)) {
            purchaseDeliverBaseService.updateDeliverCnt(purchaseDeliverOrderPo, dto, purchaseParentOrderPo,
                    purchaseParentOrderItemPoList, purchaseDeliverOrderItemPoList, purchaseChildOrderItemPoList);
        }

        // 更新采购发货单
        final DeliverOrderStatus deliverOrderStatus = purchaseDeliverBaseService.changeWmsStateToDeliverStatus(purchaseDeliverOrderPo, receiveOrderState);
        if (null == deliverOrderStatus) {
            throw new BizException("错误的发货目标状态，同步目标状态失败");
        }
        //如果是已入库状态时更新入库时间
        if (DeliverOrderStatus.WAREHOUSED.equals(deliverOrderStatus)) {
            purchaseDeliverOrderPo.setWarehousingTime(now);
        }
        purchaseDeliverOrderPo.setDeliverOrderStatus(deliverOrderStatus);
        purchaseDeliverOrderDao.updateByIdVersion(purchaseDeliverOrderPo);

        // 更新子单
        final PurchaseChildOrderPo updatePo = new PurchaseChildOrderPo();
        // 获取子单目标状态
        PurchaseOrderStatus targetStatus = purchaseDeliverBaseService.getPurchaseChildStatus(purchaseChildOrderPo.getPurchaseChildOrderNo());
        // 总上架数
        final int totalQualityGoodsCnt = purchaseChildOrderItemPoList.stream()
                .mapToInt(PurchaseChildOrderItemPo::getQualityGoodsCnt)
                .sum();
        // 采购子单目标状态为已入库且采购数与上架数相等，则目标状态修改为已完结
        if (PurchaseOrderStatus.WAREHOUSED.equals(targetStatus)
                && purchaseChildOrderPo.getPurchaseTotal().equals(totalQualityGoodsCnt)) {
            targetStatus = PurchaseOrderStatus.FINISH;
            updatePo.setCapacity(BigDecimal.ZERO);
            purchaseChildOrderPo.setPurchaseOrderStatus(targetStatus);
            // 产能变更日志
            logBaseService.purchaseCapacityLog(LogBizModule.PURCHASE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                    purchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseChildOrderPo, BigDecimal.ZERO);

        }
        updatePo.setPurchaseChildOrderId(purchaseChildOrderPo.getPurchaseChildOrderId());
        updatePo.setVersion(purchaseChildOrderPo.getVersion());
        updatePo.setPurchaseOrderStatus(targetStatus);
        // 准交数
        int timelyDeliveryCnt = 0;
        // 上架状态计算准交数(现在的时间在期望上架时间之前，则认为是准交)
        if (now.isBefore(purchaseChildOrderPo.getExpectedOnShelvesDate())) {
            timelyDeliveryCnt = batchCodeDetailList.stream()
                    .mapToInt(ReceiveOrderChangeMqDto.BatchCodeDetail::getPassAmount)
                    .sum();
        }
        // 准交数不为0时，更新准交数、准交率、准交时间
        if (timelyDeliveryCnt != 0) {
            updatePo.setTimelyDeliveryCnt(timelyDeliveryCnt + purchaseChildOrderPo.getTimelyDeliveryCnt());
            BigDecimal timelyDeliveryRate = BigDecimal.valueOf(updatePo.getTimelyDeliveryCnt())
                    .divide(BigDecimal.valueOf(purchaseChildOrderPo.getPurchaseTotal()), 2, RoundingMode.HALF_UP);
            updatePo.setTimelyDeliveryRate(timelyDeliveryRate);
            updatePo.setTimelyDeliveryTime(now);
        }
        // 如果采购子单状态变更为已入库或已完结，需要更新结算金额，同时推送价格给wms
        if (PurchaseOrderStatus.WAREHOUSED.equals(targetStatus) || PurchaseOrderStatus.FINISH.equals(targetStatus)) {
            updatePo.setTotalSettlePrice(this.getPurchaseSettlePriceByChildNo(purchaseChildOrderItemPoList));
            // 推送批次码价格（采购价）给到wms
            purchaseDeliverBaseService.batchPriceToWms(purchaseChildOrderItemPoList);

            // 如果采购子单状态变更为已入库或已完结，需要更新生产资料信息的商品采购价格
            List<ProduceDataUpdatePurchasePriceBo> produceDataUpdatePurchasePriceBoList = purchaseChildOrderItemPoList.stream().filter(itemPo -> itemPo.getPurchasePrice() != null && StringUtils.isNotBlank(itemPo.getSku()))
                    .map(itemPo -> {
                        ProduceDataUpdatePurchasePriceBo produceDataUpdatePurchasePriceBo = new ProduceDataUpdatePurchasePriceBo();
                        produceDataUpdatePurchasePriceBo.setSku(itemPo.getSku());
                        produceDataUpdatePurchasePriceBo.setGoodsPurchasePrice(itemPo.getPurchasePrice());
                        return produceDataUpdatePurchasePriceBo;
                    }).collect(Collectors.toList());
            // 触发工作流异步任务
            ProduceDataPurchasePriceDto produceDataPurchasePriceDto = new ProduceDataPurchasePriceDto();
            produceDataPurchasePriceDto.setProduceDataUpdatePurchasePriceBoList(produceDataUpdatePurchasePriceBoList);
            consistencyService.execAsyncTask(ProduceDataPurchasePriceHandler.class, produceDataPurchasePriceDto);
        }

        // 上架时、根据丢货数释放可发货数。若子单已完结，则直接设置可发货数与未交数为0
        if (PurchaseOrderStatus.FINISH.equals(targetStatus)) {
            updatePo.setShippableCnt(0);
            purchaseChildOrderItemPoList.forEach(purchaseChildOrderItemPo -> purchaseChildOrderItemPo.setUndeliveredCnt(0));
        } else {
            // 未完结时，可发货数加上丢货数，采购未交数扣减掉sku对应的商品收货数
            final int lossAmount = batchCodeDetailList.stream()
                    .mapToInt(ReceiveOrderChangeMqDto.BatchCodeDetail::getLossAmount)
                    .sum();
            updatePo.setShippableCnt(purchaseChildOrderPo.getShippableCnt() + lossAmount);
            // 采购子单未交数扣减上架数
            final Map<String, Integer> skuBatchCodeOnShelvesCntMap = batchCodeDetailList.stream()
                    .collect(Collectors.toMap(ReceiveOrderChangeMqDto.BatchCodeDetail::getBatchCode,
                            ReceiveOrderChangeMqDto.BatchCodeDetail::getPassAmount));
            purchaseChildOrderItemPoList.forEach(purchaseChildOrderItemPo -> {
                purchaseChildOrderItemPo.setUndeliveredCnt(purchaseChildOrderItemPo.getUndeliveredCnt()
                        - skuBatchCodeOnShelvesCntMap.getOrDefault(purchaseChildOrderItemPo.getSkuBatchCode(), 0));
            });
        }
        // 更新采购子单明细
        purchaseChildOrderItemDao.updateBatchByIdVersion(purchaseChildOrderItemPoList);


        // 已上架状态需要统计原料使用情况，变更采购子单的剩余原料标记
        if (PurchaseBizType.PROCESS.equals(purchaseChildOrderPo.getPurchaseBizType())) {
            final List<PurchaseChildOrderRawPo> bomRawList = purchaseChildOrderRawDao.getListByChildNo(purchaseChildOrderPo.getPurchaseChildOrderNo(), PurchaseRawBizType.FORMULA);
            final List<PurchaseChildOrderRawPo> deliveryRawList = purchaseChildOrderRawDao.getListByChildNo(purchaseChildOrderPo.getPurchaseChildOrderNo(), PurchaseRawBizType.ACTUAL_DELIVER);

            final BooleanType rawRemainTab = this.getRawRemainTab(batchCodeDetailList, bomRawList, deliveryRawList);
            updatePo.setRawRemainTab(rawRemainTab);
        }

        purchaseChildOrderDao.updateByIdVersion(updatePo);
        // 更新子单日志
        logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseChildOrderPo.getPurchaseChildOrderNo(), targetStatus.getRemark(), Collections.emptyList(),
                dto.getOperator(), dto.getOperatorName());
        // 更新发货单日志
        logBaseService.simpleLog(LogBizModule.SUPPLIER_PURCHASE_DELIVER_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseDeliverOrderPo.getPurchaseDeliverOrderNo(), targetStatus.getRemark(), Collections.emptyList(),
                dto.getOperator(), dto.getOperatorName());

        // 扣减未交数
        // 母单维度扣减采购未交数，减去收货总数
        final Map<String, Integer> skuBatchCodeCntMap = batchCodeDetailList.stream()
                .collect(Collectors.toMap(ReceiveOrderChangeMqDto.BatchCodeDetail::getBatchCode,
                        ReceiveOrderChangeMqDto.BatchCodeDetail::getPassAmount));
        final Integer purchaseTotal = skuBatchCodeCntMap.values()
                .stream()
                .reduce(Integer::sum)
                .orElse(0);
        purchaseParentOrderPo.setUndeliveredCnt(Math.max(purchaseParentOrderPo.getUndeliveredCnt() - purchaseTotal, 0));
        // 母单sku维度扣减未交数
        final Map<String, PurchaseParentOrderItemPo> skuPurchaseItemMap = purchaseParentOrderItemPoList.stream()
                .collect(Collectors.toMap(PurchaseParentOrderItemPo::getSku, Function.identity()));
        for (ReceiveOrderChangeMqDto.BatchCodeDetail item : dto.getBatchCodeDetailList()) {
            final PurchaseParentOrderItemPo purchaseParentOrderItemPo = skuPurchaseItemMap.get(item.getSkuCode());
            if (null == purchaseParentOrderItemPo) {
                return;
            }
            int subUndeliveredCnt = Math.min(purchaseParentOrderItemPo.getUndeliveredCnt(), item.getPassAmount());
            purchaseParentOrderItemPo.setUndeliveredCnt(purchaseParentOrderItemPo.getUndeliveredCnt() - subUndeliveredCnt);
        }

        // 已入库时更新母单状态
        if (PurchaseOrderStatus.WAREHOUSED.equals(targetStatus) || PurchaseOrderStatus.FINISH.equals(targetStatus)) {
            // 计算入库数
            final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList1 = purchaseChildOrderItemDao.getListByParent(purchaseParentOrderPo.getPurchaseParentOrderNo());
            final int warehousedCnt = purchaseChildOrderItemPoList1.stream()
                    .mapToInt(PurchaseChildOrderItemPo::getQualityGoodsCnt)
                    .sum();
            // 入库数大于等于采购数，母单状态变更
            if (warehousedCnt >= purchaseParentOrderPo.getPurchaseTotal()) {
                PurchaseParentOrderStatus earliestStatus = purchaseBaseService.getEarliestStatus(purchaseParentOrderPo.getPurchaseParentOrderNo(),
                        Collections.singletonList(targetStatus));
                if (null != earliestStatus && !earliestStatus.equals(purchaseParentOrderPo.getPurchaseParentOrderStatus())) {
                    // 母单的目标状态为已完结，清零母单的未交数和可拆单数
                    purchaseParentOrderPo.setUndeliveredCnt(0);
                    purchaseParentOrderPo.setCanSplitCnt(0);
                    purchaseParentOrderItemPoList.forEach(purchaseParentOrderItemPo -> purchaseParentOrderItemPo.setCanSplitCnt(0));
                    purchaseParentOrderPo.setPurchaseParentOrderStatus(earliestStatus);

                    // 更新母单日志
                    logBaseService.simpleLog(LogBizModule.PURCHASE_PARENT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                            purchaseParentOrderPo.getPurchaseParentOrderNo(), earliestStatus.getRemark(), Collections.emptyList(),
                            dto.getOperator(), dto.getOperatorName());
                }
            }
        }
        purchaseParentOrderDao.updateByIdVersion(purchaseParentOrderPo);

        // 如果母单完结更新子单可发货数量
        purchaseDeliverBaseService.updatePurchaseChildShippableCnt(purchaseParentOrderPo);

        // 更新母单、子单change(这里质检时间同步的是wms的待质检时间)
        purchaseChildOrderChangePo.setWarehousingTime(now);
        purchaseChildOrderChangePo.setWarehousingUser(dto.getOperator());
        purchaseChildOrderChangePo.setWarehousingUsername(dto.getOperatorName());
        purchaseParentOrderChangePo.setWarehousingTime(now);
        purchaseChildOrderChangeDao.updateByIdVersion(purchaseChildOrderChangePo);
        purchaseParentOrderChangeDao.updateByIdVersion(purchaseParentOrderChangePo);

        purchaseDeliverOrderItemDao.updateBatchByIdVersion(purchaseDeliverOrderItemPoList);
        purchaseParentOrderItemDao.updateBatchByIdVersion(purchaseParentOrderItemPoList);
    }

    /**
     * 更新正品数与次品数
     *
     * @param batchCodeDetailList
     * @param purchaseDeliverOrderItemPoList
     * @param purchaseChildOrderItemPoList
     * @param purchaseParentOrderItemPoList
     */
    private void updateQualityAndDefectiveCnt(List<ReceiveOrderChangeMqDto.BatchCodeDetail> batchCodeDetailList,
                                              List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList,
                                              List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList,
                                              List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList) {
        final Map<String, ReceiveOrderChangeMqDto.BatchCodeDetail> skuBatchCodeMap = batchCodeDetailList.stream()
                .collect(Collectors.toMap(ReceiveOrderChangeMqDto.BatchCodeDetail::getBatchCode, Function.identity()));
        // 更新发货单正品次品数
        purchaseDeliverOrderItemPoList.forEach(po -> {
            final ReceiveOrderChangeMqDto.BatchCodeDetail batchCodeDetail = skuBatchCodeMap.get(po.getSkuBatchCode());
            if (null == batchCodeDetail) {
                return;
            }
            po.setQualityGoodsCnt(batchCodeDetail.getPassAmount());
            po.setDefectiveGoodsCnt(batchCodeDetail.getNotPassAmount());
        });

        final Map<String, PurchaseParentOrderItemPo> skuPurchaseParentMap = purchaseParentOrderItemPoList.stream()
                .collect(Collectors.toMap(PurchaseParentOrderItemPo::getSku, Function.identity()));

        // 更新子单正品次品数
        purchaseChildOrderItemPoList.forEach(po -> {
            final ReceiveOrderChangeMqDto.BatchCodeDetail batchCodeDetail = skuBatchCodeMap.get(po.getSkuBatchCode());
            if (null == batchCodeDetail) {
                return;
            }
            po.setQualityGoodsCnt(po.getQualityGoodsCnt() + batchCodeDetail.getPassAmount());
            po.setDefectiveGoodsCnt(po.getDefectiveGoodsCnt() + batchCodeDetail.getNotPassAmount());
            // 根据sku查找对应的母单明细项
            final PurchaseParentOrderItemPo purchaseParentOrderItemPo = skuPurchaseParentMap.get(po.getSku());
            if (null == purchaseParentOrderItemPo) {
                return;
            }
            purchaseParentOrderItemPo.setQualityGoodsCnt(purchaseParentOrderItemPo.getQualityGoodsCnt() + batchCodeDetail.getPassAmount());
            purchaseParentOrderItemPo.setDefectiveGoodsCnt(purchaseParentOrderItemPo.getDefectiveGoodsCnt() + batchCodeDetail.getNotPassAmount());
        });
    }

    private BigDecimal getPurchaseSettlePriceByChildNo(List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList) {
        return purchaseChildOrderItemPoList.stream()
                .map(po -> po.getSettlePrice().multiply(new BigDecimal(po.getQualityGoodsCnt())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BooleanType getRawRemainTab(List<ReceiveOrderChangeMqDto.BatchCodeDetail> batchCodeDetailList,
                                        List<PurchaseChildOrderRawPo> bomRawList,
                                        List<PurchaseChildOrderRawPo> deliveryRawList) {
        if (CollectionUtils.isEmpty(bomRawList)) {
            return BooleanType.FALSE;
        }
        if (CollectionUtils.isEmpty(deliveryRawList)) {
            return BooleanType.FALSE;
        }

        final Map<String, Integer> skuPassAmountMap = batchCodeDetailList.stream()
                .collect(Collectors.groupingBy(ReceiveOrderChangeMqDto.BatchCodeDetail::getSkuCode,
                        Collectors.summingInt(ReceiveOrderChangeMqDto.BatchCodeDetail::getPassAmount)));
        final Map<String, Integer> skuDeliveryMap = deliveryRawList.stream().collect(Collectors.groupingBy(PurchaseChildOrderRawPo::getSku,
                Collectors.summingInt(PurchaseChildOrderRawPo::getDeliveryCnt)));


        for (PurchaseChildOrderRawPo rawPo : bomRawList) {
            final Integer passAmount = skuPassAmountMap.getOrDefault(rawPo.getSku(), 0);
            final Integer deliveryCnt = skuDeliveryMap.getOrDefault(rawPo.getSku(), 0);
            // 若正品数 * bom需求数 小于 原料总发货数，则认为有剩余原料
            if (passAmount * rawPo.getDeliveryCnt() < deliveryCnt) {
                return BooleanType.TRUE;
            }
        }

        return BooleanType.FALSE;
    }

    @Override
    public WmsEnum.ReceiveOrderState getHandlerType() {
        return WmsEnum.ReceiveOrderState.ONSHELVESED;
    }
}
