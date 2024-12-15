package com.hete.supply.scm.server.scm.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


/**
 * @author ChenWenLong
 * @date 2024/8/7 16:50
 */
@Data
@NoArgsConstructor
public class ProduceDataItemGetSuggestSupplierBo {

    @ApiModelProperty(value = "sku")
    @NotBlank(message = "sku不能为空")
    private String sku;

    @ApiModelProperty(value = "供应商代码")
    @NotBlank(message = "供应商代码不能为空")
    private String supplierCode;

}
