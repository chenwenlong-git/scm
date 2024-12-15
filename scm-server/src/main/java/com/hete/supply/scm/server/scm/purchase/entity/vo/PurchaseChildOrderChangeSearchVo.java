package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseBizType;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2022/11/22 14:04
 */
@Data
@NoArgsConstructor
public class PurchaseChildOrderChangeSearchVo {

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "sku数量")
    private Integer skuCnt;

    @ApiModelProperty(value = "总结算金额")
    private BigDecimal totalSettlePrice;

    @ApiModelProperty(value = "入库时间")
    private LocalDateTime warehousingTime;

    @ApiModelProperty(value = "采购拆分类型")
    private PurchaseBizType purchaseBizType;

    @ApiModelProperty(value = "采购单状态")
    private PurchaseOrderStatus purchaseOrderStatus;

}
