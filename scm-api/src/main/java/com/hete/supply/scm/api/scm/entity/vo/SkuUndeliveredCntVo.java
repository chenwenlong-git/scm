package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/4/23 11:48
 */
@Data
@NoArgsConstructor
public class SkuUndeliveredCntVo {
    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "采购未交数")
    private Integer undeliveredCnt;
}
