package com.hete.supply.scm.server.scm.sample.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.RawReceiptBizType;
import com.hete.supply.scm.api.scm.entity.enums.ReceiptOrderStatus;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.develop.dao.DevelopPamphletOrderDao;
import com.hete.supply.scm.server.scm.develop.dao.DevelopPamphletOrderRawDao;
import com.hete.supply.scm.server.scm.develop.entity.bo.PamphletRawReceiptOrderCreateBo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPamphletOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.dto.DeliveryOrderCancelEventDto;
import com.hete.supply.scm.server.scm.purchase.entity.dto.DeliveryOrderCreateResultEventDto;
import com.hete.supply.scm.server.scm.sample.dao.SampleChildOrderDao;
import com.hete.supply.scm.server.scm.sample.dao.SampleChildOrderRawDao;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderPo;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.supplier.entity.dto.RawReceiptMqDto;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseRawReceiptOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseRawReceiptOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseRawReceiptOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseRawReceiptOrderPo;
import com.hete.supply.wms.api.leave.entity.vo.ProcessDeliveryOrderVo;
import com.hete.support.api.exception.BizException;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/4/6 00:49
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class SampleRawBaseService {
    private final SampleChildOrderDao sampleChildOrderDao;
    private final IdGenerateService idGenerateService;
    private final PurchaseRawReceiptOrderDao purchaseRawReceiptOrderDao;
    private final PurchaseRawReceiptOrderItemDao purchaseRawReceiptOrderItemDao;
    private final LogBaseService logBaseService;
    private final SampleChildOrderRawDao sampleChildOrderRawDao;
    private final DevelopPamphletOrderDao developPamphletOrderDao;
    private final DevelopPamphletOrderRawDao developPamphletOrderRawDao;
    private final WmsRemoteService wmsRemoteService;

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.SCM_RAW_CREATE, key = "#rawReceiptMqDto.relatedOrderNo", waitTime = 1, leaseTime = -1)
    public void createRawReceiptOrder(RawReceiptMqDto rawReceiptMqDto) {
        log.info("WMS签出时scm原料收货单，Dto=>{}", rawReceiptMqDto);
        final String relatedOrderNo = rawReceiptMqDto.getRelatedOrderNo();
        final DevelopPamphletOrderPo developPamphletOrderPo = developPamphletOrderDao.getByDevelopPamphletOrderNo(relatedOrderNo);
        if (developPamphletOrderPo != null) {
            // 开发样品单逻辑
            this.createPamphletRawReceiptOrder(rawReceiptMqDto, developPamphletOrderPo);
        } else {
            throw new BizException("原料wms原料发货单签出查询不到对应的信息！");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.SCM_RAW_CREATE, key = "#deliveryOrderCreateResultEventDto.relatedOrderNo", waitTime = 1, leaseTime = -1)
    public void createRawReceiptOrder(DeliveryOrderCreateResultEventDto deliveryOrderCreateResultEventDto) {
        log.info("WMS原料出库单的创建，Dto=>{}", deliveryOrderCreateResultEventDto);
        final String relatedOrderNo = deliveryOrderCreateResultEventDto.getRelatedOrderNo();
        final DevelopPamphletOrderPo developPamphletOrderPo = developPamphletOrderDao.getByDevelopPamphletOrderNo(relatedOrderNo);
        if (developPamphletOrderPo != null) {
            // 创建原料收货单
            PamphletRawReceiptOrderCreateBo pamphletRawReceiptOrderCreateBo = new PamphletRawReceiptOrderCreateBo();
            pamphletRawReceiptOrderCreateBo.setPurchaseRawDeliverOrderNo(deliveryOrderCreateResultEventDto.getDeliveryOrderNo());
            pamphletRawReceiptOrderCreateBo.setDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
            pamphletRawReceiptOrderCreateBo.setSupplierCode(developPamphletOrderPo.getSupplierCode());
            pamphletRawReceiptOrderCreateBo.setSupplierName(developPamphletOrderPo.getSupplierName());
            pamphletRawReceiptOrderCreateBo.setRawReceiptBizType(RawReceiptBizType.DEVELOP);
            this.createPamphletRawReceiptOrderPo(pamphletRawReceiptOrderCreateBo);
        } else {
            throw new BizException("WMS原料出库单的创建查询不到对应版单的信息！");
        }
    }

    /**
     * 不在进行维护
     */
    @Deprecated
    public void createPurchaseRawReceiptOrder(RawReceiptMqDto rawReceiptMqDto, SampleChildOrderPo sampleChildOrderPo) {
        final String relatedOrderNo = rawReceiptMqDto.getRelatedOrderNo();
        final List<RawReceiptMqDto.RawReceiptItemDto> rawReceiptItemList = rawReceiptMqDto.getRawReceiptItemList();
        final int deliverCnt = rawReceiptItemList.stream()
                .mapToInt(RawReceiptMqDto.RawReceiptItemDto::getDeliverCnt)
                .sum();

        //处理重复推送导致多次创建的问题
        List<PurchaseRawReceiptOrderPo> purchaseRawReceiptOrderPoList = purchaseRawReceiptOrderDao.getListByDeliverNo(rawReceiptMqDto.getPurchaseRawDeliverOrderNo());
        if (CollectionUtils.isNotEmpty(purchaseRawReceiptOrderPoList)) {
            for (PurchaseRawReceiptOrderPo purchaseRawReceiptOrderPo : purchaseRawReceiptOrderPoList) {
                purchaseRawReceiptOrderPo.setLogistics(rawReceiptMqDto.getLogistics());
                purchaseRawReceiptOrderPo.setTrackingNo(rawReceiptMqDto.getTrackingNo());
            }
            purchaseRawReceiptOrderDao.updateBatchById(purchaseRawReceiptOrderPoList);
            return;
        }

        // 创建原料收货单
        final String purchaseRawReceiptOrderNo = idGenerateService.getConfuseCode(ScmConstant.PURCHASE_RAW_ORDER_NO_PREFIX, ConfuseLength.L_10);
        final PurchaseRawReceiptOrderPo purchaseRawReceiptOrderPo = new PurchaseRawReceiptOrderPo();
        purchaseRawReceiptOrderPo.setPurchaseRawReceiptOrderNo(purchaseRawReceiptOrderNo);
        purchaseRawReceiptOrderPo.setReceiptOrderStatus(ReceiptOrderStatus.WAIT_RECEIVE);
        purchaseRawReceiptOrderPo.setSupplierCode(sampleChildOrderPo.getSupplierCode());
        purchaseRawReceiptOrderPo.setSupplierName(sampleChildOrderPo.getSupplierName());
        purchaseRawReceiptOrderPo.setDeliverCnt(deliverCnt);
        purchaseRawReceiptOrderPo.setDeliverTime(LocalDateTime.now());
        purchaseRawReceiptOrderPo.setWarehouseCode(sampleChildOrderPo.getRawWarehouseCode());
        purchaseRawReceiptOrderPo.setWarehouseName(sampleChildOrderPo.getRawWarehouseName());
        purchaseRawReceiptOrderPo.setSampleChildOrderNo(relatedOrderNo);
        purchaseRawReceiptOrderPo.setLogistics(rawReceiptMqDto.getLogistics());
        purchaseRawReceiptOrderPo.setTrackingNo(rawReceiptMqDto.getTrackingNo());
        purchaseRawReceiptOrderPo.setPurchaseRawDeliverOrderNo(rawReceiptMqDto.getPurchaseRawDeliverOrderNo());
        purchaseRawReceiptOrderPo.setRawReceiptBizType(RawReceiptBizType.SAMPLE);
        purchaseRawReceiptOrderDao.insert(purchaseRawReceiptOrderPo);

        // 创建原料收货单子项
        final List<PurchaseRawReceiptOrderItemPo> purchaseRawReceiptOrderItemPoList = rawReceiptItemList.stream().map(item -> {
            final PurchaseRawReceiptOrderItemPo purchaseRawReceiptOrderItemPo = new PurchaseRawReceiptOrderItemPo();
            purchaseRawReceiptOrderItemPo.setPurchaseRawReceiptOrderNo(purchaseRawReceiptOrderNo);
            purchaseRawReceiptOrderItemPo.setSkuBatchCode(item.getSkuBatchCode());
            purchaseRawReceiptOrderItemPo.setSku(item.getSkuCode());
            purchaseRawReceiptOrderItemPo.setDeliverCnt(item.getDeliverCnt());
            return purchaseRawReceiptOrderItemPo;
        }).collect(Collectors.toList());
        purchaseRawReceiptOrderItemDao.insertBatch(purchaseRawReceiptOrderItemPoList);
        // 日志
        logBaseService.simpleLog(LogBizModule.SUPPLIER_RAW_RECEIPT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseRawReceiptOrderPo.getPurchaseRawReceiptOrderNo(), ReceiptOrderStatus.WAIT_RECEIVE.getRemark(), Collections.emptyList());

    }

    public void createPamphletRawReceiptOrder(RawReceiptMqDto rawReceiptMqDto, DevelopPamphletOrderPo developPamphletOrderPo) {
        log.info("WMS签出时scm原料收货单处理开发单的版单，Dto=>{}，developPamphletOrderPo=>{}", rawReceiptMqDto, developPamphletOrderPo);
        //处理重复推送导致多次创建的问题
        List<PurchaseRawReceiptOrderPo> purchaseRawReceiptOrderPoList = purchaseRawReceiptOrderDao.getListByDeliverNo(rawReceiptMqDto.getPurchaseRawDeliverOrderNo());
        if (CollectionUtils.isNotEmpty(purchaseRawReceiptOrderPoList)) {
            for (PurchaseRawReceiptOrderPo purchaseRawReceiptOrderPo : purchaseRawReceiptOrderPoList) {
                purchaseRawReceiptOrderPo.setLogistics(rawReceiptMqDto.getLogistics());
                purchaseRawReceiptOrderPo.setTrackingNo(rawReceiptMqDto.getTrackingNo());
                if (ReceiptOrderStatus.WAIT_DELIVER.equals(purchaseRawReceiptOrderPo.getReceiptOrderStatus())) {
                    purchaseRawReceiptOrderPo.setReceiptOrderStatus(ReceiptOrderStatus.WAIT_RECEIVE);
                    // 日志
                    logBaseService.simpleLog(LogBizModule.SUPPLIER_RAW_RECEIPT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                            purchaseRawReceiptOrderPo.getPurchaseRawReceiptOrderNo(), ReceiptOrderStatus.WAIT_RECEIVE.getRemark(), Collections.emptyList());
                }
            }
            purchaseRawReceiptOrderDao.updateBatchById(purchaseRawReceiptOrderPoList);
            return;
        }

        // 创建原料收货单
        PamphletRawReceiptOrderCreateBo pamphletRawReceiptOrderCreateBo = new PamphletRawReceiptOrderCreateBo();
        pamphletRawReceiptOrderCreateBo.setPurchaseRawDeliverOrderNo(rawReceiptMqDto.getPurchaseRawDeliverOrderNo());
        pamphletRawReceiptOrderCreateBo.setDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
        pamphletRawReceiptOrderCreateBo.setSupplierCode(developPamphletOrderPo.getSupplierCode());
        pamphletRawReceiptOrderCreateBo.setSupplierName(developPamphletOrderPo.getSupplierName());
        pamphletRawReceiptOrderCreateBo.setTrackingNo(rawReceiptMqDto.getTrackingNo());
        pamphletRawReceiptOrderCreateBo.setLogistics(rawReceiptMqDto.getLogistics());
        pamphletRawReceiptOrderCreateBo.setRawReceiptBizType(RawReceiptBizType.DEVELOP);
        this.createPamphletRawReceiptOrderPo(pamphletRawReceiptOrderCreateBo);

    }

    /**
     * 样品原料创建
     *
     * @param pamphletRawReceiptOrderCreateBo:
     * @return void
     * @author ChenWenLong
     * @date 2024/11/22 17:10
     */
    public void createPamphletRawReceiptOrderPo(PamphletRawReceiptOrderCreateBo pamphletRawReceiptOrderCreateBo) {
        // 查询wms出库单号
        final List<ProcessDeliveryOrderVo> deliverOrderVoList = wmsRemoteService.getDeliveryOrderByDeliverNo(Collections.singletonList(pamphletRawReceiptOrderCreateBo.getPurchaseRawDeliverOrderNo()));
        if (CollectionUtils.isEmpty(deliverOrderVoList)) {
            throw new BizException("wms出库单:{}不存在，数据异常请联系系统管理员", pamphletRawReceiptOrderCreateBo.getPurchaseRawDeliverOrderNo());
        }
        // 防止重复创建
        List<PurchaseRawReceiptOrderPo> purchaseRawReceiptOrderPoList = purchaseRawReceiptOrderDao.getListByDeliverNo(pamphletRawReceiptOrderCreateBo.getPurchaseRawDeliverOrderNo());
        if (CollectionUtils.isNotEmpty(purchaseRawReceiptOrderPoList)) {
            log.warn("wms出库单:{}已存在，禁止重复创建！", pamphletRawReceiptOrderCreateBo.getPurchaseRawDeliverOrderNo());
            return;
        }
        final ProcessDeliveryOrderVo processDeliveryOrderVo = deliverOrderVoList.get(0);
        List<ProcessDeliveryOrderVo.DeliveryProduct> productList = processDeliveryOrderVo.getProducts();
        final int deliverCnt = Optional.ofNullable(productList)
                .orElse(new ArrayList<>())
                .stream()
                .mapToInt(ProcessDeliveryOrderVo.DeliveryProduct::getAmount)
                .sum();

        // 创建原料收货单
        final String purchaseRawReceiptOrderNo = idGenerateService.getConfuseCode(ScmConstant.PURCHASE_RAW_ORDER_NO_PREFIX, TimeType.CN_DAY_YYYY, ConfuseLength.L_4);
        final PurchaseRawReceiptOrderPo purchaseRawReceiptOrderPo = new PurchaseRawReceiptOrderPo();
        purchaseRawReceiptOrderPo.setPurchaseRawReceiptOrderNo(purchaseRawReceiptOrderNo);
        purchaseRawReceiptOrderPo.setDevelopPamphletOrderNo(pamphletRawReceiptOrderCreateBo.getDevelopPamphletOrderNo());
        purchaseRawReceiptOrderPo.setReceiptOrderStatus(ReceiptOrderStatus.WAIT_DELIVER);
        purchaseRawReceiptOrderPo.setSupplierCode(pamphletRawReceiptOrderCreateBo.getSupplierCode());
        purchaseRawReceiptOrderPo.setSupplierName(pamphletRawReceiptOrderCreateBo.getSupplierName());
        purchaseRawReceiptOrderPo.setDeliverCnt(deliverCnt);
        purchaseRawReceiptOrderPo.setDeliverTime(LocalDateTime.now());
        purchaseRawReceiptOrderPo.setWarehouseCode(processDeliveryOrderVo.getWarehouseCode());
        purchaseRawReceiptOrderPo.setWarehouseName(processDeliveryOrderVo.getWarehouseName());
        purchaseRawReceiptOrderPo.setLogistics(pamphletRawReceiptOrderCreateBo.getLogistics());
        purchaseRawReceiptOrderPo.setTrackingNo(pamphletRawReceiptOrderCreateBo.getTrackingNo());
        purchaseRawReceiptOrderPo.setPurchaseRawDeliverOrderNo(processDeliveryOrderVo.getDeliveryOrderNo());
        purchaseRawReceiptOrderPo.setRawReceiptBizType(pamphletRawReceiptOrderCreateBo.getRawReceiptBizType());
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
                purchaseRawReceiptOrderPo.getPurchaseRawReceiptOrderNo(), ReceiptOrderStatus.WAIT_DELIVER.getRemark(), Collections.emptyList());
    }

    /**
     * wms取消出库单时取消原料收货单
     *
     * @param message:
     * @return void
     * @author ChenWenLong
     * @date 2024/11/25 15:57
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelRawReceiptOrder(DeliveryOrderCancelEventDto message) {
        log.info("wms取消出库单:{}，同时取消原料收货单，Dto=>{}", message.getDeliveryOrderNo(), message);
        PurchaseRawReceiptOrderPo purchaseRawReceiptOrderPo = purchaseRawReceiptOrderDao.getOneByDeliverNo(message.getDeliveryOrderNo());
        if (null == purchaseRawReceiptOrderPo) {
            throw new BizException("出库单号：{}不存在对应版单的原料收货单，取消失败！", message.getDeliveryOrderNo());
        }
        purchaseRawReceiptOrderPo.setReceiptOrderStatus(ReceiptOrderStatus.CANCEL);
        purchaseRawReceiptOrderDao.updateByIdVersion(purchaseRawReceiptOrderPo);
        // 日志
        logBaseService.simpleLog(LogBizModule.SUPPLIER_RAW_RECEIPT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseRawReceiptOrderPo.getPurchaseRawReceiptOrderNo(), ReceiptOrderStatus.CANCEL.getRemark(), Collections.emptyList());
    }
}
