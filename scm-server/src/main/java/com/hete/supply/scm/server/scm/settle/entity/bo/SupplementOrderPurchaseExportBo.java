package com.hete.supply.scm.server.scm.settle.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2023/3/6 18:32
 */
@Data
@NoArgsConstructor
public class SupplementOrderPurchaseExportBo {

    @ApiModelProperty(value = "补款单ID")
    private Long supplementOrderId;

    @ApiModelProperty(value = "补款单单号")
    private String supplementOrderNo;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "补款金额")
    private BigDecimal supplementPrice;

    @ApiModelProperty(value = "补款备注")
    private String supplementRemarks;

}
