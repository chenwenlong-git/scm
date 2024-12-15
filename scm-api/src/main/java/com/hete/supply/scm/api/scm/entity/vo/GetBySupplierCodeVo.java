package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SupplierStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplierType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ChenWenLong
 * @date 2022/11/19 10:57
 */
@Data
public class GetBySupplierCodeVo {

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商类型")
    private SupplierType supplierType;

    @ApiModelProperty(value = "供应商状态：启用(ENABLED)、禁用(DISABLED)")
    private SupplierStatus supplierStatus;
}
