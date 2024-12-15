package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SupplierGrade;
import com.hete.supply.scm.api.scm.entity.enums.SupplierStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 供应商产能规则导出
 *
 * @author yanjiawei
 * Created on 2024/8/10.
 */
@Data
@NoArgsConstructor
public class SupplierCapacityRuleExportVo {
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商别称")
    private String supplierAlias;

    @ApiModelProperty(value = "供应商状态")
    private String supplierStatus;

    @ApiModelProperty(value = "供应商等级")
    private String supplierGrade;

    @ApiModelProperty(value = "常规日产能")
    private String normalCapacity;

    @ApiModelProperty(value = "30天剩余产能")
    private String restCap30AvailCap;

    @ApiModelProperty(value = "60天剩余产能")
    private String restCap60AvailCap;

    @ApiModelProperty(value = "90天剩余产能")
    private String restCap90AvailCap;
}
