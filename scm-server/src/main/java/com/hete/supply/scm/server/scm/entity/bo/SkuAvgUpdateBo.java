package com.hete.supply.scm.server.scm.entity.bo;

import com.hete.supply.scm.server.scm.enums.SkuAvgPriceBizType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2024/1/31 15:52
 */
@Data
@NoArgsConstructor
public class SkuAvgUpdateBo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "增加的累积数")
    private Integer addAccrueCnt;

    @ApiModelProperty(value = "增加的累积总价")
    private BigDecimal addAccruePrice;

    @ApiModelProperty(value = "sku均价业务类型")
    private SkuAvgPriceBizType skuAvgPriceBizType;
}
