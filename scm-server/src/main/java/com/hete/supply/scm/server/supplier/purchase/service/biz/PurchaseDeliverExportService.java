package com.hete.supply.scm.server.supplier.purchase.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseDeliverListDto;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseDeliverExportVo;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderChangeDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderItemDao;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderItemPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierProductCompareDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import com.hete.supply.scm.server.supplier.purchase.service.base.PurchaseDeliverBaseService;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.entry.entity.dto.ReceiveOrderGetDto;
import com.hete.supply.wms.api.entry.entity.vo.ReceiveOrderForScmVo;
import com.hete.support.api.result.CommonPageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/12/18 19:25
 */
@Service
@RequiredArgsConstructor
public class PurchaseDeliverExportService {
    private final PurchaseDeliverOrderDao purchaseDeliverOrderDao;
    private final AuthBaseService authBaseService;
    private final WmsRemoteService wmsRemoteService;
    private final PurchaseChildOrderChangeDao purchaseChildOrderChangeDao;
    private final PurchaseDeliverBaseService purchaseDeliverBaseService;
    private final PlmRemoteService plmRemoteService;
    private final SupplierProductCompareDao supplierProductCompareDao;
    private final PurchaseReturnOrderDao purchaseReturnOrderDao;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;

    public Integer getExportTotals(PurchaseDeliverListDto dto) {
        //条件过滤
        if (null == purchaseDeliverBaseService.getSearchPurchaseDeliverWhere(dto)) {
            return 0;
        }
        return purchaseDeliverOrderDao.getExportTotals(dto);

    }

    public CommonPageResult.PageInfo<PurchaseDeliverExportVo> getExportList(PurchaseDeliverListDto dto) {
        //条件过滤
        if (null == purchaseDeliverBaseService.getSearchPurchaseDeliverWhere(dto)) {
            return new CommonPageResult.PageInfo<>();
        }
        CommonPageResult.PageInfo<PurchaseDeliverExportVo> pageList = purchaseDeliverOrderDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<PurchaseDeliverExportVo> records = pageList.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new CommonPageResult.PageInfo<>();
        }

        List<String> purchaseDeliverOrderNoList = records.stream().map(PurchaseDeliverExportVo::getPurchaseDeliverOrderNo).distinct().collect(Collectors.toList());
        //通过发货单查询wms的信息
        ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
        receiveOrderGetDto.setScmBizNoList(purchaseDeliverOrderNoList);
        Map<String, List<ReceiveOrderForScmVo>> receiveOrderForScmVoMap = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto).stream().collect(Collectors.groupingBy(ReceiveOrderForScmVo::getScmBizNo));
        //查询采购子单信息
        List<String> purchaseChildOrderNoList = records.stream()
                .map(PurchaseDeliverExportVo::getPurchaseChildOrderNo)
                .distinct()
                .collect(Collectors.toList());
        List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList);
        Map<String, PurchaseChildOrderPo> purchaseChildOrderPoMap = purchaseChildOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));

        // 查找供应商产品名称与sku产品名称
        final List<String> skuList = records.stream()
                .map(PurchaseDeliverExportVo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        Map<String, SupplierProductComparePo> supplierProductCompareMap = supplierProductCompareDao.getBatchSkuMap(skuList);

        // 查询退货单的数据
        Map<String, List<PurchaseReturnOrderPo>> purchaseReturnOrderPoMap = purchaseReturnOrderDao.getMapByBatchReturnBizNo(purchaseDeliverOrderNoList);

        // 查询结算单价
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNoList(purchaseChildOrderNoList);
        final Map<String, BigDecimal> purchaseChildNoSkuSettlePriceMap = purchaseChildOrderItemPoList.stream()
                .collect(Collectors.toMap(itemPo -> itemPo.getPurchaseChildOrderNo() + itemPo.getSku(),
                        PurchaseChildOrderItemPo::getSettlePrice, (item1, item2) -> item1));

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
            PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderPoMap.get(item.getPurchaseChildOrderNo());
            if (purchaseChildOrderPo != null) {
                item.setExpectedOnShelvesDate(purchaseChildOrderPo.getExpectedOnShelvesDate());
            }
            item.setSettlePrice(purchaseChildNoSkuSettlePriceMap.getOrDefault(item.getPurchaseChildOrderNo() + item.getSku(), BigDecimal.ZERO));
            if (null != item.getReceiveAmount()) {
                item.setTotalSettlePrice(item.getSettlePrice().multiply(new BigDecimal(item.getReceiveAmount())));
            }
        }).collect(Collectors.toList());

        pageList.setRecords(records);
        return pageList;
    }

    public Integer getSupplierExportTotals(PurchaseDeliverListDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        return this.getExportTotals(dto);
    }

    public CommonPageResult.PageInfo<PurchaseDeliverExportVo> getSupplierExportList(PurchaseDeliverListDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        return this.getExportList(dto);
    }

}
