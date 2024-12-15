package com.hete.supply.scm.server.scm.service.base;

import com.hete.supply.scm.server.scm.enums.wms.WarehouseType;
import com.hete.support.api.enums.BooleanType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/2/14 00:05
 */
@Service
@RequiredArgsConstructor
@Validated
public class WarehouseBaseService {
    /**
     * 根据仓库标签判断是否为直发仓
     *
     * @param warehouseTypeList
     * @return
     */
    public static BooleanType getIsDirectSendByWarehouseType(@NotEmpty List<String> warehouseTypeList) {
        if (warehouseTypeList.contains(WarehouseType.FOREIGN_SELF_RUN.getRemark())
                || warehouseTypeList.contains(WarehouseType.FOREIGN_THIRD_PARTY.getRemark())) {
            return BooleanType.TRUE;
        } else {
            return BooleanType.FALSE;
        }
    }

    /**
     * 根据仓库标签判断是否为直发仓
     *
     * @param warehouseTypes
     * @return
     */
    public static BooleanType getIsDirectSendByWarehouseType(String warehouseTypes) {
        if (warehouseTypes.contains(WarehouseType.FOREIGN_SELF_RUN.getRemark())
                || warehouseTypes.contains(WarehouseType.FOREIGN_THIRD_PARTY.getRemark())) {
            return BooleanType.TRUE;
        } else {
            return BooleanType.FALSE;
        }
    }


}
