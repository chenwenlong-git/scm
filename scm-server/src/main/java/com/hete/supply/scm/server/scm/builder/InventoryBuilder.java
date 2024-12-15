package com.hete.supply.scm.server.scm.builder;

import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderMaterialPo;
import com.hete.supply.wms.api.interna.entity.dto.InventoryForPlmDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/1/8.
 */
public class InventoryBuilder {
    public static List<InventoryForPlmDto.InventoryQueryInfo> buildInventoryQueryInfoList(List<ProcessOrderMaterialPo> processOrderMaterialPos) {
        return processOrderMaterialPos.stream().map(processOrderMaterialPo -> {
            InventoryForPlmDto.InventoryQueryInfo inventoryQueryInfo = new InventoryForPlmDto.InventoryQueryInfo();
            inventoryQueryInfo.setSkuCode(processOrderMaterialPo.getSku());
            inventoryQueryInfo.setWarehouseCode(processOrderMaterialPo.getWarehouseCode());
            inventoryQueryInfo.setLocationCode(processOrderMaterialPo.getShelfCode());
            return inventoryQueryInfo;
        }).collect(Collectors.toList());
    }
}
