package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author yanjiawei
 * Created on 2024/8/6.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ExportSupplierRestDto extends ComPageDto {
    @ApiModelProperty(value = "供应商代码")
    @NotBlank(message = "供应商代码不能为空")
    private String supplierCode;
}
