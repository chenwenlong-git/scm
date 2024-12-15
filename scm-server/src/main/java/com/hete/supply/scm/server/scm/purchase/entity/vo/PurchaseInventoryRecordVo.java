package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SupplierWarehouse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/3/22 17:49
 */
@Data
@NoArgsConstructor
public class PurchaseInventoryRecordVo {
    @ApiModelProperty(value = "id")
    private Long supplierInventoryRecordId;

    @ApiModelProperty(value = "操作数量")
    private Integer ctrlCnt;

    @ApiModelProperty(value = "供应商仓库：备货，自备")
    private SupplierWarehouse supplierWarehouse;
}
