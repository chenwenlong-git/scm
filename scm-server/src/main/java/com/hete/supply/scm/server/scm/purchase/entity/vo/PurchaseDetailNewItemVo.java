package com.hete.supply.scm.server.scm.purchase.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/7/13 21:13
 */
@Data
@NoArgsConstructor
public class PurchaseDetailNewItemVo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "需求数")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "已下单数")
    private Integer placedCnt;

    @ApiModelProperty(value = "采购可拆单数")
    private Integer canSplitCnt;

    @ApiModelProperty(value = "待发货数")
    private Integer waitDeliverCnt;

    @ApiModelProperty(value = "已发货数")
    private Integer deliveredCnt;

    @ApiModelProperty(value = "在途数")
    private Integer inTransitCnt;

    @ApiModelProperty(value = "正品数")
    private Integer qualityGoodsCnt;

    @ApiModelProperty(value = "次品数")
    private Integer defectiveGoodsCnt;

    @ApiModelProperty(value = "已入库数")
    private Integer warehousedCnt;

    @ApiModelProperty(value = "已退货数")
    private Integer returnCnt;

}
