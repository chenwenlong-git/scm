package com.hete.supply.scm.server.scm.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/5/16 17:34
 */
@Data
@NoArgsConstructor
public class SupplierCodeListDto {

    @ApiModelProperty(value = "供应商代码批量")
    @NotEmpty(message = "供应商代码不能为空")
    private List<String> supplierCodeList;

}
