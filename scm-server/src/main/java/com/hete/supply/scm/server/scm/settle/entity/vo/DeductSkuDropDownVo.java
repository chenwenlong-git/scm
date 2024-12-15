package com.hete.supply.scm.server.scm.settle.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:04
 */
@Data
@NoArgsConstructor
public class DeductSkuDropDownVo {

    @ApiModelProperty(value = "SKU")
    private String sku;

}
