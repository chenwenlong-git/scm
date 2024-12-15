package com.hete.supply.scm.server.scm.develop.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/3 15:33
 */
@Data
@NoArgsConstructor
public class DevelopSupplierPriceVo {

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商样品报价")
    private List<BigDecimal> supplierSamplePriceList;

    @ApiModelProperty(value = "样品核价")
    private List<BigDecimal> pricingSamplePriceList;


}
