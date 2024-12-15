package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author ChenWenLong
 * @date 2023/8/1 15:55
 */
@Data
@NoArgsConstructor
public class DevelopSampleSettleOrderDetailDto {
    @NotBlank(message = "样品结算单号不能为空")
    @ApiModelProperty(value = "样品结算单号")
    private String developSampleSettleOrderNo;
}
