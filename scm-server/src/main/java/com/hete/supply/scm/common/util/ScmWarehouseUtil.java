package com.hete.supply.scm.common.util;

import com.hete.supply.scm.api.scm.entity.enums.PolymerizeWarehouse;
import com.hete.supply.wms.api.WmsEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/3/1 16:05
 */
@Validated
@Slf4j
public class ScmWarehouseUtil {

    public static final String WH_WAREHOUSE_CODE = "WH01";
    private static final String SUPPLIER = "供应商";

    public static List<PolymerizeWarehouse> getPolymerizeWarehouseByTypes(WmsEnum.WarehouseType warehouseType,
                                                                          String warehouseCode,
                                                                          String warehouseName) {
        if (null == warehouseType || StringUtils.isBlank(warehouseCode) || StringUtils.isBlank(warehouseName)) {
            return Collections.emptyList();
        }
        final List<PolymerizeWarehouse> result = new ArrayList<>();
        // 判断网红仓
        if (WH_WAREHOUSE_CODE.equals(warehouseCode)) {
            result.add(PolymerizeWarehouse.WH);
        } else {
            result.add(PolymerizeWarehouse.NON_WH);
        }

        // 判断广州仓
        if (WmsEnum.WarehouseType.DOMESTIC_SELF_RUN.equals(warehouseType) && !WH_WAREHOUSE_CODE.equals(warehouseCode)) {
            result.add(PolymerizeWarehouse.GUANGZHOU);
        }

        if (WmsEnum.WarehouseType.VIRTUAL_WAREHOUSE.equals(warehouseType) && !warehouseName.contains(SUPPLIER)) {
            result.add(PolymerizeWarehouse.GUANGZHOU);
        }

        // 判断工厂虚拟仓
        if (WmsEnum.WarehouseType.VIRTUAL_WAREHOUSE.equals(warehouseType) && warehouseName.contains(SUPPLIER)) {
            result.add(PolymerizeWarehouse.VIRTUAL_WAREHOUSE);
        }

        // 判断海外三方仓
        if (WmsEnum.WarehouseType.FOREIGN_THIRD_PARTY.equals(warehouseType)) {
            result.add(PolymerizeWarehouse.FOREIGN_THIRD_PARTY);
        }

        // 判断海外自营仓
        if (WmsEnum.WarehouseType.FOREIGN_SELF_RUN.equals(warehouseType)) {
            result.add(PolymerizeWarehouse.FOREIGN_SELF_RUN);
        }

        return result;
    }
}
