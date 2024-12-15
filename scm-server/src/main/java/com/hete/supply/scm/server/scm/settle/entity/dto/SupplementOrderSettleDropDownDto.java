package com.hete.supply.scm.server.scm.settle.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author ChenWenLong
 * @date 2023/1/31 10:19
 */
@Data
@NoArgsConstructor
public class SupplementOrderSettleDropDownDto {

    @NotBlank(message = "补款单号不能为空")
    @ApiModelProperty(value = "补款单号")
    private String supplementOrderNo;

    @ApiModelProperty(value = "结算单号")
    private String settleOrderNo;

}
