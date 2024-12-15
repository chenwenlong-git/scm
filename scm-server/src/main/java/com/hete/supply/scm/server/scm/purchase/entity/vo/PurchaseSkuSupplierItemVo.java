package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.OrderSource;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierUrgentStatus;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2023/7/18 18:20
 */
@Data
@NoArgsConstructor
public class PurchaseSkuSupplierItemVo {
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "紧急状态")
    private SupplierUrgentStatus supplierUrgentStatus;

    @ApiModelProperty(value = "要求发货时间")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;


    @ApiModelProperty(value = "下单方式")
    private OrderSource orderSource;
}
