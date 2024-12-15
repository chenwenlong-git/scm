package com.hete.supply.scm.server.scm.supplier.builder;

import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryCtrlReason;
import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryCtrlType;
import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryRecordStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplierWarehouse;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryRecordPo;
import com.hete.supply.scm.server.supplier.supplier.entity.dto.InventoryChangeItemDto;
import com.hete.support.core.holder.GlobalContext;

import java.time.LocalDateTime;

/**
 * @author yanjiawei
 * Created on 2024/4/9.
 */
public class SupplierInventoryBuilder {

    public static SupplierInventoryRecordPo buildSupplierInventoryRecordPo(SupplierInventoryPo supplierInventoryPo,
                                                                           SupplierWarehouse supplierWarehouse,
                                                                           SupplierInventoryCtrlType supplierInventoryCtrlType,
                                                                           Integer curChangeCnt,
                                                                           SupplierInventoryCtrlReason supplierInventoryCtrlReason,
                                                                           String recordRemark,
                                                                           SupplierInventoryRecordStatus supplierInventoryRecordStatus) {
        SupplierInventoryRecordPo record = new SupplierInventoryRecordPo();
        record.setSupplierCode(supplierInventoryPo.getSupplierCode());
        record.setSku(supplierInventoryPo.getSku());
        record.setSupplierName(supplierInventoryPo.getSupplierName());
        record.setSpu(supplierInventoryPo.getSpu());
        record.setCategoryId(supplierInventoryPo.getCategoryId());
        record.setCategoryName(supplierInventoryPo.getCategoryName());

        int beforeInventory = 0;
        if (SupplierWarehouse.STOCK_UP.equals(supplierWarehouse)) {
            beforeInventory = supplierInventoryPo.getStockUpInventory();
        } else if (SupplierWarehouse.SELF_PROVIDE.equals(supplierWarehouse)) {
            beforeInventory = supplierInventoryPo.getSelfProvideInventory();
        } else if (SupplierWarehouse.DEFECTIVE_WAREHOUSE.equals(supplierWarehouse)) {
            beforeInventory = supplierInventoryPo.getDefectiveInventory();
        }

        record.setSupplierWarehouse(supplierWarehouse);
        record.setSupplierInventoryCtrlType(supplierInventoryCtrlType);
        record.setBeforeInventory(beforeInventory);
        record.setCtrlCnt(curChangeCnt);
        record.setAfterInventory(beforeInventory + curChangeCnt);
        record.setSupplierInventoryCtrlReason(supplierInventoryCtrlReason);
        record.setRecordRemark(recordRemark);
        record.setSupplierInventoryRecordStatus(supplierInventoryRecordStatus);
        if (SupplierInventoryRecordStatus.EFFECTIVE.equals(supplierInventoryRecordStatus)) {
            record.setEffectiveTime(LocalDateTime.now());
            record.setApproveUser(GlobalContext.getUserKey());
            record.setApproveUsername(GlobalContext.getUsername());
        }

        return record;
    }

    public static SupplierInventoryRecordPo buildSupplierInventoryRecordPo(SupplierInventoryPo supplierInventoryPo,
                                                                           SupplierWarehouse supplierWarehouse,
                                                                           SupplierInventoryCtrlType supplierInventoryCtrlType,
                                                                           Integer curChangeCnt,
                                                                           SupplierInventoryCtrlReason supplierInventoryCtrlReason,
                                                                           SupplierInventoryRecordStatus supplierInventoryRecordStatus) {
        return buildSupplierInventoryRecordPo(supplierInventoryPo, supplierWarehouse, supplierInventoryCtrlType,
                curChangeCnt, supplierInventoryCtrlReason, null,
                supplierInventoryRecordStatus);
    }

    public static SupplierInventoryRecordPo buildSupplierInventoryRecordPo(SupplierInventoryPo supplierInventoryPo,
                                                                           SupplierWarehouse supplierWarehouse,
                                                                           Integer curChangeCnt,
                                                                           SupplierInventoryCtrlReason supplierInventoryCtrlReason,
                                                                           SupplierInventoryRecordStatus supplierInventoryRecordStatus,
                                                                           String recordMark) {
        SupplierInventoryCtrlType supplierInventoryCtrlType
                = curChangeCnt > 0 ? SupplierInventoryCtrlType.WAREHOUSING : SupplierInventoryCtrlType.OUTBOUND;
        return buildSupplierInventoryRecordPo(supplierInventoryPo, supplierWarehouse, supplierInventoryCtrlType,
                curChangeCnt, supplierInventoryCtrlReason, recordMark,
                supplierInventoryRecordStatus);
    }

    public static InventoryChangeItemDto buildInventoryChangeItemDto(String supplierCode,
                                                                     String sku,
                                                                     SupplierWarehouse supplierWarehouse,
                                                                     Integer inventoryChangeCnt,
                                                                     String recordRemark) {
        InventoryChangeItemDto itemDto = new InventoryChangeItemDto();
        itemDto.setSupplierCode(supplierCode);
        itemDto.setSku(sku);
        itemDto.setSupplierWarehouse(supplierWarehouse);
        itemDto.setInventoryChangeCnt(inventoryChangeCnt);
        itemDto.setRecordRemark(recordRemark);
        return itemDto;
    }

    public static InventoryChangeItemDto buildInventoryChangeItemDto(String supplierCode,
                                                                     String sku,
                                                                     SupplierWarehouse supplierWarehouse,
                                                                     Integer inventoryChangeCnt) {
        return buildInventoryChangeItemDto(supplierCode, sku, supplierWarehouse, inventoryChangeCnt, null);
    }


}
