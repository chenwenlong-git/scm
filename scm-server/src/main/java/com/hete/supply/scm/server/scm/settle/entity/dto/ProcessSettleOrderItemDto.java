package com.hete.supply.scm.server.scm.settle.entity.dto;

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
public class ProcessSettleOrderItemDto {

    @NotNull(message = "加工结算单明细ID不能为空")
    @ApiModelProperty(value = "加工结算单明细ID")
    private Long processSettleOrderItemId;


}
