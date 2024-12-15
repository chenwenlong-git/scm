package com.hete.supply.scm.server.scm.purchase.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseDeliverListDto;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseProductSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseSearchNewDto;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.entity.vo.*;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.SdaRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.purchase.converter.PurchaseConverter;
import com.hete.supply.scm.server.scm.purchase.converter.PurchaseDeliverConverter;
import com.hete.supply.scm.server.scm.purchase.dao.*;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseSearchBo;
import com.hete.supply.scm.server.scm.purchase.entity.po.*;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseProductSearchVo;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseBaseService;
import com.hete.supply.scm.server.scm.qc.dao.QcDetailDao;
import com.hete.supply.scm.server.scm.qc.dao.QcReceiveOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcReceiveOrderPo;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierProductCompareDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import com.hete.supply.scm.server.supplier.purchase.service.base.PurchaseDeliverBaseService;
import com.hete.supply.scm.server.supplier.purchase.service.base.PurchaseReturnBaseService;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.entry.entity.dto.ReceiveOrderGetDto;
import com.hete.supply.wms.api.entry.entity.vo.ReceiveOrderForScmVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.TimeZoneId;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/12/12 15:40
 */
@Service
@RequiredArgsConstructor
public class PurchaseExportService {
    private final PurchaseParentOrderDao purchaseParentOrderDao;
    private final PurchaseBaseService purchaseBaseService;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final AuthBaseService authBaseService;
    private final PlmRemoteService plmRemoteService;
    private final SupplierProductCompareDao supplierProductCompareDao;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;
    private final PurchaseReturnBaseService purchaseReturnOrderBaseService;
    private final PurchaseDeliverBaseService purchaseDeliverBaseService;
    private final PurchaseReturnOrderDao purchaseReturnOrderDao;
    private final PurchaseDeliverOrderDao purchaseDeliverOrderDao;
    private final WmsRemoteService wmsRemoteService;
    private final PurchaseChildOrderChangeDao purchaseChildOrderChangeDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final SdaRemoteService sdaRemoteService;
    private final PurchaseChildOrderRawDao purchaseChildOrderRawDao;
    private final QcReceiveOrderDao qcReceiveOrderDao;
    private final QcDetailDao qcDetailDao;
    private final PurchaseChildOrderRawDeliverDao purchaseChildOrderRawDeliverDao;


    private final static BigDecimal PERCENTAGE_MULTIPLIER = new BigDecimal("100");
    private final static Integer EXPORT_DELIVER_ORDER_START_MONTH = 2;
    private final static String SUPPLIER_WAREHOUSE = "供应商仓";


    public Integer getChildExportTotals(PurchaseProductSearchDto dto) {
        //条件过滤
        if (null == purchaseBaseService.getSearchPurchaseChildWhere(dto)) {
            return 0;
        }
        return purchaseChildOrderDao.getChildExportTotals(dto);
    }

    public Integer getSupplierChildExportTotals(PurchaseProductSearchDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        // 供应商可以看到的状态限制
        if (CollectionUtils.isEmpty(dto.getPurchaseOrderStatusList())) {
            dto.setPurchaseOrderStatusList(PurchaseOrderStatus.getSupplierAllStatusList());
        }
        return this.getChildExportTotals(dto);
    }

    public CommonPageResult.PageInfo<PurchaseChildExportVo> getChildExportList(PurchaseProductSearchDto dto) {
        //条件过滤
        if (null == purchaseBaseService.getSearchPurchaseChildWhere(dto)) {
            return new CommonPageResult.PageInfo<>();
        }
        final CommonPageResult.PageInfo<PurchaseChildExportVo> childExportList = purchaseChildOrderDao.getChildExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<PurchaseChildExportVo> records = childExportList.getRecords();
        final List<String> purchaseChildOrderNoList = records.stream()
                .map(PurchaseChildExportVo::getPurchaseChildOrderNo)
                .collect(Collectors.toList());

        //查询退货单
        List<PurchaseReturnOrderPo> purchaseReturnOrderPoList = purchaseReturnOrderDao.getListByPurchaseChildNoList(purchaseChildOrderNoList);
        Map<String, List<PurchaseReturnOrderPo>> purchaseReturnOrderPoMap = purchaseReturnOrderPoList.stream()
                .collect(Collectors.groupingBy(PurchaseReturnOrderPo::getPurchaseChildOrderNo));

        //查询采购子单详情
        List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNoList(purchaseChildOrderNoList);
        Map<String, List<PurchaseChildOrderItemPo>> purchaseChildOrderItemPoMap = purchaseChildOrderItemPoList.stream()
                .collect(Collectors.groupingBy(PurchaseChildOrderItemPo::getPurchaseChildOrderNo));

        //获取sku名称
        List<String> skuList = purchaseChildOrderItemPoList.stream()
                .map(PurchaseChildOrderItemPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        // 获取平台名称
        final List<String> platCodeList = records.stream()
                .map(PurchaseChildExportVo::getPlatform)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);
        records.forEach(record -> {
            final PurchaseOrderStatus purchaseOrderStatus = PurchaseOrderStatus.valueOf(record.getPurchaseOrderStatus());
            record.setPurchaseOrderStatus(purchaseOrderStatus.getRemark());
            record.setPlatform(platCodeNameMap.get(record.getPlatform()));
            record.setTimelyDeliveryRateStr(record.getTimelyDeliveryRate().multiply(PERCENTAGE_MULTIPLIER) + "%");

            List<PurchaseReturnOrderPo> purchaseReturnOrderPos = purchaseReturnOrderPoMap.get(record.getPurchaseChildOrderNo());
            int returnCnt = Optional.ofNullable(purchaseReturnOrderPos)
                    .orElse(Collections.emptyList())
                    .stream()
                    .mapToInt(PurchaseReturnOrderPo::getRealityReturnCnt)
                    .sum();
            record.setReturnCnt(returnCnt);

            List<PurchaseChildOrderItemPo> purchaseChildOrderItemPos = purchaseChildOrderItemPoMap.get(record.getPurchaseChildOrderNo());
            PurchaseChildOrderItemPo purchaseChildOrderItemPo = Optional.ofNullable(purchaseChildOrderItemPos)
                    .orElse(Collections.emptyList())
                    .stream()
                    .findFirst()
                    .orElse(null);
            if (purchaseChildOrderItemPo != null) {
                record.setSku(purchaseChildOrderItemPo.getSku());
                record.setSkuEncode(skuEncodeMap.get(record.getSku()));
            }


        });
        return childExportList;
    }

    public CommonPageResult.PageInfo<PurchaseChildExportVo> getSupplierChildExportList(PurchaseProductSearchDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        // 供应商可以看到的状态限制
        if (CollectionUtils.isEmpty(dto.getPurchaseOrderStatusList())) {
            dto.setPurchaseOrderStatusList(PurchaseOrderStatus.getSupplierAllStatusList());
        }
        return this.getChildExportList(dto);
    }

    public Integer getSkuChildExportTotals(PurchaseProductSearchDto dto) {
        //条件过滤
        if (null == purchaseBaseService.getSearchPurchaseChildWhere(dto)) {
            return 0;
        }
        return purchaseChildOrderDao.getSkuChildExportTotals(dto);
    }

    public Integer getSupplierSkuChildExportTotals(PurchaseProductSearchDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        // 供应商可以看到的状态限制
        if (CollectionUtils.isEmpty(dto.getPurchaseOrderStatusList())) {
            dto.setPurchaseOrderStatusList(PurchaseOrderStatus.getSupplierAllStatusList());
        }
        return this.getSkuChildExportTotals(dto);
    }

    public CommonPageResult.PageInfo<PurchaseChildSkuExportVo> getSkuChildExportList(PurchaseProductSearchDto dto) {
        //条件过滤
        if (null == purchaseBaseService.getSearchPurchaseChildWhere(dto)) {
            return new CommonPageResult.PageInfo<>();
        }
        final CommonPageResult.PageInfo<PurchaseChildSkuExportVo> skuChildExportList = purchaseChildOrderDao.getSkuChildExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize(), false), dto);
        final List<PurchaseChildSkuExportVo> records = skuChildExportList.getRecords();
        final List<String> skuList = records.stream()
                .map(PurchaseChildSkuExportVo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> skuCategoriesMap = plmRemoteService.getSkuSecondCategoriesCnMapBySkuList(skuList);

        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        Map<String, SupplierProductComparePo> supplierProductCompareMap = supplierProductCompareDao.getBatchSkuMap(skuList);

        //查询发货单
        List<String> purchaseChildOrderNoList = records.stream().map(PurchaseChildSkuExportVo::getPurchaseChildOrderNo).distinct().collect(Collectors.toList());
        final List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseBaseService.getListByChildOrderNoListNotStatus(purchaseChildOrderNoList, DeliverOrderStatus.DELETED);
        Map<String, List<PurchaseDeliverOrderPo>> purchaseDeliverOrderPoMap = purchaseDeliverOrderPoList.stream()
                .collect(Collectors.groupingBy(PurchaseDeliverOrderPo::getPurchaseChildOrderNo));
        final List<String> purchaseDeliverOrderNoList = purchaseDeliverOrderPoList.stream()
                .map(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo)
                .distinct()
                .collect(Collectors.toList());
        // 发货单与质检单的关联关系
        final List<QcReceiveOrderPo> qcReceiveOrderPoList = qcReceiveOrderDao.getListByScmBizNoList(purchaseDeliverOrderNoList);
        final Map<String, List<String>> purchaseDeliverNoQcNoListMap = qcReceiveOrderPoList.stream()
                .collect(Collectors.groupingBy(QcReceiveOrderPo::getScmBizNo,
                        Collectors.mapping(QcReceiveOrderPo::getQcOrderNo, Collectors.toList())));
        // 查找所有质检详情
        final List<String> qcOrderNoList = qcReceiveOrderPoList.stream()
                .map(QcReceiveOrderPo::getQcOrderNo)
                .distinct()
                .collect(Collectors.toList());
        final List<QcDetailPo> qcOrderPoList = qcDetailDao.getListByQcOrderNoList(qcOrderNoList);
        final Map<String, Integer> qcOrderNoPassAmountMap = qcOrderPoList.stream()
                .collect(Collectors.groupingBy(QcDetailPo::getQcOrderNo, Collectors.summingInt(QcDetailPo::getPassAmount)));
        final Map<String, Integer> qcOrderNoNotPassAmountMap = qcOrderPoList.stream()
                .collect(Collectors.groupingBy(QcDetailPo::getQcOrderNo, Collectors.summingInt(QcDetailPo::getNotPassAmount)));

        // 获取平台名称
        final List<String> platCodeList = records.stream()
                .map(PurchaseChildSkuExportVo::getPlatform)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);
        records.forEach(record -> {
            final PurchaseOrderStatus purchaseOrderStatus = PurchaseOrderStatus.valueOf(record.getPurchaseOrderStatus());
            record.setPurchaseOrderStatus(purchaseOrderStatus.getRemark());
            if (StringUtils.isNotBlank(record.getDiscountType())) {
                record.setDiscountType(DiscountType.valueOf(record.getDiscountType()).getRemark());
            }
            record.setPlatform(platCodeNameMap.get(record.getPlatform()));
            record.setCategoryName(skuCategoriesMap.get(record.getSku()));
            record.setSkuEncode(skuEncodeMap.get(record.getSku()));
            final SupplierProductComparePo supplierProductComparePo = supplierProductCompareMap.get(record.getSupplierCode() + record.getSku());
            if (null != supplierProductComparePo) {
                record.setSupplierProductName(supplierProductComparePo.getSupplierProductName());
            }

            record.setTimelyDeliveryRateStr(record.getTimelyDeliveryRate().multiply(PERCENTAGE_MULTIPLIER) + "%");
            record.setPromiseDateChgStr(record.getPromiseDateChg().getValue());
            //发货单组成字符串
            List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList1 = purchaseDeliverOrderPoMap.get(record.getPurchaseChildOrderNo());
            if (CollectionUtils.isNotEmpty(purchaseDeliverOrderPoList1)) {
                final List<String> purchaseDeliverOrderNoList1 = purchaseDeliverOrderPoList1.stream()
                        .map(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo)
                        .distinct()
                        .collect(Collectors.toList());
                String purchaseDeliverOrderNo = String.join(",", purchaseDeliverOrderNoList1);
                record.setPurchaseDeliverOrderNo(purchaseDeliverOrderNo);
                final List<String> purQcOrderNoList = purchaseDeliverOrderNoList1.stream()
                        .map(purchaseDeliverNoQcNoListMap::get)
                        .filter(Objects::nonNull)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                final int qualityGoodsCnt = purQcOrderNoList.stream()
                        .mapToInt(qcOrderNoPassAmountMap::get)
                        .sum();

                final int defectiveGoodsCnt = purQcOrderNoList.stream()
                        .mapToInt(qcOrderNoNotPassAmountMap::get)
                        .sum();

                record.setQualityGoodsCnt(qualityGoodsCnt);
                record.setDefectiveGoodsCnt(defectiveGoodsCnt);
            }

        });
        return skuChildExportList;
    }

    public CommonPageResult.PageInfo<PurchaseChildSkuExportVo> getSupplierSkuChildExportList(PurchaseProductSearchDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        // 供应商可以看到的状态限制
        if (CollectionUtils.isEmpty(dto.getPurchaseOrderStatusList())) {
            dto.setPurchaseOrderStatusList(PurchaseOrderStatus.getSupplierAllStatusList());
        }
        return this.getSkuChildExportList(dto);
    }

    public Integer getRawChildExportTotals(PurchaseProductSearchDto dto) {
        //条件过滤
        if (null == purchaseBaseService.getSearchPurchaseChildWhere(dto)) {
            return 0;
        }
        dto.setPurchaseRawBizTypeList(Collections.singletonList(PurchaseRawBizType.DEMAND));
        return purchaseChildOrderDao.getRawChildExportTotals(dto);
    }

    public CommonPageResult.PageInfo<PurchaseChildRawExportVo> getRawChildExportList(PurchaseProductSearchDto dto) {
        //条件过滤
        if (null == purchaseBaseService.getSearchPurchaseChildWhere(dto)) {
            return new CommonPageResult.PageInfo<>();
        }
        dto.setPurchaseRawBizTypeList(Collections.singletonList(PurchaseRawBizType.DEMAND));
        final CommonPageResult.PageInfo<PurchaseChildRawExportVo> skuChildExportList = purchaseChildOrderDao.getRawChildExportList(PageDTO.of(dto.getPageNo(),
                dto.getPageSize(), false), dto);
        final List<PurchaseChildRawExportVo> records = skuChildExportList.getRecords();
        final List<String> purchaseChildOrderNoList = records.stream()
                .map(PurchaseChildRawExportVo::getPurchaseChildOrderNo)
                .distinct()
                .collect(Collectors.toList());
        final List<PurchaseChildOrderRawPo> actualRawPoList = purchaseChildOrderRawDao.getListByChildNoList(purchaseChildOrderNoList, PurchaseRawBizType.ACTUAL_DELIVER);
        // 获取实际发货数
        final Map<String, List<PurchaseChildOrderRawPo>> purchaseChildNoSkuRawPoMap = actualRawPoList.stream()
                .filter(rawPo -> PurchaseRawBizType.ACTUAL_DELIVER.equals(rawPo.getPurchaseRawBizType()))
                .filter(rawPo -> StringUtils.isBlank(rawPo.getSkuBatchCode()))
                .collect(Collectors.groupingBy(rawPo -> rawPo.getPurchaseChildOrderNo() + rawPo.getRawSupplier().getRemark() + rawPo.getSku()));
        final List<PurchaseChildOrderRawDeliverPo> purchaseChildOrderRawDeliverPoList = purchaseChildOrderRawDeliverDao.getListByChildOrderNoList(purchaseChildOrderNoList);
        final Map<String, List<PurchaseChildOrderRawDeliverPo>> purchaseChildNoDeliverPoListMap = purchaseChildOrderRawDeliverPoList.stream()
                .collect(Collectors.groupingBy(po -> po.getPurchaseChildOrderNo() + po.getSku()));


        records.forEach(record -> {
            record.setPurchaseOrderStatus(PurchaseOrderStatus.valueOf(record.getPurchaseOrderStatus()).getRemark());
            // 来源于供应商类型的重新赋值
            if (RawSupplier.SUPPLIER.equals(record.getRawSupplier())) {
                record.setRawWarehouseName(record.getRawWarehouseName() + SUPPLIER_WAREHOUSE);
            }
            final List<PurchaseChildOrderRawPo> skuRawPoList = purchaseChildNoSkuRawPoMap.get(record.getPurchaseChildOrderNo() + record.getRawSupplier().getRemark() + record.getSku());
            if (CollectionUtils.isNotEmpty(skuRawPoList)) {
                record.setDeliveryCnt(skuRawPoList.stream().mapToInt(PurchaseChildOrderRawPo::getDeliveryCnt).sum());
                record.setDispenseCnt(skuRawPoList.stream().mapToInt(PurchaseChildOrderRawPo::getDispenseCnt).sum());
                final PurchaseChildOrderRawPo purchaseChildOrderRawPo = skuRawPoList.get(0);
                if (StringUtils.isNotBlank(purchaseChildOrderRawPo.getRawWarehouseName())) {
                    if (RawSupplier.SUPPLIER.equals(purchaseChildOrderRawPo.getRawSupplier())) {
                        purchaseChildOrderRawPo.setRawWarehouseName(purchaseChildOrderRawPo.getRawWarehouseName() + SUPPLIER_WAREHOUSE);
                    }
                    record.setRawWarehouseName(purchaseChildOrderRawPo.getRawWarehouseName());
                }
            }
            StringBuilder result = new StringBuilder();
            final List<PurchaseChildOrderRawDeliverPo> purchaseChildOrderRawDeliverPoList1 = purchaseChildNoDeliverPoListMap.get(record.getPurchaseChildOrderNo() + record.getSku());
            Optional.ofNullable(purchaseChildOrderRawDeliverPoList1)
                    .orElse(Collections.emptyList())
                    .stream()
                    .filter(po -> StringUtils.isNotBlank(po.getPurchaseRawDeliverOrderNo()))
                    .filter(po -> po.getRawSupplier().equals(record.getRawSupplier()))
                    .forEach(po -> result.append(po.getPurchaseRawDeliverOrderNo()).append("*").append(po.getDispenseCnt()).append(","));
            record.setDeliveryMsg(result.toString());
        });

        final List<String> demandRawSupplierSkuList = records.stream()
                .map(rawPo -> rawPo.getPurchaseChildOrderNo() + rawPo.getRawSupplier().getRemark() + rawPo.getSku())
                .distinct()
                .collect(Collectors.toList());

        final Map<String, String> purchaseChildNoStatusMap = records.stream()
                .collect(Collectors.toMap(PurchaseChildRawExportVo::getPurchaseChildOrderNo,
                        PurchaseChildRawExportVo::getPurchaseOrderStatus, (item1, item2) -> item1));

        // 实发原料且不在需求类型sku里面的属于补充原料
        final List<PurchaseChildOrderRawPo> supplyRawSkuList = actualRawPoList.stream()
                .filter(rawPo -> PurchaseRawBizType.ACTUAL_DELIVER.equals(rawPo.getPurchaseRawBizType()))
                .filter(rawPo -> StringUtils.isBlank(rawPo.getSkuBatchCode()))
                .filter(rawPo -> !demandRawSupplierSkuList.contains(rawPo.getPurchaseChildOrderNo() + rawPo.getRawSupplier().getRemark() + rawPo.getSku()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(supplyRawSkuList)) {
            return skuChildExportList;
        }

        final List<PurchaseChildRawExportVo> supplyRawVoList = supplyRawSkuList.stream().map(rawPo -> {
            final PurchaseChildRawExportVo purchaseChildRawExportVo = new PurchaseChildRawExportVo();
            purchaseChildRawExportVo.setPurchaseParentOrderNo(rawPo.getPurchaseParentOrderNo());
            purchaseChildRawExportVo.setPurchaseChildOrderNo(rawPo.getPurchaseChildOrderNo());
            purchaseChildRawExportVo.setPurchaseOrderStatus(purchaseChildNoStatusMap.get(rawPo.getPurchaseChildOrderNo()));
            purchaseChildRawExportVo.setSku(rawPo.getSku());
            purchaseChildRawExportVo.setDeliveryCnt(rawPo.getDeliveryCnt());
            purchaseChildRawExportVo.setDispenseCnt(rawPo.getDispenseCnt());
            purchaseChildRawExportVo.setRawWarehouseName(rawPo.getRawWarehouseName());
            StringBuilder result = new StringBuilder();
            final List<PurchaseChildOrderRawDeliverPo> purchaseChildOrderRawDeliverPoList1 = purchaseChildNoDeliverPoListMap.get(purchaseChildRawExportVo.getPurchaseChildOrderNo() + purchaseChildRawExportVo.getSku());
            Optional.ofNullable(purchaseChildOrderRawDeliverPoList1)
                    .orElse(Collections.emptyList())
                    .stream()
                    .filter(po -> StringUtils.isNotBlank(po.getPurchaseRawDeliverOrderNo()))
                    .filter(po -> po.getRawSupplier().equals(rawPo.getRawSupplier()))
                    .forEach(po -> result.append(po.getPurchaseRawDeliverOrderNo()).append("*").append(po.getDispenseCnt()).append(","));
            purchaseChildRawExportVo.setDeliveryMsg(result.toString());
            return purchaseChildRawExportVo;
        }).collect(Collectors.toList());

        records.addAll(supplyRawVoList);
        // 按照单号进行排序
        final List<PurchaseChildRawExportVo> sortedRecords = records.stream()
                .sorted(Comparator.comparing(PurchaseChildRawExportVo::getPurchaseChildOrderNo).reversed())
                .collect(Collectors.toList());
        skuChildExportList.setRecords(sortedRecords);

        return skuChildExportList;
    }

    public Integer exportPurchaseParentTotals(PurchaseSearchNewDto dto) {

        PurchaseSearchBo purchaseSearchBo = purchaseBaseService.getItemParentNoList(dto);
        if (purchaseSearchBo == null) {
            throw new ParamIllegalException("导出数据为空");
        }
        return purchaseParentOrderDao.exportPurchaseParentTotals(dto, purchaseSearchBo);
    }

    public CommonResult<ExportationListResultBo<PurchaseParentExportVo>> exportPurchaseParent(PurchaseSearchNewDto dto) {
        ExportationListResultBo<PurchaseParentExportVo> resultBo = new ExportationListResultBo<>();
        PurchaseSearchBo purchaseSearchBo = purchaseBaseService.getItemParentNoList(dto);
        if (purchaseSearchBo == null) {
            return CommonResult.success(resultBo);
        }
        final CommonPageResult.PageInfo<PurchaseParentExportVo> pageInfo = purchaseParentOrderDao.exportPurchaseParent(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto, purchaseSearchBo);
        final List<PurchaseParentExportVo> records = pageInfo.getRecords();
        // 获取平台名称
        final List<String> platCodeList = records.stream()
                .map(PurchaseParentExportVo::getPlatform)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);

        records.forEach(record -> {
            record.setPurchaseDemandTypeStr(record.getPurchaseDemandType().getRemark());
            record.setPurchaseParentOrderStatusStr(record.getPurchaseParentOrderStatus().getRemark());
            record.setSkuTypeStr(record.getSkuType().getRemark());
            record.setPlatform(platCodeNameMap.get(record.getPlatform()));
            record.setCreateTimeStr(ScmTimeUtil.localDateTimeToStr(record.getCreateTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
        });
        resultBo.setRowDataList(records);
        return CommonResult.success(resultBo);
    }

    public CommonResult<ExportationListResultBo<PurchaseChildNewExportVo>> exportPurchaseChild(PurchaseSearchNewDto dto) {
        ExportationListResultBo<PurchaseChildNewExportVo> resultBo = new ExportationListResultBo<>();
        PurchaseSearchBo purchaseSearchBo = purchaseBaseService.getItemParentNoList(dto);
        if (purchaseSearchBo == null) {
            return CommonResult.success(resultBo);
        }
        dto.setIsExistsChild(BooleanType.TRUE);
        final CommonPageResult.PageInfo<PurchaseParentExportVo> pageInfo = purchaseParentOrderDao.exportPurchaseParent(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto, purchaseSearchBo);

        final List<PurchaseParentExportVo> records = pageInfo.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }


        final List<String> purchaseParentNoList = records.stream()
                .map(PurchaseParentExportVo::getPurchaseParentOrderNo)
                .collect(Collectors.toList());
        final Map<String, PurchaseParentExportVo> purchaseParentNoMap = records.stream()
                .collect(Collectors.toMap(PurchaseParentExportVo::getPurchaseParentOrderNo, Function.identity()));

        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByParentNoList(purchaseParentNoList);
        final Map<String, PurchaseChildOrderPo> purchaseChildNoMap = purchaseChildOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));

        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByParentNoList(purchaseParentNoList);
        final List<String> skuList = purchaseChildOrderItemPoList.stream()
                .map(PurchaseChildOrderItemPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        final List<String> purchaseChildOrderNoList = purchaseChildOrderPoList.stream()
                .map(PurchaseChildOrderPo::getPurchaseChildOrderNo)
                .collect(Collectors.toList());
        final List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverBaseService.getDeliverPoByPurchaseNo(purchaseChildOrderNoList);
        final Map<String, String> deliverChildNoMap = purchaseDeliverOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo,
                        PurchaseDeliverOrderPo::getPurchaseChildOrderNo));
        final List<String> purchaseDeliverOrderNoList = purchaseDeliverOrderPoList.stream()
                .map(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo)
                .collect(Collectors.toList());
        final List<PurchaseReturnOrderPo> purchaseReturnOrderPoList = purchaseReturnOrderDao.getListByReturnBizNoList(purchaseDeliverOrderNoList);
        final Map<String, List<String>> deliverNoReturnNoMap = purchaseReturnOrderPoList.stream()
                .collect(Collectors.groupingBy(PurchaseReturnOrderPo::getReturnBizNo, Collectors.mapping(PurchaseReturnOrderPo::getReturnOrderNo, Collectors.toList())));
        final Map<String, Integer> purchaseNoReturnCntMap = purchaseReturnOrderBaseService.getChildOrderReturnCntByReturnBizNoMap(deliverChildNoMap, deliverNoReturnNoMap);
        final List<PurchaseChildOrderChangePo> purchaseChildOrderChangePoList = purchaseChildOrderChangeDao.getListByChildNoList(purchaseChildOrderNoList);
        final Map<String, LocalDateTime> purchaseChildNoPlaceOrderTimeMap = purchaseChildOrderChangePoList.stream().collect(Collectors.toMap(PurchaseChildOrderChangePo::getPurchaseChildOrderNo,
                PurchaseChildOrderChangePo::getPlaceOrderTime));
        // 获取平台名称
        final List<String> platCodeList = records.stream()
                .map(PurchaseParentExportVo::getPlatform)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);

        final List<PurchaseChildNewExportVo> purchaseChildNewExportList = purchaseChildOrderItemPoList.stream()
                .filter(itemPo -> null != purchaseChildNoMap.get(itemPo.getPurchaseChildOrderNo()))
                .map(itemPo -> {
                    final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildNoMap.get(itemPo.getPurchaseChildOrderNo());
                    final PurchaseChildNewExportVo purchaseChildNewExportVo = new PurchaseChildNewExportVo();
                    purchaseChildNewExportVo.setPurchaseParentOrderNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
                    purchaseChildNewExportVo.setPurchaseParentOrderStatusStr(purchaseParentNoMap.get(itemPo.getPurchaseParentOrderNo()).getPurchaseParentOrderStatus().getRemark());
                    purchaseChildNewExportVo.setPurchaseDemandTypeStr(purchaseParentNoMap.get(itemPo.getPurchaseParentOrderNo()).getPurchaseDemandType().getRemark());
                    purchaseChildNewExportVo.setPlatform(platCodeNameMap.get(purchaseParentNoMap.get(itemPo.getPurchaseParentOrderNo()).getPlatform()));
                    purchaseChildNewExportVo.setPurchaseChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
                    purchaseChildNewExportVo.setPurchaseOrderStatusStr(purchaseChildOrderPo.getPurchaseOrderStatus().getRemark());
                    purchaseChildNewExportVo.setPurchaseOrderType(purchaseChildOrderPo.getPurchaseOrderType().getRemark());
                    purchaseChildNewExportVo.setPurchaseBizType(purchaseChildOrderPo.getPurchaseBizType().getRemark());
                    purchaseChildNewExportVo.setSku(itemPo.getSku());
                    purchaseChildNewExportVo.setSkuEncode(skuEncodeMap.get(itemPo.getSku()));
                    purchaseChildNewExportVo.setPurchaseCnt(itemPo.getPurchaseCnt());
                    purchaseChildNewExportVo.setUndeliveredCnt(itemPo.getUndeliveredCnt());
                    purchaseChildNewExportVo.setDeliverCnt(itemPo.getDeliverCnt());
                    purchaseChildNewExportVo.setWarehousingCnt(itemPo.getQualityGoodsCnt());
                    purchaseChildNewExportVo.setQualityGoodsCnt(itemPo.getQualityGoodsCnt());
                    purchaseChildNewExportVo.setDefectiveGoodsCnt(itemPo.getDefectiveGoodsCnt());
                    purchaseChildNewExportVo.setReturnCnt(purchaseNoReturnCntMap.getOrDefault(itemPo.getPurchaseChildOrderNo(), 0));
                    purchaseChildNewExportVo.setExpectedOnShelvesDateStr(ScmTimeUtil.localDateTimeToStr(purchaseChildOrderPo.getExpectedOnShelvesDate(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                    purchaseChildNewExportVo.setDeliverDateStr(ScmTimeUtil.localDateTimeToStr(purchaseChildOrderPo.getDeliverDate(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                    purchaseChildNewExportVo.setIsOverdue(purchaseChildOrderPo.getIsOverdue() == null ? "" : purchaseChildOrderPo.getIsOverdue().getValue());
                    purchaseChildNewExportVo.setIsUrgentOrder(purchaseChildOrderPo.getIsUrgentOrder() == null ? "" : purchaseChildOrderPo.getIsUrgentOrder().getValue());
                    purchaseChildNewExportVo.setPlaceOrderTime(ScmTimeUtil.localDateTimeToStr(purchaseChildNoPlaceOrderTimeMap.get(purchaseChildOrderPo.getPurchaseChildOrderNo()), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));

                    return purchaseChildNewExportVo;
                }).collect(Collectors.toList());

        resultBo.setRowDataList(purchaseChildNewExportList);
        return CommonResult.success(resultBo);
    }

    public Integer exportPurchaseBySkuTotals(PurchaseSearchNewDto dto) {
        PurchaseSearchBo purchaseSearchBo = purchaseBaseService.getItemParentNoList(dto);
        if (purchaseSearchBo == null) {
            throw new ParamIllegalException("导出数据为空");
        }
        return purchaseParentOrderDao.exportPurchaseParentBySkuTotals(dto, purchaseSearchBo);
    }

    public CommonResult<ExportationListResultBo<PurchaseParentSkuExportVo>> exportPurchaseBySku(PurchaseSearchNewDto dto) {
        ExportationListResultBo<PurchaseParentSkuExportVo> resultBo = new ExportationListResultBo<>();
        PurchaseSearchBo purchaseSearchBo = purchaseBaseService.getItemParentNoList(dto);
        if (purchaseSearchBo == null) {
            return CommonResult.success(resultBo);
        }
        final CommonPageResult.PageInfo<PurchaseParentSkuExportVo> pageInfo = purchaseParentOrderDao.exportPurchaseParentBySku(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto, purchaseSearchBo);
        final List<PurchaseParentSkuExportVo> records = pageInfo.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }

        // 获取sku产品名称
        final List<String> skuList = records.stream()
                .map(PurchaseParentSkuExportVo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        // 获取平台名称
        final List<String> platCodeList = records.stream()
                .map(PurchaseParentSkuExportVo::getPlatform)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);
        records.forEach(record -> {
            record.setPurchaseDemandTypeStr(record.getPurchaseDemandType().getRemark());
            record.setPurchaseParentOrderStatusStr(record.getPurchaseParentOrderStatus().getRemark());
            record.setPlatform(platCodeNameMap.get(record.getPlatform()));
            record.setSkuEncode(skuEncodeMap.get(record.getSku()));
        });

        resultBo.setRowDataList(records);
        return CommonResult.success(resultBo);
    }

    public CommonResult<ExportationListResultBo<PurchaseChildDeliverExportVo>> exportPurchaseChildDeliver(PurchaseDeliverListDto dto) {
        ExportationListResultBo<PurchaseChildDeliverExportVo> resultBo = new ExportationListResultBo<>();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoMonthsAgo = now.minus(EXPORT_DELIVER_ORDER_START_MONTH, ChronoUnit.MONTHS);
        dto.setCreateTimeStart(twoMonthsAgo);

        CommonPageResult.PageInfo<PurchaseDeliverExportVo> pageList = purchaseDeliverOrderDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<PurchaseDeliverExportVo> records = pageList.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }

        List<String> purchaseDeliverOrderNoList = records.stream().map(PurchaseDeliverExportVo::getPurchaseDeliverOrderNo).distinct().collect(Collectors.toList());
        //通过发货单查询wms的信息
        ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
        receiveOrderGetDto.setScmBizNoList(purchaseDeliverOrderNoList);
        Map<String, List<ReceiveOrderForScmVo>> receiveOrderForScmVoMap = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto)
                .stream()
                .collect(Collectors.groupingBy(ReceiveOrderForScmVo::getScmBizNo));

        // 查找供应商产品名称与sku产品名称
        final List<String> skuList = records.stream()
                .map(PurchaseDeliverExportVo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        Map<String, SupplierProductComparePo> supplierProductCompareMap = supplierProductCompareDao.getBatchSkuMap(skuList);

        // 查询退货单的数据
        Map<String, List<PurchaseReturnOrderPo>> purchaseReturnOrderPoMap = purchaseReturnOrderDao.getMapByBatchReturnBizNo(purchaseDeliverOrderNoList);

        records = records.stream().peek(item -> {
            item.setSkuEncode(skuEncodeMap.get(item.getSku()));
            SupplierProductComparePo supplierProductComparePo = supplierProductCompareMap.get(item.getSupplierCode() + item.getSku());
            if (null != supplierProductComparePo) {
                item.setSupplierProductName(supplierProductComparePo.getSupplierProductName());
            }

            List<ReceiveOrderForScmVo> receiveOrderForScmVoList = receiveOrderForScmVoMap.get(item.getPurchaseDeliverOrderNo());
            if (CollectionUtil.isNotEmpty(receiveOrderForScmVoList)) {
                ReceiveOrderForScmVo receiveOrderForScmVo = receiveOrderForScmVoList.stream().filter(customer -> !WmsEnum.ReceiveOrderState.ALL_RETURN.equals(customer.getReceiveOrderState())).findAny().orElse(null);
                if (null != receiveOrderForScmVo) {
                    item.setReceiveOrderNo(receiveOrderForScmVo.getReceiveOrderNo());
                    item.setReceiveOrderStateName(null != receiveOrderForScmVo.getReceiveOrderState() ? receiveOrderForScmVo.getReceiveOrderState().getRemark() : null);
                    final int onShelvesAmount = Optional.ofNullable(receiveOrderForScmVo.getOnShelfList())
                            .orElse(Collections.emptyList())
                            .stream().mapToInt(ReceiveOrderForScmVo.OnShelfOrder::getOnShelvesAmount)
                            .sum();
                    item.setReceiveAmount(onShelvesAmount);
                }
            }
            List<PurchaseReturnOrderPo> purchaseReturnOrderPoList = purchaseReturnOrderPoMap.get(item.getPurchaseDeliverOrderNo());
            if (CollectionUtil.isNotEmpty(purchaseReturnOrderPoList)) {
                int realityReturnCnt = purchaseReturnOrderPoList.stream().filter(w -> null != w.getRealityReturnCnt()).mapToInt(PurchaseReturnOrderPo::getRealityReturnCnt).sum();
                if (realityReturnCnt != 0) {
                    item.setRealityReturnCnt(realityReturnCnt);
                }
            }
        }).collect(Collectors.toList());

        final List<String> purchaseChildOrderNoList = records.stream()
                .map(PurchaseDeliverExportVo::getPurchaseChildOrderNo)
                .collect(Collectors.toList());

        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList);

        final Map<String, PurchaseChildOrderPo> purchaseChildOrderNoPoMap = purchaseChildOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));

        // 获取平台名称
        final List<String> platCodeList = purchaseChildOrderPoList.stream()
                .map(PurchaseChildOrderPo::getPlatform)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);

        final List<PurchaseChildDeliverExportVo> dataList = records.stream()
                .map(record -> {
                    final PurchaseChildDeliverExportVo purchaseChildDeliverExportVo = PurchaseDeliverConverter.INSTANCE.convert(record);
                    purchaseChildDeliverExportVo.setDeliverOrderType(record.getDeliverOrderType().getRemark());
                    purchaseChildDeliverExportVo.setDeliverDate(ScmTimeUtil.localDateTimeToStr(record.getDeliverDate(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                    purchaseChildDeliverExportVo.setDeliverTime(ScmTimeUtil.localDateTimeToStr(record.getDeliverTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                    purchaseChildDeliverExportVo.setReceiptTime(ScmTimeUtil.localDateTimeToStr(record.getReceiptTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                    purchaseChildDeliverExportVo.setWmsWarehousingTime(ScmTimeUtil.localDateTimeToStr(record.getWmsWarehousingTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                    final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderNoPoMap.get(record.getPurchaseChildOrderNo());
                    if (null != purchaseChildOrderPo) {
                        purchaseChildDeliverExportVo.setPurchaseOrderStatus(purchaseChildOrderPo.getPurchaseOrderStatus().getRemark());
                        purchaseChildDeliverExportVo.setPlatform(platCodeNameMap.get(purchaseChildOrderPo.getPlatform()));
                        purchaseChildDeliverExportVo.setPurchaseTotal(purchaseChildOrderPo.getPurchaseTotal());
                    }

                    return purchaseChildDeliverExportVo;
                })
                .collect(Collectors.toList());
        resultBo.setRowDataList(dataList);


        return CommonResult.success(resultBo);
    }

    public Integer exportPurchaseChildDeliverTotals(PurchaseDeliverListDto dto) {
        dto = new PurchaseDeliverListDto();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoMonthsAgo = now.minus(EXPORT_DELIVER_ORDER_START_MONTH, ChronoUnit.MONTHS);
        dto.setCreateTimeStart(twoMonthsAgo);

        return purchaseDeliverOrderDao.getExportTotals(dto);
    }

    public CommonResult<ExportationListResultBo<PurchaseChildPreConfirmExportVo>> exportPurchaseChildPreConfirm(PurchaseProductSearchDto dto) {
        ExportationListResultBo<PurchaseChildPreConfirmExportVo> resultBo = new ExportationListResultBo<>();

        //条件过滤
        if (null == purchaseBaseService.getSearchPurchaseChildWhere(dto)) {
            return CommonResult.success(resultBo);
        }
        if (CollectionUtils.isEmpty(dto.getPurchaseChildOrderNoList())) {
            dto.setPurchaseChildOrderNoList(new ArrayList<>());
        }

        final CommonPageResult.PageInfo<PurchaseProductSearchVo> pageInfo = purchaseChildOrderDao.searchProductPurchase(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<PurchaseProductSearchVo> records = pageInfo.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }
        // 获取平台名称
        final List<String> platCodeList = records.stream()
                .map(PurchaseProductSearchVo::getPlatform)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);

        final List<PurchaseChildPreConfirmExportVo> purchaseChildPreConfirmExportVoList = PurchaseConverter.purchaseProductSearchVoToExportVo(records, platCodeNameMap);
        final List<String> purchaseChildNoList = records.stream()
                .map(PurchaseProductSearchVo::getPurchaseChildOrderNo)
                .collect(Collectors.toList());
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNoList(purchaseChildNoList);
        //组装数据获取供应商产品名称
        final List<String> skuList = purchaseChildOrderItemPoList.stream()
                .map(PurchaseChildOrderItemPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        Map<String, SupplierProductComparePo> supplierProductCompareMap = supplierProductCompareDao.getBatchSkuMap(skuList);
        Map<String, PurchaseProductSearchVo> purchaseProductSearchVoMap = records.stream()
                .collect(Collectors.toMap(PurchaseProductSearchVo::getPurchaseChildOrderNo, Function.identity()));
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        final Map<String, List<PurchaseSkuCntVo>> purchaseChildNoSkuMap = purchaseChildOrderItemPoList.stream()
                .collect(Collectors.groupingBy(PurchaseChildOrderItemPo::getPurchaseChildOrderNo,
                        Collectors.mapping(item -> {
                            final PurchaseSkuCntVo purchaseSkuCntVo = new PurchaseSkuCntVo();
                            purchaseSkuCntVo.setSkuCode(item.getSku());
                            purchaseSkuCntVo.setSkuCnt(item.getPurchaseCnt());
                            PurchaseProductSearchVo purchaseProductVo = purchaseProductSearchVoMap.get(item.getPurchaseChildOrderNo());
                            if (null != purchaseProductVo) {
                                SupplierProductComparePo supplierProductComparePo = supplierProductCompareMap.get(purchaseProductVo.getSupplierCode() + item.getSku());
                                if (null != supplierProductComparePo) {
                                    purchaseSkuCntVo.setSupplierProductName(supplierProductComparePo.getSupplierProductName());
                                }
                            }
                            purchaseSkuCntVo.setSkuEncode(skuEncodeMap.get(item.getSku()));
                            return purchaseSkuCntVo;
                        }, Collectors.toList())));

        purchaseChildPreConfirmExportVoList.forEach(vo -> {
            final List<PurchaseSkuCntVo> purchaseSkuCntVoList = purchaseChildNoSkuMap.get(vo.getPurchaseChildOrderNo());
            if (CollectionUtils.isEmpty(purchaseSkuCntVoList)) {
                return;
            }

            final PurchaseSkuCntVo purchaseSkuCntVo = purchaseSkuCntVoList.get(0);
            vo.setSku(purchaseSkuCntVo.getSkuCode());
            vo.setSkuEncode(purchaseSkuCntVo.getSkuEncode());
        });

        resultBo.setRowDataList(purchaseChildPreConfirmExportVoList);

        return CommonResult.success(resultBo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void getPurchasePreConfirmExport(PurchaseProductSearchDto dto) {
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(),
                GlobalContext.getUsername(), FileOperateBizType.SCM_PURCHASE_PRE_CONFIRM_EXPORT.getCode(), dto));
    }

    public Integer getRawSkuChildExportTotals(PurchaseProductSearchDto dto) {
        //条件过滤
        if (null == purchaseBaseService.getSearchPurchaseChildWhere(dto)) {
            return 0;
        }
        return purchaseChildOrderDao.getRawSkuChildExportTotals(dto);
    }

    public CommonResult<ExportationListResultBo<PurchaseChildSkuRawExportVo>> getRawSkuChildExportList(PurchaseProductSearchDto dto) {
        ExportationListResultBo<PurchaseChildSkuRawExportVo> resultBo = new ExportationListResultBo<>();
        //条件过滤
        if (null == purchaseBaseService.getSearchPurchaseChildWhere(dto)) {
            return CommonResult.success(resultBo);
        }
        final CommonPageResult.PageInfo<PurchaseChildSkuRawExportVo> pageResult = purchaseChildOrderDao.getRawSkuChildExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize(), false), dto);
        final List<PurchaseChildSkuRawExportVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }

        final List<String> purchaseChildOrderNoList = records.stream()
                .map(PurchaseChildSkuRawExportVo::getPurchaseChildOrderNo)
                .distinct()
                .collect(Collectors.toList());

        List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList = purchaseChildOrderRawDao.getListByChildNoList(purchaseChildOrderNoList,
                PurchaseRawBizType.FORMULA,
                List.of(RawSupplier.HETE, RawSupplier.OTHER_SUPPLIER));
        Map<String, List<PurchaseChildOrderRawPo>> purchaseChildOrderRawPoMap = purchaseChildOrderRawPoList.stream()
                .collect(Collectors.groupingBy(PurchaseChildOrderRawPo::getPurchaseChildOrderNo));

        for (PurchaseChildSkuRawExportVo record : records) {
            record.setPurchaseOrderStatusName(null != record.getPurchaseOrderStatus() ? record.getPurchaseOrderStatus().getRemark() : "");
            final List<PurchaseChildOrderRawPo> purchaseChildOrderRawPos = purchaseChildOrderRawPoMap.get(record.getPurchaseChildOrderNo());
            if (CollectionUtils.isNotEmpty(purchaseChildOrderRawPos)) {
                record.setMaterialSku1(purchaseChildOrderRawPos.get(0).getSku());
                record.setDeliveryCnt1(purchaseChildOrderRawPos.get(0).getDeliveryCnt());

                if (purchaseChildOrderRawPos.size() > 1) {
                    record.setMaterialSku2(purchaseChildOrderRawPos.get(1).getSku());
                    record.setDeliveryCnt2(purchaseChildOrderRawPos.get(1).getDeliveryCnt());
                }

                if (purchaseChildOrderRawPos.size() > 2) {
                    record.setMaterialSku3(purchaseChildOrderRawPos.get(2).getSku());
                    record.setDeliveryCnt3(purchaseChildOrderRawPos.get(2).getDeliveryCnt());
                }

                if (purchaseChildOrderRawPos.size() > 3) {
                    record.setMaterialSku4(purchaseChildOrderRawPos.get(3).getSku());
                    record.setDeliveryCnt4(purchaseChildOrderRawPos.get(3).getDeliveryCnt());
                }

                if (purchaseChildOrderRawPos.size() > 4) {
                    record.setMaterialSku5(purchaseChildOrderRawPos.get(4).getSku());
                    record.setDeliveryCnt5(purchaseChildOrderRawPos.get(4).getDeliveryCnt());
                }

            }
        }

        resultBo.setRowDataList(records);

        return CommonResult.success(resultBo);
    }

}
