package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.RawSupplier;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/1/19 00:00
 */
@Data
@NoArgsConstructor
public class PurchaseDeliverRawVo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "预计消耗数")
    private Integer deliveryCnt;

    @ApiModelProperty(value = "实际消耗数")
    private Integer actualConsumeCnt;

    @ApiModelProperty(value = "库存")
    private Integer supplierInventory;

    @ApiModelProperty(value = "原料提供方（半成品备货版本不需要接这个参数）")
    private RawSupplier rawSupplier;
}
