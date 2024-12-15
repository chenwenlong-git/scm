package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2024/5/14 11:52
 */
@Data
@NoArgsConstructor
public class RecoOrderItemSkuIdDto {

    @ApiModelProperty(value = "主键id")
    @NotNull(message = "ID不能为空")
    private Long financeRecoOrderItemSkuId;

}
