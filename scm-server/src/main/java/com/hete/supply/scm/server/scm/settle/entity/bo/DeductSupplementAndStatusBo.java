package com.hete.supply.scm.server.scm.settle.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2024/6/14 13:51
 */
@Data
@NoArgsConstructor
public class DeductSupplementAndStatusBo {

    @ApiModelProperty(value = "补扣款单号")
    private String deductSupplementNo;

    @ApiModelProperty(value = "主键id")
    private Long businessId;

    @ApiModelProperty(value = "关联单据号")
    private String businessNo;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "金额")
    private BigDecimal totalPrice;

}
