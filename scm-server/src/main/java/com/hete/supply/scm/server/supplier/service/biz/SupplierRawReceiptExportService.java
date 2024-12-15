package com.hete.supply.scm.server.supplier.service.biz;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseRawReceiptSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseRawBizType;
import com.hete.supply.scm.api.scm.entity.enums.RawSupplier;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseRawReceiptExportVo;
import com.hete.supply.scm.api.scm.entity.vo.RawReceiptExportVo;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderChangeDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderItemDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderRawDao;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderChangePo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderItemPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderRawPo;
import com.hete.supply.scm.server.supplier.purchase.converter.SupplierPurchaseConverter;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseRawReceiptOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseRawReceiptOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseRawReceiptOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseRawReceiptOrderPo;
import com.hete.supply.scm.server.supplier.purchase.service.base.PurchaseRawReceiptBaseService;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/12/13 01:25
 */
@Service
@RequiredArgsConstructor
public class SupplierRawReceiptExportService {
    private final PurchaseRawReceiptOrderDao purchaseRawReceiptOrderDao;
    private final PurchaseRawReceiptOrderItemDao purchaseRawReceiptOrderItemDao;
    private final AuthBaseService authBaseService;
    private final PurchaseRawReceiptBaseService purchaseRawReceiptBaseService;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final PurchaseChildOrderChangeDao purchaseChildOrderChangeDao;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;
    private final PlmRemoteService plmRemoteService;
    private final PurchaseChildOrderRawDao purchaseChildOrderRawDao;


    public Integer getExportTotals(PurchaseRawReceiptSearchDto dto) {
        //条件过滤
        if (null == purchaseRawReceiptBaseService.getSearchRawReceiptWhere(dto)) {
            return 0;
        }
        return purchaseRawReceiptOrderDao.getExportTotals(dto);
    }

    public Integer getSupplierExportTotals(PurchaseRawReceiptSearchDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        return this.getExportTotals(dto);
    }

    public CommonPageResult.PageInfo<RawReceiptExportVo> getExportList(PurchaseRawReceiptSearchDto dto) {
        //条件过滤
        if (null == purchaseRawReceiptBaseService.getSearchRawReceiptWhere(dto)) {
            return new CommonPageResult.PageInfo<>();
        }

        return purchaseRawReceiptOrderDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
    }

    public CommonPageResult.PageInfo<RawReceiptExportVo> getSupplierExportList(PurchaseRawReceiptSearchDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        return this.getExportList(dto);
    }

    public CommonResult<ExportationListResultBo<PurchaseRawReceiptExportVo>> getNewRawReceiptExportList(PurchaseRawReceiptSearchDto dto) {
        final Page<PurchaseRawReceiptOrderPo> pageResult = purchaseRawReceiptOrderDao.getNewRawReceiptExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<PurchaseRawReceiptOrderPo> records = pageResult.getRecords();
        final Map<String, PurchaseRawReceiptOrderPo> purchaseRawReceiptMap = records.stream()
                .collect(Collectors.toMap(PurchaseRawReceiptOrderPo::getPurchaseRawReceiptOrderNo, Function.identity()));
        final List<String> purchaseRawReceiptNoList = records.stream()
                .map(PurchaseRawReceiptOrderPo::getPurchaseRawReceiptOrderNo)
                .collect(Collectors.toList());
        final List<PurchaseRawReceiptOrderItemPo> purchaseRawReceiptOrderItemPoList = purchaseRawReceiptOrderItemDao.getListByNoList(purchaseRawReceiptNoList);
        final List<String> purchaseChildOrderNoList = records.stream()
                .map(PurchaseRawReceiptOrderPo::getPurchaseChildOrderNo)
                .distinct()
                .collect(Collectors.toList());
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList);
        final Map<String, PurchaseChildOrderPo> purchaseOrderNoMap = purchaseChildOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));
        final List<Long> idList = purchaseChildOrderPoList.stream()
                .map(PurchaseChildOrderPo::getPurchaseChildOrderId)
                .distinct()
                .collect(Collectors.toList());
        final List<PurchaseChildOrderChangePo> purchaseChildOrderChangePoList = purchaseChildOrderChangeDao.getByChildOrderId(idList);
        final Map<Long, PurchaseChildOrderChangePo> purchaseChangeIdMap = purchaseChildOrderChangePoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderChangePo::getPurchaseChildOrderId, Function.identity()));

        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNoList(purchaseChildOrderNoList);
        final Map<String, PurchaseChildOrderItemPo> purchaseChildNoItemMap = purchaseChildOrderItemPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderItemPo::getPurchaseChildOrderNo, Function.identity(), (item1, item2) -> item1));
        final List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList = purchaseChildOrderRawDao.getListByChildNoList(purchaseChildOrderNoList,
                PurchaseRawBizType.FORMULA, Arrays.asList(RawSupplier.HETE, RawSupplier.OTHER_SUPPLIER));
        final Map<String, PurchaseChildOrderRawPo> purchaseChildSkuRawMap = Optional.ofNullable(purchaseChildOrderRawPoList)
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.toMap(po -> po.getPurchaseChildOrderNo() + po.getSku(), Function.identity()));


        final List<String> rawSkuList = purchaseRawReceiptOrderItemPoList.stream()
                .map(PurchaseRawReceiptOrderItemPo::getSku)
                .collect(Collectors.toList());

        final List<String> skuList = purchaseChildOrderItemPoList.stream()
                .map(PurchaseChildOrderItemPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        skuList.addAll(rawSkuList);
        final List<String> distinctSkuList = skuList.stream()
                .distinct()
                .collect(Collectors.toList());

        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(distinctSkuList);
        final List<PurchaseRawReceiptExportVo> purchaseRawReceiptExportVoList = SupplierPurchaseConverter.rawReceiptPoListToExportVoList(purchaseRawReceiptMap, purchaseOrderNoMap,
                purchaseChangeIdMap, purchaseChildNoItemMap, skuEncodeMap, purchaseRawReceiptOrderItemPoList, purchaseChildSkuRawMap);
        ExportationListResultBo<PurchaseRawReceiptExportVo> resultBo = new ExportationListResultBo<>();
        resultBo.setRowDataList(purchaseRawReceiptExportVoList);

        return CommonResult.success(resultBo);
    }

}
