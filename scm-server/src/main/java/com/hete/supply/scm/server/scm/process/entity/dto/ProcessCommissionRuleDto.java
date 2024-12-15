package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2023/12/14.
 */
@Data
public class ProcessCommissionRuleDto {
    @ApiModelProperty(value = "提成规则主键id")
    private Long processCommissionRuleId;

    @NotNull(message = "提成等级不能为空")
    @Min(value = 1, message = "提成等级必须大于等于1")
    @ApiModelProperty(value = "提成等级")
    private Integer commissionLevel;

    @NotNull(message = "数量起始值不能为空")
    @ApiModelProperty(value = "数量起始值")
    @Min(value = 1, message = "数量起始值必须大于等于1")
    @Max(value = Integer.MAX_VALUE, message = "数量起始值超过最大值上限" + Integer.MAX_VALUE)
    private Integer startQuantity;

    @NotNull(message = "数量结束值不能为空")
    @ApiModelProperty(value = "数量结束值")
    @Min(value = 1, message = "数量结束值必须大于等于1")
    @Max(value = Integer.MAX_VALUE, message = "数量结束值超过最大值上限" + Integer.MAX_VALUE)
    private Integer endQuantity;

    @NotNull(message = "提成系数百分比不能为空")
    @ApiModelProperty(value = "提成系数百分比", example = "80.25%", notes = "前端传80.25")
    @DecimalMin(value = "0.01", message = "系数必须大于0.00%")
    @DecimalMax(value = "999.99", message = "系数最大值为1000.00%")
    private BigDecimal commissionCoefficient;

    @ApiModelProperty(value = "版本号")
    private Integer version;
}
