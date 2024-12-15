package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/3/19 15:09
 */
@Data
@NoArgsConstructor
public class PurchaseFastSupplyDto {
    @NotBlank(message = "采购子单单号不能为空")
    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @NotBlank(message = "收货仓库编码不能为空")
    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @NotBlank(message = "收货仓库名称不能为空")
    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @NotEmpty(message = "收货仓库标签不能为空")
    @ApiModelProperty(value = "收货仓库标签")
    private List<String> warehouseTypeList;

    @NotNull(message = "采购数不能为空")
    @ApiModelProperty(value = "采购数")
    private Integer purchaseCnt;

    @NotNull(message = "结算金额不能为空")
    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;


}
