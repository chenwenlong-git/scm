package com.hete.supply.scm.server.scm.settle.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yanjiawei
 * Created on 2023/12/20.
 */
@Data
public class GetSettleOrderItemCompleteUserDto {

    @NotNull(message = "结算单id不能为空")
    @ApiModelProperty(value = "结算单id", example = "1")
    private Long processSettleOrderId;
}
