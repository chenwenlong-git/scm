package com.hete.supply.scm.server.scm.supplier.entity.dto;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 供应商产品对照关系编辑
 *
 * @author ChenWenLong
 * @date 2024/9/24 16:11
 */
@Data
public class SupplierProductCompareItemDto {

    @ApiModelProperty(value = "供应商绑定ID")
    private Long supplierProductCompareId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotBlank(message = "供应商产品名称不能为空")
    @Length(max = 100, message = "供应商产品名称字符长度不能超过 100 位")
    @ApiModelProperty(value = "供应商产品名称")
    private String supplierProductName;

    @ApiModelProperty(value = "是否启用状态:启用(TRUE)、禁用(FALSE)")
    private BooleanType supplierProductCompareStatus;

}
