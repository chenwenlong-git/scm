package com.hete.supply.scm.server.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2023/11/15 17:56
 */
@Data
@NoArgsConstructor
public class DeductSupplementBusinessSettleVo {

    @ApiModelProperty(value = "单据号")
    private String businessNo;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;

}
