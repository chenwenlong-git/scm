package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2024/4/2.
 */
@Data
public class LatestCostCoefficientsVo {
    @ApiModelProperty(value = "系数", example = "0.8")
    private BigDecimal coefficient;
}
