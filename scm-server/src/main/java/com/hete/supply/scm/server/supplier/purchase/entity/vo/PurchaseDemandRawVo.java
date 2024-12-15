package com.hete.supply.scm.server.supplier.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.RawSupplier;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/5/25 17:30
 */
@Data
@NoArgsConstructor
public class PurchaseDemandRawVo {
    @ApiModelProperty(value = "原料sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "可用库存")
    private Integer skuStock;

    @ApiModelProperty(value = "出库数")
    private Integer deliveryCnt;

    @ApiModelProperty(value = "原料仓库名称")
    private String rawWarehouseName;

    @ApiModelProperty(value = "原料提供方")
    private RawSupplier rawSupplier;
}
