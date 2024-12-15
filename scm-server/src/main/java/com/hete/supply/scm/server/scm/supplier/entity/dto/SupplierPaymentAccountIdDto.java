package com.hete.supply.scm.server.scm.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2023/12/6 10:45
 */
@Data
@NoArgsConstructor
public class SupplierPaymentAccountIdDto {

    @ApiModelProperty(value = "id")
    @NotNull(message = "ID不能为空")
    private Long supplierPaymentAccountId;

}
