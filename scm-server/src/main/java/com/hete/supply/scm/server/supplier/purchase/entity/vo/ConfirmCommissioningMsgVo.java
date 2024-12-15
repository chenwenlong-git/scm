package com.hete.supply.scm.server.supplier.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.RawSupplier;
import com.hete.supply.scm.server.scm.purchase.enums.RawExtra;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/1/9 19:23
 */
@Data
@NoArgsConstructor
public class ConfirmCommissioningMsgVo {
    @ApiModelProperty(value = "原料sku")
    private String sku;

    @ApiModelProperty(value = "预计消耗库存")
    private Integer expectedConsumeCnt;

    @ApiModelProperty(value = "库存")
    private Integer totalInventory;

    @ApiModelProperty(value = "额外原料出库")
    private RawExtra rawExtra;

    @ApiModelProperty(value = "原料提供方")
    private RawSupplier rawSupplier;
}
