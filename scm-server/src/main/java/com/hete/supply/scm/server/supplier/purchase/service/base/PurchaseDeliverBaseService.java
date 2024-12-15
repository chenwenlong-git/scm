package com.hete.supply.scm.server.supplier.purchase.service.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseDeliverListDto;
import com.hete.supply.scm.api.scm.entity.enums.DeliverOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseParentOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SystemType;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.dao.SkuInfoDao;
import com.hete.supply.scm.server.scm.entity.bo.SkuAndBatchCodeBo;
import com.hete.supply.scm.server.scm.entity.bo.SkuAndBatchCodeItemBo;
import com.hete.supply.scm.server.scm.entity.bo.SkuAvgPriceBo;
import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderChangeMqDto;
import com.hete.supply.scm.server.scm.entity.dto.WmsCancelDeliverMqDto;
import com.hete.supply.scm.server.scm.entity.po.SkuInfoPo;
import com.hete.supply.scm.server.scm.enums.SkuAvgPriceBizType;
import com.hete.supply.scm.server.scm.handler.WmsCancelDeliverHandler;
import com.hete.supply.scm.server.scm.process.entity.dto.UpdateBatchCodePriceDto;
import com.hete.supply.scm.server.scm.purchase.dao.*;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseChildNoMqDto;
import com.hete.supply.scm.server.scm.purchase.entity.po.*;
import com.hete.supply.scm.server.scm.purchase.handler.PurchaseChangeHandler;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseBaseService;
import com.hete.supply.scm.server.scm.qc.entity.bo.QcOrderBo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.supply.scm.server.scm.qc.service.ref.QcOrderRefService;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.scm.service.base.SkuAvgPriceBaseService;
import com.hete.supply.scm.server.scm.service.base.WmsMqBaseService;
import com.hete.supply.scm.server.scm.settle.dao.PurchaseSettleOrderDao;
import com.hete.supply.scm.server.scm.settle.dao.PurchaseSettleOrderItemDao;
import com.hete.supply.scm.server.scm.settle.entity.po.PurchaseSettleOrderItemPo;
import com.hete.supply.scm.server.scm.settle.entity.po.PurchaseSettleOrderPo;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierProductCompareDao;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupOpCapacityBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.scm.supplier.service.ref.SupplierCapacityRefService;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseDeliverVo;
import com.hete.supply.scm.server.supplier.purchase.handler.PurchaseDeliverStrategy;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.entry.entity.dto.ReceiveOrderGetDto;
import com.hete.supply.wms.api.entry.entity.vo.ReceiveOrderForScmVo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.handler.HandlerContext;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/21 14:11
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class PurchaseDeliverBaseService {
    private final PurchaseDeliverOrderDao purchaseDeliverOrderDao;
    private final PurchaseDeliverOrderItemDao purchaseDeliverOrderItemDao;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final PurchaseChildOrderChangeDao purchaseChildOrderChangeDao;
    private final PurchaseParentOrderDao purchaseParentOrderDao;
    private final PurchaseParentOrderChangeDao purchaseParentOrderChangeDao;
    private final PurchaseBaseService purchaseBaseService;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;
    private final LogBaseService logBaseService;
    private final PurchaseParentOrderItemDao purchaseParentOrderItemDao;
    private final PlmRemoteService plmRemoteService;
    private final SupplierProductCompareDao supplierProductCompareDao;
    private final PurchaseSettleOrderDao purchaseSettleOrderDao;
    private final PurchaseSettleOrderItemDao purchaseSettleOrderItemDao;
    private final WmsRemoteService wmsRemoteService;
    private final ConsistencySendMqService consistencySendMqService;
    private final QcOrderRefService qcOrderRefService;
    private final SkuAvgPriceBaseService skuAvgPriceBaseService;
    private final WmsMqBaseService wmsMqBaseService;
    private final SupplierCapacityRefService supplierCapacityRefService;
    private final SkuInfoDao skuInfoDao;

    public List<PurchaseDeliverVo> getDeliverDetailByNoList(List<String> childOrderNoList) {
        final PurchaseDeliverListDto dto = new PurchaseDeliverListDto();
        dto.setPurchaseChildOrderNoList(childOrderNoList);
        final CommonPageResult.PageInfo<PurchaseDeliverVo> pageResult = purchaseDeliverOrderDao.getDeliverList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<PurchaseDeliverVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new ArrayList<>();
        }
        this.fillPurchaseDeliverVo(records);
        return records;
    }


    /**
     * 处理单据数据
     *
     * @param dto
     * @param purchaseChildOrderPo
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.PURCHASE_UPDATE_PREFIX, key = "#purchaseChildOrderPo.purchaseParentOrderNo",
            waitTime = 1L, leaseTime = -1L)
    public void dealOrderData(ReceiveOrderChangeMqDto dto, PurchaseChildOrderPo purchaseChildOrderPo) {
        // 进入锁之后重新查询
        PurchaseDeliverOrderPo purchaseDeliverOrderPo = purchaseDeliverOrderDao.getOneByNo(dto.getScmBizNo());
        purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(purchaseDeliverOrderPo.getPurchaseChildOrderNo());
        PurchaseChildOrderChangePo purchaseChildOrderChangePo = purchaseChildOrderChangeDao.getByChildOrderId(purchaseChildOrderPo.getPurchaseChildOrderId());
        Assert.notNull(purchaseChildOrderChangePo, () -> new BizException("查找不到对应的采购子单，同步wms收货单数据失败"));
        final List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList = purchaseDeliverOrderItemDao.getListByDeliverOrderNo(purchaseDeliverOrderPo.getPurchaseDeliverOrderNo());
        Assert.notEmpty(purchaseDeliverOrderItemPoList, () -> new BizException("查找不到对应的发货单，同步wms收货单数据失败"));
        final PurchaseParentOrderPo purchaseParentOrderPo = purchaseParentOrderDao.getOneByNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
        Assert.notNull(purchaseParentOrderPo, () -> new BizException("查找不到对应的采购母单，同步wms收货单数据失败"));
        final List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList = purchaseParentOrderItemDao.getListByParentNo(purchaseParentOrderPo.getPurchaseParentOrderNo());
        Assert.notEmpty(purchaseParentOrderItemPoList, () -> new BizException("查找不到对应的采购母单，同步目标状态失败"));
        final PurchaseParentOrderChangePo purchaseParentOrderChangePo = purchaseParentOrderChangeDao.getByParentId(purchaseParentOrderPo.getPurchaseParentOrderId());
        Assert.notNull(purchaseParentOrderChangePo, () -> new BizException("查找不到对应的采购母单，同步wms收货单数据失败"));
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNo(purchaseDeliverOrderPo.getPurchaseChildOrderNo());
        Assert.notEmpty(purchaseChildOrderItemPoList, () -> new BizException("查找不到采购子单项，同步目标状态失败"));

        final WmsEnum.ReceiveOrderState receiveOrderState = dto.getReceiveOrderState();
        if (WmsEnum.ReceiveOrderState.RECEIVED.equals(receiveOrderState)) {
            this.syncReceived(purchaseDeliverOrderPo, dto, purchaseParentOrderChangePo,
                    purchaseChildOrderChangePo, purchaseChildOrderPo, purchaseParentOrderPo,
                    purchaseParentOrderItemPoList, purchaseDeliverOrderItemPoList, purchaseChildOrderItemPoList);
        }

        if (WmsEnum.ReceiveOrderState.WAIT_RECEIVE.equals(receiveOrderState)) {
            this.syncWaitReceive(purchaseDeliverOrderPo, dto, purchaseChildOrderPo, purchaseChildOrderItemPoList);
        }

        if (WmsEnum.ReceiveOrderState.WAIT_QC.equals(receiveOrderState)
                || WmsEnum.ReceiveOrderState.WAIT_ONSHELVES.equals(receiveOrderState)
                || WmsEnum.ReceiveOrderState.ONSHELVESED.equals(receiveOrderState)) {
            PurchaseDeliverStrategy handlerBean = HandlerContext.getHandlerBean(PurchaseDeliverStrategy.class, receiveOrderState);
            handlerBean.syncWmsChangeState(purchaseChildOrderPo, receiveOrderState, purchaseDeliverOrderPo,
                    purchaseChildOrderItemPoList, dto, purchaseDeliverOrderItemPoList, purchaseParentOrderPo,
                    purchaseParentOrderChangePo, purchaseChildOrderChangePo, purchaseParentOrderItemPoList);
        }


        // 退货，更新发货单为作废状态，同时根据判断把采购子单更新为退货或者待发货状态
        if (WmsEnum.ReceiveOrderState.ALL_RETURN.equals(receiveOrderState)) {
            purchaseDeliverOrderPo.setDeliverOrderStatus(DeliverOrderStatus.RETURN);
            purchaseDeliverOrderDao.updateByIdVersion(purchaseDeliverOrderPo);

            final PurchaseOrderStatus purchaseOrderStatus = this.getPurchaseChildStatus(purchaseChildOrderPo.getPurchaseChildOrderNo());
            if (null != purchaseOrderStatus) {
                purchaseChildOrderPo.setPurchaseOrderStatus(purchaseOrderStatus);
                purchaseChildOrderDao.updateByIdVersion(purchaseChildOrderPo);
                // 更新子单日志
                logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                        purchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseOrderStatus.getRemark(), Collections.emptyList(),
                        dto.getOperator(), dto.getOperatorName());

                // 计算入库数
                final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList1 = purchaseChildOrderItemDao.getListByParent(purchaseParentOrderPo.getPurchaseParentOrderNo());
                final int warehousedCnt = purchaseChildOrderItemPoList1.stream()
                        .mapToInt(PurchaseChildOrderItemPo::getQualityGoodsCnt)
                        .sum();
                // 采购数小于等于入库数，母单状态修改
                if (purchaseParentOrderPo.getPurchaseTotal() <= warehousedCnt) {
                    // 更新母单状态
                    final PurchaseParentOrderStatus earliestStatus = purchaseBaseService.getEarliestStatus(purchaseParentOrderPo.getPurchaseParentOrderNo(),
                            Collections.singletonList(purchaseOrderStatus));
                    if (null != earliestStatus && !purchaseParentOrderPo.getPurchaseParentOrderStatus().equals(earliestStatus)) {
                        purchaseParentOrderPo.setPurchaseParentOrderStatus(earliestStatus);
                        purchaseParentOrderDao.updateByIdVersion(purchaseParentOrderPo);
                        // 更新母单日志
                        logBaseService.simpleLog(LogBizModule.PURCHASE_PARENT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                                purchaseParentOrderPo.getPurchaseParentOrderNo(), earliestStatus.getRemark(), Collections.emptyList(),
                                dto.getOperator(), dto.getOperatorName());
                    }
                }
            }
        }

        // 收货中，更新发货单状态为收货中
        if (WmsEnum.ReceiveOrderState.RECEIVING.equals(receiveOrderState)) {
            this.updateDeliverOrderReceiving(purchaseDeliverOrderPo);
        }

        // 单据状态变更后推送单号给wms
        final PurchaseChildNoMqDto purchaseChildNoMqDto = new PurchaseChildNoMqDto();
        purchaseChildNoMqDto.setPurchaseChildOrderNoList(Collections.singletonList(purchaseDeliverOrderPo.getPurchaseChildOrderNo()));
        consistencySendMqService.execSendMq(PurchaseChangeHandler.class, purchaseChildNoMqDto);

    }

    /**
     * 更新发货单状态为收货中
     *
     * @param purchaseDeliverOrderPo
     */
    private void updateDeliverOrderReceiving(PurchaseDeliverOrderPo purchaseDeliverOrderPo) {
        purchaseDeliverOrderPo.setDeliverOrderStatus(purchaseDeliverOrderPo.getDeliverOrderStatus().toReceiving());
        purchaseDeliverOrderDao.updateByIdVersion(purchaseDeliverOrderPo);
    }

    public void updatePurchaseChildShippableCnt(PurchaseParentOrderPo purchaseParentOrderPo) {
        // 如果母单状态不为完结，则无需处理
        if (!PurchaseParentOrderStatus.COMPLETED.equals(purchaseParentOrderPo.getPurchaseParentOrderStatus())) {
            return;
        }

        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByParentNo(purchaseParentOrderPo.getPurchaseParentOrderNo());

        purchaseChildOrderPoList.forEach(purchaseChildOrderPo -> {
            purchaseChildOrderPo.setShippableCnt(ScmConstant.PURCHASE_CHILD_SHIPPABLE_CNT);
        });

        purchaseChildOrderDao.updateBatchByIdVersion(purchaseChildOrderPoList);
    }


    private void syncWaitReceive(PurchaseDeliverOrderPo purchaseDeliverOrderPo, ReceiveOrderChangeMqDto dto,
                                 PurchaseChildOrderPo purchaseChildOrderPo, List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList) {
        // 判断收货单号是否有对应的发货单，若有则mq重复，无需处理
        final List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByReceiveOrderNo(dto.getReceiveOrderNo());
        if (CollectionUtils.isNotEmpty(purchaseDeliverOrderPoList)) {
            throw new BizException("收货单号：{}，已经同步内容至发货单，mq消费重复！", dto.getReceiveOrderNo());
        }

        purchaseDeliverOrderPo.setPurchaseReceiptOrderNo(dto.getReceiveOrderNo());
        purchaseDeliverOrderDao.updateByIdVersion(purchaseDeliverOrderPo);
        final PurchaseChildOrderChangePo purchaseChildOrderChangePo = purchaseChildOrderChangeDao.getByChildOrderId(purchaseChildOrderPo.getPurchaseChildOrderId());

        // 更新子单状态为待收货
        final LocalDateTime now = LocalDateTime.now();
        // 获取子单目标状态
        final PurchaseOrderStatus purchaseOrderStatus = this.getPurchaseChildStatus(purchaseChildOrderPo.getPurchaseChildOrderNo());
        // 更新子单状态
        purchaseChildOrderPo.setPurchaseOrderStatus(purchaseOrderStatus);
        purchaseChildOrderDao.updateByIdVersion(purchaseChildOrderPo);

        // 更新子单日志
        logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseOrderStatus.getRemark(), Collections.emptyList(),
                purchaseDeliverOrderPo.getDeliverUser(), purchaseDeliverOrderPo.getDeliverUsername());

        // 更新子单change
        purchaseChildOrderChangePo.setDeliverTime(now);
        purchaseChildOrderChangePo.setPurchaseReceiptOrderNo(dto.getReceiveOrderNo());
        purchaseChildOrderChangeDao.updateByIdVersion(purchaseChildOrderChangePo);

        this.batchPriceToWms(purchaseChildOrderItemPoList);
    }

    /**
     * 推送批次码价格给wms
     *
     * @param purchaseChildOrderItemPoList
     */
    public void batchPriceToWms(List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList) {
        final SkuAndBatchCodeBo skuAndBatchCodeBo = new SkuAndBatchCodeBo();
        skuAndBatchCodeBo.setSkuAvgPriceBizType(SkuAvgPriceBizType.PURCHASE);
        final List<SkuAndBatchCodeItemBo> skuAndBatchCodeItemBoList = purchaseChildOrderItemPoList.stream().map(itemPo -> {
            final SkuAndBatchCodeItemBo skuAndBatchCodeItemBo = new SkuAndBatchCodeItemBo();
            skuAndBatchCodeItemBo.setSku(itemPo.getSku());
            skuAndBatchCodeItemBo.setSkuBatchCode(itemPo.getSkuBatchCode());
            skuAndBatchCodeItemBo.setAccrueCnt(itemPo.getPurchaseCnt());
            skuAndBatchCodeItemBo.setAccruePrice(itemPo.getPurchasePrice()
                    .multiply(new BigDecimal(itemPo.getPurchaseCnt())));
            return skuAndBatchCodeItemBo;
        }).collect(Collectors.toList());
        skuAndBatchCodeBo.setSkuAndBatchCodeItemBoList(skuAndBatchCodeItemBoList);
        final List<SkuAvgPriceBo> skuAvgPriceBoList = skuAvgPriceBaseService.getSkuAvgPrice(skuAndBatchCodeBo);
        final Map<String, BigDecimal> skuBatchCodeAvgPriceMap = skuAvgPriceBoList.stream()
                .collect(Collectors.toMap(SkuAvgPriceBo::getSkuBatchCode, SkuAvgPriceBo::getAvgPrice));

        List<UpdateBatchCodePriceDto.UpdateBatchCodeCostPrice> batchCodePriceList = purchaseChildOrderItemPoList.stream()
                .map(itemPo -> {
                    final UpdateBatchCodePriceDto.UpdateBatchCodeCostPrice updateBatchCodeCostPrice = new UpdateBatchCodePriceDto.UpdateBatchCodeCostPrice();
                    updateBatchCodeCostPrice.setBatchCode(itemPo.getSkuBatchCode());
                    updateBatchCodeCostPrice.setPrice(skuBatchCodeAvgPriceMap.get(itemPo.getSkuBatchCode()));
                    return updateBatchCodeCostPrice;
                }).collect(Collectors.toList());
        UpdateBatchCodePriceDto updateBatchCodePriceDto = new UpdateBatchCodePriceDto();
        updateBatchCodePriceDto.setBatchCodePriceList(batchCodePriceList);
        wmsMqBaseService.execSendUpdateBatchCodePriceMq(updateBatchCodePriceDto);
    }


    public DeliverOrderStatus changeWmsStateToDeliverStatus(PurchaseDeliverOrderPo purchaseDeliverOrderPo,
                                                            WmsEnum.ReceiveOrderState receiveOrderState) {
        if (WmsEnum.ReceiveOrderState.WAIT_QC.equals(receiveOrderState)) {
            return DeliverOrderStatus.WAIT_QC;
        }
        if (WmsEnum.ReceiveOrderState.WAIT_ONSHELVES.equals(receiveOrderState)) {
            return DeliverOrderStatus.WAIT_WAREHOUSING;
        }
        if (WmsEnum.ReceiveOrderState.ONSHELVESED.equals(receiveOrderState)) {
            return purchaseDeliverOrderPo.getDeliverOrderStatus().toWarehoused();
        }
        return null;

    }

    private void syncReceived(PurchaseDeliverOrderPo purchaseDeliverOrderPo,
                              ReceiveOrderChangeMqDto dto, PurchaseParentOrderChangePo purchaseParentOrderChangePo,
                              PurchaseChildOrderChangePo purchaseChildOrderChangePo,
                              PurchaseChildOrderPo purchaseChildOrderPo, PurchaseParentOrderPo purchaseParentOrderPo,
                              List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList,
                              List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList, List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList) {
        // 更新收货数
        if (CollectionUtils.isEmpty(dto.getPurchaseReceiptSyncItemList())) {
            throw new BizException("wms的收货数据为空，收货失败！");
        }

        this.updateDeliverCnt(purchaseDeliverOrderPo, dto, purchaseParentOrderPo, purchaseParentOrderItemPoList,
                purchaseDeliverOrderItemPoList, purchaseChildOrderItemPoList);
        final DeliverOrderStatus targetStatus = purchaseDeliverOrderPo.getDeliverOrderStatus().toReceived();
        purchaseDeliverOrderPo.setDeliverOrderStatus(targetStatus);
        purchaseDeliverOrderDao.updateByIdVersion(purchaseDeliverOrderPo);
        purchaseParentOrderItemDao.updateBatchByIdVersion(purchaseParentOrderItemPoList);
        purchaseDeliverOrderItemDao.updateBatchByIdVersion(purchaseDeliverOrderItemPoList);

        final LocalDateTime now = LocalDateTime.now();
        // 更新母单change
        purchaseParentOrderChangePo.setReceiptTime(now);
        purchaseParentOrderChangeDao.updateByIdVersion(purchaseParentOrderChangePo);
        // 更新子单change
        purchaseChildOrderChangePo.setPurchaseReceiptOrderNo(dto.getReceiveOrderNo());
        purchaseChildOrderChangePo.setReceiptTime(now);
        purchaseChildOrderChangePo.setReceiptUser(dto.getOperator());
        purchaseChildOrderChangePo.setReceiptUsername(dto.getOperatorName());
        purchaseChildOrderChangeDao.updateByIdVersion(purchaseChildOrderChangePo);

        // 获取子单目标状态
        final PurchaseOrderStatus purchaseOrderStatus = this.getPurchaseChildStatus(purchaseChildOrderPo.getPurchaseChildOrderNo());
        // 更新子单状态
        purchaseChildOrderPo.setPurchaseOrderStatus(purchaseOrderStatus);
        // 如果发货数与收货数不等，则释放可发货数。
        final int receiptCnt = purchaseDeliverOrderItemPoList.stream().mapToInt(PurchaseDeliverOrderItemPo::getReceiptCnt).sum();
        final int rejectCnt = dto.getPurchaseReceiptSyncItemList().stream().mapToInt(ReceiveOrderChangeMqDto.PurchaseReceiptSyncItemDto::getRejectAmount).sum();
        purchaseChildOrderPo.setShippableCnt(purchaseChildOrderPo.getShippableCnt() + (purchaseDeliverOrderPo.getDeliverCnt() - (receiptCnt + rejectCnt)));
        purchaseChildOrderDao.updateByIdVersion(purchaseChildOrderPo);

        // 更新子单日志
        logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseOrderStatus.getRemark(), Collections.emptyList(),
                dto.getOperator(), dto.getOperatorName());

        // 更新发货单日志
        logBaseService.simpleLog(LogBizModule.SUPPLIER_PURCHASE_DELIVER_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseDeliverOrderPo.getPurchaseDeliverOrderNo(), targetStatus.getRemark(), Collections.emptyList(),
                dto.getOperator(), dto.getOperatorName());
    }

    /**
     * 根据发货单获取子单目标状态
     *
     * @param purchaseChildOrderNo
     */
    public PurchaseOrderStatus getPurchaseChildStatus(String purchaseChildOrderNo) {
        final List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByChildOrderNo(purchaseChildOrderNo);
        if (CollectionUtils.isEmpty(purchaseDeliverOrderPoList)) {
            return PurchaseOrderStatus.WAIT_DELIVER;
        }

        final List<DeliverOrderStatus> deliverOrderStatusList = purchaseDeliverOrderPoList.stream()
                .map(PurchaseDeliverOrderPo::getDeliverOrderStatus)
                .collect(Collectors.toList());

        final DeliverOrderStatus earliestStatus = DeliverOrderStatus.getEarliestStatus(deliverOrderStatusList);
        return PurchaseOrderStatus.convertDeliverStatusToPurchaseStatus(earliestStatus);
    }

    /**
     * 更新收货数
     *
     * @param purchaseDeliverOrderPo
     * @param dto
     * @param purchaseParentOrderPo
     * @param purchaseParentOrderItemPoList
     * @param purchaseDeliverOrderItemPoList
     * @param purchaseChildOrderItemPoList
     */
    public void updateDeliverCnt(PurchaseDeliverOrderPo purchaseDeliverOrderPo, ReceiveOrderChangeMqDto dto,
                                 PurchaseParentOrderPo purchaseParentOrderPo,
                                 List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList,
                                 List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList,
                                 List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList) {
        // 判断是否已收货
        for (PurchaseDeliverOrderItemPo purchaseDeliverOrderItemPo : purchaseDeliverOrderItemPoList) {
            if (purchaseDeliverOrderItemPo.getReceiptCnt() != 0) {
                log.info("scm已对收货数据进行收货操作，发货单号：{}，收货信息：{}",
                        purchaseDeliverOrderItemPo.getPurchaseDeliverOrderNo(), JSONUtil.parseObj(dto));
                return;
            }
        }

        final List<ReceiveOrderChangeMqDto.PurchaseReceiptSyncItemDto> purchaseReceiptSyncItemList = dto.getPurchaseReceiptSyncItemList();

        purchaseDeliverOrderPo.setReceiptTime(dto.getBizTime());
        final Map<String, Integer> skuBatchCodeCntMap = purchaseReceiptSyncItemList.stream()
                .collect(Collectors.toMap(ReceiveOrderChangeMqDto.PurchaseReceiptSyncItemDto::getBatchCode,
                        ReceiveOrderChangeMqDto.PurchaseReceiptSyncItemDto::getReceiveAmount));
        final Set<String> skuBatchCodeSet = skuBatchCodeCntMap.keySet();
        final Integer purchaseTotal = skuBatchCodeCntMap.values()
                .stream()
                .reduce(Integer::sum)
                .orElse(0);
        // 如果收货总数为0，则更新单据状态为作废
        if (purchaseTotal.equals(0)) {
            purchaseDeliverOrderPo.setDeliverOrderStatus(DeliverOrderStatus.DELETED);
            return;
        }
        purchaseDeliverOrderItemPoList.stream()
                .filter(po -> skuBatchCodeSet.contains(po.getSkuBatchCode()))
                .forEach(po -> po.setReceiptCnt(skuBatchCodeCntMap.get(po.getSkuBatchCode())));
    }

    /**
     * 采购已发货列表查询条件
     *
     * @author ChenWenLong
     * @date 2023/5/30 14:15
     */
    public PurchaseDeliverListDto getSearchPurchaseDeliverWhere(PurchaseDeliverListDto dto) {
        if (CollectionUtils.isEmpty(dto.getPurchaseDeliverOrderNoList())) {
            dto.setPurchaseDeliverOrderNoList(new ArrayList<>());
        }
        List<String> deliverOrderNoList = dto.getPurchaseDeliverOrderNoList();

        if (CollectionUtils.isEmpty(dto.getSkuList())) {
            dto.setSkuList(new ArrayList<>());
        }

        if (StringUtils.isNotBlank(dto.getSku())) {
            dto.setSkuList(new ArrayList<>());
        }

        //批次码批量查询
        if (CollectionUtils.isNotEmpty(dto.getSkuBatchCodeList())) {
            List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPos = purchaseDeliverOrderItemDao.getListBySkuBatchCodeList(dto.getSkuBatchCodeList());
            if (CollectionUtils.isEmpty(purchaseDeliverOrderItemPos)) {
                return null;
            }
            List<String> itemNos = purchaseDeliverOrderItemPos.stream()
                    .map(PurchaseDeliverOrderItemPo::getPurchaseDeliverOrderNo)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(deliverOrderNoList)) {
                deliverOrderNoList.addAll(itemNos);
            } else {
                deliverOrderNoList.retainAll(itemNos);
            }

        }
        //批次码查询
        if (StringUtils.isNotBlank(dto.getSkuBatchCode())) {
            List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPos = purchaseDeliverOrderItemDao.getListByLikeSkuBatchCode(dto.getSkuBatchCode());
            if (CollectionUtils.isEmpty(purchaseDeliverOrderItemPos)) {
                return null;
            }
            List<String> itemNos = purchaseDeliverOrderItemPos.stream()
                    .map(PurchaseDeliverOrderItemPo::getPurchaseDeliverOrderNo)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(deliverOrderNoList)) {
                deliverOrderNoList.addAll(itemNos);
            } else {
                deliverOrderNoList.retainAll(itemNos);
            }

        }
        //产品名称批量查询
        if (CollectionUtils.isNotEmpty(dto.getSkuEncodeList())) {
            List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(dto.getSkuEncodeList());
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                dto.getSkuList().retainAll(skuListByEncode);
            }
        }
        //产品名称查询
        if (StringUtils.isNotBlank(dto.getSkuEncode())) {
            List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(List.of(dto.getSkuEncode()));
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                dto.getSkuList().retainAll(skuListByEncode);
            }
        }
        //供应商产品名称批量查询
        if (CollectionUtil.isNotEmpty(dto.getSupplierProductNameList())) {
            List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getBatchSupplierProductName(dto.getSupplierProductNameList());
            if (CollectionUtil.isEmpty(supplierProductComparePoList)) {
                return null;
            }
            List<String> supplierNos = this.getNoListBySupplierProduct(supplierProductComparePoList);
            if (CollectionUtils.isEmpty(supplierNos)) {
                return null;
            }
            if (CollectionUtils.isEmpty(deliverOrderNoList)) {
                deliverOrderNoList.addAll(supplierNos);
            } else {
                deliverOrderNoList.retainAll(supplierNos);
            }
        }
        //供应商产品名称查询
        if (StringUtils.isNotBlank(dto.getSupplierProductName())) {
            List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getByLikeSupplierProductName(dto.getSupplierProductName());
            if (CollectionUtil.isEmpty(supplierProductComparePoList)) {
                return null;
            }
            List<String> supplierNos = this.getNoListBySupplierProduct(supplierProductComparePoList);
            if (CollectionUtils.isEmpty(deliverOrderNoList)) {
                deliverOrderNoList.addAll(supplierNos);
            } else {
                deliverOrderNoList.retainAll(supplierNos);
            }
        }

        //支付完成时间查询
        if (null != dto.getPayTimeStart() && null != dto.getPayTimeEnd()) {
            List<PurchaseSettleOrderPo> purchaseSettleOrderPoList = purchaseSettleOrderDao.getListByPayTime(dto.getPayTimeStart(), dto.getPayTimeEnd());
            if (CollectionUtil.isEmpty(purchaseSettleOrderPoList)) {
                return null;
            }
            List<PurchaseSettleOrderItemPo> purchaseSettleOrderItemPoList = purchaseSettleOrderItemDao.getListByBatchPurchaseSettleOrderNo(
                    purchaseSettleOrderPoList.stream()
                            .map(PurchaseSettleOrderPo::getPurchaseSettleOrderNo)
                            .distinct()
                            .collect(Collectors.toList()));
            if (CollectionUtil.isEmpty(purchaseSettleOrderItemPoList)) {
                return null;
            }
            List<String> settleItemNos = purchaseSettleOrderItemPoList.stream()
                    .map(PurchaseSettleOrderItemPo::getBusinessNo)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(deliverOrderNoList)) {
                deliverOrderNoList.addAll(settleItemNos);
            } else {
                deliverOrderNoList.retainAll(settleItemNos);
            }
        }

        //放在条件后面
        if (CollectionUtils.isNotEmpty(dto.getSkuList())) {
            List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList = purchaseDeliverOrderItemDao.getListBySkuList(dto.getSkuList());
            if (CollectionUtils.isEmpty(purchaseDeliverOrderItemPoList)) {
                return null;
            }
            List<String> itemNoList = purchaseDeliverOrderItemPoList.stream()
                    .map(PurchaseDeliverOrderItemPo::getPurchaseDeliverOrderNo)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(deliverOrderNoList)) {
                deliverOrderNoList.addAll(itemNoList);
            } else {
                deliverOrderNoList.retainAll(itemNoList);
            }
        }
        if (StringUtils.isNotBlank(dto.getSku())) {
            List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList = purchaseDeliverOrderItemDao.getListByLikeSku(dto.getSku());
            if (CollectionUtils.isEmpty(purchaseDeliverOrderItemPoList)) {
                return null;
            }
            List<String> itemNoList = purchaseDeliverOrderItemPoList.stream()
                    .map(PurchaseDeliverOrderItemPo::getPurchaseDeliverOrderNo)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(deliverOrderNoList)) {
                deliverOrderNoList.addAll(itemNoList);
            } else {
                deliverOrderNoList.retainAll(itemNoList);
            }
        }
        dto.setPurchaseDeliverOrderNoList(deliverOrderNoList);

        return dto;
    }

    public List<PurchaseDeliverOrderPo> getDeliverPoByPurchaseNo(List<String> purchaseChildOrderNoList) {
        return purchaseDeliverOrderDao.getListByChildOrderNoList(purchaseChildOrderNoList);
    }

    public Map<String, Integer> getChildOrderDeliveredCntByPurchaseNo(List<String> purchaseChildOrderNoList) {
        final List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByChildOrderNoListNotStatusList(purchaseChildOrderNoList,
                Arrays.asList(DeliverOrderStatus.WAIT_DELIVER, DeliverOrderStatus.DELETED));

        final List<String> purchaseDeliverNoList = purchaseDeliverOrderPoList.stream()
                .map(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo)
                .collect(Collectors.toList());

        final List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList = purchaseDeliverOrderItemDao.getListByDeliverOrderNoList(purchaseDeliverNoList);

        final Map<String, String> deliverChildNoMap = purchaseDeliverOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo,
                        PurchaseDeliverOrderPo::getPurchaseChildOrderNo));

        final Map<String, Integer> deliverNoCntMap = purchaseDeliverOrderItemPoList.stream()
                .collect(Collectors.groupingBy(PurchaseDeliverOrderItemPo::getPurchaseDeliverOrderNo,
                        Collectors.summingInt(PurchaseDeliverOrderItemPo::getDeliverCnt)));

        return deliverNoCntMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> deliverChildNoMap.getOrDefault(entry.getKey(), entry.getKey()),
                        Map.Entry::getValue,
                        Integer::sum));
    }


    /**
     * 通过供应商产品名称多组搜索条件获取信息
     *
     * @param supplierProductPoList:
     * @return List<String>
     * @author ChenWenLong
     * @date 2023/7/19 15:03
     */
    public List<String> getNoListBySupplierProduct(List<SupplierProductComparePo> supplierProductPoList) {
        return purchaseDeliverOrderDao.getListBySupplierProduct(supplierProductPoList)
                .stream()
                .map(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.PURCHASE_UPDATE_PREFIX, key = "#purchaseChildOrderPo.purchaseParentOrderNo", waitTime = 1, leaseTime = -1)
    public void cancelDeliver(PurchaseDeliverOrderPo purchaseDeliverOrderPo,
                              PurchaseParentOrderPo purchaseParentOrderPo,
                              PurchaseChildOrderPo purchaseChildOrderPo,
                              List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList,
                              SystemType systemType) {

        // 从scm发起的取消发货才需要给wms发送mq取消收货单
        if (SystemType.SCM.equals(systemType)) {
            final WmsCancelDeliverMqDto wmsCancelDeliverMqDto = new WmsCancelDeliverMqDto();
            wmsCancelDeliverMqDto.setPurchaseDeliverOrderNo(purchaseDeliverOrderPo.getPurchaseDeliverOrderNo());
            wmsCancelDeliverMqDto.setLogistics(purchaseDeliverOrderPo.getLogistics());
            wmsCancelDeliverMqDto.setTrackingNo(purchaseDeliverOrderPo.getTrackingNo());
            wmsCancelDeliverMqDto.setScmBizNo(purchaseDeliverOrderPo.getPurchaseDeliverOrderNo());
            wmsCancelDeliverMqDto.setOperator(GlobalContext.getUserKey());
            wmsCancelDeliverMqDto.setOperatorName(GlobalContext.getUsername());
            consistencySendMqService.execSendMq(WmsCancelDeliverHandler.class, wmsCancelDeliverMqDto);
        }

        // 判断已经发货，再修改产能信息
        if (null != purchaseDeliverOrderPo.getDeliverTime()) {
            final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildOrderItemPoList.get(0);
            final SkuInfoPo skuInfoPo = skuInfoDao.getBySku(purchaseChildOrderItemPo.getSku());
            BigDecimal singleCapacity;
            if (null == skuInfoPo) {
                singleCapacity = BigDecimal.ZERO;
                log.info("sku:{}没有配置sku_info信息", purchaseChildOrderItemPo.getSku());
            } else {
                singleCapacity = skuInfoPo.getSingleCapacity();
            }
            final BigDecimal capacity = singleCapacity.multiply(new BigDecimal(purchaseDeliverOrderPo.getDeliverCnt()))
                    .setScale(2, RoundingMode.HALF_UP);

            final LocalDateTime zonedDateTime = TimeUtil.utcConvertZone(purchaseDeliverOrderPo.getDeliverTime(), TimeZoneId.CN);
            final List<SupOpCapacityBo> supOpCapacityBoList = new ArrayList<>();
            final SupOpCapacityBo supOpCapacityBo = new SupOpCapacityBo();
            supOpCapacityBo.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
            supOpCapacityBo.setOperateDate(zonedDateTime.toLocalDate());
            supOpCapacityBo.setOperateValue(capacity);
            supOpCapacityBo.setBizNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
            final LocalDate capacityDate = purchaseBaseService.getCapacityDate(purchaseChildOrderPo.getPromiseDate(), purchaseChildOrderPo.getSupplierCode());
            final SupOpCapacityBo supOpCapacityBo1 = new SupOpCapacityBo();
            supOpCapacityBo1.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
            supOpCapacityBo1.setOperateDate(capacityDate);
            supOpCapacityBo1.setOperateValue(capacity.negate());
            supOpCapacityBo1.setBizNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
            supOpCapacityBoList.add(supOpCapacityBo);
            supOpCapacityBoList.add(supOpCapacityBo1);
            supplierCapacityRefService.operateSupplierCapacityBatch(supOpCapacityBoList);
            // 产能变更日志
            logBaseService.purchaseCapacityLog(LogBizModule.PURCHASE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                    purchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseChildOrderPo, purchaseChildOrderPo.getCapacity());
        }

        DeliverOrderStatus deliverOrderStatus = purchaseDeliverOrderPo.getDeliverOrderStatus().toDeleted();
        purchaseDeliverOrderPo.setDeliverOrderStatus(deliverOrderStatus);
        purchaseDeliverOrderDao.updateByIdVersion(purchaseDeliverOrderPo);
        logBaseService.simpleLog(LogBizModule.SUPPLIER_PURCHASE_DELIVER_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseDeliverOrderPo.getPurchaseDeliverOrderNo(), deliverOrderStatus.getRemark(), Collections.emptyList());


        // 更新采购子单状态
        final List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByChildOrderNoWithDel(purchaseDeliverOrderPo.getPurchaseChildOrderNo());
        final List<DeliverOrderStatus> deliverOrderStatusList = purchaseDeliverOrderPoList.stream()
                .map(PurchaseDeliverOrderPo::getDeliverOrderStatus)
                .collect(Collectors.toList());
        final DeliverOrderStatus earliestDeliverStatus = DeliverOrderStatus.getEarliestStatus(deliverOrderStatusList);
        final PurchaseOrderStatus purchaseOrderStatus = PurchaseOrderStatus.convertDeliverStatusToPurchaseStatus(earliestDeliverStatus);
        purchaseChildOrderPo.setPurchaseOrderStatus(purchaseOrderStatus);
        purchaseChildOrderPo.setShippableCnt(purchaseChildOrderPo.getShippableCnt() + purchaseDeliverOrderPo.getDeliverCnt());

        purchaseChildOrderDao.updateByIdVersion(purchaseChildOrderPo);
        purchaseChildOrderItemPoList.forEach(po -> po.setDeliverCnt(po.getDeliverCnt() - purchaseDeliverOrderPo.getDeliverCnt()));
        purchaseChildOrderItemDao.updateBatchByIdVersion(purchaseChildOrderItemPoList);

        // 更新母单状态
        final PurchaseParentOrderStatus earliestStatus = purchaseBaseService.getEarliestStatus(purchaseParentOrderPo.getPurchaseParentOrderNo(),
                Collections.singletonList(PurchaseOrderStatus.WAIT_DELIVER));
        if (null != earliestStatus && !earliestStatus.equals(purchaseParentOrderPo.getPurchaseParentOrderStatus())) {
            purchaseParentOrderPo.setPurchaseParentOrderStatus(earliestStatus);
            purchaseParentOrderDao.updateByIdVersion(purchaseParentOrderPo);
        }

        // 单据状态变更后推送单号给wms
        final PurchaseChildNoMqDto purchaseChildNoMqDto = new PurchaseChildNoMqDto();
        purchaseChildNoMqDto.setPurchaseChildOrderNoList(Collections.singletonList(purchaseChildOrderPo.getPurchaseChildOrderNo()));
        consistencySendMqService.execSendMq(PurchaseChangeHandler.class, purchaseChildNoMqDto);
    }

    public PurchaseChildOrderPo getPurchaseChildPoByNo(String deliverOrderNo) {
        final PurchaseDeliverOrderPo purchaseDeliverOrderPo = purchaseDeliverOrderDao.getOneByNo(deliverOrderNo);
        if (null == purchaseDeliverOrderPo) {
            return null;
        }

        return purchaseChildOrderDao.getOneByChildOrderNo(purchaseDeliverOrderPo.getPurchaseChildOrderNo());
    }

    public void fillPurchaseDeliverVo(List<PurchaseDeliverVo> records) {
        final List<String> skuList = records.stream()
                .map(PurchaseDeliverVo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        Map<String, SupplierProductComparePo> supplierProductCompareMap = supplierProductCompareDao.getBatchSkuMap(skuList);

        //查询收货单的状态
        Map<String, List<ReceiveOrderForScmVo>> receiveOrderMap = new HashMap<>();
        Map<String, List<QcOrderBo>> receiveOrderNoQcBoListMap = new HashMap<>();
        final List<String> purchaseDeliverOrderNoList = records.stream()
                .map(PurchaseDeliverVo::getPurchaseDeliverOrderNo)
                .distinct()
                .collect(Collectors.toList());

        final List<String> purchaseReceiptOrderNoList = records.stream()
                .map(PurchaseDeliverVo::getPurchaseReceiptOrderNo)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(purchaseDeliverOrderNoList)) {
            ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
            receiveOrderGetDto.setScmBizNoList(purchaseDeliverOrderNoList);
            receiveOrderMap = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto)
                    .stream()
                    .collect(Collectors.groupingBy(ReceiveOrderForScmVo::getScmBizNo));
            receiveOrderNoQcBoListMap = qcOrderRefService.getReceiveQcOrderByReceiveOrderNoList(purchaseReceiptOrderNoList);
        }

        for (PurchaseDeliverVo record : records) {
            record.setSkuEncode(skuEncodeMap.get(record.getSku()));
            if (receiveOrderMap.containsKey(record.getPurchaseDeliverOrderNo())) {
                List<ReceiveOrderForScmVo> receiveOrderForScmVoList = receiveOrderMap.get(record.getPurchaseDeliverOrderNo());
                if (CollectionUtils.isNotEmpty(receiveOrderForScmVoList)) {
                    final ReceiveOrderForScmVo receiveOrderForScmVo = receiveOrderForScmVoList.get(0);
                    record.setReceiveOrderState(receiveOrderForScmVo.getReceiveOrderState());
                    List<ReceiveOrderForScmVo.OnShelfOrder> onShelfList = receiveOrderForScmVo.getOnShelfList();

                    final List<QcOrderBo> qcOrderBoList = receiveOrderNoQcBoListMap.get(receiveOrderForScmVo.getReceiveOrderNo());

                    int passAmount = 0;
                    int notPassAmount = 0;

                    if (CollectionUtils.isNotEmpty(qcOrderBoList)) {
                        final List<QcDetailPo> qcDetailPoList = qcOrderBoList.stream()
                                .map(QcOrderBo::getQcDetailPoList)
                                .flatMap(List::stream)
                                .collect(Collectors.toList());
                        passAmount = qcDetailPoList.stream().mapToInt(QcDetailPo::getPassAmount).sum();
                        notPassAmount = qcDetailPoList.stream().mapToInt(QcDetailPo::getNotPassAmount).sum();
                    }
                    int onShelvesAmount = Optional.ofNullable(onShelfList)
                            .orElse(Collections.emptyList())
                            .stream()
                            .filter(w -> null != w.getOnShelvesAmount())
                            .mapToInt(ReceiveOrderForScmVo.OnShelfOrder::getOnShelvesAmount)
                            .sum();

                    record.setQualityGoodsCnt(passAmount);
                    record.setDefectiveGoodsCnt(notPassAmount);
                    record.setOnShelvesAmount(onShelvesAmount);
                    final SupplierProductComparePo supplierProductComparePo = supplierProductCompareMap.get(record.getSupplierCode() + record.getSku());
                    if (null != supplierProductComparePo) {
                        record.setSupplierProductName(supplierProductComparePo.getSupplierProductName());
                    }
                }
            }
        }
    }
}
