package com.hete.supply.scm.server.scm.purchase.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseBizType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2023/3/6 18:32
 */
@Data
@NoArgsConstructor
public class PurchaseChildOrderExportBo {

    @ApiModelProperty(value = "id")
    private String purchaseChildOrderId;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "总结算金额")
    private BigDecimal totalSettlePrice;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;

    @ApiModelProperty(value = "正品数")
    private Integer qualityGoodsCnt;

    @ApiModelProperty(value = "次品数")
    private Integer defectiveGoodsCnt;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "拆分类型")
    private PurchaseBizType purchaseBizType;


}
