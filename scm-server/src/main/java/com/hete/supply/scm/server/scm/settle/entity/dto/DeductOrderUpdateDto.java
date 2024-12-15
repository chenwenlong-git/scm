package com.hete.supply.scm.server.scm.settle.entity.dto;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:34
 */
@Data
@NoArgsConstructor
public class DeductOrderUpdateDto {
    @NotNull(message = "扣款单ID不能为空")
    @ApiModelProperty(value = "扣款单ID")
    private Long deductOrderId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;

    @NotNull(message = "审核状态不能为空")
    @ApiModelProperty(value = "审核状态")
    private BooleanType examine;

    @ApiModelProperty(value = "审核拒绝备注")
    private String refuseRemarks;


}
