package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2023/10/23 15:00
 */
@Data
@NoArgsConstructor
public class ProduceDataIdDto {
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private Long produceDataId;
}
