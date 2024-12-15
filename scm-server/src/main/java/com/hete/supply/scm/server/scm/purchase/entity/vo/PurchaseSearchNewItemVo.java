package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderType;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2023/7/13 15:10
 */
@Data
@NoArgsConstructor
public class PurchaseSearchNewItemVo {
    @ApiModelProperty(value = "采购订单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty("sku")
    private String sku;

    @ApiModelProperty(value = "采购单状态")
    private PurchaseOrderStatus purchaseOrderStatus;

    @ApiModelProperty(value = "下单数")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;

    @ApiModelProperty(value = "入库数")
    private Integer qualityGoodsCnt;

    @ApiModelProperty(value = "期望上架时间")
    private LocalDateTime expectedOnShelvesDate;

    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;

    @ApiModelProperty(value = "是否首单")
    private PurchaseOrderType purchaseOrderType;

    @ApiModelProperty(value = "是否超期")
    private BooleanType isOverdue;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime placeOrderTime;
}
