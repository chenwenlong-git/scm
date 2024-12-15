package com.hete.supply.scm.server.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:04
 */
@Data
@NoArgsConstructor
public class SkuBatchCodeQuickSearchVo {

    @ApiModelProperty(value = "sku批次码")
    private String conditionFieldName;

    @ApiModelProperty(value = "sku批次码")
    private String searchFieldName;

}
