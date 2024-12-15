package com.hete.supply.scm.server.scm.cost.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2024/2/22 18:05
 */
@Data
@NoArgsConstructor
public class GoodsCostItemVo {

    @ApiModelProperty(value = "主键id")
    private Long costOfGoodsItemId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "单据号")
    private String businessNo;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "批次码创建时间")
    private LocalDateTime skuBatchCodeTime;

    @ApiModelProperty(value = "库存数量")
    private Integer inventory;

    @ApiModelProperty(value = "批次码价格")
    private BigDecimal skuBatchPrice;

    @ApiModelProperty(value = "库存金额")
    private BigDecimal inventoryPrice;

}
