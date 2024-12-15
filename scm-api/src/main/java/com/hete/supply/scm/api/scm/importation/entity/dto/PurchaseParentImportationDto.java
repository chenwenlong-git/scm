package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/12/19 22:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseParentImportationDto extends BaseImportationRowDto {
    @NotBlank(message = "需求平台不能为空")
    @ApiModelProperty(value = "需求平台")
    private String platform;

    @NotBlank(message = "需求对象不能为空")
    @ApiModelProperty(value = "需求对象")
    private String skuType;

    @NotBlank(message = "收货仓库编码不能为空")
    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @NotBlank(message = "期望上架时间不能为空")
    @ApiModelProperty(value = "期望上架时间")
    private String expectedOnShelvesDateStr;

    @ApiModelProperty(value = "期望上架时间")
    private LocalDateTime expectedOnShelvesDate;

    @ApiModelProperty(value = "订单备注")
    private String orderRemarks;

    @NotBlank(message = "SKU不能为空")
    @ApiModelProperty(value = "SKU")
    private String sku;

    @NotNull(message = "下单数不能为空")
    @ApiModelProperty(value = "下单数")
    private Integer purchaseCnt;

    @NotBlank(message = "采购需求类型不能为空")
    @ApiModelProperty(value = "采购需求类型(网红/常规)")
    private String purchaseDemandType;

}
