package com.hete.supply.scm.server.scm.supplier.entity.vo;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2023/3/28 14:45
 */
@Data
@NoArgsConstructor
public class SupplierProductCompareVo {

    @ApiModelProperty(value = "ID")
    private Long supplierProductCompareId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商产品名称")
    private String supplierProductName;

    @ApiModelProperty(value = "是否启用状态:启用(TRUE)、禁用(FALSE)")
    private BooleanType supplierProductCompareStatus;

}
