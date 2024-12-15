package com.hete.supply.scm.server.scm.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * sku原料
 *
 * @author yanjiawei
 * @date 2023/07/25 09:39
 */
@Data
@NoArgsConstructor
@ApiModel(value = "sku原料", description = "sku原料")
public class SkuMaterialVo {

    @ApiModelProperty(value = "sku")
    private String sku;
    @ApiModelProperty(value = "单件用量")
    private Integer singleNum;
}