package com.hete.supply.scm.server.scm.supplier.converter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.plm.api.goods.entity.vo.PlmCategoryVo;
import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryCtrlReason;
import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryCtrlType;
import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryRecordStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplierWarehouse;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.stockup.entity.po.StockUpOrderItemPo;
import com.hete.supply.scm.server.scm.stockup.entity.vo.StockUpSearchItemVo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierCodeAndSkuBo;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierAndSkuItemDto;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryRecordPo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierSkuInventoryVo;
import com.hete.supply.scm.server.supplier.entity.dto.RawReturnProductItemDto;
import com.hete.supply.scm.server.supplier.supplier.entity.dto.InventoryChangeItemDto;
import com.hete.support.api.exception.BizException;
import com.hete.support.core.holder.GlobalContext;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/1/10 20:27
 */
public class SupplierInventoryConverter {

    public static List<SupplierSkuInventoryVo> convertSupplierInventoryPoToVo(List<SupplierAndSkuItemDto> supplierAndSkuItemList,
                                                                              List<SupplierInventoryPo> supplierInventoryPoList) {
        final Map<String, SupplierInventoryPo> supplierCodeSkuPoMap = Optional.ofNullable(supplierInventoryPoList)
                .orElse(new ArrayList<>())
                .stream()
                .collect(Collectors.toMap(po -> po.getSupplierCode() + po.getSku(), Function.identity()));
        return supplierAndSkuItemList.stream().map(itemDto -> {
            final SupplierInventoryPo supplierInventoryPo = supplierCodeSkuPoMap.get(itemDto.getSupplierCode() + itemDto.getSku());

            final SupplierSkuInventoryVo supplierSkuInventoryVo = new SupplierSkuInventoryVo();
            supplierSkuInventoryVo.setSupplierCode(itemDto.getSupplierCode());
            supplierSkuInventoryVo.setSku(itemDto.getSku());
            if (null != supplierInventoryPo) {
                supplierSkuInventoryVo.setStockUpInventory(supplierInventoryPo.getStockUpInventory());
                supplierSkuInventoryVo.setSelfProvideInventory(supplierInventoryPo.getSelfProvideInventory());
            }
            return supplierSkuInventoryVo;
        }).collect(Collectors.toList());
    }

    public static List<SupplierInventoryRecordPo> convertInventoryDtoToRecordPo(List<InventoryChangeItemDto> inventoryChangeItemList,
                                                                                List<SupplierInventoryPo> supplierInventoryPoList,
                                                                                SupplierInventoryCtrlType supplierInventoryCtrlType,
                                                                                SupplierWarehouse supplierWarehouse,
                                                                                SupplierInventoryCtrlReason supplierInventoryCtrlReason,
                                                                                SupplierInventoryRecordStatus supplierInventoryRecordStatus,
                                                                                String relateNo) {
        if (CollectionUtils.isEmpty(inventoryChangeItemList)) {
            return Collections.emptyList();
        }

        if (CollectionUtils.isEmpty(supplierInventoryPoList)) {
            return Collections.emptyList();
        }

        final Map<String, SupplierInventoryPo> supplierSkuInventoryPoMap = supplierInventoryPoList.stream()
                .collect(Collectors.toMap(po -> po.getSupplierCode() + po.getSku(), Function.identity()));

        return inventoryChangeItemList.stream().map(dto -> {
            final SupplierInventoryPo supplierInventoryPo = supplierSkuInventoryPoMap.get(dto.getSupplierCode() + dto.getSku());
            if (null == supplierInventoryPo) {
                throw new BizException("数据错误，获取不到供应商:{}的sku:{}库存，请联系管理员！",
                        dto.getSupplierCode(), dto.getSku());
            }

            final SupplierInventoryRecordPo supplierInventoryRecordPo = new SupplierInventoryRecordPo();
            supplierInventoryRecordPo.setSupplierCode(dto.getSupplierCode());
            supplierInventoryRecordPo.setSupplierName(supplierInventoryPo.getSupplierName());
            supplierInventoryRecordPo.setSupplierWarehouse(supplierWarehouse);
            supplierInventoryRecordPo.setSpu(supplierInventoryPo.getSpu());
            supplierInventoryRecordPo.setSku(dto.getSku());
            supplierInventoryRecordPo.setCategoryId(supplierInventoryPo.getCategoryId());
            supplierInventoryRecordPo.setCategoryName(supplierInventoryPo.getCategoryName());
            if (dto.getInventoryChangeCnt() > 0) {
                supplierInventoryRecordPo.setSupplierInventoryCtrlType(SupplierInventoryCtrlType.WAREHOUSING);
            } else {
                supplierInventoryRecordPo.setSupplierInventoryCtrlType(SupplierInventoryCtrlType.OUTBOUND);
            }
            // 若入参指定了操作类型，则直接使用该入参
            if (null != supplierInventoryCtrlType) {
                supplierInventoryRecordPo.setSupplierInventoryCtrlType(supplierInventoryCtrlType);
            }
            supplierInventoryRecordPo.setCtrlCnt(dto.getInventoryChangeCnt());
            int beforeInventory = 0;
            if (SupplierWarehouse.STOCK_UP.equals(supplierWarehouse)) {
                beforeInventory = supplierInventoryPo.getStockUpInventory();
            } else if (SupplierWarehouse.SELF_PROVIDE.equals(supplierWarehouse)) {
                beforeInventory = supplierInventoryPo.getSelfProvideInventory();
            } else if (SupplierWarehouse.DEFECTIVE_WAREHOUSE.equals(supplierWarehouse)) {
                beforeInventory = supplierInventoryPo.getDefectiveInventory();
            }
            supplierInventoryRecordPo.setBeforeInventory(beforeInventory);
            supplierInventoryRecordPo.setAfterInventory(beforeInventory + dto.getInventoryChangeCnt());

            supplierInventoryRecordPo.setSupplierInventoryCtrlReason(supplierInventoryCtrlReason);
            supplierInventoryRecordPo.setRelateNo(relateNo);
            supplierInventoryRecordPo.setRecordRemark(dto.getRecordRemark());
            supplierInventoryRecordPo.setSupplierInventoryRecordStatus(supplierInventoryRecordStatus);
            if (SupplierInventoryRecordStatus.EFFECTIVE.equals(supplierInventoryRecordStatus)) {
                supplierInventoryRecordPo.setEffectiveTime(LocalDateTime.now());
                supplierInventoryRecordPo.setApproveUser(GlobalContext.getUserKey());
                supplierInventoryRecordPo.setApproveUsername(GlobalContext.getUsername());
            }
            return supplierInventoryRecordPo;
        }).collect(Collectors.toList());
    }

    public static List<SupplierCodeAndSkuBo> convertChangeDtoToSupplierSkuBo(List<InventoryChangeItemDto> inventoryChangeItemList) {
        if (CollectionUtils.isEmpty(inventoryChangeItemList)) {
            return Collections.emptyList();
        }

        return inventoryChangeItemList.stream().map(itemDto -> {
            final SupplierCodeAndSkuBo supplierCodeAndSkuBo = new SupplierCodeAndSkuBo();
            supplierCodeAndSkuBo.setSupplierCode(itemDto.getSupplierCode());
            supplierCodeAndSkuBo.setSku(itemDto.getSku());
            return supplierCodeAndSkuBo;
        }).collect(Collectors.toList());
    }

    public static SupplierInventoryRecordPo commissioningInventoryRecord(PurchaseChildOrderPo purchaseChildOrderPo,
                                                                         String sku,
                                                                         SupplierWarehouse supplierWarehouse,
                                                                         SupplierInventoryCtrlType supplierInventoryCtrlType,
                                                                         Integer beforeInventory,
                                                                         int stockUpDecrement,
                                                                         SupplierInventoryCtrlReason supplierInventoryCtrlReason,
                                                                         String relateNo, PlmCategoryVo categoryVo,
                                                                         SupplierInventoryRecordStatus supplierInventoryRecordStatus) {
        final SupplierInventoryRecordPo stockUpInventoryRecordPo = new SupplierInventoryRecordPo();
        stockUpInventoryRecordPo.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
        stockUpInventoryRecordPo.setSupplierName(purchaseChildOrderPo.getSupplierName());
        stockUpInventoryRecordPo.setSupplierWarehouse(supplierWarehouse);
        stockUpInventoryRecordPo.setSpu(purchaseChildOrderPo.getSpu());
        stockUpInventoryRecordPo.setSku(sku);
        if (null != categoryVo) {
            stockUpInventoryRecordPo.setCategoryId(categoryVo.getCategoryId());
            stockUpInventoryRecordPo.setCategoryName(categoryVo.getCategoryNameCn());
        }

        stockUpInventoryRecordPo.setSupplierInventoryCtrlType(supplierInventoryCtrlType);
        stockUpInventoryRecordPo.setBeforeInventory(beforeInventory);
        stockUpInventoryRecordPo.setCtrlCnt(Math.abs(stockUpDecrement));
        stockUpInventoryRecordPo.setAfterInventory(beforeInventory - stockUpDecrement);
        stockUpInventoryRecordPo.setSupplierInventoryCtrlReason(supplierInventoryCtrlReason);
        stockUpInventoryRecordPo.setRelateNo(relateNo);
        stockUpInventoryRecordPo.setSupplierInventoryRecordStatus(supplierInventoryRecordStatus);
        stockUpInventoryRecordPo.setEffectiveTime(LocalDateTime.now());
        stockUpInventoryRecordPo.setApproveUser(GlobalContext.getUserKey());
        stockUpInventoryRecordPo.setApproveUsername(GlobalContext.getUsername());

        return stockUpInventoryRecordPo;
    }

    public static List<StockUpSearchItemVo> itemPoListToVo(List<StockUpOrderItemPo> stockUpOrderItemPoList) {
        if (CollectionUtils.isEmpty(stockUpOrderItemPoList)) {
            return new ArrayList<>();
        }

        return stockUpOrderItemPoList.stream().map(stockUpOrderItemPo -> {
            final StockUpSearchItemVo stockUpSearchItemVo = new StockUpSearchItemVo();
            stockUpSearchItemVo.setStockUpOrderNo(stockUpOrderItemPo.getStockUpOrderNo());
            stockUpSearchItemVo.setReturnGoodsDate(stockUpOrderItemPo.getReturnGoodsDate());
            stockUpSearchItemVo.setWarehousingCnt(stockUpOrderItemPo.getWarehousingCnt());
            stockUpSearchItemVo.setReturnGoodsCnt(stockUpOrderItemPo.getReturnGoodsCnt());

            return stockUpSearchItemVo;
        }).collect(Collectors.toList());
    }

    public static List<InventoryChangeItemDto> convertInventoryDtoToItemDto(List<RawReturnProductItemDto> rawProductItemList,
                                                                            SupplierWarehouse supplierWarehouse,
                                                                            String supplierCode) {
        return rawProductItemList.stream()
                .filter(itemDto -> supplierWarehouse.equals(itemDto.getSupplierWarehouse()))
                .map(itemDto -> {
                    final InventoryChangeItemDto inventoryChangeItemDto = new InventoryChangeItemDto();
                    inventoryChangeItemDto.setSupplierCode(supplierCode);
                    inventoryChangeItemDto.setSku(itemDto.getSku());
                    inventoryChangeItemDto.setInventoryChangeCnt(itemDto.getReturnCnt());
                    return inventoryChangeItemDto;
                }).collect(Collectors.toList());
    }
}
