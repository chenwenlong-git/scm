package com.hete.supply.scm.server.scm.purchase.service.base;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.plm.api.goods.entity.vo.PlmCategoryVo;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.entity.bo.RawDeliverBo;
import com.hete.supply.scm.server.scm.entity.bo.RawReceiveOrderBo;
import com.hete.supply.scm.server.scm.entity.dto.DeliveryOrderCreateMqDto;
import com.hete.supply.scm.server.scm.entity.dto.RawProductItemDto;
import com.hete.supply.scm.server.scm.entity.vo.RawReceiveOrderVo;
import com.hete.supply.scm.server.scm.entity.vo.WmsDetailVo;
import com.hete.supply.scm.server.scm.enums.DefaultDatabaseTime;
import com.hete.supply.scm.server.scm.handler.WmsRawDeliverHandler;
import com.hete.supply.scm.server.scm.purchase.converter.PurchaseConverter;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderRawDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderRawDeliverDao;
import com.hete.supply.scm.server.scm.purchase.entity.bo.ReceiptSignOffBo;
import com.hete.supply.scm.server.scm.purchase.entity.bo.WmsDeliverBo;
import com.hete.supply.scm.server.scm.purchase.entity.dto.DeliveryOrderCreateResultEventDto;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseChildNoListDto;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseSkuAndSupplierDto;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderRawDeliverPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderRawPo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseSkuPriceVo;
import com.hete.supply.scm.server.scm.purchase.enums.PurchaseSplitRaw;
import com.hete.supply.scm.server.scm.purchase.handler.PurchaseRawStrategy;
import com.hete.supply.scm.server.scm.qc.dao.QcDetailDao;
import com.hete.supply.scm.server.scm.qc.dao.QcOrderDao;
import com.hete.supply.scm.server.scm.qc.dao.QcReceiveOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcReceiveOrderPo;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.scm.supplier.converter.SupplierInventoryConverter;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierInventoryRecordDao;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierInventoryChangeBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierInventoryResultBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierInventorySubBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryRecordPo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierInventoryBaseService;
import com.hete.supply.scm.server.supplier.entity.dto.RawReceiptMqDto;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseRawReceiptOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseRawReceiptOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.ConfirmCommissioningItemDto;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseDeliverRawDto;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseRawReceiptOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseRawReceiptOrderPo;
import com.hete.supply.scm.server.supplier.supplier.entity.dto.InventorySubItemDto;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.entry.entity.dto.ReceiveOrderGetDto;
import com.hete.supply.wms.api.entry.entity.vo.ReceiveOrderForScmVo;
import com.hete.supply.wms.api.leave.entity.vo.ProcessDeliveryOrderVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.handler.HandlerContext;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/29 14:04
 */
@Service
@RequiredArgsConstructor
@Validated
public class PurchaseRawBaseService {
    private final IdGenerateService idGenerateService;
    private final PurchaseRawReceiptOrderDao purchaseRawReceiptOrderDao;
    private final PurchaseRawReceiptOrderItemDao purchaseRawReceiptOrderItemDao;
    private final LogBaseService logBaseService;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final WmsRemoteService wmsRemoteService;
    private final PurchaseDeliverOrderDao purchaseDeliverOrderDao;
    private final QcReceiveOrderDao qcReceiveOrderDao;
    private final QcOrderDao qcOrderDao;
    private final QcDetailDao qcDetailDao;
    private final PurchaseChildOrderRawDao purchaseChildOrderRawDao;
    private final SupplierInventoryRecordDao supplierInventoryRecordDao;
    private final SupplierInventoryBaseService supplierInventoryBaseService;
    private final PlmRemoteService plmRemoteService;
    private final PurchaseChildOrderRawDeliverDao purchaseChildOrderRawDeliverDao;
    private final ConsistencySendMqService consistencySendMqService;


    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.SCM_RAW_CREATE, key = "#rawReceiptMqDto.relatedOrderNo", waitTime = 1, leaseTime = -1)
    public void createRawReceiptOrder(DeliveryOrderCreateResultEventDto rawReceiptMqDto) {
        final String relatedOrderNo = rawReceiptMqDto.getRelatedOrderNo();
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(relatedOrderNo);
        Assert.notNull(purchaseChildOrderPo, () -> new BizException("找不到对应的采购子单，创建原料收货单失败"));

        final List<ProcessDeliveryOrderVo> processDeliveryOrderVoList = wmsRemoteService.getDeliveryOrderByDeliverNo(
                Collections.singletonList(rawReceiptMqDto.getDeliveryOrderNo()));
        if (CollectionUtils.isEmpty(processDeliveryOrderVoList)) {
            throw new BizException("wms返回的出库单号：{}有误，请联系系统管理员", rawReceiptMqDto.getDeliveryOrderNo());
        }

        //处理重复推送导致多次创建的问题
        List<PurchaseRawReceiptOrderPo> purchaseRawReceiptOrderPoList = purchaseRawReceiptOrderDao.getListByDeliverNo(rawReceiptMqDto.getDeliveryOrderNo());
        if (CollectionUtils.isNotEmpty(purchaseRawReceiptOrderPoList)) {
            return;
        }
        final ReceiptSignOffBo receiptSignOffBo = new ReceiptSignOffBo();
        receiptSignOffBo.setDeliveryOrderNo(rawReceiptMqDto.getDeliveryOrderNo());
        receiptSignOffBo.setPurchaseChildOrderPo(purchaseChildOrderPo);
        receiptSignOffBo.setReceiptOrderStatus(ReceiptOrderStatus.WAIT_DELIVER);
        this.createPurchaseRawReceiptOrder(receiptSignOffBo);
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.SCM_RAW_CREATE, key = "#rawReceiptMqDto.relatedOrderNo", waitTime = 1, leaseTime = -1)
    public void createRawReceiptOrder(RawReceiptMqDto rawReceiptMqDto) {
        final String relatedOrderNo = rawReceiptMqDto.getRelatedOrderNo();
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(relatedOrderNo);
        Assert.notNull(purchaseChildOrderPo, () -> new BizException("找不到对应的采购子单，创建原料收货单失败"));

        //处理重复推送导致多次创建的问题
        List<PurchaseRawReceiptOrderPo> purchaseRawReceiptOrderPoList = purchaseRawReceiptOrderDao.getListByDeliverNo(rawReceiptMqDto.getPurchaseRawDeliverOrderNo());
        if (CollectionUtils.isNotEmpty(purchaseRawReceiptOrderPoList)) {
            for (PurchaseRawReceiptOrderPo purchaseRawReceiptOrderPo : purchaseRawReceiptOrderPoList) {
                purchaseRawReceiptOrderPo.setLogistics(rawReceiptMqDto.getLogistics());
                purchaseRawReceiptOrderPo.setTrackingNo(rawReceiptMqDto.getTrackingNo());
                final ReceiptOrderStatus receiptOrderStatus = purchaseRawReceiptOrderPo.getReceiptOrderStatus().toWaitReceive();
                purchaseRawReceiptOrderPo.setReceiptOrderStatus(receiptOrderStatus);
                // 日志
                logBaseService.simpleLog(LogBizModule.SUPPLIER_RAW_RECEIPT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                        purchaseRawReceiptOrderPo.getPurchaseRawReceiptOrderNo(), receiptOrderStatus.getRemark(), Collections.emptyList());
            }
            purchaseRawReceiptOrderDao.updateBatchById(purchaseRawReceiptOrderPoList);
            return;
        }

        final ReceiptSignOffBo receiptSignOffBo = new ReceiptSignOffBo();
        receiptSignOffBo.setPurchaseChildOrderPo(purchaseChildOrderPo);
        receiptSignOffBo.setDeliveryOrderNo(rawReceiptMqDto.getPurchaseRawDeliverOrderNo());
        receiptSignOffBo.setLogistics(rawReceiptMqDto.getLogistics());
        receiptSignOffBo.setTrackingNo(rawReceiptMqDto.getTrackingNo());
        receiptSignOffBo.setReceiptOrderStatus(ReceiptOrderStatus.WAIT_RECEIVE);
        this.createPurchaseRawReceiptOrder(receiptSignOffBo);
    }

    private void createPurchaseRawReceiptOrder(ReceiptSignOffBo receiptSignOffBo) {
        final PurchaseChildOrderPo purchaseChildOrderPo = receiptSignOffBo.getPurchaseChildOrderPo();
        // 查询wms出库单号
        final List<ProcessDeliveryOrderVo> deliverOrderVoList = wmsRemoteService.getDeliveryOrderByDeliverNo(Collections.singletonList(receiptSignOffBo.getDeliveryOrderNo()));
        final ProcessDeliveryOrderVo processDeliveryOrderVo = deliverOrderVoList.get(0);
        // 创建原料收货单
        final String purchaseRawReceiptOrderNo = idGenerateService.getConfuseCode(ScmConstant.PURCHASE_RAW_ORDER_NO_PREFIX, TimeType.CN_DAY_YYYY, ConfuseLength.L_4);
        final PurchaseRawReceiptOrderPo purchaseRawReceiptOrderPo = new PurchaseRawReceiptOrderPo();
        purchaseRawReceiptOrderPo.setPurchaseRawReceiptOrderNo(purchaseRawReceiptOrderNo);
        purchaseRawReceiptOrderPo.setReceiptOrderStatus(receiptSignOffBo.getReceiptOrderStatus());
        purchaseRawReceiptOrderPo.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
        purchaseRawReceiptOrderPo.setSupplierName(purchaseChildOrderPo.getSupplierName());
        purchaseRawReceiptOrderPo.setDeliverCnt(processDeliveryOrderVo.getDeliveryAmount());
        purchaseRawReceiptOrderPo.setDeliverTime(LocalDateTime.now());
        purchaseRawReceiptOrderPo.setWarehouseCode(processDeliveryOrderVo.getWarehouseCode());
        purchaseRawReceiptOrderPo.setWarehouseName(processDeliveryOrderVo.getWarehouseName());
        purchaseRawReceiptOrderPo.setPurchaseChildOrderNo(processDeliveryOrderVo.getRelatedOrderNo());
        purchaseRawReceiptOrderPo.setLogistics(receiptSignOffBo.getLogistics());
        purchaseRawReceiptOrderPo.setTrackingNo(receiptSignOffBo.getTrackingNo());
        purchaseRawReceiptOrderPo.setPurchaseRawDeliverOrderNo(processDeliveryOrderVo.getDeliveryOrderNo());
        purchaseRawReceiptOrderPo.setRawReceiptBizType(RawReceiptBizType.PURCHASE);
        purchaseRawReceiptOrderDao.insert(purchaseRawReceiptOrderPo);

        // 创建原料收货单子项
        final List<ProcessDeliveryOrderVo.DeliveryProduct> deliveryProductList = processDeliveryOrderVo.getProducts();
        final List<PurchaseRawReceiptOrderItemPo> purchaseRawReceiptOrderItemPoList = deliveryProductList.stream().map(item -> {
            final PurchaseRawReceiptOrderItemPo purchaseRawReceiptOrderItemPo = new PurchaseRawReceiptOrderItemPo();
            purchaseRawReceiptOrderItemPo.setPurchaseRawReceiptOrderNo(purchaseRawReceiptOrderNo);
            purchaseRawReceiptOrderItemPo.setSkuBatchCode(item.getBatchCode());
            purchaseRawReceiptOrderItemPo.setSku(item.getSkuCode());
            purchaseRawReceiptOrderItemPo.setDeliverCnt(item.getAmount());
            return purchaseRawReceiptOrderItemPo;
        }).collect(Collectors.toList());
        purchaseRawReceiptOrderItemDao.insertBatch(purchaseRawReceiptOrderItemPoList);

        // 日志
        logBaseService.simpleLog(LogBizModule.SUPPLIER_RAW_RECEIPT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseRawReceiptOrderPo.getPurchaseRawReceiptOrderNo(), receiptSignOffBo.getReceiptOrderStatus().getRemark(), Collections.emptyList());
    }

    public List<RawReceiveOrderVo> rawReceiveOrder(String scmBizNo) {
        ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
        List<String> scmBizNoList = new ArrayList<>();
        scmBizNoList.add(scmBizNo);
        receiveOrderGetDto.setScmBizNoList(scmBizNoList);
        List<ReceiveOrderForScmVo> receiveOrderForScmVoList = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto);
        if (CollectionUtils.isEmpty(receiveOrderForScmVoList)) {
            return Collections.emptyList();
        }
        final List<RawReceiveOrderBo> rawReceiveOrderBoList = receiveOrderForScmVoList.stream()
                .map(vo -> {
                    final List<ReceiveOrderForScmVo.ReceiveDeliver> receiveDeliverList = vo.getReceiveDeliverList();
                    if (CollectionUtils.isEmpty(receiveDeliverList)) {
                        return new ArrayList<RawReceiveOrderBo>();
                    }

                    return receiveDeliverList.stream()
                            .map(item -> {
                                final RawReceiveOrderBo processOrderReceiveOrderBo = new RawReceiveOrderBo();
                                processOrderReceiveOrderBo.setSku(item.getSkuCode());
                                processOrderReceiveOrderBo.setSkuBatchCode(item.getBatchCode());
                                processOrderReceiveOrderBo.setAmount(vo.getReceiveAmount());
                                processOrderReceiveOrderBo.setReceiveOrderNo(vo.getReceiveOrderNo());
                                processOrderReceiveOrderBo.setReceiveAmount(item.getReceiveAmount());
                                processOrderReceiveOrderBo.setReceiveOrderState(vo.getReceiveOrderState());
                                processOrderReceiveOrderBo.setWarehouseCode(vo.getWarehouseCode());
                                processOrderReceiveOrderBo.setWarehouseName(vo.getWarehouseName());

                                return processOrderReceiveOrderBo;
                            }).collect(Collectors.toList());
                }).flatMap(Collection::stream)
                .collect(Collectors.toList());


        final Map<String, List<RawReceiveOrderBo>> skuProcessReceiveItemMap = rawReceiveOrderBoList.stream()
                .collect(Collectors.groupingBy(RawReceiveOrderBo::getSku));


        return skuProcessReceiveItemMap.keySet()
                .stream()
                .map(sku -> {
                    final List<RawReceiveOrderBo> processOrderReceiveOrderBoList1 = skuProcessReceiveItemMap.get(sku);
                    final RawReceiveOrderBo processOrderReceiveOrderBo = processOrderReceiveOrderBoList1.get(0);
                    final int amountSum = processOrderReceiveOrderBoList1.stream()
                            .mapToInt(RawReceiveOrderBo::getAmount)
                            .sum();
                    final RawReceiveOrderVo rawReceiveOrderVo = new RawReceiveOrderVo();
                    rawReceiveOrderVo.setSku(processOrderReceiveOrderBo.getSku());
                    rawReceiveOrderVo.setAmount(amountSum);
                    List<RawReceiveOrderVo.ReceiveOrderVo> receiveOrderVoList = PurchaseConverter.rawReceiveOrderBoToReceiveOrderVo(processOrderReceiveOrderBoList1);
                    rawReceiveOrderVo.setReceiveOrderVoList(receiveOrderVoList);

                    return rawReceiveOrderVo;
                }).collect(Collectors.toList());
    }

    public WmsDetailVo getWmsDetailVo(PurchaseChildNoListDto dto) {
        final List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList
                = purchaseDeliverOrderDao.getListByChildOrderNoList(dto.getPurchaseChildOrderNoList());

        if (CollectionUtils.isEmpty(purchaseDeliverOrderPoList)) {
            return new WmsDetailVo();
        }

        final List<String> purchaseDeliverOrderNoList = purchaseDeliverOrderPoList.stream()
                .map(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo)
                .collect(Collectors.toList());
        final ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
        receiveOrderGetDto.setScmBizNoList(purchaseDeliverOrderNoList);
        final List<ReceiveOrderForScmVo> receiveOrderList = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto);

        final List<String> receiveOrderNos = Optional.ofNullable(receiveOrderList)
                .orElse(Collections.emptyList())
                .stream()
                .map(ReceiveOrderForScmVo::getReceiveOrderNo)
                .collect(Collectors.toList());
        final List<QcReceiveOrderPo> qcReceiveOrderPos = qcReceiveOrderDao.getByReceiveNos(receiveOrderNos);
        final List<String> qcOrderNos = Optional.ofNullable(qcReceiveOrderPos)
                .orElse(Collections.emptyList())
                .stream()
                .map(QcReceiveOrderPo::getQcOrderNo)
                .collect(Collectors.toList());
        final List<QcOrderPo> qcOrderPos = qcOrderDao.getByQcOrderNos(qcOrderNos);
        final List<QcDetailPo> notPassQcDetailPos = qcDetailDao.getUnPassedListByQcOrderNoList(qcOrderNos);
        return PurchaseConverter.receiveOrderListToWmsDetailVoList(receiveOrderList, qcOrderPos, notPassQcDetailPos);
    }

    public List<PurchaseSkuPriceVo> getPurchasePriceBySkuAndSupplier(PurchaseSkuAndSupplierDto dto) {
        final List<PurchaseSkuPriceVo> purchaseSkuPriceList = purchaseChildOrderDao.getPurchasePriceBySkuAndSupplier(dto.getPurchaseSkuAndSupplierItemList(),
                Arrays.asList(PurchaseOrderStatus.WAIT_CONFIRM, PurchaseOrderStatus.WAIT_FOLLOWER_CONFIRM,
                        PurchaseOrderStatus.RETURN, PurchaseOrderStatus.DELETE), DefaultDatabaseTime.DEFAULT_TIME.getDateTime());

        final Map<String, PurchaseSkuPriceVo> purchaseSkuPriceVoMap = purchaseSkuPriceList.stream()
                .collect(Collectors.toMap(vo -> vo.getSupplierCode() + vo.getSku(), Function.identity(),
                        (p1, p2) -> p1.getReceiveOrderTime().isBefore(p2.getReceiveOrderTime()) ? p2 : p1));

        return new ArrayList<>(purchaseSkuPriceVoMap.values());
    }

    public void splitOrderSupplyRaw(PurchaseChildOrderPo purchaseChildOrderPo,
                                    PurchaseChildOrderPo newPurchaseChildOrderPo,
                                    Integer purchaseTotal) {
        // 辅料不处理
        if (SkuType.SM_SKU.equals(purchaseChildOrderPo.getSkuType())) {
            return;
        }
        final List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList = purchaseChildOrderRawDao.getListByChildNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        if (CollectionUtils.isEmpty(purchaseChildOrderRawPoList)) {
            return;
        }

        // 供应商原料处理列表
        final List<PurchaseChildOrderRawPo> supplierRawPoList = purchaseChildOrderRawPoList.stream()
                .filter(rawPo -> RawSupplier.SUPPLIER.equals(rawPo.getRawSupplier()))
                .collect(Collectors.toList());

        // 我司/其他供应商处理原料列表
        final List<PurchaseChildOrderRawPo> heteOtherSupplierRawPoList = purchaseChildOrderRawPoList.stream()
                .filter(rawPo -> RawSupplier.OTHER_SUPPLIER.equals(rawPo.getRawSupplier()) || RawSupplier.HETE.equals(rawPo.getRawSupplier()))
                .collect(Collectors.toList());

        final PurchaseOrderStatus purchaseOrderStatus = purchaseChildOrderPo.getPurchaseOrderStatus();

        if (CollectionUtils.isNotEmpty(supplierRawPoList)) {
            PurchaseSplitRaw splitRawMethod = null;

            //状态=待排产/待投产
            if (PurchaseOrderStatus.WAIT_SCHEDULING.equals(purchaseOrderStatus)
                    || PurchaseOrderStatus.WAIT_COMMISSIONING.equals(purchaseOrderStatus)) {
                // 原料来源=供应商
                splitRawMethod = PurchaseSplitRaw.BEFORE_DELIVER_SUPPLIER;
            }

            // 待投产后(大货采购类型)
            if (purchaseOrderStatus.getSort() > PurchaseOrderStatus.WAIT_COMMISSIONING.getSort()) {
                splitRawMethod = PurchaseSplitRaw.AFTER_DELIVER;
            }

            if (null == splitRawMethod) {
                throw new BizException("采购单:{}状态异常,请联系系统管理员", purchaseChildOrderPo.getPurchaseChildOrderNo());
            }
            // 复制一份新的原料数据
            List<PurchaseChildOrderRawPo> newPurchaseChildOrderRawPoList = supplierRawPoList.stream()
                    .map(rawPo -> PurchaseConverter.newRawPoByOldRawPo(rawPo, rawPo.getPurchaseRawBizType(),
                            newPurchaseChildOrderPo.getPurchaseParentOrderNo(),
                            newPurchaseChildOrderPo.getPurchaseChildOrderNo()))
                    .collect(Collectors.toList());
            final Map<Long, PurchaseChildOrderRawPo> idRawPoMap = supplierRawPoList.stream()
                    .collect(Collectors.toMap(PurchaseChildOrderRawPo::getPurchaseChildOrderRawId, Function.identity()));

            PurchaseRawStrategy handlerBean = HandlerContext.getHandlerBean(PurchaseRawStrategy.class, splitRawMethod);
            handlerBean.dealSplitPurchaseRaw(newPurchaseChildOrderRawPoList, idRawPoMap, supplierRawPoList,
                    purchaseChildOrderPo, newPurchaseChildOrderPo, purchaseTotal);

            // 投产前的旧单据释放后重新预占,新单据预占
            if (purchaseChildOrderPo.getPurchaseOrderStatus().getSort() <= PurchaseOrderStatus.WAIT_COMMISSIONING.getSort()) {
                // 旧单预占的库存释放，释放库存
                this.releaseSupplierRawInventory(purchaseChildOrderPo, SupplierInventoryCtrlReason.SPLIT_ORDER);

                // 旧单重新预占库存
                final List<RawProductItemDto> rawProductItemList = supplierRawPoList.stream()
                        .filter(rawPo -> PurchaseRawBizType.DEMAND.equals(rawPo.getPurchaseRawBizType()))
                        .map(rawPo -> {
                            final RawProductItemDto rawProductItemDto = new RawProductItemDto();
                            rawProductItemDto.setSku(rawPo.getSku());
                            rawProductItemDto.setDeliveryCnt(rawPo.getDeliveryCnt());
                            return rawProductItemDto;
                        }).collect(Collectors.toList());
                this.campOnInventory(purchaseChildOrderPo, rawProductItemList, SupplierInventoryCtrlReason.SPLIT_ORDER);

                // 新单重新预占库存
                final List<RawProductItemDto> rawProductItemList1 = newPurchaseChildOrderRawPoList.stream()
                        .filter(rawPo -> PurchaseRawBizType.DEMAND.equals(rawPo.getPurchaseRawBizType()))
                        .filter(rawPo -> rawPo.getDeliveryCnt() > 0)
                        .map(rawPo -> {
                            final RawProductItemDto rawProductItemDto = new RawProductItemDto();
                            rawProductItemDto.setSku(rawPo.getSku());
                            rawProductItemDto.setDeliveryCnt(rawPo.getDispenseCnt());
                            return rawProductItemDto;
                        }).collect(Collectors.toList());
                this.campOnInventory(newPurchaseChildOrderPo, rawProductItemList1, SupplierInventoryCtrlReason.SPLIT_ORDER);
            }
        }

        // 原料来源=其他供应商/我司
        if (CollectionUtils.isNotEmpty(heteOtherSupplierRawPoList)) {
            // 复制一份新的原料数据
            List<PurchaseChildOrderRawPo> newPurchaseChildOrderRawPoList = heteOtherSupplierRawPoList.stream()
                    .map(rawPo -> PurchaseConverter.newRawPoByOldRawPo(rawPo, rawPo.getPurchaseRawBizType(),
                            newPurchaseChildOrderPo.getPurchaseParentOrderNo(),
                            newPurchaseChildOrderPo.getPurchaseChildOrderNo()))
                    .collect(Collectors.toList());
            final Map<Long, PurchaseChildOrderRawPo> idRawPoMap = heteOtherSupplierRawPoList.stream()
                    .collect(Collectors.toMap(PurchaseChildOrderRawPo::getPurchaseChildOrderRawId, Function.identity()));
            PurchaseRawStrategy handlerBean = HandlerContext.getHandlerBean(PurchaseRawStrategy.class, PurchaseSplitRaw.BEFORE_DELIVER_COMPANY_OR_OTHER);
            handlerBean.dealSplitPurchaseRaw(newPurchaseChildOrderRawPoList, idRawPoMap, heteOtherSupplierRawPoList,
                    purchaseChildOrderPo, newPurchaseChildOrderPo, purchaseTotal);

        }
    }

    public List<RawReceiveOrderVo> getSupplierRecordOrderList(String purchaseChildOrderNo) {
        final List<SupplierInventoryRecordPo> supplierInventoryRecordPoList = supplierInventoryRecordDao.getListByRelateNo(purchaseChildOrderNo,
                SupplierInventoryCtrlReason.PURCHASE_RAW_RETURN);

        if (CollectionUtils.isEmpty(supplierInventoryRecordPoList)) {
            return new ArrayList<>();
        }
        final List<RawReceiveOrderVo.ReceiveOrderVo> receiveOrderVoList = supplierInventoryRecordPoList.stream().map(recordPo -> {
            final RawReceiveOrderVo.ReceiveOrderVo receiveOrderVo = new RawReceiveOrderVo.ReceiveOrderVo();
            receiveOrderVo.setSku(recordPo.getSku());
            receiveOrderVo.setPurchaseRawReceiptOrderNo(recordPo.getSupplierInventoryRecordId() + "");
            receiveOrderVo.setReceiptCnt(recordPo.getCtrlCnt());
            receiveOrderVo.setWarehouseName(recordPo.getSupplierCode() + recordPo.getSupplierWarehouse().getName());

            return receiveOrderVo;
        }).collect(Collectors.toList());

        final Map<String, List<RawReceiveOrderVo.ReceiveOrderVo>> skuReceivePoListMap = receiveOrderVoList.stream().collect(Collectors.groupingBy(RawReceiveOrderVo.ReceiveOrderVo::getSku));

        return skuReceivePoListMap.entrySet().stream().map(entry -> {
            final RawReceiveOrderVo rawReceiveOrderVo = new RawReceiveOrderVo();
            final List<RawReceiveOrderVo.ReceiveOrderVo> receiveOrderVoList1 = entry.getValue();
            rawReceiveOrderVo.setSku(entry.getKey());
            rawReceiveOrderVo.setAmount(receiveOrderVoList1.stream()
                    .mapToInt(RawReceiveOrderVo.ReceiveOrderVo::getReceiptCnt)
                    .sum());
            rawReceiveOrderVo.setReceiveOrderVoList(receiveOrderVoList1);
            return rawReceiveOrderVo;
        }).collect(Collectors.toList());
    }

    /**
     * 投产原料处理
     *
     * @param confirmCommissioningItemList
     * @param purchaseChildOrderPo
     * @param purchaseChildOrderRawPoList  demand类型的原料list
     */
    public void confirmCommissionRaw(List<ConfirmCommissioningItemDto> confirmCommissioningItemList,
                                     PurchaseChildOrderPo purchaseChildOrderPo,
                                     List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList) {
        final List<String> skuList = confirmCommissioningItemList.stream()
                .map(ConfirmCommissioningItemDto::getSku)
                .collect(Collectors.toList());

        final Map<String, Integer> skuActualConsumeMap = confirmCommissioningItemList.stream()
                .collect(Collectors.toMap(ConfirmCommissioningItemDto::getSku, ConfirmCommissioningItemDto::getActualConsumeCnt));

        final List<String> rawSkuList = purchaseChildOrderRawPoList.stream()
                .map(PurchaseChildOrderRawPo::getSku)
                .collect(Collectors.toList());
        List<InventorySubItemDto> inventorySubItemList = new ArrayList<>();
        final Map<String, PlmCategoryVo> skuCategoryMap = plmRemoteService.getSkuSecondCategoriesVoMapBySkuList(rawSkuList);
        final List<PurchaseChildOrderRawPo> dbActualRawPoList = purchaseChildOrderRawDao.getListByChildNo(purchaseChildOrderPo.getPurchaseChildOrderNo(),
                PurchaseRawBizType.ACTUAL_DELIVER, Collections.singletonList(RawSupplier.SUPPLIER));

        final Map<String, PurchaseChildOrderRawPo> skuActualRawPoMap = dbActualRawPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderRawPo::getSku, Function.identity(), (item1, item2) -> item1));

        // 实发原料数据
        final List<PurchaseChildOrderRawPo> actualDeliverRawPoList = new ArrayList<>();
        final List<PurchaseChildOrderRawDeliverPo> purchaseChildOrderRawDeliverPoList = new ArrayList<>();
        for (PurchaseChildOrderRawPo rawPo : purchaseChildOrderRawPoList) {
            Integer actualConsumeCnt = skuActualConsumeMap.get(rawPo.getSku());
            if (null == actualConsumeCnt) {
                throw new BizException("系统数据存在异常，无法找到sku:{}的原料信息，请联系系统管理员!", rawPo.getSku());
            }
            rawPo.setActualConsumeCnt(rawPo.getActualConsumeCnt() + actualConsumeCnt);
            // 若存在实发原料，则赋值，不存在则新增原料实发数据
            PurchaseChildOrderRawPo actualDeliverRawPo = skuActualRawPoMap.get(rawPo.getSku());
            if (null == actualDeliverRawPo) {
                actualDeliverRawPo = PurchaseConverter.newRawPoByOldRawPo(rawPo,
                        PurchaseRawBizType.ACTUAL_DELIVER, rawPo.getPurchaseParentOrderNo(),
                        rawPo.getPurchaseChildOrderNo());
                actualDeliverRawPo.setPurchaseChildOrderRawId(null);
                actualDeliverRawPo.setDeliveryCnt(0);
                actualDeliverRawPo.setReceiptCnt(0);
                actualDeliverRawPo.setDispenseCnt(0);
            }

            actualDeliverRawPo.setDeliveryCnt(actualDeliverRawPo.getDeliveryCnt() + actualConsumeCnt);
            actualDeliverRawPo.setReceiptCnt(actualDeliverRawPo.getReceiptCnt() + actualConsumeCnt);
            actualDeliverRawPo.setDispenseCnt(actualDeliverRawPo.getDispenseCnt() + actualConsumeCnt);
            actualDeliverRawPoList.add(actualDeliverRawPo);

            final List<SupplierInventoryPo> supplierInventoryPoList = supplierInventoryBaseService.getInventoryBySkuAndSupplier(skuList,
                    purchaseChildOrderPo.getSupplierCode());
            final Map<String, SupplierInventoryPo> skuInventoryMap = supplierInventoryPoList.stream()
                    .collect(Collectors.toMap(SupplierInventoryPo::getSku, Function.identity()));
            final SupplierInventoryPo supplierInventoryPo = skuInventoryMap.get(rawPo.getSku());
            if (null == supplierInventoryPo) {
                throw new BizException("获取不到供应商:{}对应的sku:{}库存，数据存在异常，请联系系统管理员！",
                        purchaseChildOrderPo.getSupplierCode(), rawPo.getSku());
            }
            final SupplierInventorySubBo supplierInventorySubBo = new SupplierInventorySubBo();
            supplierInventorySubBo.setStockUpInventory(supplierInventoryPo.getStockUpInventory());
            supplierInventorySubBo.setActualConsumeCnt(actualConsumeCnt);
            final SupplierInventoryResultBo subResultByBo = supplierInventoryBaseService.getSubResultByBo(supplierInventorySubBo);
            final Integer stockUpDecrement = subResultByBo.getStockUpDecrement();
            final Integer selfProvideDecrement = subResultByBo.getSelfProvideDecrement();
            final InventorySubItemDto inventorySubItemDto = new InventorySubItemDto();
            inventorySubItemDto.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
            inventorySubItemDto.setSku(rawPo.getSku());
            inventorySubItemDto.setStockUpChangeInventory(stockUpDecrement);
            inventorySubItemDto.setSelfProvideChangeInventory(selfProvideDecrement);
            inventorySubItemList.add(inventorySubItemDto);

            // 库存操作记录
            final SupplierInventoryRecordPo stockUpInventoryRecordPo = SupplierInventoryConverter.commissioningInventoryRecord(purchaseChildOrderPo,
                    rawPo.getSku(), SupplierWarehouse.STOCK_UP, SupplierInventoryCtrlType.OUTBOUND,
                    supplierInventoryPo.getStockUpInventory(), stockUpDecrement,
                    SupplierInventoryCtrlReason.PRODUCT_COMMISSIONING, purchaseChildOrderPo.getPurchaseChildOrderNo(),
                    skuCategoryMap.get(rawPo.getSku()), SupplierInventoryRecordStatus.EFFECTIVE);
            final SupplierInventoryRecordPo selfProvideInventoryRecordPo = SupplierInventoryConverter.commissioningInventoryRecord(purchaseChildOrderPo,
                    rawPo.getSku(), SupplierWarehouse.SELF_PROVIDE, SupplierInventoryCtrlType.OUTBOUND,
                    supplierInventoryPo.getSelfProvideInventory(), selfProvideDecrement,
                    SupplierInventoryCtrlReason.PRODUCT_COMMISSIONING, purchaseChildOrderPo.getPurchaseChildOrderNo(),
                    skuCategoryMap.get(rawPo.getSku()), SupplierInventoryRecordStatus.EFFECTIVE);
            if (stockUpDecrement != 0) {
                supplierInventoryRecordDao.insert(stockUpInventoryRecordPo);
                // 增加一个关联id记录
                final PurchaseChildOrderRawDeliverPo purchaseChildOrderRawDeliverPo = PurchaseConverter.rawPoToRawDeliverPo(rawPo, stockUpInventoryRecordPo.getCtrlCnt(),
                        "", stockUpInventoryRecordPo.getSupplierInventoryRecordId(), RawSupplier.SUPPLIER, BooleanType.FALSE);
                purchaseChildOrderRawDeliverPoList.add(purchaseChildOrderRawDeliverPo);
            }
            if (selfProvideDecrement != 0) {
                supplierInventoryRecordDao.insert(selfProvideInventoryRecordPo);
                // 增加一个关联id记录
                final PurchaseChildOrderRawDeliverPo purchaseChildOrderRawDeliverPo = PurchaseConverter.rawPoToRawDeliverPo(rawPo, selfProvideInventoryRecordPo.getCtrlCnt(),
                        "", selfProvideInventoryRecordPo.getSupplierInventoryRecordId(), RawSupplier.SUPPLIER, BooleanType.FALSE);
                purchaseChildOrderRawDeliverPoList.add(purchaseChildOrderRawDeliverPo);
            }
        }
        purchaseChildOrderRawDao.insertOrUpdateBatch(actualDeliverRawPoList);
        purchaseChildOrderRawDao.updateBatchByIdVersion(purchaseChildOrderRawPoList);
        supplierInventoryBaseService.subInventoryBySkuAndSupplier(inventorySubItemList);
        purchaseChildOrderRawDeliverDao.insertBatch(purchaseChildOrderRawDeliverPoList);
    }

    /**
     * 释放库存
     *
     * @param purchaseChildOrderPo
     * @param supplierInventoryCtrlReason
     */
    public void releaseSupplierRawInventory(PurchaseChildOrderPo purchaseChildOrderPo,
                                            SupplierInventoryCtrlReason supplierInventoryCtrlReason) {
        final List<PurchaseChildOrderRawPo> supplierRawPoList = purchaseChildOrderRawDao.getListByChildNo(purchaseChildOrderPo.getPurchaseChildOrderNo(),
                PurchaseRawBizType.DEMAND, RawSupplier.SUPPLIER);

        // 不存在供应商原料，不需要释放原料
        if (CollectionUtils.isEmpty(supplierRawPoList)) {
            return;
        }
        final List<String> rawSkuList = supplierRawPoList.stream()
                .map(PurchaseChildOrderRawPo::getSku)
                .distinct()
                .collect(Collectors.toList());

        final Map<String, PlmCategoryVo> skuCategoryMap = plmRemoteService.getSkuSecondCategoriesVoMapBySkuList(rawSkuList);
        final List<SupplierInventoryPo> supplierInventoryPoList = supplierInventoryBaseService.getInventoryBySkuAndSupplier(rawSkuList,
                purchaseChildOrderPo.getSupplierCode());

        // 查找是否存在预占的库存记录，区分新旧数据处理
        final List<PurchaseChildOrderRawDeliverPo> dbRawDeliverPoList = purchaseChildOrderRawDeliverDao.getListByChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        final List<Long> recordIdList = dbRawDeliverPoList.stream().map(PurchaseChildOrderRawDeliverPo::getSupplierInventoryRecordId)
                .filter(id -> id != 0).collect(Collectors.toList());
        final List<SupplierInventoryRecordPo> campOnRecordList = supplierInventoryRecordDao.getListByIdList(recordIdList, SupplierInventoryCtrlType.CAMP_ON);
        // 若存在预占记录，则将预占记录的库存加回供应商库存，再按照投产数据计算扣减。（可用库存增加，冻结库存减少）
        if (CollectionUtils.isNotEmpty(campOnRecordList)) {
            final Map<String, SupplierInventoryRecordPo> skuInventoryRecordPoMap = campOnRecordList.stream()
                    .collect(Collectors.toMap(recordPo -> recordPo.getSku() + recordPo.getSupplierWarehouse(),
                            Function.identity(), (item1, item2) -> item1));
            final List<SupplierInventoryChangeBo> supplierInventoryChangeBoList = new ArrayList<>();
            final List<SupplierInventoryRecordPo> updateSupplierInventoryRecordPoList = new ArrayList<>();
            for (SupplierInventoryPo supplierInventoryPo : supplierInventoryPoList) {
                final SupplierInventoryRecordPo stockUpRecordPo = skuInventoryRecordPoMap.get(supplierInventoryPo.getSku() + SupplierWarehouse.STOCK_UP);
                final SupplierInventoryRecordPo selfProvideRecordPo = skuInventoryRecordPoMap.get(supplierInventoryPo.getSku() + SupplierWarehouse.SELF_PROVIDE);
                if (null == stockUpRecordPo && null == selfProvideRecordPo) {
                    continue;
                }
                if (null != stockUpRecordPo) {
                    final SupplierInventoryChangeBo supplierInventoryChangeBo = new SupplierInventoryChangeBo();
                    supplierInventoryChangeBo.setSku(stockUpRecordPo.getSku());
                    supplierInventoryChangeBo.setSupplierCode(stockUpRecordPo.getSupplierCode());
                    supplierInventoryChangeBo.setStockUpInventory(stockUpRecordPo.getCtrlCnt());
                    supplierInventoryChangeBo.setFrzStockUpInventory(-stockUpRecordPo.getCtrlCnt());
                    // 库存操作记录
                    final SupplierInventoryRecordPo stockUpInventoryRecordPo = SupplierInventoryConverter.commissioningInventoryRecord(purchaseChildOrderPo,
                            stockUpRecordPo.getSku(), SupplierWarehouse.STOCK_UP, SupplierInventoryCtrlType.RELEASE,
                            supplierInventoryPo.getStockUpInventory(), -stockUpRecordPo.getCtrlCnt(),
                            supplierInventoryCtrlReason, purchaseChildOrderPo.getPurchaseChildOrderNo(),
                            skuCategoryMap.get(stockUpRecordPo.getSku()), SupplierInventoryRecordStatus.EFFECTIVE);
                    supplierInventoryRecordDao.insert(stockUpInventoryRecordPo);
                    supplierInventoryChangeBoList.add(supplierInventoryChangeBo);
                }
                if (null != selfProvideRecordPo) {
                    final SupplierInventoryChangeBo supplierInventoryChangeBo = new SupplierInventoryChangeBo();
                    supplierInventoryChangeBo.setSku(selfProvideRecordPo.getSku());
                    supplierInventoryChangeBo.setSupplierCode(selfProvideRecordPo.getSupplierCode());
                    supplierInventoryChangeBo.setSelfProvideInventory(selfProvideRecordPo.getCtrlCnt());
                    supplierInventoryChangeBo.setFrzSelfProvideInventory(-selfProvideRecordPo.getCtrlCnt());

                    final SupplierInventoryRecordPo selfProvideInventoryRecordPo = SupplierInventoryConverter.commissioningInventoryRecord(purchaseChildOrderPo,
                            selfProvideRecordPo.getSku(), SupplierWarehouse.SELF_PROVIDE, SupplierInventoryCtrlType.RELEASE,
                            supplierInventoryPo.getSelfProvideInventory(), -selfProvideRecordPo.getCtrlCnt(),
                            supplierInventoryCtrlReason, purchaseChildOrderPo.getPurchaseChildOrderNo(),
                            skuCategoryMap.get(selfProvideRecordPo.getSku()), SupplierInventoryRecordStatus.EFFECTIVE);
                    supplierInventoryRecordDao.insert(selfProvideInventoryRecordPo);
                    supplierInventoryChangeBoList.add(supplierInventoryChangeBo);
                }
            }

            // 删除预占记录跟采购单的关联
            final List<Long> idList = dbRawDeliverPoList.stream().filter(po -> po.getSupplierInventoryRecordId() != 0)
                    .map(PurchaseChildOrderRawDeliverPo::getPurchaseChildOrderRawDeliverId)
                    .collect(Collectors.toList());
            purchaseChildOrderRawDeliverDao.removeBatchByIds(idList);
            supplierInventoryBaseService.inventoryChange(supplierInventoryChangeBoList);
            supplierInventoryRecordDao.insertBatch(updateSupplierInventoryRecordPoList);
        }
    }

    /**
     * 创建发货单额外消耗原料
     *
     * @param purchaseDeliverRawDtoList
     * @param purchaseChildOrderPo
     */
    public void createDeliverExtraRawConsume(@Valid List<PurchaseDeliverRawDto> purchaseDeliverRawDtoList,
                                             PurchaseChildOrderPo purchaseChildOrderPo) {
        // 额外库存消耗,过滤掉0的
        final List<PurchaseDeliverRawDto> purchaseDeliverRawList = Optional.ofNullable(purchaseDeliverRawDtoList)
                .orElse(new ArrayList<>()).stream()
                .filter(rawDto -> rawDto.getExtraCnt() != 0)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(purchaseDeliverRawList)) {
            return;
        }
        // 入参校验，额外消耗数小于0的，告警
        purchaseDeliverRawList.forEach(rawDto -> {
            if (rawDto.getExtraCnt() < 0) {
                throw new ParamIllegalException("sku:{}的额外消耗数:{}小于0，请重新填写", rawDto.getSku(), rawDto.getExtraCnt());
            }
        });

        List<InventorySubItemDto> inventorySubItemList = new ArrayList<>();
        final List<PurchaseChildOrderRawDeliverPo> purchaseChildOrderRawDeliverPoList = new ArrayList<>();

        final List<String> rawSkuList = purchaseDeliverRawList.stream()
                .map(PurchaseDeliverRawDto::getSku)
                .collect(Collectors.toList());
        final List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList = purchaseChildOrderRawDao.getListByChildNo(purchaseChildOrderPo.getPurchaseChildOrderNo(),
                Arrays.asList(PurchaseRawBizType.DEMAND, PurchaseRawBizType.ACTUAL_DELIVER), RawSupplier.SUPPLIER);
        final Map<String, PurchaseChildOrderRawPo> demandSkuRawPoMap = purchaseChildOrderRawPoList.stream()
                .filter(rawPo -> PurchaseRawBizType.DEMAND.equals(rawPo.getPurchaseRawBizType()))
                .collect(Collectors.toMap(PurchaseChildOrderRawPo::getSku, Function.identity()));
        final Map<String, PurchaseChildOrderRawPo> actualDeliverSkuRawPoMap = purchaseChildOrderRawPoList.stream()
                .filter(rawPo -> PurchaseRawBizType.ACTUAL_DELIVER.equals(rawPo.getPurchaseRawBizType()))
                .collect(Collectors.toMap(PurchaseChildOrderRawPo::getSku, Function.identity()));
        final List<SupplierInventoryPo> supplierInventoryPoList = supplierInventoryBaseService.getInventoryBySkuAndSupplier(rawSkuList,
                purchaseChildOrderPo.getSupplierCode());
        final Map<String, SupplierInventoryPo> skuInventoryMap = supplierInventoryPoList.stream()
                .collect(Collectors.toMap(SupplierInventoryPo::getSku, Function.identity()));

        final Map<String, PlmCategoryVo> skuCategoryMap = plmRemoteService.getSkuSecondCategoriesVoMapBySkuList(rawSkuList);

        List<PurchaseChildOrderRawPo> updateRawPoList = new ArrayList<>();
        List<PurchaseChildOrderRawPo> insertRawPoList = new ArrayList<>();

        purchaseDeliverRawList.forEach(rawDto -> {
            final PurchaseChildOrderRawPo demandRawPo = demandSkuRawPoMap.get(rawDto.getSku());
            if (null == demandRawPo) {
                throw new BizException("采购单不存在sku:{}原料信息，数据存在异常，请联系系统管理员！");
            }
            demandRawPo.setExtraCnt(rawDto.getExtraCnt());
            demandRawPo.setDispenseCnt(demandRawPo.getDispenseCnt() + rawDto.getExtraCnt());
            updateRawPoList.add(demandRawPo);
            PurchaseChildOrderRawPo actualDeliverRawPo = actualDeliverSkuRawPoMap.get(rawDto.getSku());
            if (null == actualDeliverRawPo) {
                // 新增原料实发数据
                actualDeliverRawPo = PurchaseConverter.newRawPoByOldRawPo(demandRawPo,
                        PurchaseRawBizType.ACTUAL_DELIVER, demandRawPo.getPurchaseParentOrderNo(),
                        demandRawPo.getPurchaseChildOrderNo());
                actualDeliverRawPo.setDeliveryCnt(rawDto.getExtraCnt());
                actualDeliverRawPo.setReceiptCnt(rawDto.getExtraCnt());
                actualDeliverRawPo.setExtraCnt(rawDto.getExtraCnt());
                actualDeliverRawPo.setDispenseCnt(rawDto.getExtraCnt());
                insertRawPoList.add(actualDeliverRawPo);
            } else {
                actualDeliverRawPo.setDeliveryCnt(actualDeliverRawPo.getDeliveryCnt() + rawDto.getExtraCnt());
                actualDeliverRawPo.setReceiptCnt(actualDeliverRawPo.getReceiptCnt() + rawDto.getExtraCnt());
                actualDeliverRawPo.setExtraCnt(rawDto.getExtraCnt());
                actualDeliverRawPo.setDispenseCnt(actualDeliverRawPo.getDispenseCnt() + rawDto.getExtraCnt());
                updateRawPoList.add(actualDeliverRawPo);
            }


            // 扣减库存
            final SupplierInventoryPo supplierInventoryPo = skuInventoryMap.get(rawDto.getSku());
            if (null == supplierInventoryPo) {
                throw new BizException("获取不到供应商:{}对应的sku:{}库存，数据存在异常，请联系系统管理员！",
                        purchaseChildOrderPo.getSupplierCode(), rawDto.getSku());
            }
            final SupplierInventorySubBo supplierInventorySubBo = new SupplierInventorySubBo();
            supplierInventorySubBo.setStockUpInventory(supplierInventoryPo.getStockUpInventory());
            supplierInventorySubBo.setActualConsumeCnt(rawDto.getExtraCnt());
            final SupplierInventoryResultBo subResultByBo = supplierInventoryBaseService.getSubResultByBo(supplierInventorySubBo);
            final Integer stockUpDecrement = subResultByBo.getStockUpDecrement();
            final Integer selfProvideDecrement = subResultByBo.getSelfProvideDecrement();
            final InventorySubItemDto inventorySubItemDto = new InventorySubItemDto();
            inventorySubItemDto.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
            inventorySubItemDto.setSku(rawDto.getSku());
            inventorySubItemDto.setStockUpChangeInventory(stockUpDecrement);
            inventorySubItemDto.setSelfProvideChangeInventory(selfProvideDecrement);
            inventorySubItemList.add(inventorySubItemDto);

            // 库存操作记录
            final SupplierInventoryRecordPo stockUpInventoryRecordPo = SupplierInventoryConverter.commissioningInventoryRecord(purchaseChildOrderPo,
                    rawDto.getSku(), SupplierWarehouse.STOCK_UP, SupplierInventoryCtrlType.OUTBOUND,
                    supplierInventoryPo.getStockUpInventory(), stockUpDecrement,
                    SupplierInventoryCtrlReason.DELIVER_COMMISSIONING, purchaseChildOrderPo.getPurchaseChildOrderNo(), skuCategoryMap.get(rawDto.getSku()), SupplierInventoryRecordStatus.EFFECTIVE);
            final SupplierInventoryRecordPo selfProvideInventoryRecordPo = SupplierInventoryConverter.commissioningInventoryRecord(purchaseChildOrderPo,
                    rawDto.getSku(), SupplierWarehouse.SELF_PROVIDE, SupplierInventoryCtrlType.OUTBOUND,
                    supplierInventoryPo.getSelfProvideInventory(), selfProvideDecrement,
                    SupplierInventoryCtrlReason.DELIVER_COMMISSIONING, purchaseChildOrderPo.getPurchaseChildOrderNo(), skuCategoryMap.get(rawDto.getSku()), SupplierInventoryRecordStatus.EFFECTIVE);
            if (stockUpDecrement != 0) {
                supplierInventoryRecordDao.insert(stockUpInventoryRecordPo);
                // 增加一个关联id记录
                final PurchaseChildOrderRawDeliverPo purchaseChildOrderRawDeliverPo = PurchaseConverter.rawPoToRawDeliverPo(demandRawPo, stockUpInventoryRecordPo.getCtrlCnt(),
                        "", stockUpInventoryRecordPo.getSupplierInventoryRecordId(), RawSupplier.SUPPLIER, BooleanType.FALSE);
                purchaseChildOrderRawDeliverPoList.add(purchaseChildOrderRawDeliverPo);
            }
            if (selfProvideDecrement != 0) {
                supplierInventoryRecordDao.insert(selfProvideInventoryRecordPo);
                // 增加一个关联id记录
                final PurchaseChildOrderRawDeliverPo purchaseChildOrderRawDeliverPo = PurchaseConverter.rawPoToRawDeliverPo(demandRawPo, selfProvideInventoryRecordPo.getCtrlCnt(),
                        "", selfProvideInventoryRecordPo.getSupplierInventoryRecordId(), RawSupplier.SUPPLIER, BooleanType.FALSE);
                purchaseChildOrderRawDeliverPoList.add(purchaseChildOrderRawDeliverPo);
            }
        });
        // 保存库存记录
        supplierInventoryBaseService.subInventoryBySkuAndSupplier(inventorySubItemList);
        purchaseChildOrderRawDao.updateBatchByIdVersion(updateRawPoList);
        purchaseChildOrderRawDeliverDao.insertBatch(purchaseChildOrderRawDeliverPoList);
        purchaseChildOrderRawDao.insertBatch(insertRawPoList);

    }

    /**
     * 预占库存
     * 库存预占：可用库存减少，冻结库存增加。优先扣减备货库存再扣减自备库存。
     *
     * @param purchaseChildOrderPo
     * @param rawProductItemList
     * @param reason
     */
    public void campOnInventory(PurchaseChildOrderPo purchaseChildOrderPo, List<RawProductItemDto> rawProductItemList,
                                SupplierInventoryCtrlReason reason) {
        final List<String> rawSkuList = rawProductItemList.stream()
                .map(RawProductItemDto::getSku)
                .distinct()
                .collect(Collectors.toList());
        // 查找供应商sku库
        final List<SupplierInventoryPo> supplierInventoryPoList = supplierInventoryBaseService.getInventoryBySkuAndSupplier(rawSkuList, purchaseChildOrderPo.getSupplierCode());
        final Map<String, SupplierInventoryPo> skuInventoryMap = supplierInventoryPoList.stream()
                .collect(Collectors.toMap(SupplierInventoryPo::getSku, Function.identity()));


        final Map<String, PlmCategoryVo> skuCategoryMap = plmRemoteService.getSkuSecondCategoriesVoMapBySkuList(rawSkuList);
        final List<PurchaseChildOrderRawDeliverPo> purchaseChildOrderRawDeliverPoList = new ArrayList<>();

        final List<SupplierInventoryChangeBo> supplierInventoryChangeBoList = new ArrayList<>();
        for (RawProductItemDto rawProductItemDto : rawProductItemList) {
            final SupplierInventoryPo supplierInventoryPo = skuInventoryMap.get(rawProductItemDto.getSku());
            final SupplierInventorySubBo supplierInventorySubBo = new SupplierInventorySubBo();
            supplierInventorySubBo.setStockUpInventory(supplierInventoryPo.getStockUpInventory());
            supplierInventorySubBo.setActualConsumeCnt(rawProductItemDto.getDeliveryCnt());
            final SupplierInventoryResultBo subResultByBo = supplierInventoryBaseService.getSubResultByBo(supplierInventorySubBo);
            final Integer stockUpDecrement = subResultByBo.getStockUpDecrement();
            final Integer selfProvideDecrement = subResultByBo.getSelfProvideDecrement();

            if (stockUpDecrement != 0) {
                final SupplierInventoryChangeBo supplierInventoryChangeBo = new SupplierInventoryChangeBo();
                supplierInventoryChangeBo.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
                supplierInventoryChangeBo.setSku(rawProductItemDto.getSku());
                supplierInventoryChangeBo.setStockUpInventory(-stockUpDecrement);
                supplierInventoryChangeBo.setFrzStockUpInventory(stockUpDecrement);
                supplierInventoryChangeBoList.add(supplierInventoryChangeBo);
                // 日志记录
                final SupplierInventoryRecordPo stockUpInventoryRecordPo = SupplierInventoryConverter.commissioningInventoryRecord(purchaseChildOrderPo,
                        rawProductItemDto.getSku(), SupplierWarehouse.STOCK_UP, SupplierInventoryCtrlType.CAMP_ON,
                        supplierInventoryPo.getStockUpInventory(), stockUpDecrement,
                        reason, purchaseChildOrderPo.getPurchaseChildOrderNo(),
                        skuCategoryMap.get(rawProductItemDto.getSku()), SupplierInventoryRecordStatus.EFFECTIVE);
                supplierInventoryRecordDao.insert(stockUpInventoryRecordPo);
                // 增加一个关联id记录
                final PurchaseChildOrderRawDeliverPo purchaseChildOrderRawDeliverPo = PurchaseConverter.rawPoToRawDeliverPo(rawProductItemDto.getSku(), purchaseChildOrderPo, stockUpInventoryRecordPo.getCtrlCnt(),
                        "", stockUpInventoryRecordPo.getSupplierInventoryRecordId(), RawSupplier.SUPPLIER, BooleanType.FALSE);
                purchaseChildOrderRawDeliverPoList.add(purchaseChildOrderRawDeliverPo);

            }
            if (selfProvideDecrement != 0) {
                final SupplierInventoryChangeBo supplierInventoryChangeBo = new SupplierInventoryChangeBo();
                supplierInventoryChangeBo.setSku(rawProductItemDto.getSku());
                supplierInventoryChangeBo.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
                supplierInventoryChangeBo.setSelfProvideInventory(-selfProvideDecrement);
                supplierInventoryChangeBo.setFrzSelfProvideInventory(selfProvideDecrement);
                supplierInventoryChangeBoList.add(supplierInventoryChangeBo);

                // 日志记录
                final SupplierInventoryRecordPo selfProvideInventoryRecordPo = SupplierInventoryConverter.commissioningInventoryRecord(purchaseChildOrderPo,
                        rawProductItemDto.getSku(), SupplierWarehouse.SELF_PROVIDE, SupplierInventoryCtrlType.CAMP_ON,
                        supplierInventoryPo.getSelfProvideInventory(), selfProvideDecrement,
                        reason, purchaseChildOrderPo.getPurchaseChildOrderNo(),
                        skuCategoryMap.get(rawProductItemDto.getSku()), SupplierInventoryRecordStatus.EFFECTIVE);
                supplierInventoryRecordDao.insert(selfProvideInventoryRecordPo);

                // 增加一个关联id记录
                final PurchaseChildOrderRawDeliverPo purchaseChildOrderRawDeliverPo = PurchaseConverter.rawPoToRawDeliverPo(rawProductItemDto.getSku(), purchaseChildOrderPo, selfProvideInventoryRecordPo.getCtrlCnt(),
                        "", selfProvideInventoryRecordPo.getSupplierInventoryRecordId(), RawSupplier.SUPPLIER, BooleanType.FALSE);
                purchaseChildOrderRawDeliverPoList.add(purchaseChildOrderRawDeliverPo);
            }
        }
        purchaseChildOrderRawDeliverDao.insertBatch(purchaseChildOrderRawDeliverPoList);
        if (CollectionUtils.isNotEmpty(supplierInventoryChangeBoList)) {
            supplierInventoryBaseService.inventoryChange(supplierInventoryChangeBoList);
        }
    }

    /**
     * wms 原料发货
     *
     * @param wmsDeliverBo
     * @param rawDeliverBoList
     */
    public void wmsRawDeliver(WmsDeliverBo wmsDeliverBo,
                              List<RawDeliverBo> rawDeliverBoList) {
        if (CollectionUtils.isEmpty(rawDeliverBoList)) {
            return;
        }
        final DeliveryOrderCreateMqDto deliveryOrderCreateMqDto = new DeliveryOrderCreateMqDto();
        deliveryOrderCreateMqDto.setKey(idGenerateService.getSnowflakeCode(wmsDeliverBo.getRelatedOrderNo() + "-"));
        deliveryOrderCreateMqDto.setRelatedOrderNo(wmsDeliverBo.getRelatedOrderNo());
        deliveryOrderCreateMqDto.setDeliveryType(wmsDeliverBo.getDeliveryType());
        deliveryOrderCreateMqDto.setWarehouseCode(wmsDeliverBo.getRawWarehouseCode());
        deliveryOrderCreateMqDto.setOperator(GlobalContext.getUserKey());
        deliveryOrderCreateMqDto.setOperatorName(GlobalContext.getUsername());
        deliveryOrderCreateMqDto.setProductQuality(WmsEnum.ProductQuality.GOOD);
        deliveryOrderCreateMqDto.setDispatchNow(wmsDeliverBo.getDispatchNow());
        final RawDeliverBo rawDeliverBo = rawDeliverBoList.get(0);
        deliveryOrderCreateMqDto.setParticularLocation(rawDeliverBo.getParticularLocation());

        final List<DeliveryOrderCreateMqDto.DeliveryDetail> deliveryDetailList = rawDeliverBoList.stream()
                .map(bo -> {
                    final DeliveryOrderCreateMqDto.DeliveryDetail deliveryDetail = new DeliveryOrderCreateMqDto.DeliveryDetail();
                    deliveryDetail.setSkuCode(bo.getSku());
                    deliveryDetail.setPlanDeliveryAmount(bo.getDeliveryCnt());
                    final List<RawDeliverBo.WareLocationDelivery> wareLocationDeliveryList = bo.getWareLocationDeliveryList();
                    if (CollectionUtils.isNotEmpty(wareLocationDeliveryList)) {
                        final List<DeliveryOrderCreateMqDto.WareLocationDelivery> wareLocationDeliveryList1 = wareLocationDeliveryList.stream().map(locationBo -> {
                            final DeliveryOrderCreateMqDto.WareLocationDelivery wareLocationDelivery = new DeliveryOrderCreateMqDto.WareLocationDelivery();
                            wareLocationDelivery.setDeliveryAmount(locationBo.getDeliveryAmount());
                            wareLocationDelivery.setWarehouseLocationCode(locationBo.getWarehouseLocationCode());
                            wareLocationDelivery.setBatchCode(locationBo.getBatchCode());
                            return wareLocationDelivery;
                        }).collect(Collectors.toList());
                        deliveryDetail.setWareLocationDeliveryList(wareLocationDeliveryList1);
                    }
                    return deliveryDetail;
                }).collect(Collectors.toList());
        deliveryOrderCreateMqDto.setDeliveryDetails(deliveryDetailList);
        deliveryOrderCreateMqDto.setRawDeliverMode(RawDeliverMode.SUPPLY_RAW);
        deliveryOrderCreateMqDto.setRawDeliverMode(wmsDeliverBo.getRawDeliverMode());
        deliveryOrderCreateMqDto.setPlatCode(wmsDeliverBo.getPlatform());
        deliveryOrderCreateMqDto.setConsignee(wmsDeliverBo.getConsignee());
        consistencySendMqService.execSendMq(WmsRawDeliverHandler.class, deliveryOrderCreateMqDto);
    }
}
