package com.hete.supply.scm.server.scm.ibfs.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.scm.api.scm.entity.enums.SupplierGrade;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/5/21 11:36
 */
@Data
@NoArgsConstructor
public class SupplierVo {
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @JsonIgnore
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商等级")
    private SupplierGrade supplierGrade;
}
