package com.hete.supply.scm.server.supplier.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/10/14 10:21
 */
@Data
@NoArgsConstructor
public class PurchaseReturnRepairDto {


    @NotBlank(message = "平台不能为空")
    @ApiModelProperty(value = "平台")
    private String platform;

    @NotBlank(message = "收货仓库编码不能为空")
    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @NotEmpty(message = "采购明细不能为空")
    @Valid
    @ApiModelProperty(value = "采购明细")
    private List<PurchaseReturnRepairItemDto> purchaseReturnRepairItemList;
}
