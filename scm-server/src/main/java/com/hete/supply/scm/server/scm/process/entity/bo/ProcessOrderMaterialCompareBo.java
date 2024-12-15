package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2024/11/9.
 */
@Data
public class ProcessOrderMaterialCompareBo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;
}
