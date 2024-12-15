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
public class DeductOrderExportBo {

    @ApiModelProperty(value = "扣款单ID")
    private Long deductOrderId;

    @ApiModelProperty(value = "扣款单号")
    private String deductOrderNo;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "扣款金额")
    private BigDecimal deductPrice;

    @ApiModelProperty(value = "扣款备注")
    private String deductRemarks;

}
