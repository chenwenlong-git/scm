package com.hete.supply.scm.server.scm.ibfs.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2024/5/13 15:39
 */
@Data
@NoArgsConstructor
public class RecoOrderItemTotalVo {

    @ApiModelProperty(value = "对账条目数")
    private Integer recoOrderTotalCount;

    @ApiModelProperty(value = "单据数")
    private Integer receiptTotalCount;

    @ApiModelProperty(value = "SKU数")
    private Integer skuTotalCount;

    @ApiModelProperty(value = "类数")
    private Integer typeTotalCount;

    @ApiModelProperty(value = "收单金额")
    private BigDecimal totalPrice;


}
