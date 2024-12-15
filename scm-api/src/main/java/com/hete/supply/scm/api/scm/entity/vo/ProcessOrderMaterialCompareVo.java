package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2024/11/9.
 */
@Data
public class ProcessOrderMaterialCompareVo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;
}
