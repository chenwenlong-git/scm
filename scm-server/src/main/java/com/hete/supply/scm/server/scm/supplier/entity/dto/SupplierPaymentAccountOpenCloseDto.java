package com.hete.supply.scm.server.scm.supplier.entity.dto;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2023/12/6 10:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SupplierPaymentAccountOpenCloseDto extends SupplierPaymentAccountIdAndVersionDto {

    @ApiModelProperty(value = "是否开启关闭")
    @NotNull(message = "是否开启关闭不能为空")
    private BooleanType isOpenClose;

}
