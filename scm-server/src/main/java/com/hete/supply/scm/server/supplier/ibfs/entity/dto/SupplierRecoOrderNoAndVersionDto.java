package com.hete.supply.scm.server.supplier.ibfs.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2024/5/14 11:52
 */
@Data
@NoArgsConstructor
public class SupplierRecoOrderNoAndVersionDto {

    @ApiModelProperty(value = "对账单号")
    @NotBlank(message = "对账单号不能为空")
    private String financeRecoOrderNo;

    @ApiModelProperty(value = "version")
    @NotNull(message = "version不能为空")
    private Integer version;
}
