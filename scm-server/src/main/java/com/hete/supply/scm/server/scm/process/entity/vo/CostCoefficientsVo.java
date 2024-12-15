package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yanjiawei
 * Created on 2024/2/20.
 */
@Data
@ApiModel(description = "成本系数视图对象")
public class CostCoefficientsVo {

    @ApiModelProperty(value = "成本系数ID", example = "1")
    private Long costCoefficientsId;

    @ApiModelProperty(value = "生效时间", example = "2022-01-01")
    private LocalDateTime effectiveTime;

    @ApiModelProperty(value = "系数", example = "0.8")
    private BigDecimal coefficient;

}
