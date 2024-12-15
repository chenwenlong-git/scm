package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2024/6/4 10:09
 */
@Data
@NoArgsConstructor
public class PrepaymentIdAndVersionDto {
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private Long financePrepaymentOrderId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;
}
