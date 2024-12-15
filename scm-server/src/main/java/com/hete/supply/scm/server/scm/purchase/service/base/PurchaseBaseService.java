package com.hete.supply.scm.server.scm.purchase.service.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.plm.api.goods.entity.vo.PlmGoodsDetailVo;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseChildNoListDto;
import com.hete.supply.scm.api.scm.entity.dto.*;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseSkuCntVo;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseVo;
import com.hete.supply.scm.api.scm.entity.vo.SkuUndeliveredCntVo;
import com.hete.supply.scm.api.scm.entity.vo.SupplierSkuBatchCodeVo;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.common.util.StringUtil;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.entity.bo.RawDeliverBo;
import com.hete.supply.scm.server.scm.entity.dto.RawProductItemDto;
import com.hete.supply.scm.server.scm.entity.vo.SkuBomListVo;
import com.hete.supply.scm.server.scm.enums.LogVersionValueType;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderCancelEventDto;
import com.hete.supply.scm.server.scm.process.handler.WmsProcessCancelHandler;
import com.hete.supply.scm.server.scm.purchase.converter.PurchaseChildConverter;
import com.hete.supply.scm.server.scm.purchase.converter.PurchaseConverter;
import com.hete.supply.scm.server.scm.purchase.dao.*;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseChildOrderExportBo;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseSearchBo;
import com.hete.supply.scm.server.scm.purchase.entity.bo.SkuStockBo;
import com.hete.supply.scm.server.scm.purchase.entity.bo.WmsDeliverBo;
import com.hete.supply.scm.server.scm.purchase.entity.dto.*;
import com.hete.supply.scm.server.scm.purchase.entity.po.*;
import com.hete.supply.scm.server.scm.purchase.entity.vo.*;
import com.hete.supply.scm.server.scm.purchase.enums.PurchaseReturnPlacedCnt;
import com.hete.supply.scm.server.scm.purchase.handler.PurchaseChangeHandler;
import com.hete.supply.scm.server.scm.purchase.service.ref.PurchaseRawRefService;
import com.hete.supply.scm.server.scm.qc.builder.QcOrderBuilder;
import com.hete.supply.scm.server.scm.qc.config.QcConfig;
import com.hete.supply.scm.server.scm.qc.dao.QcDetailDao;
import com.hete.supply.scm.server.scm.qc.dao.QcOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.bo.PurchaseCompleteQcBo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.supply.scm.server.scm.qc.service.base.AbstractQcOrderCreator;
import com.hete.supply.scm.server.scm.qc.service.base.PurchaseCompleteQcCreator;
import com.hete.supply.scm.server.scm.qc.service.base.QcOrderLogService;
import com.hete.supply.scm.server.scm.qc.service.ref.QcOrderRefService;
import com.hete.supply.scm.server.scm.sample.dao.SampleChildOrderDao;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderPo;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.scm.service.base.ProduceDataBaseService;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierProductCompareDao;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupOpCapacityBo;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierTimelinessDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierUrgentDto;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierWarehousePo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierTimelinessVo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierUrgentVo;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierUrgentStatus;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierBaseService;
import com.hete.supply.scm.server.scm.supplier.service.ref.SupplierCapacityRefService;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseChildBatchReceiveDto;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import com.hete.supply.scm.server.supplier.purchase.service.ref.PurchaseDeliverRefService;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.interna.entity.dto.SkuInstockInventoryQueryDto;
import com.hete.supply.wms.api.interna.entity.vo.SkuInventoryVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/5 17:42
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class PurchaseBaseService {
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final PurchaseReturnOrderDao purchaseReturnOrderDao;
    private final PurchaseReturnOrderItemDao purchaseReturnOrderItemDao;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;
    private final IdGenerateService idGenerateService;
    private final PurchaseParentOrderDao purchaseParentOrderDao;
    private final PurchaseParentOrderItemDao purchaseParentOrderItemDao;
    private final PurchaseDeliverOrderDao purchaseDeliverOrderDao;
    private final PurchaseChildOrderRawDao purchaseChildOrderRawDao;
    private final PurchaseParentOrderChangeDao purchaseParentOrderChangeDao;
    private final PurchaseChildOrderChangeDao purchaseChildOrderChangeDao;
    private final LogBaseService logBaseService;
    private final PurchaseRawRefService purchaseRawRefService;
    private final PurchaseModifyOrderDao purchaseModifyOrderDao;
    private final PurchaseModifyOrderItemDao purchaseModifyOrderItemDao;
    private final PurchaseDeliverRefService purchaseDeliverRefService;
    private final ConsistencySendMqService consistencySendMqService;
    private final PlmRemoteService plmRemoteService;
    private final SupplierProductCompareDao supplierProductCompareDao;
    private final WmsRemoteService wmsRemoteService;
    private final PurchaseDeliverOrderItemDao purchaseDeliverOrderItemDao;
    private final SampleChildOrderDao sampleChildOrderDao;
    private final RawPurchaseParentRelateDao rawPurchaseParentRelateDao;
    private final ProduceDataBaseService produceDataBaseService;
    private final QcOrderDao qcOrderDao;
    private final QcDetailDao qcDetailDao;
    private final QcConfig qcConfig;
    private final QcOrderLogService qcOrderLogService;
    private final QcOrderRefService qcOrderRefService;
    private final SupplierDao supplierDao;
    private final SupplierCapacityRefService supplierCapacityRefService;
    private final PurchaseRawBaseService purchaseRawBaseService;
    private final SupplierBaseService supplierBaseService;
    private final PurchaseChildOrderRawDeliverDao purchaseChildOrderRawDeliverDao;


    /**
     * 供应商基础消耗时间（天数）
     */
    private final static Integer BASE_SUPPLIER_CONSUME = 2;


    /**
     * 根据采购母单号获取最新的子单号
     *
     * @param purchaseParentOrderNo
     * @param dto
     * @return
     */
    public void getLatestPurchaseChildNo(String purchaseParentOrderNo, PurchaseSplitNewDto dto) {
        PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getLatestPurchaseChild(purchaseParentOrderNo);
        int indexNo = 0;
        if (null != purchaseChildOrderPo) {
            final String purchaseChildOrderNo = purchaseChildOrderPo.getPurchaseChildOrderNo();
            final String childOrderNoIndex = purchaseChildOrderNo.split("-")[1];
            indexNo = Integer.parseInt(childOrderNoIndex);
        }
        final List<PurchaseSplitItemDto> sortedList = dto.getPurchaseSplitItemList().stream()
                .sorted(Comparator.comparing(PurchaseSplitItemDto::getExpectedOnShelvesDate))
                .collect(Collectors.toList());
        for (PurchaseSplitItemDto itemDto : sortedList) {
            itemDto.setPurchaseChildOrderNo(purchaseParentOrderNo + "-" + StringUtil.toTwoDigitFormat(++indexNo));
        }
    }

    /**
     * 根据采购母单号获取最新的子单号
     *
     * @param purchaseParentOrderNo
     * @return
     */
    public String getLatestPurchaseChildNo(String purchaseParentOrderNo) {
        PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getLatestPurchaseChild(purchaseParentOrderNo);
        int indexNo = 0;
        if (null != purchaseChildOrderPo) {
            final String purchaseChildOrderNo = purchaseChildOrderPo.getPurchaseChildOrderNo();
            final String childOrderNoIndex = purchaseChildOrderNo.split("-")[1];
            indexNo = Integer.parseInt(childOrderNoIndex);
        }

        return purchaseParentOrderNo + "-" + StringUtil.toTwoDigitFormat(++indexNo);
    }

    /**
     * 采购子单通过模糊单号和类型查询采购子单
     *
     * @author ChenWenLong
     * @date 2022/11/10 11:12
     */
    public List<PurchaseChildOrderPo> getListLikeByChildNoAndType(String purchaseChildOrderNo,
                                                                  PurchaseBizType purchaseBizType,
                                                                  String supplierCode,
                                                                  List<PurchaseOrderStatus> purchaseOrderStatusNotList) {
        return purchaseChildOrderDao.getListLikeByChildNoAndType(purchaseChildOrderNo, purchaseBizType, supplierCode, purchaseOrderStatusNotList);
    }

    /**
     * 通过模糊单号查询退货单
     *
     * @author ChenWenLong
     * @date 2022/11/10 11:12
     */
    public List<PurchaseReturnOrderPo> getListLikeByPurchaseReturnOrderNo(String purchaseReturnOrderNo, String supplierCode, List<ReceiptOrderStatus> receiptOrderStatusNotList) {
        return purchaseReturnOrderDao.getListLikeByPurchaseReturnOrderNo(purchaseReturnOrderNo, supplierCode, receiptOrderStatusNotList);
    }

    /**
     * 采购子单通过模糊子单号查询
     *
     * @author ChenWenLong
     * @date 2022/11/10 11:12
     */
    public List<PurchaseChildOrderPo> getChildListLikeByChildNo(String purchaseChildOrderNo) {
        return purchaseChildOrderDao.getListLikeByChildNo(purchaseChildOrderNo);
    }

    /**
     * 采购详情通过模糊子单号查询
     *
     * @author ChenWenLong
     * @date 2022/11/10 11:12
     */
    public List<PurchaseChildOrderItemPo> getChildItemListLikeByChildNo(String purchaseChildOrderNo) {
        return purchaseChildOrderItemDao.getListByChildNo(purchaseChildOrderNo);
    }

    /**
     * 退货单通过单号查询
     *
     * @author ChenWenLong
     * @date 2022/11/10 11:42
     */
    public List<PurchaseReturnOrderItemPo> getListByPurchaseReturnOrderNo(String purchaseReturnOrderNo) {
        return purchaseReturnOrderItemDao.getListByReturnOrderNo(purchaseReturnOrderNo);
    }


    public PurchaseParentOrderStatus getEarliestStatus(String purchaseParentOrderNo, List<PurchaseOrderStatus> newOrderStatusList) {
        final List<PurchaseChildOrderPo> childOrderPoList = purchaseChildOrderDao.getListByParentNo(purchaseParentOrderNo);
        if (CollectionUtils.isEmpty(childOrderPoList)) {
            return null;
        }
        final List<PurchaseOrderStatus> statusList = childOrderPoList.stream()
                .map(PurchaseChildOrderPo::getPurchaseOrderStatus)
                .collect(Collectors.toList());
        statusList.addAll(newOrderStatusList);
        final PurchaseOrderStatus earliestStatus = PurchaseOrderStatus.getEarliestStatus(statusList);

        return PurchaseParentOrderStatus.convertPurchaseStatus(earliestStatus);
    }

    public List<PurchaseParentOrderItemPo> createDtoToPoItemList(List<PurchaseProductDemandItemDto> purchaseProductDemandItemList,
                                                                 String purchaseParentOrderNo) {
        return purchaseProductDemandItemList.stream()
                .map(item -> {
                    PurchaseParentOrderItemPo purchaseParentOrderItemPo = new PurchaseParentOrderItemPo();
                    final long snowflakeId = idGenerateService.getSnowflakeId();
                    purchaseParentOrderItemPo.setPurchaseParentOrderItemId(snowflakeId);
                    purchaseParentOrderItemPo.setPurchaseParentOrderNo(purchaseParentOrderNo);
                    purchaseParentOrderItemPo.setSku(item.getSku());
                    purchaseParentOrderItemPo.setVariantProperties(item.getVariantProperties());
                    purchaseParentOrderItemPo.setPurchaseCnt(item.getPurchaseCnt());
                    item.setPurchaseParentOrderItemId(snowflakeId);

                    return purchaseParentOrderItemPo;
                }).collect(Collectors.toList());
    }

    /**
     * 通过供应商和时间查询数据列表
     *
     * @author ChenWenLong
     * @date 2022/11/21 20:02
     */
    public List<PurchaseChildOrderChangeSearchVo> purchaseChildOrderChangeSearch(List<String> supplierCodeList, List<PurchaseBizType> purchaseBizTypeList,
                                                                                 LocalDateTime warehousingTime, LocalDateTime warehousingTimeStart,
                                                                                 LocalDateTime warehousingTimeEnd, PurchaseOrderStatus purchaseOrderStatus) {
        return purchaseChildOrderDao.purchaseChildOrderChangeSearch(supplierCodeList, purchaseBizTypeList,
                warehousingTime, warehousingTimeStart,
                warehousingTimeEnd, purchaseOrderStatus);
    }

    public PurchaseSearchBo getItemParentNoList(PurchaseSearchNewDto dto) {
        List<String> itemParentNoList = new ArrayList<>();
        final List<String> notInItemParentNoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dto.getSkuList())) {
            List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList = purchaseParentOrderItemDao.getListBySkuList(dto.getSkuList());
            if (CollectionUtils.isEmpty(purchaseParentOrderItemPoList)) {
                return null;
            }

            itemParentNoList.addAll(purchaseParentOrderItemPoList.stream()
                    .map(PurchaseParentOrderItemPo::getPurchaseParentOrderNo)
                    .distinct()
                    .collect(Collectors.toList()));

            if (CollectionUtils.isEmpty(itemParentNoList)) {
                return null;
            }
        }
        if (dto.getIsOverdue() != null) {
            final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByIsOverdue(BooleanType.TRUE);
            if (CollectionUtils.isEmpty(purchaseChildOrderPoList)) {
                return null;
            }
            if (BooleanType.TRUE.equals(dto.getIsOverdue())) {
                final List<String> purchaseParentOrderNoList = purchaseChildOrderPoList.stream()
                        .map(PurchaseChildOrderPo::getPurchaseParentOrderNo)
                        .distinct()
                        .collect(Collectors.toList());

                if (CollectionUtils.isEmpty(itemParentNoList)) {
                    itemParentNoList.addAll(purchaseParentOrderNoList);
                } else {
                    itemParentNoList = itemParentNoList.stream()
                            .filter(purchaseParentOrderNoList::contains)
                            .collect(Collectors.toList());
                }
                if (CollectionUtils.isEmpty(itemParentNoList)) {
                    return null;
                }
            } else if (BooleanType.FALSE.equals(dto.getIsOverdue())) {
                notInItemParentNoList.addAll(purchaseChildOrderPoList.stream()
                        .map(PurchaseChildOrderPo::getPurchaseParentOrderNo)
                        .distinct()
                        .collect(Collectors.toList()));
            }
        }
        if (dto.getPurchaseOrderType() != null) {
            final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByPurchaseOrderType(PurchaseOrderType.FIRST_ORDER);
            if (CollectionUtils.isEmpty(purchaseChildOrderPoList)) {
                return null;
            }
            if (PurchaseOrderType.FIRST_ORDER.equals(dto.getPurchaseOrderType())) {
                final List<String> purchaseParentOrderNoList = purchaseChildOrderPoList.stream()
                        .map(PurchaseChildOrderPo::getPurchaseParentOrderNo)
                        .distinct()
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(itemParentNoList)) {
                    itemParentNoList.addAll(purchaseParentOrderNoList);
                } else {
                    itemParentNoList = itemParentNoList.stream()
                            .filter(purchaseParentOrderNoList::contains)
                            .collect(Collectors.toList());
                }
                if (CollectionUtils.isEmpty(itemParentNoList)) {
                    return null;
                }
            } else {
                notInItemParentNoList.addAll(purchaseChildOrderPoList.stream()
                        .map(PurchaseChildOrderPo::getPurchaseParentOrderNo)
                        .distinct()
                        .collect(Collectors.toList()));
            }
        }
        if (dto.getIsUrgentOrder() != null) {
            final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByIsUrgentOrder(BooleanType.TRUE);
            if (CollectionUtils.isEmpty(purchaseChildOrderPoList)) {
                return null;
            }

            if (BooleanType.TRUE.equals(dto.getIsUrgentOrder())) {
                final List<String> purchaseParentOrderNoList = purchaseChildOrderPoList.stream()
                        .map(PurchaseChildOrderPo::getPurchaseParentOrderNo)
                        .distinct()
                        .collect(Collectors.toList());

                if (CollectionUtils.isEmpty(itemParentNoList)) {
                    itemParentNoList.addAll(purchaseParentOrderNoList);
                } else {
                    itemParentNoList = itemParentNoList.stream()
                            .filter(purchaseParentOrderNoList::contains)
                            .collect(Collectors.toList());
                }
                if (CollectionUtils.isEmpty(itemParentNoList)) {
                    return null;
                }
            } else if (BooleanType.FALSE.equals(dto.getIsUrgentOrder())) {
                notInItemParentNoList.addAll(purchaseChildOrderPoList.stream()
                        .map(PurchaseChildOrderPo::getPurchaseParentOrderNo)
                        .distinct()
                        .collect(Collectors.toList()));
            }
        }

        return PurchaseSearchBo.builder()
                .itemParentNoList(itemParentNoList)
                .notInItemParentNoList(notInItemParentNoList)
                .build();
    }

    public List<String> getItemChildNoList(PurchaseProductSearchDto dto) {
        List<String> itemChildNoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dto.getSkuList())) {
            List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListBySku(dto.getSkuList());
            if (CollectionUtils.isEmpty(purchaseChildOrderItemPoList)) {
                return null;
            }

            List<String> purchaseChildOrderNoList = purchaseChildOrderItemPoList.stream().map(PurchaseChildOrderItemPo::getPurchaseChildOrderNo).distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(itemChildNoList)) {
                itemChildNoList.addAll(purchaseChildOrderNoList);
            } else {
                itemChildNoList.retainAll(purchaseChildOrderNoList);
            }
        }
        if (CollectionUtils.isNotEmpty(dto.getSkuBatchCodeList())) {
            List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListBySkuBatchCode(dto.getSkuBatchCodeList());
            if (CollectionUtils.isEmpty(purchaseChildOrderItemPoList)) {
                return null;
            }
            List<String> purchaseChildOrderNoList = purchaseChildOrderItemPoList.stream().map(PurchaseChildOrderItemPo::getPurchaseChildOrderNo).distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(itemChildNoList)) {
                itemChildNoList.addAll(purchaseChildOrderNoList);
            } else {
                itemChildNoList.retainAll(purchaseChildOrderNoList);
            }

        }
        if (StringUtils.isNotBlank(dto.getSkuBatchCode())) {
            List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByLikeSkuBatchCode(dto.getSkuBatchCode());
            if (CollectionUtils.isEmpty(purchaseChildOrderItemPoList)) {
                return null;
            }
            List<String> purchaseChildOrderNoList = purchaseChildOrderItemPoList.stream().map(PurchaseChildOrderItemPo::getPurchaseChildOrderNo).distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(itemChildNoList)) {
                itemChildNoList.addAll(purchaseChildOrderNoList);
            } else {
                itemChildNoList.retainAll(purchaseChildOrderNoList);
            }
        }

        return itemChildNoList;
    }

    public List<PurchaseMsgParentVo> getPurchaseParentList(List<String> spuList) {
        final List<PurchaseParentOrderPo> purchaseParentOrderPoList = purchaseParentOrderDao.getFirstOrderListBySpuList(spuList);
        if (CollectionUtils.isEmpty(purchaseParentOrderPoList)) {
            return Collections.emptyList();
        }

        final Set<String> purchaseParentOrderNoSet = purchaseParentOrderPoList.stream()
                .map(PurchaseParentOrderPo::getPurchaseParentOrderNo)
                .collect(Collectors.toSet());

        final Map<String, List<PurchaseParentOrderItemPo>> itemMap = purchaseParentOrderItemDao.getMapByNoList(purchaseParentOrderNoSet);

        return purchaseParentOrderPoList.stream()
                .map(po -> {
                    final PurchaseMsgParentVo purchaseMsgParentVo = new PurchaseMsgParentVo();
                    purchaseMsgParentVo.setPurchaseParentOrderNo(po.getPurchaseParentOrderNo());
                    purchaseMsgParentVo.setPurchaseParentOrderStatus(po.getPurchaseParentOrderStatus());
                    purchaseMsgParentVo.setPurchaseTotal(po.getPurchaseTotal());
                    purchaseMsgParentVo.setWarehouseCode(po.getWarehouseCode());
                    purchaseMsgParentVo.setWarehouseName(po.getWarehouseName());

                    final List<PurchaseParentOrderItemPo> itemPoList = itemMap.get(po.getPurchaseParentOrderNo());
                    final List<PurchaseMsgParentVo.PurchaseSkuCntVo> itemVoList = Optional.ofNullable(itemPoList)
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(itemPo -> {
                                final PurchaseMsgParentVo.PurchaseSkuCntVo purchaseSkuCntVo = new PurchaseMsgParentVo.PurchaseSkuCntVo();
                                purchaseSkuCntVo.setSku(itemPo.getSku());
                                purchaseSkuCntVo.setPurchaseCnt(itemPo.getPurchaseCnt());
                                return purchaseSkuCntVo;
                            }).collect(Collectors.toList());

                    purchaseMsgParentVo.setPurchaseSkuCntList(itemVoList);
                    return purchaseMsgParentVo;
                }).collect(Collectors.toList());
    }

    /**
     * 通过编号批量更新状态
     *
     * @author ChenWenLong
     * @date 2023/1/14 15:38
     */
    public void updateBatchPurchaseChildOrderNo(List<String> purchaseChildOrderNos, PurchaseOrderStatus purchaseOrderStatus) {
        purchaseChildOrderDao.updateBatchPurchaseChildOrderNo(purchaseChildOrderNos, purchaseOrderStatus);
    }


    /**
     * 获取采购未交数量
     *
     * @param dto
     * @return
     */
    public List<PurchaseSkuCntVo> getPurchaseUndeliveredCnt(SkuCodeListDto dto) {
        final List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList = purchaseParentOrderItemDao.getListBySkuList(dto.getSkuCodeList());
        if (CollectionUtils.isEmpty(purchaseParentOrderItemPoList)) {
            return Collections.emptyList();
        }

        final List<String> purchaseParentNoList = purchaseParentOrderItemPoList.stream()
                .map(PurchaseParentOrderItemPo::getPurchaseParentOrderNo)
                .collect(Collectors.toList());

        final List<PurchaseParentOrderPo> purchaseParentOrderPoList = purchaseParentOrderDao.getListByNoListAndRidStatus(purchaseParentNoList,
                PurchaseParentOrderStatus.getUndeliveredRidStatusList());
        if (CollectionUtils.isEmpty(purchaseParentOrderPoList)) {
            return Collections.emptyList();
        }
        final List<String> filterPurchaseParentNoList = purchaseParentOrderPoList.stream()
                .map(PurchaseParentOrderPo::getPurchaseParentOrderNo)
                .collect(Collectors.toList());


        final Map<String, Integer> skuUndeliveredCntMap = purchaseParentOrderItemPoList.stream()
                .filter(itemPo -> filterPurchaseParentNoList.contains(itemPo.getPurchaseParentOrderNo()))
                .collect(Collectors.groupingBy(PurchaseParentOrderItemPo::getSku, Collectors.summingInt(PurchaseParentOrderItemPo::getUndeliveredCnt)));

        return Optional.of(skuUndeliveredCntMap.keySet())
                .orElse(Collections.emptySet())
                .stream()
                .map(sku -> {
                    final PurchaseSkuCntVo purchaseSkuCntVo = new PurchaseSkuCntVo();
                    purchaseSkuCntVo.setSkuCode(sku);
                    purchaseSkuCntVo.setSkuCnt(skuUndeliveredCntMap.get(sku));
                    return purchaseSkuCntVo;
                }).collect(Collectors.toList());
    }

    /**
     * 获取采购在途数量
     *
     * @param dto
     * @return
     */
    public List<PurchaseSkuCntVo> getPurchaseInTransitCnt(SkuCodeListDto dto) {
        final List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList = purchaseDeliverOrderItemDao.getListBySkuList(dto.getSkuCodeList());
        if (CollectionUtils.isEmpty(purchaseDeliverOrderItemPoList)) {
            return Collections.emptyList();
        }
        final List<String> purchaseDeliverOrderNoList = purchaseDeliverOrderItemPoList.stream()
                .map(PurchaseDeliverOrderItemPo::getPurchaseDeliverOrderNo)
                .distinct()
                .collect(Collectors.toList());
        final List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByNoList(purchaseDeliverOrderNoList);
        final Map<String, DeliverOrderStatus> purchaseDeliverNoStatusMap = purchaseDeliverOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo,
                        PurchaseDeliverOrderPo::getDeliverOrderStatus));

        final Map<String, Integer> skuDeliverCntMap = purchaseDeliverOrderItemPoList.stream()
                .filter(itemPo -> {
                    final DeliverOrderStatus deliverOrderStatus = purchaseDeliverNoStatusMap.get(itemPo.getPurchaseDeliverOrderNo());
                    return DeliverOrderStatus.WAIT_DELIVER.equals(deliverOrderStatus)
                            || DeliverOrderStatus.WAIT_RECEIVE.equals(deliverOrderStatus);
                })
                .collect(Collectors.groupingBy(PurchaseDeliverOrderItemPo::getSku,
                        Collectors.summingInt(PurchaseDeliverOrderItemPo::getDeliverCnt)));


        return skuDeliverCntMap.entrySet().stream()
                .map(entry -> {
                    final PurchaseSkuCntVo purchaseSkuCntVo = new PurchaseSkuCntVo();
                    purchaseSkuCntVo.setSkuCode(entry.getKey());
                    purchaseSkuCntVo.setSkuCnt(entry.getValue());
                    return purchaseSkuCntVo;
                }).collect(Collectors.toList());
    }

    /**
     * 通过编号批量查询列表详情
     *
     * @author ChenWenLong
     * @date 2023/3/6 18:20
     */
    public List<PurchaseChildOrderExportBo> getBatchPurchaseChildOrderNo(List<String> purchaseChildOrderNoList) {
        return purchaseChildOrderDao.getItemBatchPurchaseChildOrderNo(purchaseChildOrderNoList);
    }

    public void convertSkuForPurchaseChildVo(List<PurchaseChildOrderVo> purchaseChildOrderVoList) {
        if (CollectionUtils.isEmpty(purchaseChildOrderVoList)) {
            return;
        }
        final List<String> purchaseChildOrderNoList = purchaseChildOrderVoList.stream()
                .map(PurchaseChildOrderVo::getPurchaseChildOrderNo)
                .distinct()
                .collect(Collectors.toList());

        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNoList(purchaseChildOrderNoList);
        final Map<String, List<PurchaseChildOrderItemPo>> purchaseChildNoItemMap = purchaseChildOrderItemPoList.stream()
                .collect(Collectors.groupingBy(PurchaseChildOrderItemPo::getPurchaseChildOrderNo));

        purchaseChildOrderVoList.forEach(childVo -> {
            final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList1 = purchaseChildNoItemMap.get(childVo.getPurchaseChildOrderNo());
            if (CollectionUtils.isEmpty(purchaseChildOrderItemPoList1)) {
                return;
            }
            final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildOrderItemPoList1.get(0);
            childVo.setSku(purchaseChildOrderItemPo.getSku());
        });
    }

    /**
     * 通过采购子单单号批量查询并过滤已作废
     *
     * @author ChenWenLong
     * @date 2023/4/7 15:18
     */
    public List<PurchaseDeliverOrderPo> getListByChildOrderNoListNotStatus(List<String> purchaseChildOrderNoList, DeliverOrderStatus notDeliverOrderStatus) {
        return purchaseDeliverOrderDao.getListByChildOrderNoListNotStatus(purchaseChildOrderNoList, notDeliverOrderStatus);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchReceiveUpdateCtrl(PurchaseChildBatchReceiveDto dto, PurchaseChildOrderItemPo purchaseChildOrderItemPo,
                                       Map<String, String> skuBatchMap, PurchaseChildOrderPo purchaseChildOrderPo,
                                       PurchaseParentOrderChangePo purchaseParentOrderChangePo,
                                       PurchaseChildOrderChangePo purchaseChildOrderChangePo) {
        PurchaseOrderStatus targetOrderStatus;
        if (BooleanType.TRUE.equals(dto.getIsReceived())) {
            targetOrderStatus = purchaseChildOrderPo.getPurchaseOrderStatus().toWaitScheduling();
            // 更新sku批次码
            purchaseChildOrderItemPo.setSkuBatchCode(skuBatchMap.get(purchaseChildOrderItemPo.getSku()));
            purchaseChildOrderItemDao.updateByIdVersion(purchaseChildOrderItemPo);

            // 原料来源为其他供应商的，要求wms生成出库单
            final List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList = purchaseChildOrderRawDao.getListByChildNoList(purchaseChildOrderPo.getPurchaseChildOrderNo(),
                    PurchaseRawBizType.DEMAND, Collections.singletonList(RawSupplier.OTHER_SUPPLIER));
            // wms 生成原料发货单
            this.rawDeliver(purchaseChildOrderRawPoList, purchaseChildOrderPo);

            // 兼容旧数据，生成原料发货单
            if (PurchaseBizType.PROCESS.equals(purchaseChildOrderPo.getPurchaseBizType())) {
                List<PurchaseChildOrderRawDeliverPo> purchaseChildOrderRawDeliverPoList = purchaseChildOrderRawDeliverDao.getListByChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
                purchaseChildOrderRawDeliverPoList = purchaseChildOrderRawDeliverPoList.stream()
                        .filter(po -> StringUtils.isNotBlank(po.getPurchaseRawDeliverOrderNo()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(purchaseChildOrderRawDeliverPoList)) {
                    final List<PurchaseChildOrderRawPo> heteRawPoList = purchaseChildOrderRawDao.getListByChildNoList(purchaseChildOrderPo.getPurchaseChildOrderNo(),
                            PurchaseRawBizType.DEMAND, Collections.singletonList(RawSupplier.HETE));
                    this.rawDeliver(heteRawPoList, purchaseChildOrderPo);
                }
            }

            final LocalDateTime now = LocalDateTime.now();
            // 更新母单change
            purchaseParentOrderChangePo.setReceiveOrderTime(now);
            purchaseParentOrderChangeDao.updateByIdVersion(purchaseParentOrderChangePo);

            // 更新子单change
            purchaseChildOrderChangePo.setReceiveOrderTime(now);
            purchaseChildOrderChangePo.setReceiveOrderUser(GlobalContext.getUserKey());
            purchaseChildOrderChangePo.setReceiveOrderUsername(GlobalContext.getUsername());
            purchaseChildOrderChangeDao.updateByIdVersion(purchaseChildOrderChangePo);

            // 日志
            logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                    purchaseChildOrderPo.getPurchaseChildOrderNo(), targetOrderStatus.getRemark(), Collections.emptyList());
        } else {
            if (StringUtils.isBlank(dto.getRefuseRemarks())) {
                throw new ParamIllegalException("拒绝接单必须填拒绝原因");
            }
            LogVersionBo logVersionBo = new LogVersionBo();
            logVersionBo.setKey("拒绝接单原因:");
            logVersionBo.setValue(dto.getRefuseRemarks());
            logVersionBo.setValueType(LogVersionValueType.STRING);

            targetOrderStatus = purchaseChildOrderPo.getPurchaseOrderStatus().toWaitFollowerConfirm();

            logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                    purchaseChildOrderPo.getPurchaseChildOrderNo(), targetOrderStatus.getRemark(),
                    Collections.singletonList(logVersionBo));

            // 拒绝接单把类型修改为无类型
            purchaseChildOrderPo.setPurchaseBizType(PurchaseBizType.NO_TYPE);

            // 供应商原料库存释放
            purchaseRawBaseService.releaseSupplierRawInventory(purchaseChildOrderPo, SupplierInventoryCtrlReason.REFUSE_RECEIVE);

            // wms对应的出库单取消
            final ProcessOrderCancelEventDto processOrderCancelEventDto = new ProcessOrderCancelEventDto();
            processOrderCancelEventDto.setProcessOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
            processOrderCancelEventDto.setDeliveryType(WmsEnum.DeliveryType.PURCHASE_RAW_MATERIAL);
            processOrderCancelEventDto.setKey(purchaseChildOrderPo.getPurchaseChildOrderNo());
            processOrderCancelEventDto.setOperator(GlobalContext.getUserKey());
            processOrderCancelEventDto.setOperatorName(GlobalContext.getUsername());
            consistencySendMqService.execSendMq(WmsProcessCancelHandler.class, processOrderCancelEventDto);

            // 若该采购子单原先是加工采购类型，则需要删除对应的原料
            purchaseChildOrderRawDao.deleteByChildNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
            purchaseChildOrderRawDeliverDao.deleteByChildNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        }
        purchaseChildOrderPo.setPurchaseOrderStatus(targetOrderStatus);
        // 更新子单
        purchaseChildOrderDao.updateByIdVersion(purchaseChildOrderPo);
    }

    private void rawDeliver(List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList,
                            PurchaseChildOrderPo purchaseChildOrderPo) {
        if (CollectionUtils.isEmpty(purchaseChildOrderRawPoList)) {
            return;
        }
        // 按照原料仓库分组，要求wms 生成原料发货单
        final Map<String, List<PurchaseChildOrderRawPo>> rawWarehouseRawPoListMap = purchaseChildOrderRawPoList.stream()
                .collect(Collectors.groupingBy(PurchaseChildOrderRawPo::getRawWarehouseCode));

        rawWarehouseRawPoListMap.forEach((rawWarehouseCode, rawPoList) -> {
            final List<RawDeliverBo> purchaseRawDeliverBoList = rawPoList.stream()
                    .map(po -> {
                        final RawDeliverBo purchaseRawDeliverBo = new RawDeliverBo();
                        purchaseRawDeliverBo.setDeliveryCnt(po.getDeliveryCnt());
                        purchaseRawDeliverBo.setSku(po.getSku());
                        return purchaseRawDeliverBo;
                    }).collect(Collectors.toList());

            // wms 生成原料发货单
            final WmsDeliverBo wmsDeliverBo = new WmsDeliverBo();
            wmsDeliverBo.setRelatedOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
            wmsDeliverBo.setRawWarehouseCode(rawWarehouseCode);
            wmsDeliverBo.setDeliveryType(WmsEnum.DeliveryType.PURCHASE_RAW_MATERIAL);
            wmsDeliverBo.setRawDeliverMode(RawDeliverMode.RECEIVE_RAW_DELIVER);
            wmsDeliverBo.setDispatchNow(BooleanType.TRUE);
            wmsDeliverBo.setPlatform(purchaseChildOrderPo.getPlatform());
            purchaseRawRefService.wmsRawDeliver(wmsDeliverBo, purchaseRawDeliverBoList);
        });
    }

    /**
     * 大货采购子单列表、加工采购子单列表查询条件
     *
     * @author ChenWenLong
     * @date 2023/5/23 15:11
     */
    public PurchaseProductSearchDto getSearchPurchaseChildWhere(PurchaseProductSearchDto dto) {
        if (CollectionUtils.isNotEmpty(dto.getCategoryIdList())) {
            final List<String> skuList = plmRemoteService.getSkuListByCategoryIdList(dto.getCategoryIdList());
            if (CollectionUtils.isEmpty(skuList)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuList);
            } else {
                skuList.retainAll(dto.getSkuList());
                if (CollectionUtils.isEmpty(skuList)) {
                    return null;
                }
                dto.setSkuList(skuList);
            }
        }
        if (CollectionUtils.isNotEmpty(dto.getSkuEncodeList())) {
            final List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(dto.getSkuEncodeList());
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                skuListByEncode.retainAll(dto.getSkuList());
                if (CollectionUtils.isEmpty(skuListByEncode)) {
                    return null;
                }
                dto.setSkuList(skuListByEncode);
            }
        }

        if (CollectionUtils.isNotEmpty(dto.getSupplierProductNameList())) {
            final List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getBatchProductNameOrSupplierCode(dto.getSupplierProductNameList(), dto.getAuthSupplierCode());
            if (CollectionUtils.isEmpty(supplierProductComparePoList)) {
                return null;
            }
            List<String> supplierSkuList = supplierProductComparePoList.stream().map(SupplierProductComparePo::getSku).distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(supplierSkuList)) {
                return null;
            }
            List<String> supplierCodeList = supplierProductComparePoList.stream().map(SupplierProductComparePo::getSupplierCode).distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(supplierSkuList);
            } else {
                supplierSkuList.addAll(dto.getSkuList());
                dto.setSkuList(supplierSkuList);
            }
            if (CollectionUtils.isEmpty(dto.getSupplierCodeList())) {
                dto.setSupplierCodeList(supplierCodeList);
            } else {
                supplierCodeList.addAll(dto.getSupplierCodeList());
                dto.setSkuList(supplierCodeList);
            }
        }

        List<String> itemChildNoList = this.getItemChildNoList(dto);
        if (CollectionUtils.isNotEmpty(dto.getSkuList()) && CollectionUtils.isEmpty(itemChildNoList)) {
            return null;
        }
        if (StringUtils.isNotBlank(dto.getSkuBatchCode()) && CollectionUtils.isEmpty(itemChildNoList)) {
            return null;
        }
        if (CollectionUtils.isNotEmpty(dto.getSkuBatchCodeList()) && CollectionUtils.isEmpty(itemChildNoList)) {
            return null;
        }
        if (CollectionUtils.isEmpty(dto.getPurchaseChildOrderNoList())) {
            dto.setPurchaseChildOrderNoList(itemChildNoList);
        } else {
            if (CollectionUtils.isNotEmpty(itemChildNoList)) {
                itemChildNoList.retainAll(dto.getPurchaseChildOrderNoList());
                if (CollectionUtils.isEmpty(itemChildNoList)) {
                    return null;
                }
            }
        }

        if (CollectionUtils.isNotEmpty(dto.getPurchaseDeliverOrderNoList())) {
            List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByNoList(dto.getPurchaseDeliverOrderNoList());
            if (CollectionUtils.isEmpty(purchaseDeliverOrderPoList)) {
                return null;
            }
            List<String> purchaseChildOrderNoList = purchaseDeliverOrderPoList.stream()
                    .map(PurchaseDeliverOrderPo::getPurchaseChildOrderNo)
                    .distinct()
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(itemChildNoList)) {
                itemChildNoList.addAll(purchaseChildOrderNoList);
            } else {
                itemChildNoList.retainAll(purchaseChildOrderNoList);
            }
            if (CollectionUtils.isEmpty(itemChildNoList)) {
                return null;
            }
        }
        if (StringUtils.isNotBlank(dto.getSupplierProductName())) {
            List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getByLikeProductNameOrSupplierCode(dto.getSupplierProductName(), dto.getAuthSupplierCode());
            if (CollectionUtils.isEmpty(supplierProductComparePoList)) {
                return null;
            }
            List<String> supplierSkuList = supplierProductComparePoList.stream().map(SupplierProductComparePo::getSku).distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(supplierSkuList)) {
                return null;
            }
            List<String> supplierNos = this.getNoListBySupplierProduct(supplierProductComparePoList);
            if (CollectionUtils.isEmpty(itemChildNoList)) {
                itemChildNoList.addAll(supplierNos);
            } else {
                itemChildNoList.retainAll(supplierNos);
            }
        }

        if (CollectionUtils.isNotEmpty(itemChildNoList)) {
            dto.setPurchaseChildOrderNoList(itemChildNoList);
        }

        return dto;
    }

    public BooleanType skuStockInventory(@Valid @NotNull SkuStockBo skuStockBo) {
        List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList = skuStockBo.getPurchaseChildOrderRawPoList();
        purchaseChildOrderRawPoList = purchaseChildOrderRawPoList.stream()
                .filter(rawPo -> RawSupplier.HETE.equals(rawPo.getRawSupplier())
                        || RawSupplier.OTHER_SUPPLIER.equals(rawPo.getRawSupplier()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(purchaseChildOrderRawPoList)) {
            return BooleanType.TRUE;
        }

        final List<String> skuList = purchaseChildOrderRawPoList.stream()
                .map(PurchaseChildOrderRawPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        SkuInstockInventoryQueryDto skuInstockInventoryQueryDto = new SkuInstockInventoryQueryDto();
        skuInstockInventoryQueryDto.setWarehouseCode(skuStockBo.getRawWarehouseCode());
        skuInstockInventoryQueryDto.setSkuCodes(skuList);
        skuInstockInventoryQueryDto.setProductQuality(skuStockBo.getProductQuality());
        skuInstockInventoryQueryDto.setDeliveryType(skuStockBo.getDeliveryType());
        skuInstockInventoryQueryDto.setPlatCode(skuStockBo.getPlatform());
        List<SkuInventoryVo> skuInventoryList = wmsRemoteService.getSkuInventoryList(skuInstockInventoryQueryDto);
        if (CollectionUtils.isEmpty(skuInventoryList)) {
            return BooleanType.FALSE;
        }
        final Map<String, Integer> skuInventory = skuInventoryList.stream()
                .collect(Collectors.toMap(SkuInventoryVo::getSkuCode, SkuInventoryVo::getInStockAmount));

        for (PurchaseChildOrderRawPo po : purchaseChildOrderRawPoList) {
            final Integer skuStock = skuInventory.get(po.getSku());
            if (null == skuStock) {
                return BooleanType.FALSE;
            }
            if (skuStock < po.getDeliveryCnt()) {
                return BooleanType.FALSE;
            }
        }
        return BooleanType.TRUE;
    }

    /**
     * 结算批量更新状态
     *
     * @param purchaseDeliverOrderNos:
     * @param deliverOrderStatus:
     * @return void
     * @author ChenWenLong
     * @date 2023/7/1 11:18
     */
    public void updateBatchPurchaseDeliverOrderNo(List<String> purchaseDeliverOrderNos, DeliverOrderStatus deliverOrderStatus) {
        purchaseDeliverOrderDao.updateBatchPurchaseDeliverOrderNo(purchaseDeliverOrderNos, deliverOrderStatus);
    }

    public Map<String, List<PurchaseChildOrderItemPo>> getChildItemMapBySkuList(List<String> skuList) {
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListBySku(skuList);
        return purchaseChildOrderItemPoList.stream()
                .collect(Collectors.groupingBy(PurchaseChildOrderItemPo::getSku));
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
        return purchaseChildOrderDao.getListBySupplierProduct(supplierProductPoList)
                .stream()
                .map(PurchaseChildOrderPo::getPurchaseChildOrderNo)
                .collect(Collectors.toList());
    }

    public PurchaseChildOrderSimpleVo getPurchaseSimpleVoByNo(PurchaseChildNoDto dto) {
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(dto.getPurchaseChildOrderNo());
        if (null == purchaseChildOrderPo) {
            return new PurchaseChildOrderSimpleVo();
        }
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNo(dto.getPurchaseChildOrderNo());
        if (CollectionUtils.isEmpty(purchaseChildOrderItemPoList)) {
            return new PurchaseChildOrderSimpleVo();
        }
        final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildOrderItemPoList.get(0);
        final List<PlmGoodsDetailVo> plmGoodsDetailVoList = plmRemoteService.getGoodsDetail(Collections.singletonList(purchaseChildOrderItemPo.getSku()));

        return PurchaseChildOrderSimpleVo.builder()
                .purchaseChildOrderNo(dto.getPurchaseChildOrderNo())
                .platform(purchaseChildOrderPo.getPlatform())
                .supplierCode(purchaseChildOrderPo.getSupplierCode())
                .sku(purchaseChildOrderItemPo.getSku())
                .plmGoodsDetailVoList(plmGoodsDetailVoList)
                .build();
    }

    public List<SupplierSkuBatchCodeVo> getSupplierBySkuBatchCode(SkuBatchCodeDto dto) {
        List<SupplierSkuBatchCodeVo> supplierSkuBatchCodeVoList = new ArrayList<>();
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListBySkuBatchCode(dto.getSkuBatchCodeList());
        if (CollectionUtils.isNotEmpty(purchaseChildOrderItemPoList)) {
            final List<String> purchaseChildOrderNoList = purchaseChildOrderItemPoList.stream()
                    .map(PurchaseChildOrderItemPo::getPurchaseChildOrderNo)
                    .collect(Collectors.toList());

            final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList);
            final Map<String, PurchaseChildOrderPo> purchaseChildOrderNoPoMap = purchaseChildOrderPoList.stream()
                    .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));

            purchaseChildOrderItemPoList.forEach(itemPo -> {
                final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderNoPoMap.get(itemPo.getPurchaseChildOrderNo());
                final SupplierSkuBatchCodeVo supplierSkuBatchCodeVo = new SupplierSkuBatchCodeVo();
                supplierSkuBatchCodeVo.setSkuBatchCode(itemPo.getSkuBatchCode());
                supplierSkuBatchCodeVo.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
                supplierSkuBatchCodeVo.setSupplierName(purchaseChildOrderPo.getSupplierName());
                supplierSkuBatchCodeVoList.add(supplierSkuBatchCodeVo);
            });
        }

        final List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getListBySkuBatchCode(dto.getSkuBatchCodeList());
        if (CollectionUtils.isNotEmpty(sampleChildOrderPoList)) {
            sampleChildOrderPoList.forEach(sampleChildOrderPo -> {
                final SupplierSkuBatchCodeVo supplierSkuBatchCodeVo = new SupplierSkuBatchCodeVo();
                supplierSkuBatchCodeVo.setSkuBatchCode(sampleChildOrderPo.getSkuBatchCode());
                supplierSkuBatchCodeVo.setSupplierCode(sampleChildOrderPo.getSupplierCode());
                supplierSkuBatchCodeVo.setSupplierName(sampleChildOrderPo.getSupplierName());
                supplierSkuBatchCodeVoList.add(supplierSkuBatchCodeVo);
            });
        }

        return supplierSkuBatchCodeVoList;

    }

    /**
     * 更新可发货数为0
     *
     * @param purchaseChildOrderPoList:
     * @return void
     * @author ChenWenLong
     * @date 2023/11/9 09:52
     */
    public void updatePurchaseShippableCnt(List<PurchaseChildOrderPo> purchaseChildOrderPoList) {
        if (CollectionUtils.isEmpty(purchaseChildOrderPoList)) {
            return;
        }
        for (PurchaseChildOrderPo purchaseChildOrderPo : purchaseChildOrderPoList) {
            purchaseChildOrderPo.setShippableCnt(ScmConstant.PURCHASE_CHILD_SHIPPABLE_CNT);
        }
        purchaseChildOrderDao.updateBatchById(purchaseChildOrderPoList);
    }

    /**
     * 创建子单后更新母单和子单的状态
     *
     * @param purchaseChildOrderPoList
     * @param purchaseParentOrderPoList
     */
    public void updateStatusAfterCreateChildOrder(List<PurchaseChildOrderPo> purchaseChildOrderPoList,
                                                  List<PurchaseParentOrderPo> purchaseParentOrderPoList) {
        final List<Long> purchaseParentIdList = purchaseParentOrderPoList.stream()
                .map(PurchaseParentOrderPo::getPurchaseParentOrderId)
                .collect(Collectors.toList());

        final List<PurchaseParentOrderChangePo> purchaseParentOrderChangePoList = purchaseParentOrderChangeDao.getListByParentIdList(purchaseParentIdList);
        if (purchaseParentOrderChangePoList.size() != purchaseParentIdList.size()) {
            throw new BizException("找不到对应的采购需求单,采购需求单取消失败！");
        }
        final List<Long> idList = purchaseChildOrderPoList.stream()
                .map(PurchaseChildOrderPo::getPurchaseChildOrderId)
                .collect(Collectors.toList());
        List<PurchaseChildOrderChangePo> purchaseChildOrderChangePoList = purchaseChildOrderChangeDao.getByChildOrderId(idList);
        if (purchaseChildOrderChangePoList.size() != idList.size()) {
            throw new BizException("找不到对应的采购需求单,采购需求单取消失败！");
        }

        // 更新子单状态
        purchaseChildOrderPoList.forEach(po -> {
            PurchaseOrderStatus purchaseOrderStatus = po.getPurchaseOrderStatus();
            PurchaseOrderStatus waitReceiveOrder = purchaseOrderStatus.toWaitReceiveOrder();
            po.setPurchaseOrderStatus(waitReceiveOrder);

            logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                    po.getPurchaseChildOrderNo(), PurchaseOrderStatus.WAIT_RECEIVE_ORDER.getRemark(), Collections.emptyList());
        });
        purchaseChildOrderDao.updateBatchByIdVersion(purchaseChildOrderPoList);

        // 更新子单change
        purchaseChildOrderChangePoList.forEach(po -> {
            po.setConfirmTime(new DateTime().toLocalDateTime());
            po.setConfirmUser(GlobalContext.getUserKey());
            po.setConfirmUsername(GlobalContext.getUsername());
        });
        purchaseChildOrderChangeDao.updateBatchByIdVersion(purchaseChildOrderChangePoList);

        // 更新母单change
        purchaseParentOrderChangePoList.forEach(po -> {
            po.setConfirmTime(new DateTime().toLocalDateTime());
        });
        purchaseParentOrderChangeDao.updateBatchByIdVersion(purchaseParentOrderChangePoList);
    }

    public void checkRawSkuUnique(List<RawProductItemDto> rawProductItemList) {
        final List<String> skuList = rawProductItemList.stream()
                .map(RawProductItemDto::getSku)
                .distinct()
                .collect(Collectors.toList());
        if (skuList.size() != rawProductItemList.size()) {
            throw new ParamIllegalException("相同的sku不允许重复提交，请检查提交内容后重新提交！");
        }
    }

    /**
     * 创建供应商的原料采购单
     *
     * @param otherSupplierRawItemDtoList
     * @param purchaseChildNoParentPoMap
     * @param supplierCodeWarehouseMap
     * @param purchaseChildNoPoMap
     */
    public void createSupplierRawPurchaseOrder(List<RawProductItemDto> otherSupplierRawItemDtoList,
                                               Map<String, PurchaseParentOrderPo> purchaseChildNoParentPoMap,
                                               Map<String, SupplierWarehousePo> supplierCodeWarehouseMap,
                                               Map<String, PurchaseChildOrderPo> purchaseChildNoPoMap) {

        // 校验原料信息
        final List<String> skuList = otherSupplierRawItemDtoList.stream()
                .map(RawProductItemDto::getSku)
                .distinct()
                .collect(Collectors.toList());
        final SampleSkuListDto sampleSkuListDto = new SampleSkuListDto();
        sampleSkuListDto.setSkuList(skuList);
        final List<SkuBomListVo> skuBomListVoList = produceDataBaseService.getBomListBySkuList(sampleSkuListDto);
        if (CollectionUtils.isEmpty(skuBomListVoList)) {
            throw new ParamIllegalException("SKU {}无原料信息，请维护后再提交", skuList);
        }
        final List<String> bomSkuList = skuBomListVoList.stream()
                .map(SkuBomListVo::getSku)
                .distinct()
                .collect(Collectors.toList());
        // 过滤bom信息不存在的sku
        final List<String> absentSkuList = skuList.stream()
                .filter(sku -> !bomSkuList.contains(sku))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(absentSkuList)) {
            throw new ParamIllegalException("SKU {}无原料信息，请维护后再提交", absentSkuList);
        }
        // 校验虚拟仓
        final List<PurchaseParentOrderPo> newPurchaseParentOrderPoList = new ArrayList<>();
        final List<PurchaseParentOrderChangePo> newPurchaseParentOrderChangePoList = new ArrayList<>();
        final List<PurchaseParentOrderItemPo> newPurchaseParentOrderItemPoList = new ArrayList<>();
        final List<RawPurchaseParentRelatePo> rawPurchaseParentRelatePoList = new ArrayList<>();
        otherSupplierRawItemDtoList.forEach(rawDto -> {
            final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildNoPoMap.get(rawDto.getPurchaseChildOrderNo());
            final SupplierWarehousePo virtualWarehousePo = supplierCodeWarehouseMap.get(purchaseChildOrderPo.getSupplierCode());
            if (null == virtualWarehousePo) {
                throw new ParamIllegalException("供应商{}未绑定对应虚拟仓，请在基础设置-商家仓库中绑定后进行此操作", rawDto.getRawWarehouseCode());
            }
            final PurchaseParentOrderPo purchaseParentOrderPo = PurchaseConverter.itemDtoToRawPurchaseParentPo(purchaseChildNoParentPoMap.get(rawDto.getPurchaseChildOrderNo()),
                    rawDto.getSpu(), virtualWarehousePo, rawDto.getDeliveryCnt());
            final String purchaseParentOrderNo = idGenerateService.getConfuseCode(ScmConstant.PURCHASE_PARENT_NO_PREFIX, TimeType.CN_DAY_YYYY, ConfuseLength.L_4);
            purchaseParentOrderPo.setPurchaseParentOrderNo(purchaseParentOrderNo);
            // 这里的changePo还没有赋值parentId
            PurchaseParentOrderChangePo purchaseParentOrderChangePo = PurchaseConverter.parentPoToChangePo(purchaseParentOrderPo);
            final PurchaseParentOrderItemPo purchaseParentOrderItemPo = PurchaseConverter.itemDtoToRawPurchaseParentItemPo(rawDto,
                    purchaseParentOrderNo);
            // 日志
            purchaseParentOrderPo.setUpdateTime(LocalDateTime.now());
            logBaseService.simpleLog(LogBizModule.PURCHASE_PARENT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                    purchaseParentOrderPo.getPurchaseParentOrderNo(), PurchaseParentOrderStatus.WAIT_APPROVE.getRemark(),
                    Collections.emptyList());
            logBaseService.purchaseParentVersionLog(LogBizModule.PURCHASE_PARENT_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                    purchaseParentOrderPo.getPurchaseParentOrderNo(), purchaseParentOrderPo);

            // 记录原料采购单的母单号到原子单
            final RawPurchaseParentRelatePo rawPurchaseParentRelatePo = new RawPurchaseParentRelatePo();
            rawPurchaseParentRelatePo.setRawPurchaseParentOrderNo(purchaseParentOrderNo);
            rawPurchaseParentRelatePo.setPurchaseChildOrderNo(rawDto.getPurchaseChildOrderNo());
            rawPurchaseParentRelatePoList.add(rawPurchaseParentRelatePo);

            newPurchaseParentOrderPoList.add(purchaseParentOrderPo);
            newPurchaseParentOrderChangePoList.add(purchaseParentOrderChangePo);
            newPurchaseParentOrderItemPoList.add(purchaseParentOrderItemPo);
        });

        purchaseParentOrderDao.insertBatch(newPurchaseParentOrderPoList);
        final Map<String, Long> purchaseParentOrderNoIdMap = newPurchaseParentOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseParentOrderPo::getPurchaseParentOrderNo
                        , PurchaseParentOrderPo::getPurchaseParentOrderId));

        newPurchaseParentOrderChangePoList.forEach(changePo -> {
            changePo.setPurchaseParentOrderId(purchaseParentOrderNoIdMap.get(changePo.getPurchaseParentOrderNo()));
        });
        purchaseParentOrderChangeDao.insertBatch(newPurchaseParentOrderChangePoList);
        purchaseParentOrderItemDao.insertBatch(newPurchaseParentOrderItemPoList);
        rawPurchaseParentRelateDao.insertBatch(rawPurchaseParentRelatePoList);
    }

    public List<PurchaseChildOrderPo> getPoListByPurchaseChildNoList(List<String> purchaseChildOrderNoList) {
        return purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList);
    }

    /**
     * 作废采购订单
     *
     * @param dto
     */
    public void cancel(PurchaseCancelDto dto) {
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getByIdVersion(dto.getPurchaseChildOrderId(), dto.getVersion());
        Assert.notNull(purchaseChildOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        final PurchaseParentOrderPo purchaseParentOrderPo = purchaseParentOrderDao.getOneByNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
        Assert.notNull(purchaseParentOrderPo, () -> new BizException("查找不到对应的采购母单，作废操作失败"));
        final List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList = purchaseParentOrderItemDao.getListByParentNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
        Assert.notEmpty(purchaseParentOrderItemPoList, () -> new BizException("查找不到对应的采购母单，作废操作失败"));
        final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildOrderItemDao.getOneByChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        Assert.notNull(purchaseChildOrderItemPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));

        if (purchaseChildOrderPo.getPurchaseOrderStatus().getSort() < PurchaseOrderStatus.WAIT_CONFIRM.getSort()
                || purchaseChildOrderPo.getPurchaseOrderStatus().getSort() > PurchaseOrderStatus.WAIT_DELIVER.getSort()) {
            throw new ParamIllegalException("当前采购单状态不处于【{}】【{}】之间，作废操作失败",
                    PurchaseOrderStatus.WAIT_CONFIRM.getRemark(), PurchaseOrderStatus.WAIT_DELIVER.getRemark());
        }

        // 若采购单处于待发货状态作废
        if (PurchaseOrderStatus.WAIT_DELIVER.equals(purchaseChildOrderPo.getPurchaseOrderStatus())) {
            final List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
            if (CollectionUtils.isNotEmpty(purchaseDeliverOrderPoList)) {
                throw new ParamIllegalException("当前采购单：{}，已经生成发货单，无法进行作废操作", purchaseChildOrderPo.getPurchaseChildOrderNo());
            }
        }

        // 采购订单状态处于前处理之前才需要释放原料
        if (purchaseChildOrderPo.getPurchaseOrderStatus().getSort() < PurchaseOrderStatus.COMMISSION.getSort()) {
            // 供应商原料库存释放
            purchaseRawBaseService.releaseSupplierRawInventory(purchaseChildOrderPo, SupplierInventoryCtrlReason.PURCHASE_CANCEL);
        }

        BigDecimal releaseCapacity = purchaseChildOrderPo.getCapacity();
        final PurchaseOrderStatus targetStatus = PurchaseOrderStatus.DELETE;
        purchaseChildOrderPo.setPurchaseOrderStatus(targetStatus);
        purchaseChildOrderPo.setShippableCnt(0);
        purchaseChildOrderPo.setCapacity(BigDecimal.ZERO);
        purchaseChildOrderDao.updateByIdVersion(purchaseChildOrderPo);

        logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseChildOrderPo.getPurchaseChildOrderNo(), targetStatus.getRemark(), Collections.emptyList());
        // 是否返还可拆单数
        if (PurchaseReturnPlacedCnt.TRUE.equals(dto.getPurchaseReturnPlacedCnt())) {
            final Map<String, Integer> skuNotPlacedCntMap = new HashMap<>();
            skuNotPlacedCntMap.put(purchaseChildOrderItemPo.getSku(), purchaseChildOrderItemPo.getPurchaseCnt());
            purchaseParentOrderItemPoList.forEach(parentOrderItemPo -> parentOrderItemPo.setCanSplitCnt(parentOrderItemPo.getCanSplitCnt()
                    + skuNotPlacedCntMap.getOrDefault(parentOrderItemPo.getSku(), 0)));
            purchaseParentOrderPo.setCanSplitCnt(purchaseParentOrderPo.getCanSplitCnt() + purchaseChildOrderItemPo.getPurchaseCnt());
        }

        purchaseParentOrderItemPoList.forEach(parentOrderItemPo -> {
            if (parentOrderItemPo.getSku().equals(purchaseChildOrderItemPo.getSku())) {
                parentOrderItemPo.setUndeliveredCnt(Math.max(parentOrderItemPo.getUndeliveredCnt() - purchaseChildOrderItemPo.getPurchaseCnt(), 0));
            }
        });
        purchaseParentOrderPo.setUndeliveredCnt(Math.max(purchaseParentOrderPo.getUndeliveredCnt() - purchaseChildOrderItemPo.getPurchaseCnt(), 0));
        purchaseParentOrderItemDao.updateBatchByIdVersion(purchaseParentOrderItemPoList);
        purchaseParentOrderDao.updateByIdVersion(purchaseParentOrderPo);
        purchaseChildOrderItemPo.setUndeliveredCnt(0);
        purchaseChildOrderItemDao.updateByIdVersion(purchaseChildOrderItemPo);

        // 归还供应商对应产能
        if (StringUtils.isNotBlank(purchaseChildOrderPo.getSupplierCode())) {
            final LocalDate capacityDate = this.getCapacityDate(purchaseChildOrderPo.getPromiseDate(), purchaseChildOrderPo.getSupplierCode());
            final SupOpCapacityBo supOpCapacityBo = new SupOpCapacityBo();
            supOpCapacityBo.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
            supOpCapacityBo.setOperateDate(capacityDate);
            supOpCapacityBo.setOperateValue(releaseCapacity);
            supOpCapacityBo.setBizNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
            supplierCapacityRefService.operateSupplierCapacityBatch(Collections.singletonList(supOpCapacityBo));
            // 产能变更日志
            logBaseService.purchaseCapacityLog(LogBizModule.PURCHASE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                    purchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseChildOrderPo, purchaseChildOrderPo.getCapacity());
        }

        // 采购单作废，作废wms的原料出库单
        final ProcessOrderCancelEventDto processOrderCancelEventDto = new ProcessOrderCancelEventDto();
        processOrderCancelEventDto.setProcessOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        processOrderCancelEventDto.setDeliveryType(WmsEnum.DeliveryType.PURCHASE_RAW_MATERIAL);
        processOrderCancelEventDto.setKey(purchaseChildOrderPo.getPurchaseChildOrderNo());
        processOrderCancelEventDto.setOperator(GlobalContext.getUserKey());
        processOrderCancelEventDto.setOperatorName(GlobalContext.getUsername());
        consistencySendMqService.execSendMq(WmsProcessCancelHandler.class, processOrderCancelEventDto);

        // 单据状态变更后推送单号给wms
        final PurchaseChildNoMqDto purchaseChildNoMqDto = new PurchaseChildNoMqDto();
        purchaseChildNoMqDto.setPurchaseChildOrderNoList(Collections.singletonList(purchaseChildOrderPo.getPurchaseChildOrderNo()));
        consistencySendMqService.execSendMq(PurchaseChangeHandler.class, purchaseChildNoMqDto);
    }


    public List<SkuUndeliveredCntVo> getPurchaseUndeliveredCntBySku(SkuListDto dto) {
        return purchaseChildOrderDao.getPurchaseUndeliveredCntBySku(dto.getSkuList(), PurchaseOrderStatus.DELETE);
    }

    /**
     * 供应商本月货款
     *
     * @param supplierCode
     * @return
     */
    public BigDecimal getMonthGoodsPayment(String supplierCode) {
        final LocalDateTime startOfCurrentMonth = ScmTimeUtil.getStartOfCurrentMonth();
        return purchaseDeliverOrderDao.getMonthGoodsPayment(startOfCurrentMonth, LocalDateTime.now(),
                DeliverOrderStatus.WAREHOUSED, supplierCode);
    }

    /**
     * 供应商上月入库金额
     *
     * @return
     */
    public BigDecimal purchaseLastMonWarehousingMoney(String supplierCode) {
        final LocalDateTime startOfLastMonth = ScmTimeUtil.getStartOfLastMonth();
        final LocalDateTime endOfLastMonth = ScmTimeUtil.getEndOfLastMonth();
        return purchaseDeliverOrderDao.getMonthGoodsPayment(startOfLastMonth, endOfLastMonth,
                DeliverOrderStatus.WAREHOUSED, supplierCode);
    }

    /**
     * 供应商在途金额
     *
     * @return
     */
    public BigDecimal purchaseLastMonInTransitMoney(String supplierCode) {
        return purchaseDeliverOrderDao.getInTransitMoney(DeliverOrderStatus.getInTransitStatusList(), supplierCode);
    }


    @RedisLock(key = "#childOrderPo.purchaseChildOrderNo", prefix = ScmRedisConstant.PURCHASE_CHILD_ORDER_CREATE_QC,
            exceptionDesc = "采购订单完成质检中，请稍后重试！")
    public void doCompleteQc(PurchaseChildOrderPo childOrderPo, List<PurchaseChildOrderItemPo> allChildOrderItemPos) {
        String supplierCode = childOrderPo.getSupplierCode();
        List<String> needCreateQcSupplierCodes = qcConfig.getPurchaseSupplierCodes();
        if (CollectionUtil.isEmpty(needCreateQcSupplierCodes)) {
            log.info("无配置需要创建质检单供应商编码，跳过创建质检单");
            return;
        }
        if (!needCreateQcSupplierCodes.contains(supplierCode)) {
            log.info("当前供应商不属于配置供应商编码，跳过创建质检单，supplierCode:{}", supplierCode);
            return;
        }

        List<PurchaseChildOrderItemPo> matchChildOrderItems = allChildOrderItemPos.stream()
                .filter(childOrderItemPo -> Objects.equals(childOrderPo.getPurchaseChildOrderNo(),
                        childOrderItemPo.getPurchaseChildOrderNo()))
                .collect(Collectors.toList());

        PurchaseOrderType purchaseOrderType = childOrderPo.getPurchaseOrderType();
        QcOriginProperty qcOriginProperty
                = qcOrderRefService.mapPurchaseOrderTypeToQcOriginProperty(purchaseOrderType);

        PurchaseCompleteQcBo purchaseCompleteQcBo
                = QcOrderBuilder.buildPurchaseCompleteQcBo(childOrderPo, qcOriginProperty, matchChildOrderItems);

        AbstractQcOrderCreator<PurchaseCompleteQcBo, QcOrderPo> qcOrderCreator
                = new PurchaseCompleteQcCreator(qcOrderDao, qcDetailDao, idGenerateService, plmRemoteService, qcOrderLogService);
        qcOrderCreator.createQcOrder(purchaseCompleteQcBo);
    }

    public LocalDate getCapacityDate(LocalDateTime promiseDate, String supplierCode) {
        final SupplierPo supplierPo = supplierDao.getBySupplierCode(supplierCode);
        if (null == supplierPo) {
            throw new BizException("供应商编码:{}找不到对应的供应商，请联系系统管理员", supplierCode);
        }
        final LocalDateTime capacityDateTime = promiseDate.minusDays(supplierPo.getLogisticsAging()).minusDays(BASE_SUPPLIER_CONSUME);
        final LocalDateTime zonedDateTime = TimeUtil.utcConvertZone(capacityDateTime, TimeZoneId.CN);
        return zonedDateTime.toLocalDate();
    }

    public List<PurchaseVo> getPurchaseVoByNo(PurchaseChildNoListDto dto) {
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(dto.getNoList());
        final List<PurchaseChildOrderChangePo> purchaseChildOrderChangePoList = purchaseChildOrderChangeDao.getListByChildNoList(dto.getNoList());
        final Map<String, PurchaseChildOrderChangePo> purchaseChildOrderNoPoMap = purchaseChildOrderChangePoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderChangePo::getPurchaseChildOrderNo, Function.identity()));
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNoList(dto.getNoList());
        final Map<String, PurchaseChildOrderItemPo> purchaseChildOrderNoItemPoMap = purchaseChildOrderItemPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderItemPo::getPurchaseChildOrderNo,
                        Function.identity(), (item1, item2) -> item1));

        final List<PurchaseVo> resultList = new ArrayList<>();
        for (PurchaseChildOrderPo purchaseChildOrderPo : purchaseChildOrderPoList) {
            final PurchaseVo purchaseVo = PurchaseChildConverter.INSTANCE.convert(purchaseChildOrderPo);
            final PurchaseChildOrderChangePo purchaseChildOrderChangePo = purchaseChildOrderNoPoMap.get(purchaseChildOrderPo.getPurchaseChildOrderNo());
            purchaseVo.setWarehousingTime(purchaseChildOrderChangePo.getWarehousingTime());
            purchaseVo.setDeliverTime(purchaseChildOrderChangePo.getDeliverTime());
            final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildOrderNoItemPoMap.get(purchaseChildOrderPo.getPurchaseChildOrderNo());
            purchaseVo.setPurchaseChildOrderItemId(purchaseChildOrderItemPo.getPurchaseChildOrderItemId());
            purchaseVo.setSku(purchaseChildOrderItemPo.getSku());
            purchaseVo.setSkuBatchCode(purchaseChildOrderItemPo.getSkuBatchCode());
            purchaseVo.setPurchaseCnt(purchaseChildOrderItemPo.getPurchaseCnt());
            purchaseVo.setInitPurchaseCnt(purchaseChildOrderItemPo.getInitPurchaseCnt());
            purchaseVo.setDeliverCnt(purchaseChildOrderItemPo.getDeliverCnt());
            purchaseVo.setQualityGoodsCnt(purchaseChildOrderItemPo.getQualityGoodsCnt());
            purchaseVo.setDefectiveGoodsCnt(purchaseChildOrderItemPo.getDefectiveGoodsCnt());
            purchaseVo.setUndeliveredCnt(purchaseChildOrderItemPo.getUndeliveredCnt());

            resultList.add(purchaseVo);
        }

        return resultList;
    }

    public List<PurchaseSkuSupplierItemVo> getSupplierDateDetail(PurchaseSkuSupplierDto dto) {
        final List<String> purchaseChildNoList = dto.getPurchaseSkuSupplierItemList().stream().map(PurchaseSkuSupplierItemDto::getPurchaseChildOrderNo).collect(Collectors.toList());

        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(purchaseChildNoList);
        final Map<String, String> purchaseChildNoSpuMap = purchaseChildOrderPoList.stream().collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, PurchaseChildOrderPo::getSpu));
        final Map<String, PurchaseChildOrderPo> purchaseChildNoPoMap = purchaseChildOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));

        final List<SupplierUrgentDto> supplierUrgentDtoList = new ArrayList<>();
        final List<SupplierTimelinessDto> supplierTimelinessDtoList = new ArrayList<>();
        dto.getPurchaseSkuSupplierItemList().forEach(itemDto -> {
            final SupplierUrgentDto supplierUrgentDto = new SupplierUrgentDto();
            supplierUrgentDto.setSupplierCode(itemDto.getSupplierCode());
            supplierUrgentDto.setSku(itemDto.getSku());

            final long daysBetween = ChronoUnit.DAYS.between(itemDto.getCreateTime(), itemDto.getExpectedOnShelvesDate());
            supplierUrgentDto.setTimeValue(new BigDecimal(daysBetween));
            supplierUrgentDtoList.add(supplierUrgentDto);

            final SupplierTimelinessDto supplierTimelinessDto = new SupplierTimelinessDto();
            supplierTimelinessDto.setSupplierCode(itemDto.getSupplierCode());
            supplierTimelinessDto.setSku(itemDto.getSku());
            supplierTimelinessDtoList.add(supplierTimelinessDto);
        });

        final List<SupplierUrgentVo> supplierUrgentList = supplierBaseService.getUrgentVoListBySupplierCode(supplierUrgentDtoList);
        final List<SupplierTimelinessVo> supplierTimelinessList = supplierBaseService.getTimelinessVoListBySupplierCode(supplierTimelinessDtoList);
        final Map<String, SupplierUrgentStatus> supplierSkuUrgentStatusMap = supplierUrgentList.stream().collect(Collectors.toMap(vo -> vo.getSupplierCode() + vo.getSku(), SupplierUrgentVo::getSupplierUrgentStatus, (item1, item2) -> item1));
        final Map<String, BigDecimal> supplierSkuTimelinessMap = supplierTimelinessList.stream().collect(Collectors.toMap(vo -> vo.getSupplierCode() + vo.getSku(), SupplierTimelinessVo::getTimelinessValue, (item1, item2) -> item1));

        return dto.getPurchaseSkuSupplierItemList().stream().map(itemDto -> {
            final PurchaseSkuSupplierItemVo purchaseSkuSupplierItemVo = new PurchaseSkuSupplierItemVo();
            purchaseSkuSupplierItemVo.setSupplierCode(itemDto.getSupplierCode());
            purchaseSkuSupplierItemVo.setSku(itemDto.getSku());

            String purchaseChildOrderNo = itemDto.getPurchaseChildOrderNo();
            purchaseSkuSupplierItemVo.setPurchaseChildOrderNo(purchaseChildOrderNo);
            final String spu = purchaseChildNoSpuMap.get(purchaseChildOrderNo);
            // 若spu为空，则为辅料类型
            if (StringUtils.isBlank(spu)) {
                purchaseSkuSupplierItemVo.setSupplierUrgentStatus(SupplierUrgentStatus.NOT_URGENT);
            } else {
                purchaseSkuSupplierItemVo.setSupplierUrgentStatus(supplierSkuUrgentStatusMap.get(itemDto.getSupplierCode() + itemDto.getSku()));
            }
            final BigDecimal timeliness = supplierSkuTimelinessMap.get(itemDto.getSupplierCode() + itemDto.getSku());
            if (null != timeliness) {
                final LocalDateTime deliverDate = itemDto.getExpectedOnShelvesDate().minusDays(timeliness.intValue());
                purchaseSkuSupplierItemVo.setDeliverDate(deliverDate);
            }

            final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildNoPoMap.get(purchaseChildOrderNo);
            if (null != purchaseChildOrderPo) {
                purchaseSkuSupplierItemVo.setIsUrgentOrder(purchaseChildOrderPo.getIsUrgentOrder());
                purchaseSkuSupplierItemVo.setOrderSource(purchaseChildOrderPo.getOrderSource());
            }
            return purchaseSkuSupplierItemVo;
        }).collect(Collectors.toList());
    }
}
