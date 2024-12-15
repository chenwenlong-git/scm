package com.hete.supply.scm.server.scm.production.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/9/24 10:42
 */
@Data
public class PurchaseProduceDataDetailVo {

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "plm_sku的版本号")
    private Integer version;

    @ApiModelProperty(value = "单件产能")
    private BigDecimal singleCapacity;

    @ApiModelProperty(value = "生产周期")
    private BigDecimal cycle;

    @ApiModelProperty(value = "商品采购价格")
    private BigDecimal goodsPurchasePrice;

    @ApiModelProperty(value = "供应商产品对照信息列表")
    private List<SupplierProductCompareItemVo> supplierProductCompareItemList;

}
