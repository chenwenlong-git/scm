package com.hete.supply.scm.server.scm.supplier.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 供应商产能规则实体
 *
 * @author yanjiawei
 * Created on 2024/8/13.
 */
@Data
@NoArgsConstructor
public class SupCapRuleBo {
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "产能")
    private BigDecimal capacity;
}
