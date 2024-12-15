package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseDemandType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/12/12 11:32
 */
@Data
@NoArgsConstructor
public class PurchaseChildNewExportVo {
    @ApiModelProperty(value = "需求单号")
    private String purchaseParentOrderNo;

    @ApiModelProperty(value = "需求单状态")
    private String purchaseParentOrderStatusStr;

    @ApiModelProperty(value = "需求平台")
    private String platform;

    @ApiModelProperty(value = "采购订单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "采购订单状态")
    private String purchaseOrderStatusStr;

    @ApiModelProperty(value = "订单类型")
    private String purchaseOrderType;

    @ApiModelProperty(value = "采购类型")
    private String purchaseBizType;

    @ApiModelProperty("sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "下单数")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "采购未交数")
    private Integer undeliveredCnt;

    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;

    @ApiModelProperty(value = "入库数")
    private Integer warehousingCnt;

    @ApiModelProperty(value = "正品数量")
    private Integer qualityGoodsCnt;

    @ApiModelProperty(value = "次品数量")
    private Integer defectiveGoodsCnt;

    @ApiModelProperty(value = "已退货数")
    private Integer returnCnt;

    @ApiModelProperty(value = "期望上架时间")
    private String expectedOnShelvesDateStr;

    @ApiModelProperty(value = "要求发货时间")
    private String deliverDateStr;

    @ApiModelProperty(value = "是否超期")
    private String isOverdue;

    @ApiModelProperty(value = "是否加急")
    private String isUrgentOrder;

    @ApiModelProperty(value = "下单时间")
    private String placeOrderTime;

    @ApiModelProperty(value = "采购需求类型")
    private PurchaseDemandType purchaseDemandType;

    @ApiModelProperty(value = "采购需求类型")
    private String purchaseDemandTypeStr;
}
