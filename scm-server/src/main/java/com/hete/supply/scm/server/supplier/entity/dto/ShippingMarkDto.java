package com.hete.supply.scm.server.supplier.entity.dto;

import com.hete.supply.scm.server.supplier.enums.ShippingMarkBizType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/2/14 00:52
 */
@Data
@NoArgsConstructor
public class ShippingMarkDto {
    @NotBlank(message = "收货仓库编码不能为空")
    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @NotBlank(message = "收货仓库名称不能为空")
    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @NotEmpty(message = "收货仓库标签不能为空")
    @ApiModelProperty(value = "收货仓库标签")
    private List<String> warehouseTypeList;

    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotBlank(message = "供应商名称不能为空")
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @NotNull(message = "箱数不能为空")
    @ApiModelProperty(value = "箱数")
    private Integer boxCnt;

    @NotNull(message = "总发货数不能为空")
    @ApiModelProperty(value = "总发货数")
    private Integer totalDeliver;

    @NotNull(message = "箱唛业务类型不能为空")
    @ApiModelProperty(value = "箱唛业务类型")
    private ShippingMarkBizType shippingMarkBizType;

    @NotEmpty(message = "箱唛明细不能为空")
    @Valid
    @ApiModelProperty(value = "箱唛明细")
    private List<ShippingMarkItemDto> shippingMarkItemList;


}
