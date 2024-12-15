package com.hete.supply.scm.server.scm.process.converter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.entity.vo.WarehouseListVo;
import com.hete.supply.wms.api.basic.entity.vo.WarehouseVo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/3/12.
 */
public class ProcWarehouseConverter {

    /**
     * 将 WarehouseVo 列表转换为 WarehouseListVo 列表的方法。
     *
     * @param warehouseVos 待转换的 WarehouseVo 列表
     * @return 转换后的 WarehouseListVo 列表
     */
    public static List<WarehouseListVo> convertWarehouseListVos(List<WarehouseVo> warehouseVos) {
        return CollectionUtils.isEmpty(warehouseVos) ? Collections.emptyList() : warehouseVos.stream()
                .map(warehouseVo -> {
                    WarehouseListVo warehouseListVo = new WarehouseListVo();
                    warehouseListVo.setWarehouseCode(warehouseVo.getWarehouseCode());
                    warehouseListVo.setWarehouseName(warehouseVo.getWarehouseName());
                    warehouseListVo.setWarehouseType(warehouseVo.getWarehouseType());
                    return warehouseListVo;
                })
                .collect(Collectors.toList());
    }
}
