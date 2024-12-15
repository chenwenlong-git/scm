package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2024/11/27 14:08
 */
@Data
@NoArgsConstructor
public class PurchaseChildSkuRawExportVo {

    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "采购单状态")
    private PurchaseOrderStatus purchaseOrderStatus;

    @ApiModelProperty(value = "采购单状态名称")
    private String purchaseOrderStatusName;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "采购价")
    private BigDecimal purchasePrice;

    @ApiModelProperty(value = "原料sku1")
    private String materialSku1;

    @ApiModelProperty(value = "数量1")
    private Integer deliveryCnt1;

    @ApiModelProperty(value = "原料sku2")
    private String materialSku2;

    @ApiModelProperty(value = "数量2")
    private Integer deliveryCnt2;

    @ApiModelProperty(value = "原料sku3")
    private String materialSku3;

    @ApiModelProperty(value = "数量3")
    private Integer deliveryCnt3;

    @ApiModelProperty(value = "原料sku4")
    private String materialSku4;

    @ApiModelProperty(value = "数量4")
    private Integer deliveryCnt4;

    @ApiModelProperty(value = "原料sku5")
    private String materialSku5;

    @ApiModelProperty(value = "数量5")
    private Integer deliveryCnt5;

}
