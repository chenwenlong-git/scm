package com.hete.supply.scm.server.scm.stockup.converter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.server.scm.stockup.entity.dto.StockUpCreateItemDto;
import com.hete.supply.scm.server.scm.stockup.entity.po.StockUpOrderItemPo;
import com.hete.supply.scm.server.scm.stockup.entity.po.StockUpOrderPo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierCodeAndSkuBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryRecordPo;
import com.hete.supply.scm.server.supplier.stockup.entity.dto.StockUpReturnGoodsDto;
import com.hete.support.api.exception.BizException;
import com.hete.support.core.holder.GlobalContext;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/1/12 09:23
 */
public class StockUpConverter {
    public static List<StockUpOrderPo> convertCreateDtoToStockUpPo(List<StockUpCreateItemDto> dtoList,
                                                                   Map<String, SupplierInventoryPo> supplierSkuInventoryPoMap) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return Collections.emptyList();
        }

        return dtoList.stream().map(itemDto -> {
            final SupplierInventoryPo supplierInventoryPo = supplierSkuInventoryPoMap.get(itemDto.getSupplierCode() + itemDto.getSku());
            if (null == supplierInventoryPo) {
                throw new BizException("供应商库存数据异常，请联系系统管理员！");
            }

            final StockUpOrderPo stockUpOrderPo = new StockUpOrderPo();
            stockUpOrderPo.setStockUpOrderStatus(StockUpOrderStatus.TO_BE_FOLLOW_CONFIRM);
            stockUpOrderPo.setSku(supplierInventoryPo.getSku());
            stockUpOrderPo.setCategoryName(supplierInventoryPo.getCategoryName());
            stockUpOrderPo.setPlaceOrderCnt(itemDto.getPlaceOrderCnt());
            stockUpOrderPo.setRequestReturnGoodsDate(itemDto.getRequestReturnGoodsDate());
            stockUpOrderPo.setSupplierCode(supplierInventoryPo.getSupplierCode());
            stockUpOrderPo.setSupplierName(supplierInventoryPo.getSupplierName());
            return stockUpOrderPo;
        }).collect(Collectors.toList());
    }

    public static List<SupplierCodeAndSkuBo> convertCreateDtoToSupplierSkuBo(List<StockUpCreateItemDto> stockUpCreateItemDtoList) {
        if (CollectionUtils.isEmpty(stockUpCreateItemDtoList)) {
            return Collections.emptyList();
        }

        return stockUpCreateItemDtoList.stream().map(dto -> {
            final SupplierCodeAndSkuBo supplierCodeAndSkuBo = new SupplierCodeAndSkuBo();
            supplierCodeAndSkuBo.setSupplierCode(dto.getSupplierCode());
            supplierCodeAndSkuBo.setSku(dto.getSku());
            return supplierCodeAndSkuBo;
        }).collect(Collectors.toList());
    }

    public static StockUpOrderItemPo dtoToStockUpItemPo(StockUpReturnGoodsDto dto, StockUpOrderPo stockUpOrderPo) {
        final StockUpOrderItemPo stockUpOrderItemPo = new StockUpOrderItemPo();
        stockUpOrderItemPo.setStockUpOrderNo(stockUpOrderPo.getStockUpOrderNo());
        stockUpOrderItemPo.setSku(stockUpOrderPo.getSku());
        stockUpOrderItemPo.setWarehousingCnt(dto.getWarehousingCnt());
        stockUpOrderItemPo.setReturnGoodsCnt(dto.getReturnGoodsCnt());
        stockUpOrderItemPo.setReturnGoodsDate(LocalDateTime.now());

        return stockUpOrderItemPo;
    }

    public static SupplierInventoryRecordPo convertSupplierInventoryPoToRecordPo(StockUpReturnGoodsDto dto,
                                                                                 SupplierInventoryPo supplierInventoryPo,
                                                                                 String relateNo,
                                                                                 SupplierInventoryRecordStatus supplierInventoryRecordStatus) {
        final SupplierInventoryRecordPo supplierInventoryRecordPo = new SupplierInventoryRecordPo();
        supplierInventoryRecordPo.setSupplierCode(supplierInventoryPo.getSupplierCode());
        supplierInventoryRecordPo.setSupplierName(supplierInventoryPo.getSupplierName());
        supplierInventoryRecordPo.setSupplierWarehouse(SupplierWarehouse.STOCK_UP);
        supplierInventoryRecordPo.setSpu(supplierInventoryPo.getSpu());
        supplierInventoryRecordPo.setSku(supplierInventoryPo.getSku());
        supplierInventoryRecordPo.setCategoryId(supplierInventoryPo.getCategoryId());
        supplierInventoryRecordPo.setCategoryName(supplierInventoryPo.getCategoryName());
        supplierInventoryRecordPo.setSupplierInventoryCtrlType(SupplierInventoryCtrlType.WAREHOUSING);
        supplierInventoryRecordPo.setBeforeInventory(supplierInventoryPo.getStockUpInventory());
        supplierInventoryRecordPo.setCtrlCnt(dto.getWarehousingCnt());
        supplierInventoryRecordPo.setAfterInventory(supplierInventoryPo.getStockUpInventory() + dto.getWarehousingCnt());
        supplierInventoryRecordPo.setSupplierInventoryCtrlReason(SupplierInventoryCtrlReason.STOCK_UP);
        supplierInventoryRecordPo.setRelateNo(relateNo);
        supplierInventoryRecordPo.setSupplierInventoryRecordStatus(supplierInventoryRecordStatus);
        if (SupplierInventoryRecordStatus.EFFECTIVE.equals(supplierInventoryRecordStatus)) {
            supplierInventoryRecordPo.setEffectiveTime(LocalDateTime.now());
            supplierInventoryRecordPo.setApproveUser(GlobalContext.getUserKey());
            supplierInventoryRecordPo.setApproveUsername(GlobalContext.getUsername());
        }

        return supplierInventoryRecordPo;
    }
}
