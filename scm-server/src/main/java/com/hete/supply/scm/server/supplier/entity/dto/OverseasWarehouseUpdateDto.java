package com.hete.supply.scm.server.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2023/2/14 16:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OverseasWarehouseUpdateDto extends OverseasWarehouseMsgDto {
    @ApiModelProperty(value = "id")
    @NotNull(message = "id不能为空")
    private Long overseasWarehouseMsgId;

    @ApiModelProperty(value = "version")
    @NotNull(message = "version不能为空")
    private Integer version;
}
