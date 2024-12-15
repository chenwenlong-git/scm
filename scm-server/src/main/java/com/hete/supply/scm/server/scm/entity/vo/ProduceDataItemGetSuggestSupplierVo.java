package com.hete.supply.scm.server.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author ChenWenLong
 * @date 2024/8/7 16:50
 */
@Data
@NoArgsConstructor
public class ProduceDataItemGetSuggestSupplierVo {

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

}
