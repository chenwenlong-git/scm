package com.hete.supply.scm.server.scm.settle.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2023/2/24 09:30
 */
@Data
@NoArgsConstructor
public class SupplementOrderOtherDto {

    @NotNull(message = "补款金额不能为空")
    @ApiModelProperty(value = "补款金额")
    private BigDecimal supplementPrice;

    @NotBlank(message = "补款备注不能为空")
    @ApiModelProperty(value = "补款备注")
    private String supplementRemarks;
}
