package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.OrderSource;
import com.hete.supply.scm.api.scm.entity.enums.SkuType;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2024/8/20 10:08
 */
@Data
@NoArgsConstructor
public class PurchaseWmsCreateItemDto {

    @NotBlank(message = "需求平台不能为空")
    @ApiModelProperty(value = "需求平台")
    private String platform;

    @NotNull(message = "需求对象不能为空")
    @ApiModelProperty(value = "需求对象")
    private SkuType skuType;

    @NotBlank(message = "收货仓库编码不能为空")
    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @NotBlank(message = "收货仓库名称不能为空")
    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @NotBlank(message = "收货仓库标签不能为空")
    @ApiModelProperty(value = "收货仓库标签")
    private String warehouseTypes;

    @NotNull(message = "期望上架时间不能为空")
    @ApiModelProperty(value = "期望上架时间")
    private LocalDateTime expectedOnShelvesDate;

    @ApiModelProperty(value = "订单备注")
    private String orderRemarks;

    @NotBlank(message = "SPU不能为空")
    @ApiModelProperty(value = "SPU")
    private String spu;

    @NotBlank(message = "SKU不能为空")
    @ApiModelProperty(value = "SKU")
    private String sku;

    @NotNull(message = "下单数不能为空")
    @ApiModelProperty(value = "下单数")
    private Integer purchaseCnt;

    @NotBlank(message = "下单人不能为空")
    @ApiModelProperty(value = "下单人")
    private String placeOrderUser;

    @NotBlank(message = "下单人不能为空")
    @ApiModelProperty(value = "下单人")
    private String placeOrderUsername;

    @NotNull(message = "下单方式不能为空")
    @ApiModelProperty(value = "下单方式")
    private OrderSource orderSource;

    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotNull(message = "是否加急不能为空")
    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;

    @ApiModelProperty(value = "sku可售数量")
    private Integer canSaleAmount;

    @ApiModelProperty(value = "sku日销数量")
    private Integer daySaleAmount;

    @ApiModelProperty(value = "采购订单号（无需传参）")
    private String purchaseChildOrderNo;
}
