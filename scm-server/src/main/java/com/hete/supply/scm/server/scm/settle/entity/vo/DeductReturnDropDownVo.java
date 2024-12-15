package com.hete.supply.scm.server.scm.settle.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:04
 */
@Data
@NoArgsConstructor
public class DeductReturnDropDownVo {

    @ApiModelProperty(value = "单据号")
    private String businessNo;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;

}
